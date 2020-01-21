/**
 * 批次优化-card js script
 */


/**
 * 	功能: 
 * 		查询医嘱执行单下的药单 参数: inpatientNo:住院流水号
 */
function queryDruglist(inpatientNo) {
	var url = BASE_CONTEXT_PATH + "/batchoptimize/queryDruglist"; // 需要提交的url
	var container = "#table-execute-list-druglist-contianer";

	// 组装参数对象.
	var parms = new Object();
	parms.inpatientNo = inpatientNo;

	// 加载患者药单列表.
	$(container).load(url, parms, null);
}

/**
 * 功能:
 * 		以下代码是实现自动显示当前日期的功能函数
 * @returns  当前日期
 */
function today() {
	var today = new Date();
	var h = today.getFullYear();
	var m = today.getMonth() + 1;
	var d = today.getDate();
	m = m < 10 ? "0" + m : m; //  这里判断月份是否<10,如果是在月份前面加'0'
	d = d < 10 ? "0" + d : d; //  这里判断日期是否<10,如果是在日期前面加'0'
	return h + "-" + m + "-" + d;
}

/**
 * 功能:
 * 		设置查询日期为当前日期
 * @returns
 */
function setDefaultDate() {
	var dateComponentId = "#search-date";
	$(dateComponentId).val(today());
}

/**
 * 功能:
 * 		设置Date Component的背景色
 * @param backgroundColor 背景色
 * @returns 无
 * 注:此函数暂时未用.
 */
function setBackGround(backgroundColor) {
	var dateComponentId = "#search-date";
	$(dateComponentId).css("background-color", backgroundColor);	
}

//---------------批次优化---------------
/*
 * 	功能:
 * 		批次优化(优化按钮-click事件处理函数中调用)
 * @returns
 */
function batchOptimize(){
	//收集所选择的患者,或是选择的病区.
	//如果所选择的患者,则发送患者的住院流水号PatientNo
	//如果选择了某个病区,且没有选择患者,只传送病区CODE
	
	//如果用户选择的昨日医嘱
	var yesterdayToggle=$("#btn-yesterday-date").hasClass("active");
	if(yesterdayToggle){
		util.message("昨日医嘱无需优化.",null,"info");		
	}
	else{
		var patientNoArr=getSelectedIds();  //选择的患者	
		var inpatientAreaCode =$("#select-inpatient-area").val();  //选择的病区(下拉框)
		//选择了:患者 OR 病区
		if(patientNoArr.length>0 || inpatientAreaCode!='-1'){
			//当前日期
			var yyrq=$("#search-date").val();
			//console.log(yyrq);
			
			//如果选择的是:明日医嘱toggle
			var tomorrowToggle=$("#btn-tomorrow-date").hasClass("active");
			if(tomorrowToggle){
				yyrq=addDate(yyrq,1);
			}
			
			/*{
				 * 				"yyrq":"2018-10-30",
				 * 				"patientNos":"1,2,3,4",
				 * 				"inpatientAreaCodes="neike,waike"
				 * 			}*/
			
			var parms=new Object();
			
			parms.yyrq=yyrq;
			parms.patientNos=patientNoArr.join(",");
			
			//如果未选择病区,则病区参数返回""字符串
			if(inpatientAreaCode=='-1')
				parms.inpatientAreaCodes="";
			else
				parms.inpatientAreaCodes=inpatientAreaCode;
			
			batchOptimizeRequest(parms);
			
			
		}
		else{
			util.message("未选择患者或病区",null,"info");
		}
		
	}
	
}


/**
 * 功能说明:
 * 	 发送优化请求.
 * @param parms 请求参数
 * 		参数格式:
 * 			{
 * 				"yyrq":"2018-10-30",
 * 				"patientNos":"1,2,3,4",
 * 				"inpatientAreaCodes="neike,waike"
 * 			}
 * 		
 * @returns
 */
function batchOptimizeRequest(parms) {
	var urlStr = BASE_CONTEXT_PATH+"/batchoptimize/optimize"; //提交的url
	
	$.ajax({
		type : "POST", // 提交方式 get/post
		url : urlStr,
		//contentType : "application/json", // 如果采用json格式传送所有参数时必须有,如果采普通js对象传送时,则不可以加此参数
		// dataType : "html", // 表示返回值类型，不必须,如果返回的是面页，则必须
		//data : JSON.stringify(parms),
		data : parms,
		success : function(res) { // res 保存提交后返回的数据，一般为 json对象(与后台相关.)
			// console.log(res);
			if (res != null && res != "") {
				if (res.result_code == "success") {					 
					 util.message(res.result_msg,null,"info"); 
				} else {
					// 参数1:提示信息
					// 参数2:redirect 重定向URL地址
					// 参数3:图标类型 dialog type: success warning info
					// error,默认为info
					util.message(res.result_msg,null,"warning");
				}
			}
		},
		error : function(jqXHR, textStatus, errorThrown) {
			util.message("AJAX请求时发生错误!");
		}
	});
} 


