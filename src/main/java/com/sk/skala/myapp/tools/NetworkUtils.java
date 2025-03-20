package com.sk.skala.myapp.tools;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetworkUtils {

    public static String getLocalIPAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return "127.0.0.1";
        }
    }

    public static String getLocalHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "Unknown";
        }
    }

    public static boolean isReachable(String host, int timeout) {
        try {
            return InetAddress.getByName(host).isReachable(timeout);
        } catch (Exception e) {
            return false;
        }
    }
}
