package com.learnbind.ai.ccb.sdk;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

/**
 * CCB软件工程师发送的开发包.
 * 此包暂时未用,已经开发相同的功能的包.
 * @author lenovo
 *
 */
public class CCBMD5withRSA {
	public static final String CHARSET = "UTF-8";
	/**
	 * 数字签名
	 */
	public static byte [] signMethod(String str,PrivateKey key) throws Exception{
		//初始化 MD5withRSA
		Signature signature = Signature.getInstance("MD5withRSA");
		//使用私钥
		signature.initSign(key);
		//需要签名或校验的数据
		signature.update(str.getBytes(CHARSET));
		//进行数字签名
		return signature.sign();
	}
	
	/**
	 * 数字签名重载方法
	 */
	public static byte [] signMethod(String str,PrivateKey key,String charset) throws Exception{
		//初始化 MD5withRSA
		Signature signature = Signature.getInstance("MD5withRSA");
		//使用私钥
		signature.initSign(key);
		//需要签名或校验的数据
		signature.update(str.getBytes(charset));
		//进行数字签名
		return signature.sign();
	}
	/**
	 * 数字校验
	 */
	public static boolean verifyMethod(String str,byte[] sign,PublicKey key) throws Exception{
		Signature signature = Signature.getInstance("MD5withRSA");
		signature.initVerify(key);
		signature.update(str.getBytes(CHARSET));
		return signature.verify(sign);
	}
	
	//md5加密
	public static String encryptionMd5(String plainText) throws Exception {
		String re_md5 = new String();
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(plainText.getBytes("GBK"));
		byte b[] = md.digest();
		int i;
		StringBuffer buf = new StringBuffer("");
		for (int offset = 0; offset < b.length; offset++) {
			i = b[offset];
			if (i < 0) i += 256;
			if (i < 16) buf.append("0");
			buf.append(Integer.toHexString(i));
		}
		re_md5 = buf.toString().toLowerCase();
		return re_md5;
	}
}
