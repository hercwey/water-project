package com.learnbind.ai.ccb.base64;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * BASE64编码/解码工具类
 * @author lenovo
 *
 */
public class Base64Util {

	public static byte[] encode(byte[] encryptData) {
		if (encryptData == null) {
			return null;
		}

		byte[] encodeBase64 = null;
		encodeBase64=Base64.getEncoder().encode(encryptData);
		
		return encodeBase64;

	}

	public static byte[] decode(byte[] decryptData) {
		if (null == decryptData) {
			return null;
		}
		byte[] decodeBase64 = null;
		decodeBase64 =Base64.getDecoder().decode(decryptData);
		
		return decodeBase64;
	}

	/**
	 * 编码(base64) String--->Base64 String(UTF-8编码)
	 * 
	 * @param content
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String encode2Base64Str(String content) throws UnsupportedEncodingException {
		Base64.Encoder encoder = Base64.getEncoder();
		byte[] textByte = content.getBytes("UTF-8");		
		String encodedText=new String(encoder.encode(textByte),"UTF-8");  //加密后生成的字符串采用UTF-8进行编码
		//String encodedText = encoder.encodeToString(textByte); // 编码
		return encodedText;
	}

	/**
	 * byte[]--->String(UTF-8编码)
	 * 
	 * @param textByte
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String encode2Base64Str(byte[] textByte) throws UnsupportedEncodingException {
		Base64.Encoder encoder = Base64.getEncoder();
		// byte[] textByte = content.getBytes("UTF-8");
		// new String(decoder.decode(encodedText), "UTF-8") 
		return new String(encoder.encode(textByte),"UTF-8"); //加密后生成的字符串采用UTF-8进行编码
	}

	/**
	 * 解码 base64字符串--->byte[]
	 * 
	 * @param base64Source
	 * @return
	 */
	public static byte[] decode2Byte(String base64Source) {
		Base64.Decoder decoder = Base64.getDecoder();
		byte[] result = decoder.decode(base64Source);
		return result;
	}

	/**
	 * byte[]--->byte[]
	 * 
	 * @param source
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static byte[] decode2Byte(byte[] source) throws UnsupportedEncodingException {
		Base64.Decoder decoder = Base64.getDecoder();
		byte[] result = decoder.decode(source);
		return result;
	}

	/**
	 * 解码 base64字符串--->String(utf-8编码)
	 * 
	 * @param base64Source
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String decode2Str(String base64Source) throws UnsupportedEncodingException {
		Base64.Decoder decoder = Base64.getDecoder();
		byte[] result = decoder.decode(base64Source);
		String resultStr = new String(result, "UTF-8");
		return resultStr;
	}

	/**
	 * 测试编码与解码
	 * 
	 * @throws UnsupportedEncodingException
	 */
	public static void testCodeDecode() throws UnsupportedEncodingException {
		final Base64.Decoder decoder = Base64.getDecoder();
		final Base64.Encoder encoder = Base64.getEncoder();
		final String text = "字串文字";
		final byte[] textByte = text.getBytes("UTF-8");
		// 编码
		final String encodedText = encoder.encodeToString(textByte);
		System.out.println(encodedText);
		// 解码
		System.out.println(new String(decoder.decode(encodedText), "UTF-8"));
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		String testStr = "BASE64编码解码";
		System.out.println("原始字符串为:" + testStr);

		String encodedStr = Base64Util.encode2Base64Str(testStr);
		System.out.println("加密字符串为:" + encodedStr);

		String decodedStr = Base64Util.decode2Str(encodedStr);
		System.out.println("解密后字符串为:" + decodedStr);

	}

}
