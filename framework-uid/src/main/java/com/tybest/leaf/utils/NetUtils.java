package com.tybest.leaf.utils;

import com.tybest.leaf.exception.InternalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.*;
import java.util.Enumeration;

/**
 * @author tb
 * @date 2018/11/22 14:18
 */
@Slf4j
public final class NetUtils {

    private static final String LOCALHOST = "localhost";
    private static final String UNKNOWN = "unknown";


    public static void main(String[] args) {
        System.out.println(getHost());
        System.out.println(getIntranetIP());
        System.out.println(getInternetIp());
        System.out.println(ipV4ToLong(getInternetIp()));
        System.out.println(longToIpV4(ipV4ToLong(getInternetIp())));
        getInternetAddress();
    }


    public static byte[] intToBytes(Integer value) {
        byte[] byteArray = new byte[Integer.SIZE / 8];
        for (int i = 0; i < byteArray.length; i++) {
            int off = (byteArray.length - 1 - i) * 8;
            byteArray[i] = (byte) ((value >> off) & 0xFF);
        }
        return byteArray;
    }

    public static int bytesToint(byte[] b) throws Exception {
        if (b.length != 4) {
            throw new RuntimeException("can not convert bytes to int for the bytes length is :" + b.length);
        }
        int b1 = ((int) b[0] & 0x000000FF) << 24;
        int b2 = ((int) b[1] & 0x000000FF) << 16;
        int b3 = ((int) b[2] & 0x000000FF) << 8;
        int b4 = ((int) b[3] & 0x000000FF);
        return b1 | b2 | b3 | b4;
    }


    public static byte[] longToBytes(Long value) {
        byte[] byteArray = new byte[Long.SIZE / 8];
        for (int i = 0; i < byteArray.length; i++) {
            int off = (byteArray.length - 1 - i) * 8;
            byteArray[i] = (byte) ((value >> off) & 0xFF);
        }
        return byteArray;
    }

    public static Long bytesToLong(byte[] b) {
        if (b.length != 8) {
            throw new RuntimeException("can not convert bytes to long for the bytes length is :" + b.length);
        }
        long b1 = ((long) b[0] & 0x00000000000000FF) << 56;
        long b2 = ((long) b[1] & 0x00000000000000FF) << 48;
        long b3 = ((long) b[2] & 0x00000000000000FF) << 40;
        long b4 = ((long) b[3] & 0x00000000000000FF) << 32;
        long b5 = ((long) b[4] & 0x00000000000000FF) << 24;
        long b6 = ((long) b[5] & 0x00000000000000FF) << 16;
        long b7 = ((long) b[6] & 0x00000000000000FF) << 8;
        long b8 = ((long) b[7] & 0x00000000000000FF);

        return b1 | b2 | b3 | b4 | b5 | b6 | b7 | b8;
    }

    public static String longToIpV4(long longIp) {
        int octet3 = (int) ((longIp >> 24) % 256);
        int octet2 = (int) ((longIp >> 16) % 256);
        int octet1 = (int) ((longIp >> 8) % 256);
        int octet0 = (int) ((longIp) % 256);
        return octet3 + "." + octet2 + "." + octet1 + "." + octet0;
    }

    public static long ipV4ToLong(String ip) {
        String[] octets = ip.split("\\.");
        return (Long.parseLong(octets[0]) << 24) + (Integer.parseInt(octets[1]) << 16)
                + (Integer.parseInt(octets[2]) << 8) + Integer.parseInt(octets[3]);
    }

    public static long getWorkId(String ip, int bits) {
        int shift = 64 - bits;
        long iplong = ipV4ToLong(ip);
        return (iplong << shift) >>> shift;
    }

    public static String getHost() {
        String hostname;
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            throw new InternalException(e);
        }
        if (LOCALHOST.equals(hostname) || "localhost.localdomain".equals(hostname)
                || "localhost4".equals(hostname) || "localhost4.localdomain4".equals(hostname)
                || "localhost6".equals(hostname) || "localhost6.localdomain6".equals(hostname)) {
            return null;
        }
        return hostname;
    }

    /**
     * 内网IP
     *
     * @return
     */
    public static String getIntranetIP() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new InternalException(e);
        }
    }

    /**
     * 外网IP
     *
     * @return
     */
    public static String getInternetIp() {
        Enumeration<NetworkInterface> networks;
        try {
            networks = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            throw new InternalException(e);
        }
        InetAddress ip;
        Enumeration<InetAddress> addrs;
        while (networks.hasMoreElements()) {
            addrs = networks.nextElement().getInetAddresses();
            while (addrs.hasMoreElements()) {
                ip = addrs.nextElement();
                if (ip instanceof Inet4Address
                        && ip.isSiteLocalAddress()
                        && !ip.getHostAddress().equals(getIntranetIP())) {
                    return ip.getHostAddress();
                }
            }
        }
        return getIntranetIP();
    }


    public static byte[] getInternetAddress() {
        final String regex = "^192.168.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
        Enumeration<NetworkInterface> netInterfaces;
        try {
            netInterfaces = NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = netInterfaces.nextElement();
                Enumeration<InetAddress> ips = ni.getInetAddresses();
                while (ips.hasMoreElements()) {
                    InetAddress currentAddr = ips.nextElement();
                    if (currentAddr instanceof Inet4Address) {
                        String address = currentAddr.getHostAddress();
                        if (address.matches(regex)) {
                            String[] add = address.split("\\.");
                            return new byte[]{Integer.valueOf(add[0]).byteValue(),
                                    Integer.valueOf(add[1]).byteValue(),
                                    Integer.valueOf(add[2]).byteValue(),
                                    Integer.valueOf(add[3]).byteValue()};
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new InternalException(e);
        }
        return null;
    }

    public static String getIpAddr(HttpServletRequest request) {
        String ip = null;
        try {
            ip = request.getHeader("x-forwarded-for");
            if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } catch (Exception e) {
            log.error("IPUtils ERROR ", e);
        }
        // 使用代理，则获取第一个IP地址
        if (StringUtils.isEmpty(ip) && ip.length() > 15) {
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        return ip;
    }
}
