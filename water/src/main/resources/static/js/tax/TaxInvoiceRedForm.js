function TaxInvoiceRedForm(containerId, callback){
	//发票冲红对话框
	this.containerId = containerId;
	this.callback = callback;
	this.taxInvoiceRedDialog = null;
}


//-------------------PAGE LOADED READY--------------------

/*
* 	加载专用发票红字信息表信息
*/
TaxInvoiceRedForm.prototype.loadPage=function(taxInvoiceId, taxInvoiceRedDialog) {
	var self = this;
	self.taxInvoiceRedDialog = taxInvoiceRedDialog;
	const TABLE_ID = "#"+self.containerId+" .invoice-red-table";
	var url = BASE_CONTEXT_PATH + "/tax-invoice-red/load-tax-red-table";
	var parms = new Object();
	parms.taxInvoiceId = taxInvoiceId;
	$(TABLE_ID).load(url, parms, resizeFuncx);
	function resizeFuncx(){
		self.initValidate();
		self.initListener();
	}
}

TaxInvoiceRedForm.prototype.save=function() {
	var self = this;
	var FORM_ID = "#"+self.containerId+" #tax-invocie-red-form";
	var urlStr = BASE_CONTEXT_PATH + "/tax-invoice-red/save";
	var form=new FormData($(FORM_ID)[0]);
	$.ajax({
		type : "POST", // 提交方式 get/post
		url : urlStr,
		processData:false,
 		contentType:false,
		data : form,
		success : function(res) { // data 保存提交后返回的数据，一般为 json 数据
			if (res != null && res != "") {
				if (res.result_code == "success") {
					var data = new Object();
					data.success = true;
					//关闭对话框
					self.taxInvoiceRedDialog.close();
					self.callback(data);
				} else {
					return false;
				}
			}
		},
		error : function(jqXHR, textStatus, errorThrown) {
			util.message("AJAX请求时发生错误!");
			return false;
		}
	});
}

TaxInvoiceRedForm.prototype.initValidate = function() {// 验证表单
	
	var self = this;
	var FORM_ID = "#" + self.containerId + " #tax-invocie-red-form";
	$.validator.setDefaults({// 验证成功后，保存数据
		submitHandler : function() {
			self.save();
		}
	});

	console.log("------form id is :"+FORM_ID);
	
	$(FORM_ID).validate({// 设置正则验证
		rules : {
			
			xxblsh: {
	        	required: true,
	        },
		},
		messages : {
			propEmail : "请输入数字",
		}
	});

}


TaxInvoiceRedForm.prototype.initListener=function() {
	var self = this;
	var FORM_ID = "#"+self.containerId+" #tax-invocie-red-form";
	const COMPANY = "#"+self.containerId+" .btn-company-invoice";
	const OTHER = "#"+self.containerId+" .btn-other-invoice";
	const SAVE = "#"+self.containerId+" .btn-save";
	
	const SQFMC = "#"+self.containerId+" #tax-red-info-sqfmc";
	const SQFSH = "#"+self.containerId+" #tax-red-info-sqfsh";
	
	$(SQFMC).on('change', function() {// 申请方名称下拉框改变事件
		var sqfmc = $(this).find("option:selected").val();
		var sqfsh = $(this).find("option:selected").attr("data-tax-no");
		$(SQFSH).val(sqfsh);
	});
	
	$(SAVE).on('click',function(){
		$(FORM_ID).submit();
	});
}


