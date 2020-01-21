package com.learnbind.ai.tax.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.controller.tax
 *
 * @Title: PartitionBill.java
 * @Description: 划分账单--->发票
 * 			     	账单分配算法.
 *
 * Input:输入
 * 发票属性:
 * 	发票起始号码:startNo
 * 	发票库存数: inventory
 * 	单张发票最大值:10
 * 	
 * 	账单属性:
 * 		序号: serialNo  模拟ID
 * 		金额: amount
 * 		水量: waterNum
 * 		价格: price
 * Output:输出
 * 
 * 		
 *
 *	发票与账单采用Map进行模拟.
 * @author lenovo
 * @date 2019年12月4日 下午11:44:32
 * @version V1.0 
 *
 */
public class PartitionBill {
	//(1)发票对象
	private InvoiceVolInfo testInvoice=new InvoiceVolInfo();   //发票属性对象 只用于描述发票的属性
	private List<TestBill> billList=new ArrayList<>();	  //账单列表
	//private boolean isMerge=false;  //是否合并
	
	/**
	 * @Title: initInvoiceObj
	 * @Description: 初始化发票对象 
	 */
	private void initInvoiceObj() {
		testInvoice.setStartNo(1);
		testInvoice.setInventory(100);
		testInvoice.setMaxAmount(10);		
	}
	
	/**
	 * @Title: initBillList  测试数据  test data
	 * @Description: 初始化账单列表
	 * 	水量		单价		金额		
	 * 	1		1		1
	 * 	2		2		4
	 * 	3		3		9
	 * 	4		4		16
	 * 	5		5		25
	 * 					36
	 * 					49
	 * 					64
	 * 					81
	 * 					100 		
	 */
	private void initBillList1() {
		for(int i=0;i<10;i++) {
			TestBill bill=new TestBill();
			bill.setSerialNo(i+1);
			bill.setWaterNum(i+1);
			bill.setPrice(i+1);
			bill.setAmount((i+1)*(i+1));			
			billList.add(bill);
		}
	}
	
	/**
	 * @Title: initBillList2  测试数据  test data
	 * @Description: 	初始化账单,
	 * 					价格相同场景.
	 * 	账单条数		单价
	 * 		5		2	
	 * 		5		3
	 * 
	 */
	private void initBillList2() {
		for(int i=0;i<5;i++) {
			TestBill bill=new TestBill();
			bill.setSerialNo(i+1);
			bill.setWaterNum(i+1);
			bill.setPrice(2);
			bill.setAmount((i+1)*bill.getPrice());			
			billList.add(bill);
		}
		
		for(int i=0;i<5;i++) {
			TestBill bill=new TestBill();
			bill.setSerialNo(i+1);
			bill.setWaterNum(i+1);
			bill.setPrice(3);
			bill.setAmount((i+1)*bill.getPrice());			
			billList.add(bill);
		}
	}	
	
