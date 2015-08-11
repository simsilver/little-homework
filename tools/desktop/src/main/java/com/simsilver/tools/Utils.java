package com.simsilver.tools;

import com.sun.nio.zipfs.ZipFileSystem;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.rmi.server.ExportException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
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
        if(cl != null) {
            URL url = cl.getResource(path);
            if(url != null) {
                return url.getPath();
            }
        }
        return Utils.class.getResource(path).getPath();
    }

    public static ZipFile getDataArchive() {
        if (mZipArchive != null) {
            return mZipArchive;
        } else {
            File dataArchiveFile = new File(getPath("/"), dataArchiveName);
            if (!dataArchiveFile.exists()) {
                dataArchiveFile = new File(getPath("/" + dataArchiveName));
            }
            if(dataArchiveFile.exists()) {
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
        if(mZipArchive != null) {
            try {
                mZipArchive.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                mZipArchive = null;
            }
        }
    }
    public static InputStream getZipFileStream(String fileName) {
        try {
            ZipFile file = getDataArchive();
            ZipEntry targetFile = file.getEntry(fileName);
            return  file.getInputStream(targetFile);
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
}
