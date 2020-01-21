/**
 * 批量发票查询条件对话框-组件JS部分
 */
function BatchQueryInvoiceCondDialog(dialogId) {

    //对话框通用属性
    this.dialogId = dialogId;   //对话框id
    this.title = "";            //dialog title

    //查询条件对话框.
    this.KPJH = ""; //开票机号                 默认值为:""  使用默认值
    this.XSDJBH = ""; //销售单据编号      默认值为:""   使用默认值
    this.GFMC = ""; //购方名称
    this.GFSH = ""; //购方税号
    this.XFMC = ""; //销方名称           默认值为:""      使用默认值
    this.XFSH = ""; //销方税号           默认值为:"";	 使用默认值
    this.FPDM = ""; //发票代码
    this.FPHM = ""; //发票号码
    this.FPZL = ""; //发票种类
    this.QDBZ = ""; //清单标志           默认值为:""  使用默认值
    this.ZFBZ = ""; //作废标志           默认值为:""  
    this.DYBZ = ""; //打印标志           默认值为:""
    this.BSBZ = ""; //报送标志           默认值为:"" 返回
    this.KPRQQ = ""; //开票日期起
    this.KPRQZ = ""; //开票日期止

    this.buttonOkCallback = null;       //确定按钮-回调函数
    this.buttonCancelCallback = null;   //取消按钮-回调函数

    //init    
    this.initListener(); //初始化按钮click event listener
    this.setDefault(); //设置属性默认值
}

//init functions
BatchQueryInvoiceCondDialog.prototype.setDefault = function () {
    var defaultTitle = "发票查询条件";
    this.setTitle(defaultTitle);

    //TODO 常量定义可以重构,稍后再处理.
    //作废标志-默认为全选择
    const CHECK_INVALID_NO = "#" + this.dialogId + " " + ".check-invalid-no"; //未作废-checkbox
    const CHECK_INVALID_YES = "#" + this.dialogId + " " + ".check-invalid-yes"; //己作废-checkbox
    $(CHECK_INVALID_NO).prop("checked",true);
    $(CHECK_INVALID_YES).prop("checked",true);
    //$(CHECK_INVALID_NO).trigger("change");  //刷新作废标志值
    console.log("初始作废标志为:"+this.getZFBZ());

    //打印标志-默认为全选择
    const CHECK_PRINT_NO = "#" + this.dialogId + " " + ".check-print-no";   //未打印-checkbox
    const CHECK_PRINT_YES = "#" + this.dialogId + " " + ".check-print-yes"; //己打印-checkbox
    $(CHECK_PRINT_NO).prop("checked",true);
    $(CHECK_PRINT_YES).prop("checked",true);
    //$(CHECK_PRINT_NO).trigger("change");  //刷新打印标志值
    console.log("初始打印标志为:"+this.getDYBZ());


    const CHECK_SEND_NO = "#" + this.dialogId + " " + ".check-send-no";   //发送状态-未发送-checkbox
    //报送标志-默认为全选择    
    this.setSendStatusAll(true);            //设置发送状态为全选    
    console.log("初始报送标志为:"+this.getBSBZ());
}


/**
 * 功能: 设置报送状态(所有选项)
 * 参数:
 *      status : Boolean类型
 *      可选值:  true-选定状态
 *              fales-未选定状态
 */
BatchQueryInvoiceCondDialog.prototype.setSendStatusAll=function(status){
    const CHECK_SEND_NO = "#" + this.dialogId + " " + ".check-send-no";         //selector  未报送
    const CHECK_SEND_YES = "#" + this.dialogId + " " + ".check-send-yes";       //selector    己报送
    const CHECK_SEND_FAIL = "#" + this.dialogId + " " + ".check-send-fail";     //selector  报送失败
    const CHECK_SENDING = "#" + this.dialogId + " " + ".check-sending";         //selector 报送中
    const CHECK_VALID_FAIL = "#" + this.dialogId + " " + ".check-valid-fail";   //selector 验签失败
    $(CHECK_SEND_NO).prop("checked",status);
    $(CHECK_SEND_YES).prop("checked",status);
    $(CHECK_SEND_FAIL).prop("checked",status);
    $(CHECK_SENDING).prop("checked",status);
    $(CHECK_VALID_FAIL).prop("checked",status);
    //$(CHECK_SEND_NO).trigger("change");  //刷新报送标志值     
}

