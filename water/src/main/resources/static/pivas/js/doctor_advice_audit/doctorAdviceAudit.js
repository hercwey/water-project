
	//global variable
	var g_OperateType=0;  //操作类型 取以下四个常量之一
	const OPERATE_SINGLE_QUERY=1;  	//单医嘱质疑
	const OPERATE_SINGLE_NOPASS=2; 	//单医嘱拦截
	const OPERATE_MULT_QUERY=11;  	//多医嘱质疑
	const OPERATE_MULT_NOPASS=12; 	//多医嘱拦截 

	/*
		功能:
			单条医嘱-审核确认通过
		参数:
			doctorAdviceMainId 医嘱自增id (医嘱主表)
	*/
	function singleModePass(doctorAdviceMainId){
		var ids=new Array();
		ids.push(doctorAdviceMainId);
		auditBatchPass(ids);
	}
		
	/*
	 *
	 * 功能说明: 删除所选条目(通用删除函数) 参数说明: ids:所要删除的条目ID数组,(可使函数更具通用性.)
	 */
	//private
	function auditBatchPass(ids) {
		var urlStr = BASE_CONTEXT_PATH+"/doctoradviceaudit/audiBatchPass"; // 需要提交的url
		
		$.ajax({
			type : "POST", // 提交方式 get/post
			url : urlStr,
			contentType : "application/json", // 如果采用json格式传送所有参数时必须有,如果采普通js对象传送时,则不可以加此参数
			// dataType : "html", // 表示返回值类型，不必须,如果返回的是面页，则必须
			data : JSON.stringify(ids),
			success : function(res) { // res 保存提交后返回的数据，一般为 json对象(与后台相关.)
				// console.log(res);
				if (res != null && res != "") {
					if (res.result_code == "success") {
						 refreshDoctorAdviceMainCurrentPage();
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
	
	
	var g_currentDoctorAdviceMainId=0;  //用户当前选择的医嘱id(绿色显示的条目)
	
	//刷新医嘱当前页数据,成功后回调函数	
	function refreshDoctorAdviceMainCurrentPage(){
		
		//保存用户刷新前所选择的条目id
		var currentDoctorAdvice=$("table.table-doctor-advice-main tbody tr.current:first").get(0);
		if(currentDoctorAdvice!=null){
			g_currentDoctorAdviceMainId=$(currentDoctorAdvice).attr("data-id");
		}		
		
		//刷新当前页数据
		pageSearchDoctorAdviceMainTable();		
	}
	
	
	/**
	 * 自页面收集查询参数
	 * @returns  查询对象
	 */
	function buildSearchParmForSearch(){
		var parms=new Object();  //参数对象
		
		//是否需要标识新医嘱		
		var checked=$("#check-new-doctor-advice").prop('checked');     
		if(checked){
			parms.indentifyNew=1;  //标识			
		}
		else{
			parms.indentifyNew=0;  //不标识
		}
		
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
		
		
		//预审状态
		var preAuditStatus=$("#select-preaudit-status").val();
		parms.preAuditStatus=preAuditStatus;
		
		//审核状态
		var auditStatus=$("#select-audit-status").val();
		parms.auditStatus=auditStatus;
		
		//医嘱类型
		var doctorAdviceType=$("#select-doctor-advice-type").val();
		parms.doctorAdviceType=doctorAdviceType;
		
		//药品特性
		var medicalChar=$("#select-medical-char").val();
		parms.medicalChar=medicalChar;
		
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
	 * 构建默认查询参数(页面加载时首次查询)
	 * @returns  JS OBJ对象
	 */
	function buildDefaultSearchParms(){
		var parms=new Object();  //参数对象
		
		//是否标识新医嘱
		parms.indentifyNew=0;  //不标识
		
		//病区,使用数组保存病区,以便于扩展
		//如果选择了病区则加入到病区数组中,否则数组为空
		var inpatientAreaCodeArr=new Array();  //数组为空时:所有病区
		parms.inpatientAreaCodes=inpatientAreaCodeArr.join(",");  //病区代码列表
		
		//昨日医嘱toggle		
		parms.yesterdayToggle=0;  //不选择昨日
		
		//当前日期
		var currentDate=$("#search-date").val();
		parms.currentDate=currentDate;
		
		//明日医嘱toggle		
		parms.tomorrowToggle=0;  //不选择明日
		
		//预审状态
		parms.preAuditStatus=-1;  //预审条件:所有
		
		//审核状态		
		parms.auditStatus=-1;  //审核条件:所有
		
		//医嘱类型
		parms.doctorAdviceType=-1;  //医嘱类型:所有
		
		//药品特性		
		parms.medicalChar=-1;   //药品特性:所有
		
		//查询字段
		//select-search-field		
		parms.searchField=-1;   //查询字段:未选
		
		//查询字段值		
		parms.searchFieldValue="";  //查询字段值为空
		
		//页号
		parms.pageNum=1;  		//页号
		
		//页大小
		parms.pageSize=null;  	//页大小不定,则服务器端决定.
		
		return parms;
	}
	
	/*var g_OperateType=0;  //是
	const OPERATE_SINGLE_QUERY=1;  	//单医嘱质疑
	const OPERATE_SINGLE_NOPASS=2; 	//单医嘱拦截
	const OPERATE_MULT_QUERY=11;  	//多医嘱质疑
	const OPERATE_MULT_NOPASS=12; 	//多医嘱拦截
	
		功能:
		参数:
			reason: 原因列表(数组类型).
					向后台传递时:以逗号进行分隔.例如:  原因1,原因2,原因3,原因4
			errorTypeIds:原因ID(ErrorType)列表(数组类型),
					向后台传递时:以逗号进行分隔.  例如:  1,2,3
					
		注:reason列表中item个数与 errorTypeIds列表中 item个数不一定相同.
		   reason中item可为用户手工录入.
	
	 */	
	function auditQueryOrNoPass(reason,errorTypeIds){
		const NO_AUDIT=0;   //未审核
		const PASS=1;		//审核通过
		const NO_PASS=2;	//审核-拦截	
		const QUERY=3;		//审核-质疑
		
		var auditType=NO_AUDIT;
		
		//关闭输入原因对话框
		if(g_OperateType==OPERATE_SINGLE_QUERY || g_OperateType==OPERATE_MULT_QUERY){
			closeQueryDialog();
			auditType=QUERY;
		}
		if(g_OperateType==OPERATE_SINGLE_NOPASS || g_OperateType==OPERATE_MULT_NOPASS){
			closeNoPassDialog();
			auditType=NO_PASS;
		}
		
		var ids=new Array();
		if(g_OperateType==OPERATE_SINGLE_QUERY || g_OperateType== OPERATE_SINGLE_NOPASS){
			var currentDoctorAdvice=$("table.table-doctor-advice-main tbody tr.current:first").get(0);
			if(currentDoctorAdvice!=null){
				var doctorAdviceMainId=$(currentDoctorAdvice).attr("data-id");
				ids.push(doctorAdviceMainId);
			}
		}
		if(g_OperateType==OPERATE_MULT_QUERY || g_OperateType==OPERATE_MULT_NOPASS){
			var tempIds=getSelectedIds();
			if (tempIds.length>0){
				ids=tempIds;
			}
		}
		
		auditBatchQueryOrNoPass(ids,reason,errorTypeIds,auditType);  //处理质疑或拦截
		
	}
	
	//审核:质疑或拦截
	function auditBatchQueryOrNoPass(ids,reason,errorTypeIds,auditType) {
		var urlStr = BASE_CONTEXT_PATH+"/doctoradviceaudit/auditBatchQueryOrNoPass"; // 需要提交的url
		
		$.ajax({
			type : "POST", // 提交方式 get/post
			url : urlStr,
			//contentType : "application/json", // 如果采用json格式传送所有参数时必须有,如果采普通js对象传送时,则不可以加此参数
			// dataType : "html", // 表示返回值类型，不必须,如果返回的是面页，则必须
			data : {"ids":ids.join(','),   //以逗号分隔各主键
					"reason":reason.join(','),
					"errorTypeIds":errorTypeIds.join(','),
					"auditType":auditType
					},
			success : function(res) { // res 保存提交后返回的数据，一般为 json对象(与后台相关.)
				// console.log(res);
				if (res != null && res != "") {
					if (res.result_code == "success") {
						 refreshDoctorAdviceMainCurrentPage();  //刷新当前页面
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
	
	//预审确认
	function preAuditConfirm(){
		var urlStr = BASE_CONTEXT_PATH+"/doctoradviceaudit/preAuditConfirm"; // 需要提交的url
		
		$.ajax({
			type : "POST", // 提交方式 get/post
			url : urlStr,
			//contentType : "application/json", // 如果采用json格式传送所有参数时必须有,如果采普通js对象传送时,则不可以加此参数
			// dataType : "html", // 表示返回值类型，不必须,如果返回的是面页，则必须
			/*data : {"ids":ids.join(','),   //以逗号分隔各主键
					"reason":reason,
					"auditType":auditType
					},*/
			data:{},
			success : function(res) { // res 保存提交后返回的数据，一般为 json对象(与后台相关.)
				// console.log(res);
				if (res != null && res != "") {
					if (res.result_code == "success") {
						 refreshDoctorAdviceMainCurrentPage();  //刷新当前页面
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
	
	//药品标识  所有的AJAX可以模块化.
	function markCharRequest(id,medicalChar){
		var urlStr = BASE_CONTEXT_PATH+"/doctoradviceaudit/markMedicalChar"; // 需要提交的url
		
		$.ajax({
			type : "POST", // 提交方式 get/post
			url : urlStr,
			//contentType : "application/json", // 如果采用json格式传送所有参数时必须有,如果采普通js对象传送时,则不可以加此参数
			// dataType : "html", // 表示返回值类型，不必须,如果返回的是面页，则必须
			data : {
					"id":id,
					"medicalChar":medicalChar
					},
			success : function(res) { // res 保存提交后返回的数据，一般为 json对象(与后台相关.)
				// console.log(res);
				if (res != null && res != "") {
					if (res.result_code == "success") {
						 refreshDoctorAdviceMainCurrentPage();  //刷新当前页面
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
	
	//取消药品标识
	function cancelMarkCharRequest(id,medicalChar){
		var urlStr = BASE_CONTEXT_PATH+"/doctoradviceaudit/cancelMarkMedicalChar"; // 需要提交的url
		
		$.ajax({
			type : "POST", // 提交方式 get/post
			url : urlStr,
			//contentType : "application/json", // 如果采用json格式传送所有参数时必须有,如果采普通js对象传送时,则不可以加此参数
			// dataType : "html", // 表示返回值类型，不必须,如果返回的是面页，则必须
			data : {
					"id":id,
					"medicalChar":medicalChar  
					},
			success : function(res) { // res 保存提交后返回的数据，一般为 json对象(与后台相关.)
				// console.log(res);
				if (res != null && res != "") {
					if (res.result_code == "success") {
						refreshDoctorAdviceMainCurrentPage();  //刷新当前页面
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
	 *	功能:<br>
	 *		标识医嘱药物特性
	 *	参数:
	 *		medicalChar:药物特性 ,见DoctorAdviceMedicalChar.java常量定义 
	 */					
	function markMedicalChar(medicalChar){
		var currentDoctorAdvice=$("table.table-doctor-advice-main tbody tr.current:first").get(0);
		if(currentDoctorAdvice!=null){
			var id=$(currentDoctorAdvice).attr("data-id");
			//console.log(id+'---------'+medicalChar);
			markCharRequest(id,medicalChar);
		}
		else{
			showTipSingle();
		}
	}
	
	/*
	 *	功能:
	 *		取消标识:医嘱药物特性
	 *	参数:
	 *		medicalChar:药物特性 ,见DoctorAdviceMedicalChar.java常量定义 
	 */
	function cancelMarkMedicalChar(medicalChar){
		var currentDoctorAdvice=$("table.table-doctor-advice-main tbody tr.current:first").get(0);
		if(currentDoctorAdvice!=null){
			var id=$(currentDoctorAdvice).attr("data-id");
			cancelMarkCharRequest(id,medicalChar);
		}
		else{
			showTipSingle();
		}
	}

		/* search-date */
		// 设置查询日期为当前日期
		function setDefaultDate() {			
			var dateComponentId=".doctor-audit-search-panel #search-date"; //date组件Id
			$(dateComponentId).val(today());
		}

		//设置日期组件的背景
		function setBackGround() {
			var dateComponentId=".doctor-audit-search-panel #search-date"; //date组件Id
			$(dateComponentId).css("background-color", "white");
		}
		
		/**
		 * 返回所选条目的 id(每行对应一个唯一的ID)
		 * 
		 * @returns  所选医嘱
		 */
		function getSelectedIds(){
			var idArr = new Array();
	
			// 扫描用户所选择条目ID
			$(".item-selector").each(function(index, elem) {
				if (this.checked) { // 对于己选条目
					var id = $(this).attr("data-bind"); //
					idArr.push(id);
				}
			}); // end of item-selector
			return idArr;
		}
		
		
		/*
		 * 功能: 填充卡片中的内容 参数: doctorAdvice:医嘱对象.
		 */
		function fillCard(doctorAdvice){
			//卡片上方的医嘱组号.
			$("#patient-card-parentno").text(doctorAdvice.parentNo);			
			// 开立医生
			$("#card-doctor-name").text(doctorAdvice.doctorName);
			<!-- 开立时间 -->
			var startTime=dateFtt("yyyy-MM-dd hh:mm:ss",new Date(doctorAdvice.startTime));
			$("#card-start-time").text(startTime);
			
			<!-- 病区 -->
			$("#card-inpatient-area-name").text(doctorAdvice.wardname);
			
			<!-- 结束时间 -->
			var endTime=dateFtt("yyyy-MM-dd hh:mm:ss",new Date(doctorAdvice.endTime));
			$("#card-end-time").text(endTime);
			
			$("#card-patient-name").text(doctorAdvice.patname);
			$("#card-patient-avdp").text(doctorAdvice.avdp);
			<!-- 年龄  -->
			$("#card-patient-old").text(doctorAdvice.age);
			<!--频次  -->
			$("#card-frequency").text(doctorAdvice.freqCode);	
			<!-- 床号 -->
			$("#card-bedno").text(doctorAdvice.bedno);
			<!-- 滴速  -->
			$("#card-dropspeed").text(doctorAdvice.dropspeed);
			$("#card-gender").text(doctorAdvice.sex);
			
			var birthday=dateFtt("yyyy-MM-dd hh:mm:ss",new Date(doctorAdvice.birthday));
			$("#card-birthday").text(birthday);
		
			/* $("#card-diagnosis-result").text(doctorAdvice.diagnosisResult); */			
		}
		
		//清除卡片
		function clearCard(){
			$(".card-var").text("");
		}
		
		//清除医嘱列表下的药单
		function clearDrugList(){
			$(".table-doctor-advice-sub tbody").children("tr").remove();
		}
		
		/*
		 * 功能:加载医嘱下的药单 参数: parentNo:医嘱组号
		 */
		var g_doctorAdvice=null;
		function loadDrugList(parentNo,doctorAdvice){
			// console.log(displayStyle);
			g_doctorAdvice=doctorAdvice;
			//alert("test1");
			//console.log(g_doctorAdvice);
			
			var url = "/doctoradviceaudit/getDrugList/"+parentNo;			
			var containerId = "#table-doctor-advice-sub-container";
			var parms=null;
			var callbackFunc = null;
			loadPage(containerId, url, parms, loadReasonPage);
		}
		
		/**
		 * 隐藏原因面板
		 * @returns
		 */
		function hideReasonPanel(){
			var containerx="#doctor-advice-reason-container";
			$(containerx).addClass("hide-reason-panel");
			
			const CHANGE_HEIGHT=150;  //原因(Reason)面板的高度
			//设置药单表BODY的高度.
			var druglistBodyHeight=getDruglistBodyHeight();			
			var bodySelector="table.table-doctor-advice-sub tbody";
			var newHeight=(druglistBodyHeight+CHANGE_HEIGHT);		
			$(bodySelector).css("height",newHeight+"px");
		}
		
		/**
		 * 功能:			 
			 加载未通过/质疑原因及病区复核内容
			参数:
				doctorAdviceMainId:  医嘱id(主表)
		 * @returns
		 */
		function loadReasonPage(){
			//此容器在doctor_advice_audit_card页中		
			var container="#doctor-advice-reason-container";  
			var url=BASE_CONTEXT_PATH+"/doctoradviceaudit/loadReasonPage";
			
			var doctorAdvice=g_doctorAdvice;
			//console.log("load reason recheck page...."+doctorAdvice);
			//console.log(doctorAdvice);
			if(doctorAdvice.yzshzt==2 || doctorAdvice.yzshzt==3){
				var parms=new Object();			
				parms.doctorAdviceMainId=doctorAdvice.yzMainId;				
				$(container).load(url,parms,null);  //加载不通过/拦截原因panel
			}
			else{				
				hideReasonPanel();  //隐藏Reason面板
			}
			
			
		}
		
		// ---------------------dialog---------------------
		
		// 加载对话框
		
		// 加载[质疑对话框]
		function loadQueryDailog(ids,multOrSingle){
			var url = "/doctoradviceaudit/loadQueryDialog";
			var callbackFunc = showQueryDialog;
			var containerId = "#doctor-advice-audit-dialog-container";
			
			//组装参数
			var parms=new Object;
			parms.doctorAdviceMainIds=ids.join(',');
			parms.multOrSingle=multOrSingle;
			
			loadPage(containerId, url, parms, callbackFunc);
		}
		
		// 显示[质疑对话框]
		function showQueryDialog(){
			// $("modal-container-843533").;
			// console.log("show query dialog.....");
			// 可拖拽
			var dialogId="#modal-container-843533";
			$(dialogId).draggable({
				cursor : "move",
				handle : '.modal-header'
			});
			// 点击对话框外部,不关闭对话框
			$(dialogId).modal({
				backdrop : 'static',
				keyboard : false
			});
		}
		
		/*
		 *  功能: 加载[拦截对话框]
		 *  参数:
		 *  	ids:所选择的医嘱(主)id列表   数据类型:Array
		 *  	multOrSingle:操作类型
		 */
		function loadNoPassDailog(ids,multOrSingle){
			var url = "/doctoradviceaudit/loadNoPassDialog";
			var callbackFunc = showNoPassDialog;
			var containerId = "#doctor-advice-audit-dialog-container";
			
			//组装参数
			var parms=new Object;
			parms.doctorAdviceMainIds=ids.join(',');
			parms.multOrSingle=multOrSingle;	
			
			loadPage(containerId, url, parms, callbackFunc);
		}
		
		// 显示[拦截对话框]
		function showNoPassDialog(){
			var dialogId="#modal-container-843533";
			$(dialogId).draggable({
				cursor : "move",
				handle : '.modal-header'
			});
			// 点击对话框外部,不关闭对话框
			$(dialogId).modal({
				backdrop : 'static',
				keyboard : false
			});
		}
		
		
		// 关闭质疑对话框
		function closeQueryDialog(){
			var dialogId="#modal-container-843533";
			$(dialogId).modal("hide");
		}
		
		// 关闭拦截对话框
		function closeQueryDialog(){
			var dialogId="#modal-container-843533";
			$(dialogId).modal("hide");
		}
		
		function closeNoPassDialog(){
			var dialogId="#modal-container-843533";
			$(dialogId).modal("hide");
		}
		
		//加载默认的医嘱列表(此函数用于测试)
		function loadDefaultDoctorAdviceTalbe(){
			//table-doctor-advice-container
			var url=BASE_CONTEXT_PATH+"/doctoradviceaudit/loadDoctorAdviceAuditTable";
			$("#table-doctor-advice-container").load(url,null,null);
		}
		
		/*
			默认查询
			Retrieve(default)查询(加载默认列表)
		*/
		function defaultLoadPage() {	
			//alert("build default params");
			var parms=buildDefaultSearchParms();  //构建默认的查询参数
			searchDoctorAdviceMain(parms);
		}
		
		/**
		 * 查询医嘱(查询医嘱的主要函数)
		 * 注:服务器接收参数的格式为:
		 * 	(String parms), 其中parms为JSON格式
		 * @param parms  查询参数  JS OBJ
		 * @returns  无
		 */
		function searchDoctorAdviceMain(parms) {

			//初始化请求参数
			var requestParms = new Object();			
			requestParms.parms=JSON.stringify(parms);			
			
			//加载医嘱列表页面
			var url=BASE_CONTEXT_PATH+"/doctoradviceaudit/loadDoctorAdviceAuditTable";
			var containerId="#table-doctor-advice-container";
			$(containerId).load(url, requestParms, null);
		}
		
		function showTipSingle(){
			util.message("请先在医嘱列表中<strong>点击某条医嘱</strong>,<br>而后再进行通过确认.",null,"warning");
		}
		
		function showTipMult(){
			util.message("请点击医嘱前面的checkbox,<br> 而后再进行批量确认.",null,"warning");
		}
		
		/**
		 * 加载工具对话框
		 * @returns
		 */
		function loadToolsDialog(){
			var dialogContainer="#tools-dialog-container";
			var url=BASE_CONTEXT_PATH+"/doctoradviceaudit/loadToolsDialog";
			var parms=new Object();
			
			$(dialogContainer).load(url,parms,displayToolsDialog);
		}
		
		/**
		 * 显示工具对话框
		 * @returns
		 */
		function displayToolsDialog(){
			var dialogId="#modal-container-854264";
			$(dialogId).draggable({
				cursor : "move",
				handle : '.modal-header'
			});
			// 点击对话框外部,不关闭对话框
			$(dialogId).modal({
				backdrop : 'static',
				keyboard : false
			});
		}
		
		//----------------用药注意事项对话框------------------
		/*
		 *功能:
		 *	显示对话框
		 *参数:
		 *	对话框ID 
		 */
		function displayDialog(dialogId){
			//var dialogId="#modal-container-854264";
			$(dialogId).draggable({
				cursor : "move",
				handle : '.modal-header'
			});
			// 点击对话框外部,不关闭对话框
			$(dialogId).modal({
				backdrop : 'static',
				keyboard : false
			});
		}
		
		//打开用药注意事项对话框
		function displayCautionsDialog(){
			var dialogId="#modal-container-321296";
			$(dialogId).draggable({
				cursor : "move",
				handle : '.modal-header'
			});
			// 点击对话框外部,不关闭对话框
			$(dialogId).modal({
				backdrop : 'static',
				keyboard : false
			});
		}
		
		//加载用药注意事项对话框
		function loadCautionsDialog(doctorAdviceMainId){
			var url = BASE_CONTEXT_PATH+"/doctoradviceaudit/loadCautionsDialog";
			var dialogContainer="#cautions-dialog-container";   //对话框容器
			
			var parms=new Object();
			parms.doctorAdviceMainId=doctorAdviceMainId;
			
			//加载用药注意事项对话框,并显示
			$(dialogContainer).load(url,parms,displayCautionsDialog);
		}
		
		//关闭用药注意事项对话框.
		function closeCautionsDialog(){
			var dialogId="#modal-container-321296";
			$(dialogId).modal("hide");
		}
				
		//打开用药注意事项对话框
		function openCautionsDialog(){
			//获取记录指示器所指示的药贴ID
			var currentDoctorAdvice=$("table.table-doctor-advice-main tbody tr.current:first").get(0);
			if(currentDoctorAdvice!=null){
				var doctorAdviceMainId=$(currentDoctorAdvice).attr("data-id");				
				loadCautionsDialog(doctorAdviceMainId);	
			}
			else{
				util.message("请选择一个医嘱执行单条目",null,"info");
			}			
		}
		
		
		// -------------------PAGE LOADED READY--------------------
		$(function() {

			// -------------事件绑定--------------
			
			/* 单条医嘱-审核通过按钮 */
			$("#btn-single-mode-pass").on("click",function(){
				var currentDoctorAdvice=$("table.table-doctor-advice-main tbody tr.current:first").get(0);
				if(currentDoctorAdvice!=null){
					var doctorAdviceMainId=$(currentDoctorAdvice).attr("data-id");
					// console.log("ddddd"+doctorAdviceMainId);
					singleModePass(doctorAdviceMainId);	
				}
				else{
					showTipSingle();
				}
			});			
			
			
			// btn-mult-pass
			$("#btn-mult-pass").on("click",function(){
				var ids=getSelectedIds();
				if (ids.length>0){
					auditBatchPass(ids);	
				}
				else{
					showTipMult();
				}
				
			});
			
			
			/**
			 	单医嘱质疑对话框显示
			    var g_OperateType=0;  //是
				const OPERATE_SINGLE_QUERY=1;  	//单医嘱质疑
				const OPERATE_SINGLE_NOPASS=2; 	//单医嘱拦截
				const OPERATE_MULT_QUERY=11;  	//多医嘱质疑
				const OPERATE_MULT_NOPASS=12; 	//多医嘱拦截
			 */			
			$("#btn-single-mode-query").on("click",function(){
				var currentDoctorAdvice=$("table.table-doctor-advice-main tbody tr.current:first").get(0);
				if(currentDoctorAdvice!=null){
					
					var doctorAdviceMainId=$(currentDoctorAdvice).attr("data-id");
					
					var ids=new Array();
					ids.push(doctorAdviceMainId);
					
					if (ids.length>0){
						loadQueryDailog(ids,OPERATE_SINGLE_QUERY);
						g_OperateType=OPERATE_SINGLE_QUERY;
					}
				}
				else{
					showTipSingle();
				}
				
			});
			
			
			// 单医嘱拦截对话框显示
			$("#btn-single-mode-nopass").on("click",function(){
				var currentDoctorAdvice=$("table.table-doctor-advice-main tbody tr.current:first").get(0);
				if(currentDoctorAdvice!=null){
					var doctorAdviceMainId=$(currentDoctorAdvice).attr("data-id");
					
					var ids=new Array();
					ids.push(doctorAdviceMainId);
					
					if (ids.length>0){
						loadNoPassDailog(ids,OPERATE_SINGLE_NOPASS);
						g_OperateType=OPERATE_SINGLE_NOPASS;
					}
					
					
				}
				else{
					showTipSingle();
				}
			});
			
			
			// 多个医嘱质疑
			$("#btn-mult-query").on("click",function(){
				var ids=getSelectedIds();
				if (ids.length>0){
					loadQueryDailog(ids,OPERATE_MULT_QUERY);
					g_OperateType=OPERATE_MULT_QUERY;
				}
				else{
					showTipMult();
				}
				
			});
			
			// 多个医嘱拦截
			$("#btn-mult-nopass").on("click",function(){
				var ids=getSelectedIds();
				if (ids.length>0){
					loadNoPassDailog(ids,OPERATE_MULT_NOPASS);
					g_OperateType=OPERATE_MULT_NOPASS;
				}
				else{
					showTipMult();
				}				
			});
			
			//
			//预审确认
			$("#btn-mult-preaudit-confirm").on("click",function(){
				preAuditConfirm();
			});
			
			//mark-char
			//医嘱药物特性标识
			$(".mark-char").on("click",function(){
				var medicalChar=$(this).attr("data-bind-value");
				//console.log("sdfsdfsdfsdf--------"+medicalChar);
				markMedicalChar(medicalChar);
			});
			
			//cancel-mark
			//取消医嘱药物特性标识
			$(".cancel-mark").on("click",function(){
				var medicalChar=$(this).attr("data-bind-value");
				cancelMarkMedicalChar(medicalChar);
			});
			
			/**
			 *功能:
			 *		明日用药日期按扭-click事件处理
			 *逻辑:
			 *		做为一个toggle来处理 
			 */
			$(".doctor-audit-search-panel #btn-tomorrow-date").on("click",function(){
				
				/*const dateComponentId=".doctor-audit-search-panel #search-date"; //date组件Id
				const ADD_DAYS=1;
				//(1)取得当前日期,而后加1				
				var datex = new Date();  //当前日期
				var addedDate=addDate(datex,ADD_DAYS)
				//(2)置日期组件的值为新日期 
				$(dateComponentId).val(addedDate);*/
				
				//如果当前按钮为选定状态
				var actived=$(this).hasClass("active");
				//console.log("actived"+actived);
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
					var otherBtnSelector=".doctor-audit-search-panel #btn-yesterday-date";
					var otherBtn=$(otherBtnSelector);
					var otherBtnActived=$(otherBtn).hasClass("active");
					if(otherBtnActived){
						$(otherBtn).removeClass("active");
						$(otherBtn).find(".check-icon").addClass("hide-check-icon");  //不显示选中图标
						$(otherBtn).find(".check-icon").removeClass("hide-uncheck-icon");  //显示未选中图标
					}					
				}
				
			});	
			
			/**
			 *功能:
			 *		昨日用药按扭-click事件处理
			 *逻辑:
			 *		做为一个toggle来处理 
			 */
			$(".doctor-audit-search-panel #btn-yesterday-date").on("click",function(){
				
				//如果当前按钮为选定状态
				var actived=$(this).hasClass("active");
				//console.log("actived"+actived);
				//如果原来是激活状态,设置为示选中状态
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
					$(this).find(".uncheck-icon").addClass("hide-uncheck-icon"); //隐藏未选中图标
					
					//如果另一按钮是激活状态.置为非激活状态
					var otherBtnSelector=".doctor-audit-search-panel #btn-tomorrow-date";
					var otherBtn=$(otherBtnSelector);
					var otherBtnActived=$(otherBtn).hasClass("active");
					if(otherBtnActived){
						$(otherBtn).removeClass("active");
						$(otherBtn).find(".check-icon").addClass("hide-check-icon");  //隐藏选中
						$(otherBtn).find(".uncheck-icon").removeClass("hide-uncheck-icon");  //显示未选中状态
					}					
				}
				
			});	
						
			
			
			/**
			 * 工具按钮-CLICK
			 */
			$("#btn-tools").on("click",function(){
				loadToolsDialog();	
			});
			
			/**
			 * 清除输入条件按钮-CLICK
			 */
			$("#btn-clear").on("click",function(){
				$("#search-field-value").val("");
				$("#search-field-value").focus();
			});
			
			/**
			 * 查询按钮-CLICK
			 */
			$("#btn-search").on("click",function(){
				var parms=buildSearchParmForSearch();
				g_currentDoctorAdviceMainId=0;
				searchDoctorAdviceMain(parms);
			});
			
			//"用药注意事项"按钮click event process function
			$("#btn-cautions").on("click",function(){				
				//display cautions dialog
				openCautionsDialog();
			});
			
			
			
			
			
			// ------------------init------------------
			setDefaultDate();  // 设置当前日期及时间			
			defaultLoadPage(); // 默认加载医嘱页面
			

		});