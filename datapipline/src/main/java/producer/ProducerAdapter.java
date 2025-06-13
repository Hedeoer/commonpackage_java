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


    /**
     * 发送命令 (Master -> Agent):
     * Topic: master_to_agent_commands
     * Partitioner: 主节点在发送命令时，使用目标 agent_id 作为消息的 Key。Kafka 的默认分区器会根据 Key 的哈希值将消息路由到特定的分区。
     * Correlation ID: 主节点为每个命令生成一个唯一的 correlation_id (例如 UUID)。这个 ID 需要包含在发送给从节点的消息中（可以放在消息体或消息头中）。
     * Reply-to Topic (可选但推荐): 主节点可以在命令消息中（消息体或消息头）指定期望接收响应的 Topic，即 agent_to_master_responses。
     * Master 内部状态: 主节点在发送命令后，需要记录这个 correlation_id 以及一个用于接收响应的机制（例如一个 CompletableFuture 或一个回调），并设置一个超时时间。
     */
    public static void masterToAgentCommand() {


        Properties props = new Properties();
        props.put("bootstrap.servers", "hadoop102:9092");
        props.put("linger.ms", 1);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("enable.idempotence", "true");
        props.put("acks", "all");
        props.put("min.insync.replicas",2);


        Producer<String, String> producer = new KafkaProducer<>(props);
        String topic = "master_to_agent_commands";
        // 检查topic是否存在？
        if (!KafkaClientAdminUtil.topicExists(topic)) {
            return;
        }
        Integer partition = null; // 不指定，kafka自动计算分区
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
