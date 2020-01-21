package com.learnbind.ai.common;

import java.io.UnsupportedEncodingException;

import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.crypto.CryptoException;


public class AES128Util {
    public static String Encrypt(String plaintext, String textKey, String charsetName)
            throws UnsupportedEncodingException {
        AesCipherService aesCipherService = new AesCipherService();
        aesCipherService.setKeySize(128);
        byte[] plaintBytes = plaintext.getBytes(charsetName == null ? "ISO8859-1" : charsetName);
        byte[] keyBytes = textKey.getBytes(charsetName == null ? "ISO8859-1" : charsetName);
        if (keyBytes.length * 8 != 128) {
            throw new RuntimeException("key must be 128 bit !");
        }
        return aesCipherService.encrypt(plaintBytes, keyBytes).toHex();
    }


    public static String Decrypt(String ciphertext, String textKey, String charsetName)
            throws CryptoException, UnsupportedEncodingException {
        AesCipherService aesCipherService = new AesCipherService();
        aesCipherService.setKeySize(128);
        byte[] cipherBytes = Hex.decode(ciphertext);
        byte[] keyBytes = textKey.getBytes(charsetName == null ? "ISO8859-1" : charsetName);
        if (keyBytes.length * 8 != 128) {
            throw new RuntimeException("key must be 128 bit !");
        }
        return new String(aesCipherService.decrypt(cipherBytes, keyBytes).getBytes(), charsetName);
    }

    public static void main(String[] args)
            throws CryptoException, UnsupportedEncodingException {
        //System.out.println(Decrypt("69ac8328810eda3aabb429026b841fa51e806009a5f5861492e1994fd9011e43", "0123456789ABCDEF", "utf-8"));
    	System.out.println(Encrypt("pivas", "0123456789ABCDEF", "utf-8"));
    }
}
