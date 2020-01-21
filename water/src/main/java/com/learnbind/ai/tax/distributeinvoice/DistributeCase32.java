package com.learnbind.ai.tax.distributeinvoice;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

//开具的发票数量为整,金额为整.
public class DistributeCase32 {
	private static Log log = LogFactory.getLog(DistributeCase32.class);
	/**
	 * @Title: Case32
	 * @Description:  计算每张发票的商品数量
	 * @param price    单价
	 * @param amount   产品数量
	 * @param maxLimit  发票最大版面额度
	 * @return  每张发票中所开具的最大商品数量值
	 * 			>0时为正常
	 * 			<=0时为无法划分.
	 * 		
	 */
	private BigDecimal calcDistributeAmount(BigDecimal price,BigDecimal amount,int maxLimit) {
		BigDecimal sum=price.multiply(amount);
		
		NumberFormat currency = NumberFormat.getCurrencyInstance();//建立货币格式化引用
		sop("商品总金额为：\t" + currency.format(sum));
		
		int scale=price.scale();
		log.debug("价格的精度为:"+scale);
		log.debug("价格小数位数:"+scale);
		int increment=1;  //计算每次增量,以保证价格为整数.
		for(int i=0;i<scale;i++)
		{
			increment=increment*10;
		}
		BigDecimal issueAmount=new BigDecimal("0");
		if(price.multiply(new BigDecimal(increment)).compareTo(new BigDecimal(maxLimit))>0) {
			//System.out.println("无法按数量整数进行划分!");
			log.debug("无法按数量整数进行划分!");
			//return issueAmount;
		}
		else {
			while (true) {
				BigDecimal temp=issueAmount.add(new BigDecimal(increment));
				if(temp.multiply(price).compareTo(new BigDecimal(maxLimit))<=0) {
					issueAmount=temp;
				}
				else {
					break;
				}
			}		
			//System.out.println("每张发票的商品数量为:"+issueAmount.toString());
			log.debug("每张发票的商品数量为:"+issueAmount.toString());
			//return issueAmount;
		}
		return issueAmount;  //单张发票开具商品数量
	}
	
	private static void sop(Object s){
        //System.out.println(s);
		log.debug(s);
    }
	
