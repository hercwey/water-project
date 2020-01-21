/**
 * 红字信息表下载查询条件设置对话框-组件JS部分
 */
 function DownloadRedInfoDialog(dialogId){
     
    this.dialogId = dialogId;  //对话框id
    this.title = "";   //dialog title

    this.queryDateStart="";  //起始日期
    this.queryDateEnd="";   //结束日期
    
    this.buyerTaxNo="";  //采购方税号
    this.salerTaxNo="";  //销售方税号
    this.redInfoNo="";  //红字信息表编号

    this.checkSelf=true;  //本企业
    this.checkOther=true;  //其它企业
    
    this.buttonOkCallback=null;    //确定按钮-回调函数
    this.buttonCancelCallback=null;  //取消按钮-回调函数

    //init
    this.setDefault();   //设置属性默认值
    this.initListener(); //初始化按钮click event listener
 }

 //init functions
 DownloadRedInfoDialog.prototype.setDefault=function(){
    var defaultTitle = "红字发票信息表审核结果下载条件设置";
    this.setTitle(defaultTitle);
 }

 //init button click event listener
DownloadRedInfoDialog.prototype.initListener=function(){
    var self=this;

    //define constants
    const BUTTON_OK="#" + self.dialogId + " " + ".button-ok";
    const BUTTON_CANCEL="#" + self.dialogId + " " + ".button-cancel";

    //set buttons' click event listener
    $(BUTTON_OK).on('click',buttonOkListener);
    $(BUTTON_CANCEL).on('click',buttonCancelListener);

    //ok button's click event listener
    function buttonOkListener(){
    	self.close();//close dialog
        //execute callback function
        if(self.buttonOkCallback!=null){
            self.buttonOkCallback();
        }

    }

    //calcel button's click event listener
    function buttonCancelListener(){
        //execute callback function
        if(self.buttonCancelCallback!=null){
            self.buttonCancelCallback();
        }
    }


}


 //------------show and hide-------------
 DownloadRedInfoDialog.prototype.show=function(){
	console.log("dialog id is:"+this.dialogId);
	DialogUtil.openDialogFn(this.dialogId);
}

DownloadRedInfoDialog.prototype.close=function(){
    console.log("close the dialog!");
	DialogUtil.closeDialogFn(this.dialogId);
}

 //------------getter and setter------------

 //set title
 DownloadRedInfoDialog.prototype.setTitle = function (title) {
    //define selector constants of title 
    const DIALOG_TITLE = "#" + this.dialogId + " " + ".dialog-title";  		//title component selector
    this.title = title;
    //console.log("title is:"+title);
    //set title's value
    $(DIALOG_TITLE).html(this.title);
}

//get query date of starting  填开日期始 getter and setter
DownloadRedInfoDialog.prototype.getQueryDateStart=function(){
    const QUERY_DATE_START = "#" + this.dialogId + " " + ".query-date-start";  		//selector    
    var returnValue=$(QUERY_DATE_START).val();
    return returnValue;
}

//set queryDateStart
DownloadRedInfoDialog.prototype.setQueryDateStart=function(dateStart){
    const QUERY_DATE_START = "#" + this.dialogId + " " + ".query-date-start";  		//selector    
    this.queryDateStart=dateStart;
    $(QUERY_DATE_START).val(dateStart);
}


//get query date of ending  填开日期止  getter and setter
DownloadRedInfoDialog.prototype.getQueryDateEnd=function(){
    const QUERY_DATE_END = "#" + this.dialogId + " " + ".query-date-end";  		//selector    
    var returnValue=$(QUERY_DATE_END).val();
    return returnValue;
}

//set queryDateEnd
DownloadRedInfoDialog.prototype.setQueryDateEnd=function(dateEnd){
    const QUERY_DATE_END = "#" + this.dialogId + " " + ".query-date-end";  		//selector    
    this.queryDateEnd=dateEnd;  //属性值
    $(QUERY_DATE_END).val(dateEnd);    
}

//get buyer tax no  购方税号  getter and setter
DownloadRedInfoDialog.prototype.getBuyerTaxNo=function(){
    const BUYER_TAX_NO = "#" + this.dialogId + " " + ".buyer-tax-no";  		//selector    
    var returnValue=$(BUYER_TAX_NO).val();
    return returnValue;
}

DownloadRedInfoDialog.prototype.setBuyerTaxNo=function(buyerTaxNo){
    const BUYER_TAX_NO = "#" + this.dialogId + " " + ".buyer-tax-no";  		//selector    
    this.buyerTaxNo=buyerTaxNo;
    $(BUYER_TAX_NO).val(this.buyerTaxNo);
}


