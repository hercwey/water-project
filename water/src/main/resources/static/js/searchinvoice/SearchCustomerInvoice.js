//--------------------公共查询函数--------------------------
		
function SearchCustomerInvoice(containerId, selectedCallback){
	this.containerId = containerId; //选择客户发票信息对话框ID，与UI中的ID相对应
	this.selectedCallback = selectedCallback;//用户选中后的回调函数
	this.selectedInvoiceObj = null;//用户选中的开票信息
	
}
//--------------------------查询---------------------------------------------
/**
 * 	公共查询函数
 */
SearchCustomerInvoice.prototype.searchCustomerInvoice = function(parms) {
	var self = this;
	var PREFIX = "#"+self.containerId;
	const INVOICE_ITEM_ID = PREFIX+" #customer-invoice-body";
	//加载字典列表页面
	var url = BASE_CONTEXT_PATH + "/search-customer-invoice/table"
	$(INVOICE_ITEM_ID).load(url, parms, customerResizeFuncx);
	
	function customerResizeFuncx(){
		
		//console.log("call back........");
		//console.log("------------------------------"+self);
		var myEvent = new Event('resize');
        window.dispatchEvent(myEvent);
        
        self.initListener();//初始化监听器
        self.initPager();//初始化pager
		
	}
}

/*
	默认查询
	Retrieve(default)查询(加载默认列表)
*/
SearchCustomerInvoice.prototype.defaultSearchCustomerInvoice = function() {
	var self = this;
	
	const PREFIX = "#"+self.containerId;
	const SEARCH_COND_ID = PREFIX+" #search-cond";
	const INVOICE_ITEM_ID = PREFIX+" #customer-invoice-body";
	
	$(SEARCH_COND_ID).val("");//设置查询条件为空
	
	var pageNum = 0;
	var pageSize = 0;
	
	//获取参数
	var parms = new Object();
	parms.searchCond = null;
	parms.billType = null;
	//parms = self.getSearchCustomerInvoiceParams();//获取普通查询参数（不包括分页信息）
	
	parms.pageNum=pageNum;
	parms.pageSize=pageSize;
	
	//this.searchCustomerInvoice(parms);
	
	//加载字典列表页面
	
	console.log("===========默认加载选择客户开票信息JS对象："+this);
	console.log("===========默认加载选择客户开票信息JS对象："+self);
	
	parms.containerId=self.containerId;
	
	var params = new Object();
	params.parmsJSON = JSON.stringify(parms);
	
	var url = BASE_CONTEXT_PATH + "/search-customer-invoice/table"
	$(INVOICE_ITEM_ID).load(url, params, customerResizeFuncx);
	function customerResizeFuncx(){
		
		console.log("call back........");
		console.log("------------------------------"+self);
		var myEvent = new Event('resize');
        window.dispatchEvent(myEvent);
        
        self.initListener();//初始化监听器
        self.initPager();//初始化pager
		
	}

}

/**
 *  初始化pager
 */
SearchCustomerInvoice.prototype.initPager = function() {
	console.log("init pager");
	var self = this;
	var containerId = self.containerId;
	var searchCond = self.getSearchCustomerInvoiceParams();//获取普通查询参数（不包括分页信息）
	
	var parms = new Object();
	parms.serachCond = searchCond;
	parms.containerId = containerId;
	
	var parmsJSON = JSON.stringify(parms);
	
	//创建分页组件实例
	var pager=new Pager(containerId+"-pager-table");
	pager.init(loadCustomerPage,searchCond);	
	
	/*
		分页查询(采用所保存的状态)
	 */
	function loadCustomerPage(searchCond, pageNum, pageSize) {
		//var self = this;
		//debugger;
		console.log("===========选择客户开票信息JS对象："+this);
		console.log("===========选择客户开票信息JS对象："+self);
		var containerId = self.containerId;
		var PREFIX = "#"+self.containerId;
		const INVOICE_ITEM_ID = PREFIX+" #customer-invoice-body";
		
		//var parms = this.getSearchCustomerInvoiceParams();//获取查询参数（不包括分页信息）
//		var parms = new Object();
		searchCond.pageNum=pageNum;
		searchCond.pageSize=pageSize;
//		parms.searchCond=searchCond;
		searchCond.containerId=containerId;
		console.log("----------search cond is : "+JSON.stringify(searchCond));
		
		var params = new Object();
		params.parmsJSON = JSON.stringify(searchCond);

//		self.searchCustomerInvoice(params);
		var url = BASE_CONTEXT_PATH + "/search-customer-invoice/table"
		$(INVOICE_ITEM_ID).load(url, params, customerResizeFuncx);
		function customerResizeFuncx(){
			
			var myEvent = new Event('resize');
	        window.dispatchEvent(myEvent);
	        
	        self.initListener();//初始化监听器
	        self.initPager();//初始化pager			
			
		}
	}
	
}

