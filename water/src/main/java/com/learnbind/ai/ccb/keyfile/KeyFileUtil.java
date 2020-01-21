package com.learnbind.ai.ccb.keyfile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.learnbind.ai.ccb.base64.Base64Util;
import com.learnbind.ai.ccb.md5.MD5withRSA;
import com.learnbind.ai.ccb.sdk.rsa.PemRSAEncrypt;
import com.learnbind.ai.ccb.sdk.rsa.RSASignature;

/**
 * 主要包括两个方面的工作 
 * (1)用于保存RSA公钥与私钥文件 
 * (2)自密钥文件中读取相应的KEY 
 * 
 * @author lenovo
 */

public class KeyFileUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(KeyFileUtil.class);

	/**
	 * 保存密钥对到文件中
	 * 
	 * @param keyPair
	 * @param publicKeyFilePath
	 * @param privateKeyFilePath
	 * @throws NoSuchAlgorithmException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void saveRsaKeyPairToFile(KeyPair keyPair, String publicKeyFilePath, String privateKeyFilePath) {
		// KeyPair keyPair = MD5withRSA.getKeyPair(); //获取钥匙对
		PublicKey publicKey = MD5withRSA.getPublicKey(keyPair); // 获取公钥
		PrivateKey privateKey = MD5withRSA.getPrivateKey(keyPair);// 获取私钥
	
		savePublicKeyToFile(publicKey, publicKeyFilePath);
		savePrivateKeyToFile(privateKey, privateKeyFilePath);
	}

	/**
	 * 保存公钥到文件中 save rsa public key to file
	 * 
	 * @param PublicKey publicKey 公钥对象
	 * @param String    filePath  文件全路径名称
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void savePublicKeyToFile(PublicKey publicKey, String keyFilePath) {
		try {
			logger.info("---------key Path:" + keyFilePath + "-----------");
			String realPath=keyFilePath.substring(0, keyFilePath.lastIndexOf("/"));  
			File filePath =new File(realPath);    
			//如果文件夹不存在则创建    
			if (!filePath.exists() && !filePath.isDirectory()) {
				boolean flag = filePath.mkdirs();
				logger.debug("目录不存在   创建目录: "+filePath+" 结果："+flag);
			} else {
				logger.debug("目录存在 : "+filePath);
			}
			
			OutputStream outputStream = new FileOutputStream(keyFilePath);
			
			logger.info("---------file path:" + filePath + "-----------");
			
			// 生成公钥
			ObjectOutputStream oos = new ObjectOutputStream(outputStream);
			oos.writeObject(publicKey);
			oos.flush();
			oos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 保存私钥到文件中 save rsa private key to file
	 * 
	 * @param PrivateKey privateKey
	 * @param String     filePath
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void savePrivateKeyToFile(PrivateKey privateKey, String keyFilePath) {
		// 生成公钥
		logger.info("---------key Path:" + keyFilePath + "-----------");
		String realPath=keyFilePath.substring(0, keyFilePath.lastIndexOf("/"));  
		File filePath =new File(realPath);    
		//如果文件夹不存在则创建    
		if (!filePath.exists() && !filePath.isDirectory()) {
			boolean flag = filePath.mkdirs();
			logger.debug("目录不存在   创建目录: "+filePath+" 结果："+flag);
		} else {
			logger.debug("目录存在 : "+filePath);
		}		
		
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(keyFilePath));
			oos.writeObject(privateKey);
			oos.flush();
			oos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 自文件中读取公钥对象
	 * 
	 * @param keyFilePath
	 * @return
	 */
	public static PublicKey loadRsaPublicKeyFromFile(String keyFilePath) {
		PublicKey publicKey = null;
		// 生成公钥
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(new FileInputStream(keyFilePath));
			publicKey = (PublicKey) ois.readObject();
			ois.close();
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return publicKey;
	}	

	/**
	 * 自文件中读取私钥对象
	 * 
	 * @param keyFilePath
	 * @return
	 */
	public static PrivateKey loadRsaPrivateKeyFromFile(String keyFilePath) {
		PrivateKey privateKey = null;
		// 生成公钥
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(new FileInputStream(keyFilePath));
			privateKey = (PrivateKey) ois.readObject();
			ois.close();
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return privateKey;
	}
	
	/**
	 * @param key  公钥/私钥对象
	 * @return  BASE64字符串  UTF-8编码
	 */
	public static String key2Base64(Key keyx) {
		String keyStr="";
		byte[] key=keyx.getEncoded();	
		try {
			keyStr=Base64Util.encode2Base64Str(key);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return keyStr;		
	}	
	

	// ---------------------------以下为测试代码-----------------------------
	
	private static final String PUBLIC_KEY_FILE="d:/upload/keyfile/public.key";
	private static final String PRIVATE_KEY_FILE="d:/upload/keyfile/private.key";
	
	/**
	 * 已经测试
	 * 
	 * 测试将密钥写入文件中
	 * 
	 * @throws NoSuchAlgorithmException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	private static void test_saveRsaKeyPairToFile()
			throws NoSuchAlgorithmException, FileNotFoundException, IOException {
		KeyPair keyPair = MD5withRSA.getKeyPair(); // 获取钥匙对
		saveRsaKeyPairToFile(keyPair, PUBLIC_KEY_FILE, PRIVATE_KEY_FILE);
	}
	
	/**
	 * 
	 * 测试自文件中读取KEY
	 */
	private static void test_loadKeyFromFile() {
		PublicKey publicKey=loadRsaPublicKeyFromFile(PUBLIC_KEY_FILE);
		PrivateKey privateKey=loadRsaPrivateKeyFromFile(PRIVATE_KEY_FILE);
		
		String privateKeyStr=key2Base64(privateKey);
		String publicKeyStr=key2Base64(publicKey);
		logger.info("公钥BASE64编码是:"+publicKeyStr);
		logger.info("公钥BASE64编码是:"+privateKeyStr);		
	}
	
	/**
	 * 测试根据密钥字符串生成密钥对象,并进行加/解密
	 * @throws Exception 
	 */
	private static void test_sign() throws Exception {
		PublicKey publicKey=loadRsaPublicKeyFromFile(PUBLIC_KEY_FILE);
		PrivateKey privateKey=loadRsaPrivateKeyFromFile(PRIVATE_KEY_FILE);
		
		String privateKeyStr=key2Base64(privateKey);
		String publicKeyStr=key2Base64(publicKey);
		
		//测试自file中加载的密钥
		String content="1341324123443563563456346qewrqwerqwerqwer汉字#@$%#$@%@"; String
		sign= RSASignature.sign(content, privateKey, "UTF-8"); 
		boolean verify=RSASignature.doCheck(content, sign, publicKey);		  
		logger.info("校验是否通过(采用自FILE中LOAD的密钥文件内容):"+verify);
		
		
		//根据KEY BASE64字符串生成  KEY对象
		PrivateKey privateKey1= PemRSAEncrypt.getPrivateKeyByBase64Str(privateKeyStr);		
		PublicKey publicKey1=PemRSAEncrypt.getPublicKeyByBase64Str(publicKeyStr);
		
		//测试自密钥字符串生成的密钥
		sign= RSASignature.sign(content, privateKey1, "UTF-8"); 
		verify=RSASignature.doCheck(content, sign, publicKey1);		  
		logger.info("校验是否通过(采用自密钥字符串生成的密钥):"+verify);
		
		
		
		//测试自字符串生成的密钥+自文件读取的密钥
		sign= RSASignature.sign(content, privateKey, "UTF-8"); 
		verify=RSASignature.doCheck(content, sign, publicKey1);		  
		logger.info("校验是否通过(公钥来自字符串,私钥来自文件):"+verify);
		
		 
		
	}
	

	public static void main(String[] args) throws Exception {
		test_saveRsaKeyPairToFile();
		//test_loadKeyFromFile();
		//test_sign();
		
		//test_savePublicKeyToFile();
		/*
		 * String keyFilePath="e:/keyfile/ddd.txt"; String
		 * realPath=keyFilePath.substring(0, keyFilePath.lastIndexOf("/"));
		 * System.out.println(realPath);
		 */
		
		
		
	}

}