	/**
	 * @Title: partitionMethod
	 * @Description: 
	 * 				账单是否可合并?合并指标
	 * 				只有价格相同时才可以进行合并.
	 * @param isMerge 
	 */
	private void partitionMethod(boolean isMerge) {
		
		//账单列表条目个数有效性判定.
		if(this.billList.size()<=0) {
			System.out.println("未发现账单数据.........");
		}
		else {
			System.out.println("原始账单列表如下:");
			displayBill(this.billList);
		}
		
		//按price分组  price---map--->List<TestBill>
		Map<Integer,List<TestBill>> combinList=new HashMap<>();
		
		//相同价格账单个数(>1的账单)  price---map--->可合并的账单个数   
		Map<Integer,Integer> priceMap=new HashMap<>();
		if(isMerge) {  //合并账单
			
			System.out.println("合并账单状态:-----------打开");
			
			groupByPrice(this.billList,combinList,priceMap);  //按价格分组
			
			//是否有己合并的账单
			boolean combinFlag=false;
			if(priceMap.keySet().size()>0) {
				combinFlag=true;
			}
			
			System.out.println("账单列表中有可合并的账单?:   "+combinFlag);
			if(combinFlag) {				
				for(Integer price :priceMap.keySet()) {
					System.out.println("合并的价格是:"+price+"  个数是:"+priceMap.get(price));					
				}
				
				//合并账单--->新的账单列表
				//未合并账单--->新的账单列表
				List<TestBill> afterCombinList=processCombinList(combinList);	//整理后的账单列表.			
				//display result(test code)
				for(TestBill testBill:afterCombinList) {
					System.out.println("合并后:------账单价格:"+testBill.getPrice()+" 账单水量:"+testBill.getWaterNum()+" 金额:"+testBill.getAmount());
				}
				
				//afterCombinList为价格各不相同账单,此时已演化为[不合并账单处理]
				dispatchBill2Invoice(afterCombinList,testInvoice.getMaxAmount());
				
			}
			else {
				//已演化为:[不合并账单]  billList
				dispatchBill2Invoice(this.billList,testInvoice.getMaxAmount());
			}						
		}
		else {  //[不合并账单]
			System.out.println("合并账单状态:-----------关闭");
			dispatchBill2Invoice(this.billList,testInvoice.getMaxAmount());
		}
	}
	
	
	/**
	 * @Title: processBill
	 * @Description: 	  分配账单--->发票		
	 * @param billList   整理后的账单(待分配发票的账单列表)
	 * @param maxAmount  单张发票最大值
	 */
	private void dispatchBill2Invoice(List<TestBill> billList,int maxAmountPerInvoice) {
		final int DETAIL_MAX_NUM_PER_INVOICE=8;  //单张发票最大条目数
		
		//开始分配发票的账单列表
		System.out.println("待分配发票的账单列表");
		displayBill(billList);  //debug code
		
		//求和处理,之所以会先处理求和操作,是因为大部分的账单不会超过百万
		int billSum=sumBills(billList);
		//账单合计--比较--单张发票(最大额度)
		if(billSum>maxAmountPerInvoice) {  //case one:> 
			
			//单账目金额>=单张发票最大金额  列表
			List<TestBill> largerEqualList=getGEList(billList,maxAmountPerInvoice);			
			//debug code
			if(largerEqualList.size()>0) {
				System.out.println("账目金额>=单张发票最大额度-case3");
				System.out.println("case3-按单账目额度划分");
				System.out.println("账目(大于单发票最大额度)条数:  "+largerEqualList.size());
				for(TestBill bill:largerEqualList) {
					if(bill.getAmount()==maxAmountPerInvoice) {
						System.out.println("单账目---单张发票-case3.1");
					}
					else {
						System.out.println("单账目---多张发票-case3.2");
					}
				}
			}
			
			//单笔账目金额<单张发票最大金额  列表
			List<TestBill> lessList=getLessList(billList,maxAmountPerInvoice);			
			//debug code
			if(lessList.size()>0) {
				System.out.println("单笔账目金额<单张发票最大额度-case4");
				System.out.println("case4-按账目条数及账目金额划分");
				System.out.println("账目(小于单发票最大额度)条数:  "+lessList.size());
				
				int billSum1=sumBills(billList);
				if(billSum1<=maxAmountPerInvoice) {
					System.out.println("多笔账目---多张发票-case4.1");
				}
				else {
					System.out.println("多笔账目---多张发票-case4.2");
				}
			}
		}
		else {  //case two:<=
			//判定账单条数<=单张发票最大明细条目数
			if(this.billList.size()<=DETAIL_MAX_NUM_PER_INVOICE) {
				System.out.println("所有的账目可开具在一张发票中-case1");
			}
			else {
				System.out.println("每8条账目开具在一张发票中-case2");
			}
		}		
	}	
	
	/**
	 * @Title: displayBill
	 * @Description: 显示账单列表信息 
	 */
	private void displayBill(List<TestBill> billList) {		
		for(TestBill bill:billList) {
			System.out.println("序号:"+bill.getSerialNo()+  "  "+"价格:"+bill.getPrice()+" "+"数量:"+"  "+bill.getWaterNum()+" "+"金额:"+bill.getAmount());
		}
	}
	
