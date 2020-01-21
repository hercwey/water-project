/**
 * 发票号码确认对话框组件
 * 参数:
 * 	dialogId:对话框ID
 */
function InvoiceHMConfirmDialog(dialogId){
	this.dialogId=dialogId;  //对话框id
	this.title=""; 		 	 //title
	this.message=""; 		 //message
	this.invoiceType=""; 	 //发票类型
	this.invoiceDM="";  	 //发票代码
	this.invoiceHM="";  	 //发票号码

	this.buttonOkCallback=null;  		//确认按钮回调函数
	this.buttonCancelCallback=null;  	//取按按钮回调函数	
	
	this.setDefault();  //设置默认值
	this.initListener();  //初始化按钮click listener
}

/**
 * 设置默认值(title and message)
 */
InvoiceHMConfirmDialog.prototype.setDefault=function(){
	//set default value of title and message
	this.title="发票号码确认";
	this.message="现在显示的为将要开具的发票的种类、代码、号码，请认真核对装入打印机中的纸质发票的种类、代码、号码是否与之一致，如一致，可执行打印操作；如不一致，请予以更换。请确认是否填写本张发票？";

	this.setTitle(this.title);
	this.setMessage(this.message);	
}

/**
 * 初始化按钮click event listener
 */
InvoiceHMConfirmDialog.prototype.initListener=function(){
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
InvoiceHMConfirmDialog.prototype.show=function(){
	console.log("dialog id is:"+this.dialogId);
	DialogUtil.openDialogFn(this.dialogId);
}

InvoiceHMConfirmDialog.prototype.close=function(){
	DialogUtil.closeDialogFn(this.dialogId);
}

//----------------对话框属性设置(字符串属性)------------

/**
 * set title
 */
InvoiceHMConfirmDialog.prototype.setTitle=function(title){
	//define selector constants of title and message
	const DIALOG_TITLE="#"+this.dialogId+" "+".dialog-title";  		//title component selector	
	this.title=title;
	//set title,message's value
	$(DIALOG_TITLE).html(this.title);
	
}

/**
 * set message
 */
InvoiceHMConfirmDialog.prototype.setMessage=function(message){
	const DIAOG_MESSAGE="#"+this.dialogId+" "+".dialog-message";  	//message component selector
	this.message=message;
	$(DIAOG_MESSAGE).html(this.message);
}


/**
 * set invoice type
 * 设置发票类型
 * 	取值:普通发票  专用发票
 */
InvoiceHMConfirmDialog.prototype.setInvoiceType=function(invoiceType){
	const INVOICE_TYPE="#"+this.dialogId+" "+".invoice-type";
	this.invoiceType=invoiceType;
	$(INVOICE_TYPE).html(this.invoiceType);
}
/**
 * set invoice dm
 * 设置发票代码
 */
InvoiceHMConfirmDialog.prototype.setInvoiceDM=function(invoiceDM){
	const INVOICE_DM="#"+this.dialogId+" "+".invoice-dm";
	this.invoiceDM=invoiceDM;
	$(INVOICE_DM).html(this.invoiceDM);
}
/**
 * set invoice hm
 * 设置发票号码
 */
InvoiceHMConfirmDialog.prototype.setInvoiceHM=function(invoiceHM){
	const INVOICE_HM="#"+this.dialogId+" "+".invoice-hm";
	this.invoiceHM=invoiceHM;
	$(INVOICE_HM).html(this.invoiceHM);
}

//----------------对话框属性设置(回调函数属性)----------
/**
 * 设置buttonOK click callback function
 */
InvoiceHMConfirmDialog.prototype.setButtonOkCallback=function(buttonOkCallback){
	this.buttonOkCallback=buttonOkCallback;
}
/**
 * set buttonCancel callback function
 */
InvoiceHMConfirmDialog.prototype.setButtonCancelCallback=function(buttonCancelCallback){
	this.buttonCancelCallback=buttonCancelCallback;

}