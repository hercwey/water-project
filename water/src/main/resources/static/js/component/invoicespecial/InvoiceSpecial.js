//专用发票组件

/**
 * 功能:
 * 		专用发票组件-构造函数
 *	参数:
 * 		@param invoiceId  		发票ID(与UI中的ID相对应)
 * 		@param invoiceType  	发票类型  	
 * 								类型值域: special:专用发票  normal:普通发票  参见InvoiceType.js
 * 		invoiceType:  发票类型  			
 *  
 */
function InvoiceSpecial(invoiceId,invoiceType) {
	//(1)发票对象
	this.invoiceHead = new InvoiceHead();   //(1.1)发票头对象
	this.invoiceHead.setFPZL(invoiceType);  //发票种类设置为专用发票  InvoieType.FPZL_SPECIAL_INVOICE   //added by hz 2019/12/02
	
	this.invoiceDetailList = new Array();  			//(1.2)发票详情对象列表,其中的对象类型为: InvoiceDetail
	this.invoiceAddition = new InvoiceAddition();  	//(1.3)发票附加对象

	this.invoiceId = invoiceId; 	//(2)发票id,与UI中发票的ID相对应

	//发票详情相关.
	this.currRow = -1;  			//发票详情当前行
	this.currRowSelected = false;  //发票详情当前行的行选择器是否己选.  true:已经选定;  false:未选定.
	this.rowIdCounter = 0;  		//发票明细ID计数器
	
	this.editorArr=new Array();    //编辑器数组
	
	this.mInvoiceStatus=0;         //发票状态:  0:正常发票   1:作废  2:冲红
	
	//this.invoiceType=invoiceType; //发票类型
}

//----------------发票状态(getter and setter)--------------------
InvoiceSpecial.prototype.getInvoiceStatus=function(){
	return this.mInvoiceStatus
}
InvoiceSpecial.prototype.setInvoiceStatus=function(invoiceStatus){
	this.mInvoiceStatus=invoiceStatus;
}


//--------------------init listener-----------------

/**
 * 功能:
 *    编辑组件事件绑定.
 */
InvoiceSpecial.prototype.init = function () {
	this.initEditor();  //初始化各字段编辑器
	this.initInvoiceListener();	  //初始化发票listener
}

/**
 * 初始化发票listener
 */
InvoiceSpecial.prototype.initInvoiceListener=function(){
	var self=this;

	const INVOICE = "#" + this.invoiceId + " ";
	const SELECTOR_INVOICE_SKR_LABEL=INVOICE+".invoice-skr-label";
	const SELECTOR_INVOICE_SKR_INPUT=INVOICE+".invoice-skr-input";

	const SELECTOR_INVOICE_FHR_LABEL=INVOICE+".invoice-fhr-label";
	const SELECTOR_INVOICE_FHR_INPUT=INVOICE+".invoice-fhr-input";

	const SELECTOR_INVOICE_KPR_LABEL=INVOICE+".invoice-kpr-label";
	const SELECTOR_INVOICE_KPR_INPUT=INVOICE+".invoice-kpr-input";
	
	//added by hz  2019/12/12 备注信息编辑
	const SELECTOR_INVOICE_REMARK_INPUT=INVOICE+".invoice-remark-input"; 
	$(SELECTOR_INVOICE_REMARK_INPUT).on("click",remarkClick);
	$(SELECTOR_INVOICE_REMARK_INPUT).on("input propertychange",remarkChanged);
	function remarkClick(){
		//do nothing;
	}
	function remarkChanged(){
		var inputValue=$(this).val();
		console.log("输入的备注信息为:"+inputValue);
		self.invoiceHead.setBZ(inputValue);
	}	
		
	//发票点击事件
	$(INVOICE).on("click",invoiceClickListener);
	/**
	 * 隐藏所有的编辑组件
	 */
	function invoiceClickListener(){
		console.log("发票点击事件触发:invoice clicked!");		
		//隐藏所有的编辑组件
		for(var i=0;i<self.editorArr.length;i++){			
			self.editorArr[i].hideFieldEditor();  //隐藏其它的编辑器.
		}
	}

	//----------收款人,复核人,开票人 label----------------
	//点击事情---->收款人input获取焦点
	$(SELECTOR_INVOICE_SKR_LABEL).on("click",function(){
		$(SELECTOR_INVOICE_SKR_INPUT)[0].focus();
		//return false;
	});

	$(SELECTOR_INVOICE_FHR_LABEL).on("click",function(){
		$(SELECTOR_INVOICE_FHR_INPUT)[0].focus();
		//return false;
	});

	$(SELECTOR_INVOICE_KPR_LABEL).on("click",function(){
		$(SELECTOR_INVOICE_KPR_INPUT)[0].focus();
		//return false;
	});

	
}

/**
 * 初始化各字段编辑器 
 */
InvoiceSpecial.prototype.initEditor = function () {
	var self=this;

	//购方字段编辑器初始化
	this.initBuyerNameEditor();	
	this.initBuyerTaxNoEditor();
	this.initBuyerAddressTelEditor();
	this.initBuyerBankAccountNoEditor();

	//销方字段编辑器初始化
	this.initSalerNameEditor();	
	this.initSalerTaxNoEditor();
	this.initSalerAddressTelEditor();
	this.initSalerBankAccountNoEditor();

	
	//为所有的编辑器对象增加事件监听  startEdit
	for(var i=0;i<self.editorArr.length;i++){
		self.editorArr[i].addStartEditCallbackFunc("startEdit",startEditCallbackFunc);
	}

	function startEditCallbackFunc(){
		var currEditor=this;  //当前编辑器

		console.log("接收到startEdit事件");
		console.log("对象比较1:"+  (currEditor==self.buyerNameEditor)   );
		console.log("对象比较2:"+  (currEditor==self.buyerTaxNoEditor)   );
		for(var i=0;i<self.editorArr.length;i++){
			//console.log("对象比较:-----"+(currEditor!=self.editorArr[i]));
			if(currEditor!=self.editorArr[i]){
				self.editorArr[i].hideFieldEditor();  //隐藏其它的编辑器.
			}
		}
	}


	this.buyerNameEditor.addEndEditCallbackFunc("endEdit",endEditCallbackFunc);

	function endEditCallbackFunc(){
		var currEditor=this;
		console.log("接收到endEdit事件");
		console.log("对象比较1:"+  (currEditor==self.buyerNameEditor)   );
		console.log("对象比较2:"+  (currEditor==self.buyerTaxNoEditor)   );
	}

}


/**
 * 功能:初始化购方名称编辑器
 */
InvoiceSpecial.prototype.initBuyerNameEditor=function(){
	var fieldClasses = this.genInitParam("buyer-name");
	this.buyerNameEditor = new InvoiceFieldEditor(this.invoiceId,fieldClasses);
	this.editorArr.push(this.buyerNameEditor);  //将编辑器对象置于数组中.
	
}

/**
 * 功能:初始化购方纳税人识别号
 */
