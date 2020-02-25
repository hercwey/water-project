package com.learnbind.ai.service.business;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.learnbind.ai.common.util.BigDecimalUtils;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.constant.WaterPriceConstant;
import com.learnbind.ai.model.CustomerPeopleAdjust;
import com.learnbind.ai.model.SysDiscount;
import com.learnbind.ai.model.SysWaterPrice;
import com.learnbind.ai.model.UseDiscountTrace;
import com.learnbind.ai.model.UsePeopleAdjustTrace;
import com.learnbind.ai.model.UseWaterPriceTrace;

/**
 * Copyright (c) 2020 by SRD
 * 
 * @Package com.learnbind.ai.service.business
 *
 * @Title: CalculatingWaterFeeBusiness.java
 * @Description: 计算水费
 *
 * @author SRD
 * @date 2020年2月24日 上午12:31:11
 * @version V1.0 
 *
 */
@Service
public class CalculatingWaterFeeBusiness {

	/**
	 * @Fields log：日志
	 */
	private static final Logger log = LoggerFactory.getLogger(CalculatingWaterFeeBusiness.class);

	
	/**
	 * @Fields JMSHYS：居民生活用水key值
	 */
	public static final String JMSHYS = WaterPriceConstant.JMSHYS;
	/**
	 * @Fields JSON_USE_WATER_PRICE_TRACE：使用水价日志JSON
	 */
	public static final String JSON_USE_WATER_PRICE_TRACE = "useWaterPriceTraceJSON";
	/**
	 * @Fields JSON_USE_PEOPLE_ADJUST_TRACE：使用多人口调整日志JSON
	 */
	public static final String JSON_USE_PEOPLE_ADJUST_TRACE = "usePeopleAdjustTraceJSON";
	/**
	 * @Fields JSON_USE_DISCOUNT_TRACE：使用政策减免日志JSON
	 */
	public static final String JSON_USE_DISCOUNT_TRACE = "useDiscountTraceJSON";
	
	/**
	 * @Title: isMeterPrice
	 * @Description: 是否使用表计价格
	 * @param meterWaterUse		表计的用水性质
	 * @param meterPriceCode	表计的价格编码
	 * @return 
	 */
	public boolean isMeterPrice(String meterWaterUse, String meterPriceCode) {
		//如果表计价格分类不为空，且价格分类为居民生活用水或水价不为空，则使用表计价格
		if(StringUtils.isNotBlank(meterWaterUse) && (meterWaterUse.equalsIgnoreCase(JMSHYS) || StringUtils.isNotBlank(meterPriceCode))) {
			return true;
		}
		return false;
	}
	
	/**
	 * @Title: getNotLadderWaterPrice
	 * @Description: 获取非阶梯水价的水价信息
	 * @param waterUse		用水性质
	 * @param priceCode		水价编码
	 * @param waterAmount	实际水量
	 * @param wp		水价信息
	 * @return 
	 * 			水价信息
	 * 			waterAmount					实际水量
	 * 			useWaterPriceTraceJSON		使用水价日志
	 * 			usePeopleAdjustTraceJSON	使用多人口调整日志
	 * 			useDiscountTrace			使用政策减免日志
	 */
	public Map<String, Object> getNotLadderWaterPrice(String waterUse, String priceCode, BigDecimal waterAmount, SysWaterPrice wp) {
		if(wp!=null) {
			//非阶梯水价确定本期总水量和价格
			Map<String, Object> waterPriceMap = EntityUtils.entityToMap(wp);
			waterPriceMap.put("waterAmount", waterAmount);
			waterPriceMap.put("useWaterPriceTraceJSON", JSON.toJSONString(this.getUseWaterPriceTrace(wp)));
			waterPriceMap.put("usePeopleAdjustTraceJSON", null);
			waterPriceMap.put("useDiscountTrace", null);
			return waterPriceMap;
		}
		return null;
	}
	
