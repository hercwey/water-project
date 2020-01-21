package com.learnbind.ai.tax.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax.utils
 *
 * @Title: TestCase31.java
 * @Description: Case31测试.
 * 
  	（1）	需开据多张发票
	（2）	每张发票的额度不得超过100万元
	（3）	按水量进行划分，以避免出现在小数水量。
	（4）	除最后一张外，其它的发票金额最好相等。
	（5）	所开发票数最少。

 *
 * @author lenovo
 * @date 2019年12月8日 下午10:14:41
 * @version V1.0 
 *
 */
public class TestCase32 {
	
	/**
	 * @Title: testCase31
	 * @Description: 计算最少需要开具几张发票,及每张发票的商品数量
	 * @param price  单价
	 * @param amount  产品数量
	 * @param maxLimit  发票最大版面额度
	 * @return  每张发票中所开具的最大商品数量值
	 * 			>0时为正常
	 * 			<=0时为无法划分.
	 * 		
	 */
	private BigDecimal testCase32(BigDecimal price,BigDecimal amount,BigDecimal maxLimit) {
		BigDecimal sum=price.multiply(amount);
		
		NumberFormat currency = NumberFormat.getCurrencyInstance();//建立货币格式化引用
		sop("商品总金额为：\t" + currency.format(sum));
		/*
		 * NumberFormat percent = NumberFormat.getPercentInstance();
		 * percent.setMaximumFractionDigits(3); sop("货币格式：\t" +
		 * percent.format(0.2145555));
		 */
		
		int flag = sum.compareTo(maxLimit);  //合计与发票最大值比较.
		switch(flag) {
		case 0:  //相等
		case -1: //合计值小于单发票最大面额
			return amount;			
		case 1:  //大于
			break;		
		}
		
		//int num=getDecimalDegitalNum(price);  //小数位数个数.
		int scale=price.scale();
		System.out.println("价格的精度为:"+scale);
		//int num=getDecimalDegitalNum(price);  //小数位数个数.
		System.out.println("价格小数位数:"+scale);
		int increment=1;  //计算每次增量,以保证数量为整数.
		for(int i=0;i<scale;i++)
		{
			increment=increment*10;
		}
			
		
		BigDecimal issueAmount=new BigDecimal("0");
		if(price.multiply(new BigDecimal(increment)).compareTo(maxLimit)>0) {
			System.out.println("无法按数量整数进行划分!");
			//return issueAmount;
		}
		else {
			while (true) {
				BigDecimal temp=issueAmount.add(new BigDecimal(increment));
				if(temp.multiply(price).compareTo(maxLimit)==-1) {
					issueAmount=temp;
				}
				else {
					break;
				}
			}		
			System.out.println("每张发票的商品数量为:"+issueAmount.toString());
			//return issueAmount;
		}
		
		return issueAmount;  //单张发票开具商品数量
		
		
	}
	
	/**
	 * @Title: getDecimalNum 
	 * @Description: 获取小数位数个数  
	 * @param price  单价
	 * @return  小数位数个数
	 * 注:此函数暂时未用.
	 */
	/*
	private int getDecimalDegitalNum(BigDecimal price) {
		String moneyStr = String.valueOf(price);
	    String[] num = moneyStr.split("\\.");
	    if (num.length == 2) {
	        for (;;){
	            if (num[1].endsWith("0")) {
	                num[1] = num[1].substring(0, num[1].length() - 1);
	            }else {
	                break;
	            }
	        }
	        return num[1].length();
	    }else {
	        return 0;
	    }
	}
	*/
	
	public static void sop(Object s){
        System.out.println(s);
    }
	
