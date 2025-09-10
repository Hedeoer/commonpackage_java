package oss.aliyun;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.comm.Protocol;
import com.aliyun.oss.common.comm.SignVersion;
import com.aliyun.oss.model.PutObjectRequest;
import entity.SocialPicture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ConfigLoader;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * 使用服务器上传的方式：
 * 通过链接hive库表，获取图片链接，并上传到阿里云对象存储中
 */
public class ServerUpload {

    private static final Logger logger = LoggerFactory.getLogger(ServerUpload.class);


    public static List<SocialPicture> uploadOss(ArrayList<SocialPicture> list){
        /*
         * 1. 从Hive读取图片链接
         * 2. 上传到阿里云OSS
         * 3. 将OSS链接写回Hive
         */
        List<SocialPicture> failUploadPictures = Collections.emptyList();
        try(Connection hiveConnection = getHiveConnection()) {
            // 从hive读取源数据
//            ArrayList<SocialPicture> urlsToUpload = getImageUrlsFromHive(hiveConnection);
            // 构建oss客户端
            OSS ossClient = buildOssClient();
            // 将源数据中的图片链接上传oss，并更新ossUrl属性，表示图片oss已经上链
            failUploadPictures = upload(ossClient, list);
            // 将上链数据落入另一张hive表
            sinkToHiveTable(hiveConnection,list);
            logger.info("Upload process finished with result: {}", failUploadPictures.isEmpty());
            return  failUploadPictures;

        } catch (Exception e) {
            logger.error("An error occurred during the main process.", e);
        }
        return failUploadPictures;
    }

    /**
     * 从hive读取需要上传oss的图片外链列表
     *
     * @return 图片外链列表
     * @throws SQLException
     */
    private static ArrayList<SocialPicture> getImageUrlsFromHive(Connection connection) throws SQLException {
        ArrayList<SocialPicture> urls = new ArrayList<>();
        /*
        CREATE TABLE IF NOT EXISTS social_picture (
                                                                  source          STRING,
                                                                  fetch_date_time STRING,
                                                                  original_url    STRING,
                                                                  oss_url         STRING
        )
            PARTITIONED BY (
                pt STRING COMMENT '分区日期, 格式 yyyyMMdd',
                ht STRING COMMENT '分区小时, 格式 HH'
                )
            STORED AS PARQUET;
        * */
        // 假设查询的第一列是需要的URL
        String sql = "select source, fetch_date_time,original_url from mydb.social_picture where pt = '20250910' and ht = '10'";
        try (
             Statement stmt = connection.createStatement();
             ResultSet res = stmt.executeQuery(sql)) {

            logger.info("Successfully connected to Hive and executed query.");
            while (res.next()) {
                urls.add(SocialPicture.builder()
                        .source(res.getString(1))
                        .fetchDateTime(res.getString(2))
                        .originalUrl(res.getString(3))
                        .build());
            }
            logger.info("Retrieved {} URLs from Hive.", urls.size());
        }
        return urls;
    }

    /**
     * 将 contentType 做转化，用作上传oss的文件后缀，比如image/jpeg 转化为.jpg
     * @param contentType   contentType
     * @return
     */
    private static String getExtensionFromContentType(String contentType) {
        if (contentType == null || contentType.trim().isEmpty()) {
            return ".jpg"; // Default to .jpg if content type is missing
        }
        switch (contentType.toLowerCase()) {
            case "image/jpeg":
                return ".jpg";
            case "image/png":
                return ".png";
            case "image/gif":
                return ".gif";
            case "image/webp":
                return ".webp";
            case "image/bmp":
                return ".bmp";
            // Add more mappings as needed
            default:
                // For types like 'image/svg+xml', try to extract 'svg'
                String[] parts = contentType.split("/");
                if (parts.length > 1) {
                    String subtype = parts[1].split("[+;]")[0];
                    return "." + subtype;
                }
                return ".jpg"; // Fallback to .jpg
        }
    }

