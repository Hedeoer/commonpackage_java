package cdc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import entity.SocialPicture;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.AllWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import oss.aliyun.ServerUpload;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 *  方案1：该类使用flink的 kafka connector读取kafka模拟流式数据的不断捕获，然后上次阿里云 oss
 */
public class FlinkOssUploader {
    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        KafkaSource<String> source = KafkaSource.<String>builder()
                .setBootstrapServers("hadoop103:9092")
                // 主题或者分区的订阅，可以同时订阅多个主题，也可以只订阅一个主题的多个分区
                .setTopics("topic_intelligence_risk_detail")
                // 动态的分区发现，可以在主题下的分区增加时，自动发现
                .setProperty("partition.discovery.interval.ms", "10000")
                // 指定一个消费者组
                .setGroupId("GP_1")
                // 消费的方式
                .setStartingOffsets(OffsetsInitializer.earliest())
                // 反序列化的方式
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();

        env.fromSource(source, WatermarkStrategy.noWatermarks(), "Kafka Source")
                .filter(value -> value != null && !value.isEmpty())
                // map转化
                .map(new SocialPictureMap())
                // 基于系统处理时间的5秒窗口
                .windowAll(TumblingProcessingTimeWindows.of(Time.of(5, TimeUnit.SECONDS)))
                // 窗口函数
                .apply(new AllWindowFunction<SocialPicture,List<SocialPicture>, TimeWindow>() {
                    @Override
                    public void apply(TimeWindow window, Iterable<SocialPicture> values, Collector<List<SocialPicture>> out) throws Exception {
                        List<SocialPicture> originalPictures = StreamSupport.stream(values.spliterator(), false)
                                .collect(Collectors.toList());
                        // 对图片上传oss
                        List<SocialPicture> failUploadList = ServerUpload.uploadOss((ArrayList<SocialPicture>) originalPictures);
                        out.collect(originalPictures);
                    }
                })
                                .print();

        env.execute();

    }

    private static class SocialPictureMap extends RichMapFunction<String, SocialPicture> {

        private transient ObjectMapper objectMapper;

        @Override
        public void open(Configuration parameters) throws Exception {
            super.open(parameters);
            // Jackson 实例应该在 Rich Function 的 open() 方法中初始化。
            this.objectMapper = new ObjectMapper();
            objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        }

        @Override
        public SocialPicture map(String value)  {

            SocialPicture socialPicture = SocialPicture.builder().build();
            if (value != null && !value.isEmpty()) {
                try {
                    socialPicture= objectMapper.readValue(value, SocialPicture.class);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
            return socialPicture;
        }
    }

}
