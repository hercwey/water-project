package com.learnbind.ai.tax.entity;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

//预分配发票
public class PreDistInvoice {
//				"invoiceAccountItems": [
//	    					{
//	    						"accountItemIds": "14543,",
//	    						"invoiceAmount": 44241.03,
//	    						"invoicePrice": 4.71,
//	    						"invoiceTaxAmount": 1288.57,
//	    						"invoiceWaterAmount": 9393,
//	    						"taxRate": 0.03
//	    					},
//	    					{
//	    						"accountItemIds": "14546,",
//	    						"invoiceAmount": 47215.48,
//	    						"invoicePrice": 7.54,
//	    						"invoiceTaxAmount": 1375.21,
//	    						"invoiceWaterAmount": 6262,
//	    						"taxRate": 0.03
//	    					}
//	    				],
//	    				"BZ": "",
//	    				"FPDM": "1300141140",
//	    				"FPHM": 14907748
	
	private List<PreDistAccountItem> invoiceAccountItems=null;
	
	private String BZ="";
	private String FPDM="";
	private String FPHM="";
	
	public PreDistInvoice() {
		this.invoiceAccountItems=new ArrayList<PreDistAccountItem>();
	}

	public List<PreDistAccountItem> getInvoiceAccountItems() {
		return invoiceAccountItems;
	}

	public void setInvoiceAccountItems(List<PreDistAccountItem> invoiceAccountItems) {
		this.invoiceAccountItems = invoiceAccountItems;
	}

	public String getBZ() {
		return BZ;
	}

	public void setBZ(String bZ) {
		BZ = bZ;
	}

	public String getFPDM() {
		return FPDM;
	}

	public void setFPDM(String fPDM) {
		FPDM = fPDM;
	}

	public String getFPHM() {
		return FPHM;
	}

	public void setFPHM(String fPHM) {
		FPHM = fPHM;
	}
	
	public static final void main(String[] args) {
		String testjson="{\r\n" + 
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
				"				\"BZ\": \"\",\r\n" + 
				"				\"FPDM\": \"1300141140\",\r\n" + 
				"				\"FPHM\": 14907748\r\n" + 
				"			}";
		PreDistInvoice distInvoice=JSONObject.parseObject(testjson, PreDistInvoice.class);
		
		System.out.println("size is:"+distInvoice.getInvoiceAccountItems().size());
		
		
	}
	
	
	    				
	    				
}
