/**
 * 发票打印前端对象
	
	 	FPZL 发票种类	字符	2	是	固定值：
							0：专用发票 
							2：普通发票
							12：机动车票
							51：电子发票
		FPHM	发票号码	字符	20	是	
		FPDM	发票代码	字符	20	是	
		TCBZ	弹窗标志	字符	2	否	固定值
								0：不弹出参数设置窗口
								1：弹出参数设置窗口
	private String FPZL;	
	private String FPHM;	
	private String FPDM;	
	private String TCBZ;
 */
function InvoicePrint(){
	this.FPZL="0";	//发票种类,必填字段
	this.FPHM="";	//发票号码,必填字段
	this.FPDM="";	//发票代码,必填字段
	this.TCBZ="0";	//弹窗标志 默认采用不弹参数设置窗口方式  采用默认值即可. 不必重新赋值.
}

//-------------getter and setter---------------
//发票类种
InvoicePrint.prototype.getFPZL=function(){
	return this.FPZL;
}
InvoicePrint.prototype.setFPZL=function(fpzl){
	this.FPZL=fpzl;
}
//FPHM 发票号码
InvoicePrint.prototype.getFPHM=function(){
	return this.FPHM;
}
InvoicePrint.prototype.setFPHM=function(fphm){
	this.FPHM=fphm;
}

//FPDM 发票代码
InvoicePrint.prototype.getFPDM=function(){
	return this.FPDM;
}
InvoicePrint.prototype.setFPDM=function(fpdm){
	this.FPDM=fpdm;
}

//弹窗标志
InvoicePrint.prototype.getTCBZ=function(){
	return this.TCBZ;
}
InvoicePrint.prototype.setTCBZ=function(tcbz){
	this.TCBZ=tcbz;
}
