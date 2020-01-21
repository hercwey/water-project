/**
 * 发票打印对话框
 */

function InvoicePrintDialog(dialogId) {

    //对话框id
    this.dialogId = dialogId;

    //dialog title
    this.title = "";
    
    //打印发票列表
    /*
     * 		
     		
     		//只需要设置以下三个字段即可.
     		invoicePrint.setFPZL(InvoiceType.FPZL_SPECIAL_INVOICE);
			invoicePrint.setFPDM(invoice.FPDM);
			invoicePrint.setFPHM(invoice.FPHM);
			
			
			[
				{InvoicePrint对象},  //参见InvoicePrint.js对象
				{}
			]
			
			//参见InvoicePrint.js对象
			this.FPZL="0";	//发票种类,必填字段
			this.FPHM="";	//发票号码,必填字段
			this.FPDM="";	//发票代码,必填字段
			this.TCBZ="0";	//弹窗标志 默认采用不弹参数设置窗口方式  采用默认值即可. 不必重新赋值.
			
     */
    this.mInvoiceList=null;

    this.mCurrPendingNum=0;  //待打印发票索引号

    //callback function
    this.buttonPrintYesCallback = null;
    this.buttonPrintNoCallback = null;
    this.buttonPrintCloseCallback=null;
    this.buttonPrintContinueCallback=null;

    this.topOffset = "";  //上下调整
    this.leftOffset = "";  //左右调整

    this.mPausePage=false;  //每页暂停

    this.setDefault();    //设置默认值
    this.initListener();  //设置按钮click event listener.
}

//---------------init------------------
/**
 * 设置默认值(title)
 */
InvoicePrintDialog.prototype.setDefault = function () {
    //set default value of title and message
    var defaultTitle = "打印";
    this.setTitle(defaultTitle);
    
    var defaultTopOffset="0";
    var defaultLeftOffset="0";
    
    this.setTopOffset(defaultTopOffset);
    this.setLeftOffset(defaultLeftOffset);
    
}


//---------------打开,关闭对话框-----------------
InvoicePrintDialog.prototype.show=function(){
	console.log("dialog id is:"+this.dialogId);
	DialogUtil.openDialogFn(this.dialogId);
}

InvoicePrintDialog.prototype.close=function(){
	DialogUtil.closeDialogFn(this.dialogId);
}

/**
 * 初始化按钮click event listener
 */
