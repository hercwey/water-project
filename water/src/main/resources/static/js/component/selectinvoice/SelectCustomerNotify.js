//--------------------公共查询函数--------------------------
		
function SelectCustomerNotify(containerId, selectedCallback){
	this.containerId = containerId; //选择客户发票信息对话框ID，与UI中的ID相对应
	this.customerId = null;//客户ID
	this.customerName = null;//客户名称
	this.selectedCallback = selectedCallback;//用户选中后的回调函数
	this.selectedNotifyArr = new Array();//用户选择的客户账单列表
	
}

/**
 * 初始化期间
 */
SelectCustomerNotify.prototype.initPeriod = function(){
	
	var self = this;
	const PREFIX = "#"+self.containerId;
	const PERIOD_ID = PREFIX+" #period";//期间
	
	/**
	 * 	获取当前系统月份（表示为抄表记录中的期间）
	 */
	function getCurrSysMonth(){
		var date = new Date();
		var year = date.getFullYear();    //获取完整的年份(4位,1970-????)
		var month = date.getMonth()+1;       //获取当前月份(0-11,0代表1月)
		if(month<10){
			month = "0"+month;
		}
		return year+"-"+month;
	}
	$(PERIOD_ID).val(getCurrSysMonth());
}

/**
 * 初始化poput tooltip
 */
SelectCustomerNotify.prototype.initTooltip = function(trId){
	
	var self = this;
	const PREFIX = "#"+self.containerId;
	const NOTIFY_TOOLTIP_ID = "#customer-notify-tooltip";
	
	$(NOTIFY_TOOLTIP_ID).popup({
		closebutton: true,
//		pagecontainer: "#customer-bill-body",
//		type: 'tooltip',
//		vertical: 'bottom',
//		horizontal: 'center',
//		tooltipanchor: $('#btn-disp-tootip'),
//		tooltipanchor: $(trId),
//		setzindex: true,
//		offsetleft: 0,
          
	});
}

/**
 * 初始化设置已选择的标志
 */
SelectCustomerNotify.prototype.initSetSelectedFlag = function(){
	var self = this;
	const PREFIX = "#"+self.containerId;
	const INVOICE_ITEM_FLAG_TD = PREFIX+" #customer-bill-table tbody tr td.selected-flag";
	const INVOICE_ITEM_FLAG_TD_ID = PREFIX+" #customer-bill-table tbody tr td.selected-flag-";
	
	$(INVOICE_ITEM_FLAG_TD).each(function(){
		var billId = $(this).attr("data-bill-id");
		
		$.each(self.selectedNotifyArr, function(){
			var currBillId = this.billId;
			if(billId==currBillId){
				var flag = '<i class="fa fa-check-square" aria-hidden="true"></i>';
				$(INVOICE_ITEM_FLAG_TD_ID+billId).html(flag);
			}
		});
	});
	
}

//--------------------------设置选择/取消已选择标志---------------------------------------------
/**
 * 	设置已选择标记
 */
SelectCustomerNotify.prototype.setSelectedFlag = function(billId) {
	var self = this;
	const PREFIX = "#"+self.containerId;
	const INVOICE_ITEM_FLAG_TD_ID = PREFIX+" #customer-bill-table tbody tr td.selected-flag-"+billId;
	
	var html = $(INVOICE_ITEM_FLAG_TD_ID).html();
	if(html==null || html==""){//增加
		var flag = '<i class="fa fa-check-square" aria-hidden="true"></i>';
		$(INVOICE_ITEM_FLAG_TD_ID).html(flag);
	}else{//删除
		$(INVOICE_ITEM_FLAG_TD_ID).empty();
	}
	
//	$(INVOICE_ITEM_TR).each(function(index){
//		
//		var html = $(this).html();
//		if(html==null || html==""){//增加
//			var flag = '<i class="fa fa-check-square" aria-hidden="true"></i>';
//			$(this).html(flag);
//		}else{//删除
//			$(this).empty();
//		}
//		
//		//TODO 获取账单信息，并添加到已选择div中
//	});
	
}
//--------------------------查询---------------------------------------------
/**
 * 	公共查询函数
 */
