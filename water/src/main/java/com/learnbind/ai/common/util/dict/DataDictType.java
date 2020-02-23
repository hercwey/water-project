package com.learnbind.ai.common.util.dict;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.learnbind.ai.controller.dict.DataDictController;

/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.ai.common.util.dict
 *
 * @Title: DataDictType.java
 * @Description: 数据字典类型类
 *
 * @author Administrator
 * @date 2019年5月13日 下午9:29:29
 * @version V1.0 
 *
 */
public class DataDictType {

	private static Log log = LogFactory.getLog(DataDictController.class);
	
	/**
	 * @Fields TRADE_TYPE：数据字典类型：行业性质
	 */
	public static final String TRADE_TYPE = "TRADE_TYPE";//行业性质
	/**
	 * @Fields WATER_USE：数据字典类型：水用途
	 */
	public static final String WATER_USE = "WATER_USE";//水用途
	/**
	 * @Fields WARTER_CALIBER：数据字典类型：水表口径
	 */
	public static final String WARTER_CALIBER = "WARTER_CALIBER";//水表口径
	/**
	 * @Fields METER_USE：数据字典类型：水表用途
	 */
	public static final String METER_USE = "METER_USE";//水表用途
	/**
	 * @Fields METER_TYPE：数据字典类型：水表类型
	 */
	public static final String METER_TYPE = "METER_TYPE";//水表类型
	/**
	 * @Fields METER_FACTORY：数据字典类型：水表型号
	 */
	public static final String METER_MODEL = "METER_MODEL";//水表型号
	/**
	 * @Fields METER_FACTORY：数据字典类型：生产厂家
	 */
	public static final String METER_FACTORY = "METER_FACTORY";//生产厂家
	/**
	 * @Fields BANK：数据字典类型：银行
	 */
	public static final String BANK = "BANK";//银行
	/**
	 * @Fields READ_MODE：数据字典类型：抄表方式
	 */
	public static final String READ_MODE = "READ_MODE";//抄表方式
	/**
	 * @Fields PAYMENT：数据字典类型：支付方式
	 */
	public static final String PAYMENT = "PAYMENT";//支付方式
	/**
	 * @Fields LOCAL_NODE_TYPE：数据字典类型：地理位置节点类型
	 */
	public static final String LOCAL_NODE_TYPE = "LOCAL_NODE_TYPE";//地理位置节点类型
	/**
	 * @Fields CLASSIFY_NODE_TYPE：统计分类节点类型
	 */
	public static final String CLASSIFY_NODE_TYPE = "CLASSIFY_NODE_TYPE";//统计分类节点类型
	/**
	 * @Fields LOCAL_NODE_TYPE：数据字典类型：工单类型
	 */
	public static final String WORK_ORDER_TYPE = "WORK_ORDER_TYPE";//工单类型
	
	/**
	 * @Fields LOCAL_NODE_TYPE：数据字典类型：知识库类型
	 */
	public static final String KNOW_LIBRARY_TYPE = "KNOW_LIBRARY_TYPE";//知识库类型
	/**
	 * @Fields LOCAL_NODE_TYPE：数据字典类型：知识库标签
	 */
	public static final String KNOW_LIBRARY_LABELS = "KNOW_LIBRARY_LABELS";//知识库标签
	
	/**
	 * @Fields WITHHOLD_FAIL_REASON：代扣失败原因
	 */
	public static final String WITHHOLD_FAIL_REASON = "WITHHOLD_FAIL_REASON";//代扣失败原因
	
	/**
	 * @Fields INVOICE_AMOUNT_VERSION_SPECIAL：专用发票额度版本
	 */
	public static final String INVOICE_AMOUNT_VERSION_SPECIAL = "SPECIAL_INVOICE_AMOUNT_VERSION";//专用发票额度版本
	
	/**
	 * @Fields INVOICE_AMOUNT_VERSION_NORMAL：普通发票额度版本
	 */
	public static final String INVOICE_AMOUNT_VERSION_NORMAL = "NORMAL_INVOICE_AMOUNT_VERSION";//普通发票额度版本
	
	/**
	 * @Fields PROTOCOL_TYPE：水表协议类型
	 */
	public static final String PROTOCOL_TYPE = "PROTOCOL_TYPE";//水表协议类型
	
	/**
	 * @Fields SAMPLE_UNIT：采样参数单位
	 */
	public static final String SAMPLE_UNIT = "SAMPLE_UNIT";//采样参数单位
	
	/**
	 * @Fields METER_USE_TYPE：水表用水类型
	 */
	public static final String METER_USE_TYPE = "METER_USE_TYPE";//水表用水类型
	
	/**
	 * @Fields METER_FACTORY_CODE：水表厂商代码
	 */
	public static final String METER_FACTORY_CODE = "METER_FACTORY_CODE";//水表厂商代码
	
	/**
	 * @Fields dictTypeList：数据字典类型集合
	 */
	private static List<Map<String, String>> dictTypeList = new ArrayList<>();
	
