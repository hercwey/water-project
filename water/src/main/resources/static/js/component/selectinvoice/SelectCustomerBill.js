//--------------------公共查询函数--------------------------
		
function SelectCustomerBill(containerId, selectedCallback){
	this.containerId = containerId; //选择客户发票信息对话框ID，与UI中的ID相对应
	this.selectedCallback = selectedCallback;//用户选中后的回调函数
	
	this.customerId = null;//客户ID
	this.customerName = null;//客户名称
	this.selectedBillArr = new Array();//用户选择的客户账单列表
	
}

/**
 * 初始化已选择客户账单列表
 */
SelectCustomerBill.prototype.initSelectedCustomerBillItem = function(){
	var self = this;
	var billArr = self.selectedBillArr;
	
	const PREFIX = "#"+self.containerId;
	const SELECTED_BILL_TBODY_ID = PREFIX+" #selected-customer-bill-tbody";//已选择的客户账单列表tbody的ID
	
	/**
	 * 获取每行的html字符串
	 */
	function getTrHtml(bill){
		var html = '<tr id="selected-bill-tr-'+bill.accountItemId+'" data-bill-id="'+bill.accountItemId+'" title="双击删除">';
			//html += '<td>'+bill.period+'</td>';//期间
			//html += '<td>'+bill.waterUse+'</td>';//水价
			html += '<td>'+bill.basePrice+'</td>';//基础水价
			html += '<td>'+bill.sewagePrice+'</td>';//污水水价
			html += '<td>'+bill.totalPrice+'</td>';//合计水价
			html += '<td>'+bill.realWaterAmount+'</td>';//合计水量
			html += '<td>'+bill.baseWaterFee+'</td>';//基础水费
			html += '<td>'+bill.sewageWaterFee+'</td>';//污水水费
			html += '<td>'+bill.totalWaterFee+'</td>';//合计水费
			html += '</tr>';
		return html;
	}
	/**
	 * 设置最后一行合计HTML
	 */
	function setLastTrHtml(totalWaterAmount, baseTotalFee, sewageTotalFee, totalFee){
		var html = '<tr data-bill-id="">';
			//html += '<td>合计：</td>';//期间
			//html += '<td></td>';//水价
			html += '<td>合计：</td>';//基础水价
			html += '<td></td>';//污水水价
			html += '<td></td>';//合计水价
			html += '<td>'+sumWaterAmount+'</td>';//合计水量
			html += '<td>'+sumBaseWaterFee+'</td>';//基础水费
			html += '<td>'+sumSewageWaterFee+'</td>';//污水水费
			html += '<td>'+sumTotalWaterFee+'</td>';//合计水费
			html += '</tr>';
		console.log("----------最后一行合计:"+html);
		$(SELECTED_BILL_TBODY_ID).append(html);//把合计行的html字符串添加到已选择账单列表
	}
	
	$(SELECTED_BILL_TBODY_ID).empty();//清空已选择账单列表
	
	var sumWaterAmount = 0;//合计水量
	var sumBaseWaterFee = 0;//合计基础水费
	var sumSewageWaterFee = 0;//合计污水水费
	var sumTotalWaterFee = 0;//合计水费
	$.each(billArr, function(){
		
		sumWaterAmount = Calc.Add(sumWaterAmount, this.realWaterAmount);//合计水量
		//console.log("----------合计水量："+sumWaterAmount);
		sumBaseWaterFee = Calc.Add(sumBaseWaterFee, this.baseWaterFee);//合计基础水费
		//console.log("----------合计基础水费："+sumBaseWaterFee);
		sumSewageWaterFee = Calc.Add(sumSewageWaterFee, this.sewageWaterFee);//合计污水水费
		//console.log("----------合计污水水费："+sumSewageWaterFee);
		sumTotalWaterFee = Calc.Add(sumTotalWaterFee, this.totalWaterFee);//合计水费
		//console.log("----------合计水费："+sumTotalWaterFee);
		
		var trHtml = getTrHtml(this);//获取每行的html字符串
		console.log("----------行:"+trHtml);
		$(SELECTED_BILL_TBODY_ID).append(trHtml);//把每行的html字符串添加到已选择账单列表
	});
	
	setLastTrHtml(sumWaterAmount, sumBaseWaterFee, sumSewageWaterFee, sumTotalWaterFee);//设置最后一行合计HTML
	
	self.initSelectedBillItemListener();//已选择客户账单选项卡中事件监听
	
}

