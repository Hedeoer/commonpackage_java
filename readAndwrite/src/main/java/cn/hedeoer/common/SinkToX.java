package cn.hedeoer.common;

import java.io.IOException;

/**
 * 数据写出实现
 */
public interface SinkToX {
    void sink(String data, int currentOffset) throws IOException;
}
