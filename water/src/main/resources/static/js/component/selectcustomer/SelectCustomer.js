//--------------------公共查询函数--------------------------
		
function SelectCustomer(dialogId, selectedCallback){
	this.dialogId = dialogId; //选择客户发票信息对话框ID，与UI中的ID相对应
	this.selectedCallback = selectedCallback;//用户选中后的回调函数
	this.selectedCustomerObj = null;//用户选中的客户信息
	
}
//------------------------------打开关闭对话框----------------------------------------------
/**
 * 	打开选择客户信息对话框
 */
SelectCustomer.prototype.openDialog = function() {
	var self = this;
	var dialogId = self.dialogId;
	DialogUtil.openDialogFn(dialogId);//打开选择客户信息对话框
}
/**
 * 	关闭选择客户信息对话框
 */
SelectCustomer.prototype.closeDialog = function() {
	var self = this;
	var dialogId = self.dialogId;
	DialogUtil.closeDialogFn(dialogId);//关闭选择客户信息对话框
}

//--------------------------查询---------------------------------------------
/**
 * 	公共查询函数
 */
SelectCustomer.prototype.searchCustomer = function(parms) {
	
	console.log("----------search cond is : "+JSON.stringify(parms));
	
	var self = this;
	var PREFIX = "#"+self.dialogId;
	const CUSTOMER_ITEM_ID = PREFIX+" #customer-table-body";
	//加载字典列表页面
	var url = BASE_CONTEXT_PATH + "/select-customer/table"
	$(CUSTOMER_ITEM_ID).load(url, parms, customerResizeFuncx);
	
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
SelectCustomer.prototype.open = function() {
	var self = this;
	self.openDialog();//打开选择客户信息对话框
	
	const PREFIX = "#"+self.dialogId;
	const SEARCH_COND_ID = PREFIX+" #search-cond";
	const CUSTOMER_ITEM_ID = PREFIX+" #customer-table-body";
	
	$(SEARCH_COND_ID).val("");//设置查询条件为空
	
	var pageNum = 0;
	var pageSize = 0;
	
	//获取参数
	var parms = new Object();
	parms.searchCond = null;
	//parms = self.getSearchCustomerParams();//获取普通查询参数（不包括分页信息）
	
	parms.pageNum=pageNum;
	parms.pageSize=pageSize;
	
	//this.searchCustomer(parms);
	
	//加载字典列表页面
	
	console.log("===========默认加载选择客户JS对象："+this);
	console.log("===========默认加载选择客户JS对象："+self);
	
	parms.dialogId=self.dialogId;
	
	var params = new Object();
	params.parmsJSON = JSON.stringify(parms);
	
	var url = BASE_CONTEXT_PATH + "/select-customer/table"
	$(CUSTOMER_ITEM_ID).load(url, params, customerResizeFuncx);
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
SelectCustomer.prototype.initPager = function() {
	console.log("init pager");
	var self = this;
	var dialogId = self.dialogId;
	var parms = self.getSearchCustomerParams();//获取普通查询参数（不包括分页信息）
	parms.dialogId = dialogId;
	
	//创建分页组件实例
	var pager=new Pager(dialogId+"-pager-customer");
	pager.init(loadCustomerPage,parms.searchCond);	
	
	/*
		分页查询(采用所保存的状态)
	 */
	function loadCustomerPage(searchCond, pageNum, pageSize) {
		//var self = this;
		//debugger;
		console.log("===========选择客户JS对象："+this);
		console.log("===========选择客户JS对象："+self);
		var dialogId = self.dialogId;
		var PREFIX = "#"+self.dialogId;
		const CUSTOMER_ITEM_ID = PREFIX+" #customer-table-body";
		
		//var parms = this.getSearchCustomerParams();//获取查询参数（不包括分页信息）
		var parms = new Object();
		parms.pageNum=pageNum;
		parms.pageSize=pageSize;
		parms.searchCond=searchCond;
		searchCond.dialogId=dialogId;
		console.log("----------search cond is : "+JSON.stringify(parms));
		
//		self.searchCustomer(params);
		var url = BASE_CONTEXT_PATH + "/select-customer/table"
		$(CUSTOMER_ITEM_ID).load(url, parms, customerResizeFuncx);
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
SelectCustomer.prototype.searchCustomerList = function() {
	var self = this;
	var dialogId = self.dialogId;
	var pageNum = 0;
	var pageSize = 0;
	
	var parms = self.getSearchCustomerParams();//获取查询参数（不包括分页信息）
	parms.pageNum=pageNum;
	parms.pageSize=pageSize;
	parms.dialogId=self.dialogId;
	
	var params = new Object();
	params.parmsJSON = JSON.stringify(parms);
	
	//console.log(JSON.stringify(parms));
	this.searchCustomer(params);
}
/**
 * 	获取查询客户客户信息参数
 */
 SelectCustomer.prototype.getSearchCustomerParams = function(){
	var self = this;
	console.log("-----1------"+self.dialogId);
	const PREFIX = "#"+self.dialogId;
	const SEARCH_COND_ID = PREFIX+" #search-cond";
	//初始化请求参数
	var parms = new Object();
	var searchCond = $(SEARCH_COND_ID).val();
	parms.searchCond=searchCond;
	return parms;
}

//---------事件监听--------------
SelectCustomer.prototype.initListener = function(){
	var self = this;
	const PREFIX = "#"+self.dialogId;
	const SEARCH_ID = PREFIX+" #btn-search";
	const SEARCH_REST_ID = PREFIX+" #btn-search-reset";
	const SEARCH_COND_ID = PREFIX+" #search-cond";
	const CUSTOMER_ITEM_TR = PREFIX+" #customer-table tbody tr";
	
	const SAVE_ID = PREFIX+" #btn-bank-modify-and-back";
	
	/*
		查询按钮CLICK
	*/
	$(SEARCH_ID).unbind("click");  //移除click事件
	$(SEARCH_ID).on('click', searchCustomerCallbackClick);
	function searchCustomerCallbackClick(){
		console.log("search");
		self.searchCustomerList();
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
	
	/**
	 * 	绑定保存按钮的click事件
	 */
	$(SAVE_ID).unbind("click");  //移除click事件
	$(SAVE_ID).on('click', saveCallbackClick);
	function saveCallbackClick(){
		self.closeDialog();//关闭选择客户信息对话框
		self.selectedCallback(self.selectedCustomerObj);
		
	}
	
	//--------------------客户客户信息列表事件绑定部分--------------------
	/**
	 * 	绑定客户客户信息列表行的click事件
	 */
	$(CUSTOMER_ITEM_TR).unbind("click");  //移除click事件
	$(CUSTOMER_ITEM_TR).on("click", function(){
		
		//判断是否包含selected-color类
//		var isSelected = $(this).hasClass("selected-color");
//		if(isSelected){
//			$(CUSTOMER_ITEM_TR).removeClass("selected-color");//取消列表所有行选中颜色
//		}else{
//			$(CUSTOMER_ITEM_TR).removeClass("selected-color");//取消列表所有行选中颜色
//			$(this).addClass("selected-color");//选中行设置行颜色
//		}
		
		var isSelected = $(this).hasClass("selected-color");
		if(!isSelected){
			$(CUSTOMER_ITEM_TR).removeClass("selected-color");//取消列表所有行选中颜色
			$(this).addClass("selected-color");//选中行设置行颜色
			
			var customerId = $(this).attr("data-id");
			console.log("click get customer id is "+customerId);
			
			//var customerId = $(this).attr("data-id");//客户ID
			var customerName = $(this).attr("data-customer-name");//客户名称
			var customerMobile = $(this).attr("data-customer-mobile");//手机号
			var idNo = $(this).attr("data-id-no");//身份证号
			var address = $(this).attr("data-address");//地址
			var waterStatus = $(this).attr("data-water-status");//用水状态
			var status = $(this).attr("data-status");//立户状态
			
			var selectedCustomerObj = new Object();
			selectedCustomerObj.customerId = customerId;
			selectedCustomerObj.customerName = customerName;
			selectedCustomerObj.customerMobile = customerMobile;
			selectedCustomerObj.idNo = idNo;
			selectedCustomerObj.address = address;
			selectedCustomerObj.waterStatus = waterStatus;
			selectedCustomerObj.status = status;
			
			self.selectedCustomerObj = selectedCustomerObj;
		}
		
	});
	
	/**
	 * 	绑定客户信息列表行的双击事件事件
	 */
	$(CUSTOMER_ITEM_TR).unbind("dblclick");  //移除click事件
	$(CUSTOMER_ITEM_TR).on("dblclick", function(){
		//禁止双击选中文本
		window.getSelection ? window.getSelection().removeAllRanges() : document.selection.empty();
		
		var customerId = $(this).attr("data-id");//客户ID
		console.log("dblclick get customer id is "+customerId);
		
		//var customerId = $(this).attr("data-id");//客户ID
		var customerName = $(this).attr("data-customer-name");//客户名称
		var customerMobile = $(this).attr("data-customer-mobile");//手机号
		var idNo = $(this).attr("data-id-no");//身份证号
		var address = $(this).attr("data-address");//地址
		var waterStatus = $(this).attr("data-water-status");//用水状态
		var status = $(this).attr("data-status");//立户状态
		
		var selectedCustomerObj = new Object();
		selectedCustomerObj.customerId = customerId;
		selectedCustomerObj.customerName = customerName;
		selectedCustomerObj.customerMobile = customerMobile;
		selectedCustomerObj.idNo = idNo;
		selectedCustomerObj.address = address;
		selectedCustomerObj.waterStatus = waterStatus;
		selectedCustomerObj.status = status;
		
		self.selectedCustomerObj = selectedCustomerObj;
		console.log("----------"+JSON.stringify(self.selectedCustomerObj));
		$(SAVE_ID).click();
	});
}

//---------------------------------------------