/**
 * 初始化期间
 */
SelectCustomerBill.prototype.initPeriod = function(){
	
	var self = this;
	const PREFIX = "#"+self.containerId;
	const PERIOD_START_ID = PREFIX+" #period-start";//开始期间
	const PERIOD_END_ID = PREFIX+" #period-end";//结束期间
	
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
	console.log("----------期间："+getCurrSysMonth());
	$(PERIOD_START_ID).val(getCurrSysMonth());
	$(PERIOD_END_ID).val(getCurrSysMonth());
}

//--------------------------设置选择/取消已选择标志---------------------------------------------
/**
 * 	设置已选择标记
 */
SelectCustomerBill.prototype.setSelectedFlag = function(billId) {
	var self = this;
	const PREFIX = "#"+self.containerId;
	const INVOICE_ITEM_FLAG_TD_ID = PREFIX+" #invoice-bill-table tbody tr td.selected-flag-"+billId;
	
	var html = $(INVOICE_ITEM_FLAG_TD_ID).html();
	if(html==null || html==""){//增加
		var flag = '<i class="fa fa-check-square" aria-hidden="true"></i>';
		$(INVOICE_ITEM_FLAG_TD_ID).html(flag);
	}else{//删除
		//$(INVOICE_ITEM_FLAG_TD_ID).empty();
	}
	
}

/**
 * 	设置取消已选择标记
 */
SelectCustomerBill.prototype.setUnselectedFlag = function(billId) {
	var self = this;
	const PREFIX = "#"+self.containerId;
	const INVOICE_ITEM_FLAG_TD_ID = PREFIX+" #invoice-bill-table tbody tr td.selected-flag-"+billId;
	
	var html = $(INVOICE_ITEM_FLAG_TD_ID).html();
	if(html==null || html==""){//增加
		//var flag = '<i class="fa fa-check-square" aria-hidden="true"></i>';
		//$(INVOICE_ITEM_FLAG_TD_ID).html(flag);
	}else{//删除
		$(INVOICE_ITEM_FLAG_TD_ID).empty();
	}
	
}
//--------------------------查询---------------------------------------------
/**
 * 	公共查询函数
 */
SelectCustomerBill.prototype.searchCustomerBill = function(parms) {
	var self = this;
	var PREFIX = "#"+self.containerId;
	const INVOICE_ITEM_ID = PREFIX+" #customer-bill-body";
	//加载字典列表页面
	var url = BASE_CONTEXT_PATH + "/select-customer-bill/table"
	$(INVOICE_ITEM_ID).load(url, parms, customerResizeFuncx);
	
	function customerResizeFuncx(){
		
		//console.log("call back........");
		//console.log("------------------------------"+self);
		var myEvent = new Event('resize');
        window.dispatchEvent(myEvent);
        
        self.initListener();//初始化监听器
        self.initPager();//初始化pager
        self.initInvoiceBillItem();//初始化开票账单列表
		
	}
}

/**
 * 加载客户账单
 */
