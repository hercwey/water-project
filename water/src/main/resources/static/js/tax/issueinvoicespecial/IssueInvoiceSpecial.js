/*
 专用开具发票 前端操作对象
 功能:构造函数;
 构造参数:
 	salerInvoiceInfo  销方默认开票信息,参见内嵌JS对象(来自后台)
 	loginUser		  登录用户对象,参见内嵌JS对象(来自后台)
 */
function IssueInvoice(salerInvoiceInfo, loginUserName) {
	var self = this;

	this.salerIssueInvoiceInfo = new IssueInvoiceInfo(); //销方开票信息对象
	this.buyerIssueInvoiceInfo = new IssueInvoiceInfo(); //购方开票信息对象

	//购方信息
	//this.currCustomerId = ""; //当前客户ID(并非客户开票信息条目的ID)	
	//this.currCustomerInvoiceName = ""; //当前客户名称(开票企业名称)

	//设置销方对象开票信息
	//参数格式:
	/* {
		private String shortCode;  	//暂未使用
		private String companyName; //公司名称
		private String regAddress;	//地址
		private String telephone;	//电话
		private String accountBank;	//银行名称		
		private String accountNo;	//账号		
		private String taxNo;		//税号				    
		private Integer enabled;	//是否有效.
		checker
		payee

	} */
	function initSalerIssueInvoiceInfo(salerInvoiceInfo, loginUserName) {
		/* this.customerId="";		    
			this.name = "";
			this.customerName="";
			this.taxNo = "";
			this.address = "";
			this.tel = "";
			this.bank = "";
			this.accountNo = ""; 
			checker
			payee
			operator

		*/

		self.salerIssueInvoiceInfo.setCustomerId("");
		self.salerIssueInvoiceInfo.setName(self.getStr(salerInvoiceInfo.companyName));
		self.salerIssueInvoiceInfo.setTaxNo(self.getStr(salerInvoiceInfo.taxNo));
		self.salerIssueInvoiceInfo.setAddress(self.getStr(salerInvoiceInfo.regAddress));
		self.salerIssueInvoiceInfo.setTel(self.getStr(salerInvoiceInfo.telephone));
		self.salerIssueInvoiceInfo.setBank(self.getStr(salerInvoiceInfo.accountBank));
		self.salerIssueInvoiceInfo.setAccountNo(self.getStr(salerInvoiceInfo.accountNo));

		self.salerIssueInvoiceInfo.setChecker(self.getStr(salerInvoiceInfo.checker));
		self.salerIssueInvoiceInfo.setPayee(self.getStr(salerInvoiceInfo.payee));
		self.salerIssueInvoiceInfo.setOperator(self.getStr(loginUserName));
	}

	initSalerIssueInvoiceInfo(salerInvoiceInfo, loginUserName); //初始化销方开票信息对象



	//(2)发票号码确认对话框对象
	this.invoiceHMConfirmDialog = new InvoiceHMConfirmDialog("invoicehm-confirm-dialog-1"); //发票号码确认对话框-专票

	//(3)打印对话框对象.
	this.printDialog = new InvoicePrintDialog("print-dialog1"); //发票打印对话框(可以调整打印参数)	

	//(4)声明专用发票对象
	this.invoiceSpecial = new InvoiceSpecial("invoice-special-1", InvoiceType.FPZL_SPECIAL_INVOICE); //初始化专用发票类型
	this.invoiceSpecial.init(); //初始化编辑组件事件


	//(5)创建实例  客户开票信息
	var dialogId = "invoice-dialog-1"; //客户开票信息选择对话框ID
	this.customerInvoiceDialog = new SelectCustomerInvoice(dialogId, selectedCustomerIssueInvoiceInfo);

	//(6)选择客户账单.
	var billDialogId = "select-invoice-bill-dialog-1";
	this.selectCustomerBillDialog = new SelectInvoiceBillDialog(billDialogId, selectedCustomerBill);

	//用户选择的账单对象,可能有两种格式:(1)按水费通知单(2)按用户账单	
	this.currCustomerBill = null; //added by hz  on 2019/12/03
	this.preDistInvoiceList = null; //预分配发票列表.


	this.invoiceInventory = null; //发票库存对象

	this.mCurrDistInvoiceIndex = -1; //当前选定的预生成发票索引号


	this.initIssueInvoiceUI();
	this.initListener(); //初始化操作面板及测试按钮listener.
	this.initPreInvoicePanel(); //初始预生成发票处理面板;  added by hz  2019/12/15
	this.initPrintDialog(); //初始化打印对话框
	this.initInvoiceHMConfirmDialog(); //初始化发票号码确认对话框.



	//TODO: TEMP MASK 暂时显示开票信息显示用于DEBUG,  临时处理. 
	//this.hideIssueInvoice();  //隐藏发票开具界面


	//--------------选择客户开票信息处理函数-----------------

	/*
		开票员选择客户开票信息-回调函数
		{
		     "invoiceId": "544",	//开票信息ID
	         "customerId": "57097",	//客户信息ID
	         "invoiceName": "石家庄藏诺药业股份有限公司",	 //开票名称
	         "customerName":  "两个名字合成后的单位名称"   //新加入
	         "taxNo": "9113010076340699X6",				 //税号
	         "accountBank": "中国工商银行石家庄高新支行",	 //开户行
	         "accountNo": "0402021619300055237",		 //开户账号
	         "regAddress": "河北省石家庄市高新区仓盛路518号", //地址
	         "telephone": "0311-68015839"//电话
		}

		//设置购方开票信息数据结构
		buyerInfo的形式如下所示:
		{
			name:"",   		//名称(开票名称)
			customerName:"",  //显示名称(单位)
			taxNo:"",		//税号
			address:"",  	//地址
			tel:"",	  		//电话
			bank:"",	 	//银行
			accountNo:"" 	//账号
		}
		参数说明:
			invoice用于自对话框中回带参数.
	 */
	function selectedCustomerIssueInvoiceInfo(dialogIssueInvoiceInfo) {
		console.log("----------selected callback");
		console.log("客户开票信息返回");
		console.log("所选择的购方开票信息为:" + JSON.stringify(dialogIssueInvoiceInfo)); //debug code //added by hz 2019/12/13

		//(1)保存"购方开票信息"对象 根据开票员的选择设置购方开票信息.对象字段关系转换.
		var buyerInfo = saveBuyerIssueInvoiceInfo(dialogIssueInvoiceInfo);

		//(2)设置发票中购方开票信息.		
		self.setInvoiceBuyerInfo(buyerInfo);

		//(3)打开选择客户账单对话框
		self.selectCustomerBillDialog.open(buyerInfo.getCustomerId(), buyerInfo.getCustomerName());

		console.log("user selected invoice id is : " + JSON.stringify(dialogIssueInvoiceInfo));
	}

	//将自对话框返回的数据转换成业务逻辑中相关对象
	function saveBuyerIssueInvoiceInfo(issueInvoiceInfo) {

		var buyerInvoiceInfo = self.buyerIssueInvoiceInfo;

		//如果没有对应的值时,则为""字符串.
		buyerInvoiceInfo.customerId = self.getStr(issueInvoiceInfo.customerId);
		buyerInvoiceInfo.name = self.getStr(issueInvoiceInfo.invoiceName);
		buyerInvoiceInfo.customerName = self.getStr(issueInvoiceInfo.customerName);
		buyerInvoiceInfo.taxNo = self.getStr(issueInvoiceInfo.taxNo);
		buyerInvoiceInfo.address = self.getStr(issueInvoiceInfo.regAddress);
		buyerInvoiceInfo.tel = self.getStr(issueInvoiceInfo.telephone);
		buyerInvoiceInfo.bank = self.getStr(issueInvoiceInfo.accountBank);
		buyerInvoiceInfo.accountNo = self.getStr(issueInvoiceInfo.accountNo);



		return buyerInvoiceInfo;
	}


	//-----------------用户选择账单后的处理函数.--------------

	/**
	 * 选择用户账单处理函数
	 * @param {*} data  其格式如下所示:	 
	 */
	function selectedCustomerBill(data) {
		console.log("----------selected callback");
		console.log("user selected bill is : " + JSON.stringify(data));

		self.setCurrCustomerBill(data); //将客户账单保存到对象中.

		//对接收到的数据进行处理
		console.log("对用户的账单数据进行处理,而后根据发票分配算法预生成发票");
		preDistributeBill(data); //预生成发票		
		dispatchFPHM2PreInvoice(); //对预分配发票分配发票号码
		self.dispPreDistInvoiceList(); //展示预分配发票列表.

	}

	//对预生成的发票分配发票号码
	function dispatchFPHM2PreInvoice() {
		//如果库存对象为空时,则提示
		if (self.invoiceInventory != null) {
			if (self.preDistInvoiceList != null) {
				//util.message("开始预分配发票号码!", null, "info");

				var fpdm = self.invoiceInventory.getFPDM(); //发票代码
				var fphm = self.invoiceInventory.getFPHM(); //发票号码,初始号码
				var kcfpsl = Number(self.invoiceInventory.getKCFPSL()); //库存发票数量
				var preInvoiceNum = self.preDistInvoiceList.invoiceList.length;

				//如果预生成的发票数量<库存发票数量时
				if (preInvoiceNum <= kcfpsl) {
					for (var i = 0; i < self.preDistInvoiceList.invoiceList.length; i++) {
						var onePreInvoice = self.preDistInvoiceList.invoiceList[i];
						onePreInvoice.FPDM = fpdm;
						onePreInvoice.FPHM = Number(fphm) + i;
					}
				} else {
					util.message("发票库存数量不足.生成的发票数为:" + preInvoiceNum + ";发票库存数为:" + kcfpsl, null, "warning");
				}


			} else {
				util.message("未生成发票", null, "info");
			}

		} else {
			util.message("没有发票库存,请插入金税盘!", null, "info");
		}

	}

	//预分配发票,形成预分配列表;
	//两种数据格式,水费通知单格式,账单格式.
	function preDistributeBill(billData) {
		const CHOICE_TYPE_NOTIFY = 1; //水费通知单方式
		const CHOICE_TYPE_ACCOUNT = 2; //水费账单方式

		var selectedBill = billData;

		self.preDistInvoiceList = null; //置发票列表为空

		switch (selectedBill.type) {
			case CHOICE_TYPE_NOTIFY:
				processNotifyMode(billData)
				break;
			case CHOICE_TYPE_ACCOUNT:
				processAccountItemMode(billData);
				break;
		}

		//notify mode
		function processNotifyMode(billData) {
			var requestData = new Object();
			requestData.usePrice = billData.usePrice; //1:基础水价 2:污水处理费价 3:综价
			requestData.mergeAccountItem = billData.mergeAccountItem; //true or false; 是否合并账单（按价格）			
			requestData.accountItemList = new Array();

			var mergeNotify = billData.mergeNotify; //合并通知单标志, false:不合并  true:合并 
			//如果不合并时,每个用水通知单分别处理;否则合成一个来处理.
			if (mergeNotify) {
				//合并账单
				var mergedList = new Array();
				for (var i = 0; i < billData.waterNotifyList.length; i++) {
					var waterNotify = billData.waterNotifyList[i];
					for (var j = 0; j < waterNotify.notifyBillList.length; j++) {
						var oneBill = waterNotify.notifyBillList[j];
						mergedList.push(oneBill);
					}
				}
				requestData.accountItemList = mergedList; //合并后账单列表.				

				//向后台发送请求预生成发票(分配算法)
				var distResultStr = requestDistributeInvoice(requestData); //JSON字符串格式
				//此处转换类JS对象
				var distResult = JSON.parse(distResultStr);

				self.preDistInvoiceList = distResult; //保存分配结果到对象中

				console.log("发票分配结果-合并通知" + "  分配结果:" + JSON.stringify(self.preDistInvoiceList));

			} else {
				//不合并,每个通知单分别处理,需要多次请求
				var mergedList = new Array();
				for (var i = 0; i < billData.waterNotifyList.length; i++) {
					var waterNotify = billData.waterNotifyList[i];
					mergedList = waterNotify.notifyBillList;

					requestData.accountItemList = mergedList; //每个水费通知单的账单列表.				

					//向后台发送请求预生成发票(分配算法)
					var distResultStr = requestDistributeInvoice(requestData);
					console.log("发票分配结果-不合并通知-通知索引号" + i + "   " + "分配结果:" + distResultStr);
					var distResult = JSON.parse(distResultStr); //transform from json string to js obj

					//保存分配结果
					if (self.preDistInvoiceList == null) {
						self.preDistInvoiceList = new Object();
						self.preDistInvoiceList.invoiceList = new Array();
					}

					//保存每组分配结果
					for (var j = 0; j < distResult.invoiceList.length; j++) {
						var oneInvoice = distResult.invoiceList[j];
						self.preDistInvoiceList.invoiceList.push(oneInvoice);
					}

				}

				//预分配发票列表显示
				console.log("发票分配结果-不合并通知-发票列表" + JSON.stringify(self.preDistInvoiceList));
			}
			//对发票加入备注,发票代码,发票号码字段初始为空
			initPreDistInvoiceListField();
		}


		//账单模式
		function processAccountItemMode(billData) {
			//生成请求参数
			var requestData = new Object();
			requestData.usePrice = billData.usePrice; //1:基础水价 2:污水处理费价 3:综价
			requestData.mergeAccountItem = billData.mergeAccountItem; //true or false; 是否合并账单（按价格）			
			requestData.accountItemList = new Array();

			requestData.accountItemList = billData.accountItemList;

			//向后台发送请求预生成发票(分配算法)
			var distResultStr = requestDistributeInvoice(requestData);
			console.log("账单模式-分配发票" + "   " + "分配结果:" + distResultStr);
			var distResult = JSON.parse(distResultStr); //transform from json string to js obj

			self.preDistInvoiceList = distResult; //保存分配结果到对象中

			//对发票加入备注,发票代码,发票号码字段初始为空
			initPreDistInvoiceListField();
		}

		//初始化预生成发票字段中的值
		/*
		 *初始化之后的数据结构如下所示:		  
		  {
					"invoiceList": [
						{
							"invoiceAccountItems": [
								{
									"accountItemIds": "10,",
									"invoiceAmount": 50,
									"invoicePrice": 5,
									"invoiceTaxAmount": 1.46,
									"invoiceWaterAmount": 10,
									"taxRate": 0.03
								},								
								{								
								}
							]
							BZ:"",  //以下三个字段为新加入
							FPDM:"",
							FPHM:""
						},
						{
							"invoiceAccountItems": [
								{
									"accountItemIds": "9,",  //账单ID
									"invoiceAmount": 45,	 //金额
									"invoicePrice": 5,		 //价格
									"invoiceTaxAmount": 1.31, //税额
									"invoiceWaterAmount": 9,  //数量
									"taxRate": 0.03           //税率
								},
								{

								}
								
							]
							BZ:"",  //以下三个字段为新加入
							FPDM:"",
							FPHM:""
						}
					],
					"invoiceNum": 2
		}
		 */
		function initPreDistInvoiceListField() {
			if (self.preDistInvoiceList != null) {
				for (var i = 0; i < self.preDistInvoiceList.invoiceList.length; i++) {
					var oneInvoice = self.preDistInvoiceList.invoiceList[i];
					oneInvoice.BZ = ""; //初始化备注字段
					oneInvoice.FPDM = ""; //初始化发票代码
					oneInvoice.FPHM = ""; //初始化发票号码
				}
			}
		}

		//请求发票分配算法,返回结果列表.
		function requestDistributeInvoice(requestData) {
			//POST 请求到后台分配算法,此处采用同步方式来处理
			//返回请求结果并进行处理.
			var url = "/tax/distinvoice";
			var distResult = null; //

			var param = new Object();
			param.accountItemListJson = JSON.stringify(requestData);

			//开启同步处理			
			$.ajaxSettings.async = false; //关闭异步
			$.post(url, param, resultCallBack);
			$.ajaxSettings.async = true //打开异步

			function resultCallBack(resp) {
				//console.log("发票分配结果:"+resp);
				distResult = resp;
			}
			return distResult;
		}
	}

}

