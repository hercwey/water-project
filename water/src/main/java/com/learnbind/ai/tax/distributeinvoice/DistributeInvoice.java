package com.learnbind.ai.tax.distributeinvoice;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax.distributeinvoice
 *
 * @Title: DistributeInvoice.java
 * @Description: 分配发票算法对象
 *
 * @author lenovo
 * @date 2019年12月11日 下午4:31:50
 * @version V1.0
 *
 */
@Component
public class DistributeInvoice {
	private static Log log = LogFactory.getLog(DistributeInvoice.class);
	private final int MAX_AMOUNT_PER_INVOICE =100*10000; // 单发票最大金额. 百万元版
	final BigDecimal TAX_RATE=new BigDecimal("0.03");  //税率
	final int DETAIL_MAX_NUM_PER_INVOICE = 8; // 单张发票最大条目数
	
	private void displayUsePrice(int usePrice) {
		String usePriceStr="";
		switch(usePrice) {
		case 1:
			usePriceStr="使用基价";
			break;
		case 2:
			usePriceStr="使用污水处理价";
			break;
		case 3:
			usePriceStr="使用综价";
			break;
		}
		//return usePriceStr;
		log.debug("使用的价格类型是:  "+usePriceStr);
	}

	/**
	 * @Title: distributeMethod
	 * @Description: 分配算法
	 * @param accountItems 待分配账目列表.
	 * @return
	 */
	public DistributeResult distributeMethod(SourceAccountItems sourceAccountItems) {
		boolean isMerge = sourceAccountItems.isMergeAccountItem(); // 合并账单标识
		List<SourceAccountItem> billList = sourceAccountItems.getAccountItemList(); // 账单列表
		int usePrice = sourceAccountItems.getUsePrice(); // 所使用价格类型
		displayUsePrice(usePrice);  //调试时使用,可以屏蔽
		
		DistributeResult disResult=null;

		// 账单列表条目个数有效性判定.
		if (billList.size() <= 0) {
			//System.out.println("未发现账单数据.........");
			log.debug("未发现账单数据.........");
		} else {
			//System.out.println("原始账单列表如下:");
			log.debug("原始账单列表如下:");
			displayOriginBill(billList);
		}

		Map<BigDecimal, List<SourceAccountItem>> combinList = new HashMap<>(); // 按price分组 price---map--->List<TestBill>
		Map<BigDecimal, Integer> priceMap = new HashMap<>(); // 相同价格账单个数(>1的账单) price---map--->可合并的账单个数
		if (isMerge) { // 合并账单
			//System.out.println("合并账单状态:-----------打开");
			log.debug("合并账单状态:-----------打开");
			groupByPrice(billList, combinList, priceMap, usePrice); // 按价格分组

			// 查询:是否有己合并的账单
			boolean combinFlag = false;
			if (priceMap.keySet().size() > 0) {
				combinFlag = true;
			}

			//System.out.println("账单列表中有可合并的账单?:   " + combinFlag);
			log.debug("账单列表中有可合并的账单?:   " + combinFlag);
			if (combinFlag) {
				for (BigDecimal price : priceMap.keySet()) {
					//System.out.println("合并的价格是:" + price + "  个数是:" + priceMap.get(price));
					log.debug("合并的价格是:" + price + "  个数是:" + priceMap.get(price));
				}

				// 合并账单--->新的账单列表
				// 未合并账单--->新的账单列表
				List<SourceAccountItem> afterCombinList = processCombinList(combinList, usePrice); // 整理后的账单列表.
				// display result(test code)
				for (SourceAccountItem testBill : afterCombinList) {
					//System.out.println("合并后:------账单价格:" + testBill.getCombinPrice() + " 账单水量:"+ testBill.getCombinRealWaterAmount() + " 金额:" + testBill.getCombinWaterFee());
					log.debug("合并后:------账单价格:" + testBill.getCombinPrice() + " 账单水量:"+ testBill.getCombinRealWaterAmount() + " 金额:" + testBill.getCombinWaterFee());
				}

				// afterCombinList为价格各不相同账单,此时已演化为[不合并账单处理]
				disResult=dispatchBill2Invoice(afterCombinList, MAX_AMOUNT_PER_INVOICE);

			} else {
				// 已演化为:[不合并账单] billList
				preProcessBillList(billList, usePrice);
				disResult=dispatchBill2Invoice(billList, MAX_AMOUNT_PER_INVOICE);
			}
		} else { // [不合并账单]
			//System.out.println("合并账单状态:-----------关闭");
			log.debug("合并账单状态:-----------关闭");
			preProcessBillList(billList, usePrice);
			disResult=dispatchBill2Invoice(billList, MAX_AMOUNT_PER_INVOICE);
		}

		return disResult;
	}

