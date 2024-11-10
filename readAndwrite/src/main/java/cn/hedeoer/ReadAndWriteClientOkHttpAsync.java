package cn.hedeoer;

import cn.hedeoer.common.SinkToFile;
import cn.hedeoer.common.SinkToX;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 1. 写出方式需要封装，不一定是写出为文件 done
 * 2. TOTAL_RECORDS未知，需要提前查询 done
 * 3. todo 缺乏数据校验（数据总量，数据单条抽样校验）
 * 使用前注意替换自己的数据源和选择合适写出目的地（SinkToX接口的实现类），如果没有实现（目前实现了写入到本地文件，mysql），请自行编码实现
 */
public class ReadAndWriteClientOkHttpAsync {
    // ClickHouse server URL and query parameters
    private static final String CLICKHOUSE_SERVER_URL = "http://hadoop102:8123/";
    private static final String DATABASE_NAME = "default";
    private static final String TABLE_NAME = "measurements";
    private static final int LIMIT = 100000;  // Number of rows per request
    private static final int MAX_REQUESTS = 10;  // Maximum number of concurrent requests
    private static final int MAX_REQUESTS_PER_HOST = 10;  // Maximum concurrent requests per host

    public static void main(String[] args) throws InterruptedException {
        // Create a custom Dispatcher with a controlled number of maximum requests
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(MAX_REQUESTS);  // Set the global maximum number of concurrent requests
        dispatcher.setMaxRequestsPerHost(MAX_REQUESTS_PER_HOST);  // Set the maximum number of concurrent requests per host

        // Create an instance of OkHttpClient with the custom Dispatcher
        OkHttpClient client = new OkHttpClient.Builder()
                .dispatcher(dispatcher)
                .connectTimeout(60, TimeUnit.SECONDS)   // 连接超时
                .readTimeout(60, TimeUnit.SECONDS)      // 读取超时
                .writeTimeout(60, TimeUnit.SECONDS)     // 写入超时
                .build();

        // 获取数据总量

        Long totalRecordsNumber = getTotalRecordsNumber();
//        Long totalRecordsNumber = 1000000L;

        // CountDownLatch to wait for all asynchronous tasks to complete
        // 需要考虑精度损失问题
        CountDownLatch latch = new CountDownLatch((int) processDouble(totalRecordsNumber / LIMIT));


        // Loop over the total number of records in chunks
        for (int offset = 0; offset < totalRecordsNumber; offset += LIMIT) {
            int currentOffset = offset;

            String url = getRequestUrl(currentOffset);

            // Create the HTTP GET request
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            // Enqueue the request for asynchronous execution
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    // Handle the failure (e.g., log the error)
                    System.err.println("Request failed for offset: " + currentOffset);
                    e.printStackTrace();
                    latch.countDown();  // Ensure the latch is decremented even on failure
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                        // Get the response body as a string
                        String responseBody = response.body().string();

                        // Log the result to the console with the current offset
                        System.out.println("Fetched offset: " + currentOffset + ", response size: " + responseBody.length());
                        // 数据写出到文件实现
                        SinkToX sinkToX = new SinkToFile();
                        sinkToX.sink(responseBody,currentOffset);
                    } finally {
                        // Ensure the response is closed to avoid resource leaks
                        response.close();
                        latch.countDown();  // Decrement the latch when the request is complete
                    }
                }
            });
        }

        // Wait for all asynchronous tasks to complete
        latch.await();
        System.out.println("All requests completed.");
    }

    /**
     *获取需要处理的表的总数据量
     * @return  表的总数据量
     */
    private static Long getTotalRecordsNumber() {

        Long totalRecordsNumber = null;

        // Create an instance of OkHttpClient with the custom Dispatcher
        OkHttpClient client = new OkHttpClient.Builder()
                .build();
        // 请求表的的总数的http
        // Encode the query for URL
        String url = CLICKHOUSE_SERVER_URL + "?query=" + encodeQuery("select count(1) from " + DATABASE_NAME + "." + TABLE_NAME + ";" );
        System.out.println("请求连接为：" + url);

        // Create the HTTP GET request
        Request request = new Request.Builder()
                .url(url)
                .build();


        try (Response response = client.newCall(request).execute()) {
            ResponseBody body = response.body();
            assert body != null;
            totalRecordsNumber = Long.parseLong(body.string().trim());
        } catch (IOException e) {
            // Handle the failure (e.g., log the error)
            System.err.println("Request failed for request table's total numbers)");
            e.printStackTrace();
        }

        return totalRecordsNumber;
    }


    /**
     * 请求Url
     * @param currentOffset
     * @return
     */
    @NotNull
    private static String getRequestUrl(int currentOffset) {
        // Construct the query for the current offset
        String query = String.format("SELECT * FROM measurements LIMIT %d OFFSET %d FORMAT JSON", LIMIT, currentOffset);
        // Encode the query for URL
        String url = CLICKHOUSE_SERVER_URL + "?enable_http_compression=1&query=" + encodeQuery(query);
        return url;
    }

    /**
     * Utility method to URL-encode the query to ensure valid HTTP requests
     * @param query
     * @return
     */
    private static String encodeQuery(String query)  {
        try {
            return URLEncoder.encode(query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 如果有小数部分，则向上取整。
     * 如果没有小数部分，则取整数部分（即不变）
     * @param num
     * @return
     */
    public static long processDouble(double num) {
        // 检查是否有小数部分
        if (num % 1 != 0) {
            // 如果有小数部分，则向上取整
            return (long) Math.ceil(num);
        } else {
            // 如果没有小数部分，则取整数部分
            return (long) num;
        }
    }
}