package oss.aliyun;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.comm.Protocol;
import com.aliyun.oss.common.comm.SignVersion;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import entity.SocialPicture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ImageValidator;

import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

/**
 * 使用服务器上传的方式：
 * 通过链接hive库表，获取图片链接，并上传到阿里云对象存储中
 */
public class ServerUpload {

    private static final Logger logger = LoggerFactory.getLogger(ServerUpload.class);

    public static void main(String[] args) {
        /*
         * 1. 从Hive读取图片链接
         * 2. 上传到阿里云OSS
         * 3. 记录上传结果
         */
        try {

            ArrayList<SocialPicture> urlsToUpload = getImageUrlsFromHive();

//            ArrayList<SocialPicture> socialPictures = new ArrayList<>();
//            socialPictures.add(SocialPicture.builder()
//                            .originalUrl("https://img2024.cnblogs.com/blog/35695/202404/35695-20240418073152086-1370636262.jpg")
//                    .build());

            boolean success = upload(urlsToUpload);
            logger.info("上传结果：{}",success);
        } catch (Exception e) {
            logger.error("A database error occurred while fetching URLs from Hive.", e);
        }
    }

    /**
     * 从hive读取需要上传oss的图片外链列表
     * @return 图片外链列表
     * @throws SQLException
     */
    private static ArrayList<SocialPicture> getImageUrlsFromHive() throws SQLException {
        ArrayList<SocialPicture> urls = new ArrayList<>();
        /*
        CREATE TABLE IF NOT EXISTS social_picture (
        source          STRING,
        fetch_date_time STRING,
        original_url    STRING,
        oss_url         STRING
        )
        ROW FORMAT DELIMITED
        FIELDS TERMINATED BY ',' -- 假设你的数据文件是以逗号分隔的
        STORED AS parquet
        * */
        // 假设查询的第一列是需要的URL
        String sql = "select source, fetch_date_time,original_url from mydb.social_picture where pt = '20250101' and ht = '03'";
        try (Connection con = DriverManager.getConnection("jdbc:hive2://hadoop102:10000/mydb", "atguigu", "");
             Statement stmt = con.createStatement();
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
     *
     * 上传图片到 aliyun oss
     *
     * 后期需要改进的点：
     * 1. 并行上传
     * 2. 如果部分上传失败该如何处理已经上传成功的
     *
     * @param originalUrls 上传对象
     * @return 全部都上传成功才会返回true
     */
    private static Boolean upload(ArrayList<SocialPicture> originalUrls) {
        if (originalUrls == null || originalUrls.isEmpty()) {
            logger.info("No URLs to upload.");
            return true;
        }

        // 创建 ClientBuilderConfiguration 实例，用于配置 OSS 客户端参数
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        // 设置签名算法版本为 V4
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);
        // 设置使用 HTTPS 协议访问 OSS，保证传输安全性
        clientBuilderConfiguration.setProtocol(Protocol.HTTPS);

        OSS ossClient = null;
        try {
            String region = "cn-hangzhou";
            String endpoint = "oss-cn-hangzhou.aliyuncs.com";
            ossClient = OSSClientBuilder.create()
                    .endpoint(endpoint)
                    .credentialsProvider(CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider())
                    .clientConfiguration(clientBuilderConfiguration)
                    .region(region)
                    .build();

            boolean allSucceeded = true;
            String bucketName = "hedeoer";

            for (SocialPicture urlEntity : originalUrls) {
                String originalUrl = urlEntity.getOriginalUrl();
                try (InputStream inputStream = new URL(originalUrl).openStream()) {
                    if (!ImageValidator.isImageByHardcodedList(originalUrl)){
                        allSucceeded = false;
                        break;
                    }
                    // 使用UUID确保对象名称的唯一性
                    String fileName = UUID.randomUUID() + originalUrl.substring(originalUrl.lastIndexOf("."));
                    String objectName = "test/" + fileName;
                    PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, inputStream);
                    PutObjectResult putObjectResult = ossClient.putObject(putObjectRequest);
                    String accessUrl = "https://" + bucketName + "." + endpoint + "/" +  objectName;
                    // 更新oss链接地址
                    urlEntity.setOssUrl(accessUrl);
                    logger.info("Successfully uploaded from URL: {}", fileName);
                } catch (Exception e) {
                    logger.error("Failed to upload from URL");
                    allSucceeded = false;
                }
            }
            return allSucceeded;

        } catch (Exception e) {
            logger.error("A critical error occurred during the OSS operation: {}", e.getMessage(), e);
            return false;
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}