InvoiceSpecial.prototype.initBuyerTaxNoEditor=function(){
	var fieldClasses = this.genInitParam("buyer-tax-no");
	this.buyerTaxNoEditor = new InvoiceFieldEditor(this.invoiceId,fieldClasses);
	this.editorArr.push(this.buyerTaxNoEditor);  //将编辑器对象置于数组中.
}

/**
 * 功能:初始化购方地址及电话
 */
InvoiceSpecial.prototype.initBuyerAddressTelEditor=function(){
	var fieldClasses = this.genInitParam("buyer-address-tel");
	this.buyerAddressTelEditor = new InvoiceFieldEditor(this.invoiceId,fieldClasses);
	this.editorArr.push(this.buyerAddressTelEditor);  //将编辑器对象置于数组中.
}

/**
 * 购方:银行及帐号
 */
InvoiceSpecial.prototype.initBuyerBankAccountNoEditor=function(){
	var fieldClasses = this.genInitParam("buyer-bank-account-no");
	this.buyerBankAccountNoEditor = new InvoiceFieldEditor(this.invoiceId,fieldClasses);
	this.editorArr.push(this.buyerBankAccountNoEditor);  //将编辑器对象置于数组中.
}


//-----------销方------------
/**
 * 功能:销方名称编辑器
 */
InvoiceSpecial.prototype.initSalerNameEditor=function(){
	var fieldClasses = this.genInitParam("saler-name");
	this.salerNameEditor = new InvoiceFieldEditor(this.invoiceId,fieldClasses);
	this.editorArr.push(this.salerNameEditor);  //将编辑器对象置于数组中.
}

/**
 * 功能:初始化销方纳税人识别号编辑器
 */
InvoiceSpecial.prototype.initSalerTaxNoEditor=function(){
	var fieldClasses = this.genInitParam("saler-tax-no");
	this.salerTaxNoEditor = new InvoiceFieldEditor(this.invoiceId,fieldClasses);
	this.editorArr.push(this.salerTaxNoEditor);  //将编辑器对象置于数组中.
}

/**
 * 功能:初始化销方地址及电话
 */
InvoiceSpecial.prototype.initSalerAddressTelEditor=function(){
	var fieldClasses = this.genInitParam("saler-address-tel");
	this.salerAddressTelEditor = new InvoiceFieldEditor(this.invoiceId,fieldClasses);
	this.editorArr.push(this.salerAddressTelEditor);  //将编辑器对象置于数组中.
}

/**
 * 购方:银行及帐号
 */
InvoiceSpecial.prototype.initSalerBankAccountNoEditor=function(){
	var fieldClasses = this.genInitParam("saler-bank-account-no");
	this.salerBankAccountNoEditor = new InvoiceFieldEditor(this.invoiceId,fieldClasses);
	this.editorArr.push(this.salerBankAccountNoEditor);  //将编辑器对象置于数组中.
}

/**
 * 功能:生成初始化参数对象
 */
InvoiceSpecial.prototype.genInitParam=function(fieldClass){
	var fieldClasses = new Object();
	fieldClasses.col = fieldClass+"-col";  				//列column
	fieldClasses.span = fieldClass;  					//显示span
	fieldClasses.editor = fieldClass+"-editor";  		//编辑器
	fieldClasses.input = fieldClass+"-input";  			//输入框
	fieldClasses.btnEdit = "btn-"+ fieldClass+  "-edit";//编辑按钮
	fieldClasses.btnClear = "btn-" +fieldClass+ "-clear";//清除按钮

	return fieldClasses;
}

//-----------发票头InvoiceHead   getter and setter-----------------
/**
 * 获取发票表头
 */
InvoiceSpecial.prototype.getInvoiceHead = function () {
	return this.invoiceHead;
}

/**
 * 功能:	设置发票表头
 * 参数:invoiceHead发票头对象 InvoiceHead  
 */
InvoiceSpecial.prototype.setInvoiceHead = function (invoiceHead) {
	this.invoiceHead = invoiceHead;
}

//----------发票附加:invoiceAddition getter and setter-----------
InvoiceSpecial.prototype.getInvoiceAddition = function () {
	return this.invoiceAddition;
}
InvoiceSpecial.prototype.setInvoiceAddition = function (invoiceAddition) {
	this.invoiceAddition = invoiceAddition;
}

/**
 * 功能:设置发票内容
 */
InvoiceSpecial.prototype.setInvoice = function (invoice) {
	var invoiceHead = invoice.getInvoiceHead();
	var invoiceDetailList = invoice.getInvoiceDetailList();

	this.setInvoiceHead(invoiceHead);
	this.setInvoiceDetailList(invoiceDetailList);
}

//----------------display invoice head and detail-------------------
/**
 * 功能:	显示发票头
 * 注:使用对象中属性
 */
InvoiceSpecial.prototype.displayInvoiceHead = function () {
	var self = this;

	/*
		"FPZL": "",		--->
		"GFMC": "",		--->buyer-name
		"GFSH": "",		--->buyer-tax-no
		"GFDZDH": "",	--->buyer-address-tel
		"GFYHZH": "",	--->buyer-bank-account-no

		"BZ": "",		--->invoice-remark

		"SKR": "",		--->invoice-skr
		"FHR": "",		--->invoice-fhr
		"KPR": "",		--->invoice-kpr

		"XFYHZH": "",	--->saler-bank-account-no
		"XFDZDH": "",	--->saler-address-tel

		"QDBZ": "0", 	--->清单标志
		"XSDJBH": "",	--->销售单据编号
		"KPBZ": "0", 	--->开票标志
		"JPGG": ""		--->卷票规则

		//以下两个字段只用于显示,不必提交.
		销方名称--->  
		销方税号
	*/

	const PREFIX = "#" + self.invoiceId + " ";

	//建立class name与字段值map relation,  class name--->field value
	//以下字段显示在span 标签中.
	var mapList = new Array();
	mapList.push({ className: 'buyer-name', fieldValue: self.invoiceHead.getGFMC() });
	mapList.push({ className: 'buyer-tax-no', fieldValue: self.invoiceHead.getGFSH() });
	mapList.push({ className: 'buyer-address-tel', fieldValue: self.invoiceHead.getGFDZDH() });
	mapList.push({ className: 'buyer-bank-account-no', fieldValue: self.invoiceHead.getGFYHZH() });
	

	mapList.push({ className: 'saler-bank-account-no', fieldValue: self.invoiceHead.getXFYHZH() });
	mapList.push({ className: 'saler-address-tel', fieldValue: self.invoiceHead.getXFDZDH() });

	for (var i = 0; i < mapList.length; i++) {
		var map = mapList[i];
		var class_name = PREFIX + " ." + map.className;
		$(class_name).html(map.fieldValue);
	}
	
	//以下三个字段显示在input控件中
	var mapList1=new Array();
	//modified by hz 2019/12/12 显示备注信息后无法进行编辑BUG处理
	mapList1.push({ className: 'invoice-remark-input', fieldValue: self.invoiceHead.getBZ() });
	
	//收款人,复核人,开票人 输入框处理.
	mapList1.push({ className: 'invoice-skr-input', fieldValue: self.invoiceHead.getSKR() });
	mapList1.push({ className: 'invoice-fhr-input', fieldValue: self.invoiceHead.getFHR() });
	mapList1.push({ className: 'invoice-kpr-input', fieldValue: self.invoiceHead.getKPR() });	
	for (var i = 0; i < mapList1.length; i++) {
		var map = mapList1[i];
		var class_name = PREFIX + " ." + map.className;
		//console.log("开票人等信息:"+);
		$(class_name).val(map.fieldValue);
	}	
}

