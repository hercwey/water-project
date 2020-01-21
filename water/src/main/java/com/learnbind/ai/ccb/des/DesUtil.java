package com.learnbind.ai.ccb.des;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;


public class DesUtil {

    /**
     * 加密
     * @param srcStr
     * @param charset
     * @param sKey
     * @return
     */
    public static String encrypt(String srcStr, Charset charset, String sKey) {
        byte[] src = srcStr.getBytes(charset);
        byte[] buf = DesCBC.encrypt(src, sKey);
        return DesCBC.parseByte2HexStr(buf);
    }

    /**
     * 解密
     *
     * @param hexStr
     * @param sKey
     * @return
     * @throws Exception
     */
    public static String decrypt(String hexStr, Charset charset, String sKey) throws Exception {
        byte[] src = DesCBC.parseHexStr2Byte(hexStr);
        byte[] buf = DesCBC.decrypt(src, sKey);
        return new String(buf, charset);
    }
    
    private static final String  SKEY    = "abcdefgh";
    //TODO 可以根据实际情况修改编码
    private static final Charset CHARSET = Charset.forName("UTF-8");
    
    /**
     * DES测试
     */
    private static void test1() {
    	// 待加密内容
        String str = "测试内容，今天周四";
        String encryptResult = DesUtil.encrypt(str, CHARSET, SKEY);
        System.out.println(encryptResult);
        // 直接将如上内容解密
        String decryResult = "";
        try {
            decryResult = DesUtil.decrypt(encryptResult, CHARSET, SKEY);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        System.out.println(decryResult);
    }
    
    /**
     * DES测试
     * DES加密
     * 采用DESede解密
     */
    private static void test2() {
    	// 待加密内容
        String str = "测试内容，今天周四";
        System.out.println("加密前:"+str);
        
        String encryptResult = DesUtil.encrypt(str, CHARSET, SKEY);
        System.out.println("加密后:"+encryptResult);
        
        // 直接将如上内容解密
        String decryResult = "";
        try {
            //decryResult = DesUtil.decrypt(encryptResult, CHARSET, SKEY);
        	byte[] tempArr=DesCBC.parseHexStr2Byte(encryptResult);
        	//String tempStr=new String(tempArr,CHARSET);
        	String tempStr=DesCBC.parseByte2HexStr(tempArr);
        	//System.out.println("加密后:"+tempStr);        	
        	
        	decryResult = DesUtil.decrypt(tempStr, CHARSET, SKEY);
        	System.out.println("解密后:"+decryResult);
        	
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        //System.out.println(decryResult);
    }
    
   
    
    
    

    public static void main(String[] args) throws UnsupportedEncodingException {
    	//test1();
    	//test2();
    	//test3();
    }
}
