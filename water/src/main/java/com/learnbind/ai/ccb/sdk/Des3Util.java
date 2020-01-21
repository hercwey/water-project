package com.learnbind.ai.ccb.sdk;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.neuqsoft.pay.authorize.service.SystemParameterService;

public class Des3Util {
	
	private static final Logger logger = LoggerFactory.getLogger(Des3Util.class);
	
	
	
	/**
	 * @param src  明文
	 * @param key  密钥
	 * @param charSet 字符集(系统中约定采用UTF-8编码)
	 * @return  BASE64编码的密文
	 * @throws NoSuchAlgorithmException
	 * @throws GeneralSecurityException
	 * @throws UnsupportedEncodingException
	 * 
	 * 加密算法:3DES
	 */
	public static String encryptUseDES3(String src,String keyStr,String charSet) throws NoSuchAlgorithmException, GeneralSecurityException, UnsupportedEncodingException {
		byte[] key=Base64.decodeBase64(keyStr);	
		
		logger.debug("debug:加密时DES KEY的长度是:"+key.length);
		
		SecretKey secretKey = new SecretKeySpec(key, "DESede");//恢复密钥
		Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");//Cipher完成加密或解密工作类
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);//对Cipher初始化，解密模式
		byte[] cipherByte = cipher.doFinal(src.getBytes(charSet));//加密data
		
