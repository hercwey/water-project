/**
 * 普通发票卷选择对话框组件-JS部分.
 */

function NormalInvoiceVolDialog(dialogId) {
	this.dialogId = dialogId; //对话框ID
	this.title = ""; //标题

	this.normalInvoiceVolList = null; //普通发票卷列表对象.

	this.choiceCallback = null; //选择发票卷的回调函数
	this.exitCallback = null; //退出按钮回调函数

	this.setDefault(); //设置默认值
	this.initBtnListener(); //初始化按钮click listener
}

//---------------设置回调函数--------------

/**
 * 功能:设置选择按钮回调函数
 * 	(1)callback签名:  function callbackx(resp)  
 * 	(2)回调函数参数说明:返回的数据格式.
 * 		{
 * 			"FPZL":"0",
 * 			"FPDM":"001",
 * 			"FPHM":"002",
 * 			"KCFPSL":"100"
 * 		}
 * 	
 */
NormalInvoiceVolDialog.prototype.setChoiceCallback = function (callback) {
	this.choiceCallback = callback;
}

/**
 * 功能:设置退出按钮回调函数
 * @param  callback  回调函数  此函数的签名为:  function callback();  无参数.
 */
NormalInvoiceVolDialog.prototype.setExitCallback = function (callback) {
	this.exitCallback = callback;
}


//----------设置默认值及按钮click事件初始化-----------
/**
 * 设置默认值(title and message)
 */
NormalInvoiceVolDialog.prototype.setDefault = function () {
	//set default value of title 
	this.title = "增值税普通发票-发票卷";
	this.setTitle(this.title);
}


/**
 * 初始化按钮click event listener
 */
NormalInvoiceVolDialog.prototype.initBtnListener = function () {
	var self = this;

	//按钮选择器常量定义
	const PREFIX = "#" + this.dialogId;

	const BUTTON_CHOICE_INVOICE_VOL = PREFIX + " " + ".btn-choice-invoice-vol"; //选择发票卷按钮  
	const BUTTON_EXIT = PREFIX + " " + ".btn-exit"; //退出按钮	

	$(BUTTON_CHOICE_INVOICE_VOL).on('click', btnChoiceInvoiceVol); //choice invoice vol  click event listener
	$(BUTTON_EXIT).on('click', btnExit); //exit button click event listener


	//button ok click event listener
	function btnChoiceInvoiceVol() {
		self.close(); //关闭对话框
		console.log("button choice clicked!");
		if (self.choiceCallback != null) {
			var resultData = getResultData();  
			self.choiceCallback(resultData);
		}
	}

	//button cancel click event listener
	function btnExit() {
		self.close(); //关闭对话框
		console.log("button exit clicked!");
		if (self.exitCallback != null) {
			self.exitCallback(); //执行回调函数.
		}
	}

	/**
	 * 获取用户选择结果.
	 * 返回:
	 * 		null或是如下格式的对象.
	 * 		{
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
	function getResultData() {
		const CURR_CLASS_NAME = "curr";
		const CURR_ROW_SELECTOR = PREFIX + " " + ".invoice-vol-table tbody tr" + "." + CURR_CLASS_NAME; //当前行选择器.

		var resultData = null;
		var currRow = $(CURR_ROW_SELECTOR);
		if (currRow.length>0) { //如果行存在时
			resultData = new Object();
			var serialNo = parseInt(currRow.attr("data-bind-serial-no")); //序号
			var inventory = self.normalInvoiceVolList[serialNo];
			//resultData=inventory;  //返回用户选择的发票卷.  采用引用的方式赋值
			//此处需要进行测试
			resultData = Object.assign({}, inventory);  //采用COPY方式赋值 
			
			/*
				resultData.FPZL = InvoiceType.FPZL_NORMAL_INVOICE; //普通发票
				resultData.FPDM = inventory.FPDM; //发票代码
				resultData.FPHM = inventory.FPHM; //发票号码
				resultData.KCFPSL = inventory.KCFPSL; //库存发票数量			
			 */		
		}
		return resultData;

	}

}

/**
 * 初始化列表行click event listener
 */
NormalInvoiceVolDialog.prototype.initRowListener = function () {
	var self = this;

	//按钮选择器常量定义
	const PREFIX = "#" + this.dialogId;
	//const BUTTON_CHOICE_INVOICE_VOL = PREFIX + " " + ".btn-choice-invoice-vol"; //选择发票卷按钮  
	const ROW_SELECTOR = PREFIX + " " + ".invoice-vol-table tbody tr"; //行选择器.

	$(ROW_SELECTOR).on('click', rowClick); //set row click event listener

	/**
	 *	用户点击某行时切换CSS 
	 */
	function rowClick() {
		const CLASS_CURR = "curr";
		var currFlag = $(this).hasClass(CLASS_CURR);
		if (currFlag) {
			//do nothing
		} else {
			$(ROW_SELECTOR).removeClass(CLASS_CURR);
			$(this).addClass(CLASS_CURR);
		}
	}

}

//------------打开,关闭对话框-----------------
NormalInvoiceVolDialog.prototype.show = function () {
	console.log("dialog id is:" + this.dialogId);

	this.queryInventory(InvoiceType.FPZL_NORMAL_INVOICE); //查询普通发票卷(发票库存)
	DialogUtil.openDialogFn(this.dialogId);

}

NormalInvoiceVolDialog.prototype.close = function () {
	DialogUtil.closeDialogFn(this.dialogId);
}

