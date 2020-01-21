package com.learnbind.ai.common.util;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import org.apache.commons.codec.binary.Base64;
/**
 * @ClassName RsaUtil
 * @Description RSA加密/解密工具类(非对称密钥)
 * @author Administrator
 * @Date 2017年11月15日 上午11:27:41
 * @version 1.0.0
 */
public class RsaUtil {
	public static int PUBLIC_KEY_INDEX=0; //公钥索引号
	public static int PRIVATE_KEY_INDEX=1; //私钥索引号


    /**
     * @Description RSA加密
     * @param data 需要加密的字符串
     * @param publicKey 公钥
     * @return 返回加密后的字符串
     */
    public static String encryptData(String data, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] dataToEncrypt = data.getBytes("utf-8");
            byte[] encryptedData = cipher.doFinal(dataToEncrypt);
            String encryptString = Base64.encodeBase64String(encryptedData);
            return encryptString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * @Description RSA解密
     * @param data  需要解密的字符串
     * @param privateKey 私钥
     * @return 返回解密后的字符串
     */
    public static String decryptData(String data, PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] descryptData = Base64.decodeBase64(data);
            byte[] descryptedData = cipher.doFinal(descryptData);
            String srcData = new String(descryptedData, "utf-8");
            return srcData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
     }
    
    /**
     * @Description 生成私钥与公钥
     * @return list[0]存放公钥 list[1]存放私钥  
     */
    
    public static List<String> createKeyPairs() {
    	List<String> keyList=new ArrayList<String>();
        try {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "BC");
            generator.initialize(512, new SecureRandom());
            KeyPair pair = generator.generateKeyPair();
            PublicKey pubKey = pair.getPublic();
            PrivateKey privKey = pair.getPrivate();
            byte[] pk = pubKey.getEncoded();
            byte[] privk = privKey.getEncoded();
            String strpk = new String(Base64.encodeBase64(pk));
            String strprivk = new String(Base64.encodeBase64(privk));

            //System.out.println("公钥Base64编码:" + strpk);
            //System.out.println("私钥Base64编码:" + strprivk);
            
            //将私钥与公钥放置到列表中.
            keyList.add(strpk); 
            keyList.add(strprivk);
            return keyList;
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;

    }
    
    /**
     * @Description 测试用函数
     */
    public static void testRsa() {
        try {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

            List<String> keyList= RsaUtil.createKeyPairs();
            
            String strpk = keyList.get(PUBLIC_KEY_INDEX);//"MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAMimSk/LsfSqQhEoh4bjHRyRTYvcLFKiWJQnE8Dp5gzllDVqplFLMibF4GW0j3zjvMNNy2WdEEOpUSLY2JCSy0ECAwEAAQ==";
            String strprivk = keyList.get(PRIVATE_KEY_INDEX);//"MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAk9KwCb0+jTxW0YpDHzpO45i7/sFM63BiUVC+NAISzdpN5Q4fen2mB3enDXg26bSf+XdKTd3Vq/iHNkm+U4N9JwIDAQABAkAch9iUOKNfDRtQnBfyagWZ5fu64sIe2vUO7r+XOCM6+a/BvKV+5aMRpR6ts8OyEz9F+KCagc8eSEO0DAFjurQ5AiEA72jh09XwAHpvUONQu8JyziZtB5Cpf/y2iCC3ucxJ510CIQCeEQ+2sd4jC7P+wdCB0K1HxXtslxD3Bq50yVtsyI3CUwIhAJUpQ4o4QNALeE9tUV+qRt0qE8Qi3Xhge1lVCSM5pNIBAiACY0OXgOxYHy8i5A6gR2S2ttb8dvO8p48vGHOXGxh5HQIgfiKMcSTfflaQBBgzDFvaVnsfs2ajbv9tNcWuAP7u6aA=";

            X509EncodedKeySpec pubX509 = new X509EncodedKeySpec(Base64.decodeBase64(strpk.getBytes()));
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decodeBase64(strprivk.getBytes()));

            KeyFactory keyf = KeyFactory.getInstance("RSA", "BC");
            //KeyFactory keyf = KeyFactory.getInstance("RSA/ECB/NoPadding");
            
            PublicKey pubKey = keyf.generatePublic(pubX509);
            PrivateKey privKey = keyf.generatePrivate(priPKCS8);

            RsaUtil rsaUtil =  new RsaUtil();
            String data = "admin";
            System.out.println("加密前字符串：data=" + data);
            String encryptData = null;
            if (pubKey != null && (data != null && !data.equals(""))) {
                encryptData = rsaUtil.encryptData(data, pubKey);
                System.out.println("加密后字符串：encryptData=" + encryptData);
            }
            String descryptData = null;
            if (privKey != null && (encryptData != null && !encryptData.equals(""))) {
                descryptData = rsaUtil.decryptData(encryptData, privKey);
                System.out.println("解密后字符串：descryptData=" + descryptData);
            } 
         }catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    /**
     * @Description 测试用函数
     */
    public static void testRsa1() {
        try {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

            String strpk = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAM2pAs4SbMrNMeLunPGDVH44iBy8qXGuH2cS5SqtLPe6DpK5xbbMLi1dhzBX9vhO0T8OcxwRUkAMTVuqgUFb58kCAwEAAQ==";
            String strprivk = "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAzakCzhJsys0x4u6c8YNUfjiIHLypca4fZxLlKq0s97oOkrnFtswuLV2HMFf2+E7RPw5zHBFSQAxNW6qBQVvnyQIDAQABAkEAlDLoXIsWboWzHy6HVdondtVR8DNbfxnUczhAf6JPNytHRaq5vqeWBxYZOqkoVqMKKyDCfgVyCn+tnW9x+VDyiQIhAObzc9bXZ/OqqmL9cns8nDyqeNTFVvq+0vh5HCSx7ETXAiEA4/dWrPAlLn6Z23sZPvqHAQxumiDH78uhblAlnUTdBF8CIFBhx52tF1ctvWP6xo+yBsbupmOX07AuHouwy2TSMGO9AiB1AQzupC8hRv9nBC54AZPrONtHpk2YSTao7N/xcVfTFwIgMtobku7L/YHxPCV2v8FyXiYEvpqtY0cg+TowfiUrKOw=";

            X509EncodedKeySpec pubX509 = new X509EncodedKeySpec(Base64.decodeBase64(strpk.getBytes()));
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decodeBase64(strprivk.getBytes()));

            KeyFactory keyf = KeyFactory.getInstance("RSA", "BC");
            PublicKey pubKey = keyf.generatePublic(pubX509);
            PrivateKey privKey = keyf.generatePrivate(priPKCS8);

            RsaUtil rsaUtil =  new RsaUtil();
            String data = "admin";
            System.out.println("加密前字符串：data=" + data);
            String encryptData = null;
            if (pubKey != null && (data != null && !data.equals(""))) {
                encryptData = rsaUtil.encryptData(data, pubKey);
                System.out.println("加密后字符串：encryptData=" + encryptData);
                System.out.println("加密后字符串：encryptData=ICRyUa5GOVsY6esTHgsRKHFmE5oG83khPwuOimRSfuvjTM9vb6qrFQx04bF18ez8wQkJ/rZvaAVhH86ILvTXfQ==");
                String a = rsaUtil.decryptData("ICRyUa5GOVsY6esTHgsRKHFmE5oG83khPwuOimRSfuvjTM9vb6qrFQx04bF18ez8wQkJ/rZvaAVhH86ILvTXfQ==", privKey);
                System.out.println("解密后字符串：descryptData=" + a);
            }
            String descryptData = null;
            if (privKey != null && (encryptData != null && !encryptData.equals(""))) {
                descryptData = rsaUtil.decryptData(encryptData, privKey);
                System.out.println("解密后字符串：descryptData=" + descryptData);
            } 
         }catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    public static void main(String[] args) {
    	testRsa1();
	}
    
}