	//分配算法
	public DistributeResult dispatchInvoice(SourceAccountItem bill,int maxAmountPerInvoice,BigDecimal taxRate) {
		final RoundingMode roundMode=RoundingMode.HALF_UP;
		
		DistributeResult distResult=new DistributeResult();  //分配结果
		
		//每张发票商品数量
		BigDecimal issueAmount= calcDistributeAmount(bill.getCombinPrice(),bill.getCombinRealWaterAmount(),maxAmountPerInvoice);
		//System.out.println("计算结果: 每张发票的商品数量为:"+issueAmount.toString());
		log.debug("计算结果: 每张发票的商品数量为:"+issueAmount.toString());
		//System.out.println("---------------------------------");
		log.debug("---------------------------------");
		
		if(issueAmount.compareTo(new BigDecimal("0"))==0) {
			//System.out.println("无法划分.");
			log.debug("无法划分.");
		}
		else {
			BigDecimal taxPrice=bill.getCombinPrice();  //含税价
			BigDecimal waterAmount=bill.getCombinRealWaterAmount(); //水量		 
			
			BigDecimal noTaxPrice=taxPrice.divide(taxRate.add(new BigDecimal("1")),18,roundMode);  //保留18位精度
			
			BigDecimal jeSum=new BigDecimal("0");  //金额合计
			BigDecimal seSum=new BigDecimal("0");  //税额合计
			BigDecimal roundSum=new BigDecimal("0"); //四舍五入税额合计
			
			BigDecimal totalTax=noTaxPrice.multiply(waterAmount).multiply(taxRate);  //总税额
			
			//System.out.println("总税额为:"+totalTax.setScale(2, roundMode));
			//System.out.println("----------------------------------");
			log.debug("总税额为:"+totalTax.setScale(2, roundMode));
			log.debug("----------------------------------");
			
			
			//先确定发票数量.
			int invoiceNumUp=waterAmount.divide(issueAmount, 0, RoundingMode.UP).intValue();		//小值
			int invoiceNumDown=waterAmount.divide(issueAmount, 0, RoundingMode.DOWN).intValue();  	//大值
			
			for(int i=0;i<invoiceNumDown;i++) {
				BigDecimal je=taxPrice.multiply(issueAmount);  //单张发票开具金额
				
				//System.out.println("单张No:"+(i+1));
				//System.out.println("单张发票金额为:"+je);
				log.debug("单张No:"+(i+1));
				log.debug("单张发票金额为:"+je);					
				jeSum=jeSum.add(je);  //金额合计
				
				BigDecimal se=noTaxPrice.multiply(issueAmount).multiply(taxRate);  //单发票税额
				//System.out.println("单张发票税额为:"+se.setScale(2, roundMode));
				log.debug("单张发票税额为:"+se.setScale(2, roundMode));
				
				seSum=seSum.add(se);  //税额合计				
				roundSum=roundSum.add(se.setScale(2, roundMode));
				
				//生成一张新的发票
				ResultAccountItems oneInvoice=  distResult.genOneInvoice();  //生成一张新的发票
				ResultAccountItem item=new ResultAccountItem();
				item.setAccountItemIds(bill.getCombinAccountItemIds());  	//账单IDS
				item.setInvoicePrice(bill.getCombinPrice());  				//开票价格
				item.setInvoiceWaterAmount(issueAmount);  					//开票水量
				item.setInvoiceAmount(je.setScale(2, roundMode));  			//开票金额
				item.setInvoiceTaxAmount(se.setScale(2, roundMode));  		//开票税额
				item.setTaxRate(taxRate);  //开票税率
				
				oneInvoice.addAccountItem(item);
				
			}
			//System.out.println("求整开票金额合计为:"+jeSum.setScale(2, roundMode));
			//System.out.println("求整开票税额合计为:"+seSum.setScale(2, roundMode));
			//System.out.println("Round求整开票税额合计为:"+roundSum);
			//System.out.println("---------------------------------");
			
			log.debug("求整开票金额合计为:"+jeSum.setScale(2, roundMode));
			log.debug("求整开票税额合计为:"+seSum.setScale(2, roundMode));
			log.debug("Round求整开票税额合计为:"+roundSum);
			log.debug("---------------------------------");
			
			if(invoiceNumUp!=invoiceNumDown) {  //如果不相等  有最后一张金额非整发票
				
				BigDecimal remainNum=waterAmount.subtract(issueAmount.multiply(new BigDecimal(invoiceNumDown)));
				
				BigDecimal remainJe=taxPrice.multiply(remainNum);  //剩余数量金额
				//System.out.println("最后一张发票金额为:"+remainJe.setScale(2, roundMode));
				log.debug("最后一张发票金额为:"+remainJe.setScale(2, roundMode));
				jeSum=jeSum.add(remainJe);  //金额合计
				
				BigDecimal remainSe=noTaxPrice.multiply(remainNum).multiply(taxRate);  //单发票税额
				seSum=seSum.add(remainSe);  //税额合计
				roundSum=roundSum.add(remainSe.setScale(2, roundMode));
				//System.out.println("最后一张发票税额为:"+remainSe.setScale(2, roundMode));
				log.debug("最后一张发票税额为:"+remainSe.setScale(2, roundMode));
				
				//修正最后一张发票的税额.
				BigDecimal roundRemainSe=remainSe.setScale(2, roundMode);  //四舍五入后税额(最后一张发票)
				BigDecimal mValue=roundSum.subtract(seSum);  //修正值
				mValue=mValue.setScale(2, roundMode);  //四舍五入修正值;
				//System.out.println("修正值为:"+mValue.setScale(2, roundMode));
				log.debug("修正值为:"+mValue.setScale(2, roundMode));
				if(mValue.compareTo(new BigDecimal("0"))< 0) {
					roundRemainSe=roundRemainSe.add(mValue.abs());
				}
				else {
					roundRemainSe=roundRemainSe.subtract(mValue.abs());
				}
				//System.out.println("最后一张发票税额为(修正后):"+roundRemainSe.setScale(2, roundMode));
				log.debug("最后一张发票税额为(修正后):"+roundRemainSe.setScale(2, roundMode));				
				
				//生成一张新的发票
				ResultAccountItems oneInvoice=  distResult.genOneInvoice();  //生成一张新的发票
				ResultAccountItem item=new ResultAccountItem();
				item.setAccountItemIds(bill.getCombinAccountItemIds());  	//账单IDS
				item.setInvoicePrice(bill.getCombinPrice());  				//开票价格
				item.setInvoiceWaterAmount(remainNum);  					//开票水量
				item.setInvoiceAmount(remainJe.setScale(2, roundMode));  			//开票金额
				item.setInvoiceTaxAmount(roundRemainSe.setScale(2, roundMode));  		//开票税额(采用修正后的值)
				item.setTaxRate(taxRate);  //开票税率				
				oneInvoice.addAccountItem(item);
				
			}
			else {
				//System.out.println("账单余额发票为:无");
				log.debug("账单余额发票为:无");
			}
			
			//System.out.println("---------------------------------");
			//System.out.println("合计开票总金额为:"+jeSum.setScale(2, roundMode));
			//System.out.println("合计开票总税额为:"+seSum.setScale(2, roundMode));
			//System.out.println("Round合计开票总税额为:"+roundSum);
			log.debug("---------------------------------");
			log.debug("合计开票总金额为:"+jeSum.setScale(2, roundMode));
			log.debug("合计开票总税额为:"+seSum.setScale(2, roundMode));
			log.debug("Round合计开票总税额为:"+roundSum);
			
			BigDecimal sub=roundSum.subtract(seSum);  //按分配后发票四舍五入后的税额-按总额计算后的总税额			
			//System.out.println("税额的差值是:"+sub);
			log.debug("税额的差值是:"+sub);
		}	
		
		return distResult;
	}
	
}
