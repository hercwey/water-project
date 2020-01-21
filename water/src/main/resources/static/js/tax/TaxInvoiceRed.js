function TaxInvoiceRedFun() {

	// 发票冲红对话框
	var DIALOG_TAX_RED = null;
	// 记录查询方式。0默认查询，1多条件查询
	this.G_GENERAL_SEARCH = null;
	var batchQueryInvoiceCondDialog = null;
	var invoiceConfirmDialog = null;
	var selectedInvoice = null;//已选中发票信息

}

TaxInvoiceRedFun.prototype.init = function(batchQueryInvoiceCondDialog) {
	var self = this;

	// 发票冲红对话框
	self.DIALOG_TAX_RED = "modal-container-tax-red-dialog";
	// 记录查询方式。0默认查询，1多条件查询
	self.G_GENERAL_SEARCH = 0;
	self.batchQueryInvoiceCondDialog = batchQueryInvoiceCondDialog;

	// 设置系统默认年份
	self.setYearDefault();
	// ------------init-------------
	// 主页:默认查询(加载默认列表)
	self.defaultSearch();
	self.initListener();
	self.initInvoiceConfirmDialog();// 初始化发票确认对话框

}
/**
 * 获取当前系统年份（表示为发票查询的年份）
 */
TaxInvoiceRedFun.prototype.getCurrSysYear = function() {
	var date = new Date();
	var year = date.getFullYear(); // 获取完整的年份(4位,1970-????)
	return year;
}

/**
 * 设置账单期间默认值为系统月份
 */
TaxInvoiceRedFun.prototype.setYearDefault = function() {
	var self = this;
	const INVOICE_YEAR = "#invoice-year";
	$(INVOICE_YEAR).val(self.getCurrSysYear());
}

/*
 * 公共查询函数
 * 
 * 查询核对类型列表
 */
TaxInvoiceRedFun.prototype.search = function(parms) {
	var self = this;
	var url = BASE_CONTEXT_PATH + "/tax-invoice/table"
	
	self.selectedInvoice = null;//设置已选中发票信息为空
	
	$(".tax-invocie-table").load(url, parms, resizeFuncx);
	function resizeFuncx() {
		var myEvent = new Event('resize');
		window.dispatchEvent(myEvent);
		
		self.initTableListener();//初始化发票列表的click事件
		self.chbzDetailClick();//初始化发票列表中红字信息查看click事件
	}
}

// 获取查询参数
TaxInvoiceRedFun.prototype.getSearchParams = function() {
	const SEARCH_FORM = "#tax-invoice-search-form ";
	const SEARCH_COND = SEARCH_FORM + ".search-cond";
	const INVOICE_YEAR = SEARCH_FORM + ".invoice-year";
	const INVOICE_MONTH = SEARCH_FORM + ".invoice-month";
	const INVOICE_FPZL = SEARCH_FORM + ".invoice-fpzl";

	var searchCond = $(SEARCH_COND).val();
	var year = $(INVOICE_YEAR).val();
	var month = $(INVOICE_MONTH).val();
	var fpzl = $(INVOICE_FPZL).val();

	// 初始化请求参数
	var obj = new Object();
	obj.searchCond = searchCond;
	obj.year = year;
	obj.month = month;
	obj.fpzl = fpzl;
	return obj;
}

/*
 * 默认查询 Retrieve(default)查询(加载默认列表)
 */
TaxInvoiceRedFun.prototype.defaultSearch = function() {

	var self = this;

	var pageNum = 0;
	var pageSize = 0;

	// 获取参数
	var parms = null;
	console.log("=======================G_GENERAL_SEARCH:"
			+ self.G_GENERAL_SEARCH);
	if (self.G_GENERAL_SEARCH == 0) {
		parms = self.getSearchParams();// 获取普通查询参数（不包括分页信息）
	} else if (self.G_GENERAL_SEARCH == 1) {
		parms = self.advancedSearchParams();// 获取高级查询参数
	}


	parms.pageNum = pageNum;
	parms.pageSize = pageSize;

	self.search(parms);
}

/*
 * 设置日期选择input背景色
 */
