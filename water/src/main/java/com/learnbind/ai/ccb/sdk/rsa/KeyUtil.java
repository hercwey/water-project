package com.learnbind.ai.ccb.sdk.rsa;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.learnbind.ai.ccb.des.DesECBUtil;
import com.learnbind.ai.common.util.fileutil.DateUtils;

public class KeyUtil {
    /**
     * 签名算法
     */
    public static final String SIGN_ALGORITHMS = "SHA256withRSA";
    //pbulic static final String SIGN_ALGORITHMS = "SHA256withRSA";
    public static void main(String[] args) throws Exception {
        /*String filepath = "D:\\key\\MDAQ\\";
        Base64 base64 = new Base64();

        System.out.println("--------------公钥加密私钥解密过程-------------------");
        String signKey = "ihep_公钥加密私钥解密";
        // 公钥加密过程
        byte[] cipherData = PemRSAEncrypt.encrypt(PemRSAEncrypt.loadPublicKeyByStr(PemRSAEncrypt.loadPublicKeyByFile(filepath)),
                signKey.getBytes());
        String cipher = new String(base64.encode(cipherData));
        // 私钥解密过程
        byte[] res = PemRSAEncrypt.decrypt(PemRSAEncrypt.loadPrivateKeyByStr(PemRSAEncrypt.loadPrivateKeyByFile(filepath)),
                base64.decode(cipher));
        String restr = new String(res);
        System.out.println("原文：" + signKey);
        System.out.println("加密：" + cipher);
        System.out.println("解密：" + restr);
        System.out.println();

        System.out.println("--------------私钥加密公钥解密过程-------------------");
//        signKey = "ihep_私钥加密公钥解密";
        // 私钥加密过程
        cipherData = PemRSAEncrypt.encrypt(PemRSAEncrypt.loadPrivateKeyByStr(PemRSAEncrypt.loadPrivateKeyByFile(filepath)),
                signKey.getBytes());
        cipher = new String(base64.encode(cipherData));
        // 公钥解密过程
        res = PemRSAEncrypt.decrypt(PemRSAEncrypt.loadPublicKeyByStr(PemRSAEncrypt.loadPublicKeyByFile(filepath)),
                base64.decode(cipher));
        restr = new String(res);
        System.out.println("原文：" + signKey);
        System.out.println("加密：" + cipher);
        System.out.println("解密：" + restr);
        System.out.println();


        System.out.println("---------------私钥签名过程------------------");
//        String content = "ihep_这是用于签名的原始数据";
        String content = signKey;
        String signstr = RSASignature.sign(content, PemRSAEncrypt.loadPrivateKeyByStr(PemRSAEncrypt.loadPrivateKeyByFile(filepath)));
        System.out.println("签名原串：" + content);
        System.out.println("签名串：" + signstr);
        System.out.println();

        System.out.println("---------------公钥校验签名------------------");
        System.out.println("签名原串：" + content);
        System.out.println("签名串：" + signstr);

        System.out.println("验签结果：" + RSASignature.doCheck(content, signstr, PemRSAEncrypt.loadPublicKeyByStr(PemRSAEncrypt.loadPublicKeyByFile(filepath))));
        System.out.println();*/
    	byte[] bytesData =KeyUtil.DESEncrypt("00000000".getBytes(), "00000000".getBytes(), "DES/ECB/NoPadding");
        System.out.println(StringUtil.Base64Encrypt(bytesData));
        System.out.println(StringUtil.bytes2hex(bytesData));
        
        byte[] bytesDatass = KeyUtil.DESDecrypt(bytesData, "00000000".getBytes(), "DES/ECB/NoPadding");
        System.out.println(new String(bytesDatass));
    	//System.out.println(loadPrivateKeyByFile("F:\\keyLinux.der"));
    }
    /**
     * 私钥签名
     * @param content 待签名信息
     * @param privateKey 私钥字符串
     * @return
     */
    public static String RSASignature(String content,String privateKey){
    	try {
            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
            signature.initSign(PemRSAEncrypt.getPrivateKeyByBase64Str(privateKey));
            signature.update(content.getBytes());
            byte[] signed = signature.sign();
            return StringUtil.bytes2hex(signed);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;   	
    }
    /**
     * 私钥签名
     * @param content 待签名信息
     * @param privateKey 私钥字符串
     * @return
     */
    public static byte[] RSASignature2Bytes(String content,String privateKey){
    	try {
            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
            signature.initSign(PemRSAEncrypt.getPrivateKeyByBase64Str(privateKey));
            signature.update(content.getBytes());
            byte[] signed = signature.sign();
            return signed;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;   	
    }
    
    /**
     * 私钥签名
     * @param content 待签名信息
     * @param privateKey 私钥字符串
     * @param encode 字符编码
     * @return
     */
    public static String RSASignature(String content,String privateKey,String encode){
    	try {
            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
            signature.initSign(PemRSAEncrypt.getPrivateKeyByBase64Str(privateKey));
            signature.update(content.getBytes(encode));
            byte[] signed = signature.sign();
            return StringUtil.bytes2hex(signed);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;     	
    }
    
    /**
     * 私钥签名,返回byte数组
     * @param content 待签名信息
     * @param privateKey 私钥字符串
     * @param encode 字符编码
     * @return
     */
    public static byte[] RSASignature2Bytes(String content,String privateKey,String encode){
    	try {
            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
            signature.initSign(PemRSAEncrypt.getPrivateKeyByBase64Str(privateKey));
            signature.update(content.getBytes(encode));
            byte[] signed = signature.sign();
            return signed;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;     	
    }
    
    /**
     * 公钥验签
     * @param content 原字符串
     * @param sign 私钥签名后的信息
     * @param publicKey 公钥字符串
     * @return
     */
    public static boolean RSAVerifySignature(String content, String sign,String publicKey,String encode){
    	try {
            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);

            signature.initVerify(PemRSAEncrypt.getPublicKeyByBase64Str(publicKey));
            signature.update(content.getBytes(encode));

            boolean bverify = signature.verify(StringUtil.hex2Byte(sign));
            return bverify;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false; 	
    }
    /**
     * 公钥验签
     * @param content 原字符串
     * @param sign 私钥签名后的信息
     * @param publicKey 公钥字符串
     * @return
     */
    public static boolean RSAVerifySignature(String content, String sign,String publicKey){
    	try {
            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);

            signature.initVerify(PemRSAEncrypt.getPublicKeyByBase64Str(publicKey));
            signature.update(content.getBytes());

            boolean bverify = signature.verify(StringUtil.hex2Byte(sign));
            return bverify;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false; 	
    }

    
    /**
     * 公钥加密
     * @param content 私钥加密后的字符串
     * @param publicKey 公钥字符串
     * @return 解密后的字符串 
     * @throws Exception 
     */
    public static byte[] RSAEncrypt(byte[] content, String publicKey) throws Exception{
    	if (publicKey == null) {
            throw new Exception("加密公钥为空, 请设置");
        }
        Cipher cipher = null;
        try {
            // 使用默认RSA
            cipher = Cipher.getInstance("RSA");
            // cipher= Cipher.getInstance("RSA", new BouncyCastleProvider());
            cipher.init(Cipher.ENCRYPT_MODE, PemRSAEncrypt.getPublicKeyByBase64Str(publicKey));
            byte[] output = cipher.doFinal(content);
            return output;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此加密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            throw new Exception("加密公钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new Exception("明文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("明文数据已损坏");
        }   	
    }

    /**
     * 私钥解密
     * @param content 原字符串
     * @param privateKey 私钥字符串
     * @return 加密后字符串
     * @throws Exception 
     */
    public static byte[] RSADecrypt(byte[] content, String privateKey) throws Exception{
        if (privateKey == null) {
            throw new Exception("解密私钥为空, 请设置");
        }
        Cipher cipher = null;
        try {
            // 使用默认RSA
            cipher = Cipher.getInstance("RSA");
            // cipher= Cipher.getInstance("RSA", new BouncyCastleProvider());
            cipher.init(Cipher.DECRYPT_MODE, PemRSAEncrypt.getPrivateKeyByBase64Str(privateKey));
            byte[] output = cipher.doFinal(content);
            return output;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此解密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            throw new Exception("解密私钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new Exception("密文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("密文数据已损坏");
        }
   	
    }
    
    /**
     * 私钥加密
     * @param content 原字符串
     * @param privateKey 私钥字符串
     * @return 加密后字符串
     * @throws Exception 
     */
    public static byte[] RSAPrivateEncrypt(byte[] content, String privateKey) throws Exception{
    	if (privateKey == null) {
            throw new Exception("加密私钥为空, 请设置");
        }
        Cipher cipher = null;
        try {
            // 使用默认RSA
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, PemRSAEncrypt.getPrivateKeyByBase64Str(privateKey));
            byte[] output = cipher.doFinal(content);
            return output;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此加密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            throw new Exception("加密私钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new Exception("明文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("明文数据已损坏");
        }
   	
    }
    
    /**
     * 公钥解密
     * @param content 私钥加密后的字符串
     * @param publicKey 公钥字符串
     * @return 解密后的字符串 
     * @throws Exception 
     */
    public static byte[] RSAPubicDecrypt(byte[] content, String publicKey) throws Exception{
    	if (publicKey == null) {
            throw new Exception("解密公钥为空, 请设置");
        }
        Cipher cipher = null;
        try {
            // 使用默认RSA
            cipher = Cipher.getInstance("RSA");
            // cipher= Cipher.getInstance("RSA", new BouncyCastleProvider());
            cipher.init(Cipher.DECRYPT_MODE,  PemRSAEncrypt.getPublicKeyByBase64Str(publicKey));
            byte[] output = cipher.doFinal(content);
            return output;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此解密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            throw new Exception("解密公钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new Exception("密文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("密文数据已损坏");
        }  	
    }
    /**
     * 从文件中加载私钥
     * @param path 私钥文件名
     * @return 私钥串
     * @throws MyException
     */
    public static String loadPrivateKeyByFile(String path) throws MyException{
    	String privateStr="";
    	try {
    		privateStr = PemRSAEncrypt.loadPrivateKeyByFile(path);
		} catch (Exception e) {
			throw new MyException("读取私钥错误 异常:"+DateUtils.nowDateTime(), e); 
		}
		return privateStr;
    }
 
    /**
     * 从文件中加载公钥
     * @param path 公钥文件名
     * @return 私钥串
     * @throws MyException
     */
    public static String loadPublicKeyByFile(String path) throws MyException{
    	String privateStr="";
    	try {
    		privateStr = PemRSAEncrypt.loadPublicKeyByFile(path);
		} catch (Exception e) {
			throw new MyException("读取公钥错误 异常:"+DateUtils.nowDateTime(), e); 
		}
		return privateStr;
    }
   /**
    * 使用des进行加密
    * @param data 待加密的数据
    * @param desKey 密钥
    * @param cipherPaddingMode 加密填充模式
    *        "DES/ECB/PKCS5Padding"
    *        "DES/CBC/PKCS5Padding"
    * @return
    * @throws MyException 
    */
   public static byte[] DESEncrypt(byte[] data, byte[] desKey,String cipherPaddingMode) throws MyException{
	   byte[] bytes;	   
	   try {
		   bytes=DesECBUtil.encrypt(data, desKey, cipherPaddingMode);
	   } catch (Exception e) {
		   throw new MyException("加密 异常:"+DateUtils.nowDateTime(), e); 
	   }
	   return bytes;
   }
   /**
    * 使用des进行解密
    * @param data 待解密的数据
    * @param desKey 密钥
    * @param cipherPaddingMode 加密填充模式
    *        "DES/ECB/PKCS5Padding"
    *        "DES/CBC/PKCS5Padding"
    * @return
    * @throws MyException 
    */
   public static byte[] DESDecrypt(byte[] data, byte[] desKey,String cipherPaddingMode) throws MyException{
	   byte[] bytes;	   
	   try {
		   bytes = DesECBUtil.decrypt(data, desKey, cipherPaddingMode);
	   } catch (Exception e) {
		   throw new MyException("加密 异常:"+DateUtils.nowDateTime(), e); 
	   }
	   return bytes;
   }

}