	//根据账单中的价格置组合后字段值(有可能未合并)
	private void preProcessBillList(List<SourceAccountItem> billList, int usePrice) {
		for (SourceAccountItem bill : billList) {
			bill.setCombinAccountItemIds(bill.getAccountItemId() + ",");
			bill.setCombinPrice(bill.getPrice(usePrice));
			bill.setCombinRealWaterAmount(bill.getRealWaterAmount());
			bill.setCombinWaterFee(bill.getWaterFee(usePrice));
		}
	}
	
	/**
	 * @Title: processBill
	 * @Description: 分配账单--->发票
	 * @param billList  整理后的账单(待分配发票的账单列表)
	 * @param maxAmount 单张发票最大值
	 */
	private DistributeResult dispatchBill2Invoice(List<SourceAccountItem> billList, int maxAmountPerInvoice) {
		
		

		DistributeResult distResult=new DistributeResult();
		
		// 开始分配发票的账单列表
		//System.out.println("待分配发票的账单列表");
		log.debug("待分配发票的账单列表");
		displayBill(billList); // debug code

		// 求和处理,之所以会先处理求和操作,是因为大部分的账单不会超过百万
		BigDecimal billSum = sumBills(billList);
		// 账单合计--比较--单张发票(最大额度)
		// if(billSum>maxAmountPerInvoice) {

		if (billSum.compareTo(new BigDecimal(maxAmountPerInvoice)) == 1) { // case one:> 合计金额>单发票最大面额

			// 单账目金额>=单张发票最大金额 列表
			List<SourceAccountItem> largerEqualList = getGEList(billList, maxAmountPerInvoice);
			// debug code
			if (largerEqualList.size() > 0) {
				//System.out.println("账目金额>=单张发票最大额度-case3");
				//System.out.println("case3-按单账目额度划分");
				//System.out.println("账目(大于单发票最大额度)条数:  " + largerEqualList.size());
				log.debug("账目金额>=单张发票最大额度-case3");
				log.debug("case3-按单账目额度划分");
				log.debug("账目(大于单发票最大额度)条数:  " + largerEqualList.size());
				for (SourceAccountItem bill : largerEqualList) {
					if (bill.getCombinWaterFee().compareTo(new BigDecimal(maxAmountPerInvoice)) == 0) {
						//System.out.println("单账目---单张发票-case3.1");
						log.debug("单账目---单张发票-case3.1");						
						createOneInvoice(bill,distResult); //生成一张新的发票			
					} else {
						//System.out.println("单账目---多张发票-case3.2");
						log.debug("单账目(可能是合并后)---多张发票-case3.2");
						//TODO ADD CODE HERE  参见TESTCASE3.2
						DistributeCase32 case32=new DistributeCase32();
						DistributeResult tempResult=case32.dispatchInvoice(bill, maxAmountPerInvoice, TAX_RATE); //分配结果
						//加入到结果列表中
						if(tempResult.getInvoiceNum()>0) {
							for(int i=0;i<tempResult.getInvoiceNum();i++) {
								distResult.addOneInvoice(tempResult.getOneInvoiceByIndex(i));
							}
						}
					}
				}
			}

			// 单笔账目金额<单张发票最大金额 列表
			List<SourceAccountItem> lessList = getLessList(billList, maxAmountPerInvoice);
			// debug code
			if (lessList.size() > 0) {
				//System.out.println("单笔账目金额<单张发票最大额度-case4");
				//System.out.println("case4-按账目条数及账目金额划分");
				//System.out.println("账目(小于单发票最大额度)条数:  " + lessList.size());
				log.debug("单笔账目金额<单张发票最大额度-case4");
				log.debug("case4-按账目条数及账目金额划分");
				log.debug("账目(小于单发票最大额度)条数:  " + lessList.size());

				BigDecimal billSum1 = sumBills(lessList);
				//if (billSum1 <= maxAmountPerInvoice) {
				if (billSum1.compareTo(new BigDecimal(maxAmountPerInvoice))<=0 ) {  //列表中条目合计<=单张发票最大额
					//System.out.println("多笔账目---多张发票-case4.1");
					log.debug("多笔账目---多张发票(mod 8)-case4.1");
					processCase41(lessList,distResult,DETAIL_MAX_NUM_PER_INVOICE);
					//TODO simple
				} else {
					//System.out.println("多笔账目---多张发票-case4.2");
					log.debug("多笔账目---多张发票-case4.2");
					//TODO 参见 TESTCASE4.2
					DistributeCase42 case42=new DistributeCase42();
					DistributeResult tempResult=case42.dispatchInvoice(lessList, maxAmountPerInvoice, TAX_RATE);
					//加入到结果列表中.
					if(tempResult.getInvoiceNum()>0) {
						for(int i=0;i<tempResult.getInvoiceNum();i++) {
							distResult.addOneInvoice(tempResult.getOneInvoiceByIndex(i));
						}
					}
					
				}
			}
		} 
		else { // case two:<=  所有账单合计金额<=单发票最大面额
			// 判定账单条数<=单张发票最大明细条目数
			if (billList.size() <= DETAIL_MAX_NUM_PER_INVOICE) {
				//System.out.println("所有的账目可开具在一张发票中-case1");
				log.debug("所有的账目可开具在一张发票中-case1");
				processCase41(billList,distResult,DETAIL_MAX_NUM_PER_INVOICE);
				//TODO simple
			} else {
				System.out.println("每8条账目开具在一张发票中-case2");
				log.debug("每8条账目开具在一张发票中(MOD 8)-case2");				
				//TODO simple
				processCase41(billList,distResult,DETAIL_MAX_NUM_PER_INVOICE);
			}
		}
		
		return distResult;
	}
	
