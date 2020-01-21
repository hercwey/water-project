package com.learnbind.ai.ccb.sdk.rsa;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * <p>
 * 1.
 * </p>
 *
 * @author PollyLuo
 * @version 1.0.0
 */
public class RSASignature {

    /**
     * 签名算法
     */
    public static final String SIGN_ALGORITHMS_SHA256withRSA = "SHA256withRSA";
    public static final String SIGN_ALGORITHMS_MD5withRSA="MD5withRSA";

    /**
     * RSA签名
     *
     * @param content    待签名数据
     * @param privateKey 商户私钥
     * @param encode     字符集编码
     * @return 签名值    签名的16进制表示字符串.
     */
    public static String sign(String content, PrivateKey privateKey, String encode) {
        try {
            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS_MD5withRSA);
            signature.initSign(privateKey);
            signature.update(content.getBytes(encode));
            byte[] signed = signature.sign();
            return byte2Hex(signed);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String sign(String content, PrivateKey privateKey) {
        try {
            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS_MD5withRSA);
            signature.initSign(privateKey);
            signature.update(content.getBytes());
            byte[] signed = signature.sign();
            return byte2Hex(signed);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 将byte[] 转换成字符串
     */
    public static String byte2Hex(byte[] srcBytes) {
        StringBuilder hexRetSB = new StringBuilder();
        for (byte b : srcBytes) {
            String hexString = Integer.toHexString(0x00ff & b);
            hexRetSB.append(hexString.length() == 1 ? 0 : "").append(hexString);
        }
        return hexRetSB.toString();
    }

    /**
     * RSA验签名检查
     *
     * @param content   待签名数据
     * @param sign      签名值
     * @param publicKey 分配给开发商公钥
     * @param encode    字符集编码
     * @return 布尔值
     */
    public static boolean doCheck(String content, String sign, PublicKey publicKey, String encode) {
        try {
            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS_MD5withRSA);

            signature.initVerify(publicKey);
            signature.update(content.getBytes(encode));

            boolean bverify = signature.verify(hex2Bytes(sign));
            return bverify;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean doCheck(String content, String sign, PublicKey publicKey) {
        try {
            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS_MD5withRSA);
            signature.initVerify(publicKey);
            signature.update(content.getBytes());
            boolean bverify = signature.verify(hex2Bytes(sign));
            return bverify;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    /**
     * 将16进制字符串转为转换成字符串
     */
    public static byte[] hex2Bytes(String source) {
        byte[] sourceBytes = new byte[source.length() / 2];
        for (int i = 0; i < sourceBytes.length; i++) {
            sourceBytes[i] = (byte) Integer.parseInt(source.substring(i * 2, i * 2 + 2), 16);
        }
        return sourceBytes;
    }


}