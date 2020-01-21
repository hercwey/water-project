/**
 * 发票号码确认对话框组件
 * 参数:
 * 	dialogId:对话框ID
 */
function DisplayInvoiceSpecial(dialogId){
	this.dialogId=dialogId;  //对话框id
	this.title=""; 		 	 //title
	this.message=""; 		 //message
	this.invoiceType=""; 	 //发票类型
	this.invoiceDM="";  	 //发票代码
	this.invoiceHM="";  	 //发票号码
	
	//发票对象
	this.invoiceSpecial = new InvoiceSpecial("invoice-special-1", InvoiceType.FPZL_SPECIAL_INVOICE); //初始化专用发票类型
	this.invoiceSpecial.init(); //初始化编辑组件事件

	this.buttonOkCallback=null;  		//确认按钮回调函数
	this.buttonCancelCallback=null;  	//取按按钮回调函数	
	
	this.setDefault();  //设置默认值
	this.initListener();  //初始化按钮click listener
}

//获取发票对象
DisplayInvoiceSpecial.prototype.getInvoice=function(){
	return this.invoiceSpecial;
	//在前台可以设置相应的值.
	//setHead
	//setDetailList
	//setAddition

	//displayInvoice();  //显示发票即可.

	//参见此处的代码:IssueInvoice.prototype.displayPreDistInvoice = function (index) 
}

/**
 * 设置默认值(title and message)
 */
DisplayInvoiceSpecial.prototype.setDefault=function(){
	//set default value of title and message
	this.title="发票号码确认";
	this.message="现在显示的为将要开具的发票的种类、代码、号码，请认真核对装入打印机中的纸质发票的种类、代码、号码是否与之一致，如一致，可执行打印操作；如不一致，请予以更换。请确认是否填写本张发票？";

	this.setTitle(this.title);
	this.setMessage(this.message);	
}

/**
 * 初始化按钮click event listener
 */
DisplayInvoiceSpecial.prototype.initListener=function(){
	var self=this;

	//按钮选择器常量定义
	const BUTTON_OK="#"+this.dialogId+" "+".button-ok";  
	const BUTTON_CANCEL="#"+this.dialogId+" "+".button-cancel";

	$(BUTTON_OK).on('click',buttonOkClickFunc);
	$(BUTTON_CANCEL).on('click',buttonCancelClickFunc);

	//button ok click event listener
	function buttonOkClickFunc(){
		self.close();  //关闭对话框
		console.log("button ok clicked!");
		if(self.buttonOkCallback!=null){
			self.buttonOkCallback();
		}
	}

	//button cancel click event listener
	function buttonCancelClickFunc(){
		self.close();  //关闭对话框
		if(self.buttonCancelCallback!=null){
			self.buttonCancelCallback();
		}
	}
}