	//case31
	private void createOneInvoice(SourceAccountItem bill,DistributeResult distResult) {
		ResultAccountItems oneInvoice=distResult.genOneInvoice(); 
		addAccountItem2Invoice(oneInvoice,bill);
	}
	
	//case41,case1,case2
	private void processCase41(List<SourceAccountItem> billList, DistributeResult distResult,
			int detailMaxNumPerInvoice) {
		ResultAccountItems newInvoice = null;
		for (int i = 0; i < billList.size(); i++) {
			if (i % detailMaxNumPerInvoice == 0) {
				newInvoice = distResult.genOneInvoice();  //相当于mod detailMaxNumPerInvoice 生成发票
			}
			SourceAccountItem bill = billList.get(i);
			addAccountItem2Invoice(newInvoice,bill);
		}

	}
	
	//向一张发票中增加账目
	private void addAccountItem2Invoice(ResultAccountItems oneInvoice,SourceAccountItem bill) {
		ResultAccountItem item = new ResultAccountItem();
		item.setAccountItemIds(bill.getCombinAccountItemIds()); // 账单IDS
		item.setInvoicePrice(bill.getCombinPrice()); // 开票价格
		item.setInvoiceWaterAmount(bill.getCombinRealWaterAmount());// 开票水量
		item.setInvoiceAmount(bill.getCombinWaterFee()); // 开票金额
		item.setInvoiceTaxAmount(calcTax(bill.getCombinPrice(), bill.getCombinRealWaterAmount(), TAX_RATE)); // 开票税额
		item.setTaxRate(TAX_RATE); // 开票税率
		oneInvoice.addAccountItem(item);
	}

