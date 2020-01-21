/**
 * 功能:
 * 	发票中的附加信息,类似于JAVA中的POJO类
 * 
 * 使用说明:
 * 	(1)此处的信息只用于显示,显示某张发票时使用
 *  (2)在发票开具时无需提交
 * 	
 *  发票附加信息对象-构造函数.
 */
function InvoiceAddition(){

	this.invoiceDM="";  //发票代码
	this.invoiceHM="";  //发票号码
	this.issueInvoiceDate="";  //发票开具日期   显示的格式为:2019年11月01日  值的格式为:  2019-11-01

	this.salerName="";  	//销方名称
	this.salerTaxNo="";  	//销方税号

	this.invoiceAmountSum="";   //合计-金额
	this.invoiceTaxSum="";		//合计-税

	this.invoicePriceTaxSumChinese="";  //价税合计中文
	this.invoicePriceTaxSumNumber="";  //价税合计数字	
}

//-------invoiceDM----getter and setter----------
InvoiceAddition.prototype.getInvoiceDM=function(){
	return this.invoiceDM;
}

InvoiceAddition.prototype.setInvoiceDM=function(invoiceDM){
	this.invoiceDM=invoiceDM;
}

//-------invoiceHM----getter and setter--------------
InvoiceAddition.prototype.getInvoiceHM=function(){
	return this.invoiceHM;
}

InvoiceAddition.prototype.setInvoiceHM=function(invoiceHM){
	this.invoiceHM=invoiceHM;
}

//---------issueInvoiceDate  getter and setter----------------
InvoiceAddition.prototype.getIssueInvoiceDate=function(){
	return this.issueInvoiceDate;
}

InvoiceAddition.prototype.setIssueInvoiceDate=function(issueDate){
	this.issueInvoiceDate=issueDate;
}

//--------salerName  getter and setter-------------
InvoiceAddition.prototype.getSalerName=function(){
	return this.salerName;
}

InvoiceAddition.prototype.setSalerName=function(salerName){
	this.salerName=salerName;
}

//---------salerTaxNo  getter and setter-----------
InvoiceAddition.prototype.getSalerTaxNo=function(){
	return this.salerTaxNo;
}
InvoiceAddition.prototype.setSalerTaxNo=function(salerTaxNo){
	this.salerTaxNo=salerTaxNo;
}

//---------invoiceAmountSum getter and setter---------
InvoiceAddition.prototype.getInvoiceAmountSum=function(){
	return this.invoiceAmountSum;
}
InvoiceAddition.prototype.setInvoiceAmountSum=function(amountSum){
	this.invoiceAmountSum=amountSum;
}

//---------invoiceTaxSum  getter and setter----------
InvoiceAddition.prototype.getInvoiceTaxSum=function(){
    return this.invoiceTaxSum;
}
InvoiceAddition.prototype.setInvoiceTaxSum=function(taxSum){
	this.invoiceTaxSum=taxSum;
}

//-------------invoicePriceTaxSumChinese---getter and setter-------------------
InvoiceAddition.prototype.getInvoicePriceTaxSumChinese=function(){
	return this.invoicePriceTaxSumChinese;
}

InvoiceAddition.prototype.setInvoicePriceTaxSumChinese=function(priceTaxSumChinese){
	this.invoicePriceTaxSumChinese=priceTaxSumChinese;
}

//---------------invoicePriceTaxSumNumber  getter and setter----------------
InvoiceAddition.prototype.getInvoicePriceTaxSumNumber=function(){
	return this.invoicePriceTaxSumNumber;
}

InvoiceAddition.prototype.setInvoicePriceTaxSumNumber=function(priceTaxSumNumber){
	this.invoicePriceTaxSumNumber=priceTaxSumNumber;
}
//reset
InvoiceAddition.prototype.reset=function(){
	this.invoiceDM="";  //发票代码
	this.invoiceHM="";  //发票号码
	this.issueInvoiceDate=new Date();  //发票开具日期   显示的格式为:2019年11月01日  值的格式为:  2019-11-01

	this.salerName="";  	//销方名称
	this.salerTaxNo="";  	//销方税号

	this.invoiceAmountSum="0.00";   //合计-金额
	this.invoiceTaxSum="0.00";		//合计-税

	this.invoicePriceTaxSumChinese="零圆整";  //价税合计中文
	this.invoicePriceTaxSumNumber="0.00";  //价税合计数字	
}