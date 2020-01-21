package com.learnbind.ai.ccb;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.learnbind.ai.ccb.business.CCBBusiness;

public class TestReg {
	private static final Logger logger = LoggerFactory.getLogger(CCBBusiness.class);

	public static void  main(String[] args) {
		String strx="                      中国民生银行系统内代发代扣成功清单\r\n" + 
				"\r\n" + 
				"交易日期  ：2019-04-23              资金编号    ：2019042310010303B3FF\r\n" + 
				"记账方式  ：代扣                    业务类型    ：00201\r\n" + 
				"代理商账户：699909093               代理商户名称：石家庄高新技术产业开发区供水排水公司\r\n" + 
				"总笔数    ：8                       总金额      ：RMB388.68\r\n" + 
				"\r\n" + 
				"客户账号          客户名称            金额           \r\n" + 
				"=================================================================================\r\n" + 
				"6216911005281626  高超蒙              37.92          \r\n" + 
				"6226191004704625  耿建                47.40          \r\n" + 
				"6226191004704484  孟立                56.88          \r\n" + 
				"6226191004701340  王盼                23.70          \r\n" + 
				"6216911005281493  魏长                71.10          \r\n" + 
				"6226191004695682  夏春展              52.14          \r\n" + 
				"6226191004700979  张贵存              14.22          \r\n" + 
				"6216911005281485  左晓胜              85.32          \r\n" + 
				"=================================================================================\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"	\r\n" + 
				"                      中国民生银行系统内代发代扣失败清单\r\n" + 
				"\r\n" + 
				"交易日期  ：2019-04-23              资金编号    ：2019042310010303B3FF\r\n" + 
				"记账方式  ：代扣                    业务类型    ：00201\r\n" + 
				"代理商账户：699909093               代理商户名称：石家庄高新技术产业开发区供水排水公司\r\n" + 
				"总笔数    ：10                      总金额      ：RMB421.86\r\n" + 
				"\r\n" + 
				"客户账号          客户名称            金额           失败原因\r\n" + 
				"=================================================================================\r\n" + 
				"6226191004696326  安晓峰              75.84          可用余额小于要扣款金额\r\n" + 
				"6226191004701084  陈磊                18.96          账户通可调余额不足\r\n" + 
				"6226191003111251  李广惠              56.88          可用余额小于要扣款金额\r\n" + 
				"6216911005281030  梁浩帅              4.74           可用余额小于要扣款金额\r\n" + 
				"6226191004701043  任娟粉              14.22          可用余额小于要扣款金额\r\n" + 
				"6226191004701290  魏亚京              23.70          可用余额小于要扣款金额\r\n" + 
				"6216911005281154  徐龙                113.76         可用余额小于要扣款金额\r\n" + 
				"6226191004704658  张朝                42.66          可用余额小于要扣款金额\r\n" + 
				"6216911005281576  张奇苗              28.44          可用余额小于要扣款金额\r\n" + 
				"6216911005281113  张瑞欣              42.66          可用余额小于要扣款金额\r\n" + 
				"=================================================================================";
		
		Pattern pattern = Pattern.compile("=+\r\n([^=]+)=+");
		Matcher matcher = pattern.matcher(strx);
		StringBuffer buffer = new StringBuffer();
		while(matcher.find()){
			
			logger.info("match:"+matcher.group(1));
			String tempStr=matcher.group(1);
			String[] strArr=tempStr.split("\r\n");
			for(String str:strArr) {
				logger.info(str);
			}
			
		    //buffer.append(matcher.group());
			
		    //buffer.append("/r/n");              
		    //System.out.println(buffer.toString());
		}
		
		//logger.info("test ");
		
	}
}