	/**
	 * @Title: processCombinList
	 * @Description: 将可合并的账单合并成一个账单,重新整理账单
	 * @param combinList
	 * @return
	 */
	private List<SourceAccountItem> processCombinList(Map<BigDecimal, List<SourceAccountItem>> combinList,
			int usePrice) {
		List<SourceAccountItem> afterCombinList = new ArrayList<>();
		for (BigDecimal price : combinList.keySet()) {
			List<SourceAccountItem> samePriceList = combinList.get(price);
			if (samePriceList.size() <= 1) { // 无需合并的账单
				SourceAccountItem tempBill = new SourceAccountItem();
				tempBill.setCombinAccountItemIds(samePriceList.get(0).toString());
				tempBill.setCombinPrice(samePriceList.get(0).getPrice(usePrice));
				tempBill.setCombinRealWaterAmount(samePriceList.get(0).getRealWaterAmount());
				tempBill.setCombinWaterFee(samePriceList.get(0).getWaterFee(usePrice));
				afterCombinList.add(tempBill);
			} else { // 需合并的账单---求和--->新账单
				SourceAccountItem sumBill = new SourceAccountItem();
				sumBill.setCombinPrice(samePriceList.get(0).getPrice(usePrice));
				sumBill.setCombinAccountItemIds("");
				sumBill.setCombinRealWaterAmount(new BigDecimal("0"));
				sumBill.setCombinWaterFee(new BigDecimal("0"));

				for (SourceAccountItem tempBill : samePriceList) {
					sumBill.setCombinAccountItemIds(
							sumBill.getCombinAccountItemIds() + tempBill.getAccountItemId() + ",");
					sumBill.setCombinRealWaterAmount(
							sumBill.getCombinRealWaterAmount().add(tempBill.getRealWaterAmount()));
					sumBill.setCombinWaterFee(sumBill.getCombinWaterFee().add(tempBill.getWaterFee(usePrice)));
				}
				afterCombinList.add(sumBill); // 合并后的账单
			}
		}
		return afterCombinList;
	}
	
	/**
	 * @Title: calcTax
	 * @Description: 计算税额
	 * @param price   含税价
	 * @param amount  商品数量
	 * @param taxRate 税率
	 * @return  税额
	 */
	private BigDecimal calcTax(BigDecimal taxPrice,BigDecimal amount,BigDecimal taxRate) {
		final RoundingMode roundMode=RoundingMode.HALF_UP;
		//BigDecimal taxPrice=bill.getCombinPrice();  //含税价
		BigDecimal waterAmount=amount; //水量		 
		
		BigDecimal noTaxPrice=taxPrice.divide(taxRate.add(new BigDecimal("1")),18,roundMode);  //保留18位精度
		
		BigDecimal totalTax=noTaxPrice.multiply(waterAmount).multiply(taxRate);  //总税额
		
		log.debug("税额为:"+totalTax.setScale(2, roundMode));
		log.debug("----------------------------------");
		return totalTax.setScale(2, roundMode);
	}

	/**
	 * @Title: displayBill
	 * @Description: 显示账单列表信息(合并信息)
	 */
	private void displayBill(List<SourceAccountItem> billList) {
		for (SourceAccountItem bill : billList) {
			/*
			 * System.out.println("账单ID:" + bill.getCombinAccountItemIds() + "  " + "价格:" +
			 * bill.getCombinPrice() + " " + "数量:" + "  " + bill.getCombinRealWaterAmount()
			 * + " " + "金额:" + bill.getCombinWaterFee());
			 */
			log.debug("账单ID:" + bill.getCombinAccountItemIds() + "  " + "价格:" + bill.getCombinPrice() + " "
					+ "数量:" + "  " + bill.getCombinRealWaterAmount() + " " + "金额:" + bill.getCombinWaterFee());
		}
	}
	
	//显示原始账单
	private void displayOriginBill(List<SourceAccountItem> billList) {
		for (SourceAccountItem bill : billList) {
			/*
			 * System.out.println("账单ID:" + bill.getAccountItemId() + "  " + "基价:" +
			 * bill.getBasePrice() + " "+ "污水处理费价:" + bill.getSewagePrice() + " "+ "综价:" +
			 * bill.getTotalPrice() + " "+ "数量:" + "  " + bill.getRealWaterAmount() + " "+
			 * "基价金额:" + bill.getBaseWaterFee()+" "+ "污水处理费金额:" +
			 * bill.getSewageWaterFee()+" "+ "综价金额:" + bill.getTotalWaterFee() );
			 */
			log.debug("账单ID:" + bill.getAccountItemId() + "  " +
					"基价:" + bill.getBasePrice() + " "+
					"污水处理费价:" + bill.getSewagePrice() + " "+
					"综价:" + bill.getTotalPrice() + " "+
					"数量:" + "  " + bill.getRealWaterAmount() + " "+ 
					"基价金额:" + bill.getBaseWaterFee()+" "+
					"污水处理费金额:" + bill.getSewageWaterFee()+" "+
					"综价金额:" + bill.getTotalWaterFee()
					);
		}
	}