SelectCustomerBill.prototype.loadPage = function(customerId, customerName) {
	
	console.log("++++++++++++++++++++++++++++客户ID："+customerId+"，客户名称："+customerName);
	
	var self = this;
	self.customerId = customerId;//客户ID
	self.customerName = customerName;//客户名称
	
	const PREFIX = "#"+self.containerId;
	const PERIOD_START_ID = PREFIX+" #period-start";//开始期间
	const PERIOD_END_ID = PREFIX+" #period-end";//结束期间
	//const SEARCH_COND_ID = PREFIX+" #search-cond";
	const INVOICE_ITEM_ID = PREFIX+" #customer-bill-body";
	//const INVOICE_ITEM_ID = PREFIX+" #customer-bill-item";
	const SELECTED_BILL_TBODY_ID = PREFIX+" #selected-customer-bill-tbody";//已选择的客户账单列表tbody的ID
	
	const IS_MARGE_BILL_ID = PREFIX+" #dialog-operate-div #is-merge-bill";
	const INVOICE_WATER_FEE_TYPE = PREFIX+" #dialog-operate-div #invoice-fee-type input:radio";

	const CHECKBOX_WATER_FEE_ID = PREFIX+" #checkbox-water-fee";//水费checkbox
	const CHECKBOX_STORED_ID = PREFIX+" #checkbox-stored";//预存checkbox
	
	//设置查询水费checkbox和查询预存checkbox复选框为选中状态
	$(CHECKBOX_WATER_FEE_ID).prop("checked", true);//设置查询水费checkbox复选框选中
	$(CHECKBOX_STORED_ID).prop("checked", true);//设置查询预存checkbox复选框选中
	//设置已选择的账单列表选项卡中复选框和单选框的默认值
	$(IS_MARGE_BILL_ID).prop("checked", false);//设置合并账单复选框未勾选
	$(INVOICE_WATER_FEE_TYPE).eq(0).prop("checked", true);//设置开票水费类型为基础水费
	
	
	$(PREFIX+" #selected-customer-bill-nav a[href='#customer-bill-item']").tab("show");//跳转到账单列表选项卡
	
	console.log("-------------init period");
	self.initPeriod();//初始化期间
	console.log("-------------init period end ");
	 
	//$(SEARCH_COND_ID).val("");//设置查询条件为空
	self.selectedBillArr = new Array();//重新new个数组
	$(INVOICE_ITEM_ID).empty();//清空客户账单列表
	$(SELECTED_BILL_TBODY_ID).empty();//清空已选择的客户账单列表
	
	var pageNum = 0;
	var pageSize = 0;
	
//	customerId
//	periodStart
//	periodEnd
//	searchWaterFeeBill
//	searchStoredBill
	var parms = self.getSearchCustomerBillParams();//获取普通查询参数（不包括分页信息）
	
	parms.pageNum=pageNum;
	parms.pageSize=pageSize;
	
	//this.searchCustomerBill(parms);
	
	//加载字典列表页面
	
	console.log("===========默认加载选择客户账单信息JS对象："+this);
	console.log("===========默认加载选择客户账单信息JS对象："+self);
	
	parms.containerId=self.containerId;
	
	var params = new Object();
	params.parmsJSON = JSON.stringify(parms);
	console.log("----------默认查询参数："+JSON.stringify(parms));
	var url = BASE_CONTEXT_PATH + "/select-customer-bill/table"
	console.log("load customer bill id:"+INVOICE_ITEM_ID);
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
SelectCustomerBill.prototype.initPager = function() {
	console.log("init pager");
	var self = this;
	var containerId = self.containerId;
	var searchCond = self.getSearchCustomerBillParams();//获取普通查询参数（不包括分页信息）
	
	var parms = new Object();
	parms.serachCond = searchCond;
	parms.containerId = containerId;
	
	var parmsJSON = JSON.stringify(parms);
	
	//创建分页组件实例
	var pager=new Pager(containerId+"-pager-customer-bill");
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
		var url = BASE_CONTEXT_PATH + "/select-customer-bill/table"
		$(INVOICE_ITEM_ID).load(url, params, customerResizeFuncx);
		function customerResizeFuncx(){
			
			var myEvent = new Event('resize');
	        window.dispatchEvent(myEvent);
	        
	        self.initListener();//初始化监听器
	        self.initPager();//初始化pager		
			
	        self.initInvoiceBillItem();//初始化开票账单列表
		}
	}
	
}

