package com.learnbind.ai.common;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AESUtil {
    private static final Logger log = LoggerFactory.getLogger(AESUtil.class);

    private static byte[] Encrypt(String sSrc, String raw) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");

        secureRandom.setSeed(raw.getBytes("UTF-8"));

        kgen.init(128, secureRandom);
        SecretKey secretKey = kgen.generateKey();
        byte[] encodeFormat = secretKey.getEncoded();

        SecretKeySpec key = new SecretKeySpec(encodeFormat, "AES");

        Cipher cipher = Cipher.getInstance("AES");

        byte[] byteContent = sSrc.getBytes("utf-8");
        cipher.init(1, key);
        byte[] result = cipher.doFinal(byteContent);
        return result;
    }

    public static String Encrypt(String sSrc)
            throws Exception {
        if ((sSrc == null) || (sSrc.equals(""))) {
            return sSrc;
        }
        char[] key = {'þ', 'î', 'ã', '4', '/', '', 'â', '¾', 'Á', 'Ô', 'ß', 'Ã', ' ', 'x', '', '\022'};

        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < key.length; i++) {
            int temp = key[i];
            buffer.append(temp);
        }
        return parseByte2HexStr(Encrypt(sSrc, buffer.toString()));
    }


    private static byte[] Decrypt(byte[] sSrc, String raw)
            throws Exception {
        byte[] result = null;
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");

            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");

            secureRandom.setSeed(raw.getBytes("UTF-8"));

            kgen.init(128, secureRandom);
            SecretKey secretKey = kgen.generateKey();
            byte[] encodeFormat = secretKey.getEncoded();

            SecretKeySpec key = new SecretKeySpec(encodeFormat, "AES");

            Cipher cipher = Cipher.getInstance("AES");

            cipher.init(2, key);
            return cipher.doFinal(sSrc);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }


    public static String Decrypt(String sSrc)
            throws Exception {
        if ((sSrc == null) || (sSrc.equals(""))) {
            return sSrc;
        }
        char[] key = {'þ', 'î', 'ã', '4', '/', '', 'â', '¾', 'Á', 'Ô', 'ß', 'Ã', ' ', 'x', '', '\022'};

        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < key.length; i++) {
            int temp = key[i];
            buffer.append(temp);
        }

        return new String(Decrypt(parseHexStr2Byte(sSrc), buffer.toString()));
    }


    public static String parseByte2HexStr(byte[] buf) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }


    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }

        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = ((byte) (high * 16 + low));
        }

        return result;
    }
}
