package cn.hedeoer.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 数据写出到文件实现
 */
public class SinkToFile implements SinkToX {
    @Override
    public void sink(String data, int currentOffset) throws IOException {
        // Save the response to a file (for example, one file per request)
        String fileName = "data_offset_" + currentOffset + ".json";
        Files.write(Paths.get(fileName), data.getBytes());
    }
}
