/**
 * 作废发票进度对话框组件  JS部分
 * 参数:
 * 	dialogId:对话框ID
 */
function InvalidInvoiceProgressDialog(dialogId){
	this.dialogId=dialogId;  //对话框id
	this.title=""; 		 	 //title
	this.message=""; 		 //message
	
	this.invoiceType=""; 	 //发票类型
	this.invoiceDM="";  	 //发票代码
	this.invoiceHM="";  	 //发票号码

	this.refreshValue="100";  //进度条刷新速度

	this.setDefault();  	//设置默认值	
}

/**
 * 设置默认值(title and message)
 */
InvalidInvoiceProgressDialog.prototype.setDefault=function(){
	//set default value of title and message
	this.title="未开发票作废过程";
	this.message=this.genMessage();
	
	this.setTitle(this.title);
	this.setMessage(this.message);	
}

//------------打开,关闭对话框-----------------
InvalidInvoiceProgressDialog.prototype.show=function(){
	console.log("dialog id is:"+this.dialogId);
	DialogUtil.openDialogFn(this.dialogId);
}

InvalidInvoiceProgressDialog.prototype.close=function(){
	DialogUtil.closeDialogFn(this.dialogId);
}

/**
 * 启动进度条
 */
InvalidInvoiceProgressDialog.prototype.startProgress=function(){
	var self=this;

	const PROGRESS_BAR="#"+this.dialogId+" "+".progress-bar";  		//progress bar component selector	

	// 模拟进度条：百分数增加，0-30时为红色，30-60为黄色，60-90为蓝色，>90为绿色
	var value = 0;
	setInterval(function(e){		
		if (value < 100) {
			value = parseInt(value) + 1;
			$(PROGRESS_BAR).css("width", value + "%").text(value + "%");
			if (value>=0 && value<=30) {
				$(PROGRESS_BAR).addClass("bg-danger");
		    } else if (value>=30 && value <=60) {
		    	$(PROGRESS_BAR).removeClass("bg-danger");
		        $(PROGRESS_BAR).addClass("bg-warning");
		    } else if (value>=60 && value <=90) {
		        $(PROGRESS_BAR).removeClass("bg-warning");
		        $(PROGRESS_BAR).addClass("bg-info");
		    } else if(value >= 90 && value<100) {
		        $(PROGRESS_BAR).removeClass("bg-info");
		        $(PROGRESS_BAR).addClass("bg-success");    
		    }
		}
		else{
			value=0;
		}
	}, self.refreshValue);

}


//----------------对话框属性设置(字符串属性)------------
/**
 * set title
 */
InvalidInvoiceProgressDialog.prototype.setTitle=function(title){
	//define selector constants of title and message
	const DIALOG_TITLE="#"+this.dialogId+" "+".dialog-title";  		//title component selector	
	this.title=title;
	//set title,message's value
	$(DIALOG_TITLE).html(this.title);
	
}

/**
 * set message
 */
InvalidInvoiceProgressDialog.prototype.setMessage=function(message){
	const DIAOG_MESSAGE="#"+this.dialogId+" "+".dialog-message";  	//message component selector
	this.message=message;
	$(DIAOG_MESSAGE).html(this.message);
}


/**
 * set invoice type
 * 设置发票类型
 * 	取值:普通发票  专用发票
 */
InvalidInvoiceProgressDialog.prototype.setInvoiceType=function(invoiceType){
	const INVOICE_TYPE="#"+this.dialogId+" "+".invoice-type";
	this.invoiceType=invoiceType;
	$(INVOICE_TYPE).html(this.invoiceType);
}
/**
 * set invoice dm
 * 设置发票代码
 */
InvalidInvoiceProgressDialog.prototype.setInvoiceDM=function(invoiceDM){	
	this.invoiceDM=invoiceDM;	
	this.message=this.genMessage();
	this.setMessage(this.message);
}

/**
 * set invoice hm
 * 设置发票号码
 */
InvalidInvoiceProgressDialog.prototype.setInvoiceHM=function(invoiceHM){
	this.invoiceHM=invoiceHM;
	this.message=this.genMessage();
	this.setMessage(this.message);
}

/**
 * (DM,HM)--->message
 */
InvalidInvoiceProgressDialog.prototype.genMessage=function(){
	var message="正在作废  发票代码:"+"<strong>"+this.invoiceDM+"</strong>"+" "
					    +"发票号码:"+"<strong>"+this.invoiceHM+"</strong>";
	return message;
}

/**
 * 设置进度条的刷新速度
 * refreshValue:刷新的毫秒数，时间越短刷新的速度越快。
 */
InvalidInvoiceProgressDialog.prototype.setRefreshValue=function(refreshValue){
	this.refreshValue=refreshValue;
}
