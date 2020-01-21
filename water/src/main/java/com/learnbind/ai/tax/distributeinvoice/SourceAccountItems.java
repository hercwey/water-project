package com.learnbind.ai.tax.distributeinvoice;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax.distributeinvoice
 *
 * @Title: InvoiceAccountItems.java
 * @Description: 待分配账目对象
 * 	用于接收自前台发送的请求参数.
 *
 * @author lenovo
 * @date 2019年12月11日 下午3:22:36
 * @version V1.0 
 *
 */
public class SourceAccountItems {
	/*
		{	
			usePrice:xxx,   1:基础水价 2:污水处理费价 3:综价
		    mergeAccountItem:true,  //true or false; 是否合并账单（按价格）
		    accountItemList:  		//账目列表
			[
		       {
		       	  "accountItemId":"xxx",  //账目id
		          "basePrice": "7.54",
				  "sewagePrice": "0",
				  "totalPrice": "7.54",
				  " realWaterAmount ": "41",
				  "baseWaterFee": "309.14",
				  "sewageWaterFee": "0",
				  "totalWaterFee": "309.14"
		       },
				{}
		  	]
		}
	*/
	private int usePrice;  //使用价格类型   1:基础水价 2:污水处理费价 3:综价
	

	private boolean mergeAccountItem;  //是否合并账单标识
	private List<SourceAccountItem> accountItemList=null;  //待分配账单列表
	
	public SourceAccountItems() {
		accountItemList=new ArrayList<>();
	}	

	public boolean isMergeAccountItem() {
		return mergeAccountItem;
	}

	public void setMergeAccountItem(boolean mergeAccountItem) {
		this.mergeAccountItem = mergeAccountItem;
	}

	public List<SourceAccountItem> getAccountItemList() {
		return accountItemList;
	}

	public void setAccountItemList(List<SourceAccountItem> accountItemList) {
		this.accountItemList = accountItemList;
	}
	
	
	public int getUsePrice() {
		return usePrice;
	}

	public void setUsePrice(int usePrice) {
		this.usePrice = usePrice;
	}
	
	
}