/**
 * 功能:显示发票附加信息
 */
InvoiceSpecial.prototype.displayInvoiceAddition = function () {
	var self = this;
	const PREFIX = "#" + self.invoiceId + " ";

	/*

	this.invoiceDM="";  //发票代码  --->invoice-dm
	this.invoiceHM="";  //发票号码  --->invoice-hb 
	this.issueInvoiceDate="";  //发票开具日期--->issue-invoice-date

	this.salerName="";  	//销方名称--->saler-name
	this.salerTaxNo="";  	//销方税号--->saler-tax-no

	this.invoiceAmountSum="";   //合计-金额--->invoice-amount-sum
	this.invoiceTaxSum="";		//合计-税----->invoice-tax-sum

	this.invoicePriceTaxSumChinese="";  //价税合计中文--->invoice-price-tax-sum-chinese
	this.invoicePriceTaxSumNumber="";  //价税合计数字--->invoice-price-tax-sum-number

	*/

	//建立class name与字段值map relation,  class name--->field value	
	var mapList = new Array();
	mapList.push({ className: 'invoice-dm', fieldValue: self.invoiceAddition.getInvoiceDM() });
	mapList.push({ className: 'invoice-hm', fieldValue: self.invoiceAddition.getInvoiceHM() });

	var issueInvoiceDate = getCNFormatDate(self.invoiceAddition.getIssueInvoiceDate());  //变换日期格式  2019-10-01---->2019年10月01日
	console.log("开票日期:" + issueInvoiceDate);
	mapList.push({ className: 'issue-invoice-date', fieldValue: issueInvoiceDate });

	mapList.push({ className: 'saler-name', fieldValue: self.invoiceAddition.getSalerName() });
	mapList.push({ className: 'saler-tax-no', fieldValue: self.invoiceAddition.getSalerTaxNo() });

	mapList.push({ className: 'invoice-amount-sum', fieldValue: self.invoiceAddition.getInvoiceAmountSum() });
	mapList.push({ className: 'invoice-tax-sum', fieldValue: self.invoiceAddition.getInvoiceTaxSum() });

	mapList.push({ className: 'invoice-price-tax-sum-chinese', fieldValue: self.invoiceAddition.getInvoicePriceTaxSumChinese() });
	mapList.push({ className: 'invoice-price-tax-sum-number', fieldValue: self.invoiceAddition.getInvoicePriceTaxSumNumber() });

	for (var i = 0; i < mapList.length; i++) {
		var map = mapList[i];
		var class_name = PREFIX + " ." + map.className;
		$(class_name).html(map.fieldValue);
	}
}

//------------------------------发票详情相关操作-----------------------

/**
 * 功能:	显示发票详情列表
 */
InvoiceSpecial.prototype.displayInvoiceDetailList = function () {
	console.log("display invoice detail in special invoice obj");

	//移除详情中的所有行(不清除数据列表)
	this.removeAllInvoiceDetail(false);

	//关闭异步,采用同步方式请求详情行的HTML
	$.ajaxSettings.async = false;

	for (var i = 0; i < this.invoiceDetailList.length; i++) {
		this.addHTMLRow(); //UI部分		
		this.displayInvoiceDetail(i);  //data 部分  显示一行:row data
	}

	$.ajaxSettings.async = true//打开异步
}

/**
 * 功能:移除tbody中的所有行.移除所有的行后,当前行置为-1;
 * 参数:removeDataFlag:是否删除相对应的数据
 *                true-删除;  false-不删除 
 */
InvoiceSpecial.prototype.removeAllInvoiceDetail = function (removeDataFlag) {
	const DETAIL_TBODY_SELECTOR = "#" + this.invoiceId + " .invoice-detail tbody";
	$(DETAIL_TBODY_SELECTOR).empty();

	//如果有清空数据标志,则清空所有的发票详情
	if (removeDataFlag) {
		this.invoiceDetailList.length = 0;
	}

	this.setCurrRow(-1);
}

/**
 * 功能:显示一条详情
 * 参数:行索引号
 */
InvoiceSpecial.prototype.displayInvoiceDetail = function (rowIndex) {
	var self = this;
	var invoiceDetail = self.invoiceDetailList[rowIndex];
	var row = self.getRowJQueryObj(rowIndex);  //获取指定行对象

	if (row == null) {
		return;
	}
	/*
	this.SPMC = ""; // 商品名称 --->product-name-col
	this.GGXH=""; // 规格型号	--->product-type-col
	this.JLDW=""; // 计量单位  	--->product-unit-col
	this.SL=""; // 数量		   --->product-quantity-col
	this.DJ=""; // 单价		   --->product-price-col
	this.JE = ""; // 金额	   --->product-amount-col	
	this.SLV = ""; // 税率     --->product-tax-rate-col
	this.SE=""; // 税额		   --->product-tax-amount-col
	
	this.HSBZ = ""; // 含税标志
	this.BMBBH=""; // 编码版本号
	this.SSFLBM=""; // 税收分类编码
	this.YHZC="0"; // 是否享受优惠政策 [优惠政策],默认值为不享受
	this.YHZCNR=""; // 享受优惠政策内容
	this.LSLBS=""; // 零税率标识
	this.QYZBM=""; // 企业自编码
	this.KCE=""; // 扣除额
	*/
	var mapList = new Array();
	mapList.push({ className: 'product-name-input', fieldValue: invoiceDetail.getSPMC() });
	mapList.push({ className: 'product-type-input', fieldValue: invoiceDetail.getGGXH() });
	mapList.push({ className: 'product-unit-input', fieldValue: invoiceDetail.getJLDW() });
	mapList.push({ className: 'product-quantity-input', fieldValue: invoiceDetail.getSL() });
	mapList.push({ className: 'product-price-input', fieldValue: invoiceDetail.getDJ() });	
	mapList.push({ className: 'product-tax-rate-input', fieldValue: invoiceDetail.getSLV() });	

	for (var i = 0; i < mapList.length; i++) {
		var map = mapList[i];
		var className = map.className;
		var fieldValue = map.fieldValue;		
		$(row).find("." + className).val(fieldValue);			
	}

	var mapList1=new Array();
	mapList1.push({ className: 'product-amount', fieldValue: invoiceDetail.getJE() });
	mapList1.push({ className: 'product-tax-amount', fieldValue: invoiceDetail.getSE() });
	for (var i = 0; i < mapList1.length; i++) {
		var map = mapList1[i];
		var className = map.className;
		var fieldValue = map.fieldValue;		
		$(row).find("." + className).html(fieldValue);			
	}

}