//--------------查询发票信息并在列表中显示-------------
NormalInvoiceVolDialog.prototype.queryInventory = function (invoiceType) {
	var self = this;
	const url = BASE_CONTEXT_PATH + "/tax/inventory/query"; //请求地址

	//发票类型列表
	var invoiceTypeList = new Array();
	invoiceTypeList.push(invoiceType);

	var param = new Object();
	param.invoiceTypeList = invoiceTypeList;

	//--------------------temp mask-------------------------
	//测试时暂时屏蔽,待测试完毕后打开.	
	$.post(url, param, successCallback); //采用load动态加载查询结果  查询普通发票卷请求
	

	//-----------------------test start--------------------
	//TEST DATA
	/*var data1 = new Object();
	data1.FPDM = "dm001";
	data1.FPHM = "001"
	data1.KCFPSL = "100";

	var data2 = new Object();
	data2.FPDM = "dm002";
	data2.FPHM = "001"
	data2.KCFPSL = "200";

	var inventoryArr = new Array();
	inventoryArr.push(data1);
	inventoryArr.push(data2);

	self.normalInvoiceVolList=inventoryArr;

	
	displayNormalInvoiceVolList(inventoryArr); //测试代码.动态增加table row
*/	
	//--------------------------test end.-----------------

	
	//查询库存成功-专用发票-回调函数	
	function successCallback(resp) {
		console.log("返回的数据是-普通发票:" + resp);
		if (resp != "") {
			console.log("返回数据正常");
			//将JSON转换为JS数组对象.此处必须将resp转换成JS对象.
			var inventoryArr = JSON.parse(resp);
			//var specialInventory = inventoryArr[0];  //可能多个

			//(1)保存普通发票卷对象列表
			self.normalInvoiceVolList = inventoryArr;

			//(2)显示普通发票卷对象列表
			displayNormalInvoiceVolList(inventoryArr);

		}
		else{
			console.log("读取发票库存时发生错误!");
			util.message("未读取到普通发票库存数据!",null,"warning");
		}

	}

	/**
	 * 显示库存发票卷库存列表.
	 * @param {*} inventoryArr  普通发票卷库存列表
	 */
	function displayNormalInvoiceVolList(inventoryArr) {
		console.log("显示普通发票卷信息");
		/* 
			RETCODE	错误代码	字符	100	是	
			RETMSG	错误信息	字符	100	是
			FPHM	发票号码	字符	20		
			FPDM	发票代码	字符	20		
			KCFPSL	发票库存数量	数值			
			JSSBRQ	金税设备日期	日期	19			格式yyyy-MM-dd hh:mm:ss	
														年月日的格式以本地短时间格式为准
			XFSH	销方税号	字符	20		
			SCFS	上传方式	字符	2		固定值
												0：手动上传
												1：自动上传
			KPDH	开票点号	字符	3		
			CSQBZ	是否已到抄税期	字符	2		固定值
											0：未到抄税期
											1：已到抄税期
			SSQBZ	是否已到锁死期	字符	2		固定值
											0：未到抄税期
											1：已到锁死期
			KPFS	开票方式	字符	2		固定值
											0：单机开票
											1：服务器开票
			KPFWQH	开票服务器号	字符	3		
			JSPH	金税盘号	字符	20		
			XFMC	销方名称	字符	100		
		 */
		const PREFIX = "#" + self.dialogId;

		const TBODY_SELECTOR = PREFIX + " " + ".invoice-vol-table tbody "; //table tbody选择器.
		const ROW_SELECTOR = PREFIX + " " + ".invoice-vol-table tbody tr";
		var tbody = $(TBODY_SELECTOR);

		//清空原来列中的内容
		$(TBODY_SELECTOR).empty();

		//迭代加入发票库存信息
		for (var i = 0; i < inventoryArr.length; i++) {
			var inventory = inventoryArr[i];


			var row = $("<tr data-bind-serial-no=" + "'" + i + "'" + "></tr>"); //一行 属性为序号

			if (i == 0) {
				row.addClass("curr");
			}

			var colSerialNo = $("<td></td>"); //序号列
			colSerialNo.html((i + 1) + ""); //列值

			var colInvoiceType = $("<td></td>"); //发票类别名称
			colInvoiceType.html("普通发票");

			var colFPDM = $("<td></td>"); //发票代码
			colFPDM.html(inventory.FPDM);

			var colFPHM = $("<td></td>"); //发票号码
			colFPHM.html(inventory.FPHM);

			var colKCFPSL = $("<td></td>"); //数量列
			colKCFPSL.html(inventory.KCFPSL);

			//在行中加入以上列
			row.append(colSerialNo);
			row.append(colInvoiceType);
			row.append(colFPDM);
			row.append(colFPHM);
			row.append(colKCFPSL);

			//把row加入到tbody中.
			tbody.append(row); //加入到tbody中.
		}

		$(ROW_SELECTOR).unbind("click"); //移除原来的click事件,

		self.initRowListener(); //重新绑定事件

	}

}



//--------------通用属性getter and setter--------------
/**
 * set title标题
 */
NormalInvoiceVolDialog.prototype.setTitle = function (title) {
	//define selector constants of title and message
	const DIALOG_TITLE = "#" + this.dialogId + " " + ".dialog-title"; //title component selector	
	this.title = title;
	$(DIALOG_TITLE).html(this.title); //set title,message's value

}