/**
 * 返回所选条目的 id(每行对应一个唯一的ID)
 * 
 * @returns
 */
function getSelectedIds(){
	var idArr = new Array();

	// 扫描用户所选择条目ID
	$(".item-selector").each(function(index, elem) {
		if (this.checked) { // 对于己选条目
			var id = $(this).attr("data-bind"); 
			idArr.push(id);
		}
	}); // end of item-selector
	return idArr;
}

//-------------------查询医嘱执行单列表----------------------

/*
 *	功能:
 *		刷新当前页数据
 *		分页查询时使用
 * 
 */
function pageSearchDoctorAdviceExecuteList(){
	var parms=buildPageSearchParm();
	loadPatientAreaTable(parms);	
}

/*
 * 	功能:
 * 		查询医嘱执行单列表
 * 		查询按钮使用.
 * 
 * 	@param parms  查询参数
 * 	@returns 无
 */
function searchDoctorAdviceExecuteList(){
	var parms=buildSearchParm();
	loadPatientAreaTable(parms)
}

/*
 * 	功能:
 * 		默认查询
 * 		页面初始加载时调用
 */
function searchDefaultDoctorAdviceExecuteList(){
	var parms=buildDefaultSearchParm();
	loadPatientAreaTable(parms);
}


/**
 * 自页面组建查询参数
 * @returns
 */
function buildSearchParm(){
	var parms=new Object();  //参数对象
	
	//病区,使用数组保存病区,以便于扩展
	//如果选择了病区则加入到病区数组中,否则数组为空
	var inpatientAreaCode=$("#select-inpatient-area").val();
	var inpatientAreaCodeArr=new Array();		
	if(inpatientAreaCode!=-1){
		inpatientAreaCodeArr.push(inpatientAreaCode);
	}
	parms.inpatientAreaCodes=inpatientAreaCodeArr.join(',');  //病区代码列表,以","进行分隔
	
	//昨日医嘱toggle
	var yesterdayToggle=$("#btn-yesterday-date").hasClass("active");
	if(yesterdayToggle){
		parms.yesterdayToggle=1;
	}
	else{
		parms.yesterdayToggle=0;
	}
	
	
	//当前日期
	var currentDate=$("#search-date").val();
	parms.currentDate=currentDate;
	
	//明日医嘱toggle
	var tomorrowToggle=$("#btn-tomorrow-date").hasClass("active");
	if(tomorrowToggle){
		parms.tomorrowToggle=1;
	}
	else{
		parms.tomorrowToggle=0;
	}
		
	//药品类型
	var medicCategoryId=$("#select-medic-category").val();
	parms.medicCategory=medicCategoryId;
	
	//批行批次(id)
	// 与后台的zxbc相对应
	var batchId=$("#select-batch").val();
	parms.batch=batchId;
	
	//查询字段
	//select-search-field
	var searchField=$("#select-search-field").val();
	parms.searchField=searchField;
	
	//查询字段值
	//input type="text"的取值仍是采用val()函数
	var searchFieldValue=$("#search-field-value").val();  
	parms.searchFieldValue=searchFieldValue;
	
	//页号
	parms.pageNum=1;  //页号为首页
	
	//页大小
	parms.pageSize=null;  //页大小由服务器端决定.
	
	return parms;
}

/**
 * 组建页查询参数
 * @returns
 */
function buildPageSearchParm(){
	//参数准备
	curr_searchBean.pageNum=curr_pageNum;
	curr_searchBean.pageSize=curr_pageSize;
	
	return curr_searchBean;
	
}

/**
 * 组建默认查询参数
 * @returns
 */
function buildDefaultSearchParm(){
	var parms=new Object();  //参数对象
	
	//病区,使用数组保存病区,以便于扩展
	//如果选择了病区则加入到病区数组中,否则数组为空	
	var inpatientAreaCodeArr=new Array();		
	parms.inpatientAreaCodes=inpatientAreaCodeArr.join(',');  //病区代码列表,以","进行分隔
	
	//昨日医嘱
	parms.yesterdayToggle=0;
	
	//当前日期
	var currentDate=$("#search-date").val();
	parms.currentDate=currentDate;
	
	//明日医嘱toggle
	parms.tomorrowToggle=0;
	
		
	//药品类型	
	parms.medicCategory=-1;
	
	//批行批次(id)
	// 与后台的zxbc相对应	
	parms.batch=-1;
	
	//查询字段
	parms.searchField=-1;   //查询字段:未选
	
	//查询字段值		
	parms.searchFieldValue="";  //查询字段值为空
	
	//页号
	parms.pageNum=1;  		//页号
	
	//页大小
	parms.pageSize=null;  	//页大小不定,则服务器端决定.
	
	return parms;
}


