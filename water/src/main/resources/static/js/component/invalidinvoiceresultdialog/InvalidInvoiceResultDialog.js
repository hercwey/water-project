/**
 * 作废发票进度对话框组件  JS部分
 * 参数:
 * 	dialogId:对话框ID
 */
function InvalidInvoiceResultDialog(dialogId){
	this.dialogId=dialogId;  //对话框id
	this.title=""; 		 	 //title
	this.message=""; 		 //message
	
	//this.invoiceType=""; 	 //发票类型
	//this.invoiceDM="";  	 //发票代码
	//this.invoiceHM="";  	 //发票号码

	//this.refreshValue="100";  //进度条刷新速度

	this.setDefault();  	//设置默认值	
}

/**
 * 设置默认值(title and message)
 */
InvalidInvoiceResultDialog.prototype.setDefault=function(){
	//set default value of title and message
	this.title="确认提示";
	this.message="本次未开票作废共1张<br>作废成功:1张,<br>作废失败:0张.";
	
	this.setTitle(this.title);
	this.setMessage(this.message);	
}

//------------打开,关闭对话框-----------------
InvalidInvoiceResultDialog.prototype.show=function(){
	console.log("dialog id is:"+this.dialogId);
	DialogUtil.openDialogFn(this.dialogId);
}

InvalidInvoiceResultDialog.prototype.close=function(){
	DialogUtil.closeDialogFn(this.dialogId);
}

//----------------对话框属性设置(字符串属性)------------
/**
 * set title
 */
InvalidInvoiceResultDialog.prototype.setTitle=function(title){
	//define selector constants of title and message
	const DIALOG_TITLE="#"+this.dialogId+" "+".dialog-title";  		//title component selector	
	this.title=title;
	//set title,message's value
	$(DIALOG_TITLE).html(this.title);
	
}

/**
 * set message
 */
InvalidInvoiceResultDialog.prototype.setMessage=function(message){
	const DIAOG_MESSAGE="#"+this.dialogId+" "+".dialog-message";  	//message component selector
	this.message=message;
	$(DIAOG_MESSAGE).html(this.message);
}
