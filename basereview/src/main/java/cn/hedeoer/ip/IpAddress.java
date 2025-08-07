package cn.hedeoer.ip;

import org.apache.commons.validator.routines.InetAddressValidator;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IpAddress {
    public static void main(String[] args) {

        String ipAddress1 = "192.168.1.1";
        String ipAddress2 = "256.256.256.256"; // Invalid IP address for testing
        String ipAddress3 = "www.baidu.com"; // Valid domain name for testing
        // 2401:ce20:1:12:0:0:0:0
        String ipAddress4 = "2401:ce20:1:12::";
//        System.out.println(isValidIpAddressByTools(ipAddress4));
        System.out.println(isValidIpAddressByTools(ipAddress3));
    }

    /**
     * Checks if the given string is a valid IP address.
     * 需要通过dns查询来验证IP地址的合法性
     * 如果输入的是域名，返回的IP地址是合法的，但是依旧不是IP地址
     * 如果为 2401:ce20:1:12::，返回2401:ce20:1:12:0:0:0:0
     * @param ipAddress 字符串
     * @return 如果是合法的IP地址，返回true，否则返回false
     */
    private static boolean isValidIpAddress(String ipAddress) {
        boolean result = false;
        if ("".equals(ipAddress) || ipAddress == null) {
            return result;
        }
        try {
            InetAddress inetAddress = InetAddress.getByName(ipAddress);
            // Check if the resolved address matches the input string
            // 比如输入的是域名，返回的IP地址是合法的，但是依旧不是IP地址
            result = inetAddress.getHostAddress().equals(ipAddress);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    /**
     *通过工具库commons-validator 来验证IP地址的合法性
     * @param ipAddress 字符串
     * @return 如果是合法的IP地址，返回true，否则返回false
     */
    private static boolean isValidIpAddressByTools(String ipAddress) {
        boolean result = false;
        if ("".equals(ipAddress) || ipAddress == null) {
            return result;
        }

        InetAddressValidator inetAddressValidator = new InetAddressValidator();
        return inetAddressValidator.isValid(ipAddress);

    }
}
