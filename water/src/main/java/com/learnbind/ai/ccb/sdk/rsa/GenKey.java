package com.learnbind.ai.ccb.sdk.rsa;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import com.learnbind.ai.ccb.base64.Base64Util;


/**
 * 给定base64编码的RSA公钥和私钥，将string类型转换为PublicKey和PrivateKey类型
 * @author lenovo
 *
 */
public class GenKey {

	
	/**
	 * @param base64s 根据公钥字符串生成公钥(base64编码)
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static RSAPublicKey getRSAPublidKeyBybase64(String base64s) throws NoSuchAlgorithmException {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64Util.decode2Byte(base64s));
        RSAPublicKey publicKey = null;

        try {
        	KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            publicKey = (RSAPublicKey)keyFactory.generatePublic(keySpec);
        } catch (InvalidKeySpecException var4) {
            //LOGGER.error("base64编码=" + base64s + "转RSA公钥失败", var4);
        }

        return publicKey;
    }

    /**
     * @param base64s  根据私钥字符串生成私钥(base64编码)
     * @return
     * @throws NoSuchAlgorithmException
     * 注:密钥格式为: PKCS8
     */
    public static RSAPrivateKey getRSAPrivateKeyBybase64(String base64s) throws NoSuchAlgorithmException {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64Util.decode2Byte(base64s));
        RSAPrivateKey privateKey = null;

        try {
        	KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            privateKey = (RSAPrivateKey)keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException var4) {
            //LOGGER.error("base64编码=" + base64s + "转RSA私钥失败", var4);
        }

        return privateKey;
    }
}