//init button click event listener
BatchQueryInvoiceCondDialog.prototype.initListener = function () {
    var self = this;

    //-------------------按钮回调函数调用------------------
    //define constants
    const BUTTON_OK = "#" + self.dialogId + " " + ".button-ok";
    const BUTTON_CANCEL = "#" + self.dialogId + " " + ".button-cancel";

    //set buttons' click event listener
    $(BUTTON_OK).on('click', buttonOkListener);
    $(BUTTON_CANCEL).on('click', buttonCancelListener);

    //ok button's click event listener
    function buttonOkListener() {
        self.close(); //close dialog
        //execute callback function
        if (self.buttonOkCallback != null) {
            self.buttonOkCallback();
        }

    }

    //calcel button's click event listener
    function buttonCancelListener() {
        //execute callback function
        if (self.buttonCancelCallback != null) {
            self.buttonCancelCallback();
        }
    }

    //--------------作废标志事件处理--------------
    const CHECK_INVALID_NO = "#" + self.dialogId + " " + ".check-invalid-no"; //未作废-checkbox
    const CHECK_INVALID_YES = "#" + self.dialogId + " " + ".check-invalid-yes"; //己作废-checkbox

    //invalid-no  click
    $(CHECK_INVALID_NO).on('click', function () {
        this.blur();
        this.focus();
    });

    //invalid-yes change
    $(CHECK_INVALID_NO).on('change', function () {
        var resultValue = self.calcZFBZ();
        console.log("作废标志:" + resultValue);
        self.ZFBZ = resultValue;
    });


    //invalid-no click
    $(CHECK_INVALID_YES).on('click', function () {
        this.blur();
        this.focus();
    });

    //invalid-yes change
    $(CHECK_INVALID_YES).on('change', function () {
        var resultValue = self.calcZFBZ();
        console.log("作废标志:" + resultValue);
        self.ZFBZ = resultValue;
    });

    //---------------打印标志事件处理-----------------
    const CHECK_PRINT_NO = "#" + self.dialogId + " " + ".check-print-no";   //未打印-checkbox
    const CHECK_PRINT_YES = "#" + self.dialogId + " " + ".check-print-yes"; //己打印-checkbox

    //print-no click(用于触发change事件兼容性)
    $(CHECK_PRINT_NO).on('click', function () {
        this.blur();
        this.focus();
    });

    //print-no change
    $(CHECK_PRINT_NO).on('change', function () {
        var resultValue = self.calcDYBZ();
        console.log("打印标志:" + resultValue);
        self.DYBZ = resultValue;
    });


    //print-yes click
    $(CHECK_PRINT_YES).on('click', function () {
        this.blur();
        this.focus();
    });

    //print-yes change
    $(CHECK_PRINT_YES).on('change', function () {
        var resultValue = self.calcDYBZ();
        console.log("打印标志:" + resultValue);
        self.DYBZ = resultValue;
    });

    //--------------报送标志-------------

    const CHECK_SEND_NO = "#" + self.dialogId + " " + ".check-send-no";         //selector  未报送
    const CHECK_SEND_YES = "#" + self.dialogId + " " + ".check-send-yes";       //selector  己报送
    const CHECK_SEND_FAIL = "#" + self.dialogId + " " + ".check-send-fail";     //selector  报送失败
    const CHECK_SENDING = "#" + self.dialogId + " " + ".check-sending";         //selector 报送中
    const CHECK_VALID_FAIL = "#" + self.dialogId + " " + ".check-valid-fail";   //selector 验签失败

    //触发change事件兼容性处理
    $(CHECK_SEND_NO).on('click', function () {
        this.blur();
        this.focus();
    });
    $(CHECK_SEND_YES).on('click', function () {
        this.blur();
        this.focus();
    });
    $(CHECK_SEND_FAIL).on('click', function () {
        this.blur();
        this.focus();
    });
    $(CHECK_SENDING).on('click', function () {
        this.blur();
        this.focus();
    });
    $(CHECK_VALID_FAIL).on('click', function () {
        this.blur();
        this.focus();
    });


    //on change event
    $(CHECK_SEND_NO).on('change', function () {
        var resultValue = self.calcBSBZ();
        console.log("sned-no triggered!:" + resultValue);
        self.BSBZ = resultValue;
    });
    $(CHECK_SEND_YES).on('change', function () {
        var resultValue = self.calcBSBZ();
        console.log("send-yes triggered!" + resultValue);
        self.BSBZ = resultValue;
    });
    $(CHECK_SEND_FAIL).on('change', function () {
        var resultValue = self.calcBSBZ();
        console.log("send-fail triggered:" + resultValue);
        self.BSBZ = resultValue;
    });
    $(CHECK_SENDING).on('change', function () {
        var resultValue = self.calcBSBZ();
        console.log("sending-triggered:" + resultValue);
        self.BSBZ = resultValue;
    });
    $(CHECK_VALID_FAIL).on('change', function () {
        var resultValue = self.calcBSBZ();
        console.log("valid-fail triggered" + resultValue);
        self.BSBZ = resultValue;
    });

    const BUTTON_SEND_STATUS_SELECT_ALL = "#" + self.dialogId + " " + ".button-send-status-select-all";         //selector  报送状态-全选
    const BUTTON_SEND_STATUS_DESELECT_ALL = "#" + self.dialogId + " " + ".button-send-status-deselect-all";         //selector  报送状态-全取消

    //报送状态-全选按钮-click event listener
    $(BUTTON_SEND_STATUS_SELECT_ALL).on('click',function(){
        self.setSendStatusAll(true);
        $(CHECK_SEND_NO).trigger('change');
        console.log("全选报送标志为:"+self.getBSBZ());

    });

    //报送状态-全取消按钮-click event listener
    $(BUTTON_SEND_STATUS_DESELECT_ALL).on('click',function(){
        self.setSendStatusAll(false);
        $(CHECK_SEND_NO).trigger('change');
        console.log("全取消报送标志为:"+self.getBSBZ());
    });

    

}