//初始化预生成发票操作面板
IssueInvoice.prototype.initPreInvoicePanel = function () {
	var self = this;

	//----------修改备注按钮 click event listener-----------
	const BTN_MODIFY_REMARK = ".pre-invoice-operate-panel .modify-remark"; //修改备注按钮
	const TEXTAREA_PRE_INVOICE_REMARK = ".pre-invoice-remark-input"; //备注输入框选择器

	$(BTN_MODIFY_REMARK).on('click', function () {
		var idPrefix = "pre-distribute-invoice-";
		var index = self.getCurrDistInvoiceIndex();
		if (index >= 0) {
			var invoiceId = idPrefix + index;
			displayToolTip(invoiceId);

		}
	});

	//弹出tooltip,用于修改当前发票备注
	function displayToolTip(preInvoiceId) {

		//获取当前预生成发票:备注
		function getCurrPreInvoiceBZ() {
			var resultVal = "";
			var index = self.getCurrDistInvoiceIndex();
			console.log("当前发票索引号为:" + index);
			if (index >= 0) {
				resultVal = self.preDistInvoiceList.invoiceList[index].BZ;
			}
			return resultVal;
		}

		//获取当前预生成发票:FPHM
		function getCurrPreInvoiceFPHM() {
			var resultVal = "";
			var index = self.getCurrDistInvoiceIndex();
			console.log("当前发票索引号为:" + index);
			if (index >= 0) {
				resultVal = self.preDistInvoiceList.invoiceList[index].FPHM;
			}
			return resultVal;
		}

		//在TOOLTIP中显示当前预生成发票之:发票号码
		function dispCurrPreInvoiceFPHM(fphm) {
			var fphmSelectorInToolTip = ".tooltip-pre-invoice-fphm";
			$(fphmSelectorInToolTip).html(fphm);
		}


		function setEditorValue(remark) {
			$(TEXTAREA_PRE_INVOICE_REMARK).val(remark);
		}


		$('#tooltip').popup({
			type: 'tooltip',
			vertical: 'bottom',
			horizontal: 'center',
			tooltipanchor: $("#" + preInvoiceId),
			closebutton: true,
			offsetleft: 0,
			onopen: function () {
				//alert('Popup just opened!');
				//console.log("popup just opened");	
				//设置编辑器的内容为当前预生成发票的备注
				var remark = getCurrPreInvoiceBZ();
				setEditorValue(remark);

				//显示预分配发票号码
				var fphm = getCurrPreInvoiceFPHM();
				dispCurrPreInvoiceFPHM(fphm);

				//设置备注编辑获取焦点
				setTimeout(function () {
					const PRE_REMARK = TEXTAREA_PRE_INVOICE_REMARK;
					$(PRE_REMARK).focus();
				}, 100)
			},

		});

		$("#tooltip").popup('show');

	}

	//----------确认并打印 click event listener-------------
	//TODO: 确认并打印		
	const MENU_CONFIRM_AND_PRINT = ".confirm-and-print";
	$(MENU_CONFIRM_AND_PRINT).on('click', function () {
		console.log(MENU_CONFIRM_AND_PRINT);
		console.log("保存发票后开始打印...")
		//先保存预生成的发票
		saveInvoiceToBusiness(true); //保存预生成的发票

	});


	//-----------确认并且不打印 click event listener----------
	//TODO: 确认不打印,需要进一步确定保存的状态.稍后重构.
	const MENU_CONFIRM_AND_NOT_PRINT = ".comfirm-and-not-print";
	$(MENU_CONFIRM_AND_NOT_PRINT).on('click', function () {
		console.log(MENU_CONFIRM_AND_NOT_PRINT);

		saveInvoiceToBusiness(false); //保存预生成的发票

	});

	//保存发票到营收系统及金税盘系统中,采用同步方式请求.
	//采用异步方式进行处理.	
	function saveInvoiceToBusiness(printInvoiceFlag) {
		//(1)收集发票信息,包括四个方面的内容:发票信息(发票列表),购方信息,销方信息,金税盘发票样式对象
		//购方信息,销方信息可以采用开票信息对象		
		//参见TaxController中    /issueinvoice/saveinvoice
		const url = BASE_CONTEXT_PATH + "/tax/issueinvoice/saveinvoice"; //请求地址


		//准备请求参数.		
		//转换成JSON格式而后向后台传递参数
		// @param salerIssueInvoiceInfoJSON  销方开票信息
		// @param buyerIssueInvoiceInfoJSON  购方开票信息
		// @param preInvoiceListJSON         预开发票列表		
		//  invoicePatternJSON				 发票样版(金税盘部分)
		var reqParam = new Object();
		reqParam.salerIssueInvoiceInfoJSON = JSON.stringify(self.getSalerIssueInvoiceInfo());
		reqParam.buyerIssueInvoiceInfoJSON = JSON.stringify(self.getBuyerIssueInvoiceInfo());
		reqParam.preInvoiceListJSON = JSON.stringify(self.preDistInvoiceList);

		//生成一个发票对象(JS)
		var invoicePattern = new Invoice();
		var invoiceHead = invoicePattern.getInvoiceHead();
		invoiceHead.setFPZL(InvoiceType.FPZL_SPECIAL_INVOICE); //此处设置发票种类:专用发票
		var invoiceDetail = new InvoiceDetail();
		invoicePattern.addInvoiceDetail(invoiceDetail);

		reqParam.invoicePatternJSON = JSON.stringify(invoicePattern); //转换模板为JSON


		var respJSON = "";

		//采用同步方式调用  $.ajaxSettings.async = false;  $.ajaxSettings.async = true;
		//采用异步方式请求
		//TODO:  采用异步方式处理.

		self.showWaitingDialog("正在开具发票...",null);  //显示模式对话框
		$.post(url, reqParam, saveInvoiceCallback);  //发送请求(保存发票--->DB,保存发票--->金税盘)

		//console.log("保存发票请求结束." + respJSON);	
		//保存发票回调函数.
		function saveInvoiceCallback(resp) {

			self.hideWaitingDailog();  //关闭等待对话框

			respJSON = resp;
			console.log(JSON.stringify(resp));
			
			if (respJSON != null && respJSON != "") {
				//var obj = $.parseJSON(res);						
				if (respJSON.result_code == "success") {  //发票开具成功
					
					//util.message(respJSON.result_msg, resetIssue, "success");
					console.log("开具发票成功!");

					if(!printInvoiceFlag){  //如果不打印时
						self.showPopupDialog(respJSON.result_msg, resetIssue);					
					}  
					else{  //保存后打印发票						
						self.showPopupDialog(respJSON.result_msg+",开始准备打印发票", startPrint); //显示打印对话框.
					}
					
				} else {  //开具失败
					//dialog type: success warning info error,默认为info
					console.log("开具发票失败!");
					//util.message(respJSON.result_msg, null, "warning");
					self.showPopupDialog("开具发票失败,请查询是否已经插入金税盘或是已经打开客户端.", null);
					return;					
				}
			}
	

			//复位开具发票
			function resetIssue() {
				self.resetIssueInvoice(); 
			}

			//显示打印对话框
			//TODO: 开始打印-显示打印对话框			
			function startPrint() {
				
				var invoiceList=new Array();
				for(var i=0;i<self.preDistInvoiceList.invoiceList.length;i++){
					var invoice=self.preDistInvoiceList.invoiceList[i];
					var invoicePrint=new InvoicePrint();
					invoicePrint.setFPZL(InvoiceType.FPZL_SPECIAL_INVOICE);
					invoicePrint.setFPDM(invoice.FPDM);
					invoicePrint.setFPHM(invoice.FPHM);
					invoiceList.push(invoicePrint);
				}

				console.log("开始打印..................设置打印按钮的状态");
				self.printDialog.setInvoiceList(invoiceList);  //设置打印对话框中需打印的发票列表.				                 
				self.printDialog.setInitButtonStatus();
				self.printDialog.show();
			}
			
		}		

	}

	//-----------取消本次预生成发票 click event listener----------
	const MENU_CANCEL_PRE_INVOICE = ".cancel-pre-invoice";
	$(MENU_CANCEL_PRE_INVOICE).on('click', function () {
		console.log(MENU_CANCEL_PRE_INVOICE);

		//TODO: 保存或打印后,需要使用此函数
		self.resetIssueInvoice(); //(1)复位发票开具

	});

	


	//---------清除备注按钮--------
	const BTN_CLEAR_PRE_INVOICE_REMARK = ".clear-pre-invoice-remark";
	$(BTN_CLEAR_PRE_INVOICE_REMARK).on('click', function () {
		$(TEXTAREA_PRE_INVOICE_REMARK).val(""); //备注输入框
		//查询相应的预生成发票,并置备注项为空
		var currIndex = self.getCurrDistInvoiceIndex();
		if (currIndex >= 0) {
			self.preDistInvoiceList.invoiceList[currIndex].BZ = "";
		}

	});
	
	//获取并加入上期备注  2019/12/26  
	const BTN_GET_PRE_INVOICE_REMARK = ".get-pre-invoice-remark";
	$(BTN_GET_PRE_INVOICE_REMARK).on('click', function () {
		const url = BASE_CONTEXT_PATH + "/tax-invoice/getPreRemark";   //请求上一次的备注信息
		
		//准备请求参数
		var param=new Object();
		param.customerId=self.buyerInvoiceInfo.customerId;  //客户ID
		$.post(url,param,getRemarkCallback);
		
		function getRemarkCallback(resp){
			console.log("获取到的上次备注信息:" + JSON.stringify(resp));
			if (resp != null && resp != "") {
				//var obj = $.parseJSON(res);						
				if (resp.result_code == "success") {
					//发票开具成功
					//util.message(respJSON.result_msg, null, "success");
					var remark=respJSON.result_msg;
					$(TEXTAREA_PRE_INVOICE_REMARK).val(remark); //备注输入框
					//查询相应的预生成发票,并置备注项为上次备注
					var currIndex = self.getCurrDistInvoiceIndex();
					if (currIndex >= 0) {
						self.preDistInvoiceList.invoiceList[currIndex].BZ = remark;
					}
					
				} else {
					//dialog type: success warning info error,默认为info
					util.message(respJSON.result_msg, null, "warning");
					return;
				}
			}
		}

	});

	//---------当备注内容变化时写入相应的预生成发票中-----onchange-----
	$(TEXTAREA_PRE_INVOICE_REMARK).on("input propertychange", function () {
		var remark = $(this).val();
		console.log('editing remark is:' + remark);
		var currIndex = self.getCurrDistInvoiceIndex();
		if (currIndex >= 0) {
			self.preDistInvoiceList.invoiceList[currIndex].BZ = remark;
		}

	});

}