SelectCustomerNotify.prototype.searchCustomerBill = function(parms) {
	var self = this;
	var PREFIX = "#"+self.containerId;
	const INVOICE_ITEM_ID = PREFIX+" #customer-bill-body";
	//加载字典列表页面
	var url = BASE_CONTEXT_PATH + "/select-customer-notify/table"
	$(INVOICE_ITEM_ID).load(url, parms, customerResizeFuncx);
	
	function customerResizeFuncx(){
		
		//console.log("call back........");
		//console.log("------------------------------"+self);
		var myEvent = new Event('resize');
        window.dispatchEvent(myEvent);
        
        self.initListener();//初始化监听器
        //self.initPager();//初始化pager
        self.initSetSelectedFlag();//初始化设置已选择的标志
        $("[data-toggle='tooltip']").tooltip();
	}
}

/**
 * 加载客户通知单
 */
SelectCustomerNotify.prototype.loadPage = function(customerId, customerName) {
	
	console.log("===============================客户ID："+customerId+"，客户名称："+customerName);
	
	var self = this;
	self.customerId = customerId;//客户ID
	self.customerName = customerName;//客户名称
	
	const PREFIX = "#"+self.containerId;
	const PERIOD_ID = PREFIX+" #period";//开始期间
	const SEARCH_COND_ID = PREFIX+" #search-cond";
	const INVOICE_ITEM_ID = PREFIX+" #customer-bill-body";
	const SELECTED_BILL_TBODY_ID = PREFIX+" #selected-customer-bill-tbody";//已选择的客户账单列表tbody的ID
	
	const IS_MARGE_BILL_ID = PREFIX+" #search-customer-bill-div .is-merge-bill";
	const IS_MARGE_NOTIFY_ID = PREFIX+" #search-customer-bill-div .is-merge-notify";
	const INVOICE_WATER_FEE_TYPE = PREFIX+" #search-customer-bill-div #invoice-fee-type input:radio";
	//设置已选择的账单列表选项卡中复选框和单选框的默认值
	$(IS_MARGE_BILL_ID).prop("checked", false);//设置合并账单复选框未勾选
	$(INVOICE_WATER_FEE_TYPE).eq(0).prop("checked", true);//设置开票水费类型为基础水费
	
	$(PREFIX+" #selected-customer-bill-nav a[href='#customer-bill-item']").tab("show");//跳转到账单列表选项卡
	
	self.initPeriod();//初始化期间
	 
	$(SEARCH_COND_ID).val("");//设置查询条件为空
	self.selectedNotifyArr = new Array();//重新new个数组
	$(INVOICE_ITEM_ID).empty();//清空客户账单列表
	$(SELECTED_BILL_TBODY_ID).empty();//清空已选择的客户账单列表
	
	var pageNum = 0;
	var pageSize = 0;
	
	var period = $(PERIOD_ID).val();//获取期间
	//获取参数
	var parms = new Object();
	parms.customerId = customerId;
	parms.period = period;
	//parms.periodEnd = periodEnd;
	parms.searchCond = null;
	//parms = self.getSearchCustomerBillParams();//获取普通查询参数（不包括分页信息）
	
	parms.pageNum=pageNum;
	parms.pageSize=pageSize;
	
	//this.searchCustomerBill(parms);
	
	//加载字典列表页面
	
	console.log("===========默认加载选择客户账单信息JS对象："+this);
	console.log("===========默认加载选择客户账单信息JS对象："+self);
	
	parms.containerId=self.containerId;
	parms.customerId=self.customerId;
	
	var params = new Object();
	params.parmsJSON = JSON.stringify(parms);
	console.log("----------默认查询参数："+JSON.stringify(parms));
	var url = BASE_CONTEXT_PATH + "/select-customer-notify/table"
	$(INVOICE_ITEM_ID).load(url, params, customerResizeFuncx);
	function customerResizeFuncx(){
		
		console.log("call back........");
		console.log("------------------------------"+self);
		var myEvent = new Event('resize');
        window.dispatchEvent(myEvent);
        
        self.initListener();//初始化监听器
        //self.initPager();//初始化pager
        //self.initTooltip();//初始化poput tooltip
		
	}

}

/**
 *  初始化pager
 */