	public static final void main(String[] args) {
		TestCase32 test31=new TestCase32();
		//注:此处采用字符定义BigDecimal.以保证精度.
		BigDecimal taxPrice=new BigDecimal("2.58");
		BigDecimal waterAmount=new BigDecimal("858798");
		BigDecimal maxLimit=new BigDecimal("1000000");
		BigDecimal taxRate=new BigDecimal("0.03");
		
		RoundingMode roundMode=RoundingMode.HALF_UP;
		
		//每张发票商品数量
		BigDecimal issueAmount= test31.testCase32(taxPrice,waterAmount,maxLimit);
		System.out.println("计算结果: 每张发票的商品数量为:"+issueAmount.toString());
		System.out.println("---------------------------------");
		if(issueAmount.compareTo(new BigDecimal("0"))==0) {
			System.out.println("无法划分.");
		}
		else {
			BigDecimal noTaxPrice=taxPrice.divide(taxRate.add(new BigDecimal("1")),18,roundMode);  //保留5位精度
			
			BigDecimal jeSum=new BigDecimal("0");
			BigDecimal seSum=new BigDecimal("0");
			BigDecimal roundSum=new BigDecimal("0");
			
			BigDecimal totalTax=noTaxPrice.multiply(waterAmount).multiply(taxRate);  //总税额
			
			System.out.println("总税额为:"+totalTax.setScale(2, roundMode));
			System.out.println("----------------------------------");
			
			
			//先确定发票数量.
			int invoiceNumUp=waterAmount.divide(issueAmount, 0, RoundingMode.UP).intValue();		//小值
			int invoiceNumDown=waterAmount.divide(issueAmount, 0, RoundingMode.DOWN).intValue();  	//大值
			
			for(int i=0;i<invoiceNumDown;i++) {
				BigDecimal je=taxPrice.multiply(issueAmount);  //单张发票开具金额
				System.out.println("单张No:"+(i+1));
				System.out.println("单张发票金额为:"+je);
				jeSum=jeSum.add(je);  //金额合计
				
				BigDecimal se=noTaxPrice.multiply(issueAmount).multiply(taxRate);  //单发票税额
				System.out.println("单张发票税额为:"+se.setScale(2, roundMode));
				seSum=seSum.add(se);  //税额合计
				
				roundSum=roundSum.add(se.setScale(2, roundMode));
				
			}
			System.out.println("求整开票金额合计为:"+jeSum.setScale(2, roundMode));
			System.out.println("求整开票税额合计为:"+seSum.setScale(2, roundMode));
			System.out.println("Round求整开票税额合计为:"+roundSum);
			System.out.println("---------------------------------");
			
			
			if(invoiceNumUp!=invoiceNumDown) {  //如果相等
				
				BigDecimal remainNum=waterAmount.subtract(issueAmount.multiply(new BigDecimal(invoiceNumDown)));
				
				BigDecimal remainJe=taxPrice.multiply(remainNum);  //剩余数量金额
				System.out.println("最后一张发票金额为:"+remainJe.setScale(2, roundMode));
				jeSum=jeSum.add(remainJe);  //金额合计
				
				BigDecimal remainSe=noTaxPrice.multiply(remainNum).multiply(taxRate);  //单发票税额
				seSum=seSum.add(remainSe);  //税额合计
				roundSum=roundSum.add(remainSe.setScale(2, roundMode));
				System.out.println("最后一张发票税额为:"+remainSe.setScale(2, roundMode));
				
				//修正最后一张发票的税额.
				BigDecimal roundRemainSe=remainSe.setScale(2, roundMode);  //四舍五入后税额(最后一张发票)
				BigDecimal mValue=roundSum.subtract(seSum);  //修正值
				mValue=mValue.setScale(2, roundMode);  //四舍五入修正值;
				System.out.println("修正值为:"+mValue.setScale(2, roundMode));
				if(mValue.compareTo(new BigDecimal("0"))< 0) {
					roundRemainSe=roundRemainSe.add(mValue.abs());
				}
				else {
					roundRemainSe=roundRemainSe.subtract(mValue.abs());
				}
				System.out.println("最后一张发票税额为(修正后):"+roundRemainSe.setScale(2, roundMode));
				
			}
			else {
				System.out.println("账单余额发票为:无");
			}
			
			System.out.println("---------------------------------");
			System.out.println("合计开票总金额为:"+jeSum.setScale(2, roundMode));
			System.out.println("合计开票总税额为:"+seSum.setScale(2, roundMode));
			System.out.println("Round合计开票总税额为:"+roundSum);
			
			BigDecimal sub=roundSum.subtract(seSum);  //按分配后发票四舍五入后的税额-按总额计算后的总税额			
			System.out.println("税额的差值是:"+sub);
			
			int flag = roundSum.compareTo(maxLimit);  //合计与发票最大值比较.
			switch(flag) {
			case 0:  //相等
				break;
			case -1: //合计值小于单发票最大面额
				break;
			case 1:  //大于
				break;		
			}
			
		}
		
	}
	
	
}