//----------------------复位开具发票---------------------
//复位开具发票	
IssueInvoice.prototype.resetIssueInvoice=function() {
	var self=this;

	var invoiceObj = self.invoiceSpecial; //当前发票对象(UI+DATA)

	//(1)获取发票相关数据对象
	//(1.1)发票头对象
	var invoiceHead = invoiceObj.getInvoiceHead();
	invoiceHead.reset();

	//(1.2)发票附加对象
	var invoiceAddition = invoiceObj.getInvoiceAddition();
	invoiceAddition.reset();

	//(1.3)发票列表对象,清除当前发票对象中所有发票详情.
	invoiceObj.removeAllInvoiceDetail(true);

	//(1.4)预生成发票列表
	self.preDistInvoiceList = null;

	//(1.5)待开票用户账单
	//this.currCustomerBill = null; //added by hz  on 2019/12/03		
	self.setCurrCustomerBill(null);

	//(1.6)发票库存对象		
	self.setInvoiceInventory(null); //发票库存对象

	//(1.7)客户开票信息复位		
	self.buyerIssueInvoiceInfo.reset();

	//(1.8)预生成发票当前索引
	self.setCurrDistInvoiceIndex(-1); //当前选定的预生成发票索引号

	//(2)显示发票数据对象
	invoiceObj.displayInvoice();

	//(3)隐藏预分配发票
	self.hidePreInvoiceList();
	self.hidePreInvoiceOperatePanel();

	//(4)发票号码确认,重新开始流程.
	self.queryInventory(InvoiceType.FPZL_SPECIAL_INVOICE);  //查询专用发票号码.

}

