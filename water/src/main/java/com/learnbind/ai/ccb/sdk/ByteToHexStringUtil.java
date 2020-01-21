package com.learnbind.ai.ccb.sdk;

public class ByteToHexStringUtil {
	/**
	 * CCB建设银行接口. 在密钥传输交换阶段，使用 DES 进行加解密前需要调用以下方法对 DES 的密钥进行转
	 * 换，即转换后的才是正真的密钥。该转换仅适用于密钥传输交换阶段，后续发送报文与 使用 HTTP 传输文件时的加解密阶段，以及使用 RSA
	 * 进行签名与验签阶段，均不需要 进行以下的转换。
	 */

	/**
	 * java 示例，其它语言可以参考修改 将 16 进制字符串转换成 16 进制数字数组
	 *
	 * @param hexString DES 的明文
	 * @return
	 */
	public static byte[] asc2bin(String hexString) {
		byte[] hexbyte = hexString.getBytes();
		byte[] bitmap = new byte[hexbyte.length / 2];
		for (int i = 0; i < bitmap.length; i++) {
			hexbyte[i * 2] -= hexbyte[i * 2] > '9' ? 7 : 0;
			hexbyte[i * 2 + 1] -= hexbyte[i * 2 + 1] > '9' ? 7 : 0;
			bitmap[i] = (byte) ((hexbyte[i * 2] << 4 & 0xf0) | (hexbyte[i * 2 + 1] & 0x0f));
		}
		return bitmap;

	}
	
	/**
	 * 辅助函数 将二进制转换成16进制
	 *
	 * @param buf
	 * @return
	 */
	public static String bytesToHexString(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
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
}