//------------show and hide-------------
BatchQueryInvoiceCondDialog.prototype.show = function () {
    console.log("dialog id is:" + this.dialogId);
    DialogUtil.openDialogFn(this.dialogId);
}

BatchQueryInvoiceCondDialog.prototype.close = function () {
    console.log("close the dialog!");
    DialogUtil.closeDialogFn(this.dialogId);
}

//------------getter and setter------------

//set title
BatchQueryInvoiceCondDialog.prototype.setTitle = function (title) {
    //define selector constants of title 
    const DIALOG_TITLE = "#" + this.dialogId + " " + ".dialog-title"; //title component selector
    this.title = title;
    //console.log("title is:"+title);
    //set title's value
    $(DIALOG_TITLE).html(this.title);
}

//-----------------开票日期起止 getter and setter---------------

//get 开票日期起
BatchQueryInvoiceCondDialog.prototype.getKPRQQ = function () {
    const QUERY_DATE_START = "#" + this.dialogId + " " + ".query-date-start"; //selector    
    var returnValue = $(QUERY_DATE_START).val();
    return returnValue;
}

//set queryDateStart
//set 开票日期起
BatchQueryInvoiceCondDialog.prototype.setKPRQQ = function (dateStart) {
    const QUERY_DATE_START = "#" + this.dialogId + " " + ".query-date-start"; //selector    
    this.KPRQQ = dateStart;
    $(QUERY_DATE_START).val(dateStart);
}

//get 开票日期止
BatchQueryInvoiceCondDialog.prototype.getKPRQZ = function () {
    const QUERY_DATE_END = "#" + this.dialogId + " " + ".query-date-end"; //selector    
    var returnValue = $(QUERY_DATE_END).val();
    return returnValue;
}

//set 开票日期止
BatchQueryInvoiceCondDialog.prototype.setKPRQZ = function (dateEnd) {
    const QUERY_DATE_END = "#" + this.dialogId + " " + ".query-date-end"; //selector    
    this.KPRQZ = dateEnd; //属性值
    $(QUERY_DATE_END).val(dateEnd);
}

//-------------------购方名称及税号条件---------------------
//get 购方税号  buyer tax no  
BatchQueryInvoiceCondDialog.prototype.getGFSH = function () {
    const BUYER_TAX_NO = "#" + this.dialogId + " " + ".buyer-tax-no"; //selector    
    var returnValue = $(BUYER_TAX_NO).val();
    return returnValue;
}

//set 购方税号
BatchQueryInvoiceCondDialog.prototype.setGFSH = function (buyerTaxNo) {
    const BUYER_TAX_NO = "#" + this.dialogId + " " + ".buyer-tax-no"; //selector    
    this.GFSH = buyerTaxNo;
    $(BUYER_TAX_NO).val(this.buyerTaxNo);
}