//-----------------------显示及隐藏等待对话框----------------------
/**
 * 显示等待对话框,
 * message:显示的消息
 * closeCallback:关闭后的回调函数.
 */
IssueInvoice.prototype.showWaitingDialog=function(message,closeCallback){
	var waitingDialog = "#waiting-dialog";
	//waiting-dialog-message
	var msgSelector="#waiting-dialog .waiting-dialog-message";			
			
			$(waitingDialog).popup({
				closebutton: false,
				blur:false,  //点击区域外时,不要关闭.
				onclose: function () {
					if(closeCallback!=null){
						closeCallback();
					}			
				}
			});

			$(msgSelector).html(message);

			$(waitingDialog).popup('show');	
}
//close waiting dialog
IssueInvoice.prototype.hideWaitingDailog=function(){
	var waitingDialog = "#waiting-dialog";
	$(waitingDialog).popup('hide');	
}

//----------------------显示信息对话框----------------------
//显示信息提示框.
IssueInvoice.prototype.showPopupDialog = function (message, closeCallback) {
	var tooltipDialog = "#tooltip-dialog";
	var msg = "#tooltip-dialog .tooltip-dialog-message";

	console.log("要显示的信息是:"+message);
	$(msg).html(message);

	$(tooltipDialog).popup({
		closebutton: true,
		onclose: function () {
			if(closeCallback!=null){
				closeCallback();
			}			
		}
	});

	$(tooltipDialog).popup('show');

}

IssueInvoice.prototype.setInvoiceInventory = function (invoiceInventory) {
	this.invoiceInventory = invoiceInventory;
}

//初始发票开具页面
IssueInvoice.prototype.initIssueInvoiceUI = function () {
	//隐藏面板及解析后的发票列表.
	this.hidePreInvoiceList();
	this.hidePreInvoiceOperatePanel();
}

//---------------预生成发票当前索引--  getter and setter--------------
IssueInvoice.prototype.setCurrDistInvoiceIndex = function (currDistInvoiceIndex) {
	this.mCurrDistInvoiceIndex = currDistInvoiceIndex;
}
IssueInvoice.prototype.getCurrDistInvoiceIndex = function () {
	return this.mCurrDistInvoiceIndex;
}



//dispPreDistInvoiceList
//显示预分配发票.(预生成发票列表面板)
IssueInvoice.prototype.dispPreDistInvoiceList = function () {
	var self = this;
	//self.preDistInvoiceList;

	//隐藏面板及解析后的发票列表.
	self.hidePreInvoiceList();
	self.hidePreInvoiceOperatePanel();

	var listSelector = ".issue-invoice-panel .pre-invoice-list h5";
	var list = $(listSelector).empty();

	var idPrefix = "pre-distribute-invoice-";
	if (self.preDistInvoiceList != null) {
		var firstInvoice = null;
		for (var i = 0; i < self.preDistInvoiceList.invoiceList.length; i++) {
			var onePreInvoice = $("<span class='badge badge-pill badge-info' style='margin-left:5px;'  data-bind-index=" + i +
				" id='" + idPrefix + i + "'" +
				" ></span>");
			//onePreInvoice.html("<i class='fa fa-film' aria-hidden='true'></i>&nbsp;"+i+"&nbsp;");
			var invoiceSerialNo=i+1;
			onePreInvoice.html("&nbsp;" + invoiceSerialNo + "&nbsp;");
			list.append(onePreInvoice);

			//当某张发票被点击时.
			onePreInvoice.on('click', onePreInvoiceClick);

			if (i == 0) {
				firstInvoice = onePreInvoice;
			}

		}

		if (self.preDistInvoiceList.invoiceList.length > 0) {
			self.showPreInvoiceList();
			self.showPreInvoiceOperatePanel();

			//firstInvoice.trigger('click'); //默认选择第一张发票(预生成发票)
			//选择第一张发票
			firstInvoice.removeClass("badge-info");
			firstInvoice.addClass("badge-success");
			self.displayPreDistInvoice(0); //显示第一张发票的内容
			//self.mCurrDistInvoiceIndex			
			self.setCurrDistInvoiceIndex(0); //设置预生成发票当前索引号


		} else {
			util.message("未生成发票!", null, "info");
			self.setCurrDistInvoiceIndex(-1);
		}
	}

	//点击某张预分配的发票时,显示此发票信息.	
	function onePreInvoiceClick() {
		var index = $(this).attr("data-bind-index");
		var invoiceSelector = ".issue-invoice-panel .pre-invoice-list h5 span";

		$(invoiceSelector).removeClass("badge-success");
		$(invoiceSelector).addClass("badge-info");
		$(this).removeClass("badge-info");
		$(this).addClass("badge-success");

		//var preInvoiceId=$(this).attr("id");
		//displayToolTip(preInvoiceId);

		console.log("pre invoice clicked! index is:" + index);

		self.displayPreDistInvoice(index); //显示当前发票(UI+DATA)

		self.setCurrDistInvoiceIndex(index); //设置预生成发票当前索引号
	}
}

//------------------显示一张预分配的发票-------------------

/**
 * 功能:显示一张预分配的发票
 * 参数:发票索引号 
 */
IssueInvoice.prototype.displayPreDistInvoice = function (index) {
	var self=this;
	/*
		 预生成发票对象格式如下:
		 其中的数据主要为发票详情列表,税额及金额等已经计算完毕,不必重新计算(以避免计算时误差),只显示即可.
		 是否附加备注对象?稍后处理.
		{
					"invoiceList": [
						{
							"invoiceAccountItems": [
								{
									"accountItemIds": "10,",
									"invoiceAmount": 50,
									"invoicePrice": 5,
									"invoiceTaxAmount": 1.46,
									"invoiceWaterAmount": 10,
									"taxRate": 0.03
								},								
								{								
								}
							]
							BZ:"",
							FPDM:"",
							FPHM:"",
						},
						{
							"invoiceAccountItems": [
								{
									"accountItemIds": "9,",  //账单ID
									"invoiceAmount": 45,	 //金额
									"invoicePrice": 5,		 //价格
									"invoiceTaxAmount": 1.31, //税额
									"invoiceWaterAmount": 9,  //数量
									"taxRate": 0.03           //税率
								},
								{

								}
								
							]
							BZ:"",
							FPDM:"",
							FPHM:"",
						}
					],
					"invoiceNum": 2
		}
	 */
	var onePreInvoice = self.preDistInvoiceList.invoiceList[index]; //当前预分配发票(data)
	var invoiceObj = self.invoiceSpecial; //当前发票对象(UI+DATA)

	//(1)获取发票相关数据对象
	var invoiceHead = invoiceObj.getInvoiceHead(); //(1.1)发票头对象
	var invoiceAddition = invoiceObj.getInvoiceAddition(); //(1.2)发票附加对象
	invoiceObj.removeAllInvoiceDetail(true); //(1.3)发票列表对象,清除当前发票对象中所有发票详情.


	//(2)设置发票数据对象的值,并赋值回invoiceObj对象中
	//(2.1)发票头对象,除备注字段外,其它的字段均已经在前面的过程中赋值	
	invoiceHead.setBZ(onePreInvoice.BZ);
	invoiceObj.setInvoiceHead(invoiceHead); //(UI+DATA)

	//(2.2)发票详情列表对象
	var oneInvoiceAccountItemList = onePreInvoice.invoiceAccountItems;
	for (var i = 0; i < oneInvoiceAccountItemList.length; i++) {
		var accountItem = oneInvoiceAccountItemList[i];

		var invoiceDetail = new InvoiceDetail();

		invoiceDetail.setSL(accountItem.invoiceWaterAmount); //数量
		invoiceDetail.setDJ(accountItem.invoicePrice); //单价		
		invoiceDetail.setJE(accountItem.invoiceAmount); //金额
		invoiceDetail.setSE(accountItem.invoiceTaxAmount); //税额
		invoiceDetail.setSLV(accountItem.taxRate); //税率
		//其它字段均有默认值,不必再进行赋值

		invoiceObj.addInvoiceDetail(invoiceDetail); //加入到发票对象中(UI+DATA)
	}

	//(2.3)发票附加对象.
	//set invoiceAddition props;
	invoiceAddition.setInvoiceDM(onePreInvoice.FPDM);
	invoiceAddition.setInvoiceHM(onePreInvoice.FPHM);
	invoiceAddition.setIssueInvoiceDate(new Date());

	self.invoiceSpecial.setInvoiceAddition(invoiceAddition);

	//(2.4)
	invoiceObj.calcInvoiceSum(); //计算合计及转换成中文合计


	//(3)显示发票数据对象
	invoiceObj.displayInvoice();

}

