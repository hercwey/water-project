/**
 * 通用地理位置ztree class
 */

function LocationTree(treeIdx) {
	this.locTreeId=treeIdx;    //location tree Id (UI)
	this.ztreeOjb = null;     //初始化后zTree对象

	this.currSelectedTreeNode=null;  //用于保存用户选择的当前节点(Tree Node)	

	//是否查询模式
	this.G_SEARCH_MODE_YES = 1; 	// 查询模式
	this.G_SEARCH_MODE_NO = 0; 		// 非查询模式
	
	this.searchMode = 0; 				// 查询模式,主要用于区分是主动加载tree还是在查询时加载Tree node

	this.g_curr_marked_node_id = null;	// 已经标记的当前Node对象Id
	this.currMarkedNode = null; 		// 当前已经标记的节点ID	
	this.notFoundNodeIdx = -1; 			// 未找到的结点索引,在idArr2中的索引.
	
	this.nodeClickOuterFunc=null;    		//node点击时 调用的外部函数(业务相关)
	
	var idArr2=null;  
	
	
	 
}

/**
	初始化Tree
*/
LocationTree.prototype.initTree=function(){
	/**
	 * 通用地理位置ztree配置对象
	 */
	var zTreeSetting = {
		edit: {
            enable: false,
            showRemoveBtn: this.showRemoveBtn,
            showRenameBtn: this.showRenameBtn,
            drag: {
                isCopy: true, //所有操作都是move
                isMove: false,
                prev: false,
                next: false,
                inner: false
            }
        },
		async : {
			enable : true,
			url : "/location/get-nodes",
			autoParam : [ "id" ]
		},
		view : {
			selectedMulti : false  // 禁止多点同时选中的功能			
		},
		check : {
			enable : false,
			chkStyle : "checkbox",
		},
		data : {
			key : {
				name : "name",
			},
			simpleData : {
				enable : true,
				idKey : "id",
				pIdKey : "pid",
				rootPId : 0
			}
		},
		callback : {			
			beforeAsync : this.zTreeBeforeAsync,	// 用于捕获异步加载之前的事件回调函数
			onAsyncError : this.zTreeOnAsyncError,// 用于捕获异步加载异常结束的事件回调函数	
			onAsyncSuccess : this.zTreeOnAsyncSuccess,// 用于捕获异步加载正常结束的事件回调函数
						
			beforeDrag: this.beforeDrag,  	//before drag
			onDrag:this.onDrag,   			//onDrag			
		},
	};
	// console.log("start init");
	console.log("location tree id is:"+this.locTreeId);
	var ztreeId="#"+"location-tree-"+this.locTreeId;		
	console.log("tree selector is:"+ztreeId);
	
	gThat=this;   // ref of instance	
	
		
	this.ztreeObj = $.fn.zTree.init($(ztreeId), zTreeSetting);  //init zTree	
	this.initSearchPanel(this.locTreeId,this.ztreeObj,this);  //init search panel
	this.initTreeEventListener(this.locTreeId,this.ztreeObj,this);  //init node click
	
}

/**
 * 是否显示remove button(显示前判定)
 */
LocationTree.prototype.showRemoveBtn=function(treeId, treeNode) {
	//console.log('show remove btn');
    //return !treeNode.isFirstNode;
	return false;
}
/**
 * 是否显示rename button(显示前判定)
 */
LocationTree.prototype.showRenameBtn=function(treeId, treeNode) {
	//console.log('show rename btn');
    //return !treeNode.isLastNode;
	return false;
}

/**
 * before drag
 */
LocationTree.prototype.beforeDrag=function(treeId, treeNodes){
	console.log("before drag in location tree");
    /*for (var i=0,l=treeNodes.length; i<l; i++) {
        if (treeNodes[i].drag === false) {
            return false;
        }
    }*/
    return true;
}

/**
 * on drag
 */
LocationTree.prototype.onDrag=function(event){
	console.log("on drag in location tree");	
	return false;
}

/**
 * 初始化树节点click事件-listener
 */