TaxInvoiceRedFun.prototype.setBackGround = function() {
	const SEARCH_FORM = "#tax-invoice-search-form ";
	const INVOICE_YEAR = SEARCH_FORM + ".invoice-year";

	$(INVOICE_YEAR).css("background-color", "white");

}

/*
 * 注:公共函数 功能:返回指定日期相应月份的第一天的日期 返回:月份的第一天的日期
 * 
 */
TaxInvoiceRedFun.prototype.getFirstDayDateOfMonth = function(datex) {
	var today = new Date(datex);
	var year = today.getFullYear();
	console.log("year is:" + year);
	var month = today.getMonth() + 1;
	if (month < 10) {
		month = "0" + month;
	}
	var dateStart = year + '-' + month + '-' + '01';
	return dateStart;
}

/*
 * 注:公共函数 功能:返回指定日期相应月份的最后一天的日期 返回:月份的最后一天的日期
 */
TaxInvoiceRedFun.prototype.getLastDayDateOfMonth = function(datex) {

	var self = this;

	var today = new Date(datex);
	var year = today.getFullYear();
	var month = today.getMonth() + 1;
	var days = self.getMonthDays(year, month);

	if (month < 10) {
		month = "0" + month;
	}

	var dateEnd = year + '-' + month + '-' + days;

	return dateEnd;
}

// 业务相关-设置开票日期起
TaxInvoiceRedFun.prototype.setQueryDateStart = function(datex) {
	var self = this;

	var dateStart = self.getFirstDayDateOfMonth(datex);
	console.log("date start is:" + dateStart);
	batchQueryInvoiceCondDialog.setKPRQQ(dateStart);
}

// 业务相关-设置开票日期止
TaxInvoiceRedFun.prototype.setQueryDateEnd = function(datex) {
	var self = this;

	var dateEnd = self.getLastDayDateOfMonth(datex);
	console.log("date end is:" + dateEnd);
	batchQueryInvoiceCondDialog.setKPRQZ(dateEnd);
}

/*
 * 函数:获取指定年份,月份的最大天数. 天 的值是0，js会返回指定月份的最后一天，即最大天数。
 */
TaxInvoiceRedFun.prototype.getMonthDays = function(year, month) {
	return new Date(year, month, 0).getDate();
}

/**
 * 初始化发票确认对话框
 */
TaxInvoiceRedFun.prototype.initInvoiceConfirmDialog = function() {
	var self = this;

	var invoiceComfirmDialog = new InvoiceHMConfirmDialog(
			"invoice-confirm-dialog"); // 发票确认对话框

	self.invoiceConfirmDialog = invoiceComfirmDialog;
}


/**
 * 返回所选条目的 id(每行对应一个唯一的ID)
 * 
 * @returns
 */
TaxInvoiceRedFun.prototype.getSelectedInvoice = function(that) {
	
	console.log("get selected invoice");
	
	var self = this;
	const TABLE_ID = "#tax-invoice-table";
	const TABLE_TR = TABLE_ID+" tbody tr";
	
	var isSelected = $(that).hasClass("selected-color");
	if(!isSelected){
		$(TABLE_TR).removeClass("selected-color");//取消列表所有行选中颜色
		$(that).addClass("selected-color");//选中行设置行颜色
		
		var id = $(that).attr("data-bind"); //
		var fpzl = $(that).attr("data-fpzl"); //
		var chbz = $(that).attr("data-chbz"); //
		var zfbz = $(that).attr("data-zfbz"); //

		var fpdm = $(that).attr("data-fpdm"); //
		var fphm = $(that).attr("data-fphm"); //

		var obj = new Object();
		obj.id = id;
		obj.fpzl = fpzl;
		obj.chbz = chbz;
		obj.zfbz = zfbz;
		obj.fpdm = fpdm;
		obj.fphm = fphm;
		self.selectedInvoice = obj;//设置已选中发票信息
		
	}else{
		$(TABLE_TR).removeClass("selected-color");//取消列表所有行选中颜色
		self.selectedInvoice = null;//设置已选中发票信息为空
	}
	
	// 扫描用户所选择条目ID
	/*$(".item-selector").each(function(index, elem) {
		if (this.checked) { // 对于己选条目
			var id = $(this).attr("data-bind"); //
			var fpzl = $(this).attr("data-fpzl"); //
			var chbz = $(this).attr("data-chbz"); //
			var zfbz = $(this).attr("data-zfbz"); //

			var fpdm = $(this).attr("data-fpdm"); //
			var fphm = $(this).attr("data-fphm"); //

			var obj = new Object();
			obj.id = id;
			obj.fpzl = fpzl;
			obj.chbz = chbz;
			obj.zfbz = zfbz;
			obj.fpdm = fpdm;
			obj.fphm = fphm;
			self.selectedInvoice = obj;
			return false;
		}
	});*/ // end of item-selector
	console.log("selected invoice is :"+JSON.stringify(self.selectedInvoice));
}

