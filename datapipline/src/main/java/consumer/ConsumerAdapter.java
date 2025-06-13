package consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeaders;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ConsumerAdapter {

    /**
     * 从节点处理命令并发送响应 (Agent -> Master):
     * 消费命令:
     * 每个从节点 agent_X 启动一个 Kafka Consumer。
     * 关键点： 为了让 agent_X 只接收到发给自己的命令，你有两种主要方式：
     * 方式(简单但不高效): 每个 Agent 属于一个独立的 Consumer Group (例如，group_id 设置为 agent_X_group)，并订阅 master_to_agent_commands Topic 的所有分区。Agent 在收到消息后，
     *                   检查消息内容（或消息头）中的目标 agent_id 是否是自己。如果是，则处理；否则，忽略。这种方式会导致 Agent 拉取很多不属于自己的消息，造成网络和处理资源的浪费。
     * 处理命令: 从节点执行命令。
     * 发送响应:
     * Topic: agent_to_master_responses (或者从命令消息中获取的 reply_to_topic)。
     * Message Content: 响应消息中必须包含从原始命令中获取的 correlation_id。同时包含执行结果、状态等。
     * Message Key (可选): 可以使用 master_node_id (如果主节点也有多个实例) 或 correlation_id 作为 Key，但这对于响应来说通常不是强必需的，除非主节点希望按特定顺序处理来自不同 Agent 的响应。
     */
    public static void agentToMasterResponse() {
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", "hadoop102:9092");
        props.setProperty("group.id", "test");
        props.setProperty("enable.auto.commit", "false");
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        // 订阅的主题
        consumer.subscribe(Arrays.asList("master_to_agent_commands"));

        while (true) {
            // 不断拉取订阅的主题
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records) {
                // 处理消息
                ObjectMapper mapper = new ObjectMapper();
                RecordHeaders headers = (RecordHeaders)record.headers();
                // 获取 correlationId
                Header correlationIdHeader = headers.lastHeader("correlationId");
                String correlationId = new String(correlationIdHeader.value(), StandardCharsets.UTF_8);
                // 获取 replyToTopic
                Header replyToTopicHeader = headers.lastHeader("replyToTopic");
                String replyToTopic = new String(replyToTopicHeader.value(), StandardCharsets.UTF_8);
                String key = record.key();
                String value = record.value();
                JsonNode valueRoot = null;
                try {
                    valueRoot = mapper.readTree(value);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                String command = valueRoot.get("command").asText();

                if (command.equals("EXECUTE_TASK")) {
                    // 将处理的结果写入到另外topic（replyToTopic）
                    System.out.println("写入成功！！");
                }
            }
            // 手动提交
            consumer.commitSync();

        }

    }

    public static void main(String[] args) {

        agentToMasterResponse();
    }
}