SelectCustomerNotify.prototype.initPager = function() {
	console.log("init pager");
	var self = this;
	var containerId = self.containerId;
	var searchCond = self.getSearchCustomerBillParams();//获取普通查询参数（不包括分页信息）
	
	var parms = new Object();
	parms.serachCond = searchCond;
	parms.containerId = containerId;
	
	var parmsJSON = JSON.stringify(parms);
	
	//创建分页组件实例
	var pager=new Pager(containerId+"-pager-customer-nofity");
	pager.init(loadCustomerPage,searchCond);	
	
	/*
		分页查询(采用所保存的状态)
	 */
	function loadCustomerPage(searchCond, pageNum, pageSize) {
		//var self = this;
		//debugger;
		console.log("===========选择客户账单信息JS对象："+this);
		console.log("===========选择客户账单信息JS对象："+self);
		var containerId = self.containerId;
		var PREFIX = "#"+self.containerId;
		const INVOICE_ITEM_ID = PREFIX+" #customer-bill-body";
		
		//var parms = this.getSearchCustomerBillParams();//获取查询参数（不包括分页信息）
//		var parms = new Object();
		searchCond.pageNum=pageNum;
		searchCond.pageSize=pageSize;
//		parms.searchCond=searchCond;
		searchCond.containerId=containerId;
		console.log("----------search cond is : "+JSON.stringify(searchCond));
		
		var params = new Object();
		params.parmsJSON = JSON.stringify(searchCond);

//		self.searchCustomerBill(params);
		var url = BASE_CONTEXT_PATH + "/select-customer-notify/table"
		$(INVOICE_ITEM_ID).load(url, params, customerResizeFuncx);
		function customerResizeFuncx(){
			
			var myEvent = new Event('resize');
	        window.dispatchEvent(myEvent);
	        
	        self.initListener();//初始化监听器
	        //self.initPager();//初始化pager		
	        self.initSetSelectedFlag();//初始化设置已选择的标志
	        $("[data-toggle='tooltip']").tooltip();
			
		}
	}
	
}

/*
	查询按钮点击时调用
	采用用户所输入的查询条件进行查询.
*/
SelectCustomerNotify.prototype.searchCustomerBillList = function() {
	var self = this;
	var containerId = self.containerId;
	var pageNum = 0;
	var pageSize = 0;
	
	var parms = self.getSearchCustomerBillParams();//获取查询参数（不包括分页信息）
	parms.pageNum=pageNum;
	parms.pageSize=pageSize;
	parms.containerId=self.containerId;
	
	var params = new Object();
	params.parmsJSON = JSON.stringify(parms);
	
	//console.log(JSON.stringify(parms));
	this.searchCustomerBill(params);
}
/**
 * 	获取查询客户账单参数
 */
SelectCustomerNotify.prototype.getSearchCustomerBillParams = function(){
	var self = this;
	const PREFIX = "#"+self.containerId;
	const PERIOD_ID = PREFIX+" #period";//期间
	const SEARCH_COND_ID = PREFIX+" #search-cond";
	
	var customerId = self.customerId;//客户ID
	var period = $(PERIOD_ID).val();//获取期间
	var searchCond = $(SEARCH_COND_ID).val();//获取用户录入的查询条件
	
	//初始化请求参数
	var parms = new Object();
	parms.customerId = customerId;
	parms.period = period;
	parms.searchCond=searchCond;
	return parms;
}

/**
 * 加载通知单详情（加完成功后显示）
 */
SelectCustomerNotify.prototype.loadNotifyDetail = function(notifyId){
	var self = this;
	const PREFIX = "#"+self.containerId;
	
	const NOTIFY_TOOLTIP_ID = "#customer-notify-tooltip";
	const NOTIFY_CONTAINER_CLASS = "#customer-notify-tooltip .water-fee-notify-container";

	var parms = new Object();
	parms.notifyId = notifyId;
	var url = BASE_CONTEXT_PATH + "/select-customer-notify/detail"
	$(NOTIFY_CONTAINER_CLASS).load(url, parms, loadNotifyDetailCallback);
	function loadNotifyDetailCallback(res){
		console.log("load notify detail callback...");
		var myEvent = new Event('resize');
        window.dispatchEvent(myEvent);
        
        self.initTooltip();//初始化poput tooltip
        $(NOTIFY_TOOLTIP_ID).popup('show');
        
	}
	
}