	/**
	 * @Fields prop：new Properties
	 */
	private static Properties prop = new Properties();
	static {
		
		/** try{
			//properties文件路径
			String path = "/config/dict-type-config.properties";
			//读取属性文件properties
			FileInputStream is = new FileInputStream(path);
	        InputStreamReader reader = new InputStreamReader(is, "UTF-8");
	        prop.load(reader);
	        
			Iterator<String> it = prop.stringPropertyNames().iterator();
			while (it.hasNext()) {
				String key = it.next();
				String value = prop.getProperty(key);
				System.out.println(key + ":" + value);
				Map<String, String> dictType = new HashMap<>();
				dictType.put("code", key);
				dictType.put("name", value);
				dictTypeList.add(dictType);
			}

		}catch(Exception e){
			e.printStackTrace();
			log.info("----------读取配置文件异常！----------", e);
		} **/
		
		Map<String, String> dictType = new HashMap<>();
		dictType.put("code", "TRADE_TYPE");
		dictType.put("name", "行业性质");
		dictTypeList.add(dictType);
		Map<String, String> dictType1 = new HashMap<>();
		dictType1.put("code", "WATER_USE");
		dictType1.put("name", "用水性质");
		dictTypeList.add(dictType1);
		Map<String, String> dictType2 = new HashMap<>();
		dictType2.put("code", "WARTER_CALIBER");
		dictType2.put("name", "水表口径");
		dictTypeList.add(dictType2);
		Map<String, String> dictType3 = new HashMap<>();
		dictType3.put("code", "METER_USE");
		dictType3.put("name", "水表用途");
		dictTypeList.add(dictType3);
		Map<String, String> dictType4 = new HashMap<>();
		dictType4.put("code", "METER_TYPE");
		dictType4.put("name", "水表类型");
		dictTypeList.add(dictType4);
		Map<String, String> dictType5 = new HashMap<>();
		dictType5.put("code", "METER_MODEL");
		dictType5.put("name", "水表型号");
		dictTypeList.add(dictType5);
		Map<String, String> dictType6 = new HashMap<>();
		dictType6.put("code", "METER_FACTORY");
		dictType6.put("name", "生产厂家");
		dictTypeList.add(dictType6);
		Map<String, String> dictType7 = new HashMap<>();
		dictType7.put("code", "BANK");
		dictType7.put("name", "银行");
		dictTypeList.add(dictType7);
		Map<String, String> dictType8 = new HashMap<>();
		dictType8.put("code", "READ_MODE");
		dictType8.put("name", "抄表方式");
		dictTypeList.add(dictType8);
		Map<String, String> dictType9 = new HashMap<>();
		dictType9.put("code", "PAYMENT");
		dictType9.put("name", "支付方式");
		dictTypeList.add(dictType9);
		Map<String, String> dictType10 = new HashMap<>();
		dictType10.put("code", "LOCAL_NODE_TYPE");
		dictType10.put("name", "地理位置节点类型");
		dictTypeList.add(dictType10);
		Map<String, String> dictType11 = new HashMap<>();
		dictType11.put("code", "WOK_ORDER_TYPE");
		dictType11.put("name", "工单类型");
		dictTypeList.add(dictType11);
		Map<String, String> dictType12 = new HashMap<>();
		dictType12.put("code", "KNOW_LIBRARY_TYPE");
		dictType12.put("name", "知识库类型");
		dictTypeList.add(dictType12);
		Map<String, String> dictType13 = new HashMap<>();
		dictType13.put("code", "KNOW_LIBRARY_LABELS");
		dictType13.put("name", "知识库标签");
		dictTypeList.add(dictType13);
		Map<String, String> dictType14 = new HashMap<>();
		dictType14.put("code", "WITHHOLD_FAIL_REASON");
		dictType14.put("name", "代扣失败原因");
		dictTypeList.add(dictType14);
		Map<String, String> dictType15 = new HashMap<>();
		dictType15.put("code", "SPECIAL_INVOICE_AMOUNT_VERSION");
		dictType15.put("name", "专用发票额度版本");
		dictTypeList.add(dictType15);
		Map<String, String> dictType16 = new HashMap<>();
		dictType16.put("code", "NORMAL_INVOICE_AMOUNT_VERSION");
		dictType16.put("name", "普通发票额度版本");
		dictTypeList.add(dictType16);
		Map<String, String> dictType17 = new HashMap<>();
		dictType17.put("code", "CLASSIFY_NODE_TYPE");
		dictType17.put("name", "统计分类节点类型");
		dictTypeList.add(dictType17);
		
		Map<String, String> dictType18 = new HashMap<>();
		dictType18.put("code", "PROTOCOL_TYPE");
		dictType18.put("name", "水表协议类型");
		dictTypeList.add(dictType18);
		Map<String, String> dictType19 = new HashMap<>();
		dictType19.put("code", "SAMPLE_UNIT");
		dictType19.put("name", "采样参数单位");
		dictTypeList.add(dictType19);
		Map<String, String> dictType20 = new HashMap<>();
		dictType20.put("code", "METER_USE_TYPE");
		dictType20.put("name", "水表用水类型");
		dictTypeList.add(dictType20);
		Map<String, String> dictType21 = new HashMap<>();
		dictType21.put("code", "METER_FACTORY_CODE");
		dictType21.put("name", "水表厂商代码");
		dictTypeList.add(dictType21);
	}

	/**
	 * main方法测试
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(getValue("BANK"));
		System.out.println(getDictTypeList());
		
	}

	/**
	 * @Title: getValue
	 * @Description: 根据数据字典类型编码获取名称
	 * @param key
	 * @return 
	 */
	public static String getValue(String key){ 
		return prop.getProperty(key); 
	}
	
	/**
	 * @Title: getDictTypeList
	 * @Description: 获取数据字典类型集合
	 * @return 
	 */
	public static List<Map<String, String>> getDictTypeList(){
		return dictTypeList;
	}
	 
}