//-------------------属性getter and setter-----------------

// 客户账单  getter and setter
/** 
 * customerBill: 格式如下所示:
  	{
		"isMerge": false,
		"selectedBillArr": [{
			"billId": "15073",
			"customerId": "57234",
			"period": "2019-10",
			"waterUse": "园林用水",
			"basePrice": "7.54",
			"sewagePrice": "0",
			"totalPrice": "7.54",
			"waterAmount": "200",
			"baseWaterFee": "0",
			"sewageWaterFee": "0",
			"totalWaterFee": "1508"
		}, 
		{}]		
	}

	当用户未选择数据时,返回的数据格式如下所示:
	{
		"isMerge": false,
		"selectedBillArr": []		
	}
 */
IssueInvoice.prototype.setCurrCustomerBill = function (customerBill) {
	this.currCustomerBill = customerBill;
}

IssueInvoice.prototype.getCurrCustomerBill = function () {
	return this.currCustomerBill;
}


//---------------发票开具界面显示及隐藏------------------

/*
	隐藏发票开具界面
 */
IssueInvoice.prototype.hideIssueInvoice = function () {
	const INVOICE_CONTAINER = "#invoice-special-container"; //发票组件容器
	const ISSUE_INVOICE_PANEL = ".issue-invoice-panel"; //发票开具操作面板
	//const ISSUE_INVOICE_TEST_PANEL = ".issue-invoice-test-panel"; //发票开具测试面板		

	$(INVOICE_CONTAINER).hide();
	$(ISSUE_INVOICE_PANEL).hide();
	//$(ISSUE_INVOICE_TEST_PANEL).hide();

	console.log("隐藏开票界面.");
}

/*
	显示发票开具界面
 */
IssueInvoice.prototype.showIssueInvoice = function () {
	const INVOICE_CONTAINER = "#invoice-special-container"; //发票组件容器
	const ISSUE_INVOICE_PANEL = ".issue-invoice-panel"; //发票开具操作面板
	//const ISSUE_INVOICE_TEST_PANEL = ".issue-invoice-test-panel"; //发票开具测试面板		

	$(INVOICE_CONTAINER).show();
	$(ISSUE_INVOICE_PANEL).show();
	//$(ISSUE_INVOICE_TEST_PANEL).show();

	console.log("显示开票界面.");
}

/*
	初始发票号码确认对话框
	设置对话框中按钮的回调函数
 */
IssueInvoice.prototype.initInvoiceHMConfirmDialog = function () {
	var self = this;

	//设置发票号码确认对话框中确认按钮及取消按钮的回调函数
	this.invoiceHMConfirmDialog.setButtonOkCallback(buttonOkCallback);
	this.invoiceHMConfirmDialog.setButtonCancelCallback(buttonCancelCallback);

	function buttonOkCallback() {
		console
			.log("invoicehm confirm dialog- button ok callback function is called!");
		self.showIssueInvoice(); //显示发票开具界面
	}

	function buttonCancelCallback() {
		console
			.log("invoicehm confirm dialog-button cancel callback function is called!");
		self.hideIssueInvoice(); //隐藏发票开具界面
	}
}

/**
 * 初始化发票打印对话框
 * 设置对话框中按钮的回调函数
 * added by hz   in  2019/11/27
 * TODO: 
 */
IssueInvoice.prototype.initPrintDialog = function () {
	var self = this;
	//self.printDialog.setButtonPreviewCallback(buttonPrintPreviewCallback);
	self.printDialog.setButtonPrintYesCallback(buttonPrintYesCallback);
	self.printDialog.setButtonPrintNoCallback(buttonPrintNoCallback);
	self.printDialog.setButtonPrintCloseCallback(buttonPrintCloseCallback);  //设置打印对话框关闭按钮回调函数

	

	//打印发票(可能多张)-button click event listener
	function buttonPrintYesCallback() {
		console.log("button printYes callback function is called!");
		var topOffset = self.printDialog.getTopOffset();
		var leftOffset = self.printDialog.getLeftOffset();

		console.log("top offset is:" + topOffset);
		console.log("left offset is" + leftOffset);

		//(1)此处需要向web service发送调整打印参数请求. 
		//TODO: 稍后处理. 
		//(1.1)打印参数调整  稍后处理,暂时不做请求处理,如果需要可以加入.

		//(2)自发票对象中获取发送开具发票请求.待请求正确返回时,方可打印发票.
		//printInvoice();

	}



	//TODO: 打印发票	OLD_CODE
	function printInvoice() {
		console.log("打印当前发票");

		const url = BASE_CONTEXT_PATH + "/tax/printinvoice/print"; //请求打印发票

		//TODO: 打印发票

		var invoiceList = self.preDistInvoiceList.invoiceList;
		for (var i = 0; i < invoiceList.length; i++) {
			invoice = invoiceList[i];

			var invoicePrint = new InvoicePrint();
			invoicePrint.setFPZL(InvoiceType.FPZL_SPECIAL_INVOICE);
			invoicePrint.setFPDM(invoice.FPDM);
			invoicePrint.setFPHM(invoice.FPHM);  

			//打印请求参数
			var params = new Array();
			params.push(invoicePrint); //invoicePrint---放置到--->Array

			//准备请求参数.		
			var paramsStr = JSON.stringify(params); //转换成JSON格式而后向后台传递参数
			var reqParm = new Object(); //请求参数对象
			reqParm.invoiceListJSON = paramsStr; //参见TaxController中    /printinvoice/print

			//发送打印请求
			$.post(url, reqParm, printInvoiceCallback);

		}


		//发票打印回调函数		
		function printInvoiceCallback(resp) {
			console.log("调用:发票打印请求后回调函数" + resp);
		}

	}

	//不打印
	function buttonPrintNoCallback() {
		console.log("button printNo callback function is called!");
		self.resetIssueInvoice(); //开具发票复位
	}

	//TODO:  打印完毕(发票已经打印完毕)
	function buttonPrintCloseCallback(){
		console.log("button printNo callback function is called!");
		self.resetIssueInvoice(); //开具发票复位

	}

	/**
	 * 功能:
	 * 	获取需要打印的当前发票
	 * 返回:
	 * 	需打印发票对象  InvoicePrint 类型,见InvoicePrint.js对象
	 */
	/* function getPrintInvoice() {
		var invoiceHead = self.invoiceSpecial.getInvoiceHead(); //读取发票头
		var invoiceAddition = self.invoiceSpecial.getInvoiceAddition(); //读取附加信息

		var invoicePrint = new InvoicePrint();
		invoicePrint.FPZL = invoiceHead.getFPZL(); //发票种类,必填字段
		invoicePrint.FPHM = invoiceAddition.getInvoiceHM(); //发票号码,必填字段 
		invoicePrint.FPDM = invoiceAddition.getInvoiceDM(); //发票代码,必填字段

		return invoicePrint;
	} */

}

/*
	显示打印对话框
	此函数暂时未用到
 */
IssueInvoice.prototype.showPrintDialog = function () {
	this.printDialog.show();
}

/*
	功能:查询发票库存
	invoiceType:发票类型,参见InvoiceType.js
 */
