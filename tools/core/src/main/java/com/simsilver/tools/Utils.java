package com.simsilver.tools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 */
public class Utils {
    private static String md5BaseString = "0123456789abcdef";
    private static String dataArchiveName = "data.zip";
    private static ZipFile mZipArchive = null;


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

    public static String getPath(String path) {
        ClassLoader cl = Utils.class.getClassLoader();
        String GotPath = null;
        if (cl != null) {
            URL url = cl.getResource(path);
            if (url != null) {
                GotPath = url.getPath();
            }
        } else {
            URL url = Utils.class.getResource(path);
            if (url != null) {
                GotPath = url.getPath();
            }
        }
        return GotPath;
    }

    public static ZipFile getDataArchive() {
        if (mZipArchive != null) {
            return mZipArchive;
        } else {
            File dataArchiveFile = new File(getPath(dataArchiveName));
            if (dataArchiveFile.exists()) {
                try {
                    mZipArchive = new ZipFile(dataArchiveFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return mZipArchive;
        }
    }

    public static void closeDataArchive() {
        if (mZipArchive != null) {
            try {
                mZipArchive.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                mZipArchive = null;
            }
        }
    }

    public static InputStream getDataFileStream(String fileName) {
        try {
            String path = Utils.getPath("/");
            File dFile = new File(path, fileName);
            if (dFile.exists()) {
                return new FileInputStream(dFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            ZipFile file = getDataArchive();
            ZipEntry targetFile = file.getEntry(fileName);
            return file.getInputStream(targetFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getMD5(final String s) {
        try {
            return getMD5(s.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    public static byte[] readInputStream(InputStream in, int totalSize) throws IOException {

        byte[] cache = new byte[totalSize];
        int size;
        if ((size = in.read(cache)) > 0) {
            totalSize -= size;
            if (totalSize != 0) {
                throw new IOException("read stream size unexpected");
            }
        }
        return cache;
    }

    public static byte[] readInputStream(InputStream in) throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] cache = new byte[4096];
        int size;
        while ((size = in.read(cache)) > 0) {
            byteArrayOutputStream.write(cache, 0, size);
        }
        byte[] data = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        return data;
    }

    public static String getStackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }

    /*
     * "001,234,1234,568"
     */
    public static boolean findId(String id, String list) {
        Pattern p = Pattern.compile("\\b"+ id + "\\b");
        Matcher m = p.matcher(list);
        return m.find();
    }
}
