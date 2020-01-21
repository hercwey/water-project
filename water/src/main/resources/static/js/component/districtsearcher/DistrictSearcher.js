//查询组件JS

/*
 * 
 * 查询组件
 * 
 * 构造函数说明:
 * 	searcherId:  组件ID
 *  changeCallBackFunc:  
 *  	当下拉框有change变化时,用户传入的回调函数(可以为null)
 *        
 */
function DistrictSearcher(searcherIdx,changeCallbackFunc){
	//选择器常量
	this.BLOCK_SELECTOR="#"+searcherIdx+"  .location-block";   		  	//小区
	this.BUILDING_SELECTOR="#"+searcherIdx+"  .location-building";   	//楼号
	this.UNIT_SELECTOR="#"+searcherIdx+"  .location-unit";    	  		//单元

	//对象属性
	this.searcherId=searcherIdx;  			//组件ID
	this.dropdownChangeCallbackFunc=changeCallbackFunc;  	//当下拉框数据有变化时,所调用的外部函数
	
	/*---------------------------------
	 函数功能:
	 	初始化
	 --------------------------------*/
	this.init=function(){
		//console.log("component id is:"+searcherId);
		console.log("init function called!"+this.searcherId);
		initDropdownListener(this.dropdownChangeCallbackFunc, this.BLOCK_SELECTOR,this.BUILDING_SELECTOR,this.UNIT_SELECTOR);  //初始化下拉框change事件监听器		
		getLocationBlock(this.BLOCK_SELECTOR);  //加载小区下拉列表
	}
	
	/*------------------------------
	  	获取小区ID
	 ------------------------------*/
	this.getBlockId = function(){
		return getDropdownValue(this.BLOCK_SELECTOR);
	}
	/*----------------------------
	  	获取小区楼号ID
	 ----------------------------*/
	this.getBuildingId = function(){
		return getDropdownValue(this.BUILDING_SELECTOR);
	}
	/*----------------------------
	  	获取小区楼号单元ID
	 ----------------------------*/
	this.getUnitId = function(){
		return getDropdownValue(this.UNIT_SELECTOR);
	}
	
	/*
	   	获取下拉框中的值
	 */
	getDropdownValue=function(selector){
		const SELECTED="option:selected";
		const ATTR="data-location-traceids";		
		return $(selector).find(SELECTED).attr(ATTR);
	}
	
	/*---------------------------
		reset
	---------------------------*/
	this.reset=function(){
		//设置小区-楼号-单元下拉框为未选择
		$(this.BLOCK_SELECTOR).val("");//设置地理位置-小区值为空
		$(this.BUILDING_SELECTOR).val("");//设置地理位置-楼号值为空
		$(this.BUILDING_SELECTOR).attr("disabled", true);//设置地理位置-楼号不可编辑
		$(this.UNIT_SELECTOR).val("");//设置地理位置-单元值为空
		$(this.UNIT_SELECTOR).attr("disabled", true);//设置地理位置-单元不可编辑
	}
		

	/*
		调用外部函数
	 */
	callOuterFunc=function(outerFunc){
		if(outerFunc!=null){
			console.log("calling outer func...");
			outerFunc();
		}
		else{
			console.log("outer func is null......");
		}
	}
	
	/*
	 	默认加载小区列表
	 */
	getLocationBlock = function(selectorx){
		var url = BASE_CONTEXT_PATH + "/location/get-block";
		
		var params=null
		$.post(url, params, function(res){
			console.log(res);  //	由于后端返回的是JSON对象,所以不必再进行转换. 
			if (res != null && res != "") {
				//var obj = $.parseJSON(res);				
				if (res.result_code == "success") {
					var locationList = res.locationList;
					setLocationBlockHtml(locationList,selectorx);  //设置小区列表					
				} else {
					//dialog type: success warning info error,默认为info
					util.message(res.result_msg,null,"warning");
				}
			} 
		});
	}
	
	/**
	 * 	查询地理位置-小区/楼号/单元
	 */
	getLocationChildNodeFn = function(locationId, nodeType,building_selector,unit_selector){
		var url = BASE_CONTEXT_PATH + "/location/get-block-building-unit";
		
		var params = new Object();
		params.locationId = locationId;
		params.nodeType = nodeType;
		$.post(url, params, function(res){
			//console.log(res);  //	由于后端返回的是JSON对象,所以不必再进行转换. 
			if (res != null && res != "") {
				//var obj = $.parseJSON(res);						
				if (res.result_code == "success") {
					var locationList = res.locationList;
					if(nodeType=="BUILDING"){
						setLocationBuildingHtml(locationList,building_selector);
					}else{
						setLocationUnitHtml(locationList,unit_selector);
					}
				} else {
					//dialog type: success warning info error,默认为info
					util.message(res.result_msg,null,"warning");
				}
			} 
		});
	}
	
	/**
	 * 	设置-小区下拉列表
	 */
	setLocationBlockHtml = function(locationList,selectorx){		
		//$("#customer-item-search .location-building").attr("disabled", false);//设置地理位置-楼号可编辑
		//$(BLOCK_SELECTOR).attr("disabled", false);//设置地理位置-楼号可编辑
		console.log("init block.....");
		
		var html = "<option value='' data-location-traceids=''>——小区——</option>";
		$.each(locationList, function(){
			html = html + "<option value='"+this.id+"' data-location-traceids='"+this.traceIds+"' data-location-code='"+this.code+"' data-location-name='"+this.name+"'>"+this.name+"</option>";
		});
		console.log("init block  BLOCK_SELECTOR"+selectorx);
		$(selectorx).html(html);
	}
	
	
	/**
	 * 	设置-楼号下拉列表
	 */
	setLocationBuildingHtml = function(locationList,selectorx){		
		//$("#customer-item-search .location-building").attr("disabled", false);//设置地理位置-楼号可编辑
		$(selectorx).attr("disabled", false);//设置地理位置-楼号可编辑
		
		var html = "<option value='' data-location-traceids=''>——楼号——</option>";
		$.each(locationList, function(){
			html = html + "<option value='"+this.id+"' data-location-traceids='"+this.traceIds+"' data-location-code='"+this.code+"' data-location-name='"+this.name+"'>"+this.name+"</option>";
		});
		$(selectorx).html(html);
	}
	
	/**
	 * 	设置-单元下拉列表
	 */
	setLocationUnitHtml = function(locationList,selectorx){
		
		$(selectorx).attr("disabled", false);//设置地理位置-单元可编辑
		
		var html = "<option value='' data-location-traceids=''>——单元——</option>";
		$.each(locationList, function(){
			html = html + "<option value='"+this.id+"' data-location-traceids='"+this.traceIds+"' data-location-code='"+this.code+"' data-location-name='"+this.name+"'>"+this.name+"</option>";
		});
		$(selectorx).html(html);
	}
		
	/*
	 	初始下拉框change event listener
	 */
	initDropdownListener=function(callbackFunc, block_selector,building_selector,unit_selector){
		
		//console.log("debug-searchId:"+this.searcherId);
		console.log("debug-BLOCK_SELECTOR"+block_selector);
		
		/**
		 * 	小区-change事件
		 */
		$(block_selector).on('change',function() {
				
			$(building_selector).val("");				//设置地理位置-楼号值为空
			$(building_selector).attr("disabled", true);//设置地理位置-楼号不可编辑

			$(unit_selector).val("");					//设置地理位置-单元值为空
			$(unit_selector).attr("disabled", true);	//设置地理位置-单元不可编辑

			var locationId = $(this).val();
			if(locationId!=null && locationId!=""){
				var nextNodeType = $(this).attr("data-location-next-node-type");
				getLocationChildNodeFn(locationId, nextNodeType,building_selector,unit_selector);
			}
			//globalLocation.cancelAllSelectedNode();//取消ztree的选中状态
			//defaultSearch();//查询客户账单
			callOuterFunc(callbackFunc);
		});
		
		/**
		 * 	楼号-change事件
		 */
		$(building_selector).on('change',function() {
			
			$(unit_selector).val("");//设置地理位置-单元值为空
			$(unit_selector).attr("disabled", true);//设置地理位置-单元不可编辑
			
			var locationId = $(this).val();
			if(locationId!=null && locationId!=""){
				var nextNodeType = $(this).attr("data-location-next-node-type");
				getLocationChildNodeFn(locationId, nextNodeType,building_selector,unit_selector);	
			}
			//globalLocation.cancelAllSelectedNode();//取消ztree的选中状态
			//defaultSearch();//查询客户账单
			callOuterFunc(callbackFunc);

		});

		/**
		 * 	单元-change事件
		 */
		$(unit_selector).on('change',function() {

			var locationId = $(this).val();
			if(locationId!=null && locationId!=""){
				var nextNodeType = $(this).attr("data-location-next-node-type");
			}
			//globalLocation.cancelAllSelectedNode();//取消ztree的选中状态
			//defaultSearch();//查询客户账单
			callOuterFunc(callbackFunc);
		});
	}

	
}