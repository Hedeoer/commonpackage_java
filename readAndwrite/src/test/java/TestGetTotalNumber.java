import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class TestGetTotalNumber {

    private static final String CLICKHOUSE_SERVER_URL = "http://hadoop102:8123/";
    private static final String DATABASE_NAME = "default";
    private static final String TABLE_NAME = "measurements";
    public static void main(String[] args) {

        System.out.println(getTotalRecordsNumber());
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

    private static String encodeQuery(String query)  {
        try {
            return URLEncoder.encode(query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