	/**
	 * @Title: getLadderWaterPrice
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param customerId			客户ID
	 * @param period				期间
	 * @param waterAmount			本期水量
	 * @param yearWaterAmount		年总用水量
	 * @param waterPriceList		水价列表集合
	 * @param peopleAdjust			多人口调整信息
	 * @param cfgWaterAmount		多人口调整中配置的每增加一人所增加的水量（默认值：36）
	 * @param discount				政策减免
	 * @return 
	 * 				水价信息 
	 * 				waterAmount					实际水量 
	 * 				useWaterPriceTraceJSON 		使用水价日志 
	 * 				usePeopleAdjustTraceJSON	使用多人口调整日志 
	 * 				useDiscountTrace			使用政策减免日志
	 */
	public List<Map<String, Object>> getLadderWaterPrice(Long customerId, String period, BigDecimal waterAmount, BigDecimal yearWaterAmount, List<SysWaterPrice> waterPriceList, CustomerPeopleAdjust peopleAdjust, BigDecimal cfgWaterAmount, SysDiscount discount) {
		//(2)确定阶梯和水价
		List<Map<String, Object>> waterPriceMapList = this.judgeLadder(customerId, period, waterAmount, yearWaterAmount, waterPriceList, peopleAdjust, cfgWaterAmount);
		log.debug("----------计算水价----------  阶梯水价及水量:"+waterPriceMapList);
		//(3)确定最终水价(判断政策减免)
		if(discount!=null) {
			waterPriceMapList = this.getDiscountWaterPrice(waterPriceMapList, discount);
			log.debug("----------计算水价----------  判断政策减免后的阶梯水价及水量:"+waterPriceMapList);
		}
		return waterPriceMapList;
	}
	
