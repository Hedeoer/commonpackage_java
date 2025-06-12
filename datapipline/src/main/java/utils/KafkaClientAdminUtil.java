package utils;


import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.apache.kafka.common.errors.UnknownTopicOrPartitionException;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class KafkaClientAdminUtil {
    public static boolean topicExists(String topicName) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "hadoop102:9092");
        try (AdminClient admin = AdminClient.create(props)) {
            DescribeTopicsResult result = admin.describeTopics(Collections.singleton(topicName));
            result.topicNameValues().get(topicName).get(); // 阻塞查询，会抛出异常
            return true; // 无异常说明Topic存在
        } catch (ExecutionException e) {
            if (e.getCause() instanceof UnknownTopicOrPartitionException) {
                return false;
            }
            throw new RuntimeException("查询Topic失败", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("查询被中断", e);
        }
    }
}
