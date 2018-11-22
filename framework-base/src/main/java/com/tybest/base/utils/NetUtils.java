//package com.tybest.base.utils;
//
//import lombok.extern.slf4j.Slf4j;
//
//import java.net.Inet4Address;
//import java.net.InetAddress;
//import java.net.NetworkInterface;
//import java.util.Enumeration;
//
///**
// * @author tb
// * @date 2018/11/14 15:33
// */
//@Slf4j
//public class NetUtils {
//
//    public static void main(String[] args) {
//        String ip = getInternetIp();
//        System.out.println(ip);
//        long iplong = ipV4ToLong(ip);
//        System.out.println(iplong);
//        System.out.println(longToIpV4(iplong));
//        System.out.println(getWorkId(ip,32));
//    }
//
//
//    public static String longToIpV4(long longIp) {
//        int octet3 = (int) ((longIp >> 24) % 256);
//        int octet2 = (int) ((longIp >> 16) % 256);
//        int octet1 = (int) ((longIp >> 8) % 256);
//        int octet0 = (int) ((longIp) % 256);
//        return octet3 + "." + octet2 + "." + octet1 + "." + octet0;
//    }
//
//    public static long ipV4ToLong(String ip) {
//        String[] octets = ip.split("\\.");
//        return (Long.parseLong(octets[0]) << 24) + (Integer.parseInt(octets[1]) << 16)
//                + (Integer.parseInt(octets[2]) << 8) + Integer.parseInt(octets[3]);
//    }
//
//    public static long getWorkId(String ip, int bits) {
//        int shift = 64 - bits;
//        long iplong = ipV4ToLong(ip);
//        return (iplong << shift) >>> shift;
//    }
//
//    public static String getHostName()
//    {
//        try {
//            String hostname = InetAddress.getLocalHost().getHostName();
//            if ("localhost".equals(hostname) || "localhost.localdomain".equals(hostname)
//                    || "localhost4".equals(hostname) || "localhost4.localdomain4".equals(hostname)
//                    || "localhost6".equals(hostname) || "localhost6.localdomain6".equals(hostname))
//            {
//                return null;
//            }
//            return hostname;
//        } catch (Exception e)
//        {
//            return null;
//        }
//    }
//
//    public static byte[] getInetAdress() {
//        final String regex = "^172.20.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
//        byte[] ip = null;
//        Enumeration<NetworkInterface> netInterfaces;
//        try {
//            netInterfaces = NetworkInterface.getNetworkInterfaces();
//            while (netInterfaces.hasMoreElements()) {
//                NetworkInterface ni = netInterfaces
//                        .nextElement();
//                Enumeration<InetAddress> ips = ni.getInetAddresses();
//                while (ips.hasMoreElements()) {
//                    InetAddress currentAddr = ips.nextElement();
//                    if (currentAddr instanceof Inet4Address) {
//                        String address = currentAddr.getHostAddress();
//                        if (address.matches(regex)) {
//                            String[] add = address.split("\\.");
//                            byte[] re = { Integer.valueOf(add[0]).byteValue() ,
//                                    Integer.valueOf(add[1]).byteValue(),
//                                    Integer.valueOf(add[2]).byteValue(),
//                                    Integer.valueOf(add[3]).byteValue() };
//                            return re;
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return ip;
//    }
//
//    /**
//     * 获得内网IP
//     * @return 内网IP
//     */
//    public static String getIntranetIp(){
//        try{
//            return InetAddress.getLocalHost().getHostAddress();
//        } catch(Exception e){
//            throw new RuntimeException(e);
//        }
//    }
//
//    /**
//     * 获得外网IP
//     * @return 外网IP
//     */
//    public static String getInternetIp(){
//        try{
//            Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
//            InetAddress ip;
//            Enumeration<InetAddress> addrs;
//            while (networks.hasMoreElements())
//            {
//                addrs = networks.nextElement().getInetAddresses();
//                while (addrs.hasMoreElements())
//                {
//                    ip = addrs.nextElement();
//                    if (ip instanceof Inet4Address
//                            && ip.isSiteLocalAddress()
//                            && !ip.getHostAddress().equals(getIntranetIp()))
//                    {
//                        return ip.getHostAddress();
//                    }
//                }
//            }
//            return getIntranetIp();
//        } catch(Exception e){
//            throw new RuntimeException(e);
//        }
//    }
//}
