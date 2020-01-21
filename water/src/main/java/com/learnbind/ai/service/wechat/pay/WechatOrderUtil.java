package com.learnbind.ai.service.wechat.pay;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class WechatOrderUtil {
	
	/**
	 * 采用时间+随机数生成订单号
	 * @return
	 */
	public static String genOrderNo() {
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar calendar = Calendar.getInstance();
		String dateName = df.format(calendar.getTime());

		Random ne = new Random();// 实例化一个random的对象ne
		int x = ne.nextInt(999 - 100 + 1) + 100;// 为变量赋随机值100-999
		String random_order = String.valueOf(x);
		String order_id = dateName + random_order;
		return order_id;
	}
	
	
	/**
	 * 采用时间+随机数生成订单号
	 * @return
	 */
	public static String getOrderIdByTime() {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String newDate = sdf.format(new Date());
			String result = "";
			Random random = new Random();
			for (int i = 0; i < 3; i++) {
				result += random.nextInt(10);
			}
			return newDate + result;
	}
	
	
	private static SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);
	
	/**
	 * 采用日期+Snowflake生成订单号 以保证订单号的唯一性.
	 * 
	 * 采用  当前日期(格式为:yyyyMMddHHmm)-Snowflake生成的序列号做为id    yyyyMMddHHmm-123456789012345678 
	 * 其中日期部分的长度为12位  Snowflake的所生成ID的长度为: 18位  中间的分隔符长度为1位   合计为31位.
	 *  
	 * @return  长度为31位的字符
	 */
	public static String genOrderIdBySnowflake() {
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
		Calendar calendar = Calendar.getInstance();
		String dateName = df.format(calendar.getTime());
		Long id=idWorker.nextId();
		String orderNo=dateName+"-"+id.toString();
		return orderNo;
	}
	
	
	/**
	 * added by hz 2019/12/27
	 * @Title: genOrderIdBySnowflakeCCB
	 * @Description: 生成ccbWechat定单ID  30字符长度
	 * @return  yyyyMMddHHmm123456789012345678  前面是年月日,后面是flake生成的18位的长度.
	 */
	public static String genOrderIdBySnowflakeCCB() {
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
		Calendar calendar = Calendar.getInstance();
		String dateName = df.format(calendar.getTime());
		Long id=idWorker.nextId();
		String orderNo=dateName+id.toString();
		return orderNo;
	}

}