IssueInvoice.prototype.queryInventory = function (invoiceType) {
	self = this;

	const url = BASE_CONTEXT_PATH + "/tax/inventory/query"; //请求地址

	//发票类型列表
	var invoiceTypeList = new Array();
	invoiceTypeList.push(invoiceType);

	var param = new Object();
	param.invoiceTypeList = invoiceTypeList;

	//TODO: 查询发票时:显示一个等待对话框.	
	self.showWaitingDialog("正在查询发票库存.",null);
	$.post(url, param, querySpecialCallback); //采用load动态加载查询结果
	

	/*
		查询库存成功-专用发票-回调函数
	 */
	function querySpecialCallback(resp) {
		var invoiceTypeName = InvoiceType.getInvoiceName(invoiceType); //根据发票类型获取发票名称
		console.log("返回的库存数据是:" + invoiceTypeName + "------" + resp);

		self.hideWaitingDailog();  //关闭等待对话框

		if (resp != "") {
			console.log("返回库存发票数据");
			//将JSON转换为JS数组对象.此处必须将resp转换成JS对象.
			var inventoryArr = JSON.parse(resp);
			if (inventoryArr.length > 0) {

				var inventory = inventoryArr[0];


				console.log("发票库存对象:" + JSON.stringify(inventory));
				console.log("发票代码" + inventory.FPDM);
				console.log("发票号码" + inventory.FPHM);

				var fpdm = inventory.FPDM;
				var fphm = inventory.FPHM;

				self.invoiceHMConfirmDialog.setInvoiceType(invoiceTypeName); //发票名称
				self.invoiceHMConfirmDialog.setInvoiceDM(fpdm);
				self.invoiceHMConfirmDialog.setInvoiceHM(fphm);

				//设置待开具发票中必要的信息字段
				self.setCurrentInvoiceDM(fpdm); //设置当前发票的代码
				self.setCurrentInvoiceHM(fphm); //设置当前发票的号码				
				//开票日期		
				var issueDate = dateFtt("yyyy-MM-dd", new Date());
				console.log("当前开票日期为:" + issueDate);
				self.setIssueInvoiceDate(issueDate); //设置当前开票日期

				self.setSalerInfo(); //设置销方信息						
				self.setOperaterInfo(); //设置操作员信息

				saveInvoiceInventory(inventory); //保存库存对象  added by hz at 2019/12/15

				//显示发票号码确认对话框
				self.invoiceHMConfirmDialog.show();

			} else {
				console.log("未获取到库存数据!");				
				//util.message("未获取到库存数据!",null,"info");			
			}

		} else {
			console.log("未获取到库存数据!");				
			//util.message("查询库存失败,请联系系统管理员", null, "info");

			self.showPopupDialog("查询发票库存失败,请查询是否已经插入金税盘或是已经打开客户端.", null);
		}
	}


	/*
	功能:设置库存对象的值
	参数格式如下所示:  参数为JS对象.
	{
    "RETCODE": "3011",
    "RETMSG": "3011-金税盘状态信息读取成功 [TCD_0_15,]",
    "FPDM": "1300141140",
    "FPHM": "14907748",
    "KCFPSL": "1000",
    "JSSBRQ": "2019-10-21 11:55:23",
    "XFSH": "130100999999220",
    "SCFS": "1",
    "KPDH": "1",
    "CSQBZ": "0",
    "SSQBZ": "0",
    "KPFS": "0",
    "KPFWQH": "",
    "JSPH": "661555297643",
    "XFMC": "航信培训企业"
	}
	*/
	function saveInvoiceInventory(inventory) {
		if (self.invoiceInventory == null) {
			self.invoiceInventory = new InvoiceInventory();
		}

		self.invoiceInventory.setRETCODE(inventory.RETCODE);
		self.invoiceInventory.setRETMSG(inventory.RETMSG);
		self.invoiceInventory.setFPDM(inventory.FPDM);
		self.invoiceInventory.setFPHM(inventory.FPHM);
		self.invoiceInventory.setKCFPSL(inventory.KCFPSL);
		self.invoiceInventory.setJSSBRQ(inventory.JSSBRQ);
		self.invoiceInventory.setXFSH(inventory.XFSH);
		self.invoiceInventory.setSCFS(inventory.SCFS);
		self.invoiceInventory.setKPDH(inventory.KPDH);
		self.invoiceInventory.setCSQBZ(inventory.CSQBZ);
		self.invoiceInventory.setSSQBZ(inventory.SSQBZ);
		self.invoiceInventory.setKPFS(inventory.KPFS);
		self.invoiceInventory.setKPFWQH(inventory.KPFWQH);
		self.invoiceInventory.setJSPH(inventory.JSPH);
		self.invoiceInventory.setXFMC(inventory.XFMC);

		console.log("发票库存保存成功.");

	}

}

//-------------初始化需待开的发票--------------
//	发票号码 发票代码	开票日期	售方信息
/*
	功能:设置当前发票号码并显示
	参数:fphm 发票号码
 */
IssueInvoice.prototype.setCurrentInvoiceHM = function (fphm) {
	//发票号码-属于附加信息		
	var invoiceAddition = this.invoiceSpecial.getInvoiceAddition();
	invoiceAddition.setInvoiceHM(fphm);

	//设置发票附加信息并显示
	this.invoiceSpecial.setInvoiceAddition(invoiceAddition);
	this.invoiceSpecial.displayInvoiceAddition();
}

/*
	功能:设置当前发票代码并显示
	参数:fpdm 发票代码
 */
IssueInvoice.prototype.setCurrentInvoiceDM = function (fpdm) {
	//发票号码-属于附加信息		
	var invoiceAddition = this.invoiceSpecial.getInvoiceAddition();
	invoiceAddition.setInvoiceDM(fpdm);

	//设置发票附加信息并显示
	this.invoiceSpecial.setInvoiceAddition(invoiceAddition);
	this.invoiceSpecial.displayInvoiceAddition();
}

/*
	功能:设置当前开票日期
	参数:	开票日期  格式  yyyy-MM-dd
 */
IssueInvoice.prototype.setIssueInvoiceDate = function (issueDate) {
	//发票号码-属于附加信息		
	var invoiceAddition = this.invoiceSpecial.getInvoiceAddition();
	invoiceAddition.setIssueInvoiceDate(issueDate);

	//设置发票附加信息并显示
	this.invoiceSpecial.setInvoiceAddition(invoiceAddition);
	this.invoiceSpecial.displayInvoiceAddition();
}

//mapList.push({ className: 'saler-bank-account-no', fieldValue: self.invoiceHead.getXFYHZH() });
//mapList.push({ className: 'saler-address-tel', fieldValue: self.invoiceHead.getXFDZDH() });

/*
	功能:
		设置销方信息,4个方面的信息(销方名称  销方税号  销方地址电话  销方银行账号)
		其中:   地址电话 银行账号  	必填信息项
			   销方名称  销方税号	可填信息项			
	参数:
		//TODO (1)后续采用参数设置,此处暂时采用常量设置
		//(2)销方信息可以保存到数据库的数据字典中.(现保存在数据库中)

 */
IssueInvoice.prototype.setSalerInfo = function () {

	var salerInfo = this.getSalerIssueInvoiceInfo();

	//判定开票信息对象是否为空.
	if (salerInfo == null) {
		console.log("销方开票信息为空!");
		return;
	}

	/* //以下两个字段属于发票头信息  test code
	const SALER_BANK_ACCOUNT_NO="销方银行账号:123";
	const SALER_ADDRESS_TEL="销方地址电话:黄河道,0311";

	//以下两个字段属于发票附加信息  test code
	const SALER_NAME="销方名称:石家庄高新区供水排水公司";
	const SALER_TAX_NO="高新水司税号:1130"; */

	/* 	
				private String shortCode;  //暂未使用
			    private String companyName;  //公司名称
			    private String regAddress;	//地址
			    private String telephone;	//电话
			    private String accountBank;	//银行名称		
			    private String accountNo;	//账号		
			    private String taxNo;		//税号		
			    private Integer enabled;	//是否有效. */

	//以下两个字段属于发票头信息
	const SALER_BANK_ACCOUNT_NO = salerInfo.getBank() + " " +
		salerInfo.getAccountNo();
	const SALER_ADDRESS_TEL = salerInfo.getAddress() + " " +
		salerInfo.getTel();

	//以下两个字段属于发票附加信息
	const SALER_NAME = salerInfo.getName();
	const SALER_TAX_NO = salerInfo.getTaxNo();

	//发票号码-属于附加信息		
	var invoiceAddition = this.invoiceSpecial.getInvoiceAddition();
	invoiceAddition.setSalerName(SALER_NAME); //销方名称
	invoiceAddition.setSalerTaxNo(SALER_TAX_NO); //销方税号
	//设置发票附加信息并显示
	this.invoiceSpecial.setInvoiceAddition(invoiceAddition);
	this.invoiceSpecial.displayInvoiceAddition();

	//获取发票头部对象并设置其中的值
	var invoiceHead = this.invoiceSpecial.getInvoiceHead();
	invoiceHead.setXFYHZH(SALER_BANK_ACCOUNT_NO); //销方银行及账号
	invoiceHead.setXFDZDH(SALER_ADDRESS_TEL); //销方地址及电话

	//将值set至发票中并显示
	this.invoiceSpecial.setInvoiceHead(invoiceHead);
	this.invoiceSpecial.displayInvoiceHead();

}

/*
	功能:
		设置发票购方信息,包括:购方名称,购方税号,购方地址电话,购方银行账号
	重构:
		(1)采用参数传递数据
		(2)且数据来自于DB,由用户来选择.			
	参数名称:
		购方开票信息
		buyerInfo的形式如下所示:
		{
			name:"",   	//名称
			taxNo:"",	//税号
			address:"", //地址
			tel:"",	  	//电话
			bank:"",	//银行
			accountNo:"" //账号
		}	
 */
IssueInvoice.prototype.setInvoiceBuyerInfo = function (buyerInfo) {
	var invoiceHead = this.invoiceSpecial.getInvoiceHead();

	invoiceHead.setGFMC(buyerInfo.name); //购方名称
	invoiceHead.setGFSH(buyerInfo.taxNo); //购方税号

	var gfdzdh = this.getStr(buyerInfo.address) + " " + this.getStr(buyerInfo.tel);
	invoiceHead.setGFDZDH(gfdzdh); //购方地址电话
	var gfyhzh = this.getStr(buyerInfo.bank) + " " + this.getStr(buyerInfo.accountNo);
	invoiceHead.setGFYHZH(gfyhzh); //购方银行账号

	//将值set至发票中并显示
	this.invoiceSpecial.setInvoiceHead(invoiceHead);
	this.invoiceSpecial.displayInvoiceHead();

}

