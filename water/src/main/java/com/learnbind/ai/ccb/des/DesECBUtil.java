package com.learnbind.ai.ccb.des;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

/***
 * DES ECB PKCS5Padding 对称加密 解密
 */
public class DesECBUtil {
	
	private static final String KEY_ALGORITHM="DES";  //KEY生成算法
	private static final String CIPHER_ALGORITHM="DES/ECB/PKCS5Padding";   //CIPHER生成算法
	
	/**
	 * 加密数据
	 * 
	 * @param encryptString  需要加密的数据
	 * @param encryptKey  密钥
	 * @return  base64编码的密文
	 * @throws Exception
	 */
	public static String encryptDES(String encryptString, String encryptKey) throws Exception {

		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(getKey(encryptKey), KEY_ALGORITHM));
		byte[] encryptedData = cipher.doFinal(encryptString.getBytes("UTF-8"));
		return Base64.encodeBase64String(encryptedData);
	}
	
	/**
	 * 加密数据
	 * 
	 * @param encryptString
	 * @param encryptKey
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptDES2Byte(String encryptString, String encryptKey) throws Exception {
		Cipher cipher = Cipher.getInstance("CIPHER_ALGORITHM");
		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(getKey(encryptKey), KEY_ALGORITHM));
		byte[] encryptedData = cipher.doFinal(encryptString.getBytes("UTF-8"));
		//return Base64.encodeBase64String(encryptedData);
		return encryptedData;
	}
	
	
	public static byte[] encryptDES2Byte(byte[] sourceBytes, String encryptKey) throws Exception {
		Cipher cipher = Cipher.getInstance("CIPHER_ALGORITHM");
		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(getKey(encryptKey), KEY_ALGORITHM));
		byte[] encryptedData = cipher.doFinal(sourceBytes);
		//return Base64.encodeBase64String(encryptedData);
		return encryptedData;
	}
	

	/**
	 * key 不足8位补位
	 * 
	 * @param string
	 */
	public static byte[] getKey(String keyRule) {
		Key key = null;
		byte[] keyByte = keyRule.getBytes();
		// 创建一个空的八位数组,默认情况下为0
		byte[] byteTemp = new byte[8];
		// 将用户指定的规则转换成八位数组
		for (int i = 0; i < byteTemp.length && i < keyByte.length; i++) {
			byteTemp[i] = keyByte[i];
		}
		key = new SecretKeySpec(byteTemp, "DES");
		return key.getEncoded();
	}

	/***
	 * 解密数据
	 * 
	 * @param decryptString
	 * @param decryptKey
	 * @return
	 * @throws Exception
	 */

	public static String decryptDES(String decryptString, String decryptKey) throws Exception {

		byte[] sourceBytes = Base64.decodeBase64(decryptString);
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(getKey(decryptKey), KEY_ALGORITHM));
		byte[] decoded = cipher.doFinal(sourceBytes);
		return new String(decoded, "UTF-8");

	}
	
	
	public static byte[] decryptDES(byte[] sourceBytes, String decryptKey) throws Exception {

		//byte[] sourceBytes = Base64.decodeBase64(decryptString);
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(getKey(decryptKey), KEY_ALGORITHM));
		byte[] decoded = cipher.doFinal(sourceBytes);
		return decoded;
	}
	
	/**
	 * @param data
	 * @param desKey
	 * @param cipherPaddingMode
	 * @return
	 * @throws InvalidKeyException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws NoSuchPaddingException 
	 * @throws NoSuchAlgorithmException 
	 */
	public static byte[] encrypt(byte[] data, byte[] desKey,String cipherPaddingMode) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
		String key=new String(desKey);
		Cipher cipher = Cipher.getInstance(cipherPaddingMode);
		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(getKey(key), KEY_ALGORITHM));
		byte[] encryptedData = cipher.doFinal(data);
		return encryptedData;
	}
	
	/**
	 * @param data
	 * @param desKey
	 * @param cipherPaddingMode
	 * @return
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 */
	public static byte[] decrypt(byte[] data, byte[] desKey,String cipherPaddingMode) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
		String key=new String(desKey);
		Cipher cipher = Cipher.getInstance(cipherPaddingMode);
		cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(getKey(key), KEY_ALGORITHM));
		byte[] decoded = cipher.doFinal(data);
		return decoded;
	}
	

	public static void main(String[] args) throws Exception {
		String clearText = "加密的文本";
		String key = "123432";// 密钥
		System.out.println("明文：" + clearText + "\n密钥：" + key);
		String encryptText = encryptDES(clearText, key);
		System.out.println("加密后：" + encryptText);
		String decryptText = decryptDES(encryptText, key);
		System.out.println("解密后：" + decryptText);
	}
}