/*
	查询按钮点击时调用
	采用用户所输入的查询条件进行查询.
*/
SearchCustomerInvoice.prototype.searchCustomerInvoiceList = function() {
	var self = this;
	var containerId = self.containerId;
	var pageNum = 0;
	var pageSize = 0;
	
	var parms = self.getSearchCustomerInvoiceParams();//获取查询参数（不包括分页信息）
	parms.pageNum=pageNum;
	parms.pageSize=pageSize;
	parms.containerId=self.containerId;
	
	var params = new Object();
	params.parmsJSON = JSON.stringify(parms);
	
	//console.log(JSON.stringify(parms));
	self.searchCustomerInvoice(params);
}
/**
 * 	获取查询客户开票信息参数
 */
 SearchCustomerInvoice.prototype.getSearchCustomerInvoiceParams = function(){
	var self = this;
	console.log("-----1------"+self.containerId);
	const PREFIX = "#"+self.containerId;
	const SEARCH_COND_ID = PREFIX+" #search-cond";
	const SEARCH_BILL_TYPE_ID = PREFIX+" #bill-type";
	//初始化请求参数
	var parms = new Object();
	var searchCond = $(SEARCH_COND_ID).val();
	var billType = $(SEARCH_BILL_TYPE_ID).val();
	parms.searchCond=searchCond;
	parms.billType=billType;
	return parms;
}

//---------事件监听--------------
SearchCustomerInvoice.prototype.initListener = function(){
	var self = this;
	const PREFIX = "#"+self.containerId;
	const SEARCH_ID = PREFIX+" #btn-search";
	const SEARCH_REST_ID = PREFIX+" #btn-search-reset";
	const SEARCH_COND_ID = PREFIX+" #search-cond";
	const SEARCH_BILL_TYPE_ID = PREFIX+" #bill-type";
	const INVOICE_ITEM_TR = PREFIX+" #customer-invoice-table tbody tr";
	
	/*
		开票类型下拉框change
	*/
	$(SEARCH_BILL_TYPE_ID).unbind("change");  //移除change事件
	$(SEARCH_BILL_TYPE_ID).on('change', billTypeChangeCallbackClick);
	function billTypeChangeCallbackClick(){
		console.log("change");
		self.searchCustomerInvoiceList();
	}
	
	/*
		查询按钮CLICK
	*/
	$(SEARCH_ID).unbind("click");  //移除click事件
	$(SEARCH_ID).on('click', searchInvoiceCallbackClick);
	function searchInvoiceCallbackClick(){
		console.log("search");
		self.searchCustomerInvoiceList();
	}
	
	/*
		查询条件复位按钮CLICK
	*/
	$(SEARCH_REST_ID).unbind("click");  //移除click事件
	$(SEARCH_REST_ID).on('click', searchResetCallbackClick);
	function searchResetCallbackClick(){
		$(SEARCH_COND_ID).val("");
		$(SEARCH_COND_ID).focus();  //获得焦点
	}
	
	/*
		查询条件输入框 KEYUP
	*/
	$(SEARCH_COND_ID).unbind("keyup");  //移除click事件
	$(SEARCH_COND_ID).on('keyup', searchInputCallbackKeyup);
	function searchInputCallbackKeyup(){
		if(event.keyCode == 13){
			$(SEARCH_ID).trigger("click");	                
        }
	}
	
	//--------------------客户开票信息列表事件绑定部分--------------------
	/**
	 * 	绑定客户开票信息列表行的click事件
	 */
	$(INVOICE_ITEM_TR).unbind("click");  //移除click事件
	$(INVOICE_ITEM_TR).on("click", function(){
		
		//判断是否包含selected-color类
//		var isSelected = $(this).hasClass("selected-color");
//		if(isSelected){
//			$(INVOICE_ITEM_TR).removeClass("selected-color");//取消列表所有行选中颜色
//		}else{
//			$(INVOICE_ITEM_TR).removeClass("selected-color");//取消列表所有行选中颜色
//			$(this).addClass("selected-color");//选中行设置行颜色
//		}
		
		var isSelected = $(this).hasClass("selected-color");
		if(!isSelected){
			$(INVOICE_ITEM_TR).removeClass("selected-color");//取消列表所有行选中颜色
			$(this).addClass("selected-color");//选中行设置行颜色
			
			var invoiceId = $(this).attr("data-invoice-id");
			console.log("click get invoice id is "+invoiceId);
			
			//var invoiceId = $(this).attr("data-invoice-id");//开票信息ID
			var customerId = $(this).attr("data-customer-id");//客户信息ID
			var customerName = $(this).attr("data-customer-name");//客户名称
			var invoiceName = $(this).attr("data-invoice-name");//开票名称
			var invoiceType = $(this).attr("data-bill-type");//开票类型；1：专用发票，2：普通发票，3：手工发票
			var taxNo = $(this).attr("data-tax-no");//税号
			var accountBank = $(this).attr("data-account-bank");//开户行
			var accountNo = $(this).attr("data-account-no");//账号
			var regAddress = $(this).attr("data-reg-address");//地址
			var telephone = $(this).attr("data-telephone");//电话
			
			if(customerName!=null && customerName!=""){
				if(invoiceName!=customerName){
					customerName = invoiceName+"("+customerName+")";
				}
			}
			
			var selectedInvoiceObj = new Object();
			selectedInvoiceObj.invoiceId = invoiceId;
			selectedInvoiceObj.customerId = customerId;
			selectedInvoiceObj.invoiceName = invoiceName;
			selectedInvoiceObj.customerName = customerName;
			selectedInvoiceObj.invoiceType = invoiceType;
			selectedInvoiceObj.taxNo = taxNo;
			selectedInvoiceObj.accountBank = accountBank;
			selectedInvoiceObj.accountNo = accountNo;
			selectedInvoiceObj.regAddress = regAddress;
			selectedInvoiceObj.telephone = telephone;
			
			console.log("------"+JSON.stringify(selectedInvoiceObj));
			
			self.selectedInvoiceObj = selectedInvoiceObj;
		}
		
	});
	
}

//---------------------------------------------
