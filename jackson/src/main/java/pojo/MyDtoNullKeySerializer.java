package pojo;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class MyDtoNullKeySerializer extends StdSerializer<Object> {
    public MyDtoNullKeySerializer() {
        this(null);
    }

    public MyDtoNullKeySerializer(Class<Object> t) {
        super(t);
    }

    /**
     * 将null值的key转换为""
     * @param nullKey Value to serialize; can <b>not</b> be null.
     * @param jsonGenerator Generator used to output resulting Json content
     * @param unused Provider that can be used to get serializers for
     *   serializing Objects value contains, if any.
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Override
    public void serialize(Object nullKey, JsonGenerator jsonGenerator, SerializerProvider unused)
      throws IOException, JsonProcessingException {
        jsonGenerator.writeFieldName("");
    }
}