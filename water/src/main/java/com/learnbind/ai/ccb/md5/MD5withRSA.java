package com.learnbind.ai.ccb.md5;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

import com.learnbind.ai.ccb.sdk.rsa.StringUtil;

/**
 *  * 数字签名  * 
 * 1：MD5withRSA，：将正文通过MD5数字摘要后，将密文 再次通过生成的RSA密钥加密，生成数字签名，  *
 * 		将明文与密文以及公钥发送给对方，对方拿到私钥/公钥对数字签名进行解密，然后解密后的，与明文经过MD5加密进行比较  * 如果一致则通过  *
 * 2：使用Signature的API来实现MD5withRSA  
 * * @author CUICHUNCHI  *  
 */
public class MD5withRSA {
	//密钥长度常量
	private static final int KEY_LEN_512=512;
	private static final int KEY_LEN_1024=1024;
	private static final int KEY_LEN_2048=2048;
	private static final int KEY_LEN_4096=4096;
	
	/**
	 * 密钥算法
	 */
	private static final String KEY_ALGORITHM = "RSA";        //KYE生成算法
	private static final String SIGN_ALGORITHM="MD5withRSA";  //数字签名算法
	private static final String DIGEST_ALGORITHM="MD5";       //摘要算法
	
	/**
	 * 加密/解密算法 / 工作模式 / 填充方式
	 * Java 6支持PKCS5Padding填充方式
	 * Bouncy Castle支持PKCS7Padding填充方式
	 */
	//private static final String CIPHER_ALGORITHM = "RSA/CBC/PKCS5Padding";

	//RSA/ECB/PKCS1Padding  可用
	private static final String CIPHER_ALGORITHM = "RSA";  //
		

	/**
	 * 使用RSA生成一对钥匙
	 * 
	 * @param str
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static KeyPair getKeyPair() throws NoSuchAlgorithmException {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
		keyPairGenerator.initialize(KEY_LEN_1024);		
		//生成返回带有公钥和私钥的对象
		KeyPair generateKeyPair = keyPairGenerator.generateKeyPair();
		return generateKeyPair;
	}

	/**
	 * 生成私钥
	 * 
	 * @param str @return 
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static PrivateKey getPrivateKey(KeyPair key) {
		PrivateKey generatePrivate = null;
		try {
			PrivateKey private1 = key.getPrivate();
			byte[] encoded = private1.getEncoded();
			//PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
			KeyFactory factory = KeyFactory.getInstance("RSA");
			generatePrivate = factory.generatePrivate(keySpec);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return generatePrivate;
	}

	/**
	 * 私钥加密
	 * 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 */
	public static byte[] encrtpyByPrivateKey(byte[] bb, PrivateKey key)
			throws IllegalBlockSizeException, BadPaddingException {
		byte[] doFinal = null;
		try {
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			doFinal = cipher.doFinal(bb);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return doFinal;
	}

	/**
	 * 获取公钥
	 * 
	 * @param str @return 
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static PublicKey getPublicKey(KeyPair keyPair) {
		PublicKey publicKey = null;
		try {
			PublicKey public1 = keyPair.getPublic();
			byte[] encoded = public1.getEncoded();
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
			KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
			publicKey = factory.generatePublic(keySpec);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return publicKey;
	}

	/**
	 * 使用公钥解密
	 * 
	 * @param str @return 
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[] decodePublicKey(byte[] b, PublicKey key) {
		byte[] doFinal = null;
		try {
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, key);
			doFinal = cipher.doFinal(b);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return doFinal;
	}

	//通过MD5加密
	public static byte[] encryptMD5(String str) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance(DIGEST_ALGORITHM);
		byte[] digest2 = digest.digest(str.getBytes());
		return digest2;
	}

	//sign签名
	public static byte[] sign(String str, PrivateKey key)
			throws NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException {
		byte[] encryptMD5 = encryptMD5(str);
		byte[] encrtpyByPrivateKey = encrtpyByPrivateKey(encryptMD5, key);
		return encrtpyByPrivateKey;
	}

	//校验
	public static boolean verify(String str, byte[] sign, PublicKey key) throws NoSuchAlgorithmException {
		byte[] encryptMD5 = encryptMD5(str);
		byte[] decodePublicKey = decodePublicKey(sign, key);
		String a = new String(encryptMD5);
		String b = new String(decodePublicKey);
		if (a.equals(b)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Signature的用法 数字签名
	 * 
	 * @param args
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException 
	 * @throws SignatureException 
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static byte[] signMethod(String str, PrivateKey key)
			throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
		//初始化 MD5withRSA
		Signature signature = Signature.getInstance(SIGN_ALGORITHM);
		//使用私钥
		signature.initSign(key);
		//需要签名或校验的数据
		signature.update(str.getBytes());
		return signature.sign();// 进行数字签名
	}

	/**
	 * 数字校验
	 * 
	 * @param args
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException 
	 * @throws SignatureException 
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static boolean verifyMethod(String str, byte[] sign, PublicKey key)
			throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
		Signature signature = Signature.getInstance(SIGN_ALGORITHM);
		signature.initVerify(key);
		signature.update(str.getBytes());
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
		
		public static String encryptionMd5(byte[] plainText) throws Exception {
			String re_md5 = new String();
			MessageDigest md = MessageDigest.getInstance("MD5");
			//md.update(plainText.getBytes("GBK"));
			md.update(plainText);
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
		
		
		//-----------------------------test case--------------------------------
	
	@SuppressWarnings("unused")
	private static void test() throws NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, SignatureException {
		String str = "司法改革晚上v的方法的官方";
		System.out.println("待加密的明文：" + str);
		byte[] encryptMD5 = encryptMD5(str);
		System.out.println("MD5加密后：" + encryptMD5);
		//获取钥匙对
		KeyPair keyPair = getKeyPair();
		//获取公钥
		PublicKey publicKey = getPublicKey(keyPair);
		//获取私钥
		PrivateKey privateKey = getPrivateKey(keyPair);

		byte[] sign = sign(str, privateKey);
		boolean verify = verify(str, sign, publicKey);
		System.out.println("是否一致" + verify);

		byte[] encrtpyByPrivateKey = encrtpyByPrivateKey(encryptMD5, privateKey);
		System.out.println("使用私钥加密过后：" + encrtpyByPrivateKey);
		byte[] decodePublicKey = decodePublicKey(encrtpyByPrivateKey, publicKey);
		System.out.println("使用公钥解密过后：" + decodePublicKey);
		System.out.print("判断解密过后，是否一致");
		System.out.println(new String(decodePublicKey).equals(new String(encryptMD5)));

		/******************** 基于SignatureAPI签名 *************************************/
		String signStr = "的方法十分士大夫";
		byte[] signMethod = signMethod(signStr, privateKey);

		boolean verifyMethod = verifyMethod(signStr, signMethod, publicKey);
		System.out.println("使用SignatureAPI 数字签名是否一致：" + verifyMethod);
	}
	
	private static void testMD5() throws Exception {
		String str="12444556456356qwerqwerqwerqwer";
		//经过测试,这两种方法所计算的值是相同的.
		byte[] md1=encryptMD5(str);  
		String md1Str=StringUtil.bytes2hex(md1);		
		String md2Str=encryptionMd5(str);
		
		System.out.println("md1str--------"+md1Str);
		System.out.println("md2str--------"+md2Str);
		
	}

	public static void main(String[] args) {
		try {
			testMD5();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
