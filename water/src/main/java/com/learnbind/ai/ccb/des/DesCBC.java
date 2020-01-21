package com.learnbind.ai.ccb.des;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import com.learnbind.ai.common.DateUtil;

/**
 * DES加解密工具
 * 
 * @author lenovo
 *
 */
public class DesCBC {
	// 算法名称
	public static final String KEY_ALGORITHM = "DES";
	// 算法名称/加密模式/填充方式
	public static final String CIPHER_ALGORITHM_ECB = "DES/ECB/PKCS5Padding";
	public static final String CIPHER_ALGORITHM_CBC = "DES/CBC/PKCS5Padding";  //使用的模式

	/**
	 * 加密
	 * 
	 * @param data
	 * @param sKey
	 * @return
	 */
	public static byte[] encrypt(byte[] data, String sKey) {
		try {
			byte[] key = sKey.getBytes();
			// 初始化向量
			IvParameterSpec iv = new IvParameterSpec(key);
			DESKeySpec desKey = new DESKeySpec(key);
			// 创建一个密匙工厂，然后用它把DESKeySpec转换成securekey
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
			SecretKey securekey = keyFactory.generateSecret(desKey);
			// Cipher对象实际完成加密操作
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC);
			// 用密匙初始化Cipher对象
			cipher.init(Cipher.ENCRYPT_MODE, securekey, iv);
			// 现在，获取数据并加密
			// 正式执行加密操作
			return cipher.doFinal(data);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解密
	 * 
	 * @param src
	 * @param sKey
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] src, String sKey) throws Exception {
		byte[] key = sKey.getBytes();
		// 初始化向量
		IvParameterSpec iv = new IvParameterSpec(key);
		// 创建一个DESKeySpec对象
		DESKeySpec desKey = new DESKeySpec(key);
		// 创建一个密匙工厂
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
		// 将DESKeySpec对象转换成SecretKey对象
		SecretKey securekey = keyFactory.generateSecret(desKey);
		// Cipher对象实际完成解密操作
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC);
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.DECRYPT_MODE, securekey, iv);
		// 真正开始解密操作
		return cipher.doFinal(src);
	}
	
	
	

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
	public static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex);
		}
		return sb.toString();
	}

	/**
	 * 辅助函数 将16进制转换为二进制
	 *
	 * @param hexStr
	 * @return
	 */
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

	/**
	 * TODO 用于实际项目中. 测试状态:已经测试 生成通用KEY,用于交换密钥时使用
	 * 
	 * @param customerNo 客户号
	 * @return
	 */
	public static String genCommonKey(String customerNo) {
		String firstPart = "";
		if (customerNo.length() >= 10) {
			firstPart = customerNo.substring(customerNo.length() - 10);
		} else {
			firstPart = String.format("%10s", customerNo);
			firstPart = firstPart.replace(" ", "0");
		}
		String dateStr = DateUtil.format(new Date(), "yyMMdd");
		return firstPart + dateStr;
	}

	/**
	 * 解密加密的KEY
	 * 
	 * @param encryptKey 采用DES加密的key
	 * @param decryptKey 解密密钥 服务方用 使用 DES 算法对密钥加密，DES 密钥约定为：企业客户号+ 交换当日日期（YYMMDD 6 ）
	 *                   位）。 客户方收到密钥数据后，用相同的 DES 密钥解密，即可得到服务方的密钥。其中企业 客户号取后 10
	 *                   位，不足10 位的，前面补 0；
	 * @return 解密后的密钥
	 * 
	 *         注:在密钥交换阶段,加密KEY与解密KEY相同
	 */
	public static String decryptKey(byte[] encryptKey, String decryptKey) {
		String key = "";
		try {

			// 解密时需要经过以下几个步骤
			// (1)解密前转换
			// 将收到的字节数组进行转换成16进制字符串,而后采用asc2bin函数进行转换
			String hexStr = DesCBC.parseByte2HexStr(encryptKey);
			// System.out.println("加密字符串是:" + hexStr);
			byte[] transArr = DesCBC.asc2bin(hexStr); // 项目中要求这样处理 加密前
			// (2)采用密钥对接收到的内容解密
			byte[] decryptArr = DesCBC.decrypt(transArr, decryptKey);
			// (3)解密结果
			// display the encrypt result
			key = new String(decryptArr);

			// System.out.println("解密后:" + decryResultStr);

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return key;
	}

	/**
	 * 加密key
	 * 
	 * @param key        普通字符串
	 * @param encryptKey 加密所使用的key 服务方用 使用 DES 算法对密钥加密，DES 密钥约定为：企业客户号+
	 *                   交换当日日期（YYMMDD 6 ） 位）。 客户方收到密钥数据后，用相同的 DES
	 *                   密钥解密，即可得到服务方的密钥。其中企业 客户号取后 10 位，不足10 位的，前面补 0；
	 * @return 加密后的密钥byte[]
	 * 
	 *         注:在密钥交换阶段,加密KEY与解密KEY相同
	 */
	public static byte[] encryptKey(String key, String encryptKey) {
		String hexStrEncrypt = DesCBC.parseByte2HexStr(key.getBytes());
		byte[] encryptArr = DesCBC.asc2bin(hexStrEncrypt); // 项目中要求这样处理 加密前
		// (2)采用密钥对以上内容进行加密
		byte[] encryptResultArr = DesCBC.encrypt(encryptArr, encryptKey); // 加密后的数组
		return encryptResultArr;
	}

	private static final String SKEY = "12345678";
	// TODO 可以根据实际情况修改编码
	private static final Charset CHARSET = Charset.forName("UTF-8");

	/**
	 * 
	 * TODO 可用于实际的项目中.此处模拟密钥的传输过程. 项目相关的测试(密码交换部分)
	 * 
	 * @throws UnsupportedEncodingException
	 */
	private static void test3() throws UnsupportedEncodingException {
		// 待加密内容
		String content = "123456abcdefghijklmnopqrstuvwxyz汉字12333!@#!#@!@#!@#";
		System.out.println("加密前:" + content);

		// 加密需要经过以下两个步骤
		// (1)加密前进行转换.(项目要求)
		// 将需要发送的内容转换成16进制字符串,而后采用asc2bin函数进行转换.
		// ASC->HEX->asc2bin转换
		String hexStrEncrypt = DesCBC.parseByte2HexStr(content.getBytes());
		byte[] encryptArr = DesCBC.asc2bin(hexStrEncrypt); // 项目中要求这样处理 加密前
		// (2)采用密钥对以上内容进行加密
		byte[] encryptResultArr = DesCBC.encrypt(encryptArr, SKEY); // 加密后的数组

		try {

			// 解密时需要经过以下几个步骤
			// (1)解密前转换
			// 将收到的字节数组进行转换成16进制字符串,而后采用asc2bin函数进行转换
			String hexStr = DesCBC.parseByte2HexStr(encryptResultArr);
			System.out.println("加密字符串是:" + hexStr);
			byte[] transArr = DesCBC.asc2bin(hexStr); // 项目中要求这样处理 加密前
			// (2)采用密钥对接收到的内容解密
			byte[] decryResultArr = DesCBC.decrypt(transArr, SKEY);
			// (3)解密结果
			// display the encrypt result
			String decryResultStr = new String(decryResultArr);

			System.out.println("解密后:" + decryResultStr);

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		// System.out.println(decryResult);
	}
	
	
	/**
	 * 可用于实际的项目中.此处模拟密钥的传输过程. 项目相关的测试(密码交换部分)
	 * 已经测试.
	 * added by hz   2019/07/08
	 * @throws Exception
	 */
	private static void test6() throws Exception {
		// 待加密内容
		String content = "123456abcdefghijklmnopqrstuvwxyz汉字12333!@#!#@!@#!@#";
		System.out.println("加密前:" + content);

		// 加密需要经过以下两个步骤
		// (1)加密前进行转换.(项目要求)
		// 将需要发送的内容转换成16进制字符串,而后采用asc2bin函数进行转换.
		// ASC->HEX->asc2bin转换
		String hexStrEncrypt = DesCBC.parseByte2HexStr(content.getBytes());
		byte[] encryptArr = DesCBC.asc2bin(hexStrEncrypt); // 项目中要求这样处理 加密前
		// (2)采用密钥对以上内容进行加密
		//byte[] encryptResultArr = Des.encrypt(encryptArr, SKEY); // 加密后的数组
		byte[] encryptResultArr= DesECBUtil.encryptDES2Byte(encryptArr, SKEY);

		try {

			// 解密时需要经过以下几个步骤
			// (1)解密前转换
			// 将收到的字节数组进行转换成16进制字符串,而后采用asc2bin函数进行转换
			String hexStr = DesCBC.parseByte2HexStr(encryptResultArr);
			System.out.println("加密字符串是:" + hexStr);
			byte[] transArr = DesCBC.asc2bin(hexStr); // 项目中要求这样处理 加密前
			// (2)采用密钥对接收到的内容解密
			byte[] decryResultArr = DesECBUtil.decryptDES(transArr, SKEY);
			// (3)解密结果
			// display the encrypt result
			String decryResultStr = new String(decryResultArr);

			System.out.println("解密后:" + decryResultStr);

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		// System.out.println(decryResult);
	}

	/**
	 * 非密码交换阶段的DES加密
	 */
	private static void test4() {
		// 待加密内容
		String content = "123456abcdefghijklmnopqrstuvwxyz汉字12333!@#!#@!@#!@#";
		System.out.println("加密前:" + content);

		// (1)加密
		byte[] encryptArr = content.getBytes();
		byte[] encryptResultArr = DesCBC.encrypt(encryptArr, SKEY); // 加密后的数组

		System.out.println("加密后:" + new String(encryptResultArr));

		try {

			// (2)采用密钥对接收到的内容解密
			byte[] decryResultArr = DesCBC.decrypt(encryptResultArr, SKEY);
			// (3)解密结果
			String decryResultStr = new String(decryResultArr);
			System.out.println("解密后:" + decryResultStr); // display the encrypt result

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		// System.out.println(decryResult);
	}

	/**
	 * 注:des加密的密码长度至少是8个字节,否则无效.
	 * 
	 * @throws UnsupportedEncodingException
	 */
	private static void test5() throws UnsupportedEncodingException {
		String key = "12345678";
		// 待加密内容
		String content = "123456abcdefghijklmnopqrstuvwxyz汉字12333!@#!#@!@#!@#";
		System.out.println("加密前:" + content);

		byte[] encryptArr = DesCBC.encryptKey(content, key);
		System.out.println("加密后:" + new String(encryptArr, "UTF-8"));

		String decryptStr = DesCBC.decryptKey(encryptArr, key);
		System.out.println("解密后:" + decryptStr);
	}

	public static void main(String[] args) throws Exception {
		// test1();
		// test2();
		// test3();
		// test4();

		//test5();
		
		test6();

		// String str=genCommonKey("123456789");
		// System.out.println("common key is:"+str);
	}
}
