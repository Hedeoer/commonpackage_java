package entity;

import lombok.Builder;
import lombok.Data;

import java.util.Objects;

/**
 * 端口信息实体类
 */
@Data
@Builder
public class PortInfo {
    private String agentId;          // agent节点的唯一标识
    private String protocol;         // 协议
    private Integer portNumber;          // 端口号
    private String processName;      // 进程名
    private Integer processId;           // 进程ID
    private String commandLine;      // 完整命令行
    private String listenAddress;    // 监听地址
    private String family;           // 监听的地址类型(ipv4,ipv6)

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        // 不比较父类字段，因为父类包含时间字段
        PortInfo portInfo = (PortInfo) o;
        return Objects.equals(portNumber, portInfo.portNumber) &&
                Objects.equals(processId, portInfo.processId) &&
                Objects.equals(agentId, portInfo.agentId) &&
                Objects.equals(protocol, portInfo.protocol) &&
                Objects.equals(processName, portInfo.processName) &&
                Objects.equals(commandLine, portInfo.commandLine) &&
                Objects.equals(listenAddress, portInfo.listenAddress) &&
                Objects.equals(family, portInfo.family);
    }

    @Override
    public int hashCode() {
        // 不包含父类的hashCode，因为父类包含时间字段
        return Objects.hash(agentId, protocol, portNumber, processName, processId, commandLine, listenAddress,family);
    }
}