//get saler tax no  售方税号  getter and setter
DownloadRedInfoDialog.prototype.getSalerTaxNo=function(){
    const SALER_TAX_NO = "#" + this.dialogId + " " + ".saler-tax-no";  		//selector    
    var returnValue=$(SALER_TAX_NO).val();
    return returnValue;
}

DownloadRedInfoDialog.prototype.setSalerTaxNo=function(salerTaxNo){
    const SALER_TAX_NO = "#" + this.dialogId + " " + ".saler-tax-no";  		//selector    
    this.salerTaxNo=salerTaxNo;
    $(SALER_TAX_NO).val(this.salerTaxNo);    
}

//get redinfo no  红字信息表编号  getter and setter
DownloadRedInfoDialog.prototype.getRedInfoNo=function(){
    const RED_INFO_NO = "#" + this.dialogId + " " + ".red-info-no";  		//selector    
    var returnValue=$(RED_INFO_NO).val();
    return returnValue;
}

DownloadRedInfoDialog.prototype.setRedInfoNo=function(redInfoNo){
    const RED_INFO_NO = "#" + this.dialogId + " " + ".red-info-no";  		//selector    
    this.redInfoNo=redInfoNo;
    $(RED_INFO_NO).val(redInfoNo);    
}


//get check self 
DownloadRedInfoDialog.prototype.getCheckSelf=function(){
    const CHECK_SELF = "#" + this.dialogId + " " + ".check-self";  		//selector 
    var returnValue=$(CHECK_SELF).prop("checked");    
    return returnValue;
}

//get check other
DownloadRedInfoDialog.prototype.getCheckOther=function(){
    const CHECK_OTHER = "#" + this.dialogId + " " + ".check-other";  		//selector    
    var returnValue=$(CHECK_OTHER).prop("checked");
    return returnValue;
}

//set buttonOk's callback function
DownloadRedInfoDialog.prototype.setButtonOkCallback=function(callback){
    this.buttonOkCallback=callback;
}

//set buttonCancel's callback function
DownloadRedInfoDialog.prototype.setButtonCancelCallback=function(callback){
    this.buttonCancelCallback=callback;
}

//------------get query condition object---业务相关-----------

//填开日期起
DownloadRedInfoDialog.prototype.getTKRQQ=function(){
    const QUERY_DATE_START = "#" + this.dialogId + " " + ".query-date-start";  		//selector    
    var returnValue=$(QUERY_DATE_START).val();
    return returnValue;
}

//填开日期止
DownloadRedInfoDialog.prototype.getTKRQZ=function(){
    const QUERY_DATE_END = "#" + this.dialogId + " " + ".query-date-end";  		//selector    
    var returnValue=$(QUERY_DATE_END).val();
    return returnValue;
}


//购方税号
DownloadRedInfoDialog.prototype.getGFSH=function(){
    const BUYER_TAX_NO = "#" + this.dialogId + " " + ".buyer-tax-no";  		//selector    
    var returnValue=$(BUYER_TAX_NO).val();
    return returnValue;
}

//购方税号
DownloadRedInfoDialog.prototype.getXFSH=function(){
    const SALER_TAX_NO = "#" + this.dialogId + " " + ".saler-tax-no";  		//selector    
    var returnValue=$(SALER_TAX_NO).val();
    return returnValue;
}

//红字信息表编号
DownloadRedInfoDialog.prototype.getXXBBH=function(){
    const RED_INFO_NO = "#" + this.dialogId + " " + ".red-info-no";  		//selector    
    var returnValue=$(RED_INFO_NO).val();
    return returnValue;
}

//信息表范围  XXBFW
DownloadRedInfoDialog.prototype.getXXBFW=function(){
    const CHECK_SELF = "#" + this.dialogId + " " + ".check-self";  		//selector 
    var checkSelf=$(CHECK_SELF).prop("checked");    

    //get check other
    const CHECK_OTHER = "#" + this.dialogId + " " + ".check-other";  		//selector    
    var checkOther=$(CHECK_OTHER).prop("checked");

    var resultValue=-1;

    /*
        checkSelf checkOther  return value  含义
            true    true            0       全部
            true    false           1       本企业申请
            false   true            2       其它企业申请
            false   false           -1      均未选时(此时所返回的值为业务非法值)
    */

    switch(checkSelf){
        case true:
            if(checkOther){    //true
                resultValue=0;
            }
            else{  //false
                resultValue=1;
            }
            break;
        case false:
            if(checkOther){   //true
                resultValue=2;
            }
            else{   //false
                resultValue=-1;
            }
            break;
    }  

    return resultValue;

}


