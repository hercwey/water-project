/**
 * 购方,销方开票信息对象
 */

function IssueInvoiceInfo(){
	this.customerId="";
	this.name = "";  //开票名称
	this.customerName="";           //客户名称
	this.taxNo = "";
	this.address = "";
	this.tel = "";
	this.bank = "";
	this.accountNo = "";
	this.checker="";  //复核人
	this.payee="";    //收款人
	this.operator="";  //开票人
}

//customerId
IssueInvoiceInfo.prototype.getCustomerId=function(){
	return this.customerId;
}                           
IssueInvoiceInfo.prototype.setCustomerId=function(customerId){
	this.customerId=customerId;
}
//name
IssueInvoiceInfo.prototype.getName=function(){
	return this.name;
}
IssueInvoiceInfo.prototype.setName=function(name){
	this.name=name;
}
//taxNo
IssueInvoiceInfo.prototype.getTaxNo=function(){
	return this.taxNo;
}
IssueInvoiceInfo.prototype.setTaxNo=function(taxNo){
	this.taxNo=taxNo;
}
//address
IssueInvoiceInfo.prototype.getAddress=function(){
	return this.address;
}
IssueInvoiceInfo.prototype.setAddress=function(address){
	this.address=address;
}
//tel
IssueInvoiceInfo.prototype.getTel=function(){
	return this.tel;	
}
IssueInvoiceInfo.prototype.setTel=function(tel){
	this.tel=tel;
}
//bank
IssueInvoiceInfo.prototype.getBank=function(){
	return this.bank;
}
IssueInvoiceInfo.prototype.setBank=function(bank){
	this.bank=bank;
}
//accountNo
IssueInvoiceInfo.prototype.getAccountNo=function(){
	return this.accountNo;
}
IssueInvoiceInfo.prototype.setAccountNo=function(accountNo){
	this.accountNo=accountNo;
}
//checker
IssueInvoiceInfo.prototype.getChecker=function(){
	return this.checker;
}
IssueInvoiceInfo.prototype.setChecker=function(checker){
	this.checker=checker;
}
//payee
IssueInvoiceInfo.prototype.getPayee=function(){
	return this.payee;
}
IssueInvoiceInfo.prototype.setPayee=function(payee){
	this.payee=payee;
}
//operator
IssueInvoiceInfo.prototype.getOperator=function(){
	return this.operator;
}
IssueInvoiceInfo.prototype.setOperator=function(operator){
	this.operator=operator;
}

IssueInvoiceInfo.prototype.setCustomerName=function(customerName){
	this.customerName=customerName
}

IssueInvoiceInfo.prototype.getCustomerName=function(){
	return this.customerName;
}


//reset
IssueInvoiceInfo.prototype.reset=function(){
	this.customerId="";
	this.name = "";
	this.taxNo = "";
	this.address = "";
	this.tel = "";
	this.bank = "";
	this.accountNo = "";
}