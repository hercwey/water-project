//--------------------公共查询函数--------------------------
		
function SelectCustomerBillDialog(dialogId, selectedCallback){
	this.dialogId = dialogId; //选择客户发票信息对话框ID，与UI中的ID相对应
	this.containerId = null;//对话框内容器ID
	this.customerId = null;//客户ID
	this.customerName = null;//客户名称
	this.selectedCallback = selectedCallback;//用户选中后的回调函数
	this.selectedBillArr = new Array();//用户选择的客户账单列表
	
}

/**
 * 设置对话框header部分中的客户名称
 */
SelectCustomerBillDialog.prototype.setDialogHeadCustomerName = function(){
	var self = this;
	const PREFIX = "#"+self.dialogId;
	const DIALOG_HEADER_CUSTOMER_NAME_ID = PREFIX+" #dialog-header-customer-name";
	const customerName = self.customerName;
	if(customerName!=null && customerName!=""){
		$(DIALOG_HEADER_CUSTOMER_NAME_ID).text(customerName);
	}
	
}

/**
 * 	加载选择账单页面
 */
SelectCustomerBillDialog.prototype.loadPage = function() {
	var self = this;
	var PREFIX = "#"+self.dialogId;
	const SELECT_BILL_DIV_CLASS = PREFIX+" .load-customer-bill-div";
	
	const CUSTOMER_BILL_ID = self.containerId;//对话框内容器ID
	
	//设置选择账单类型，1：按通知单选择；2：按客户账单选择
	const SELECT_CUSTOMER_BILL = 2;//按客户账单选择
	const SELECT_CUSTOMER_NOTIFY = 1;//按客户通知单选择
	
	console.log("load page type is 2：按客户账单选择");
	//加载选择开票账单页面
	var url = BASE_CONTEXT_PATH + "/select-invoice-bill/load-page";
	var parms = new Object();
	parms.type = SELECT_CUSTOMER_BILL;
	parms.containerId = CUSTOMER_BILL_ID;
	$(SELECT_BILL_DIV_CLASS).load(url, parms, customerResizeFuncx);
	function customerResizeFuncx(){
		
		//console.log("call back........");
		//console.log("------------------------------"+self);
		var myEvent = new Event('resize');
        window.dispatchEvent(myEvent);
        
        self.initCustomerBillPage();
		
	}
}

/**
 * 初始化页面
 */
SelectCustomerBillDialog.prototype.initCustomerBillPage = function() {
	var self = this;
	var PREFIX = "#"+self.dialogId;
	const CUSTOMER_BILL_ID = self.containerId;//对话框内容器ID
	
	var selectCustomerBill = new SelectCustomerBill(CUSTOMER_BILL_ID, self.selectedCallback);
	selectCustomerBill.loadPage(self.customerId, self.customerName);
}

//------------------------------打开关闭对话框----------------------------------------------
/**
 * 	打开选择客户账单对话框
 */
SelectCustomerBillDialog.prototype.open = function(customerId, customerName) {
	var self = this;
	var dialogId = self.dialogId;
	const CONTAINER_ID = dialogId+"-bill";
	
	self.containerId = CONTAINER_ID;//对话框内容器ID
	self.customerId = customerId;//客户ID
	self.customerName = customerName;//客户名称
	
	self.setDialogHeadCustomerName();//设置对话框header部分中的客户名称
	self.loadPage();
	
	DialogUtil.openDialogFn(dialogId);//打开选择客户账单对话框
}
/**
 * 	关闭选择客户账单对话框
 */
SelectCustomerBillDialog.prototype.close = function() {
	var self = this;
	var dialogId = self.dialogId;
	DialogUtil.closeDialogFn(dialogId);//关闭选择客户账单对话框
}

//---------------------------------------------