	/**
	 * @Title: getLargerList
	 * @Description: 获取[所有账单金额>=单张发票最大额度] 的账单
	 * @param billList  整理后的账单
	 * @param maxAmountPerInvoice  单张发票最大额度
	 * @return 
	 */
	private List<TestBill> getGEList(List<TestBill> billList,int maxAmountPerInvoice){		
		List<TestBill> geList=new ArrayList<>();
		for(TestBill bill:billList) {
			if(bill.getAmount()>=maxAmountPerInvoice) {
				geList.add(bill);
			}
		}
		return geList;		
	}
	
	/**
	 * @Title: getLessList
	 * @Description: 获取[所有账单金额<单张发票最大额度] 的账单
	 * @param billList  整理后的账单
	 * @param maxAmountPerInvoice 单张发票最大额度
	 * @return 
	 */
	private List<TestBill> getLessList(List<TestBill> billList,int maxAmountPerInvoice){		
		List<TestBill> lessList=new ArrayList<>();
		for(TestBill bill:billList) {
			if(bill.getAmount()<maxAmountPerInvoice) {
				lessList.add(bill);
			}
		}
		return lessList;		
	}
	
	/**
	 * @Title: sumBills
	 * @Description: 合计账单
	 * @param billList
	 * @return 
	 */
	private Integer sumBills(List<TestBill> billList) {
		Integer sum=0;
		for(TestBill bill:billList) {
			sum=sum+bill.getAmount();
		}		
		return sum;
	}
	
	/**
	 * @Title: groupByPrice
	 * @Description: 按价格分组
	 * @param billList
	 * @param combinList
	 * @param priceMap 
	 */
	private void groupByPrice(List<TestBill> billList,Map<Integer,List<TestBill>> combinList,Map<Integer,Integer> priceMap) {
		for(TestBill bill:billList) {
			List<TestBill> groupList=combinList.get(bill.getPrice());
			if(groupList==null) {
				groupList=new ArrayList<TestBill>();
				groupList.add(bill);
				combinList.put(bill.getPrice(), groupList);
			}
			else {					
				groupList.add(bill);
				if(groupList.size()>1) {						
					priceMap.put(bill.getPrice(), groupList.size()); 
				}
			}
		}
		
	}
	
	
	/**
	 * @Title: processCombinList
	 * @Description: 将可合并的账单合并成一个账单,重新整理账单
	 * @param combinList
	 * @return 
	 */
	private  List<TestBill>  processCombinList(Map<Integer,List<TestBill>> combinList){
		List<TestBill> afterCombinList=new ArrayList<>();  				
		for(Integer price :combinList.keySet()) {
			List<TestBill> samePriceList=combinList.get(price);
			if(samePriceList.size()<=1) {  //无需合并的账单
				afterCombinList.add(samePriceList.get(0));
			}
			else {  //需合并的账单---求和--->新账单
				TestBill sumBill=new TestBill();
				sumBill.setSerialNo(-1);
				sumBill.setPrice(price);
				sumBill.setWaterNum(0);
				sumBill.setAmount(0);
				for(TestBill tempBill:samePriceList) {
					sumBill.setWaterNum(sumBill.getWaterNum()+ tempBill.getWaterNum());  //合计水量
					sumBill.setAmount(sumBill.getAmount()+tempBill.getAmount());	//合计金额
				}
				afterCombinList.add(sumBill);  //合并后的账单						
			}										
		}
		return afterCombinList;
	}
	
	public static final void main(String[] args) {
		PartitionBill partitionBill=new PartitionBill();
		partitionBill.initInvoiceObj();
		//partitionBill.initBillList1();  //初始化测试数据(账单列表-不可合并)
		partitionBill.initBillList2();  //初始化测试数据(账单列表-可合并)
		partitionBill.partitionMethod(true);
		
	}
	
	
}