//------------打开,关闭对话框-----------------
//参数:指定的发票ID
DisplayInvoiceSpecial.prototype.show=function(invoiceId){
	//console.log("dialog id is:"+this.dialogId);
	//DialogUtil.openDialogFn(this.dialogId);
var self=this;
	
	console.log("dialog id is:"+this.dialogId);
	console.log("待显示发票ID:"+invoiceId);
	var url=BASE_CONTEXT_PATH+"/tax-invoice/invoice/query";  //查询指定ID发票请求地址
	
	//查询参数
	var param=new Object();
	param.invoiceId=invoiceId;
	
	$.post(url,param,queryCallback);
	
	function queryCallback(resp){
		console.log("查询到的发票是:"+resp);
		if(resp!=""){
			var invoiceObj=self.invoiceSpecial;  //发票对象(UI+DATA)			
			var invoiceData=JSON.parse(resp);  //当前发票
			
			//(1)获取发票相关数据对象
			var invoiceHead = invoiceObj.getInvoiceHead(); //(1.1)发票头对象
			var invoiceAddition = invoiceObj.getInvoiceAddition(); //(1.2)发票附加对象
			invoiceObj.removeAllInvoiceDetail(true); //(1.3)发票列表对象,清除当前发票对象中所有发票详情.
			
			/*
			 * 
			 * 
	this.FPZL="";	 	//发票种类
	this.GFMC="";		//购方名称
	this.GFSH="";		//购方税号
	this.GFDZDH="";	  	//购方地址及电话
	this.GFYHZH="";	  	//购方银行账号
	this.BZ="";	      	//备注
	this.SKR="";	  	//收款人
	this.FHR="";	  	//复核人
	this.KPR="";	  	//开票人
	this.XFYHZH="";	 	//销方银行及账号
	this.XFDZDH="";	  	//销方地址及电话
	this.QDBZ="0";	  	//清单标志,默认值:0-不开具清单
	this.XSDJBH="";	  	//销售单据编号
	this.KPBZ="0";	  	//开票标志. 默认值: 0-开票  1:校验
	this.JPGG="";   	//卷票规格  此值可以不填写.采用默认值,而后进行测试.	
			 * 
			 * 
			 * */
			
			/*"chbz": 0,
	        "deleted": 0,
	        "dybz": "1",
	        "dyr": "系统管理员",
	        "dyrq": 1577076938000,
	        "dyrqStr": "2019-12-23 12:55:38",
	        "fhr": "001",
	        "fpdm": "1300124620",
	        "fphm": "5026145",
	        "fpzl": "2",
	        "gfmc": "石家庄学院",
	        "gfsh": "12130100401758808D",
	        "hjje": 0,
	        "hjse": 0,
	        "id": 245,
	        "kpbz": "0",
	        "kpr": "系统管理员",
	        "kprq": 1576833576000,
	        "kprqStr": "2019-12-20 05:19:36",
	        "qdbz": "0",
	        "skr": "002",
	        "xfdzdh": "建行石家庄开发区支行 13001612008050000022",
	        "xfmc": "石家庄高新技术产业开发区供水排水公司",
	        "xfsh": "130100999999220",
	        "zfbz": 1,
	        "zfr": "系统管理员",
	        "zfrq": 1577076939000,
	        "zfrqStr": "2019-12-23 12:55:39"*/
			
			
			var taxInvoice=invoiceData.taxInvoice;			
			
			invoiceHead.GFMC=taxInvoice.gfmc;		//购方名称
			invoiceHead.GFSH=taxInvoice.gfsh;		//购方税号
			invoiceHead.GFDZDH=taxInvoice.gfdzdh;	  	//购方地址及电话
			invoiceHead.GFYHZH=taxInvoice.gfyhzh;	  	//购方银行账号
			invoiceHead.BZ=taxInvoice.bz;	      	//备注
			invoiceHead.SKR=taxInvoice.skr;	  	//收款人
			invoiceHead.FHR=taxInvoice.fhr;	  	//复核人
			invoiceHead.KPR=taxInvoice.kpr;	  	//开票人
			invoiceHead.XFYHZH=taxInvoice.xfyhzh;	 	//销方银行及账号
			invoiceHead.XFDZDH=taxInvoice.xfdzdh;	  	//销方地址及电话
			invoiceHead.QDBZ="0";	  	//清单标志,默认值:0-不开具清单
			invoiceHead.XSDJBH="";	  	//销售单据编号
			invoiceHead.KPBZ="0";	  	//开票标志. 默认值: 0-开票  1:校验
			
			
			var taxInvoiceDetailList=invoiceData.invoiceDetailList;
			
			//(2.2)发票详情列表对象
			var oneInvoiceAccountItemList = taxInvoiceDetailList;
			for (var i = 0; i < taxInvoiceDetailList.length; i++) {
				var item = taxInvoiceDetailList[i];

				var invoiceDetail = new InvoiceDetail();
				
				invoiceDetail.SPMC = "自来水"; // 商品名称
				invoiceDetail.HSBZ = "1"; // 含税标志  水司默认为含税价  0:不含税  1:含税   采用默认值
				invoiceDetail.SLV = "0.03"; // 税率
				invoiceDetail.JE = item.je; // 金额
				invoiceDetail.DJ=item.dj; // 单价
				invoiceDetail.JLDW="吨"; // 计量单位  采用默认值
				invoiceDetail.GGXH=""; // 规格型号 不必设置
				invoiceDetail.SE=item.se; // 税额  系统会自动计算
				invoiceDetail.SL=item.sl; // 数量  需要设置值
				
				invoiceDetail.BMBBH="33.0"; //编码版本号  不必设置值   采用默认值
				
				invoiceDetail.SSFLBM="110030101"; // 税收分类编码 采用默认值
				invoiceDetail.YHZC="0"; // 是否享受优惠政策 [优惠政策],默认值为不享受  采用默认值
				invoiceDetail.YHZCNR=""; // 享受优惠政策内容  采用默认值
				invoiceDetail.LSLBS=""; // 零税率标识 空为非零税率  采用默认值
				invoiceDetail.QYZBM=""; // 企业自编码  采用默认值
				invoiceDetail.KCE=""; // 扣除额  采用默认值
				
				//其它字段均有默认值,不必再进行赋值

				invoiceObj.addInvoiceDetail(invoiceDetail); //加入到发票对象中(UI+DATA)
			}

			//(2.3)发票附加对象.
			//set invoiceAddition props;
			invoiceAddition.setInvoiceDM(taxInvoice.fpdm);
			invoiceAddition.setInvoiceHM(taxInvoice.fphm);
			invoiceAddition.setIssueInvoiceDate(taxInvoice.kprqStr);
			invoiceAddition.setSalerName(taxInvoice.xfmc);
			invoiceAddition.setSalerTaxNo(taxInvoice.xfsh)
			//(2.4)
			invoiceObj.calcInvoiceSum(); //计算合计及转换成中文合计
			
			//(2.5)  added at 2020/02/04
			var invoiceImageStatus=getInvoiceStatus(taxInvoice.zfbz,taxInvoice.chbz);
			invoiceObj.setInvoiceStatus(invoiceImageStatus);
			
			
			//(3)显示发票数据对象
			invoiceObj.displayInvoice();
			
		}
	}
	
	//0:正常  1:作废  2:冲红
	//zfbz:作废标志     0:正常  1:作废
	//chbz:冲红标志     0:正常  1:作废
	function getInvoiceStatus(zfbz,chbz){		
		if(zfbz==0 && chbz==0){
			return 0;
		}		
		if(zfbz==1){
			return 1;
		}		
		if(chbz==1){
			return 2;
		}
	}
	
	
	DialogUtil.openDialogFn(this.dialogId);
	
}

