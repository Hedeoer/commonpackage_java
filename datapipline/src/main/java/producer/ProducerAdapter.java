package producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeaders;
import utils.KafkaClientAdminUtil;

import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 发送消息到指定的 Kafka topic
 */
public class ProducerAdapter {


    public static void masterToAgentCommand() {


        Properties props = new Properties();
        props.put("bootstrap.servers", "hadoop102:9092");
        props.put("linger.ms", 1);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<>(props);
        String topic = "master_to_agent_commands";
        // 检查topic是否存在？
        if (!KafkaClientAdminUtil.topicExists(topic)) {
            return;
        }
        Integer partition = null; // 需要计算
        String key = "agent01";  // 从节点机器的唯一标识
        String value = "{\"command\":\"EXECUTE_TASK\",\"timestamp\":1634567890123,\"priority\":\"HIGH\",\"parameters\":{\"taskId\":\"task-a1b2c3d4\",\"executionTimeout\":3600,\"retryCount\":3,\"args\":[\"--verbose\",\"--force\"]}}"; // json消息体
        String commandId = UUID.randomUUID().toString();
        String replyToTopic = "agent_to_master_responses";
        Headers headers = new RecordHeaders()
                .add("correlationId", commandId.getBytes())
                .add("replyToTopic", replyToTopic.getBytes());
        Future<RecordMetadata> sent = producer.send(new ProducerRecord<>(topic, partition, null, key, value, headers));
        try {
            // 同步发送（阻塞等待）
            sent.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        // 客户端的消息缓存刷出
        producer.flush();
        System.out.println("Message sent to Kafka topic: " + topic );

    }

    public static void main(String[] args) {
        masterToAgentCommand();
    }
}