LocationTree.prototype.initTreeEventListener=function(locTreeId,ztreeObj,that){
	
	/*
	 * 树节点click事件-处理函数
	 */
	/*function treeNodeClickCallback(event, treeId, treeNode){
		
		
		that.currSelectedTreeNode = treeNode;
		console.log("......currSelectedTreeNode:"+that.currSelectedTreeNode);
		console.log("..................test................");
		console.log("..........treeId........"+treeId);
		console.log("..........that........"+that);
		
		that.nodeClickOuterFunc(treeNode);  //调用外部业务函数
		
	}*/
	
	function zTreeOnMouseDown(event, treeId, treeNode) {
	    //alert(treeNode ? treeNode.tId + ", " + treeNode.name : "isRoot");
		
		if (treeNode != null) {
			that.ztreeObj.selectNode(treeNode); // 标记结点.change color or select the node?
			//currSelectedTreeNode = node;// 当前选择的地理位置节点
		}
		
		that.currSelectedTreeNode = treeNode;
		that.nodeClickOuterFunc(treeNode);  //调用外部业务函数
	};
	
	/*
	* 事件回调-异步加载成功 
	*/
	function zTreeOnAsyncSuccess (event, treeId, treeNode, msg) {
		console.log("async success is called!");
		if (treeNode == null || typeof (treeNode) == "undefined") {
			LocationTree.prototype.locationZTreeLoaded(treeId);// ztree加载完成
		}
		
         // 如果是查询模式
		//consoloe.log("global Location search obj is:"+this.globalLocationSearch);
		if (that.searchMode== that.G_SEARCH_MODE_YES) {
			LocationTree.prototype.markNodeInTree(that.notFoundNodeIdx, that.idArr2, ztreeObj,that);
		}
		
		//console.log("async tree node is:"+JSON.stringify(treeNode));
		
		$(".ztree li").attr("draggable",true);
	};
	
	//ztreeObj.setting.callback.onClick=treeNodeClickCallback;
	ztreeObj.setting.callback.onMouseDown=zTreeOnMouseDown;
	ztreeObj.setting.callback.onAsyncSuccess=zTreeOnAsyncSuccess;  //用于捕获异步加载出现异常错误的事件回调函数
}

/**
 * 设置节点-CLICK调用的外部业务函数
 * outerFunc
 * 函数签名为:    
 * 		xxx(treeNode)
 * 		treeNode:当前点击的Tree node
 */
LocationTree.prototype.setClickOuterFunction=function(outerFunc){
	this.nodeClickOuterFunc=outerFunc;
}

/**
 * 显示ztree正在加载中
 * ztree加载中
 *  treeContainerId   tree容器ID   不带#号
 */
LocationTree.prototype.locationZTreeLoading = function(treeContainerId) {
	const infoClass="loadinginfo";
	console.log("container id is:"+treeContainerId);	
 	$("#"+treeContainerId).html("<li class='"  +  infoClass + "'>正在加载中，请稍等...</li>");
 	console.log("正在加载中，请稍等...");
}

/**
 * ztree加载完毕 
 */
LocationTree.prototype.locationZTreeLoaded = function(treeContainerId) {
	
	console.log("treeContainerId is"+treeContainerId);
	
	//var idArr=treeContainerId.split("-");
	//var treeIdx=idArr[idArr.length-1];   //最后一部分为treeIdx  初始化时使用的参数	
	const infoClass=".loadinginfo";
	
 	$("#"+treeContainerId+" "+infoClass).remove();
 	
 	console.log("加载完毕...");
}

//=================用户调用函数==============

/**
	获取当前选择的节点
	如果当前节点有子节点则包含子节点信息
	currNode
*/
LocationTree.prototype.getSelectedTreeNode = function() {
	console.log(this.currSelectedTreeNode);
	return this.currSelectedTreeNode;
}

/**
 *  获取当前结节的traceId
 *  traceId形式（ID-ID-ID-ID）
 */
 LocationTree.prototype.getTraceIds = function() {
	if (this.currSelectedTreeNode != null) {
		return this.currSelectedTreeNode.traceIds;
	}
	return null;
}

/**
 * 	取消选中的节点
 */
 LocationTree.prototype.cancelAllSelectedNode = function() {	
	var treeObj = this.ztreeObj;
	treeObj.cancelSelectedNode();	// 取消所有选中的节点
	this.currSelectedTreeNode = null;// 设置当前选中的节点为空
}


//====================事件回调函数==================

 /*
 * 事件回调-异步加载之前
 * treeId   界面中Tree UI 组件ID
 */
 LocationTree.prototype.zTreeBeforeAsync = function(treeId, treeNode) {
 	console.log("before async is called!");
 	console.log("before async     treeId is:"+treeId);
 	console.log("before async     treeNode is:"+treeNode);
 	//var ztreeId="#"+treeId+" .location-ztree";
 	if (treeNode == null || typeof (treeNode) == "undefined") {
 		LocationTree.prototype.locationZTreeLoading(treeId);	// ztree加载中
 		
 	}
 	return true;
 };


	
/*
* 事件回调-异步加载出现异常
*/
LocationTree.prototype.zTreeOnAsyncError = function(event, treeId, treeNode, XMLHttpRequest,textStatus, errorThrown) {
	console.log("async error is called!");
	if (treeNode == null || typeof (treeNode) == "undefined") {		
		LocationTree.prototype.locationZTreeLoaded(treeId);// 删除 ztree加载时的提示信息
	}

	//console.log("treeId:" + treeId + ", treeNode:" + treeNode);
	//console.log("textStatus:" + textStatus);
	//console.log("errorThrown:" + errorThrown);
	//console.log("XMLHttpRequest:" + XMLHttpRequest);
};

