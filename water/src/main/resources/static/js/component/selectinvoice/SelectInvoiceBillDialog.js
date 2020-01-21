//--------------------公共查询函数--------------------------
		
function SelectInvoiceBillDialog(dialogId, selectedCallback){
	this.dialogId = dialogId; //选择客户发票信息对话框ID，与UI中的ID相对应
	this.containerId = null;//对话框内容器ID
	this.customerId = null;//客户ID
	this.customerName = null;//客户名称
	
	this.selectedCallback = selectedCallback;//用户选中后的回调函数
	this.selectedBillArr = new Array();//用户选择的客户账单列表
	
	this.type = 1;//设置选择账单类型，1：按通知单选择；2：按客户账单选择
	
}

/**
 * 清除开票账单类型按钮的当前选中class
 */
SelectInvoiceBillDialog.prototype.clearInvoiceBillBtnClass = function(){
	var self = this;
	const PREFIX = "#"+self.dialogId;
	console.log("clear invoice bill active class");
	const INVOICE_BILL_TYPE_BTN = PREFIX+" .invoice-bill-type button";
	$(INVOICE_BILL_TYPE_BTN).removeClass("active");
}

/**
 * 设置选择账单类型，1：按通知单选择；2：按客户账单选择（未用）
 */
SelectInvoiceBillDialog.prototype.setType = function(type){
	var self = this;
	self.type = type;
}

/**
 * 移除初始化的poput tooltip
 */
SelectInvoiceBillDialog.prototype.removeTooltip = function(){
	
	var self = this;
	const PREFIX = "#"+self.dialogId;
	const NOTIFY_TOOLTIP_ID = "#customer-notify-tooltip_wrapper";
	
	$(NOTIFY_TOOLTIP_ID).remove();//移除初始化的poput tooltip
}

/**
 * 设置对话框header部分中的客户名称
 */
SelectInvoiceBillDialog.prototype.setDialogHeadCustomerName = function(){
	var self = this;
	const PREFIX = "#"+self.dialogId;
	const DIALOG_HEADER_CUSTOMER_NAME_ID = PREFIX+" #dialog-header-customer-name";
	const customerName = self.customerName;
	//if(customerName!=null && customerName!=""){
		$(DIALOG_HEADER_CUSTOMER_NAME_ID).text(customerName);
	//}
	
}

//------------------------------打开关闭对话框----------------------------------------------
/**
 * 	打开选择账单对话框
 */
SelectInvoiceBillDialog.prototype.open = function(customerId, customerName) {
	var self = this;
	var dialogId = self.dialogId;
	console.log("open dialog");
	const CONTAINER_ID = dialogId+"-bill";
	
	self.containerId = CONTAINER_ID;//对话框内容器ID
	self.customerId = customerId;//客户ID
	self.customerName = customerName;//客户名称
	
	self.initListener();//初始化监听器
	self.setDialogHeadCustomerName();//设置对话框header部分中的客户名称
	self.loadPage();//加载选择账单页面
	
	DialogUtil.openDialogFn(dialogId);//打开选择客户账单对话框
	
}
/**
 * 	关闭选择账单对话框
 */
SelectInvoiceBillDialog.prototype.close = function() {
	var self = this;
	var dialogId = self.dialogId;
	console.log("close dialog");
	DialogUtil.closeDialogFn(dialogId);//关闭选择客户账单对话框
}

/**
 * 	加载选择账单页面
 */