//---------事件监听--------------
SelectCustomerNotify.prototype.initListener = function(){
	var self = this;
	const PREFIX = "#"+self.containerId;
	const SELECTED_CUSTOMER_BILL_ITEM_LI_ID = PREFIX+" #selected-customer-bill-item-li";
	
	const SEARCH_COND_ID = PREFIX+" #search-cond";
	const SEARCH_ID = PREFIX+" #btn-search";
	const SEARCH_REST_ID = PREFIX+" #btn-search-reset";
	
	const INVOICE_ITEM_TR = PREFIX+" #customer-bill-table tbody tr";
	const SHOW_NOTIFY_BTN_CLASS = PREFIX+" #customer-bill-table tbody tr .btn-show-customer-notify";
	
	
	const NOTIFY_TOOLTIP_ID = "#customer-notify-tooltip";
	
	const SAVE_ID = PREFIX+" #btn-bank-modify-and-back";
	
	const IS_MARGE_BILL_ID = PREFIX+" #search-customer-bill-div .is-merge-bill";
	const IS_MARGE_NOTIFY_ID = PREFIX+" #search-customer-bill-div .is-merge-notify";
	const INVOICE_WATER_FEE_TYPE = PREFIX+" #search-customer-bill-div #invoice-fee-type input[name='waterFee']:checked";
	
	/**
	 * 解绑查询按钮的click事件，再重新绑定
	 */
	$(SEARCH_ID).unbind("click");  //移除click事件
	$(SEARCH_ID).on('click', searchInvoiceCallbackClick);
	function searchInvoiceCallbackClick(){
		console.log("search");
		self.searchCustomerBillList();
	}
	
	/**
	 * 解绑重置按钮的click事件，再重新绑定
	 */
	$(SEARCH_REST_ID).unbind("click");  //移除click事件
	$(SEARCH_REST_ID).on('click', searchResetCallbackClick);
	function searchResetCallbackClick(){
		$(SEARCH_COND_ID).val("");
		$(SEARCH_COND_ID).focus();  //获得焦点
	}
	
	/**
	 * 解绑查询条件输入框的keyup事件，再重新绑定
	 */
	$(SEARCH_COND_ID).unbind("keyup");  //移除click事件
	$(SEARCH_COND_ID).on('keyup', searchInputCallbackKeyup);
	function searchInputCallbackKeyup(){
		if(event.keyCode == 13){
			$(SEARCH_ID).trigger("click");	                
        }
	}
	
	/**
	 * 	解绑保存按钮的click事件，再重新绑定
	 */
	$(SAVE_ID).unbind("click");  //移除click事件
	$(SAVE_ID).on('click', saveCallbackClick);
	function saveCallbackClick(){
		
		if(self.selectedNotifyArr.length<=0){
			//dialog type: success warning info error,默认为info
			util.message("请选择【"+self.customerName+"】的通知单！", null, "warning");
			return false;
		}
		
		//设置选择账单类型，1：按通知单选择；2：按客户账单选择
		const SELECT_CUSTOMER_BILL = 2;//按客户账单选择
		const SELECT_CUSTOMER_NOTIFY = 1;//按客户通知单选择
		
		var isMergeNotify = $(IS_MARGE_NOTIFY_ID).prop("checked");//是否合并通知单
		console.log("是否合并通知单："+isMergeNotify);
		var isMergeBill = $(IS_MARGE_BILL_ID).prop("checked");//是否合并账单
		console.log("是否合并账单："+isMergeBill);
		var invoiceFeeType = $(INVOICE_WATER_FEE_TYPE).val();//开票水费类型
		console.log("开票水费类型："+invoiceFeeType);
		
		var data = new Object();
		data.type = SELECT_CUSTOMER_NOTIFY;
		data.customerId = self.customerId;
		data.mergeNotify = isMergeNotify;
		data.usePrice = invoiceFeeType;
		data.mergeAccountItem = isMergeBill;
		data.waterNotifyList = self.selectedNotifyArr;
		self.selectedCallback(data);
		
	}
	
	//--------------------客户通知单列表事件绑定部分--------------------
	/**
	 * 绑定列表行的单击事件
	 */
	$(SHOW_NOTIFY_BTN_CLASS).unbind("click");  //移除click事件
	$(SHOW_NOTIFY_BTN_CLASS).on("click", function(){
		
		console.log("click tr show tooltip");
		var notifyId = $(this).attr("data-bind-id");//通知单ID
		console.log("click get notify id is "+notifyId);
		
		self.loadNotifyDetail(notifyId);//加载通知单详情（加完成功后显示）
		
	});
	
	/**
	 * 	绑定列表行的双击事件
	 */
	$(INVOICE_ITEM_TR).unbind("dblclick");  //移除click事件
	$(INVOICE_ITEM_TR).on("dblclick", function(){
		//禁止双击选中文本
		window.getSelection ? window.getSelection().removeAllRanges() : document.selection.empty();
		
		var notifyId = $(this).attr("data-bind-id");//通知单ID
		console.log("dblclick get notify id is "+notifyId);
		
//		data-bind-id=${item.id}, 
//		data-customer-id=${item.customerId}, 
//		data-period=${item.period}, 
//		data-sum-base-amount=${item.sumBaseAmount}, 
//		data-sum-sewage-amount=${item.sumSewageAmount}, 
//		data-past-base-owe-amount=${item.pastBaseOweAmount}, 
//		data-past-sewage-owe-amount=${item.pastSewageOweAmount}, 
//		data-total-owe-amount=${item.totalOweAmount}
		
		const PREFIX = "#"+self.containerId;
		const INVOICE_ITEM_FLAG_TD_ID = PREFIX+" #customer-bill-table tbody tr td.selected-flag-"+notifyId;
		
		var html = $(INVOICE_ITEM_FLAG_TD_ID).html();
		if(html==null || html==""){//把账单信息增加到数组
			//var notifyId = $(this).attr("data-bind-id");//通知单ID
			var customerId = $(this).attr("data-customer-id");//客户ID
			var period = $(this).attr("data-period");//期间
			var sumBaseAmount = $(this).attr("data-sum-base-amount");//本期基础水费
			var sumSewageAmount = $(this).attr("data-sum-sewage-amount");//本期污水水费
			var pastBaseOweAmount = $(this).attr("data-past-base-owe-amount");//往期基础水费欠费
			var pastSewageOweAmount = $(this).attr("data-past-sewage-owe-amount");//往期污水水费欠费
			var totalOweAmount = $(this).attr("data-total-owe-amount");//总欠费金额
			
			var notifyObj = new Object();
			notifyObj.notifyId = notifyId;
			notifyObj.customerId = customerId;
			notifyObj.period = period;
			notifyObj.sumBaseAmount = sumBaseAmount;
			notifyObj.sumSewageAmount = sumSewageAmount;
			notifyObj.pastBaseOweAmount = pastBaseOweAmount;
			notifyObj.pastSewageOweAmount = pastSewageOweAmount;
			notifyObj.totalOweAmount = totalOweAmount;
			
			console.log("----------增加到数组的通知单对象："+JSON.stringify(notifyObj));
			
			var notifyDetailRes = $(this).attr("data-notify-res");//通知单明细
			//console.log("dblclick get notify detail list is "+JSON.parse(notifyDetailList));
			console.log("dblclick get notify detail res is "+notifyDetailRes);
			
			self.selectedNotifyArr.push(JSON.parse(notifyDetailRes));//增加到数组
		}else{//把账单信息从数据中删除
			var arr = self.selectedNotifyArr;
			for(var i=0; i<arr.length; i++){
				var currNotify = arr[i];
				console.log("curr notify is "+currNotify);
				var currNotifyId = currNotify.notifyId;
				console.log("notify id is "+notifyId+", curr notify id is "+currNotifyId);
				if(notifyId==currNotifyId){
					arr.splice(i, 1);
					console.log("----------从数组中删除的通知单对象："+JSON.stringify(currNotify));
					break;
				}
			}
		}
		
		console.log("----------用户已选择的账单对象【"+self.selectedNotifyArr.length+"】："+JSON.stringify(self.selectedNotifyArr));
		
		self.setSelectedFlag(notifyId);//设置选择/取消标志
		
	});
	
}

//---------------------------------------------