LocationTree.prototype.parseTraceIds = function(traceIds) {
	// 分解traceIds--->idArr1
	var idArr1 = traceIds.split("-");
	// 生成traceArr用于保存已经找到的结点索引
	var traceArr = new Array();

	// 生成idArr 用于保存查询到的结点路径.
	// 如果returnNode的traceIds="1-2-3"
	// 此时idArr1中的内容为: 0--->1; 1--->2; 3--->3
	// idArr中的内容为: 0--->1; 2--->1-2; 3--->1-2-3
	var idArr = new Array(); // 生成用于保存结点路径的数组
	for (var i=0; i<idArr1.length; i++) {
		var tempStr = "";
		for (var j=0; j<=i; j++) {
			if (j==0) {
				tempStr = idArr1[j]
			} else {
				tempStr = tempStr+'-'+idArr1[j];
			}
		}
		idArr[i] = tempStr
	}
	return idArr;
}

/*
 * 查询地理位置
 * 		that :   LocationTree组件引用   
 * 		ztreeObj:ztree组件对象引用(是that的一部分)
 */
LocationTree.prototype.searchLocation = function(parms,ztreeObj,that) {
	// zTreeLoading();//ztree加载中
	var url = BASE_CONTEXT_PATH + "/location/search";// 需要提交的url
	
	//查询成功后:调用此函数
	function searchCallback(res){
		console.log("search callback function is called!");
		if (res != null && res != "") {
			// var obj = $.parseJSON(res);
			if (res.result_code == "success") {
				var location = res.location;
				if (location != null) {
					that.g_curr_marked_node_id = location.id; //已经标记的当前Node对象Id
					//console.log("-=-=-=-=-=-=-=-=-=-=-=-=-=-="+g_curr_marked_node_id);					
					var traceIds = location.traceIds;
					
					//TODO  ADD CODE HERE   global variables
					that.idArr2 = LocationTree.prototype.parseTraceIds(traceIds);					
					console.log("=======idArr2========="+JSON.stringify(that.idArr2));
					LocationTree.prototype.markNodeInTree(0,that.idArr2,ztreeObj,that); // 在Tree中标记结点
				}

			} else {
				// dialog type: success warning info
				// error,默认为info
				util.message(res.result_msg, null, "warning");
			}
		}
	}
	
	console.log("start search..............");
	$.post(url, parms, searchCallback);
	
	return;	
}

LocationTree.prototype.reAsyncChildNodes = function(traceIds,ztreeObj) {

	var treeObj = ztreeObj;	
	// 异步刷新某节点
	var nodes = treeObj.getNodesByParam("traceIds", traceIds);
	console.log(JSON.stringify(nodes));
	if (nodes.length > 0) {
		var node = nodes[0];
		treeObj.reAsyncChildNodes(node, "refresh");
	}
}


/**
 *  在树中标记结点
 *  startIdx:自路径跟踪数组的哪个节点开始 
 */ 
LocationTree.prototype.markNodeInTree = function(startIndex,idArr2x,ztreeObj,that) {
	
	var idArr2 = idArr2x;
	
	var treeObj=ztreeObj;
	
	var node = null;
	
	// 生成traceArr用于保存已经找到的结点索引
	var traceArr = new Array();

	for (var i=startIndex; i<idArr2.length; i++) {

		var traceIds = idArr2[i];

		// find node in tree

		console.log("start mark node..........");		
		
		var nodes = treeObj.getNodesByParam("traceIds", traceIds);
		console.log("===========traceIds======="+traceIds);
		console.log("===========nodes======="+JSON.stringify(nodes));
		// 如果找到且是需要标记的目标节点,标记之
		if (nodes.length>0) {
			node = nodes[0];
			// treeObj.reAsyncChildNodes(node, "refresh");
			if (i==idArr2.length-1) {

				treeObj.selectNode(node); // 标记结点.change color or select the node?
				
				// TODO ADD CODE HERE
				that.currSelectedTreeNode = node;// 当前选择的地理位置节点
				// TODO ADD CODE HERE
				that.searchMode = 0; // 查询模式结束
			} else {
				traceArr[i] = node.traceIds;
			}
		} 
		else 
		{
			if (traceArr.length > 0) {
				
				//TODO ADD CODE HERE
				that.notFoundNodeIdx = i;
				
				LocationTree.prototype.reAsyncChildNodes(traceArr[traceArr.length - 1],ztreeObj); // 异步加载在Tree中可以找到的最近的父节点的子节点.

			} else {
				//TODO GLOBAL VARIABLES   
				that.searchMode = 0; // 查询模式结束,此时说明未在树中查询到最根节点,直接返回即可.
			}
			break;
		}

	}

	if (node != null) {
		treeObj.selectNode(node); // 标记结点.change color or select the node?
		currSelectedTreeNode = node;// 当前选择的地理位置节点
	}
}