/**
 * 	功能: 
 * 		加载医嘱执行单-患者列表
 */
function loadPatientAreaTable(parms) {
	var url = BASE_CONTEXT_PATH + "/batchoptimize/searchPatientAreaTable"; // 需要提交的url
	var container = "#table-patient-area-container";

	var requestParms = new Object();			
	requestParms.parms=JSON.stringify(parms);
	
	$(container).load(url, requestParms, null);
}


//--------------------INIT FUNCTION---------------------

/**
 * 初始化
 */
function init(){
	setDefaultDate();  //设置默认时间  
	searchDefaultDoctorAdviceExecuteList(); // 加载医嘱执行单---患者列表
}

// -------------------PAGE LOADED READY-----------------
$(function() {
	
	// -----------事件绑定(EVENT BINDING)--------------
	
	/*
	 * 	查询按钮-CLICK EVENT BINDING
	 * 	TODO need implement
	 */
	$('#btn-search').on('click',function(){
		var parms=buildSearchParm();
		searchDoctorAdviceExecuteList(parms);
				
	});
	
	/*
	 * 	查询条件清除按钮-CLICK EVENT BINDING
	 */
	$('#btn-clear').on('click',function(){
		var searchCondInputId="#search-field-value";
		$(searchCondInputId).val("");
	});
	
	
	
	/*
	 * 	优化按钮-click event binding
	 * 	TODO need implement
	 */
	$('#btn-optimize').on('click',function(){
		batchOptimize();
	});	
	
	
	/*
	 *	功能:
	 *		明日医嘱-toggle按钮-click event binding
	 *	逻辑:
	 *		做为一个toggle来处理 
	 */
	$(".batch-optimize-search-panel #btn-tomorrow-date").on("click",function(){
		
		//取得按钮的选定状态
		var actived=$(this).hasClass("active");
		//如果原来是激活状态
		if(actived){ 
			$(this).removeClass("active");
			$(this).find(".check-icon").addClass("hide-check-icon"); //不显示选中图标
			$(this).find(".uncheck-icon").removeClass("hide-uncheck-icon"); //显示未选中图标
		}
		//如果是未激活状态
		else{
			//激活当前按钮
			$(this).addClass("active");
			$(this).find(".check-icon").removeClass("hide-check-icon"); //显示选中图标
			$(this).find(".uncheck-icon").addClass("hide-uncheck-icon"); //不显示未选中图标
			
			//如果另一按钮是激活状态.置为非激活状态
			var otherBtnSelector=".batch-optimize-search-panel #btn-yesterday-date";
			var otherBtn=$(otherBtnSelector);
			var otherBtnActived=$(otherBtn).hasClass("active");
			if(otherBtnActived){
				$(otherBtn).removeClass("active");
				$(otherBtn).find(".check-icon").addClass("hide-check-icon");  //不显示选中图标
				$(otherBtn).find(".check-icon").removeClass("hide-uncheck-icon");  //显示未选中图标
			}
			
								
		}
		
	});
	
	/*
	 *	功能:
	 *		昨日医嘱-toggle按钮-click event binding
	 *	逻辑:
	 *		做为一个toggle来处理 
	 */
	$(".batch-optimize-search-panel #btn-yesterday-date").on("click",function(){
		
		//取得按钮的选定状态
		var actived=$(this).hasClass("active");
		//如果原来是激活状态
		if(actived){ 
			$(this).removeClass("active");
			$(this).find(".check-icon").addClass("hide-check-icon"); //不显示选中图标
			$(this).find(".uncheck-icon").removeClass("hide-uncheck-icon"); //显示未选中图标
		}
		//如果是未激活状态
		else{
			//激活当前按钮
			$(this).addClass("active");
			$(this).find(".check-icon").removeClass("hide-check-icon"); //显示选中图标
			$(this).find(".uncheck-icon").addClass("hide-uncheck-icon"); //不显示未选中图标
			
			//如果另一按钮是激活状态.置为非激活状态
			var otherBtnSelector=".batch-optimize-search-panel #btn-tomorrow-date";
			var otherBtn=$(otherBtnSelector);
			var otherBtnActived=$(otherBtn).hasClass("active");
			if(otherBtnActived){
				$(otherBtn).removeClass("active");
				$(otherBtn).find(".check-icon").addClass("hide-check-icon");  //隐藏选中
				$(otherBtn).find(".uncheck-icon").removeClass("hide-uncheck-icon");  //显示未选中状态
			}		
			
								
		}
		
	});
	
	
	
	
	
	
	//---------------INITIALIZE----------------
	init();  //"执行"初始化
	

});