//工具函数:判定对象是否已经定义或是为空,如果是则返回""字符串
IssueInvoice.prototype.getStr = function (data) {
	if (!data) {
		return '';
	} else if (typeof (data) == "undefined") {
		return '';
	}

	return data.toString();
}

//成员变量 getter and setter
IssueInvoice.prototype.getSalerIssueInvoiceInfo = function () {
	return this.salerIssueInvoiceInfo;
}
IssueInvoice.prototype.getBuyerIssueInvoiceInfo = function () {
	return this.buyerIssueInvoiceInfo;
}

/*
	功能:设置操作员信息,包括:开票人,审核人,收款人	
 */
IssueInvoice.prototype.setOperaterInfo = function () {
	//(1)获取发票头对象
	var invoiceHead = this.invoiceSpecial.getInvoiceHead();
	//(2)设置开票人,复核人,收款人
	var info = this.getSalerIssueInvoiceInfo();
	invoiceHead.setKPR(info.getOperator()); //开票人
	invoiceHead.setFHR(info.getChecker()); //复核人  //modified by hz in 2019/12/02
	invoiceHead.setSKR(info.getPayee()); //收款人  此字段为必填字段.

	//(3)将值set至发票中并显示
	//this.invoiceSpecial.setInvoiceHead(invoiceHead);
	this.invoiceSpecial.displayInvoiceHead();
}

/*
	功能:初始化监听器 操作面板部分
	参数:无
 */
IssueInvoice.prototype.initListener = function () {
	var self = this; //reference instance Object of QueryInventory ;

	//操作面板按钮部分.	
	const BTN_INVOICE_CUSTOMER = "#btn-invoice-customer"; //按钮:查询客户开票信息 //added by hz 2019/11/26
	const BTN_PRINT_INVOICE = "#btn-print-invoice"; //按钮:打印发票		

	const BTN_ADD_ROW = "#btn-add-row"; //增加行按钮
	const BTN_DEL_ROW = "#btn-del-row"; //删除行按钮

	$(BTN_ADD_ROW).on('click', addRow); //增加详情行
	$(BTN_DEL_ROW).on('click', delRow); //删除详情行

	$(BTN_INVOICE_CUSTOMER).on('click', openCustomerInvoiceDialog); //选择购方客户

	$(BTN_PRINT_INVOICE).on('click', printInvoice); //打印当前发票


	//test button
	const BTN_ISSUE_INVOICE = "#btn-issue-invoice"; //按钮:开具发票
	const BTN_SET_INVOICE_HEAD = "#btn-set-invoice-head"; //disp发票头
	const BTN_SET_INVOICE_ADDITION = "#btn-set-invoice-addition"; //disp发票附加
	const BTN_SET_INVOICE_DETAIL = "#btn-set-invoice-detail"; //disp发票详情条目
	const BTN_DISP_INVOICE = "#btn-disp-invoice"; //显示发票
	const BTN_OPEN_CUSTOMER_DIALOG = "#btn-open-customer-dialog"; //打开客户发票信息选择对话框

	$(BTN_ISSUE_INVOICE).on('click', issueInvoice); //开具发票			

	$(BTN_SET_INVOICE_HEAD).on('click', dispInvoiceHead); //设置发票头
	$(BTN_SET_INVOICE_ADDITION).on('click', dispInvoiceAddition); //发票附加
	$(BTN_SET_INVOICE_DETAIL).on('click', dispInvoiceDetail); //设置发票详情条目
	$(BTN_DISP_INVOICE).on('click', dispInvoice); //显示发票

	$(BTN_OPEN_CUSTOMER_DIALOG).on('click', openCustomerInvoiceDialog); //打开选择客户发票信息对话框


	/*
		增加发票明细行
	 */
	function addRow() {
		self.invoiceSpecial.addRow();
	}

	/*
		删除发票明细行(当前行)
	 */
	function delRow() {
		self.invoiceSpecial.delRow();
	}

	/*
		打开选择客户开票信息对话框
		选择客户开票信息
	 */
	function openCustomerInvoiceDialog() {
		self.customerInvoiceDialog.defaultSearchCustomerInvoice(); //打开选择对话框.
	}


	/**
	 * 打印发票(操作面板-打印按钮 click event listener)
	 // TODO: 打印发票-OLD CODE  
	 */
	function printInvoice() {
		/* 
			此处暂时采用保存时返回的错误信息进行校验,

			// TODO: 稍后再加入其它特定的校验规则及错误显示对话框			
			(1)在打印前需做有效性验证. 判定其中的值是否满足业务要求.
			如果验证无效,则显示对话框提示校验结果.而后退出.
			如果验证成功,则继续后面的流程. 
		*/

		//(2)保存发票信息
		//(2.1)保存发票到金税盘  实际就是发票开具.		
		var respJSON = issueInvoiceJson(); //向web service发送请求
		console.log("开具发票请求返回内容如下:" + JSON.stringify(respJSON));
		if (respJSON != null && respJSON != "") {
			//var obj = $.parseJSON(res);						
			if (respJSON.result_code == "success") {
				//发票开具成功
				util.message(respJSON.result_msg, null, "success");
			} else {
				//dialog type: success warning info error,默认为info
				util.message(respJSON.result_msg, null, "warning");
				return;
			}
		}


		console.log("发票开具完毕,继续保存发票到营收系统中.....");

		//(2.2)保存发票到营收系统中. 保存的前期是已经保存到金税盘.
		var respJSON = saveInvoice2BusSys();
		if (respJSON != null && respJSON != "") {
			//var obj = $.parseJSON(res);						
			if (respJSON.result_code == "success") {
				//发票保存成功
				util.message(respJSON.result_msg, null, "success");
			} else {
				//dialog type: success warning info error,默认为info
				util.message(respJSON.result_msg, null, "warning");
				//return;
			}
		}


		//(3)在打印前需显示打印对话框,其中有三个按钮(打印,不打印,预览)
		self.printDialog.show(); //显示打印对话框

		console.log("已经开具发票,已经保存发票,显示打印对话框,点击打印按钮开始打印.");
	}

	/*
	 * 功能:
	 * 		向后台发送开具发票请求.
	 * 返回:
	 * 		json格式数据
	 */
	function issueInvoiceJson() {
		var invoice = self.getIssueInvoiceParams();
		var respJSON = self.sendIssueInvoiceRequestJson(invoice);
		return respJSON;
	}

	/**
	 * 功能:
	 * 	保存发票到业务系统中
	 */
	function saveInvoice2BusSys() {
		var invoice = self.getIssueInvoiceParams();
		var bill = self.getCurrCustomerBill(); //客户选择的账单对象
		var customerId = self.buyerIssueInvoiceInfo.getCustomerId(); //当前客户ID
		var respJSON = self.sendSaveInvoiceRequestJson(invoice, bill, customerId);
		return respJSON;
	}

	//-------------以下均为测试代码------------

	/**
	 * 开具发票(保存发票---->金税盘)
	 */
	function issueInvoice() {
		var invoice = self.getIssueInvoiceParams();
		self.sendIssueInvoiceRequest(invoice);

	}

	/*
		(1)设置发票表头
		(2)显示发票表头
		test code
	 */
	function dispInvoiceHead() {
		setInvoiceHead();
		invoiceSpecial.displayInvoiceHead();
	}

	/*
		(1)设置发票表头
		test code
	 */
	function setInvoiceHead() {
		console.log("set invoice head and display");
		var invoiceHead = new InvoiceHead();

		invoiceHead.setGFMC("购方名称----TEST");
		invoiceHead.setGFSH("购方税号---TEST");

		invoiceHead.setGFDZDH("购方地址电话---TEST");
		invoiceHead.setGFYHZH("购方银行账号-TEST");

		invoiceHead.setKPR("开票人");
		invoiceHead.setFHR("复核人");
		invoiceHead.setSKR("收款人");

		invoiceHead.setXFDZDH("销方地址电话---TEST");
		invoiceHead.setXFYHZH("销方银行账号-TEST");

		invoiceHead.setBZ("备注消息");

		self.invoiceSpecial.setInvoiceHead(invoiceHead);
	}

	/*
		(1)设置发票附加 
		(2)显示发票附加信息
		test code
	 */
	function dispInvoiceAddition() {
		console.log("set invoice addition and display");
		setInvoiceAddition(); //设置发票附加
		self.invoiceSpecial.displayInvoiceAddition();
	}

	/*
		设置发票附加信息
		test code
	 */
	function setInvoiceAddition() {
		console.log("set invoice addition and display");

		var invoiceAddition = new InvoiceAddition();
		//SET invoiceAddition props;
		invoiceAddition.setInvoiceDM("1300141140-T");
		invoiceAddition.setInvoiceHM("14907748-T");
		invoiceAddition.setIssueInvoiceDate(new Date());

		self.invoiceSpecial.setInvoiceAddition(invoiceAddition);
		//invoiceSpecial.displayInvoiceAddition();
	}

	/*

		用于生成发票详情数据并显示

		(1)设置发票详情条目
		(2)显示发票详情条目
		test code
	 */
	function dispInvoiceDetail() {
		setInvoiceDetail(); //设置发票详情列表.

		console.log("invoice-special invoice detail list'length is:   " + self.invoiceSpecial.getInvoiceDetailNum());


		self.invoiceSpecial.calcTaxAndSum(); //计算发票合计等内容;  计算信息--->发票附加对象中

		self.invoiceSpecial.displayInvoiceAddition(); //显示附加信息

		self.invoiceSpecial.displayInvoiceDetailList(); //显示发票详情;
		//setInvoiceAddition(); //设置发票附加信息
	}

	/*
		(1)设置发票详情
		test code
	 */
	function setInvoiceDetail() {
		console.log("set invoice detail list and display");

		//需要设置的值如下:
		//(1)数量 
		//(2)单价
		//(3)金额
		//(4)其它的均采用默认值				

		for (var i = 0; i < 3; i++) {
			var invoiceDetail = new InvoiceDetail();
			invoiceDetail.setSL(i + 1); //数量
			invoiceDetail.setDJ((i + 1) * (1 + 0.06)); //单价
			invoiceDetail.setSLV("0.06"); //税率
			self.invoiceSpecial.addInvoiceDetail(invoiceDetail);
		}

		console.log("invoice-special invoice detail list'length is:      " +
			self.invoiceSpecial.getInvoiceDetailNum());
	}

	/*
		显示发票
		test code
	 */
	function dispInvoice() {
		console.log("display invoice clicked!");
		setInvoiceHead();
		setInvoiceDetail();
		//console.log("invoice-special invoice detail list'length is:      "+invoiceSpecial.getInvoiceDetailNum());			
		setInvoiceAddition(); //设置发票附加信息			
		self.invoiceSpecial.calcTaxAndSum(); //计算发票合计等内容;  计算信息--->发票附加对象中

		self.invoiceSpecial.displayInvoice(); //显示发票
	}

	//测试预生成发票
	//隐藏预生成发票列表及操作面板
	const BTN_HIDE_PRE_INVOICE = "#btn-hide-pre-invoice"; //显示
	const BTN_SHOW_PRE_INVOICE = "#btn-show-pre-invoice"; //隐藏
	const BTN_SET_PRE_INVOICE_LIST = "#btn-set-pre-invoice-list"; //设置预生成发票列表.

	$(BTN_HIDE_PRE_INVOICE).on('click', function () {
		self.hidePreInvoiceList();
		self.hidePreInvoiceOperatePanel();
	});
	$(BTN_SHOW_PRE_INVOICE).on('click', function () {
		self.showPreInvoiceList();
		self.showPreInvoiceOperatePanel();
	});
	$(BTN_SET_PRE_INVOICE_LIST).on('click', function () {
		var listSelector = ".issue-invoice-panel .pre-invoice-list h5";
		var list = $(listSelector).empty();
		for (var i = 1; i < 10; i++) {
			var onePreInvoice = $("<span class='badge badge-pill badge-info' style='margin-left:5px;'></span>");
			//onePreInvoice.html("<i class='fa fa-film' aria-hidden='true'></i>&nbsp;"+i+"&nbsp;");
			onePreInvoice.html("&nbsp;" + i + "&nbsp;");
			list.append(onePreInvoice);
		}
	});

}