//-------------------------------------

//显示发票状态图标
//added at 2020/01/04
InvoiceSpecial.prototype.displayInvoiceStatus=function(){
	const IMAGE_SELECTOR=".img-invoice-status";
	const IMAGE_REDINFO="/images/redinfo.bmp";
	const IMAGE_OBSOLETE="/images/obsolete.bmp";
	switch(this.mInvoiceStatus){
	case 0:  //正常
		$(IMAGE_SELECTOR).css('display','none'); 
		break;
	case 1:  //作废
		$(IMAGE_SELECTOR).css('display','inline-block'); 
		$(IMAGE_SELECTOR).attr("src",IMAGE_OBSOLETE);
		break;
	case 2:  //冲红
		$(IMAGE_SELECTOR).attr("src",IMAGE_REDINFO);
		$(IMAGE_SELECTOR).css('display','inline-block'); 
		break
	}
	
}

/**
 * 功能:显示整张发票(invoice head and details)
 */
InvoiceSpecial.prototype.displayInvoice = function () {
	this.displayInvoiceHead();  	//(1)显示表头InvoiceHead
	this.displayInvoiceAddition();  //(2)显示发票附加信息InvoiceAddition
	this.displayInvoiceDetailList(); //(3)显示InvoiceDetail
	this.displayInvoiceStatus();
}

/**
 * 功能:
 *    计算详情合计
 *      合计-金额(不在包中)
 *      合计-税额(不在包中)
 *      价税合计-中文(不在包中)
 *      价税合计-小写(不在包中)
 */
InvoiceSpecial.prototype.calcTaxAndSum = function () {
	//const SPECIAL_TAX_RATE = 0.06;    //增值税发票税率;
	//const NORMAL_TAX_RATE = 0.05;     //普通发票税率;

	//计算详情合计及内容	
	var amountSum = Number(0);  //详情条目金额合计
	var taxSum = Number(0);     //详情条目税额合计
	var priceTaxSum = Number(0);  //价税合计  

	for (var i = 0; i < this.invoiceDetailList.length; i++) {
		var invoiceDetail = this.invoiceDetailList[i];

		var dj = InvoiceSpecial.toNumber(invoiceDetail.getDJ(), 5, true);  	//条目单价-含税价
		var slv=InvoiceSpecial.toNumber(invoiceDetail.getSLV(), 2, true);  //税率
		//var priceNoTax = dj / (1 + SPECIAL_TAX_RATE);  	//不含税价
		
		//console.log("求和结果:"+((slv-0+1.00)));
		
		var priceNoTax = dj / (slv-0+1.00);  	//不含税价,注意在计算加法时,不要形成字符串相加的情况.

		var sl = InvoiceSpecial.toNumber(invoiceDetail.getSL(), 2, true); 	//条目数量		
		var je = InvoiceSpecial.toNumber(dj * sl, 2, true);  				//条目金额		
		//console.log("je is:"+je);

		var se = InvoiceSpecial.toNumber(sl*priceNoTax * slv, 2, true);   		//条目税额

		invoiceDetail.setJE(je);
		invoiceDetail.setSE(se);

		amountSum = Number(amountSum) + Number(je);     //金额合计
		//console.log("amountSum is:"+amountSum);
		taxSum = Number(taxSum) + Number(se);           //税额合计 
	}


	/*
	   this.invoiceAmountSum="";     //合计-金额
	   this.invoiceTaxSum="";		//合计-税
 
	   this.invoicePriceTaxSumChinese="";  //价税合计中文
	   this.invoicePriceTaxSumNumber="";  //价税合计数字	
	*/

	taxSum = InvoiceSpecial.toNumber(taxSum, 2, true);  	//格式化
	amountSum = InvoiceSpecial.toNumber(amountSum, 2, true)  //格式化
	priceTaxSum = amountSum; //价税合计

	this.invoiceAddition.setInvoiceAmountSum(amountSum);  	//金额合计
	this.invoiceAddition.setInvoiceTaxSum(taxSum);  		//税额合计
	this.invoiceAddition.setInvoicePriceTaxSumNumber(priceTaxSum);

	var chineseMoney = MoneyTransform.changeMoneyToChinese(priceTaxSum);  //转换成汉字
	this.invoiceAddition.setInvoicePriceTaxSumChinese(chineseMoney);

}

//计算合计  added by hz 2019/12/14
InvoiceSpecial.prototype.calcInvoiceSum = function () {	

	//计算详情合计及内容	
	var amountSum = Number(0);  //详情条目金额合计
	var taxSum = Number(0);     //详情条目税额合计
	var priceTaxSum = Number(0);  //价税合计  

	for (var i = 0; i < this.invoiceDetailList.length; i++) {
		var invoiceDetail = this.invoiceDetailList[i];
		
		var je = InvoiceSpecial.toNumber(invoiceDetail.getJE(), 2, true);  	    	//条目金额		
		//console.log("je is:"+je);

		var se = InvoiceSpecial.toNumber(invoiceDetail.getSE(), 2, true);   		//条目税额

		//invoiceDetail.setJE(je);
		//invoiceDetail.setSE(se);

		amountSum = Number(amountSum) + Number(je);     //金额合计 含税金额
		//console.log("amountSum is:"+amountSum);
		taxSum = Number(taxSum) + Number(se);           //税额合计 
	}


	/*
	   this.invoiceAmountSum="";     //合计-金额
	   this.invoiceTaxSum="";		//合计-税
 
	   this.invoicePriceTaxSumChinese="";  //价税合计中文
	   this.invoicePriceTaxSumNumber="";  //价税合计数字	
	*/

	taxSum = InvoiceSpecial.toNumber(taxSum, 2, true);  	//格式化
	amountSum = InvoiceSpecial.toNumber(amountSum, 2, true)  //格式化
	priceTaxSum = amountSum; //价税合计

	this.invoiceAddition.setInvoiceAmountSum(amountSum);  	//金额合计
	this.invoiceAddition.setInvoiceTaxSum(taxSum);  		//税额合计
	this.invoiceAddition.setInvoicePriceTaxSumNumber(priceTaxSum);

	var chineseMoney = MoneyTransform.changeMoneyToChinese(priceTaxSum);  //转换成汉字
	console.log("汉字金额:"+chineseMoney);
	this.invoiceAddition.setInvoicePriceTaxSumChinese(chineseMoney);

}

/**
 * 重新计算当前行,所有金额并显示
 */
InvoiceSpecial.prototype.recalcTaxAndSumAndDisplay=function(){
	this.calcTaxAndSum();
	this.displayInvoiceAddition();  //(2)显示发票附加信息InvoiceAddition
	this.displayInvoiceDetail(this.currRow);  //(3)显示InvoiceDetail
	
}

/**
 * 功能:判定字段是否为空
 * 参数:str需要判定的字符串
 */
