package com.learnbind.ai.jsengine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.jsengine
 *
 * @Title: PartitionTest.java
 * @Description: 分水量规则工具类
 *
 * @author lenovo
 * @date 2019年9月27日 下午3:05:47
 * @version V1.0
 *
 */
@Service
public class PartitionRuleUtil {
	private static final Logger logger = LoggerFactory.getLogger(PartitionRuleUtil.class);
	public final static String METER_PARAM_PREFIX="meter_";   //表计参数前缀

	/**
	 * @Title: getRuleParams
	 * @Description: 自规则中析取形式变量
	 * @param rule  规则(带有形式参数的业务规则)
	 * @return
	 */
	public List<String> getRuleFormalParams(String rule) {		
		List<String> paramList = new ArrayList<>();		
		String pattern = "("+ METER_PARAM_PREFIX+"[\\d]+)";  //表计元素正则表达式  meter_xxx
		// 创建 Pattern 对象
		Pattern r = Pattern.compile(pattern);

		// 现在创建 matcher 对象
		Matcher m = r.matcher(rule);		
		while(m.find()){			
			//logger.info("match:"+m.group(1));
			String param=m.group(1);
			paramList.add(param);
			//System.out.println("meter is:"+tempStr);
		}
		return paramList;
	}
	
	/**
	 * 
	 * @Title: getMeterIndentify
	 * @Description: 返回表计ID
	 * @param meterParam  表计参数变量
	 * @return 返回表计indentify(表计在系统中的ID,或是表计编号,可以唯一确定表计)
	 * 		如果有ID部分则返回
	 * 		否则返回null
	 */
	public String getMeterIndentify(String meterParam) {		
		String indentify=null;
		
		String pattern = "([\\d]+)";  //表计元素正则表达式  meter_xxx
		// 创建 Pattern 对象
		Pattern r = Pattern.compile(pattern);

		// 现在创建 matcher 对象
		Matcher m = r.matcher(meterParam);		
		while(m.find()){			
			//logger.info("match:"+m.group(1));
			String param=m.group(1);
			indentify=param;
			//System.out.println("meter is:"+tempStr);
		}
		
		return indentify;
	}
	
	/**
	 * 
	 * @Title: setRuleActualParams
	 * @Description: 将实际参数设置到规则中.
	 * @param actualParamList  实际参数列表
	 * 					形式参数名称1----->参数值1
	 * 					形式参数名称2----->参数值2
	 * @param rule  带有形式参数的规则
	 * @return  替换为实际参数的规则
	 * 
	 * 注:  actualParamList 实际参数列表
	 */
	public String setRuleActualParams(Map<String,String> actualParamMap,String rule) {
		String actualRule=rule;
		for(String key:actualParamMap.keySet()) {
			actualRule=actualRule.replace(key, actualParamMap.get(key));
		}		
		return actualRule;		
	}
	
	/**
	 * 
	 * @Title: validateRule
	 * @Description: 对规则进行校验
	 * @param ruleStr  规则字符串
	 * @return 如果验证通过则返回true,否则返回false
	 */
	public boolean validateRule(String ruleStr) {
		int testData=100;
		boolean ruleValid=false;
		//(1)形式参数列表
		List<String> formalParamList=getRuleFormalParams(ruleStr);
		//(2)形-实结合
		Map<String,String> actualMap=new HashMap<>();
		for(String formalParam:formalParamList) {
			actualMap.put(formalParam,Integer.toString(testData));
			testData=testData+100;
		}		
		String actualRule=setRuleActualParams(actualMap,ruleStr);  //设置实际参数后的规则
		
		//(3)开始进行校验  采用JS ENGINE校验正确性.
		JSEngineUtils jsEngine=new JSEngineUtils();    	
    	try {
    		System.out.println("校验的规则为:"+actualRule);
			jsEngine.start(actualRule);
			ruleValid=true;
			logger.debug("校验的规则为:"+actualRule+"  校验结果为:"+ruleValid);
		} catch (ScriptException e) {			
			ruleValid=false;
			logger.debug("校验的规则为:"+actualRule+"  校验结果为:"+ruleValid);
			//e.printStackTrace();
		}		
		return ruleValid;
	}
	
	/**
	 * @Title: getRealExpression
	 * @Description: 获取表达式
	 * @param rule
	 * @return 
	 */
	public String getRealExpression(String rule) {
		logger.debug("输入的规则："+rule);
		String realExpression = StringUtils.replace(rule, ",", "");
		logger.debug("输出的规则表达式："+realExpression);
		return realExpression;
	}
	
	//------------------test function----------------
	
	/**
	 * @Title: testGetMeterIndentify
	 * @Description: 测试获取表计indentify
	 */	
	private void testGetMeterIndentify() {
		String indentify=getMeterIndentify("meter_100");
		System.out.println("meter indentify is:"+indentify);
	}
	
	/**
	 * @Title: testGetRuleParams
	 * @Description: 测试析取表计变量
	 */
	private void testGetRuleFormalParams(){
		String partitionRule = "(meter_100+meter_200)*0.75";  //原始表计表达式
		PartitionRuleUtil partTest=new PartitionRuleUtil();
		List<String> paramList=partTest.getRuleFormalParams(partitionRule);
		for(String param:paramList) {
			System.out.println("param is:"+param);
		}
	}
	
	/**
	 * 
	 * @Title: testsetRuleActualParams
	 * @Description: 设置规则的实际参数
	 */
	private void testSetRuleActualParams() {
		String partitionRule = "(meter_100+meter_200)*0.75";
		
		Map<String,String> actualMap=new HashMap<>();
		actualMap.put("meter_100", "50");
		actualMap.put("meter_200", "60");
		String actualRule=setRuleActualParams(actualMap,partitionRule);  //设置实际参数后的规则
		System.out.println("actual rule is:"+actualRule);
		
	}
	
	/**
	 * 
	 * @Title: testExecJS
	 * @Description: 测试执行javascript 
	 */
	private void testExecJS() {
		String script="(100+300)*0.75";
		JSEngineUtils jsEngine = new JSEngineUtils();
		try {
			Double result=(Double)jsEngine.start(script);
			System.out.println("calc reslt is:"+result);
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		//JSEngineUtils jsEngine = new JSEngineUtils();		
		PartitionRuleUtil partTest=new PartitionRuleUtil();
		partTest.testGetRuleFormalParams();
		//partTest.testSetRuleActualParams();
		//partTest.testExecJS();
		//partTest.testGetMeterIndentify();
		partTest.getRealExpression("meter_63493,-,100,*,0");
	}

}
