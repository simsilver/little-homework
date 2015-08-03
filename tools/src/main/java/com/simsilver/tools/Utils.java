package com.simsilver.tools;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 */
public class Utils {
    private static String md5BaseString = "0123456789abcdef";

    public static String getMD5(final byte[] bytes) {
        try {
            final MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] md5bytes = md5.digest(bytes);
            final StringBuilder sb = new StringBuilder();
            for (byte b : md5bytes) {
                int ch = (b >> 4) & 0xF;
                int cl = b & 0xf;
                sb.append(md5BaseString.charAt(ch));
                sb.append(md5BaseString.charAt(cl));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static String getMD5(final String s) {
        try {
            return getMD5(s.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }
}