InvoicePrintDialog.prototype.initListener = function () {
    var self = this;

    //按钮选择器常量定义
    const BUTTON_PRINT_YES = "#" + this.dialogId + " " + ".button-print-yes";
    const BUTTON_PRINT_NO = "#" + this.dialogId + " " + ".button-print-no";
    const BUTTON_PRINT_CLOSE = "#" + this.dialogId + " " + ".button-print-close";
    const BUTTON_PRINT_CONTINUE="#" + this.dialogId + " " + ".button-print-continue";
    
    const CHECK_PAUSE_PAGE="#" + this.dialogId + " " + ".pause-page";
    
    //每页暂停 click
    $(CHECK_PAUSE_PAGE).on('click',function(){
        var flag = $(this).prop("checked");        
        self.mPausePage=flag;
        console.log("每页暂停状态:"+self.mPausePage);
    });
    
    $(BUTTON_PRINT_YES).on('click', buttonPrintYesClickFunc);
    $(BUTTON_PRINT_NO).on('click', buttonPrintNoClickFunc);
    $(BUTTON_PRINT_CLOSE).on('click', buttonPrintCloseClickFunc);
    $(BUTTON_PRINT_CONTINUE).on('click',buttonPrintContinueClick);
    
    //继续打印button print contine click event listener
    function buttonPrintContinueClick(){
        sendPrintRequest(self.mCurrPendingNum,pausePrintCallback);

        //每页暂时打印回调函数
        function pausePrintCallback(respJSON){
            if (respJSON != null && respJSON != "") {
				//var obj = $.parseJSON(res);						
				if (respJSON.result_code == "success") {  //发票开具成功					
					//util.message(respJSON.result_msg, null, "success");
					console.log("每页暂停模式:发送打印发票请求成功!");					
					//self.showPopupDialog(respJSON.result_msg, resetIssue);										
					
				} else {  //开具失败
					//dialog type: success warning info error,默认为info
					console.log("开具发票失败!");					
                    //self.showPopupDialog("每页暂停模式:开具发票失败,请查询是否已经插入金税盘或是已经打开客户端.", null);
                    util.message(respJSON.result_msg, null, "success");
					return;					
				}
            }            

            //发票列表长度
            var invoiceNum=self.mInvoiceList.length;  

            var currInvoice=self.mInvoiceList[self.mCurrPendingNum];
            
            var pageNo=self.mCurrPendingNum+1;  //页号
            var printMsg="发票张数:"+invoiceNum+"  "+"当前:" +pageNo +"/" +invoiceNum+ "  "+"发票号码:"+currInvoice.getFPHM()+"  打印请求已经发送";
            self.setPrintMessage(printMsg);
            console.log("调用:发票打印请求后回调函数-每页暂时模式" + respJSON);

            self.mCurrPendingNum=self.mCurrPendingNum+1;  //待打印页索引.
            
            //设置按钮的状态.如果已经打印完毕
            if(pageNo==invoiceNum){
                $(BUTTON_PRINT_CONTINUE).hide();
                $(BUTTON_PRINT_CLOSE).show();  //显示打印完毕按钮
            }           
        }

        //继续打印按钮click回调函数.
    	if(self.buttonPrintContinueCallback!=null){
    		self.buttonPrintContinueCallback();  
    	}
    }


    function sendPrintRequest(index,callback){
        const url = BASE_CONTEXT_PATH + "/tax/printinvoice/print"; //请求打印发票
        var invoice = self.mInvoiceList[index];
    
        var invoicePrint = new InvoicePrint();
        invoicePrint.setFPZL(invoice.getFPZL());
        invoicePrint.setFPDM(invoice.getFPDM());
        invoicePrint.setFPHM(invoice.getFPHM());

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



    //开始打印button BUTTON_PRINT_YES click event listener  
    function buttonPrintYesClickFunc() {        
        $(CHECK_PAUSE_PAGE).attr("disabled",true);  //开始打印后无法再修改是否每页暂停
        
        $(BUTTON_PRINT_YES).attr("disabled",true);  //开始打印时,开始打印按钮无效
        $(BUTTON_PRINT_NO).hide();  //开始打印时,不打印按钮隐藏
        
        


        //TODO: 开始打印
        var currPage=-1;
        var pauseFlag=self.getPausePage();    //获取是否每页暂停标志     
        //TODO: 连续打印
        if(!pauseFlag){  //连续打印
            for (var i = 0; i < self.mInvoiceList.length; i++) {
                sendPrintRequest(i,continuePrintCallback);          
            }

        }
        else{  //TODO: 每页暂停
            console.log("每页暂停模式打印开始-起始页......当前索引:"+self.mCurrPendingNum);
            sendPrintRequest(self.mCurrPendingNum,pausePrintCallback);
        }
        
        //每页暂时打印回调函数
        function pausePrintCallback(respJSON){
            console.log("每页暂停模式-回调函数-起始页");
            if (respJSON != null && respJSON != "") {
				//var obj = $.parseJSON(res);						
				if (respJSON.result_code == "success") {  //发票开具成功					
					//util.message(respJSON.result_msg, null, "success");
					console.log("每页暂停模式:发送打印发票请求成功!");					
					//self.showPopupDialog(respJSON.result_msg, resetIssue);										
					
				} else {  //开具失败
					//dialog type: success warning info error,默认为info
					console.log("开具发票失败!");					
					//self.showPopupDialog("每页暂停模式:开具发票失败,请查询是否已经插入金税盘或是已经打开客户端.", null);
					return;					
				}
            }            

            var invoiceNum=self.mInvoiceList.length;

            //currPage=currPage+1;  //自0开始计数
            var currInvoice=self.mInvoiceList[self.mCurrPendingNum];
            
            var pageNo=self.mCurrPendingNum+1;  //页号
            var printMsg="发票张数:"+invoiceNum+"  "+"当前:" +pageNo +"/" +invoiceNum+ "  "+"发票号码:"+currInvoice.getFPHM()+"  打印请求已经发送";
            self.setPrintMessage(printMsg);

            console.log("调用:发票打印请求后回调函数-每页暂时模式" + respJSON);

            self.mCurrPendingNum=self.mCurrPendingNum+1;  //待打印页索引.
            
            //设置按钮的状态.
            if(pageNo==invoiceNum){
                $(BUTTON_PRINT_YES).hide();
                $(BUTTON_PRINT_CLOSE).show();  //显示打印完毕按钮
            }
            else{
                $(BUTTON_PRINT_YES).hide();
                $(BUTTON_PRINT_CONTINUE).show();  //显示继续按钮.
            }
        }
        

		//连续打印回调函数		
		function continuePrintCallback(respJSON) {
            if (respJSON != null && respJSON != "") {
				//var obj = $.parseJSON(res);						
				if (respJSON.result_code == "success") {  //发票开具成功
					
					//util.message(respJSON.result_msg, null, "success");
					console.log("连续打印模式:发送打印发票请求成功!");					
					//self.showPopupDialog(respJSON.result_msg, resetIssue);										
					
				} else {  //开具失败
					//dialog type: success warning info error,默认为info
					console.log("连续打印模式:开具发票失败!");					
					//self.showPopupDialog("连续打印模式:开具发票失败,请查询是否已经插入金税盘或是已经打开客户端.", null);
					return;					
				}
            }            

            var invoiceNum=self.mInvoiceList.length;

            currPage=currPage+1;  //自0开始计数
            var currInvoice=self.mInvoiceList[currPage];


            var pageNo=currPage+1;  //页号
            var printMsg="发票张数:"+invoiceNum+"  "+"当前:" +pageNo +"/" +invoiceNum+ "  "+"发票号码:"+currInvoice.getFPHM()+"  打印请求已经发送";
            self.setPrintMessage(printMsg);
            console.log("调用:发票打印请求后回调函数-连续打印" + respJSON);
            
            //设置按钮的状态.
            if(pageNo==invoiceNum){
                $(BUTTON_PRINT_YES).hide();
                $(BUTTON_PRINT_CLOSE).show();
            }
        }
            	
    	//开始打印按钮回调函数.
        if (self.buttonPrintYesCallback != null) {
            self.buttonPrintYesCallback();
        }

    }

    //不打印button button_print_no click event listener
    function buttonPrintNoClickFunc() {
        self.close();  //关闭对话框
        if (self.buttonPrintNoCallback != null) {
            self.buttonPrintNoCallback();
        }
    }
    
    //打印完毕button button_print_close click event listener    
    function buttonPrintCloseClickFunc(){
    	self.close();  //关闭对话框
    	if (self.buttonPrintCloseCallback != null) {
            self.buttonPrintCloseCallback();
        }
    }
        
}

//-------------setter---------------
/**
 * set title
 */
InvoicePrintDialog.prototype.setTitle = function (title) {
    //define selector constants of title and message
    const DIALOG_TITLE = "#" + this.dialogId + " " + ".dialog-title";  		//title component selector	
    this.title = title;
    //set title's value
    $(DIALOG_TITLE).html(this.title);
}

//set topOffset
InvoicePrintDialog.prototype.setTopOffset = function (topOffset) {
    //define selector constants of topOffset
    const TOP_OFFSET = "#" + this.dialogId + " " + ".top-offset";  		//top-offset input selector
    this.topOffset = topOffset;
    $(TOP_OFFSET).val(this.topOffset);
}

//set leftOffset
InvoicePrintDialog.prototype.setLeftOffset = function (leftOffset) {
    //define selector constants of leftOffset
    const LEFT_OFFSET = "#" + this.dialogId + " " + ".left-offset";
    this.leftOffset = leftOffset;
    $(LEFT_OFFSET).val(this.leftOffset);
}

//TODO: set 需要打印的发票列表
InvoicePrintDialog.prototype.setInvoiceList=function(invoiceList){
    this.mInvoiceList=invoiceList;
    this.mCurrPendingNum=0;

    var invoiceNum=invoiceList.length;

    var currInvoice=invoiceList[this.mCurrPendingNum];  //待打印发票
    var currPageNo=this.mCurrPendingNum+1;  //

	var printMsg="发票张数:"+invoiceNum+"  "+"当前:" +currPageNo +"/" +invoiceNum+ "  "+"发票号码:"+currInvoice.getFPHM();
	this.setPrintMessage(printMsg);
}

//---------------------set callback function-------------------------
//set buttonPrintYes's callback function
InvoicePrintDialog.prototype.setButtonPrintYesCallback = function (callback) {
    this.buttonPrintYesCallback = callback;
}

//set buttonPrintNo's callback function
InvoicePrintDialog.prototype.setButtonPrintNoCallback = function (callback) {
    this.buttonPrintNoCallback = callback;
}

//set buttonPrintNo's callback function
InvoicePrintDialog.prototype.setButtonPrintCloseCallback = function (callback) {
    this.buttonPrintCloseCallback = callback;
}

//set buttonPrintContinue's callback function
InvoicePrintDialog.prototype.setButtonPrintContinueCallback = function (callback) {
    this.buttonPrintContinueCallback = callback;
}


//---------------------getter----------------------
//get top offset
InvoicePrintDialog.prototype.getTopOffset=function(){
    const TOP_OFFSET = "#" + this.dialogId + " " + ".top-offset";  		//top-offset input selector    
    var topOffset=$(TOP_OFFSET).val();
    return topOffset;
}

//get left offset
InvoicePrintDialog.prototype.getLeftOffset=function(){
    const LEFT_OFFSET = "#" + this.dialogId + " " + ".left-offset";  	//left-offset input selector    
    var leftOffset=$(LEFT_OFFSET).val();
    return leftOffset;
}

//获取是否每页暂停
InvoicePrintDialog.prototype.getPausePage=function(){	
	return this.mPausePage;
}

//------------------set button status-------------
//初始状态(显示打印/不打印按钮)
InvoicePrintDialog.prototype.setInitButtonStatus=function(){
    var self=this;

	 const BUTTON_PRINT_YES = "#" + this.dialogId + " " + ".button-print-yes";
	 const BUTTON_PRINT_NO = "#" + this.dialogId + " " + ".button-print-no";
	 const BUTTON_PRINT_CLOSE = "#" + this.dialogId + " " + ".button-print-close";
	 const BUTTON_PRINT_CONTINUE="#" + this.dialogId + " " + ".button-print-continue";
     
     //开始打印按钮可见且有效.
     $(BUTTON_PRINT_YES).show();
     $(BUTTON_PRINT_YES).attr("disabled",false);  //开始打印时,开始打印按钮无效

     //每页暂停checkbox可操作,连续打印状态
     const CHECK_PAUSE_PAGE="#" + this.dialogId + " " + ".pause-page";
     self.mPausePage=false;
     $(CHECK_PAUSE_PAGE).attr("disabled",false);
     $(CHECK_PAUSE_PAGE).prop("checked",false);


     //其它按钮隐藏
	 $(BUTTON_PRINT_NO).show();	 
	 $(BUTTON_PRINT_CLOSE).hide();
	 $(BUTTON_PRINT_CONTINUE).hide();	 
}

//只显示继续打印按钮
/* InvoicePrintDialog.prototype.setPrintContinueButtonStatus=function(){
	 const BUTTON_PRINT_YES = "#" + this.dialogId + " " + ".button-print-yes";
	 const BUTTON_PRINT_NO = "#" + this.dialogId + " " + ".button-print-no";
	 const BUTTON_PRINT_CLOSE = "#" + this.dialogId + " " + ".button-print-close";
	 const BUTTON_PRINT_CONTINUE="#" + this.dialogId + " " + ".button-print-continue";
	 
	 $(BUTTON_PRINT_YES).hide();
	 $(BUTTON_PRINT_NO).hide();	 
	 $(BUTTON_PRINT_CLOSE).hide();
	 
	 $(BUTTON_PRINT_CONTINUE).show();	 
} */

//设置打印信息
InvoicePrintDialog.prototype.setPrintMessage=function(message){
	const printMessage = "#" + this.dialogId + " " + ".print-message";
	$(printMessage).html(message);
}

//enable:  true or false   true:允许点击  false:不允许点击.
/* InvoicePrintDialog.prototype.setBtnCloseStatus=function(enable){
	if(enable){
		$(BUTTON_PRINT_CLOSE).removeClass("disable");
	}
	else
	{
		$(BUTTON_PRINT_CLOSE).addClass("disable");
	}
	
} */