SelectInvoiceBillDialog.prototype.loadPage = function() {
	var self = this;
	var PREFIX = "#"+self.dialogId;
	const SELECT_BILL_DIV_CLASS = PREFIX+" .select-invoice-bill-div";
	
	const INVOICE_BILL_ID = self.containerId;//对话框内容器ID
	
	console.log("load page type is :"+self.type+"（设置选择账单类型，1：按通知单选择；2：按客户账单选择）");
	
	//加载选择开票账单页面
	var url = BASE_CONTEXT_PATH + "/select-invoice-bill/load-page";
	var parms = new Object();
	parms.type = self.type;
	parms.containerId = INVOICE_BILL_ID;
	$(SELECT_BILL_DIV_CLASS).load(url, parms, customerResizeFuncx);
	function customerResizeFuncx(){
		
		//console.log("call back........");
		//console.log("------------------------------"+self);
		var myEvent = new Event('resize');
        window.dispatchEvent(myEvent);
        
        //设置选择账单类型，1：按通知单选择；2：按客户账单选择
    	const SELECT_CUSTOMER_BILL = 2;//按客户账单选择
    	const SELECT_CUSTOMER_NOTIFY = 1;//按客户通知单选择
        //self.initListener();//初始化监听器
    	
    	if(self.type==SELECT_CUSTOMER_NOTIFY){
    		self.initCustomerNotifyPage();//初始化加载客户通知单页面
    	}else{
    		self.initCustomerBillPage();//初始化加载客户账单页面
    	}
		
	}
}

/**
 * 初始化客户通知单组件
 */
SelectInvoiceBillDialog.prototype.initCustomerNotifyPage = function() {
	var self = this;
	var PREFIX = "#"+self.dialogId;
	const CUSTOMER_NOTIFY_ID = self.containerId;//对话框内容器ID
	console.log("init customer notify page...");
	var selectCustomerNotify = new SelectCustomerNotify(CUSTOMER_NOTIFY_ID, self.selectedCallback);
	selectCustomerNotify.loadPage(self.customerId, self.customerName);
}

/**
 * 初始化客户账单组件
 */
SelectInvoiceBillDialog.prototype.initCustomerBillPage = function() {
	var self = this;
	var PREFIX = "#"+self.dialogId;
	const CUSTOMER_BILL_ID = self.containerId;//对话框内容器ID
	
	var selectCustomerBill = new SelectCustomerBill(CUSTOMER_BILL_ID, self.selectedCallback);
	selectCustomerBill.loadPage(self.customerId, self.customerName);
}

//---------事件监听--------------
SelectInvoiceBillDialog.prototype.initListener = function(){
	var self = this;
	const PREFIX = "#"+self.dialogId;
	const BTN_SELECT_CUSTOMER_BILL_CLASS = PREFIX+" .btn-select-customer-bill";//按客户账单选择按钮选择器
	const BTN_SELECT_CUSTOMER_NOTIFY_CLASS = PREFIX+" .btn-select-customer-notify";//按客户通知单选择按钮选择器
	
	////设置选择账单类型，1：按通知单选择；2：按客户账单选择
	const SELECT_CUSTOMER_BILL = 2;//按客户账单选择
	const SELECT_CUSTOMER_NOTIFY = 1;//按客户通知单选择
	
	/**
	 * 解绑按客户账单选择按钮的click事件，再重新绑定
	 */
	$(BTN_SELECT_CUSTOMER_BILL_CLASS).unbind("click");  //移除click事件
	$(BTN_SELECT_CUSTOMER_BILL_CLASS).on('click', selectedBillItemTabCallbackClick);
	function selectedBillItemTabCallbackClick(){
		console.log("click select customer bill...");
		
		self.clearInvoiceBillBtnClass();//清除开票账单类型按钮的选中class
		$(this).addClass("active");
		
		self.type = SELECT_CUSTOMER_BILL;//设置选择账单类型为2：按客户账单选择
		self.loadPage();//加载选择账单页面
	}
	
	/**
	 * 解绑按客户通知单选择按钮的click事件，再重新绑定
	 */
	$(BTN_SELECT_CUSTOMER_NOTIFY_CLASS).unbind("click");  //移除click事件
	$(BTN_SELECT_CUSTOMER_NOTIFY_CLASS).on('click', searchInvoiceCallbackClick);
	function searchInvoiceCallbackClick(){
		console.log("click select customer notify...");
		
		self.removeTooltip();//移除初始化的poput tooltip
		
		self.clearInvoiceBillBtnClass();//清除开票账单类型按钮的选中class
		$(this).addClass("active");
		
		self.type = SELECT_CUSTOMER_NOTIFY;//设置选择账单类型为1：按通知单选择
		self.loadPage();//加载选择账单页面
	}
	
}

//---------------------------------------------