	/**
	 * @Title: getLargerList
	 * @Description: 获取[所有账单金额>=单张发票最大额度] 的账单
	 * @param billList            整理后的账单
	 * @param maxAmountPerInvoice 单张发票最大额度
	 * @return
	 */
	private List<SourceAccountItem> getGEList(List<SourceAccountItem> billList, int maxAmountPerInvoice) {
		List<SourceAccountItem> geList = new ArrayList<>();
		for (SourceAccountItem bill : billList) {
			if (bill.getCombinWaterFee().compareTo(new BigDecimal(maxAmountPerInvoice)) >= 0) {
				geList.add(bill);
			}
		}
		return geList;
	}

	/**
	 * @Title: getLessList
	 * @Description: 获取[所有账单金额<单张发票最大额度] 的账单
	 * @param billList            整理后的账单
	 * @param maxAmountPerInvoice 单张发票最大额度
	 * @return
	 */
	private List<SourceAccountItem> getLessList(List<SourceAccountItem> billList, int maxAmountPerInvoice) {
		List<SourceAccountItem> lessList = new ArrayList<>();
		for (SourceAccountItem bill : billList) {
			if (bill.getCombinWaterFee().compareTo(new BigDecimal(maxAmountPerInvoice)) < 0) {
				lessList.add(bill);
			}
		}
		return lessList;
	}

	/**
	 * @Title: sumBills
	 * @Description: 合计账单金额
	 * @param billList 账单合并后列表.
	 * @return
	 */
	private BigDecimal sumBills(List<SourceAccountItem> billList) {
		BigDecimal sum = new BigDecimal("0");
		for (SourceAccountItem bill : billList) {
			sum = sum.add(bill.getCombinWaterFee());
		}
		return sum;
	}

	/**
	 * @Title: groupByPrice
	 * @Description: 按价格分组
	 * @param billList   input parameter
	 * @param combinList output parameter
	 * @param priceMap   output parameter
	 * @param usePrice   使用价格类型
	 */
	private void groupByPrice(List<SourceAccountItem> billList, Map<BigDecimal, List<SourceAccountItem>> combinList,
			Map<BigDecimal, Integer> priceMap, int usePrice) {
		for (SourceAccountItem bill : billList) {
			List<SourceAccountItem> groupList = combinList.get(bill.getPrice(usePrice));
			if (groupList == null) {
				groupList = new ArrayList<SourceAccountItem>();
				groupList.add(bill);
				combinList.put(bill.getPrice(usePrice), groupList);
			} else {
				groupList.add(bill);
				if (groupList.size() > 1) {
					priceMap.put(bill.getPrice(usePrice), groupList.size());
				}
			}
		}
	}
	
	public static void main(String[] args) {
		SourceAccountItems testItems=new SourceAccountItems();
		testItems.setMergeAccountItem(false); //合并账单
		testItems.setUsePrice(3);  //
		
		List<SourceAccountItem> itemList =testItems.getAccountItemList();
		for(int i=1;i<10;i++) {
			SourceAccountItem item=new SourceAccountItem();
			item.setAccountItemId((i+1l));
			item.setBasePrice(new BigDecimal(2));
			item.setSewagePrice(new BigDecimal(3));
			item.setTotalPrice(new BigDecimal(5));
			item.setRealWaterAmount(new BigDecimal(i+1));
			item.setBaseWaterFee(new BigDecimal((i+1)*2));
			item.setSewageWaterFee(new BigDecimal((i+1)*3));
			item.setTotalWaterFee(new BigDecimal((i+1)*5));
			itemList.add(item);
		}
		
		DistributeInvoice distInvoice=new DistributeInvoice();
		DistributeResult distResult=distInvoice.distributeMethod(testItems);
		
		//System.out.println("分配结果:"+JSON.toJSONString(distResult));
		String jsonStr=JSON.toJSONString(distResult);
		log.debug("分配结果:"+jsonStr);
		
		
	}

}