	/**
	 * @Title: getLadderWaterPrice
	 * @Description: 确定阶梯水价的阶梯和水量
	 * @param customerId			客户ID
	 * @param period				期间
	 * @param waterAmount			本期水量
	 * @param yearWaterAmount		年总用水量
	 * @param waterPriceList		水价列表集合
	 * @param peopleAdjust			多人口调整信息
	 * @param cfgWaterAmount		多人口调整中配置的每增加一人所增加的水量（默认值：36）
	 * @return 
	 * 			水价信息
	 * 			waterAmount					实际水量
	 * 			useWaterPriceTraceJSON		使用水价日志
	 * 			usePeopleAdjustTraceJSON	使用多人口调整日志
	 * 			useDiscountTrace			使用政策减免日志
	 */
	public List<Map<String, Object>> judgeLadder(Long customerId, String period, BigDecimal waterAmount, BigDecimal yearWaterAmount, List<SysWaterPrice> waterPriceList, CustomerPeopleAdjust peopleAdjust, BigDecimal cfgWaterAmount) {
		
		List<Map<String, Object>> retWaterPriceMapList = new ArrayList<>();//返回水价和水量集合
		
//		String[] periodArr = period.split("-");
//		String year = periodArr[0];//从期中获取年
		
		BigDecimal zero = new BigDecimal("0");//BigDecimal类型的0值，用于后面的判断
		
		//判断多人口调整，获取所增加的总水量
		BigDecimal addWaterAmount = new BigDecimal(0.00);
		if(peopleAdjust!=null) {
			int addNum = peopleAdjust.getAddNum();//多人口调整记录中增加的人数
			
			BigDecimal peopleNum = new BigDecimal(addNum);//多人口调整记录中增加的人数(转为BigDecimal类型)
			addWaterAmount = BigDecimalUtils.multiply(cfgWaterAmount, peopleNum);//计算所增加的水量,增加的水量=系统配置的一人所增加的水量*增加人数
		}
		
		//判断多人口调整所增加的总水量是否>0
		boolean isGreaterZero = BigDecimalUtils.greaterThan(addWaterAmount, zero);
		if(addWaterAmount!=null && isGreaterZero) {//如果多人口调整所增加的总水量不为null，并且>0
			//年总用水量-多人口调整所增加的总水量=实际年用水量
			yearWaterAmount = BigDecimalUtils.subtract(yearWaterAmount, addWaterAmount);
		}
		
		//获取本期总水量
		BigDecimal totalWaterAmount = waterAmount;
		
		//年总的用水量(返回不包含本期的年总用水量)+本期总用水量=年总用水量
		BigDecimal tempYearTotalWaterAmount = BigDecimalUtils.add(yearWaterAmount, totalWaterAmount);
		//判断年总的用水量是否<=0,如果<=0则直接返回第一阶梯
		boolean lessOrEquals = BigDecimalUtils.lessOrEquals(tempYearTotalWaterAmount, zero);
		if(lessOrEquals) {//如果年总的用水量<=0，则直接返回第一阶梯水价
			
			SysWaterPrice record = waterPriceList.get(0);//获取第一阶梯水价信息
			//确定阶梯为当前循环的阶梯，确定水量为本期总水量
			Map<String, Object> waterPriceMap = EntityUtils.entityToMap(record);
			waterPriceMap.put("waterAmount", totalWaterAmount);
			this.putUseWaterPriceTrace(waterPriceMap, record);//增加水价日志到Map
			this.putUsePeopleAdjustTrace(waterPriceMap, peopleAdjust, cfgWaterAmount);//增加多人口调整日志到Map
			//this.putUseDiscountTrace(waterPriceMap, null);//增加政策减免日志到Map
			//把水价和水量加入到返回集合
			retWaterPriceMapList.add(waterPriceMap);
			return retWaterPriceMapList;
		}
		
		//-----------------------准备循环水价列表---------------------------------------------------------------------------------------
		//循环阶梯水价列表确定阶梯
		int size = waterPriceList.size();
		int lastSize = waterPriceList.size()-1;
		for(int i=0; i<size; i++) {
			//获取当前阶梯的初值和终值
			SysWaterPrice tempWaterPrice = waterPriceList.get(i);
			//String ladderStart = tempWaterPrice.getLadderStart();//当前阶梯初值
			String ladderEnd = tempWaterPrice.getLadderEnd();//当前阶梯终值

			if(i==lastSize) {//如果遍历的是最后一个阶梯价格，则直接返回最后阶梯的价格,水量为本期总水量
				//确定阶梯为第后一阶梯，确定水量为本期总水量
				Map<String, Object> waterPriceMap = EntityUtils.entityToMap(tempWaterPrice);
				waterPriceMap.put("waterAmount", totalWaterAmount);
				this.putUseWaterPriceTrace(waterPriceMap, tempWaterPrice);//增加水价日志到Map
				this.putUsePeopleAdjustTrace(waterPriceMap, peopleAdjust, cfgWaterAmount);//增加多人口调整日志到Map
				//this.putUseDiscountTrace(waterPriceMap, null);//增加政策减免日志到Map
				//把水价和水量加入到返回集合
				retWaterPriceMapList.add(waterPriceMap);
				break;
			}
			
			BigDecimal ladderEndBd = new BigDecimal(ladderEnd);//阶梯终值
			//加上本期用水量后的年总用水量=年总用水量+本期用水量
			BigDecimal yearTotalWaterAmount = BigDecimalUtils.add(yearWaterAmount, totalWaterAmount);
			//加上本期用水量后的年总用水量<=阶梯终值,则直接返回阶梯;否则拆分水量,并计算超出阶梯后的水量和价格
			boolean flag3 = BigDecimalUtils.lessOrEquals(yearTotalWaterAmount, ladderEndBd);//加上本期用水量后的年总用水量<=当前阶梯终值
			//boolean flag4 = BigDecimalUtils.greaterOrEquals(yearTotalWaterAmount, nextLadderEndBd);//加上本期用水量后的年总用水量>=下一阶梯终值
			if(flag3) {
				//确定阶梯为当前循环的阶梯，确定水量为本期总水量
				Map<String, Object> waterPriceMap = EntityUtils.entityToMap(tempWaterPrice);
				waterPriceMap.put("waterAmount", totalWaterAmount);
				this.putUseWaterPriceTrace(waterPriceMap, tempWaterPrice);//增加水价日志到Map
				this.putUsePeopleAdjustTrace(waterPriceMap, peopleAdjust, cfgWaterAmount);//增加多人口调整日志到Map
				//this.putUseDiscountTrace(waterPriceMap, null);//增加政策减免日志到Map
				//把水价和水量加入到返回集合
				retWaterPriceMapList.add(waterPriceMap);
				break;
			}else{//加上本期用水量后的年总用水量>大于当前阶梯终值
				//获取下一阶梯水价信息
				SysWaterPrice nextWaterPrice = waterPriceList.get(i+1);
				String nextLadderEnd = nextWaterPrice.getLadderEnd();//获取下一阶梯水价信息的阶梯终值
				BigDecimal nextLadderEndBd = new BigDecimal(nextLadderEnd);//把下一阶梯水价信息的阶梯终值转为decimal类型
				boolean flag5 = BigDecimalUtils.lessOrEquals(yearWaterAmount, nextLadderEndBd);//年总的用水量(返回不包含本期的年总用水量)<=下一阶梯终值
				if(flag5) {
					//(1)计算到阶梯终值还差多少水量
					BigDecimal subWaterAmount = BigDecimalUtils.subtract(ladderEndBd, yearWaterAmount);
					boolean flag6 = BigDecimalUtils.greaterOrEquals(subWaterAmount, zero);
					if(flag6) {
						//确定阶梯为当前循环的阶梯，确定水量为到阶梯终值所差的水量（H）
						Map<String, Object> waterPriceMap = EntityUtils.entityToMap(tempWaterPrice);
						waterPriceMap.put("waterAmount", subWaterAmount);
						this.putUseWaterPriceTrace(waterPriceMap, tempWaterPrice);//增加水价日志到Map
						this.putUsePeopleAdjustTrace(waterPriceMap, peopleAdjust, cfgWaterAmount);//增加多人口调整日志到Map
						//this.putUseDiscountTrace(waterPriceMap, null);//增加政策减免日志到Map
						//把水价和水量加入到返回集合
						retWaterPriceMapList.add(waterPriceMap);
						
						//年用水量+阶梯终值所差的水量=最新的年总用水量
						yearWaterAmount = BigDecimalUtils.add(yearWaterAmount, subWaterAmount);
						
						//计算本期水量-到阶梯终值所差的水量还剩多少水量,剩下的水量为最新的本期总水量
						totalWaterAmount = BigDecimalUtils.subtract(totalWaterAmount, subWaterAmount);
					}
				}
			}
		}
		
		return retWaterPriceMapList;
	}
	