InvoiceSpecial.isNull = function (str) {
	if (typeof (str) == "undefined" || str == null || str === "")
		return true;
	else
		return false;
};

/**
 * 功能(func): 将字符串转为数字 
 * 参数(params): 
 *     str:需要转换为数字的字符串
 *     fixedLen:小数位长度
 *     roundFlag:是否四舍五入  true:采用四舍五入规则  false:不采用四舍五入规则
 * return:
 *     如果字符串为空时,则返回0.00;
 *     如果字符串不为空时,则根据fixedLen及roundFlag来计算 
 */
InvoiceSpecial.toNumber = function (str, fixedLen, roundFlag) {
	var resultValue = 0.00;
	if (InvoiceSpecial.isNull(str)) {
		//do nothing
	}
	else {
		var num = Number(str);
		if (roundFlag) {  //采有四舍五入规则
			resultValue = num.toFixed(fixedLen);
		}
		else {  //不采用四舍五入规则
			var multer = fixedLen * 10;
			resultValue = Math.floor(num * multer) / multer;
		}
	}
	return resultValue;
}

/**
 * 功能:增加发票明细
 * 参数:
 * 	invoiceDetail发票明细对象  InvoiceDetail
 */
InvoiceSpecial.prototype.addInvoiceDetail = function (invoiceDtail) {
	this.invoiceDetailList.push(invoiceDtail);
}

/**
 * 功能:获取当前发票详情条数
 * @returns
 * 	发票详情条目个数
 */
InvoiceSpecial.prototype.getInvoiceDetailNum = function () {
	return this.invoiceDetailList.length;
}

/**
 * 	功能:  获取发票详情列表.
 */
InvoiceSpecial.prototype.getInvoiceDetailList=function(){
	return this.invoiceDetailList;
}



//------------------add detail row-----------------

/**
 * 功能:
 * 		获取发票明细ID计数器的值
 * 返回:
 * 		返回一个发票明细ID
 * 注:	每获取一个后加1 
 */
InvoiceSpecial.prototype.getNewRowId = function () {
	this.rowIdCounter = this.rowIdCounter + 1;
	return this.rowIdCounter;
}
/**
 * 功能:
 * 	增加发票详情行(同时增加详情行data),对外函数
 *  
 * 注:自后台加载新行.
 * 
 * 影响因素:
 * 		一般情况:
 * 			(1)当前行 (2)当前行选择器是否己选
 * 		
 * 		  边界情况(1):空列表时
 * 		  边界情况(2):最大不可以超过5条商品明细(暂未做此限制)
 * 增加行时同时增加对应的商品明细对象.	
 * 
 */
InvoiceSpecial.prototype.addRow = function () {
	var self = this;   //保存InvoiceSpecial实例

	console.log("add row in special invoice!");
	const url = BASE_CONTEXT_PATH + "/tax/issueinvoice/special/genrow";  //向后台请求新的详情行.	

	//向后台请求生成新的行HTML
	//(1)生成rowId
	var newRowId = self.getNewRowId();  //生成新行id
	console.log("new row id is:" + newRowId);
	//(2)向后台发送请求.
	$.post(url, { rowId: newRowId }, insertDetailRow, "html");

	/**
	 * 功能:
	 * 		插入商品明细行
	 * 参数:
	 * 		商品明细行HTML(自后台返回)
	 */
	function insertDetailRow(rowHTML) {
		//console.log("received data is:"+data);		
		const DETAIL_TBODY_SELECTOR = "#" + self.invoiceId + " .invoice-detail tbody";  //发票详情tbody选择器		

		//如果当前列表为空时,插入到紧后一行
		if (self.currRow == -1) {
			//console.log("append invoice detail to tail");
			//console.log("selector is:"+DETAIL_TBODY_SELECTOR);
			$(DETAIL_TBODY_SELECTOR).append(rowHTML);  //新加入的行为当前行(UI部分);
			self.appendEmptyInvoiceDetail();  //新加行对应的发票详情条目(DATA部分)
			self.setCurrRow(0);  	//只设置当前行索引,并未设置行选择器. currRow=0; rowSelected=false;			
		}
		else {
			var currRowIndex = self.currRow;  //获取当前行索引号			
			var rowSelected = self.getRowSelectorStatus(currRowIndex); //判定指定行选择器是否为选定状态.			
			var rowJQueryObj = self.getRowJQueryObj(currRowIndex);  //获取指定行对象		

			//如果选定,则插入到当前行的前面
			if (rowSelected) {
				$(rowJQueryObj).before(rowHTML);  //(UI)
				self.insertEmptyInvoiceDetail(currRowIndex);  //(DATA)
				self.setCurrRow(currRowIndex);  //设置新增行-当前行索引号				
			}
			else {  //插入到当前行的后面.
				//UI部分
				$(rowJQueryObj).after(rowHTML);
				self.setCurrRow(currRowIndex + 1);  //设置新增行-当前行索引号				

				//DATA 部分
				//如果currRow是最后一条之前的条目				
				if (currRowIndex + 1 <= self.getInvoiceDetailNum) {
					self.insertEmptyInvoiceDetail(currRowIndex + 1);  //(DATA)
				}
				else {  //在最后追加
					self.appendEmptyInvoiceDetail();  //新加行对应的发票详情条目(DATA部分)
				}
			}
		}

		//TEST:查看详情列表的长度,稍后MASK
		console.log(self.invoiceDetailList.length);

		self.removeAllRowSelector();  //移除所有行选择器的选定状态

		//设置row click event listener
		self.setRowSelectorClickListener();  //row choicer click event listener
		self.setRowClickListener();			 //row click event listener
		self.setDetailRowColumnListener(self.currRow);   //设置详情列监听器
		
		self.recalcTaxAndSumAndDisplay();  //重新计算并显示.

	}
}

/**
 * 增加详情行(不增加DATA部分)
 * 注:可与上面的函数重构一起重构.
 */