/**
 * 点击冲红标志icon事件
 */
TaxInvoiceRedFun.prototype.chbzDetailClick = function() {
	
	// 点击发票信息中冲红标志的icon，显示发票的红字信息
	$("#tax-invoice-table span[name='redInfoChbz']").on('click', function(){
		var fphm = $(this).attr("data-fphm"); //发票号码
		var fpdm = $(this).attr("data-fpdm"); //发票代码
		var chbz = $(this).attr("data-chbz"); //冲红标志
		if(chbz == "0"){
			return false;
		}
		previewRedInfoCallback(fphm, fpdm);
	});
	
	// 点击发票信息中发票代码的icon，显示发票的红字信息
	$("#tax-invoice-table span[name='redInfoFpdm']").on('click', function(){
		var fphm = $(this).attr("data-fphm"); //发票号码
		var fpdm = $(this).attr("data-fpdm"); //发票代码
		var chbz = $(this).attr("data-chbz"); //冲红标志
		if(chbz == "0"){
			return false;
		}
		previewRedInfoCallback(fphm, fpdm);
	});
	
	// 点击发票信息中发票号码的icon，显示发票的红字信息
	$("#tax-invoice-table span[name='redInfoFphm']").on('click', function(){
		var fphm = $(this).attr("data-fphm"); //发票号码
		var fpdm = $(this).attr("data-fpdm"); //发票代码
		var chbz = $(this).attr("data-chbz"); //冲红标志
		if(chbz == "0"){
			return false;
		}
		previewRedInfoCallback(fphm, fpdm);
	});
	
	//显示红字信息表信息
	function previewRedInfoCallback(fphm, fpdm){
		var self = this;
		const NOTIFY_TOOLTIP_ID = "#customer-notify-tooltip";
		const NOTIFY_CONTAINER_CLASS = "#customer-notify-tooltip .water-fee-notify-container";
		var parms = new Object();
		parms.fphm = fphm;
		parms.fpdm = fpdm;
		var url = BASE_CONTEXT_PATH + "/tax-invoice-red/tax-red-info";
		$(NOTIFY_CONTAINER_CLASS).load(url, parms, loadNotifyDetailCallback);
		function loadNotifyDetailCallback(res){
	        
	        $(NOTIFY_TOOLTIP_ID).popup('show');
	        
		}
	}

}
/**
 * 获取发票种类
 */
TaxInvoiceRedFun.prototype.getFpzlStr = function(fpzl) {
	var fpzlStr = "专用发票";
	if (fpzl == "2") {
		fpzlStr = "普通发票";
	}
	return fpzlStr;
}

