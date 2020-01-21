/**
	 
				FPZL	发票种类	字符	2	是	固定值：
								0：专用发票 
								2：普通发票
								12：机动车票
								51：电子发票
				FPHM	发票号码	字符	20	是	
				FPDM	发票代码	字符	20	是
			
			
			private String FPZL;
			private String FPHM;
			private String FPDM;
 */

function InvoiceInvalid(){
	this.FPZL="0"; //发票种类
	this.FPHM=""; //发票号码
	this.FPDM="";  //发票代码
}

//-------------getter and setter---------------
//发票类种
InvoiceInvalid.prototype.getFPZL=function(){
	return this.FPZL;
}
InvoiceInvalid.prototype.setFPZL=function(fpzl){
	this.FPZL=fpzl;
}
//FPHM 发票号码
InvoiceInvalid.prototype.getFPHM=function(){
	return this.FPHM;
}
InvoiceInvalid.prototype.setFPHM=function(fphm){
	this.FPHM=fphm;
}

//FPDM 发票代码
InvoiceInvalid.prototype.getFPDM=function(){
	return this.FPDM;
}
InvoiceInvalid.prototype.setFPDM=function(fpdm){
	this.FPDM=fpdm;
}