	/**
	 * @Title: getDiscountWaterPrice
	 * @Description: 获取政策减免水价
	 * @param waterPriceMapList		水价及水量
	 * @param discount				政策减免
	 * @return 
	 */
	private List<Map<String, Object>> getDiscountWaterPrice(List<Map<String, Object>> waterPriceMapList, SysDiscount discount) {
		
		BigDecimal zero = new BigDecimal("0");//BigDecimal类型的0值，用于后面的判断
		
		//如果政策减免为空时，则不处理政策减免
		if(discount==null) {
			return waterPriceMapList;
		}
		
		//判断政策减免
		log.debug("----------计算水价----------  政策减免:"+discount);
		log.debug("----------计算水价----------  政策减免【前】水价及水量:"+waterPriceMapList);
		
		for(Map<String, Object> waterPriceMap : waterPriceMapList) {
			
			//this.putUseWaterPriceTrace(waterPriceMap, tempWaterPrice);//增加水价日志到Map
			//this.putUsePeopleAdjustTrace(waterPriceMap, pa, cfgWaterAmount);//增加多人口调整日志到Map
			this.putUseDiscountTrace(waterPriceMap, discount);//增加政策减免日志到Map
			log.debug("----------计算水价----------  政策减免【前】水价及水量:"+waterPriceMap);
			
			//水价表配置的基础水价和污水处理费
			BigDecimal basePrice = new BigDecimal(waterPriceMap.get("basePrice").toString());
			BigDecimal treatmentFee = new BigDecimal(waterPriceMap.get("treatmentFee").toString());
			
			if(discount!=null) {
				BigDecimal discountBasePrice = discount.getBasePrice();//政策减免基础水价
				BigDecimal discountTreatmentFee = discount.getTreatmentFee();//政策减免污水处理费
				
				boolean flag1 = BigDecimalUtils.greaterThan(discountBasePrice, zero);//政策减免基础水价>0
				boolean flag2 = BigDecimalUtils.greaterThan(discountTreatmentFee, zero);//政策减免污水处理费>0
				if(flag1 && flag2) {
					BigDecimal totalPrice = BigDecimalUtils.add(discountBasePrice, discountTreatmentFee);
					waterPriceMap.put("basePrice", discountBasePrice);//基础水价
					waterPriceMap.put("treatmentFee", discountTreatmentFee);//污水处理费
					waterPriceMap.put("totalPrice", totalPrice);//总单价=基础水价+污水处理费
					log.debug("----------计算水价----------  政策减免【后】水价及水量:"+waterPriceMap);
				}else if(flag1) {
					BigDecimal totalPrice = BigDecimalUtils.add(discountBasePrice, treatmentFee);
					waterPriceMap.put("basePrice", discountBasePrice);//基础水价
					waterPriceMap.put("treatmentFee", discountTreatmentFee);//污水处理费
					waterPriceMap.put("totalPrice", totalPrice);//总单价=基础水价+污水处理费
					log.debug("----------计算水价----------  政策减免【后】水价及水量:"+waterPriceMap);
				}else if(flag2) {
					BigDecimal totalPrice = BigDecimalUtils.add(basePrice, discountTreatmentFee);
					waterPriceMap.put("basePrice", basePrice);//基础水价
					waterPriceMap.put("treatmentFee", discountTreatmentFee);//污水处理费
					waterPriceMap.put("totalPrice", totalPrice);//总单价=基础水价+污水处理费
					log.debug("----------计算水价----------  政策减免【后】水价及水量:"+waterPriceMap);
				}
			}
		}
		
		log.debug("----------计算水价----------  政策减免【后】水价及水量:"+waterPriceMapList);
		return waterPriceMapList;
	}
	