//get  购方名称  
BatchQueryInvoiceCondDialog.prototype.getGFMC = function () {
    const BUYER_NAME = "#" + this.dialogId + " " + ".buyer-name"; //selector    
    var returnValue = $(BUYER_NAME).val();
    return returnValue;
}

//set 购方名称
BatchQueryInvoiceCondDialog.prototype.setGFMC = function (buyerName) {
    const BUYER_NAME = "#" + this.dialogId + " " + ".buyer-name"; //selector    
    this.GFMC = buyerName;
    $(BUYER_NAME).val(this.salerTaxNo);
}

//----------发票代码,发票号码,发票种类  getter and setter---------

//get 发票代码
BatchQueryInvoiceCondDialog.prototype.getFPDM = function () {
    const INVOICE_CODE = "#" + this.dialogId + " " + ".invoice-code"; //selector    
    var returnValue = $(INVOICE_CODE).val();
    return returnValue;
}
//set 发票代码
BatchQueryInvoiceCondDialog.prototype.setFPDM = function (fpdm) {
    const INVOICE_CODE = "#" + this.dialogId + " " + ".invoice-code"; //selector    
    this.FPDM = fpdm;
    $(INVOICE_CODE).val(fpdm);
}

//get 发票号码 
BatchQueryInvoiceCondDialog.prototype.getFPHM = function () {
    const INVOICE_NO = "#" + this.dialogId + " " + ".invoice-code"; //selector 
    var returnValue = $(INVOICE_NO).val();
    return returnValue;
}

//set 发票号码 
BatchQueryInvoiceCondDialog.prototype.setFPHM = function (fphm) {
    const INVOICE_NO = "#" + this.dialogId + " " + ".invoice-code"; //selector    
    this.FPHM = fphm;
    $(INVOICE_NO).val(fphm);
}

//get 发票种类
BatchQueryInvoiceCondDialog.prototype.getFPZL = function () {
    const INVOICE_TYPE = "#" + this.dialogId + " " + ".invoice-type"; //selector 
    var returnValue = $(INVOICE_TYPE).val();
    return returnValue;
}


//--------------设置按钮回调函数------------

//set buttonOk's callback function
BatchQueryInvoiceCondDialog.prototype.setButtonOkCallback = function (callback) {
    this.buttonOkCallback = callback;
}

//set buttonCancel's callback function
BatchQueryInvoiceCondDialog.prototype.setButtonCancelCallback = function (callback) {
    this.buttonCancelCallback = callback;
}

//-------------作废标志,打印标志,报送标志----------
//get 作废标志
BatchQueryInvoiceCondDialog.prototype.getZFBZ = function () {
    return this.ZFBZ;
}

//get 打印标志
BatchQueryInvoiceCondDialog.prototype.getDYBZ = function () {
    return this.DYBZ;
}

//get 报送标志
BatchQueryInvoiceCondDialog.prototype.getBSBZ = function () {
    return this.BSBZ;
}


/**
 * 计算作废标志
 * 返回值:
 * 	固定值
		0：未作废
		1：已作废
		空为全部
 */
BatchQueryInvoiceCondDialog.prototype.calcZFBZ = function () {
    //此函数应该为check box changed event 处理函数 
    const CHECK_INVALID_NO = "#" + this.dialogId + " " + ".check-invalid-no"; //selector
    const CHECK_INVALID_YES = "#" + this.dialogId + " " + ".check-invalid-yes"; //selector

    var invalidNo = $(CHECK_INVALID_NO).prop("checked");
    //console.log("作废标志1:"+invalidNo);
    var invalidYes = $(CHECK_INVALID_YES).prop("checked");
    //console.log("作废标志2:"+invalidYes);

    var resultVal = "";

    //未作废(check)	己作废(check)	返回值
    //	true			true		 ""
    //	true			false	 	"0"
    //	false			true	 	"1"
    //	false			false	 	""


    switch (invalidNo) {
        case true:
            if (invalidYes) {
                resultVal = ""; //未作废:true  已作废:true
            } else {
                resultVal = "0"; //未作废:true  已作废:false
            }
            break;
        case false:
            if (invalidYes) {
                resultVal = "1"; //未作废:false  已作废:true
            } else {
                resultVal = ""; //未作废:false  已作废:false
            }
            break;
    }

    return resultVal;
}


/**
 * 计算打印标志
 * 返回:
 *  固定值
        0：未打印
        1：已打印
        空为全部
 */
