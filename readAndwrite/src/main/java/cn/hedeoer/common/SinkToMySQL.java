package cn.hedeoer.common;

import cn.hedeoer.bean.Measurement;
import cn.hedeoer.bean.MetaColumn;
import cn.hedeoer.bean.ResponseParseResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据写入到mysql
 * 1. 批量插入,默认1000条
 * 2. 写入数据表前会校验目标表是否已经构建，只是校验目标表是否存在，不会进行细节schema校验，比如字段类型，字段名；如果没有目表不会进行后续数据导入
 * 3. 数据写入具有事务性，写入失败会回滚，保证数据的完整性
 * 4. todo 抽样数据对比
 */
public class SinkToMySQL implements SinkToX{

    private static  String DriverName  ;
    private static  String DATABASE;
    private static  String TABLENAME;
    private static  String URL ;
    private static  String USER;
    private static  String PASSWORD;
    private static  Connection connection ;
    private static  Integer insertBatchSize ;

    static {
        DriverName = "com.mysql.jdbc.Driver";
        DATABASE = "Test";
        TABLENAME = "measurements";
        URL = "jdbc:mysql://hadoop102:3306/"+DATABASE+"?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf8";
        USER = "root";
        PASSWORD = "aaaaaa";
        connection = getConnection();
        insertBatchSize = 1000;

    }

    /**
     * 将数据源的一批数据写入到Mysql数据库
     * @param data 数据
     * @param currentOffset 在数据源的开始偏移量，可随意填写，只是记录将要插入的这批数据在原始数据源中的起始偏移量
     * @throws IOException
     */
    @Override
    public void sink(String data, int currentOffset)  {



        long start = System.currentTimeMillis();
        connection = getConnection();//获取数据库连接
        if(connection == null ) return;
        // todo 字段写死的，后期可以通过反射获取
//        String sql = "INSERT INTO " + DATABASE + "." + TABLENAME + " (city, temperature) VALUES (?, ?)";
        String insertSql = generateInsertSQL(DATABASE,TABLENAME);
        PreparedStatement ps = null;

        ResponseParseResult responseParseResult =  getMappingBean(MetaColumn.class, Measurement.class,ResponseParseResult.class,data);
        List<Measurement> measurements = responseParseResult.getMeasurements();

        // todo 查验目标表是否已经构建,只是从目标库中查询表有没有存在，不会进行schema校验；如果没有目表不会进行后续数据导入
        //SELECT COUNT(*)
        //FROM INFORMATION_SCHEMA.TABLES
        //WHERE TABLE_SCHEMA = 'Test'
        //AND TABLE_NAME = 'measurements';
        Boolean isExists = checkTableExists(DATABASE, TABLENAME);
        if(!isExists) {
            System.out.println("目标表不存在，数据导入失败");
            return;
        }

        try {
            ps = connection.prepareStatement(insertSql);
            connection.setAutoCommit(false);//取消自动提交
            for (int i = 1; i <= measurements.size(); i++) {
                Measurement measurement = measurements.get(i - 1);

                // 使用反射获取 JavaBean 的所有属性并设置值
                Field[] fields = measurement.getClass().getDeclaredFields();

                for (int index = 0; index < fields.length; index++) {
                    Field field = fields[index];
                    field.setAccessible(true);  // 如果字段是私有的，需要设置可访问性
                    ps.setObject(index + 1, field.get(measurement));  // 获取属性值并赋值给 PreparedStatement
                }


                ps.addBatch();
                if (i % insertBatchSize == 0) {
                    ps.executeBatch();
                    ps.clearBatch();
                }
            }
            ps.executeBatch();
            ps.clearBatch();
            connection.commit();//所有语句都执行完毕后才手动提交sql语句
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        //truncate table user;
        //select count(*) from user;
        System.out.println("数据插入用时：" + (System.currentTimeMillis() - start)  +"【单位：毫秒】");
    }

    /**
     * 生成插入语句
     * @param database 数据库
     * @param tableName 表名
     * @return
     */
    private static String generateInsertSQL(String database, String tableName) {

        List<String> columns = new ArrayList<>();
        ResultSet resultSet = null;

        // 获取表的列信息
        try  {
            // 获取数据库元数据对象
            DatabaseMetaData metaData = connection.getMetaData();

            resultSet = metaData.getColumns(database, null, tableName, null);
            while (resultSet.next()) {
                // 获取列名并添加到列表中
                String columnName = resultSet.getString("COLUMN_NAME");
                columns.add(columnName);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                assert resultSet != null;
                resultSet.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        // 构建字段名部分
        StringBuilder fieldPart = new StringBuilder();
        StringBuilder valuePart = new StringBuilder();

        for (int i = 0; i < columns.size(); i++) {
            fieldPart.append(columns.get(i));
            valuePart.append("?");

            // 如果不是最后一个字段，添加逗号
            if (i < columns.size() - 1) {
                fieldPart.append(", ");
                valuePart.append(", ");
            }
        }

        // 生成最终的 SQL 语句
        return  "INSERT INTO " + database + "." + tableName + " (" + fieldPart + ") VALUES (" + valuePart + ")";
    }

    private static Boolean checkTableExists(String database, String tableName)  {
        Boolean isExists = false;
        String sql = "SELECT COUNT(*)\n" +
                "FROM INFORMATION_SCHEMA.TABLES\n" +
                "WHERE TABLE_SCHEMA = '"+database+"'\n" +
                "AND TABLE_NAME = '"+tableName+"';";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                int value = resultSet.getInt(1);
                if(value == 1) isExists = true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return isExists;
    }

    /**
     * 将请求响应数据解析为java bean
     * @param metaColumnClass 表元数据bean类
     * @param measurementClass 表数据bean类
     * @param data 请求响应数据
     * @return ResponseParseResult
     */
    private static ResponseParseResult getMappingBean(Class<MetaColumn> metaColumnClass, Class<Measurement> measurementClass, Class<ResponseParseResult> responseParseResultClass,String data)  {


        // 解析数据
        ResponseParseResult responseParseResult = null;
        try {
            responseParseResult = responseParseResultClass.getConstructor().newInstance();
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }


        try {
            responseParseResult = new ObjectMapper()
                    .readValue(data, ResponseParseResult.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return responseParseResult;
    }


    /**
     * 获取数据库连接
     * @return
     */
    public static Connection getConnection() {
        try {
            Class.forName(DriverName);
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
//        System.out.println(getConnection());
//        System.out.println(checkTableExists(DATABASE,TABLENAME));
        System.out.println(generateInsertSQL(DATABASE,TABLENAME));
    }

}