//隐藏/显示  预生成发票列表
IssueInvoice.prototype.hidePreInvoiceList = function () {
	var listSelector = ".issue-invoice-panel .pre-invoice-list";
	$(listSelector).hide();
}
IssueInvoice.prototype.showPreInvoiceList = function () {
	var listSelector = ".issue-invoice-panel .pre-invoice-list";
	$(listSelector).show();
}
//隐藏/显示  预生成发票列表操作面板
IssueInvoice.prototype.hidePreInvoiceOperatePanel = function () {
	//pre-invoice-operate-panel
	var panelSelector = ".issue-invoice-panel .pre-invoice-operate-panel";
	$(panelSelector).hide();
}

IssueInvoice.prototype.showPreInvoiceOperatePanel = function () {
	//pre-invoice-operate-panel
	var panelSelector = ".issue-invoice-panel .pre-invoice-operate-panel";
	$(panelSelector).show();
}


/*
	功能:
		自发票组件中获取请求开具发票的数据
		校验可以在此处理
	返回
		发票开具参数
		[
			{
				invoice1:Invoice(开具发票对象,参见Invoice.js)
			}
		]
*/
IssueInvoice.prototype.getIssueInvoiceParams = function () {
	var invoice = new Invoice(); //开具发票之请求参数对象
	//todo: add code here
	var invoiceHead = self.invoiceSpecial.getInvoiceHead(); //自发票组件中读取发票数据.作为开具发票的请求参数  发票头
	var invoiceDetailList = self.invoiceSpecial.getInvoiceDetailList(); //获取发票详情列表

	invoice.setInvoiceHead(invoiceHead);
	invoice.setInvoiceDetailList(invoiceDetailList);

	var invoiceList = new Array(); //发票列表  即使只有一张发票信息时也需要采用数组来传递.
	invoiceList.push(invoice); //向列表中增加对象

	return invoiceList;
}

/*
功能:
	发送开具发票请求,返回页面.
参数:
	需要打印的发票信息
 */
IssueInvoice.prototype.sendIssueInvoiceRequest = function (params) {
	var self = this; //发票开具前端对象.
	const url = BASE_CONTEXT_PATH + "/tax/issueinvoice/issue"; //请求地址
	const result_container = "#issue-invoice-result-container"; //存放查询结果容器

	//准备请求参数.
	var reqParm = new Object();
	var paramsStr = JSON.stringify(params); //转换成JSON格式而后向后台传递参数		
	reqParm.invoiceListJSON = paramsStr; //参见TaxController中    /issueinvoice/issue

	$(result_container).load(url, reqParm, issueInvoiceCallback); //采用load动态加载查询结果

	/*
	 * 发票开具回调函数
	 */
	function issueInvoiceCallback() {
		console.log("调用:发票开具请求后回调函数");
	}
}

/*
 * 功能:
 * 		发送开票请求. 返回JSON格式结果
 * 返回:
 * 		后台传递过来的JSON数据,参见 RequestResult.java	
 */
IssueInvoice.prototype.sendIssueInvoiceRequestJson = function (params) {
	var self = this; //发票开具前端对象.
	const url = BASE_CONTEXT_PATH + "/tax/issueinvoice/issuejson"; //请求地址

	//准备请求参数.
	var reqParm = new Object();
	var paramsStr = JSON.stringify(params); //转换成JSON格式而后向后台传递参数		
	reqParm.invoiceListJSON = paramsStr; //参见TaxController中    /issueinvoice/issue

	//$(result_container).load(url, reqParm, issueInvoiceCallback); //采用load动态加载查询结果

	var respJSON = "";

	//采用同步方式调用
	$.ajaxSettings.async = false;
	$.post(url, reqParm, issueInvoiceCallback);
	$.ajaxSettings.async = true;

	return respJSON;


	/*
	 * 发票开具回调函数
	 */
	function issueInvoiceCallback(resp) {
		console.log("调用:发票开具请求后回调函数");
		respJSON = resp;
	}
}

/**
 * 发送保存发票请求
 * 参数说明:
 * 		invoiceParams:发票参数 格式同
 * 		bill:格式见
 */
//TODO: 发送保存请求-OLD CODE
IssueInvoice.prototype.sendSaveInvoiceRequestJson = function (invoiceParams, bill, customerId) {
	var self = this; //发票开具前端对象.
	const url = BASE_CONTEXT_PATH + "/tax/issueinvoice/saveinvoice"; //请求地址	


	//准备请求参数.
	var reqParm = new Object();
	//转换成JSON格式而后向后台传递参数		
	reqParm.invoiceListJSON = JSON.stringify(invoiceParams); //参见TaxController中    /issueinvoice/saveinvoice
	reqParm.bill = JSON.stringify(bill); //所选账单
	reqParm.customerId = customerId; //客户ID
	/* //暂时未传递此参数
	//客户名称(客户开票信息中名称,而非营收系统中客户的名称) 
	repParm.customerName=customerName;       */

	var respJSON = "";

	//采用同步方式调用
	$.ajaxSettings.async = false;
	$.post(url, reqParm, saveInvoiceCallback);
	$.ajaxSettings.async = true;

	console.log("保存发票请求结束." + respJSON);
	return respJSON;


	/*
	 * 发票开具回调函数
	 */
	function saveInvoiceCallback(resp) {
		console.log("调用:发票开具请求后回调函数");
		respJSON = resp;
	}
}

/*
	功能:加载专用发票表单
	注:	此功能暂未用
 */
IssueInvoice.prototype.loadInvoiceSpecial = function () {
	const INVOICE_SPECIAL_CONTAINER = "#invoice-special-container"; //发票容器
	const url = BASE_CONTEXT_PATH + "/tax/issueinvoice/loadinvoice"; //请求地址

	$(INVOICE_SPECIAL_CONTAINER).load(url, null, null);
}