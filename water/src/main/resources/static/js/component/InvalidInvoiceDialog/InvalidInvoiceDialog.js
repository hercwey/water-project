/**
 * 未开发票号码作废确认对话框组件
 * 参数:
 * 	dialogId:对话框ID
 */
function InvalidInvoiceDialog(dialogId){
	this.dialogId=dialogId;  //对话框id
	this.title=""; 		 	 //title
	this.message=""; 		 //message
	this.invoiceType=""; 	 //发票类型名称
	this.invoiceDM="";  	 //发票代码
	this.invoiceHM="";  	 //发票号码
	
	this.invoiceNum="0";	//发票份数
	this.invalidNum="0";	//作废份数	

	this.buttonOkCallback=null;  		//确认按钮回调函数
	this.buttonCancelCallback=null;  	//取按按钮回调函数	
	
	this.setDefault();    //设置默认值
	this.initListener();  //初始化按钮click listener
}

/**
 * 设置默认值(title and message)
 */
InvalidInvoiceDialog.prototype.setDefault=function(){
	//set default value of title and message
	this.title="未开发票号作废,发票号码确认";
	this.message="批量作废发票，请录入要作废的发票份数，点击<strong>【确定】</strong>按钮,系统将自动作废指定份数的发票;点击<strong >【取消】</strong>按钮，将退出批量作废功能。";
	this.invalidNum="1"; //默认作废数量

	this.setTitle(this.title);	
	this.setMessage(this.message);	
	this.setInvalidNum(this.invalidNum);  //设置作废数量
}

/**
 * 初始化按钮click event listener
 */
InvalidInvoiceDialog.prototype.initListener=function(){
	var self=this;

	//按钮选择器常量定义
	const BUTTON_OK="#"+this.dialogId+" "+".button-ok";  
	const BUTTON_CANCEL="#"+this.dialogId+" "+".button-cancel";

	$(BUTTON_OK).on('click',buttonOkClickFunc);
	$(BUTTON_CANCEL).on('click',buttonCancelClickFunc);

	//button ok click event listener
	function buttonOkClickFunc(){
		self.close();  //关闭对话框
		console.log("button ok clicked!");
		if(self.buttonOkCallback!=null){
			self.buttonOkCallback();
		}
	}

	//button cancel click event listener
	function buttonCancelClickFunc(){
		self.close();  //关闭对话框
		if(self.buttonCancelCallback!=null){
			self.buttonCancelCallback();
		}
	}

}


//------------打开,关闭对话框-----------------
InvalidInvoiceDialog.prototype.show=function(){
	console.log("dialog id is:"+this.dialogId);
	DialogUtil.openDialogFn(this.dialogId);
}

InvalidInvoiceDialog.prototype.close=function(){
	DialogUtil.closeDialogFn(this.dialogId);
}

//----------------对话框属性设置(字符串属性)------------

/**
 * set title
 */
InvalidInvoiceDialog.prototype.setTitle=function(title){
	//define selector constants of title and message
	const DIALOG_TITLE="#"+this.dialogId+" "+".dialog-title";  		//title component selector	
	this.title=title;
	//set title,message's value
	$(DIALOG_TITLE).html(this.title);
	
}

/**
 * set message
 */
InvalidInvoiceDialog.prototype.setMessage=function(message){
	const DIAOG_MESSAGE="#"+this.dialogId+" "+".dialog-message";  	//message component selector
	this.message=message;
	$(DIAOG_MESSAGE).html(this.message);
}


/**
 * set invoice type
 * 设置发票类型
 * 	取值:普通发票  专用发票
 */
InvalidInvoiceDialog.prototype.setInvoiceType=function(invoiceType){
	const INVOICE_TYPE="#"+this.dialogId+" "+".invoice-type";
	this.invoiceType=invoiceType;
	$(INVOICE_TYPE).html(this.invoiceType);
}
/**
 * set invoice dm
 * 设置发票代码
 */
InvalidInvoiceDialog.prototype.setInvoiceDM=function(invoiceDM){
	const INVOICE_DM="#"+this.dialogId+" "+".invoice-dm";
	this.invoiceDM=invoiceDM;
	$(INVOICE_DM).html(this.invoiceDM);
}

// getter of 发票代码
InvalidInvoiceDialog.prototype.getInvoiceDM=function(){
	return this.invoiceDM;
}

/**
 * set invoice hm
 * 设置发票号码
 */
InvalidInvoiceDialog.prototype.setInvoiceHM=function(invoiceHM){
	const INVOICE_HM="#"+this.dialogId+" "+".invoice-hm";
	this.invoiceHM=invoiceHM;
	$(INVOICE_HM).html(this.invoiceHM);
}

// getter of invoiceHM
InvalidInvoiceDialog.prototype.getInvoiceHM=function(){
	return this.invoiceHM;
}


/**
 * 设置发票张数(份数)
 */
InvalidInvoiceDialog.prototype.setInvoiceNum=function(invoiceNum){
	const INVOICE_NUM="#"+this.dialogId+" "+".invoice-num";
	this.invoiceNum=invoiceNum;
	$(INVOICE_NUM).html(this.invoiceNum); 
}

/**
 * setter:要作废的发票张数
 */
InvalidInvoiceDialog.prototype.setInvalidNum=function(invalidNum){
	const INVALID_NUM="#"+this.dialogId+" "+".invalid-num";
	this.invalidNum=invalidNum;
	$(INVALID_NUM).val(invalidNum);
}

/**
 * getter: invalidNum (要作废的发票数量)
 */
InvalidInvoiceDialog.prototype.getInvalidNum=function(){
	const INVALID_NUM="#"+this.dialogId+" "+".invalid-num";
	var returnValue=$(INVALID_NUM).val();
	return returnValue;
}



//----------------对话框属性设置(回调函数属性)----------
/**
 * 设置buttonOK click callback function
 */
InvalidInvoiceDialog.prototype.setButtonOkCallback=function(buttonOkCallback){
	this.buttonOkCallback=buttonOkCallback;
}
/**
 * set buttonCancel callback function
 */
InvalidInvoiceDialog.prototype.setButtonCancelCallback=function(buttonCancelCallback){
	this.buttonCancelCallback=buttonCancelCallback;

}