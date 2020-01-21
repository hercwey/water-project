/**
 * current : 当前行指示器所对应的td
 */
function displayIndicator() {
	var col = "table.table-patient-area tbody tr.current td.indicator";
	var indicator = "<i class='fa fa-caret-right'></i>";
	hideAllIndicator();
	$(col).append(indicator);
}

/**
 * 	隐藏所有的指示器
 */
function hideAllIndicator() {
	$("table.table-patient-area tbody tr td.indicator i").remove();
}

/**
 * 	功能:
 * 		列表加载完成后默认选中第一条数据
 * 		如果没有查询到数据时,清除药单列表
 */
function selectFirstRow(){
	const tableBodySelector="table.table-patient-area tbody";
	//查询列表中是否有数据
	var firstRow = $(tableBodySelector).find('tr:first');
	//查询到数据,选择第一条
	if(firstRow.length>0){
		firstRow.click();
	}
	//如果没有查询到则清除药单列表
	else{
		$(".table-execute-list-druglist tbody tr").empty();
	}
}


//-------------------init funciton-------------------

/**
 *	初始化函数 
 * @returns
 */
function init(){
	selectFirstRow(); //选择第一行.
}


$(function() {

	//-------------------EVENT BINDING--------------------
	
	/*
	 * 	功能:
	 *  	当点击某医嘱执行单条目时,显示此患者下的药单
	 */ 
	$("table.table-patient-area tbody tr").on('click',function() {
		var currRow = "table.table-patient-area tbody tr.current";
		var currentDoctorAdvice = $(currRow + ":first").get(0);
		if (currentDoctorAdvice != null) {
			if ($(currentDoctorAdvice).attr(
					"data-bind-inpatient-no") == $(this).attr(
					"data-bind-inpatient-no")) { // 如果点击的是同一行,则不再刷新此医嘱下的药单.
				// console.log("row is returned!");
				return;
			}
		}

		// console.log("row is clicked!!!!");
		// 当前光条
		$(currRow).removeClass("current");
		$(this).addClass("current");
		// 显示指示器
		displayIndicator();

		var inpatientNo = $(this).attr("data-bind-inpatient-no"); // 住院流水号
		// console.log("row clicked!.......");
		queryDruglist(inpatientNo);
	});

	/*
	 * 	选择/取消选择 当前页记录 select all or deselect all
	 */
	$("#select-all-toggle").on('click', function(e) {
		var prefix = "item-selector-";

		if (this.checked) {
			// 查询列表中的项目
			$(".item-selector").each(function(index, elem) {
				this.checked = true;
				var surfix = $(this).attr("data-bind"); // item'id
				var rowId = prefix + surfix;
				$('#' + rowId).addClass("selected");
			});
		} else {
			$(".item-selector").each(function(index, elem) {
				this.checked = false;
				var surfix = $(this).attr("data-bind");
				var rowId = prefix + surfix;
				$('#' + rowId).removeClass("selected");
			});
		} // end of else
	});
	
	
	//========分页（页码导航）========

	/*
	 	【分页】导航： 当点击页号时读取需要导航到的页码及每页记录数（pageNum,pageSize）
	 	 data-bind:pageNum-pageSize形式 
	 	 如果data-bind为空字符串，不做动作 
	 	 当用户点击某页时，	 则发送请求
	*/
	$(".pagination li a").on('click', function(e) {
		var dataBind = $(this).attr("data-bind");
		//当dataBind有数据时处理
		if (dataBind != "") {
			var pageArr = dataBind.split("-");
			// 置分页数据
			curr_pageNum = pageArr[0];
			curr_pageSize = pageArr[1];
			//加载页			
			pageSearchDoctorAdviceExecuteList(); 
		}
	});
	
	
	
	//-----------------INIT-------------------
	init();
	

});