InvoiceSpecial.prototype.addHTMLRow = function () {
	var self = this;   //保存InvoiceSpecial实例

	console.log("add row in special invoice!");
	const url = BASE_CONTEXT_PATH + "/tax/issueinvoice/special/genrow";  //向后台请求新的详情行.	

	//向后台请求生成新的行HTML
	//(1)生成rowId
	var newRowId = self.getNewRowId();  //生成新行id
	console.log("new row id is:" + newRowId);
	//(2)向后台发送请求.
	$.post(url, { rowId: newRowId }, insertDetailRow, "html");

	/**
	 * 功能:
	 * 		插入商品明细行
	 * 参数:
	 * 		商品明细行HTML(自后台返回)
	 */
	function insertDetailRow(rowHTML) {
		//console.log("received data is:"+data);		
		const DETAIL_TBODY_SELECTOR = "#" + self.invoiceId + " .invoice-detail tbody";  //发票详情tbody选择器		

		//如果当前列表为空时,插入到紧后一行
		if (self.currRow == -1) {
			$(DETAIL_TBODY_SELECTOR).append(rowHTML);  //新加入的行为当前行(UI部分);
			//mask data part
			//self.appendEmptyInvoiceDetail();  //新加行对应的发票详情条目(DATA部分)
			self.setCurrRow(0);  	//只设置当前行索引,并未设置行选择器. currRow=0; rowSelected=false;			
		}
		else {
			var currRowIndex = self.currRow;  //获取当前行索引号			
			var rowSelected = self.getRowSelectorStatus(currRowIndex); //判定指定行选择器是否为选定状态.			
			var rowJQueryObj = self.getRowJQueryObj(currRowIndex);  //获取指定行对象		

			//如果选定,则插入到当前行的前面
			if (rowSelected) {
				$(rowJQueryObj).before(rowHTML);  //(UI)
				//mask data part
				//self.insertEmptyInvoiceDetail(currRowIndex);  //(DATA)
				self.setCurrRow(currRowIndex);  //设置新增行-当前行索引号				
			}
			else {  //插入到当前行的后面.
				//UI部分
				$(rowJQueryObj).after(rowHTML);
				self.setCurrRow(currRowIndex + 1);  //设置新增行-当前行索引号	

				//DATA 部分
				//如果currRow是最后一条之前的条目				
				//mask data part
				/*
				if(currRowIndex+1<=self.getInvoiceDetailNum){					
					self.insertEmptyInvoiceDetail(currRowIndex+1);  //(DATA)
				}
				else{  //在最后追加
					self.appendEmptyInvoiceDetail();  //新加行对应的发票详情条目(DATA部分)
				}
				*/
			}
		}

		//TEST:查看详情列表的长度,稍后MASK
		console.log(self.invoiceDetailList.length);

		self.removeAllRowSelector();  //移除所有行选择器的选定状态

		//设置row click event listener
		self.setRowSelectorClickListener();  //row choicer click event listener  此函数可以重构
		self.setRowClickListener();			 //row click event listener   此函数可以重构
		self.setDetailRowColumnListener(self.currRow);   //设置详情列监听器

	}
}

/**
 * 设置详情行各编辑列的event listener.
 */
InvoiceSpecial.prototype.setDetailRowColumnListener=function(rowIndex){
	var self=this;  //发票实例引用.

	const DETAIL_ROW_SELECTOR = "#" + self.invoiceId + " .invoice-detail tbody tr";  //行选择器(jquery)
	const PRODUCT_NAME_INPUT=".product-name-input";  		//class-名称
	const PRODUCT_TYPE_INPUT=".product-type-input";  		//class-规格
	const PRODUCT_UNIT_INPUT=".product-unit-input";  		//class-单位
	const PRODUCT_QUANTITY_INPUT=".product-quantity-input";  //class-数量
	const PRODUCT_PRICE_INPUT=".product-price-input";  		//class-价格
	const PRODUCT_TAX_RATE_INPUT=".product-tax-rate-input";  //class-税率

	var row=$(DETAIL_ROW_SELECTOR)[rowIndex];
	var productNameInput = $(row).find(PRODUCT_NAME_INPUT);	
	var productTypeInput = $(row).find(PRODUCT_TYPE_INPUT);	
	var productUnitInput = $(row).find(PRODUCT_UNIT_INPUT);
	var productQuantityInput = $(row).find(PRODUCT_QUANTITY_INPUT);	
	var productPriceInput = $(row).find(PRODUCT_PRICE_INPUT);	
	var productTaxRateInput = $(row).find(PRODUCT_TAX_RATE_INPUT);

	//-------------商品名称:product name input event listener------------

	$(productNameInput).on("click",productNameInputClick);
	$(productNameInput).on("input propertychange",productNameInputChange);  //onchange
	$(productNameInput).on("keyup",productNameInputKeyUp);  //onkeyup
	
	/**
	 *  click event listener
	 */
	function productNameInputClick(){
		console.log("详情行-商品名称-clicked-行索引号"+rowIndex);
		console.log("详情行-商品名称-clicked");
	}

	/**
	 * change event listener
	 */
	function productNameInputChange(){
		var invoiceDetailItem=self.invoiceDetailList[rowIndex];
		var inputValue=$(this).val();
		console.log("商品名称输入值为:"+inputValue);
		invoiceDetailItem.setSPMC(inputValue);
	}

	/**
	 * keyup event listener
	 * @param {*} event 
	 */
	function productNameInputKeyUp(event){
		switch(event.which){
			case 13:  //回车
				productTypeInput.focus();
				break;
		}
	}

	//-------------商品规格:product type input event listener------------

	$(productTypeInput).on("click",productTypeInputClick);
	$(productTypeInput).on("input propertychange",productTypeInputChange);  //onchange
	$(productTypeInput).on("keyup",productTypeInputKeyUp);  //onkeyup
	
	/**
	 *  click event listener
	 */
	function productTypeInputClick(){
		console.log("详情行-商品规格-clicked-行索引号"+rowIndex);
		console.log("详情行-商品规格-clicked");
	}

	/**
	 * change event listener
	 */
	function productTypeInputChange(){
		var invoiceDetailItem=self.invoiceDetailList[rowIndex];
		var inputValue=$(this).val();
		console.log("商品规格输入值为:"+inputValue);
		invoiceDetailItem.setGGXH(inputValue);
	}

	/**
	 * keyup event listener
	 * @param {*} event 
	 */
	function productTypeInputKeyUp(event){
		switch(event.which){
			case 13:  //回车
				productUnitInput.focus();
				break;
		}
	}

	//-------------商品计量单位:product unit input event listener------------

	$(productUnitInput).on("click",productUnitInputClick);
	$(productUnitInput).on("input propertychange",productUnitInputChange);  //onchange
	$(productUnitInput).on("keyup",productUnitInputKeyUp);  //onkeyup
	
	/**
	 *  click event listener
	 */
	function productUnitInputClick(){
		console.log("详情行-商品计量单位-clicked-行索引号"+rowIndex);
		console.log("详情行-商品计量单位-clicked");
	}

	/**
	 * change event listener
	 */
	function productUnitInputChange(){
		var invoiceDetailItem=self.invoiceDetailList[rowIndex];
		var inputValue=$(this).val();
		console.log("计量单位输入值为:"+inputValue);
		invoiceDetailItem.setJLDW(inputValue);
	}

	/**
	 * keyup event listener
	 * @param {*} event 
	 */
	function productUnitInputKeyUp(event){
		switch(event.which){
			case 13:  //回车
				productQuantityInput.focus();
				break;
		}
	}

	//-------------商品数量:product quantity input event listener------------

	$(productQuantityInput).on("click",productQuantityInputClick);
	$(productQuantityInput).on("input propertychange",productQuantityInputChange);  //onchange
	$(productQuantityInput).on("keyup",productQuantityInputKeyUp);  //onkeyup
	
	/**
	 *  click event listener
	 */
	function productQuantityInputClick(){
		console.log("详情行-商品数量-clicked-行索引号"+rowIndex);
		console.log("详情行-商品数量-clicked");
		
	}

	/**
	 * change event listener
	 */
	function productQuantityInputChange(){
		var invoiceDetailItem=self.invoiceDetailList[rowIndex];
		var inputValue=$(this).val();
		console.log("商品数量输入值为:"+inputValue);
		invoiceDetailItem.setSL(inputValue);
		self.recalcTaxAndSumAndDisplay();
	}

	/**
	 * keyup event listener
	 * @param {*} event 
	 */
	function productQuantityInputKeyUp(event){
		switch(event.which){
			case 13:  //回车
				productPriceInput.focus();  //下一列获取焦点
				break;
		}
	}

	//-------------商品价格:product price input event listener------------

	$(productPriceInput).on("click",productPriceInputClick);
	$(productPriceInput).on("input propertychange",productPriceInputChange);  //onchange
	$(productPriceInput).on("keyup",productPriceInputKeyUp);  //onkeyup
	
	/**
	 *  click event listener
	 */
	function productPriceInputClick(){
		console.log("详情行-商品价格-clicked-行索引号"+rowIndex);
		console.log("详情行-商品价格-clicked");
	}

	/**
	 * change event listener
	 */
	function productPriceInputChange(){
		var invoiceDetailItem=self.invoiceDetailList[rowIndex];
		var inputValue=$(this).val();
		console.log("价格输入值为:"+inputValue);
		invoiceDetailItem.setDJ(inputValue);
		self.recalcTaxAndSumAndDisplay();
	}

	/**
	 * keyup event listener
	 * @param {*} event 
	 */
	function productPriceInputKeyUp(event){
		switch(event.which){
			case 13:  //回车
				productTaxRateInput.focus();  //下一列获取焦点
				break;
		}
	}	
	
	//-------------商品税率:product tax rate input event listener------------

	$(productTaxRateInput).on("click",productTaxRateInputClick);
	//$(productTaxRateInput).on("change",productTaxRateInputChange);  //onchange
	$(productTaxRateInput).on("keyup",productTaxRateInputKeyUp);  //onkeyup
	
	/**
	 *  click event listener
	 */
	function productTaxRateInputClick(){
		console.log("详情行-商品税率-clicked-行索引号"+rowIndex);
		console.log("详情行-商品税率-clicked");
		var invoiceDetailItem=self.invoiceDetailList[rowIndex];
		var inputValue=$(this).val();
		console.log("税率输入值为:"+inputValue);
		invoiceDetailItem.setSLV(inputValue);
		self.recalcTaxAndSumAndDisplay();
	}

	/**
	 * change event listener
	 */
	/*function productTaxRateInputChange(){
		var invoiceDetailItem=self.invoiceDetailList[rowIndex];
		var inputValue=$(this).val();
		console.log("税率输入值为:"+inputValue);
		invoiceDetailItem.setSLV(inputValue);
	}*/

	/**
	 * keyup event listener
	 * @param {*} event 
	 */
	function productTaxRateInputKeyUp(event){
		switch(event.which){
			case 13:  //回车
				productNameInput.focus();  //下一列获取焦点
				break;
		}
	}


}