	//---------------------------------获取日志部分 开始------------------------------------------------------------------------------------------------------------------------
	/**
	 * @Title: putUseWaterPriceTrace
	 * @Description: put使用水价日志json字符串到水价Map中
	 * @param waterPriceMap
	 * @param wp 
	 */
	private void putUseWaterPriceTrace(Map<String, Object> waterPriceMap, SysWaterPrice wp) {
		UseWaterPriceTrace wpTrace = this.getUseWaterPriceTrace(wp);
		String json = JSON.toJSONString(wpTrace);
		waterPriceMap.put(JSON_USE_WATER_PRICE_TRACE, json);
	}
	/**
	 * @Title: getUseWaterPriceTrace
	 * @Description: 获取使用水价日志
	 * @param wp		水价日志表
	 * @return 
	 */
	private UseWaterPriceTrace getUseWaterPriceTrace(SysWaterPrice wp) {
		wp.setId(null);
		Map<String, Object> wpMap = EntityUtils.entityToMap(wp);
		UseWaterPriceTrace wpTrace = EntityUtils.mapToEntity(wpMap, UseWaterPriceTrace.class);
		return wpTrace;
	}
	
	/**
	 * @Title: putUseDiscountTrace
	 * @Description: put使用政策减免日志json字符串到水价Map中
	 * @param waterPriceMap		水价信息和水量信息Map
	 * @param discount 			政策减免信息
	 */
	private void putUseDiscountTrace(Map<String, Object> waterPriceMap, SysDiscount discount) {
		if(discount!=null) {
			UseDiscountTrace discountTrace = this.getUseDiscountTrace(discount);
			String json = JSON.toJSONString(discountTrace);
			waterPriceMap.put(JSON_USE_DISCOUNT_TRACE, json);
		}else {
			waterPriceMap.put(JSON_USE_DISCOUNT_TRACE, null);
		}
		
	}
	/**
	 * @Title: getUseDiscountTrace
	 * @Description: 获取使用政策减免日志
	 * @param discount	政策减免信息
	 * @return 
	 */
	private UseDiscountTrace getUseDiscountTrace(SysDiscount discount) {
		discount.setId(null);
		Map<String, Object> discountMap = EntityUtils.entityToMap(discount);
		UseDiscountTrace discountTrace = EntityUtils.mapToEntity(discountMap, UseDiscountTrace.class);
		return discountTrace;
	}
	
	/**
	 * @Title: putUsePeopleAdjustTrace
	 * @Description: put使用多人口调整日志json字符串到水价Map中
	 * @param waterPriceMap		水价信息和水量信息Map
	 * @param waterAmount 		系统配置的每增加一个所增加的水量
	 */
	private void putUsePeopleAdjustTrace(Map<String, Object> waterPriceMap, CustomerPeopleAdjust pa, BigDecimal waterAmount) {
		if(pa!=null) {//如果多人口调整记录不为空时，增加使用多人口调整日志
			UsePeopleAdjustTrace paTrace = this.getUsePeopleAdjustTrace(pa, waterAmount);
			String json = JSON.toJSONString(paTrace);
			waterPriceMap.put(JSON_USE_PEOPLE_ADJUST_TRACE, json);
		}else {
			waterPriceMap.put(JSON_USE_PEOPLE_ADJUST_TRACE, null);
		}
		
	}
	/**
	 * @Title: getUsePeopleAdjustTrace
	 * @Description: 获取使用多人口调整日志
	 * @param pa			多人口调整记录
	 * @param waterAmount	系统配置的每增加一个所增加的水量
	 * @return 
	 */
	private UsePeopleAdjustTrace getUsePeopleAdjustTrace(CustomerPeopleAdjust pa, BigDecimal waterAmount) {
		pa.setId(null);
		Map<String, Object> paMap = EntityUtils.entityToMap(pa);
		UsePeopleAdjustTrace paTrace = EntityUtils.mapToEntity(paMap, UsePeopleAdjustTrace.class);
		paTrace.setWaterAmount(waterAmount);
		return paTrace;
	}
	
	//---------------------------------获取日志部分 结束------------------------------------------------------------------------------------------------------------------------
	
}
