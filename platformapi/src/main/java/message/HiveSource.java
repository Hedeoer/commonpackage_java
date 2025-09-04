package message;

import cn.idev.excel.FastExcel;
import entity.Measurements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;

/**
 * 功能：从Hive中查询数据，生成Excel临时文件，并发送到飞书群聊中，并通知指定群用户
 * 需要根据具体的表结构添加，改动对应实体类，飞书授权码等
 */
public class HiveSource {

    private static final Logger logger = LoggerFactory.getLogger(HiveSource.class);

    public static void main(String[] args) {
//        Class.forName("org.apache.hive.jdbc.HiveDriver");
        try(
            Connection con = DriverManager.getConnection("jdbc:hive2://hadoop102:10000/mydb", "atguigu", "");
            Statement stmt = con.createStatement()
            ) {

            logger.info("Connected successfully: " + con);
            String tableName = "mydb.measurements_partitions";
            String sql = "select city,\n" +
                    "       measurement,\n" +
                    "       city_hash\n" +
                    "from "+tableName+"\n" +
                    "where city_hash = -1022324546 and measurement > 20 " +
                    "limit 10";
            ResultSet res = stmt.executeQuery(sql);

            ArrayList<Measurements> measurements = new ArrayList<>();
            while (res.next()) {
                measurements.add(Measurements.builder()
                                .city(res.getString(1) )
                                .measurement(res.getDouble(2))
                                .cityHash(res.getInt(3))
                        .build());
            }

            // 使用fastexcel转化为excel文件
            String fileName = "气温监测" + System.currentTimeMillis() + ".xlsx";

            FastExcel.write(fileName, Measurements.class)
                    .sheet("Sheet1")
                    .doWrite(measurements);

            // 读取excel文件并上传到飞书云文档
            File file = new File(fileName);
            double sizeMB = file.length() / 1024.0;
            logger.info("Excel file created successfully: {} Size: {} MB", fileName, sizeMB);
            PermanentFileMessage.sendPermanentFileMessage(fileName);
            // 删除本地文件
            if (file.delete()) {
                logger.info("Local file deleted successfully: {}", fileName);
            } else {
                logger.warn("Failed to delete local file: {}", fileName);
            }


        } catch (ClassNotFoundException e) {
            logger.error("Hive JDBC driver not found: {}", e.getMessage(), e);
            System.exit(1);
        } catch (SQLException e) {
            logger.error("Failed to connect to Hive: {}", e.getMessage(), e);
            System.exit(1);
        } catch (Exception e) {
            logger.error("Unexpected error occurred: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