TaxInvoiceRedFun.prototype.initListener = function() {
	var self = this;

	const SEARCH_FORM = "#tax-invoice-search-form ";
	const INVOICE_FPZL = SEARCH_FORM + ".invoice-fpzl";
	const INVOICE_YEAR = SEARCH_FORM + ".invoice-year";
	const INVOICE_MONTH = SEARCH_FORM + ".invoice-month";
	const SEARCH_COND = SEARCH_FORM + ".search-cond";
	const BTN_SEARCH = "#btn-general-search";//高级查询
	const BTN_SIMPLE_SEARCH = "#btn-tax-search";//普通查询
	
	$(INVOICE_FPZL).on('change', function() {// 发票种类下拉框改变事件
		self.G_GENERAL_SEARCH = 0;
		self.defaultSearch();
	});

	$(INVOICE_MONTH).on('change', function() {// 月份下拉框改变事件
		self.G_GENERAL_SEARCH = 0;
		self.defaultSearch();
	});
	
	// 普通查询按钮click事件
	$(BTN_SIMPLE_SEARCH).on('click', function() {
		self.defaultSearch();
	});
	
	/*
		查询条件输入框 KEYUP
	*/
	$(SEARCH_COND).on('keyup', searchInputCallbackKeyup);
	function searchInputCallbackKeyup(){
		if(event.keyCode == 13){
			$(BTN_SIMPLE_SEARCH).trigger("click");	                
	    }
	}

	// 高级查询按钮click事件
	$(BTN_SEARCH).on('click', function() {
		var year = $(INVOICE_YEAR).val();
		var month = $(INVOICE_MONTH).val();
		var datex = year;
		if (month != null && month != "") {
			datex = datex + "," + month;
		}
		// 设置查询初始日期
		self.setQueryDateStart(datex);
		// 设置查询截止日期
		self.setQueryDateEnd(datex);
		self.batchQueryInvoiceCondDialog.show();

	});

	// 冲红按钮click事件
	function bindTaxInvoiceRedClickEvent() {
		// 获取所选条目
		var selectInvocie = self.selectedInvoice;//获取已选中发票信息
		if (selectInvocie == null) {// 发票作废时只能选择一条
			util.message("请选择一张发票");
			return false;
		}
		var fpzl = selectInvocie.fpzl;
		console.log(fpzl)
		var invoiceId = selectInvocie.id;
		var chbz = selectInvocie.chbz;
		if (chbz == "1") {
			util.message("您选择的发票已完成过冲红操作，不能重复操作");
			return false;
		}
		/**
		 * 打开冲红弹窗的回调函数
		 */
		function openInvoiceRedCallback(data) {
			console.log("----------selected callback");
			console.log("user selected bill detail is : "
					+ JSON.stringify(data));

			if (data.success) {
				self.defaultSearch();// 刷新页面
			} else {
				util.message("冲红失败，请联系管理员！");
			}

		}

		var redDialogId = "invoice-red-dialog-1";
		// 创建实例
		var taxInvoiceRedDialogFun = new TaxInvoiceRedDialogFun(redDialogId,
				openInvoiceRedCallback);
		console.log(taxInvoiceRedDialogFun.dialogId)
		taxInvoiceRedDialogFun.open(invoiceId, fpzl);// 打开对话框

	}
	// 冲红按钮click事件
	$('.btn-tax-red').on('click', bindTaxInvoiceRedClickEvent);

	// 作废按钮click事件
	$('.btn-destory-invoice').on('click', confirmDestoryInvocieClickEvent);

	// 发票作废提示框是否作废发票
	function confirmDestoryInvocieClickEvent() {
		var selectInvocie = self.selectedInvoice;//获取已选中发票信息
		if (selectInvocie == null) {
			util.message("请选择一张发票");
			return false;
		}
		var zfbz = selectInvocie.fpzl;
		var fpdm = selectInvocie.fpdm;
		var fphm = selectInvocie.fphm;
		var fpzl = selectInvocie.fpzl;
		var fpzlStr = self.getFpzlStr(fpzl);
		var invoiceId = selectInvocie.id;

		if (zfbz == 1) {
			util.message("该发票已作废，不能重复操作");
			return false;
		}

		showInvoiceDestoryDialog();

	}
	/**
	 * 显示发票作废对话框
	 */
	function showInvoiceDestoryDialog() {
		var invoiceDestoryDialog = self.invoiceConfirmDialog;
		invoiceDestoryDialog.setTitle("发票作废");
		invoiceDestoryDialog
				.setMessage("现在显示的为将要作废的发票的种类、代码、号码，请认真核对。请确认是否作废本张发票？");
		
		var selectedInvoice = self.selectedInvoice;//获取已选中发票信息

		var fpzlStr = self.getFpzlStr(selectedInvoice.fpzl);
		invoiceDestoryDialog.setInvoiceType(fpzlStr);
		invoiceDestoryDialog.setInvoiceDM(selectedInvoice.fpdm);
		invoiceDestoryDialog.setInvoiceHM(selectedInvoice.fphm);
		invoiceDestoryDialog.setButtonOkCallback(destoryOkCallback);
		invoiceDestoryDialog.setButtonCancelCallback(destoryCancelCallback);
		invoiceDestoryDialog.show();

		//TODO: 发票作废处理
		function destoryOkCallback() {
			console.log("button destory ok callback function is called!");
			console.log("----------"+JSON.stringify(selectedInvoice));

			sendCancelRequest(selectedInvoice,invalidCallback);

			//TODO: 需要刷新当前页.  2019/12/23

		}


		/**
		 * 发送作废发票请求
		 * selectedInvoice:选定的当前发票
		 * callback 请求后的回调函数
		 */			
		function sendCancelRequest(selectedInvoice,callback){
			const url = BASE_CONTEXT_PATH + "/tax/invalidinvoice/invalid/json"; 
		
			var invoiceInvalid = new InvoiceInvalid();
			invoiceInvalid.setFPZL(selectedInvoice.fpzl);
			invoiceInvalid.setFPDM(selectedInvoice.fpdm);
			invoiceInvalid.setFPHM(selectedInvoice.fphm);
	
			//打印请求参数
			var params = new Array();
			params.push(invoiceInvalid); //OBJ---放置到--->Array
	
			//准备请求参数.		
			var paramsStr = JSON.stringify(params); //转换成JSON格式而后向后台传递参数
			var reqParm = new Object();             //请求参数对象
			reqParm.invoiceListJSON = paramsStr; 
	
			//发送打印请求
			$.post(url, reqParm, callback);
		}

		//作废发票回调
		function  invalidCallback(resp){
			if (resp != null && resp != "") {
				//var obj = $.parseJSON(res);						
				if (resp.result_code == "success") {  //发票开具成功					
					console.log("发票查询页面-作废发票请求成功!");	
					self.defaultSearch();//作废成功后刷新界面
					util.message(resp.result_msg, null, "success");					
					//self.showPopupDialog(resp.result_msg, resetIssue);										
					
				} else {  //开具失败
					//dialog type: success warning info error,默认为info
					util.message(resp.result_msg, null, "error");
					console.log("发票查询页面-作废发票失败!");					
                    //self.showPopupDialog("每页暂停模式:开具发票失败,请查询是否已经插入金税盘或是已经打开客户端.", null);
                    
					return;					
				}
            }
		}





		function destoryCancelCallback() {
			console.log("button destory cancel callback function is called!");
		}
	}
	
	
	// 打印按钮click事件
	$('.btn-print-invoice').on('click', confirmPrintInvocieClickEvent);
	
	// 发票打印提示框是否打印发票
	function confirmPrintInvocieClickEvent() {
		var selectInvocie = self.selectedInvoice;//获取已选中发票信息
		if (selectInvocie == null) {
			util.message("请选择一张发票");
			return false;
		}
		var zfbz = selectInvocie.fpzl;
		var fpdm = selectInvocie.fpdm;
		var fphm = selectInvocie.fphm;
		var fpzl = selectInvocie.fpzl;
		var fpzlStr = self.getFpzlStr(fpzl);
		var invoiceId = selectInvocie.id;

		showInvoicePrintDialog();

	}
	
	/**
	 * 显示发票打印对话框
	 */
	function showInvoicePrintDialog() {
		var invoicePrintDialog = self.invoiceConfirmDialog;
		invoicePrintDialog.setTitle("发票打印");
		invoicePrintDialog.setMessage("现在显示的为将要打印的发票的种类、代码、号码，请认真核对。请确认是否打印本张发票？");
		
		var selectedInvoice = self.selectedInvoice;//获取已选中发票信息
		var fpzlStr = self.getFpzlStr(selectedInvoice.fpzl);
		invoicePrintDialog.setInvoiceType(fpzlStr);
		invoicePrintDialog.setInvoiceDM(selectedInvoice.fpdm);
		invoicePrintDialog.setInvoiceHM(selectedInvoice.fphm);
		invoicePrintDialog.setButtonOkCallback(printOkCallback);
		invoicePrintDialog.setButtonCancelCallback(printCancelCallback);
		invoicePrintDialog.show();

		//TODO: 打印发票处理
		function printOkCallback() {
			console.log("button print ok callback function is called!");
			console.log("----------"+JSON.stringify(selectedInvoice));

			sendPrintRequest(selectedInvoice,printCallback); 

			//TODO: 需要加入刷新当前页处理  2019/12/23
		}


		/**
		 * 发送打印发票请求
		 * selectedInvoice:选定的当前发票
		 * callback 请求后的回调函数
		 */			
		function sendPrintRequest(selectedInvoice,callback){
			const url = BASE_CONTEXT_PATH + "/tax/printinvoice/print"; //请求打印发票		
		
			var invoicePrint = new InvoicePrint();
			invoicePrint.setFPZL(selectedInvoice.fpzl);
			invoicePrint.setFPDM(selectedInvoice.fpdm);
			invoicePrint.setFPHM(selectedInvoice.fphm);
	
			//打印请求参数
			var params = new Array();
			params.push(invoicePrint); //invoicePrint---放置到--->Array
	
			//准备请求参数.		
			var paramsStr = JSON.stringify(params); //转换成JSON格式而后向后台传递参数
			var reqParm = new Object();             //请求参数对象
			reqParm.invoiceListJSON = paramsStr; //参见TaxController中 /printinvoice/print
	
			//发送打印请求
			$.post(url, reqParm, callback);
		}

		//打印回调
		function  printCallback(resp){
			if (resp != null && resp != "") {
				//var obj = $.parseJSON(res);						
				if (resp.result_code == "success") {  //发票开具成功					
					console.log("发票查询页面-打印发票请求成功!");
					self.defaultSearch();//打印成功后刷新界面
					util.message(resp.result_msg, null, "success");					
					//self.showPopupDialog(resp.result_msg, resetIssue);										
					
				} else {  //开具失败
					//dialog type: success warning info error,默认为info
					util.message(resp.result_msg, null, "error");
					console.log("发票查询页面-打印发票失败!");					
                    //self.showPopupDialog("每页暂停模式:开具发票失败,请查询是否已经插入金税盘或是已经打开客户端.", null);
                    
					return;					
				}
            }
		}

		function printCancelCallback() {
			console.log("button print cancel callback function is called!");
		}
	}

	/**
	 *  显示发票详情click事件
	 */	
	$('.btn-tax-detail').on('click', displayInvocieDetailClickEvent);
	//TODO: 显示发票详情
	function displayInvocieDetailClickEvent(){
		//(1)获取当前选择的发票对象
		var selectInvocie = self.selectedInvoice;//获取已选中发票信息
		if (selectInvocie == null) {
			util.message("请选择一张发票");
			return false;
		}
		var zfbz = selectInvocie.fpzl;  
		var fpdm = selectInvocie.fpdm;
		var fphm = selectInvocie.fphm;
		var fpzl = selectInvocie.fpzl;
		var fpzlStr = self.getFpzlStr(fpzl);
		var invoiceId = selectInvocie.id;

		if(fpzl=="2"){
			var normalDialog=new DisplayInvoiceNormal("display-normal-dialog");
			normalDialog.setTitle("显示普通发票");
			//normalDialog.setMessage("2222222");		
			normalDialog.show(invoiceId);
		}
		else{
			var specialDialog=new DisplayInvoiceSpecial("display-special-dialog");
			specialDialog.setTitle("显示专用发票");
			//specialDialog.setMessage("2222222");
			specialDialog.show(invoiceId);
		}
	}
}

/**
 * 初始化发票列表的click事件
 */
TaxInvoiceRedFun.prototype.initTableListener = function() {
	
	var self = this;
	const TABLE_ID = "#tax-invoice-table";
	const TABLE_TR = TABLE_ID+" tbody tr";
	/**
	 * 绑定发票列表行的click事件
	 */
	$(TABLE_TR).on('click', clickRowCallback);
	function clickRowCallback(){
		console.log("----------click row");
		self.getSelectedInvoice(this);
	}
	//绑定双击显示发票详情
	$(TABLE_TR).on('dblclick', doubleClickRowCallback);
	function doubleClickRowCallback(){
		const TABLE_ID = "#tax-invoice-table";
		const TABLE_TR = TABLE_ID+" tbody tr";
		//判断双击操作之前的状态，若该行未选中则执行选中操作
		var isSelected = $(this).hasClass("selected-color");
		if(!isSelected){
			self.getSelectedInvoice(this);
		}
		
		 $( ".btn-tax-detail" ).trigger( "click" );
	}
}
