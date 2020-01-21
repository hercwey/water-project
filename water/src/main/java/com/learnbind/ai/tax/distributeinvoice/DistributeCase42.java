package com.learnbind.ai.tax.distributeinvoice;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DistributeCase42 {
	private static Log log = LogFactory.getLog(DistributeCase42.class);
	
	class MyComparator implements Comparator<SourceAccountItem> {
		@Override
		public int compare(SourceAccountItem o1, SourceAccountItem o2) {
			// TODO Auto-generated method stub
			if(o1.getCombinRealWaterAmount().compareTo(o2.getCombinRealWaterAmount())==1)
			//if (o1.getAmount() > o2.getAmount())
				return -1;
			else if (o1.getCombinRealWaterAmount().compareTo(o2.getCombinRealWaterAmount())==-1)
				return 1;
			else {
				return 0;
			}
		}
	}

	public DistributeResult dispatchInvoice(List<SourceAccountItem> billList, int maxAmountPerInvoice,BigDecimal taxRate) {
		final int MAX_DETAIL_NUM_PER_INVOICE = 8; // 单张发票最大条目数
		//Set<Integer> initGroup = new HashSet<Integer>(); // 声明一个集合

		DistributeResult distResult=new DistributeResult();  //分配结果
		
		// 排序后的列表为
		//System.out.println("排序前列表:");
		log.debug("排序前列表:");
		displayBill(billList);

		// (1)先将billList中的元素排序(逆序,自大到小)
		billList.sort(new MyComparator());
		// 排序后的列表为
		//System.out.println("排序后列表(倒序):");
		log.debug("排序后列表(倒序):");
		displayBill(billList);
		// groupNo--->set()
		// g1--->set()
		// g2--->set()
		// g3--->set()
		Map<Integer, Set<Integer>> resultMap = new HashMap<>(); // 分组结果
		Set<Integer> groupedSet = new HashSet<>(); // 已分组的元素索引
		Integer currGroupNo = 0;

		int headPointer = 0;
		int tailPointer = billList.size() - 1;

		// 开始分配
		while (true) {
			if (groupedSet.size() == billList.size()) break;
			
			if (groupedSet.size() < billList.size()) {
				if (resultMap.get(currGroupNo) == null) {  //生成新组
					Set<Integer> newSet = new HashSet<>(); 
					newSet.add(headPointer);
					resultMap.put(currGroupNo, newSet);
					
					groupedSet.add(headPointer);

					// 自尾加入
					// 当前组个数
					while (tailPointer > headPointer) {
						int gItems = newSet.size();
						BigDecimal gSum = groupSum(billList, newSet);
						BigDecimal preCalcSum = gSum .add( billList.get(tailPointer).getCombinWaterFee());
						if (gItems < MAX_DETAIL_NUM_PER_INVOICE && (preCalcSum.compareTo(new BigDecimal(maxAmountPerInvoice)) <=0)) {
							newSet.add(tailPointer); // 加入分组
							groupedSet.add(tailPointer); // 加入己分组元素集合
							tailPointer = tailPointer - 1;							
						} else {
							currGroupNo=currGroupNo+1;  //新的组号
							headPointer=headPointer+1;
							break;
						}
					}
				}
				else {
					//System.out.println("do nothing");
					log.debug("do nothing");
				}
			} 
			else {
				break;  //已经分配完毕
			}
		}
		
		//display group result  显示分组结果.
		for(Integer groupNo:resultMap.keySet()) {
			Set<Integer> group=resultMap.get(groupNo);
			ResultAccountItems oneInvoice=distResult.genOneInvoice();  //生成一张新的发票
			//System.out.println("分组:"+groupNo);
			String indexStr="";
			for(Integer index: group) {
				indexStr=indexStr+" ,"+index;
				
				SourceAccountItem bill=billList.get(index);
				ResultAccountItem item=new ResultAccountItem();
				item.setAccountItemIds(bill.getCombinAccountItemIds());  	//账单IDS
				item.setInvoicePrice(bill.getCombinPrice());  				//开票价格
				item.setInvoiceWaterAmount(bill.getCombinRealWaterAmount());//开票水量
				item.setInvoiceAmount(bill.getCombinWaterFee());  			//开票金额
				item.setInvoiceTaxAmount(calcTax(bill.getCombinPrice(),bill.getCombinRealWaterAmount(),taxRate));  		//开票税额
				item.setTaxRate(taxRate);  //开票税率
				
				oneInvoice.addAccountItem(item);
				
			}
			//System.out.println("分组号:"+groupNo+" "+"分组索引:"+indexStr);
			log.debug("分组号:"+groupNo+" "+"分组索引:"+indexStr);
		}
		
		return distResult;

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
		
		//System.out.println("总税额为:"+totalTax.setScale(2, roundMode));
		//System.out.println("----------------------------------");
		//log.debug("税额为:"+totalTax.setScale(2, roundMode));
		//log.debug("----------------------------------");
		return totalTax.setScale(2, roundMode);
	}

	private BigDecimal groupSum(List<SourceAccountItem> billList, Set<Integer> gset) {
		BigDecimal sum = new BigDecimal("0");
		for (Integer index : gset) {
			sum = sum.add(billList.get(index).getCombinWaterFee()); 
		}
		return sum;
	}
	
	
	/**
	 * @Title: displayBill
	 * @Description: 显示账单列表信息
	 */
	private void displayBill(List<SourceAccountItem> billList) {
		int i=0;
		for (SourceAccountItem bill : billList) {
			/*
			 * System.out.println("Index:"+ (i++)+ "账单ID:" + bill.getCombinAccountItemIds()
			 * + "  " + "价格:" + bill.getCombinPrice() + " " + "数量:" + "  " +
			 * bill.getCombinRealWaterAmount() + " " + "金额:" + bill.getCombinWaterFee());
			 */
			log.debug("Index:"+ (i++)+    "账单ID:" + bill.getCombinAccountItemIds() + "  " + "价格:" + bill.getCombinPrice() + " " + "数量:" + "  "
					+ bill.getCombinRealWaterAmount() + " " + "金额:" + bill.getCombinWaterFee());
		}
	}
	
	
}
