package com.learnbind.ai.ccb.sdk.rsa;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.learnbind.ai.controller.ccb.NeuqGetCcbPubKeyController;

/**
 * 
 * 根据密钥字符串(BASE64编码)生成密钥
 * @author lenovo
 *
 */
public class PemRSAEncrypt {
	
	private static final Logger logger = LoggerFactory.getLogger(NeuqGetCcbPubKeyController.class);
	
	
	/**
	 * 根据密钥字符串生成公共密钥
	 * @param base64PublicKey  base64编码的密钥字符串
	 * @return  RSAPublicKey公钥
	 * @throws Exception
	 */
	public static RSAPublicKey getPublicKeyByBase64Str(String base64PublicKey) throws Exception{
		return getPublicKeyFromPublicKeyBase64Str(base64PublicKey);
	}
	
	/**
	 * @param base64PrivateKey
	 * @return
	 * @throws Exception
	 */
	public static RSAPrivateKey getPrivateKeyByBase64Str(String base64PrivateKey) throws Exception{
		return getPrivateKeyFromPrivateKeyBase64Str(base64PrivateKey);
	}
	
	
	/**
	 * 
	 * @param filePath 自私钥文件中获取私钥字符串
	 * @return  文件路径
	 * //TODO  此处为桩模块  稍后处理 
	 */
	public static String loadPrivateKeyByFile(String filePath) {
		return "";
	}
	
	/**
	 * 自公钥文件中获取公钥字符串
	 * @param filePath  文件路径
	 * @return
	 * //TODO  此处为桩模块  稍后处理. 
	 */
	public static String loadPublicKeyByFile(String filePath) {
		return "";
	}
	
	
	/**
	 *  根据公钥字符串生成RSA公钥
	 * @param publicKeyStr  公钥字符串  base64编码
	 * @return  RSA公钥对象
	 * @throws Exception
	 */
	private static  RSAPublicKey getPublicKeyFromPublicKeyBase64Str(String base64PublicKeyStr) throws Exception {
		Base64 decoder = new Base64();
		byte[] buffer = (byte[]) decoder.decode(base64PublicKeyStr.getBytes());
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
		return (RSAPublicKey) keyFactory.generatePublic(keySpec);
	}
	
	
	/**
	 * 
	 * @param base64PrivateKeyStr
	 * @return
	 * @throws Exception
	 */
	private static  RSAPrivateKey getPrivateKeyFromPrivateKeyBase64Str(String base64PrivateKeyStr) throws Exception {
		
		RSAPrivateKey privateKey=null;
		Base64 decoder = new Base64();
		byte[] buffer = (byte[]) decoder.decode(base64PrivateKeyStr.getBytes());
		
		PKCS8EncodedKeySpec priPKCS8;
		  try {
		   priPKCS8 = new PKCS8EncodedKeySpec(buffer);
		   KeyFactory keyf = KeyFactory.getInstance("RSA");
		   privateKey = (RSAPrivateKey)keyf.generatePrivate(priPKCS8);
		  } catch (NoSuchAlgorithmException e) {
		   e.printStackTrace();
		  } catch (InvalidKeySpecException e) {
		   e.printStackTrace();
		  }
		  return privateKey;
	}	
	
}