		return new String(Base64.encodeBase64(cipherByte),charSet);
	}
	
	/**
	 * @param src  明文
	 * @param key  密钥
	 * @param charSet 字符集(系统中约定采用UTF-8编码)
	 * @return  BASE64编码的密文
	 * @throws NoSuchAlgorithmException
	 * @throws GeneralSecurityException
	 * @throws UnsupportedEncodingException
	 * 
	 * 加密算法:3DES
	 */
	public static String encryptUseDES3(byte[] src,String keyStr,String charSet) throws NoSuchAlgorithmException, GeneralSecurityException, UnsupportedEncodingException {
		byte[] key=Base64.decodeBase64(keyStr);	
		
		logger.debug("debug:加密时DES KEY的长度是:"+key.length);
		
		SecretKey secretKey = new SecretKeySpec(key, "DESede");//恢复密钥
		Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");//Cipher完成加密或解密工作类
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);//对Cipher初始化，解密模式
		byte[] cipherByte = cipher.doFinal(src);//加密data
		
		return new String(Base64.encodeBase64(cipherByte),charSet);
	}
	
	
	
	/**
	 * @param src  BASE64编码的密文
	 * @param key	密钥
	 * @param charSet  编码类型:UTF-8编码  系统约定
	 * @return UTF-8编码的明文
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws UnsupportedEncodingException
	 */
	public static String decryptUseDES3(byte[] data,String keyStr,String charSet) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		//byte[]  keyx=key.getBytes("UTF-8");
		byte[] key=Base64.decodeBase64(keyStr);
		
		SecretKey secretKey = new SecretKeySpec(key, "DESede");//恢复密钥
		Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");//Cipher完成加密或解密工作类		//
		cipher.init(Cipher.DECRYPT_MODE, secretKey);//对Cipher初始化，解密模式
		byte[] cipherByte = cipher.doFinal(data);//解密data
		return new String(cipherByte,charSet);
	}
	
	public static String decryptNopadding(String src,String key,String charSet) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		SecretKey secretKey = new SecretKeySpec(build3DesKey(key), "DESede");//恢复密钥
		Cipher cipher = Cipher.getInstance("DESede/ECB/Nopadding");//Cipher完成加密或解密工作类
		cipher.init(Cipher.DECRYPT_MODE, secretKey);//对Cipher初始化，解密模式
		byte[] cipherByte = cipher.doFinal(Base64.decodeBase64(src.getBytes(charSet)));//解密data
		return new String(cipherByte,charSet);
	}
	
	/*
	 * public static String encryptCcb(String src) { try { //TODO 自数据库中读取DES密钥
	 * String desKey =
	 * SystemParameterService.getInstance().getParameterValue("Ccb-DESede-key");
	 * //String desKey = "123"; return encrypt(src,desKey,"UTF-8"); } catch
	 * (Exception e) { logger.error("建行数据加密异常："+e.getMessage(),e); return null; } }
	 * 
	 * public static String decryptCcb(String src) { try { //TODO 自数据库读取DES密钥 String
	 * desKey =
	 * SystemParameterService.getInstance().getParameterValue("Ccb-DESede-key");
	 * //String desKey = "123"; return decrypt(src,desKey,"UTF-8"); } catch
	 * (Exception e) { logger.error("建行数据解密异常："+e.getMessage(),e); return null; } }
	 */
	
	private static byte[] build3DesKey(String key) throws UnsupportedEncodingException {
		byte[] keyArray = new byte[24];
		byte[] initKey = key.getBytes("UTF-8");
		logger.info("生成KYE时,KEY的长度是是"+initKey.length);
		if(keyArray.length > initKey.length) {
			//如果initKey不够24位，则拷贝initKey数组整个长度的内容到keyArray数组中
			System.arraycopy(initKey, 0, keyArray, 0, initKey.length);
		} else {
			//如果initKey大于24位，则拷贝initKey数组24个长度的内容到keyArray数组中
			System.arraycopy(initKey, 0, keyArray, 0, keyArray.length);
		}
		return keyArray;
	}
	
	/**
	 * 
	 * 对需要传输的密钥采用公共密钥进行加密  DES加密  
	 * 
	 * @param data  需要加密的密钥
	 * @param key   公共密钥
	 * @return
	 */
	public static byte[] keyEncrypt(byte[] data,String key) {
		//项目中要求对公共密钥进行特殊处理(在密钥交换阶段)
		byte[] keyByte = ByteToHexStringUtil.asc2bin(key);
		logger.info("-----------------keyEncrypt key------------");
		logger.info(key);
		logger.info("--------------keyEncrypt Begin--------------");
		logger.info(ByteToHexStringUtil.bytesToHexString(keyByte));
		logger.info("--------------keyEncrypt End--------------");
		SecretKey secretKey = new SecretKeySpec(keyByte, "DES");//恢复密钥
		try {
			Cipher cipher = Cipher.getInstance("DES");//Cipher完成加密或解密工作类
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);//对Cipher初始化，解密模式
			return cipher.doFinal(data);//加密data
		} catch (Exception e) {
			logger.error("建行密钥传输数据加密异常："+e.getMessage(),e);
		}
		return null;
	}
	
	/**
	 * 
	 * 对接收到的密钥采用公共密钥进行解密  DES解密
	 * 
	 * @param data  需解密的密钥
	 * @param key   公共密钥
	 * @return
	 */
	public static byte[] keyDecrypt(byte[] data,String key) {
		//项目中要求对公共密钥进行特殊处理(在密钥交换阶段)
		byte[] keyByte = ByteToHexStringUtil.asc2bin(key);
		logger.info("-----------------keyDecrypt key------------");
		logger.info(key);
		logger.info("--------------keyDecrypt Begin--------------");
		logger.info(ByteToHexStringUtil.bytesToHexString(keyByte));
		logger.info("--------------keyDecrypt End--------------");
		SecretKey secretKey = new SecretKeySpec(keyByte, "DES");//恢复密钥
		try {
			Cipher cipher = Cipher.getInstance("DES");//Cipher完成加密或解密工作类
			cipher.init(Cipher.DECRYPT_MODE, secretKey);//对Cipher初始化，解密模式
			return cipher.doFinal(data);//解密data
		} catch (Exception e) {
			logger.error("建行密钥传输数据解密异常："+e.getMessage(),e);
		}
		return null;
	}
	
	public static void testxx() {
		String key = "9021230001171025";
		String src = "dkdeikdik127384321323张学博";
		System.out.println(src);
		byte[] esse = keyEncrypt(src.getBytes(),key);
		String result = new String(Base64.encodeBase64(esse));
		System.out.println(result);
		
		byte[] esds = keyDecrypt(esse,key);
		String desRes = new String(esds);
		System.out.println(desRes);
	}
	
	public static void myTest() throws NoSuchAlgorithmException, UnsupportedEncodingException, GeneralSecurityException {
//		String xx="test1224234234$#%^$%^$%^$%^汉字";
//		String key="34563476457656877897089$%&*^&*^%*%^*&%^*";
//		String encrypt=Des3Util.encryptUseDES3(xx,key,"UTF-8");
//		
//		String decrypt=Des3Util.decryptUseDES3(encrypt, key, "UTF-8");
//		System.out.print("decrypt str is:"+decrypt);
		
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException, GeneralSecurityException {
		myTest();
	}

}