/**
 *   初始化查询面板 
 *   treeId  LocationTree'id  与初始化函数中的treeId相同  
 *   ztreeObj ztreeObj引用  
 * 
 */
LocationTree.prototype.initSearchPanel=function(treeId,ztreeObj,that){
	//查询相关UI ID
	const BTN_SEARCH="#"+treeId+" .search-btn";			//查询按钮-ID
	const BTN_CLEAR="#"+treeId+" .search-clear-btn";  	//清除按钮-ID
	const BTN_PREV="#"+treeId+" .search-prev-btn";	 	//上一个-ID
	const BTN_NEXT="#"+treeId+" .search-next-btn";    	//下一个-ID
	const INPUT_SEARCH_COND="#"+treeId+" .search-cond";  //查询条件输入框-ID
	
	//查询状态常量
	const ACTION_SEARCH = 0;	//表示开始查询  初始查询
	const ACTION_PREV = -1;	//表示查询上一个
	const ACTION_NEXT = 1;	//表示查询下一个
	
	/**
	 * 	绑定查询面板中查询按钮的click事件
	 *  回调函数中如何确定上级对象?
	 */
	$(BTN_SEARCH).on("click", function(){
		console.log("search button clicked!");
		//读取查询条件
		var searchCond = $(INPUT_SEARCH_COND).val();
		//如果查询条件为空或null值
		if(searchCond==null || searchCond==""){			
			return;
		}
		
		//置查询模式
		//console.log("access var:"+that.G_SEARCH_MODE_YES);
		//console.log("access var1:"+that.searchMode);		
		that.searchMode=that.G_SEARCH_MODE_YES;
		
		console.log("search start");
		//查询条件 
		var parms = new Object();
		parms.searchCond = searchCond;
		parms.id = null;		
		parms.action = ACTION_SEARCH;		
		LocationTree.prototype.searchLocation(parms,ztreeObj,that);//根据条件查询地理位置并展开对应节点		
	});
	
	
	
	/**
	 * 	绑定清除按钮的click事件，清除查询输入框内容，且输入框获得焦点
	 */
	$(BTN_CLEAR).on("click", function(){
		console.log("clear button clicked!");
		//清除查询输入框内容，且输入框获得焦点
		$(INPUT_SEARCH_COND).val("");
		$(INPUT_SEARCH_COND).focus();//获得焦点
		
		that.g_curr_marked_node_id=null;
	});

	/*
		绑定查询条件输入框的change事件,当查询条件变化时,重新查询.
	*/
	$(INPUT_SEARCH_COND).on("change", function(){
		console.log("input changed!");
		that.g_curr_marked_node_id=null;
	});
	
	/**
	 * 	绑定input输入框的键盘事件,键盘上按下回车键时,自动执行click
	 */
	$(INPUT_SEARCH_COND).on('keypress',function(event){
	    if(event.keyCode=="13") {
	    	$(BTN_SEARCH).click();
	    }
	});

	/**
	 * 	绑定查询面板中查询上一个按钮的click事件
	 */
	$(BTN_PREV).on("click", function(){
		console.log("previous button clicked!");
		var searchCond = $(INPUT_SEARCH_COND).val();//查询条件
		if(searchCond==null || searchCond==""){			
			return;
		}
		
		//置查询模式
		that.searchMode=that.G_SEARCH_MODE_YES;
		
		//查询条件 
		var parms = new Object();
		parms.searchCond = searchCond;
		parms.id = that.g_curr_marked_node_id;
		parms.action = ACTION_PREV;
		
		LocationTree.prototype.searchLocation(parms,ztreeObj,that);//根据条件查询地理位置并展开对应节点
		
	});

	/**
	 * 	绑定查询面板中查询下一个按钮的click事件
	 */
	$(BTN_NEXT).on("click", function(){
		console.log("next button clicked!");
		var searchCond = $("#"+treeId+" .search-cond").val();//查询条件
		if(searchCond==null || searchCond==""){
			return;
		}
		
		//置查询模式
		that.searchMode=that.G_SEARCH_MODE_YES;
		
		//查询条件 
		var parms = new Object();
		parms.searchCond = searchCond;
		parms.id = that.g_curr_marked_node_id;
		parms.action = ACTION_NEXT;
		
		LocationTree.prototype.searchLocation(parms,ztreeObj,that);//根据条件查询地理位置并展开对应节点
		
	});
}
