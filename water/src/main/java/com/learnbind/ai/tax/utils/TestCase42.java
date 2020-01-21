package com.learnbind.ai.tax.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax.utils
 *
 * @Title: TestCase42.java
 * @Description: 场景4.2 条件: C1:多个条目合计值>100万元 C2:每条目金额<100万元 分配算法:
 *               多个条目组合分配开具在多张发票中。
 *
 * @author lenovo
 * @date 2019年12月9日 上午10:19:06
 * @version V1.0
 *
 */
public class TestCase42 {

	// (1)发票对象
	private InvoiceVolInfo testInvoice = new InvoiceVolInfo(); // 发票属性对象 只用于描述发票的属性
	private List<CustBill> billList = new ArrayList<>(); // 账单列表
	// private boolean isMerge=false; //是否合并

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
	 * @Title: initBillList 测试数据 test data
	 * @Description: 初始化账单列表 水量 单价 金额 1 1 1 2 2 4 3 3 9 4 4 16 5 5 25 36 49 64 81
	 *               100
	 */
	private void initBillList1() {
		for (int i = 0; i < 9; i++) {
			CustBill bill = new CustBill();
			bill.setSerialNo(i + 1);
			bill.setWaterNum(i + 1);
			bill.setPrice(i + 1);
			bill.setAmount((i + 1) * (i + 1));
			billList.add(bill);
		}
	}

	/**
	 * @Title: initBillList2 测试数据 test data
	 * @Description: 初始化账单, 价格相同场景. 账单条数 单价 5 2 5 3
	 * 
	 */
	private void initBillList2() {
		for (int i = 0; i < 5; i++) {
			CustBill bill = new CustBill();
			bill.setSerialNo(i + 1);
			bill.setWaterNum(i + 1);
			bill.setPrice(2);
			bill.setAmount((i + 1) * bill.getPrice());
			billList.add(bill);
		}

		for (int i = 0; i < 5; i++) {
			CustBill bill = new CustBill();
			bill.setSerialNo(i + 1);
			bill.setWaterNum(i + 1);
			bill.setPrice(3);
			bill.setAmount((i + 1) * bill.getPrice());
			billList.add(bill);
		}
	}

	class MyComparator implements Comparator<CustBill> {
		@Override
		public int compare(CustBill o1, CustBill o2) {
			// TODO Auto-generated method stub
			if (o1.getAmount() > o2.getAmount())
				return -1;
			else if (o1.getAmount() < o2.getAmount())
				return 1;
			else {
				return 0;
			}
		}

	}

	/**
	 * @Title: displayBill
	 * @Description: 显示账单列表信息
	 */
	private void displayBill(List<CustBill> billList) {
		int i=0;
		for (CustBill bill : billList) {
			System.out.println("Index:"+ (i++)+    "序号:" + bill.getSerialNo() + "  " + "价格:" + bill.getPrice() + " " + "数量:" + "  "
					+ bill.getWaterNum() + " " + "金额:" + bill.getAmount());
		}
	}

	private void dispatchInvoice(List<CustBill> billList, int maxAmountPerInvoice) {
		final int MAX_DETAIL_NUM_PER_INVOICE = 5; // 单张发票最大条目数
		Set<Integer> initGroup = new HashSet<Integer>(); // 声明一个集合

		// 排序后的列表为
		System.out.println("排序前列表:");
		displayBill(billList);

		// (1)先将billList中的元素排序(逆序,自大到小)
		billList.sort(new MyComparator());
		// 排序后的列表为
		System.out.println("排序后列表(倒序):");
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
						int gSum = groupSum(billList, newSet);
						int preCalcSum = gSum + billList.get(tailPointer).getAmount();
						if (gItems < MAX_DETAIL_NUM_PER_INVOICE && preCalcSum <= maxAmountPerInvoice) {
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
					System.out.println("do nothing");
				}
			} 
			else {
				break;  //已经分配完毕
			}
		}
		
		//display group result  显示分组结果.
		for(Integer groupNo:resultMap.keySet()) {
			Set<Integer> group=resultMap.get(groupNo);
			//System.out.println("分组:"+groupNo);
			String indexStr="";
			for(Integer index: group) {
				indexStr=indexStr+" ,"+index;				
			}
			System.out.println("分组号:"+groupNo+" "+"分组索引:"+indexStr);
		}

	}

	private int groupSum(List<CustBill> billList, Set<Integer> gset) {
		int sum = 0;
		for (Integer index : gset) {
			sum = sum + billList.get(index).getAmount();
		}
		return sum;
	}

	public static final void main(String[] args) {
		TestCase42 testCase = new TestCase42();
		testCase.initBillList1();
		testCase.dispatchInvoice(testCase.billList, 100);
	}

}
