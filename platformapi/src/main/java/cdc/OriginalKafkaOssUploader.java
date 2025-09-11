package cdc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import entity.SocialPicture;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Suppressed;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import oss.aliyun.ServerUpload;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 方案2：该类使用kafka stream 原生api - 消费者来模拟流式数据的不断捕获，然后上次阿里云 oss
 * 对比方案1（使用flink流式处理），kafka引入的依赖更少，但是对数据容错处理，灵活度均不如 flink
 * 更多关于 kafka stream的说明查看官方文档： https://kafka.apache.org/documentation/streams/
 */
public class OriginalKafkaOssUploader {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(OriginalKafkaOssUploader.class);
    static {
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    }
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "test-003");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "hadoop102:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        StreamsBuilder builder = new StreamsBuilder();

        // 序列化器
        Serde<List> listSerde = new JsonSerde<>(List.class);
        // 设置订阅主题
        builder.<String, String>stream("topic_intelligence_risk_detail")
                // 如果消息没有key，手动复制默认key
                .selectKey((key, value) -> key != null ? key : "default-key")
                // 过滤掉消息的value非json格式的
                .filter((key, value) -> isValidJson(value)) // 使用静态方法过滤
                // 按照分组
                .groupByKey()
                // 5 s的处理时间窗口
                .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofSeconds(5)))
                // 窗口内的聚合逻辑，将value聚合为一个list《string》
                .aggregate(
                        ArrayList<String>::new, // 初始化一个空的 ArrayList
                        (key, value, aggregateList) -> {
                            aggregateList.add(value); // 将JSON字符串直接添加到List
                            return aggregateList;
                        },
                        // 当流中的对象类型改变后，任何可能触发重分区（如groupByKey, groupBy）或状态存储（如aggregate, count）的操作，都需要你明确提供该对象的Serde
                        // 需要指定序列化器，由于value的值由 string变为了list<string>
                        Materialized.with(Serdes.String(), listSerde)
                )
                //抑制（或扣留）窗口聚合的中间结果，只在窗口关闭时才发出最终的、唯一的结果，实现攒批效果
                .suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded()))
                // 转化为 流
                .toStream()
                // 进行最终转换和上传，每次窗口关闭触发一次 foreach计算
                .foreach((windowedKey, jsonStringList) -> {
                    // 1. 将 List<String> 转换为 List<SocialPicture>
                    ArrayList<SocialPicture> picturesToUpload = new ArrayList<>();
                    for (Object o : jsonStringList) {
                        try {
                            picturesToUpload.add(objectMapper.readValue(o.toString(),SocialPicture.class));
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    if (picturesToUpload.isEmpty()) {
                        return;
                    }
                    // 2. 上传OSS
                    logger.info("窗口 {} 关闭，准备上传 {} 条图片链接", windowedKey.window(), picturesToUpload.size());
                    List<SocialPicture> failedUploads = ServerUpload.uploadOss(picturesToUpload);
                    logger.info("上传成功 {} 条图片链接", picturesToUpload.size() - failedUploads.size());
                });

        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();
    }

    // 将过滤逻辑提取为静态方法，更清晰
    private static boolean isValidJson(String value) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }
        try {
            objectMapper.readTree(value);
            return true;
        } catch (JsonProcessingException e) {
            logger.warn("发现非法的JSON消息: {}", value);
            return false;
        }
    }

    public static class JsonSerde<T> implements Serde<T> {
        private final ObjectMapper objectMapper = new ObjectMapper();
        private final Class<T> targetClass;
        public JsonSerde(Class<T> targetClass) {
            this.targetClass = targetClass;
        }
        @Override
        public Serializer<T> serializer() {
            return (topic, data) -> {
                try {
                    return objectMapper.writeValueAsBytes(data);
                } catch (Exception e) {
                    throw new SerializationException("Error serializing JSON message", e);
                }
            };
        }
        @Override
        public Deserializer<T> deserializer() {
            return (topic, data) -> {
                if (data == null) {
                    return null;
                }
                try {
                    return objectMapper.readValue(data, targetClass);
                } catch (IOException e) {
                    throw new SerializationException("Error deserializing JSON message", e);
                }
            };
        }
    }
}
