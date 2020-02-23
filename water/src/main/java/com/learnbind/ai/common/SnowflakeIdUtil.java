package com.learnbind.ai.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.learnbind.ai.service.wechat.pay.SnowflakeIdWorker;

public class SnowflakeIdUtil {
	
	private static SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);
	
	/**
	 * @Title: genYYYYMMDDAndId
	 * @Description: 获取唯一ID
	 * @return 
	 * 		yyyyMMdd+SnowflakeId
	 */
	public static String genYYYYMMDDAndId() {
		//DateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance();
		String dateName = df.format(calendar.getTime());
		Long snowflakeId=idWorker.nextId();
		String id=dateName+snowflakeId.toString();
		return id;
	}
}