/*
	查询按钮点击时调用
	采用用户所输入的查询条件进行查询.
*/
SelectCustomerBill.prototype.searchCustomerBillList = function() {
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
 * 		返回参数：
 * 			customerId
 * 			periodStart
 * 			periodEnd
 * 			searchWaterFeeBill
 * 			searchStoredBill
 * 
 */
SelectCustomerBill.prototype.getSearchCustomerBillParams = function(){
	var self = this;
	const PREFIX = "#"+self.containerId;
	const PERIOD_START_ID = PREFIX+" #period-start";//开始期间
	const PERIOD_END_ID = PREFIX+" #period-end";//结束期间
	const CHECKBOX_WATER_FEE_ID = PREFIX+" #checkbox-water-fee";//水费checkbox
	const CHECKBOX_STORED_ID = PREFIX+" #checkbox-stored";//预存checkbox
	//const SEARCH_COND_ID = PREFIX+" #search-cond";
	
	var customerId = self.customerId;//客户ID
	var periodStart = $(PERIOD_START_ID).val();//获取开始期间
	var periodEnd = $(PERIOD_END_ID).val();//获取结束期间
	var searchWaterFeeBill = $(CHECKBOX_WATER_FEE_ID).prop("checked");//水费checkbox
	console.log("查询水费账单："+searchWaterFeeBill);	
	var searchStoredBill = $(CHECKBOX_STORED_ID).prop("checked");//预存checkbox
	console.log("查询预存账单："+searchStoredBill);	
	//var searchCond = $(SEARCH_COND_ID).val();//获取用户录入的查询条件
	
	//初始化请求参数
	var parms = new Object();
	parms.customerId = customerId;
	parms.periodStart = periodStart;
	parms.periodEnd = periodEnd;
	parms.searchWaterFeeBill = searchWaterFeeBill;
	parms.searchStoredBill = searchStoredBill;
	//parms.searchCond=searchCond;
	return parms;
}

/**
 * 初始化开票账单列表
 */
SelectCustomerBill.prototype.initInvoiceBillItem = function(){
	console.log("init invoice bill item.");
	
	var self = this;
	const PREFIX = "#"+self.containerId;
	const INVOICE_ITEM_TR = PREFIX+" #invoice-bill-table tbody tr";
	
	//取消所有已选择的标志
	$(INVOICE_ITEM_TR).each(function(){
		var billId = $(this).attr("data-bill-id");
		console.log("设置取消已选择标志");
		self.setUnselectedFlag(billId);//设置取消已选择标志
		
	});
	
	var selectedArr = self.selectedBillArr;
	for(var i=0; i<selectedArr.length; i++){
		var currBill = selectedArr[i];
		var currBillId = currBill.accountItemId;
		
		console.log("设置已选择标志");
		self.setSelectedFlag(currBillId);//设置已选择标志
		
	}
	
}

//---------事件监听--------------
SelectCustomerBill.prototype.initListener = function(){
	var self = this;
	const PREFIX = "#"+self.containerId;
	const SELECTED_CUSTOMER_BILL_ITEM_LI_ID = PREFIX+" #selected-customer-bill-item-li";
	const INVOICE_BILL_ITEM_LI_ID = PREFIX+" #invoice-bill-item-li";
	
	const CHECKBOX_WATER_FEE_ID = PREFIX+" #checkbox-water-fee";//水费checkbox
	const CHECKBOX_STORED_ID = PREFIX+" #checkbox-stored";//预存checkbox
	//const SEARCH_COND_ID = PREFIX+" #search-cond";
	const SEARCH_ID = PREFIX+" #btn-search";
	const SEARCH_REST_ID = PREFIX+" #btn-search-reset";
	
	const INVOICE_ITEM_TBODY = PREFIX+" #customer-bill-table tbody";
	const INVOICE_ITEM_TR = PREFIX+" #invoice-bill-table tbody tr";
	
	const SAVE_ID = PREFIX+" #btn-bank-modify-and-back";
	
	const IS_MARGE_BILL_ID = PREFIX+" #dialog-operate-div #is-merge-bill";
	const INVOICE_WATER_FEE_TYPE = PREFIX+" #dialog-operate-div #invoice-fee-type input[name='waterFee']:checked";
	
	/**
	 * 解绑开票账单列表选项卡的click事件，再重新绑定
	 */
	$(INVOICE_BILL_ITEM_LI_ID).unbind("click");  //移除click事件
	$(INVOICE_BILL_ITEM_LI_ID).on('click', invoiceBillItemTabCallbackClick);
	function invoiceBillItemTabCallbackClick(){
		console.log("click tab init invoice bill item.");
		self.initInvoiceBillItem();//初始化开票账单列表
		
	}
	
	/**
	 * 解绑已选择客户账单列表选项卡的click事件，再重新绑定
	 */
	$(SELECTED_CUSTOMER_BILL_ITEM_LI_ID).unbind("click");  //移除click事件
	$(SELECTED_CUSTOMER_BILL_ITEM_LI_ID).on('click', selectedBillItemTabCallbackClick);
	function selectedBillItemTabCallbackClick(){
		console.log("init selected bill item.");
		self.initSelectedCustomerBillItem();//初始化已选择客户账单列表
	}
	
	/**
	 * 解绑水费checkbox复选框的click事件，再重新绑定
	 */
	$(CHECKBOX_WATER_FEE_ID).unbind("click");  //移除click事件
	$(CHECKBOX_WATER_FEE_ID).on('click', searchBillCallbackClick);
	/**
	 * 解绑预存checkbox复选框的click事件，再重新绑定
	 */
	$(CHECKBOX_STORED_ID).unbind("click");  //移除click事件
	$(CHECKBOX_STORED_ID).on('click', searchBillCallbackClick);
	/**
	 * 水费checkbox和预存checkbox复选框的click事件的回调函数
	 */
	function searchBillCallbackClick(){
		console.log("click checkbox search bill");
		
		var parms = self.getSearchCustomerBillParams();//获取普通查询参数（不包括分页信息）
		var searchWaterFeeBill = parms.searchWaterFeeBill;
		var searchStoredBill = parms.searchStoredBill;
		if(searchWaterFeeBill || searchStoredBill){//如果水费checkbox或预存checkbox有一个是选中状态时查询，否则清空账单列表
			console.log("click checkbox search bill is true");
			$(SEARCH_ID).click();
		}else{
			console.log("click checkbox search bill is false");
			$(INVOICE_ITEM_TBODY).empty();
		}
		
	}
	
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
	/*$(SEARCH_REST_ID).unbind("click");  //移除click事件
	$(SEARCH_REST_ID).on('click', searchResetCallbackClick);
	function searchResetCallbackClick(){
		$(SEARCH_COND_ID).val("");
		$(SEARCH_COND_ID).focus();  //获得焦点
	}*/
	
	/**
	 * 解绑查询条件输入框的keyup事件，再重新绑定
	 */
	/*$(SEARCH_COND_ID).unbind("keyup");  //移除click事件
	$(SEARCH_COND_ID).on('keyup', searchInputCallbackKeyup);
	function searchInputCallbackKeyup(){
		if(event.keyCode == 13){
			$(SEARCH_ID).trigger("click");	                
        }
	}*/
	
	/**
	 * 	解绑保存按钮的click事件，再重新绑定
	 */
	$(SAVE_ID).unbind("click");  //移除click事件
	$(SAVE_ID).on('click', saveCallbackClick);
	function saveCallbackClick(){
		
		if(self.selectedBillArr.length<=0){
			//dialog type: success warning info error,默认为info
			util.message("请选择【"+self.customerName+"】的账单！", null, "warning");
			return false;
		}
		
		//设置选择账单类型，1：按通知单选择；2：按客户账单选择
		const SELECT_CUSTOMER_BILL = 2;//按客户账单选择
		const SELECT_CUSTOMER_NOTIFY = 1;//按客户通知单选择
		
		var isMerge = $(IS_MARGE_BILL_ID).prop("checked");//是否合并账单
		console.log("合并账单："+isMerge);
		
		var invoiceFeeType = $(INVOICE_WATER_FEE_TYPE).val();//开票水费类型
		console.log("开票水费类型："+invoiceFeeType);
		
		var data = new Object();
		data.type = SELECT_CUSTOMER_BILL;
		data.customerId = self.customerId;
		data.mergeAccountItem = isMerge;
		data.usePrice = invoiceFeeType;
		data.accountItemList = self.selectedBillArr;
		self.selectedCallback(data);
		
	}
	
	//--------------------客户账单列表事件绑定部分--------------------
	
	/**
	 * 	绑定客户账单列表行的双击事件事件
	 */
	$(INVOICE_ITEM_TR).unbind("dblclick");  //移除click事件
	$(INVOICE_ITEM_TR).on("dblclick", function(){
		//禁止双击选中文本
		window.getSelection ? window.getSelection().removeAllRanges() : document.selection.empty();
		
		var billId = $(this).attr("data-bill-id");
		console.log("dblclick invoice bill item get bill id is "+billId);
		
//		data-bill-id=${item.id}, 
//		data-customer-id=${item.customerId}, 
//		data-period=${item.period}, 
//		data-base-price=${item.basePrice}, 
//		data-sewage-price=${item.sewagePrice}, 
//		data-total-price=${item.totalPrice}, 
//		data-water-amount=${item.waterAmount}, 
//		data-base-water-fee=${item.baseWaterFee}, 
//		data-sewage-water-fee=${item.sewageWaterFee}, 
//		data-total-water-fee=${item.totalWaterFee}
		
		const PREFIX = "#"+self.containerId;
		const INVOICE_ITEM_FLAG_TD_ID = PREFIX+" #invoice-bill-table tbody tr td.selected-flag-"+billId;
		
		var html = $(INVOICE_ITEM_FLAG_TD_ID).html();
		if(html==null || html==""){//把账单信息增加到数组
			//var billId = $(this).attr("data-bill-id");//账单ID
			//var customerId = $(this).attr("data-customer-id");//客户信息ID
			//var period = $(this).attr("data-period");//期间
			//var waterUse = $(this).attr("data-water-use");//水价
			var basePrice = $(this).attr("data-base-price");//基础水价
			var sewagePrice = $(this).attr("data-sewage-price");//污水水价
			var totalPrice = $(this).attr("data-total-price");//总水价
			var waterAmount = $(this).attr("data-water-amount");//水量
			var baseWaterFee = $(this).attr("data-base-water-fee");//基础水费
			var sewageWaterFee = $(this).attr("data-sewage-water-fee");//污水水费
			var totalWaterFee = $(this).attr("data-total-water-fee");//总水费
			
			var billObj = new Object();
			billObj.accountItemId = billId;
			//billObj.customerId = customerId;
			//billObj.period = period;
			//billObj.waterUse = waterUse;
			billObj.basePrice = basePrice;
			billObj.sewagePrice = sewagePrice;
			billObj.totalPrice = totalPrice;
			billObj.realWaterAmount = waterAmount;
			billObj.baseWaterFee = baseWaterFee;
			billObj.sewageWaterFee = sewageWaterFee;
			billObj.totalWaterFee = totalWaterFee;
			
			console.log("----------增加到数组的账单对象："+JSON.stringify(billObj));
			
			self.selectedBillArr.push(billObj);//增加到数组
			self.setSelectedFlag(billId);//设置已选择标志
			
		}else{//把账单信息从数据中删除
			var arr = self.selectedBillArr;
			for(var i=0; i<arr.length; i++){
				var currBill = arr[i];
				var currBillId = currBill.accountItemId;
				if(billId==currBillId){
					arr.splice(i, 1);
					console.log("----------从数组中删除的账单对象："+JSON.stringify(currBill));
					break;
				}
			}
			self.setUnselectedFlag(billId);//取消已选择标志
		}
		
		console.log("----------用户已选择的账单对象【"+self.selectedBillArr.length+"】："+JSON.stringify(self.selectedBillArr));
		
	});
	
}

/**
 * 已选择客户账单选项卡中事件监听
 */
SelectCustomerBill.prototype.initSelectedBillItemListener = function(){
	
	var self = this;
	var billArr = self.selectedBillArr;
	
	const PREFIX = "#"+self.containerId;
	const SELECTED_BILL_TBODY_TR = PREFIX+" #selected-customer-bill-tbody tr";
	
	/**
	 * 	绑定已选择客户账单选项卡中列表行的双击事件事件
	 */
	$(SELECTED_BILL_TBODY_TR).unbind("dblclick");  //移除click事件
	$(SELECTED_BILL_TBODY_TR).on("dblclick", function(){
		//禁止双击选中文本
		window.getSelection ? window.getSelection().removeAllRanges() : document.selection.empty();
		
		var billId = $(this).attr("data-bill-id");
		console.log("dblclick selected bill get bill id is "+billId);
		
		//const SELECTED_BILL_TBODY_TR_ID = PREFIX+" #selected-customer-bill-tbody #selected-bill-tr-"+billId;
		
		if(billId!=null && billId!=""){
			var arr = self.selectedBillArr;
			for(var i=0; i<arr.length; i++){
				var currBill = arr[i];
				var currBillId = currBill.accountItemId;
				if(billId==currBillId){
					arr.splice(i, 1);
					//$(SELECTED_BILL_TBODY_TR_ID).remove();//删除行
					console.log("----------从数组中删除的账单对象："+JSON.stringify(currBill));
					break;
				}
			}
			
			console.log("----------用户已选择的账单对象【"+self.selectedBillArr.length+"】："+JSON.stringify(self.selectedBillArr));
			
			console.log("deleted success, init selected bill item.");
			self.initSelectedCustomerBillItem();//初始化已选择客户账单列表
		}
		
	});
}

//---------------------------------------------