/**
 * 功能:
 * 		设置详情行-行选定器-click事件处理
 */
InvoiceSpecial.prototype.setRowSelectorClickListener = function () {
	var self = this;  //发票实例引用.

	const DETAIL_ROW_CHOICE_SELECTOR = "#" + self.invoiceId + " .invoice-detail tbody tr td.row-choicer";  //发票详情行---行选定---选择器
	const CLASS_NAME_SELECTED = "selected";
	//(1)先移除事件绑定,而后再加入事件绑定
	$(DETAIL_ROW_CHOICE_SELECTOR).unbind("click");  //移除click事件
	//(2)绑定click事件
	$(DETAIL_ROW_CHOICE_SELECTOR).on("click", rowChoicerColClick);

	/*
	 * 功能:
	 * 	行选择器click	  		  
	 */
	function rowChoicerColClick() {
		//(0)移除原来的行选择器;
		self.removeAllRowSelector();
		//(1)对于行选择器,不管原来是选定状态还是非选定状态,均设置为选定状态.
		$(this).removeClass(CLASS_NAME_SELECTED);
		$(this).addClass(CLASS_NAME_SELECTED);

		//(2)将所click的行设置为当前行;
		var row = $(this).parent();
		var rowIndex = $(row).index();
		console.log("row index is:" + $(row).index());
		self.setCurrRow(rowIndex);

		return false;
	}
}

/**
 * 功能:
 * 	set 详情行click event listener
 */
InvoiceSpecial.prototype.setRowClickListener = function () {
	var self = this;  //发票实例引用.	
	const DETAIL_ROW_SELECTOR = "#" + self.invoiceId + " .invoice-detail tbody tr";  //发票详情行
	const CLASS_CURR = "curr";

	//重新绑定click事件
	$(DETAIL_ROW_SELECTOR).unbind('click');
	$(DETAIL_ROW_SELECTOR).on('click', detailRowClick);

	/*
	 * 功能:
	 * 	详情行click event listener
	 */
	function detailRowClick() {
		var rowIndex = $(this).index();
		console.log("click row index is:" + rowIndex);
		//(1)取消所有行选定器状态
		self.removeAllRowSelector();
		//(2)设置为当前行(索引号)
		self.setCurrRow(rowIndex);
	}
}

/**
 * 功能:
 * 		获取指定行-行选择器的状态
 * 返回:
 * 		如果指定行选择器是选定状态,则返回true,否则返回false;
 */
InvoiceSpecial.prototype.getRowSelectorStatus = function (rowIndex) {

	const DETAIL_ROW_SELECTOR = "#" + this.invoiceId + " .invoice-detail tbody tr";  //发票详情行选择器
	const ROW_SELECTOR_CLASS = "selected";  //行选择器CLASS

	//console.log("selector is:"+DETAIL_ROW_SELECTOR);

	var row = $(DETAIL_ROW_SELECTOR)[rowIndex];
	var col = $(row).find("td.row-choicer")

	//console.log("selected obj is:"+JSON.stringify(col));

	//获取指定行的行选择器是否选定,如果选定则返回true,否则返回false;
	var rowSelected = $(col).hasClass(ROW_SELECTOR_CLASS);
	//console.log("row index is:"+rowIndex);
	//console.log("selector status is:"+rowSelected);
	return rowSelected;
}

/**
 * 功能:
 * 		设置指定行选择器为选定状态
 * 参数:
 * 		rowIndex  行索引号;
 */
InvoiceSpecial.prototype.setRowSelectorStatus = function (rowIndex) {
	const DETAIL_ROW_SELECTOR = "#" + this.invoiceId + " .invoice-detail tbody tr";  //发票详情行选择器
	const ROW_SELECTOR_CLASS = "selected";  //行选择器CLASS

	var row = $(DETAIL_ROW_SELECTOR)[rowIndex];   //rowIndex--->row
	var col = $(row).find("td.row-choicer")		//row-------->col

	//获取指定行的行选择器是否选定,如果选定则返回true,否则返回false;
	var rowSelected = $(col).hasClass(ROW_SELECTOR_CLASS);
	if (!rowSelected) {
		$(col).addClass(ROW_SELECTOR_CLASS);
	}
}

