/**
 * 功能:
 * 		发票对象:前台
 * 		发票对象构造函数
 * 说明:
 * 		发票对象封装
 */
function Invoice(){
	this.invoiceHead=new InvoiceHead();  	//发票头
	this.invoiceDetailList=new Array();	//发票详情
}

//------------getter and setter of invoiceHead-------------
Invoice.prototype.getInvoiceHead=function(){
	return this.invoiceHead;
}

Invoice.prototype.setInvoiceHead=function(invoiceHead){
	this.invoiceHead=invoiceHead;
}

//----------------getter and setter of invoiceDetailList-----------
Invoice.prototype.setInvoiceDetailList=function(invoiceDetailList){
	this.invoiceDetailList=invoiceDetailList;
}

Invoice.prototype.getInvoiceDetailList=function(){
	return this.invoiceDetailList;
}

//-----------------------------------------
/**
 * 功能:
 * 	增加发票详情条目
 * 参数:
 * 	invoiceDetail-发票详情条目
 */
Invoice.prototype.addInvoiceDetail=function(invoiceDetail){
	this.invoiceDetailList.push(invoiceDetail);
}
