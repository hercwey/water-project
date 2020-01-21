package com.learnbind.ai.controller.wechat;

import java.math.BigDecimal;

import com.learnbind.ai.common.util.BigDecimalUtils;

public class TestBigDecimal {

	public static void main(String[] args) {
		//BigDecimal x=new BigDecimal(1.25);
		//String xStr=x.multiply(new BigDecimal(100)).toPlainString();
		
		BigDecimal x=BigDecimalUtils.multiply(new BigDecimal(1.24),new BigDecimal(100),0);
		String strx=x.toPlainString();
		
		
		System.out.println(strx);
	}
}
