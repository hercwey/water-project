package com.learnbind.ai.iot.protocol.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BCDUtil {

	/**
	 * BCD字节数组 ===> String
	 * 
	 * @param bytes
	 * @return 十进制字符串
	 */
	public static String bcd2String(byte[] bytes) {

		// 由于网络数据为低地址在前, 高地址在后, 解析时需要先将数组数据反序
		byte[] buffer = new byte[bytes.length];
		for(int i = bytes.length-1;i >= 0;i--){
			buffer[bytes.length-i-1] = bytes[i];
		}

		StringBuilder temp = new StringBuilder(buffer.length * 2);
		for (int i = 0; i < buffer.length; i++) {
			// 高四位
			temp.append((buffer[i] & 0xf0) >>> 4);
			// 低四位
			temp.append(buffer[i] & 0x0f);
		}

		return temp.toString().substring(0, 1).equalsIgnoreCase("0") ? temp.toString().substring(1) : temp.toString();
	}

	/**
	 * 字符串 ==> BCD字节数组
	 * 
	 * @param str 十进制字符串
	 * @return BCD字节数组
	 */
	public static byte[] string2Bcd(String str) {

		// 奇数,前补零
		if ((str.length() & 0x1) == 1) {
			str = "0" + str;
		}

		byte buffer[] = new byte[str.length() / 2];
		byte bs[] = str.getBytes();

		// 高字节在前, 低字节在后
		for (int i = 0; i < buffer.length; i++) {

			byte high = ascII2Bcd(bs[2 * i]);
			byte low = ascII2Bcd(bs[2 * i + 1]);

			buffer[i] = (byte) ((high << 4) | low);
		}

		// 由于转换到网络数据传输时, 低地址在前, 高地址在后, 因此需要将数组顺序反转
		byte[] ret = new byte[buffer.length];
		for(int i = buffer.length-1;i >= 0;i--){
			ret[buffer.length-i-1] = buffer[i];
		}

		return ret;
	}

	public static void setBcdBytes(byte[] bytes, String str){

		// 判断目的缓冲区长度是否足够
		if (bytes.length * 2 < str.length()){
			throw new IndexOutOfBoundsException("Byte array too short!");
		}

		byte[] tmp = string2Bcd(str);
		System.arraycopy(tmp, 0, bytes, 0, tmp.length);
	}

	/*
	public static void setBcdBytesByTimeString(byte[] bytes, String timeString) throws Exception{
		Date date = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		date = formatter.parse(timeString);

		int year = date.getYear();
		int month = date.getMonth();
		int day = date.getDay();
		int hour = date.getHours();
		int min = date.getMinutes();
		int sec = date.getSeconds();

		byte[]
	}

	public static String bcd2TimeString(byte[] bytes){

	}
*/
	private static byte ascII2Bcd(byte asc) {
		if ((asc >= '0') && (asc <= '9'))
			return (byte) (asc - '0');
		else if ((asc >= 'A') && (asc <= 'F'))
			return (byte) (asc - 'A' + 10);
		else if ((asc >= 'a') && (asc <= 'f'))
			return (byte) (asc - 'a' + 10);
		else
			return (byte) (asc - 48);
	}
}
