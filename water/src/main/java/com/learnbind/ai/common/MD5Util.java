package com.learnbind.ai.common;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MD5Util {
    protected static char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    protected static MessageDigest messagedigest = null;

    private static final Logger logger = LoggerFactory.getLogger(MD5Util.class);


    static {
        try {
            messagedigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage(), e);
        }
    }


    public static String getFileMD5String(File file)
            throws IOException {
        byte[] byteBuffer = FileUtils.readFileToByteArray(file);
        messagedigest.update(byteBuffer);
        return bufferToHex(messagedigest.digest());
    }


    public static String getMD5String(String s) {
        return getMD5String(s.getBytes());
    }


    private static String getMD5String(byte[] bytes) {
        messagedigest.update(bytes);
        return bufferToHex(messagedigest.digest());
    }


    private static String bufferToHex(byte[] bytes) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte[] bytes, int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = hexDigits[((bt & 0xF0) >> 4)];
        char c1 = hexDigits[(bt & 0xF)];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }


    public static boolean checkPassword(String password, String md5PwdStr) {
        String s = getMD5String(password);
        return s.equals(md5PwdStr);
    }

    public static void main(String[] args) {
        try {
            long begin = System.currentTimeMillis();

            File big = new File("F:\\oygd\\workspace\\zchl-dev\\src\\main\\webapp\\padTemplate\\f5863483.xml");
            File two = new File("F:\\w_work\\C03\\Doc\\01.客户提供物\\1.2.规格\\pad\\NewFile.xml");
            String md51 = getFileMD5String(big);
            String md52 = getFileMD5String(two);

            long end = System.currentTimeMillis();
            System.out.println("md51:" + md51);
            System.out.println("md52:" + md52);
            System.out.println("md51==md52 :" + md51.equals(md52));
            System.out.println("time:" + (end - begin) / 1000L + "s");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