BatchQueryInvoiceCondDialog.prototype.calcDYBZ = function () {
    //此函数应该为check box changed event 处理函数 
    const CHECK_PRINT_NO = "#" + this.dialogId + " " + ".check-print-no";   //selector
    const CHECK_PRINT_YES = "#" + this.dialogId + " " + ".check-print-yes"; //selector

    var invalidNo = $(CHECK_PRINT_NO).prop("checked");
    console.log("打印标志1-未打印标志:"+invalidNo);
    var invalidYes = $(CHECK_PRINT_YES).prop("checked");
    console.log("打印标志2-未打印标志:"+invalidYes);

    var resultVal = "";

    //未打印(check)	己打印(check)	返回值
    //	true			true		 ""
    //	true			false	 	"0"
    //	false			true	 	"1"
    //	false			false	 	""


    switch (invalidNo) {
        case true:
            if (invalidYes) {
                resultVal = ""; //未打印:true  已打印:true
            } else {
                resultVal = "0"; //未打印:true  已打印:false
            }
            break;
        case false:
            if (invalidYes) {
                resultVal = "1"; //未打印:false  已打印:true
            } else {
                resultVal = ""; //未打印:false  已打印:false
            }
            break;
    }

    return resultVal;
}

/**
 * 计算报送标志
 * 返回:
 *  固定值
        0：未报送
        1：已报送
        2：报送失败
        3：报送中
        4：验签失败
        空为全部
        
        全选,全不选时均为"" 即查询所有
        否则返回逗号分隔的各状态,形式如下所示(所有情形):
        x
        x,y
        x,y,z
        x,y,z,h
        对于非空的情况,需要根据状态值查询多次,而后将集合合并后显示.                            

 */
BatchQueryInvoiceCondDialog.prototype.calcBSBZ = function () {
	//报送标志UI-选择器
    const CHECK_SEND_NO = "#" + this.dialogId + " " + ".check-send-no"; 		//selector  未报送
    const CHECK_SEND_YES = "#" + this.dialogId + " " + ".check-send-yes"; 		//selector    己报送
    const CHECK_SEND_FAIL = "#" + this.dialogId + " " + ".check-send-fail"; 	//selector  报送失败
    const CHECK_SENDING = "#" + this.dialogId + " " + ".check-sending"; 		//selector 报送中
    const CHECK_VALID_FAIL = "#" + this.dialogId + " " + ".check-valid-fail"; 	//selector 验签失败
    
    //发送标志常量
    const SEND_NO="0";
    const SEND_YES="1";
    const SNED_FAIL="2";
    const SENDING="3";
    const VALID_FAIL="4";


    var sendNoFlag = $(CHECK_SEND_NO).prop("checked");
    console.log("未报送标志:"+sendNoFlag);

    var sendYesFlag = $(CHECK_SEND_YES).prop("checked");
    console.log("己报送标志:"+sendYesFlag);

    var sendFailFlag = $(CHECK_SEND_FAIL).prop("checked");
    console.log("报送失败标志:"+sendFailFlag);

    var sendingFlag=$(CHECK_SENDING).prop("checked");
    console.log("正在报送标志:"+sendingFlag);

    var validFailFlag=$(CHECK_VALID_FAIL).prop("checked");
    console.log("验签失败标志:"+validFailFlag);


    var resultVal = "";

    
    //报送标志-未报送
    if(sendNoFlag){resultVal=genBSBZ(resultVal,SEND_NO);  }
    if(sendYesFlag){resultVal=genBSBZ(resultVal,SEND_YES);  }
    if(sendFailFlag){resultVal=genBSBZ(resultVal,SNED_FAIL);  }
    if(sendingFlag){resultVal=genBSBZ(resultVal,SENDING);  }
    if(validFailFlag){resultVal=genBSBZ(resultVal,VALID_FAIL);  }


    //如果是全部选定时,返回""-全部
    if(sendNoFlag && sendYesFlag && sendFailFlag && sendingFlag && validFailFlag){
        resultVal="";
    }

    return resultVal;


    /**
     * 
     * 生成报送标志
     * @param {String} oldFlag 
     * @param {String} newFlag 
     */
    function genBSBZ(oldFlag,newFlag){
        var returnFlag="";
        if(oldFlag==""){
    		returnFlag=newFlag;
    	}    		
    	else{
    		returnFlag=oldFlag+","+newFlag;
        }
        return returnFlag;
    }
    
    
}


