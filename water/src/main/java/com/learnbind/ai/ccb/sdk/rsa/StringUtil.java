package com.learnbind.ai.ccb.sdk.rsa;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class StringUtil {
	/**
     * 将byte[] 转换成字符串
     */
    public static String bytes2hex(byte[] srcBytes) {
        StringBuilder hexRetSB = new StringBuilder();
        for (byte b : srcBytes) {
            String hexString = Integer.toHexString(0x00ff & b);
            hexRetSB.append(hexString.length() == 1 ? 0 : "").append(hexString);
        }
        return hexRetSB.toString();
    }
    
    /**
     * byte[]--->String
     * @param textByte
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String Base64Encrypt(byte[] textByte) throws UnsupportedEncodingException {
    	Base64.Encoder encoder = Base64.getEncoder();    	
    	//byte[] textByte = content.getBytes("UTF-8");
    	//new String(decoder.decode(encodedText), "UTF-8")  //解密后生成的字符串采用UTF-8进行编码	
    	
    	String encodedText = encoder.encodeToString(textByte); 	//编码
    	return encodedText;
    }
    
    /**
     * 解码  base64字符串--->byte[]
     * @param source
     * @return
     */
    public static byte[] Base64Decrypt(String source) {
    	Base64.Decoder decoder = Base64.getDecoder();
    	byte[] result=decoder.decode(source);    	
    	return result;
    }
    
    /**
     * 将16进制字符串转为转换成字符串
     */
    public static byte[] hex2Byte(String source) {
        byte[] sourceBytes = new byte[source.length() / 2];
        for (int i = 0; i < sourceBytes.length; i++) {
            sourceBytes[i] = (byte) Integer.parseInt(source.substring(i * 2, i * 2 + 2), 16);
        }
        return sourceBytes;
    }
    
}
