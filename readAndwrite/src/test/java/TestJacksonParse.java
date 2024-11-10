import cn.hedeoer.bean.Measurement;
import cn.hedeoer.bean.MetaColumn;
import cn.hedeoer.bean.ResponseParseResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class TestJacksonParse {
    public static void main(String[] args) {
        String data = "{ \"meta\": [ { \"name\": \"city\", \"type\": \"String\" }, { \"name\": \"temperature\", \"type\": \"Float64\" } ], \"data\": [ { \"city\": \"Abha\", \"temperature\": 27.1 }, { \"city\": \"Abha\", \"temperature\": 16.9 }, { \"city\": \"Abha\", \"temperature\": 25.39 }, { \"city\": \"Abha\", \"temperature\": 18.62 }, { \"city\": \"Abha\", \"temperature\": -1.04 }, { \"city\": \"Abha\", \"temperature\": 23.01 }, { \"city\": \"Abha\", \"temperature\": 2.82 }, { \"city\": \"Abha\", \"temperature\": 9.44 }, { \"city\": \"Abha\", \"temperature\": 21.93 }, { \"city\": \"Abha\", \"temperature\": 29.62 } ], \"rows\": 10, \"rows_before_limit_at_least\": 10, \"statistics\": { \"elapsed\": 0.004552388, \"rows_read\": 20, \"bytes_read\": 420 } }";

        ResponseParseResult mappingBean = getMappingBean(data);
        System.out.println(mappingBean);
    }


    private static ResponseParseResult getMappingBean(String data) {
        // todo 查验目标表是否已经构建

        // todo 解析数据
        ResponseParseResult responseParseResult = null;
        try {
            responseParseResult = new ObjectMapper()
                    .readValue(data, ResponseParseResult.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return responseParseResult;
    }
}
