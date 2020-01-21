function TaxInvoiceRedDialogFun(redDialogId, openInvoiceRedCallback){
	//发票冲红对话框
	this.dialogId = redDialogId;
	this.openInvoiceRedCallback = openInvoiceRedCallback;
	this.invocieId = null; 
	this.fpzl = null; 
	
}


//-------------------PAGE LOADED READY--------------------

/*
* 	打开(Open) 发票冲红对话框
*/
TaxInvoiceRedDialogFun.prototype.open=function(invoiceId, fpzl) {
	var self = this;
	self.invoiceId = invoiceId;
	self.fpzl = fpzl;
	if(fpzl == "0"){//选择的是专用发票
		//清空普通发票
		 $(".general-tax-red-main").empty();
		self.loadInvocieRedMain();
	} else{
		//清空专用发票
		 $(".tax-red-main").empty();
		self.loadGeneralInvocieRedMain();
	}
	
	DialogUtil.openDialogFn(self.dialogId);
}
/* 
关闭(CLOSE) 发票冲红对话框
*/
TaxInvoiceRedDialogFun.prototype.close=function() {
	var self = this;
	
	
	DialogUtil.closeDialogFn(self.dialogId);
}

//专用发票冲红
TaxInvoiceRedDialogFun.prototype.loadInvocieRedMain=function() {
	var self = this;
	const DIALOG_ID = "#"+self.dialogId;
	const RED_MAIN = DIALOG_ID + " .tax-red-main";
	var url = "/tax-invoice-red/load-tax-red-main";
	var parms = new Object();
	parms.dialogId = self.dialogId;
	$(RED_MAIN).load(url, parms, function(){
		const CONTAIN_ID =  self.dialogId+"-table";
		var taxInvoiceRedForm=new TaxInvoiceRedForm(CONTAIN_ID, self.openInvoiceRedCallback);
		taxInvoiceRedForm.loadPage(self.invoiceId, self);
	});
	
}

//普通发票冲红
TaxInvoiceRedDialogFun.prototype.loadGeneralInvocieRedMain=function() {
	var self = this;
	const DIALOG_ID = "#"+self.dialogId;
	const RED_MAIN = DIALOG_ID + " .general-tax-red-main";
	var url = "/tax-invoice-red/load-general-tax-red-main";
	var parms = new Object();
	parms.dialogId = self.dialogId;
	$(RED_MAIN).load(url, parms, function(){
		const CONTAIN_ID =  self.dialogId+"-table";
		var taxGeneralInvoiceRedForm=new TaxGeneralInvoiceRedForm(CONTAIN_ID, self.openInvoiceRedCallback);
		taxGeneralInvoiceRedForm.loadGeneralPage(self.invoiceId, self);
	});
	
}