    /**
     * 上传图片列表到OSS
     * 1. 多线程上传
     * 2. 会记录上传失败的记录
     * @param originalUrls 包含原始图片URL的实体列表
     * @return 如果所有图片都成功上传或者没有需要上传的数据，则返回 空列表；如果出现上传任务失败，将记录失败的记录到列表中。
     */
    public static List<SocialPicture> upload(OSS ossClient,ArrayList<SocialPicture> originalUrls) {
        List<SocialPicture> failedUrlEntities = new ArrayList<>();
        if (originalUrls == null || originalUrls.isEmpty()) {
            logger.info("图片列表为空，无需上传。");
            return failedUrlEntities;
        }

        final String endpoint = ConfigLoader.getProperty("oss.endpoint");
        final String bucketName = ConfigLoader.getProperty("oss.bucket.name");

        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        try  {

            List<UploadTask> tasks = originalUrls.stream()
                    .map(urlEntity -> new UploadTask(urlEntity, executorService.submit(() ->
                            uploadSingleUrl(ossClient, urlEntity, bucketName, endpoint))))
                    .collect(Collectors.toList());

            // 使用 for 循环遍历所有任务,判断是否有上传失败的.
            for (UploadTask task : tasks) {
                try {
                    if (!task.getFuture().get()) { // 任务返回false，表示失败
                        failedUrlEntities.add(task.getUrlEntity());
                    }
                } catch (InterruptedException | ExecutionException e) { // 任务执行异常，也表示失败
                    logger.error("一个上传任务执行失败。URL: {}", task.getUrlEntity(), e);
                    failedUrlEntities.add(task.getUrlEntity());
                }
            }
            return failedUrlEntities;
        } catch (Exception e) {
            // 此处捕获 OSSClient 初始化或任务提交过程中的异常
            logger.error("OSS操作发生严重错误或资源初始化失败。", e);
            return failedUrlEntities;
        } finally {
            // 2. 使用 finally 块来确保 ExecutorService 被关闭
            executorService.shutdown();
            logger.info("线程池已关闭。");
        }
    }
    /**
     * 构建OSS客户端的辅助方法
     */
    private static OSS buildOssClient() throws ClientException, com.aliyuncs.exceptions.ClientException {
        final String region = ConfigLoader.getProperty("oss.region");
        final String endpoint = ConfigLoader.getProperty("oss.endpoint");
        ClientBuilderConfiguration config = new ClientBuilderConfiguration();
        config.setSignatureVersion(SignVersion.V4);
        config.setProtocol(Protocol.HTTPS);
        return OSSClientBuilder.create()
                .endpoint(endpoint)
                .credentialsProvider(CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider())
                .clientConfiguration(config)
                .region(region)
                .build();
    }
    /**
     * 处理并上传单个URL的辅助方法。
     *
     * @param ossClient  OSS客户端实例
     * @param urlEntity  要处理的图片实体
     * @param bucketName OSS Bucket名称
     * @param endpoint   OSS Endpoint
     * @return 上传是否成功
     */
    private static boolean uploadSingleUrl(OSS ossClient, SocialPicture urlEntity, String bucketName, String endpoint) {
        String originalUrl = urlEntity.getOriginalUrl();
        try {
            URL url = new URL(originalUrl);
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(10000); // 10秒连接超时
            connection.setReadTimeout(10000);    // 10秒读取超时
            String contentType = connection.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                logger.warn("URL内容不是图片，已跳过: {}. Content-Type: {}", originalUrl, contentType);
                return false; // 明确返回失败
            }
            // 使用 try-with-resources 自动关闭 InputStream
            try (InputStream inputStream = connection.getInputStream()) {
                String extension = getExtensionFromContentType(contentType);
                String fileName = UUID.randomUUID() + extension;
                String objectName = "test/" + fileName;
                ossClient.putObject(new PutObjectRequest(bucketName, objectName, inputStream));
                String accessUrl = "https://" + bucketName + "." + endpoint + "/" + objectName;
                urlEntity.setOssUrl(accessUrl); // 更新对象状态
                logger.info("成功从URL上传: {}, Content-Type: {}", originalUrl, contentType);
                return true;
            }
        } catch (Exception e) {
            // 捕获所有与单个URL处理相关的异常 (网络、IO、OSS上传等)
            logger.error("处理并上传URL时失败: {}", originalUrl, e);
            return false;
        }
    }
    /**
     * 将oss链接落入hive新表 social_picture_oss
     * @param connection hive链接
     * @param uploadedPictures 需要插入的数据
     */
    private static void sinkToHiveTable(Connection connection, ArrayList<SocialPicture> uploadedPictures) {
        if (uploadedPictures == null || uploadedPictures.isEmpty()) {
            logger.info("No pictures to update in Hive.");
            return;
        }

        // Note: This UPDATE approach assumes the Hive table and JDBC driver support batched updates.
        // For non-transactional Parquet tables, this might be inefficient or unsupported.
        // A more robust approach for large-scale updates would be using INSERT OVERWRITE.
        // 将数据插入到另一张表：social_picture_oss
        insertWithDynamicPartitioning(connection,uploadedPictures);
    }

    /**
     * 动态分区插入
     * @param connection hive链接
     * @param pictures 需要插入的数据
     * @throws SQLException
     */
    public static void insertWithDynamicPartitioning(Connection connection, List<SocialPicture> pictures) {

        try {

            // 一级分区
            Set<String> leve1Partitions = pictures.stream()
                    .map(picture -> picture.getFetchDateTime().replace("-", "").substring(0, 8))
                    .collect(Collectors.toSet());

            // 设置Hive动态分区参数
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("SET hive.exec.dynamic.partition=true");
                stmt.execute("SET hive.exec.dynamic.partition.mode=nonstrict");
                stmt.execute("SET hive.exec.max.dynamic.partitions=10000");
                stmt.execute("SET hive.exec.max.dynamic.partitions.pernode=1000");
            }

            // 使用动态分区插入
            String insertSql = "INSERT INTO TABLE social_picture_oss " +
                    "PARTITION (pt, ht) " +
                    "SELECT source, fetch_date_time, original_url, oss_url, " +
                    "substr(regexp_replace(fetch_date_time,\"-\",\"\"),1,8) as pt, " +
                    "substr(regexp_replace(fetch_date_time,\"-\",\"\"),10,2) as ht " +
                    "FROM (VALUES ";

            // 构建VALUES子句
            StringBuilder valuesClause = new StringBuilder();
            for (int i = 0; i < pictures.size(); i++) {
                SocialPicture picture = pictures.get(i);
                valuesClause.append(String.format("('%s', '%s', '%s', '%s')",
                        picture.getSource(),
                        picture.getFetchDateTime(),
                        picture.getOriginalUrl(),
                        picture.getOssUrl()));

                if (i < pictures.size() - 1) {
                    valuesClause.append(",");
                }
            }

            insertSql += valuesClause.toString() + ") as t(source, fetch_date_time, original_url, oss_url)";

            // 手动刷新已经存在的分区，使得数据可以被正常读取
            // REFRESH TABLE social_picture_oss PARTITION (pt = "20250910", pt = "20250911");
            StringBuilder buildRefresh = new StringBuilder();
            ArrayList<String> partitions = new ArrayList<>(leve1Partitions);
            int size = partitions.size();
            for (int index = 0; index < size; index++) {

                if (size == 1) {
                    buildRefresh.append("REFRESH TABLE social_picture_oss PARTITION (").append("pt = '").append(partitions.get(index)).append("')");
                    break;
                }

                if (index == 0){
                    buildRefresh.append("REFRESH TABLE social_picture_oss PARTITION (").append("pt = '").append(partitions.get(index)).append("',");
                    continue;
                }
                if (index == size -1){
                    buildRefresh.append("pt = '").append(partitions.get(index)).append("')");
                    continue;
                }
                buildRefresh.append("pt = '").append(partitions.get(index)).append("', ");
            }
            String refreshPartitionSql = buildRefresh.toString();

            // MSCK REPAIR 发现新分区
//            String msckSql = "MSCK REPAIR TABLE mydb.social_picture_oss";

            try (Statement stmt = connection.createStatement()) {
                // 动态分区插入
                stmt.execute(insertSql);
                // 发现新分区
//                stmt.execute(msckSql);
                // 加载受影响的所有分区数据使得新插入的数据可被读取
                stmt.execute(refreshPartitionSql);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 获取hive链接
     * @return
     */
    public static Connection getHiveConnection(){
        try {
            return DriverManager.getConnection(
                    ConfigLoader.getProperty("hive.jdbc.url"),
                    ConfigLoader.getProperty("hive.user"),
                    ConfigLoader.getProperty("hive.password"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private static final class UploadTask {
        private final SocialPicture urlEntity;
        private final Future<Boolean> future;

        public UploadTask(SocialPicture urlEntity, Future<Boolean> future) {
            this.urlEntity = urlEntity;
            this.future = future;
        }

        public SocialPicture getUrlEntity() {
            return urlEntity;
        }

        public Future<Boolean> getFuture() {
            return future;
        }
    }

}
