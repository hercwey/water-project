package com.learnbind.ai.ccb.des;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
 
/**
 * SecretUtils {3DES加密解密的工具类 }
 * @author William
 * @date 2013-04-19
 */
public class DESede {
 
    //定义加密算法，有DES、DESede(即3DES)、Blowfish
    //public static final String Algorithm = "DESede";
    public static final String Algorithm_Desede = "DESede";  //项目上采用此算法.  3DES
    //public static final String Algorithm_Desedex = "DESede/ECB/PKCS5Padding";  //项目上采用此算法.  3DES
    //public static final String Algorithm_Desedex = "DESede/ECB/PKCS5Padding";  //项目上采用此算法.  3DES
    
    ////////public static final String Algorithm_Desedex = "DESede/ECB/PKCS5Padding";  //项目上采用此算法.  3DES
    
    
    public static final String Algorithm_Desedex = "DESede/ECB/PKCS5Padding";  //项目上采用此算法.  3DES
    
    public static final String Algorithm_Des = "DES";
    
    public static final String Algorithm_Blowfish = "Blowfish";
    
    private static final String PASSWORD_CRYPT_KEY = "2012PinganVitality075522628888ForShenZhenBelter075561869839";
    
    
    /**
     * 加密方法
     * @param src 源数据的字节数组
     * @return 
     */
    public static byte[] encryptMode(String algorithm ,String key,byte[] src) {
        try {
             SecretKey deskey = new SecretKeySpec(build3DesKey(key), "DESede");    //生成密钥
             Cipher c1 = Cipher.getInstance(algorithm);    //实例化负责加密/解密的Cipher工具类
             c1.init(Cipher.ENCRYPT_MODE, deskey);    //初始化为加密模式
             return c1.doFinal(src);
         } catch (java.security.NoSuchAlgorithmException e1) {
             e1.printStackTrace();
         } catch (javax.crypto.NoSuchPaddingException e2) {
             e2.printStackTrace();
         } catch (java.lang.Exception e3) {
             e3.printStackTrace();
         }
         return null;
     }
    
    
    /**
     * 解密函数
     * @param src 密文的字节数组
     * @return
     */
    public static byte[] decryptMode(String algorithm,String key,byte[] src) {      
        try {
            SecretKey deskey = new SecretKeySpec(build3DesKey(key), "DESede");
            Cipher c1 = Cipher.getInstance(algorithm);
            c1.init(Cipher.DECRYPT_MODE, deskey);    //初始化为解密模式
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }
        return null;
     }
    
    
    
    /*
     * 根据字符串生成密钥字节数组 
     * @param keyStr 密钥字符串
     * @return 
     * @throws UnsupportedEncodingException
     */
    public static byte[] build3DesKey(String keyStr) throws UnsupportedEncodingException{
        byte[] key = new byte[24];    //声明一个24位的字节数组，默认里面都是0
        byte[] temp = keyStr.getBytes("UTF-8");    //将字符串转成字节数组
        
        /*
         * 执行数组拷贝
         * System.arraycopy(源数组，从源数组哪里开始拷贝，目标数组，拷贝多少位)
         */
        if(key.length > temp.length){
            //如果temp不够24位，则拷贝temp数组整个长度的内容到key数组中
            System.arraycopy(temp, 0, key, 0, temp.length);
        }else{
            //如果temp大于24位，则拷贝temp数组24个长度的内容到key数组中
            System.arraycopy(temp, 0, key, 0, key.length);
        }
        return key;
    }
    
    /**
     	3DES使用流程
     	【Java使用3DES加密解密的流程】
    	①传入共同约定的密钥（keyBytes）以及算法（Algorithm），来构建SecretKey密钥对象
        	SecretKey deskey = new SecretKeySpec(keyBytes, Algorithm);    
    	②根据算法实例化Cipher对象。它负责加密/解密
        	Cipher c1 = Cipher.getInstance(Algorithm);    
    	③传入加密/解密模式以及SecretKey密钥对象，实例化Cipher对象
        	c1.init(Cipher.ENCRYPT_MODE, deskey);    
    	④传入字节数组，调用Cipher.doFinal()方法，实现加密/解密，并返回一个byte字节数组
        	c1.doFinal(src);
     */
    
    private static void test() {
    	String msg = "3DES加密解密案例";
        System.out.println("【加密前】：" + msg);
        
        //加密
        byte[] secretArr = DESede.encryptMode(Algorithm_Desede, PASSWORD_CRYPT_KEY, msg.getBytes());    
        System.out.println("【加密后】：" + new String(secretArr));
        
        //解密
        byte[] myMsgArr = DESede.decryptMode(Algorithm_Desede, PASSWORD_CRYPT_KEY,secretArr);  
        System.out.println("【解密后】：" + new String(myMsgArr));
    }
    
    
    	 
    /**
     * @param args
     */
    public static void main(String[] args) {    	
        test();
    }
    
    
    
}