DisplayInvoiceSpecial.prototype.close=function(){
	DialogUtil.closeDialogFn(this.dialogId);
}

//----------------对话框属性设置(字符串属性)------------

/**
 * set title
 */
DisplayInvoiceSpecial.prototype.setTitle=function(title){
	//define selector constants of title and message
	const DIALOG_TITLE="#"+this.dialogId+" "+".dialog-title";  		//title component selector	
	this.title=title;
	//set title,message's value
	$(DIALOG_TITLE).html(this.title);
	
}

/**
 * set message
 */
DisplayInvoiceSpecial.prototype.setMessage=function(message){
	const DIAOG_MESSAGE="#"+this.dialogId+" "+".dialog-message";  	//message component selector
	this.message=message;
	$(DIAOG_MESSAGE).html(this.message);
}


/**
 * set invoice type
 * 设置发票类型
 * 	取值:普通发票  专用发票
 */
DisplayInvoiceSpecial.prototype.setInvoiceType=function(invoiceType){
	const INVOICE_TYPE="#"+this.dialogId+" "+".invoice-type";
	this.invoiceType=invoiceType;
	$(INVOICE_TYPE).html(this.invoiceType);
}
/**
 * set invoice dm
 * 设置发票代码
 */
DisplayInvoiceSpecial.prototype.setInvoiceDM=function(invoiceDM){
	const INVOICE_DM="#"+this.dialogId+" "+".invoice-dm";
	this.invoiceDM=invoiceDM;
	$(INVOICE_DM).html(this.invoiceDM);
}
/**
 * set invoice hm
 * 设置发票号码
 */
DisplayInvoiceSpecial.prototype.setInvoiceHM=function(invoiceHM){
	const INVOICE_HM="#"+this.dialogId+" "+".invoice-hm";
	this.invoiceHM=invoiceHM;
	$(INVOICE_HM).html(this.invoiceHM);
}

//----------------对话框属性设置(回调函数属性)----------
/**
 * 设置buttonOK click callback function
 */
DisplayInvoiceSpecial.prototype.setButtonOkCallback=function(buttonOkCallback){
	this.buttonOkCallback=buttonOkCallback;
}
/**
 * set buttonCancel callback function
 */
DisplayInvoiceSpecial.prototype.setButtonCancelCallback=function(buttonCancelCallback){
	this.buttonCancelCallback=buttonCancelCallback;

}