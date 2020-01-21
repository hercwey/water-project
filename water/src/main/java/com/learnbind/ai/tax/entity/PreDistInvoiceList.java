package com.learnbind.ai.tax.entity;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

public class PreDistInvoiceList {
	List<PreDistInvoice> invoiceList;
	
	public PreDistInvoiceList() {
		this.invoiceList=new ArrayList<>();
	}
	
	public List<PreDistInvoice> getInvoiceList() {
		return invoiceList;
	}
	
	public void setInvoiceList(List<PreDistInvoice> invoiceList) {
		this.invoiceList = invoiceList;
	}

	public static final void main(String[] args) {
		String testJSON="{\r\n" + 
				"		\"invoiceList\": [\r\n" + 
				"			{\r\n" + 
				"				\"invoiceAccountItems\": [\r\n" + 
				"					{\r\n" + 
				"						\"accountItemIds\": \"14543,\",\r\n" + 
				"						\"invoiceAmount\": 44241.03,\r\n" + 
				"						\"invoicePrice\": 4.71,\r\n" + 
				"						\"invoiceTaxAmount\": 1288.57,\r\n" + 
				"						\"invoiceWaterAmount\": 9393,\r\n" + 
				"						\"taxRate\": 0.03\r\n" + 
				"					},\r\n" + 
				"					{\r\n" + 
				"						\"accountItemIds\": \"14546,\",\r\n" + 
				"						\"invoiceAmount\": 47215.48,\r\n" + 
				"						\"invoicePrice\": 7.54,\r\n" + 
				"						\"invoiceTaxAmount\": 1375.21,\r\n" + 
				"						\"invoiceWaterAmount\": 6262,\r\n" + 
				"						\"taxRate\": 0.03\r\n" + 
				"					}\r\n" + 
				"				],\r\n" + 
				
				"			},\r\n" + 
				"			{\r\n" + 
				"				\"invoiceAccountItems\": [\r\n" + 
				"					{\r\n" + 
				"						\"accountItemIds\": \"14545,\",\r\n" + 
				"						\"invoiceAmount\": 152114.16,\r\n" + 
				"						\"invoicePrice\": 4.71,\r\n" + 
				"						\"invoiceTaxAmount\": 4430.51,\r\n" + 
				"						\"invoiceWaterAmount\": 32296,\r\n" + 
				"						\"taxRate\": 0.03\r\n" + 
				"					},\r\n" + 
				"					{\r\n" + 
				"						\"accountItemIds\": \"14544,\",\r\n" + 
				"						\"invoiceAmount\": 162343.74,\r\n" + 
				"						\"invoicePrice\": 7.54,\r\n" + 
				"						\"invoiceTaxAmount\": 4728.46,\r\n" + 
				"						\"invoiceWaterAmount\": 21531,\r\n" + 
				"						\"taxRate\": 0.03\r\n" + 
				"					}\r\n" + 
				"				],\r\n" + 
				"				\"BZ\": \"\",\r\n" + 
				"				\"FPDM\": \"1300141140\",\r\n" + 
				"				\"FPHM\": 14907749\r\n" + 
				"			}\r\n" + 
				"		]\r\n" + 
				"	}";
		PreDistInvoiceList invoiceList=  JSONObject.parseObject(testJSON, PreDistInvoiceList.class);
		System.out.println("列表长度为:"+invoiceList.getInvoiceList().size());
	}
	
	
}