/**
 * 功能:
 * 		设置指定行为当前行-样式部分
 * 参数:
 * 		rowIndex 行索引号
 */
InvoiceSpecial.prototype.setRowStatus = function (rowIndex) {
	const DETAIL_ROW_SELECTOR = "#" + this.invoiceId + " .invoice-detail tbody tr";  //发票详情行选择器
	const CLASS_NAME_CURR = "curr";  //行选择器CLASS

	var row = $(DETAIL_ROW_SELECTOR)[rowIndex];   //rowIndex--->row	
	//获取指定行的行选择器是否选定,如果选定则返回true,否则返回false;
	var isCurrRow = $(row).hasClass(CLASS_NAME_CURR);
	if (!isCurrRow) {
		$(row).addClass(CLASS_NAME_CURR);
	}
}

/**
 * 功能:
 * 		追加发票详情条目
 */
InvoiceSpecial.prototype.appendEmptyInvoiceDetail = function () {

	var invoiceDetail = this.genInvoiceDetail();  //生成一张新的发票详情数据
	this.invoiceDetailList.push(invoiceDetail);
}

/**
 * 功能:
 * 		在指定的行索引处插入详情条目
 */
InvoiceSpecial.prototype.insertEmptyInvoiceDetail = function (rowIndex) {
	var invoiceDetail = this.genInvoiceDetail();  //生成一张新的发票详情数据
	//在指定的索引处插入发票详情条目对象
	this.invoiceDetailList.splice(rowIndex, 0, invoiceDetail);
}

/**
 * 功能:
 * 		//生成一张新的发票详情数据
 */
InvoiceSpecial.prototype.genInvoiceDetail=function(){
	var invoiceDetail=new InvoiceDetail();  //生成发票详情条目
	invoiceDetail.setSPMC("*水*自来水");  	//商品名称
	invoiceDetail.setHSBZ("1");     		//含税标志
	invoiceDetail.setSLV("0.03");  			//税率
	invoiceDetail.setGGXH("饮用水");  		//规则型号
	invoiceDetail.setJLDW("吨");   			//计量单位		
	invoiceDetail.setBMBBH("v0.01");  		//编码版本号
	invoiceDetail.setSSFLBM("001003");  	//税收分类编码
	invoiceDetail.setQYZBM("");  			//企业自编码-与具体的业务相关,一般是业务系统中的ID

	return invoiceDetail;
	
}

//--------------------delete row------------------

/**
 * 功能:
 * 	 删除指定的行索引处的详情条目
 * @param {Object} rowIndex  行索引号
 * 
 * 注:data部分
 * 
 */

InvoiceSpecial.prototype.deleteInvoiceDetail = function (rowIndex) {
	this.invoiceDetailList.splice(rowIndex, 1);
}
/**
 * 功能:
 * 	删除指定的详情行
 * @param {Object} rowIndex  行索引号
 * 注:UI部分
 */
InvoiceSpecial.prototype.deleteInvoiceDetailRow = function (rowIndex) {
	const DETAIL_ROW_SELECTOR = "#" + this.invoiceId + " .invoice-detail tbody tr";  //发票详情行选择器	
	var row = $(DETAIL_ROW_SELECTOR)[rowIndex];   //rowIndex--->row
	$(row).remove();
}



/**
 * 功能:
 * 		获取指定行对象(JQuery)
 * 返回:
 * 		指定的行对象(JQUERY)	
 */
InvoiceSpecial.prototype.getRowJQueryObj = function (rowIndex) {
	const DETAIL_ROW_SELECTOR = "#" + this.invoiceId + " .invoice-detail tbody tr";  //发票详情行选择器

	var rowJqueryObj = $(DETAIL_ROW_SELECTOR)[rowIndex];  //获取指定行的行选择器是否选定,如果选定则返回true,否则返回false;
	return rowJqueryObj;
}

/**
 * 功能:
 * 		设置商品明细当前行索引号.
 * 参数:
 * 		rowIndex:行索引号
 */
InvoiceSpecial.prototype.setCurrRow = function (rowIndex) {
	this.currRow = rowIndex;
	if (rowIndex >= 0) {
		this.removeCurrRowStatus();		//移除所有行curr class
		this.setRowStatus(rowIndex);  	//样式部分
	}
}

/**
 * 功能:
 * 	移除所有详情行class:curr
 */
InvoiceSpecial.prototype.removeCurrRowStatus = function () {
	const DETAIL_SELECTOR = "#" + this.invoiceId + " .invoice-detail tbody tr";
	const CLASS_CURR = "curr";

	console.log("remove curr class");

	$(DETAIL_SELECTOR).removeClass(CLASS_CURR);
}

/**
 * 功能:
 * 		移除所有的行选择器
 *	返回:
 *		无
 */
InvoiceSpecial.prototype.removeAllRowSelector = function () {
	const DETAIL_ROW_SELECTOR = "#" + this.invoiceId + " .invoice-detail tbody tr td.row-choicer";  //发票详情行选择器
	const CLASS_NAME = "selected";

	$(DETAIL_ROW_SELECTOR).removeClass(CLASS_NAME);
}


/**
 * 删除当前行(同时删除数据)
 * 边界情况:
 * 		列表为空:无法删除
 * 删除算法:
 * 		删除当前行后:当前行的下一行变为当前行(索引号相同);
 * 		如果删除的是最后一行,则最后一行的上一行是当前行;
 * 		
 * 	删除行时同时删除对应的商品明细列表对象.
 */
InvoiceSpecial.prototype.delRow = function () {
	console.log("del row in special invoice!");
	//删除当前行
	var rowIndex = this.currRow;

	//如果当前行索引<0时(列表为空)
	if (rowIndex < 0) {
		return;
	}
	else {
		var invoiceDetailNum = this.getInvoiceDetailNum();  //详情列表item个数,删除前

		this.deleteInvoiceDetail(rowIndex);  	//DATA部分
		this.deleteInvoiceDetailRow(rowIndex);  //UI部分

		//设定新的当前行		
		if ((invoiceDetailNum - 1) <= 0) {  //列表已为空
			console.log("invoice detail list is empty.");
			this.setCurrRow(-1);
		}
		else {
			var newCurrRow = -1;
			if (rowIndex == (invoiceDetailNum - 1)) {  //删除的是最后一行
				newCurrRow = rowIndex - 1;
			}
			else {  //删除的非最后一行
				newCurrRow = rowIndex;
			}
			this.setCurrRow(newCurrRow);
		}

		//debug 
		var currInvoiceDetailNum = this.getInvoiceDetailNum();
		console.log("发票详情列表个数:" + currInvoiceDetailNum);
	}

	/*
	当前行示例:
	    2
	0 1 2 3 4 5 

	    2
	0 1 3 4 5
	*/

	this.calcTaxAndSum();
	this.displayInvoiceAddition();  //(2)显示发票附加信息InvoiceAddition
}




