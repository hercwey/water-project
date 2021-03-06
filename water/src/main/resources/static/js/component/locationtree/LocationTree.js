/**
 * 通用地理位置ztree class
 */
function LocationTree(treeId) {

	var locationSearch = null;
	var searchCallback = null;
	var globalZTreeCallback = null;
	var searchPanel = null;
	
	var ztreeOjb = null;// //保存初始化当前zTree的对象
	this.G_SELECTED_TREE_NODE = null;// 用于保存用户选择的当前节点数据

	/**
	 * 获取当前选择的节点（如果当前节点有子节点则包含子节点信息）
	 */
	this.getSelectedTreeNodeFn = function() {
		return this.G_SELECTED_TREE_NODE;
	}

	/**
	 * 获取地理位置痕迹ID（ID-ID-ID-ID）
	 */
	this.getTraceIds = function() {
		if (this.G_SELECTED_TREE_NODE != null) {
			return this.G_SELECTED_TREE_NODE.traceIds;
		}
		return null;
	}

	/**
	 * ztree加载中
	 */
	this.locationZTreeLoading = function() {
		$("#global-location-ztree").html("<li id='ztree-loading'>正在加载中，请稍等...</li>");
	}
	/**
	 * ztree加载完成
	 */
	this.locationZTreeLoaded = function() {
		$("#global-location-ztree #ztree-loading").remove();
	}

	/**
	 * 取消选中的节点
	 */
	this.cancelAllSelectedNode = function() {
		// var treeObj = $.fn.zTree.getZTreeObj("global-location-ztree");
		var treeObj = ztreeOjb;
		treeObj.cancelSelectedNode();// 取消所有选中的节点
		this.G_SELECTED_TREE_NODE = null;// 设置当前选中的节点为空
	}

	this.initLocationTree = function() {
		/**
		 * 通用地理位置ztree配置对象
		 */
		var globalZTreeLocationSetting = {
			async : {
				enable : true,
				url : "/location/get-nodes",
				autoParam : [ "id" ]
			},
			view : {
				selectedMulti : false
			// 禁止多点同时选中的功能
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
				onClick : globalZTreeCallback.zTreeLocationOnClick,// 节点被点击的事件回调函数
				beforeAsync : globalZTreeCallback.zTreeBeforeAsync,// 用于捕获异步加载之前的事件回调函数
				onAsyncError : globalZTreeCallback.zTreeOnAsyncError,// 用于捕获异步加载正常结束的事件回调函数
				onAsyncSuccess : globalZTreeCallback.zTreeOnAsyncSuccess,// 用于捕获异步加载出现异常错误的事件回调函数
			},
		};
		// console.log("start init");
		ztreeOjb = $.fn.zTree.init($("#"+treeId+" .location-ztree"), globalZTreeLocationSetting);
		
		locationSearch = new GlobalLocationSearch(ztreeOjb);
		searchCallback = new GlobalSearchCallback(ztreeOjb);
		globalZTreeCallback = new GlobalZTreeCallback(locationSearch, searchCallback);
		searchPanel = new SearchPanel(treeId, locationSearch);
		
		//return z
	}

}

/**
 * 通用地理位置ztree的回调class
 */
function GlobalZTreeCallback(globalLocationSearch, globalSearchCallback) {
	/**
	 * 异步加载之前的事件回调函数
	 */
	this.zTreeBeforeAsync = function(treeId, treeNode) {
		if (treeNode == null || typeof (treeNode) == "undefined") {
			globalLocation.locationZTreeLoading();// ztree加载中
		}

		return true;
	};
	/**
	 * 异步加载正常结束的回调函数
	 */
	this.zTreeOnAsyncSuccess = function(event, treeId, treeNode, msg) {
		if (treeNode == null || typeof (treeNode) == "undefined") {
			globalLocation.locationZTreeLoaded();// ztree加载完成
		}

		// 如果是查询模式
		if (globalLocationSearch.searchMode == globalLocationSearch.G_SEARCH_MODE_YES) {
			globalSearchCallback.markNodeInTree(notFoundNodeIdx);
		}

	};
	/**
	 * 异步加载出现异常错误的回调函数
	 */
	this.zTreeOnAsyncError = function(event, treeId, treeNode, XMLHttpRequest,
			textStatus, errorThrown) {
		if (treeNode == null || typeof (treeNode) == "undefined") {
			globalLocation.locationZTreeLoaded();// ztree加载完成
		}

		console.log("treeId:" + treeId + ", treeNode:" + treeNode);
		console.log("textStatus:" + textStatus);
		console.log("errorThrown:" + errorThrown);
		console.log("XMLHttpRequest:" + XMLHttpRequest);
	};

	/*
	 * 节点被点击的事件回调函数
	 */
	this.zTreeLocationOnClick = function(event, treeId, treeNode) {
		
		globalLocation.G_SELECTED_TREE_NODE = treeNode;// 当前选择的地理位置节点
		
		clickZTreeNodeCallback(globalLocation.getTraceIds());// ztree树click某节点后的回调函数，在调用主页面加入此函数
	}
}

// ------------------------------ 地理位置查询部分 ------------------------------
/**
 * 通用地理位置查询
 */
function GlobalLocationSearch(ztreeOjb, searchCallback) {

	
	
	this.G_ACTION_SEARCH = 0;// 表示开始查询
	this.G_ACTION_PREV = -1;// 表示查询上一个
	this.G_ACTION_NEXT = 1;// 表示查询下一个

	this.G_SEARCH_MODE_YES = 1; // 查询模式
	this.G_SEARCH_MODE_NO = 0; // 非查询模式

	this.g_curr_marked_node_id = null;// 已经标记的当前Node对象Id
	this.currMarkedNode = null; // 当前已经标记的节点ID
	this.searchMode = 0; // 查询模式,主要用于区分是主动加载tree还是在查询时加载Tree node
	this.notFoundNodeIdx = -1; // 未找到的结点索引,在idArr2中的索引.
	idArr2 = null; // 结点trace 序列.

	/**
	 * 根据条件查询地理位置并展开对应节点
	 */
	this.searchLocation = function(parms) {
		// zTreeLoading();//ztree加载中
		var url = BASE_CONTEXT_PATH + "/location/search";// 需要提交的url
		$.post(url, parms, function(res) {
			console.log(res);// 由于后端返回的是JSON对象,所以不必再进行转换.
			if (res != null && res != "") {
				// var obj = $.parseJSON(res);
				if (res.result_code == "success") {
					var location = res.location;
					if (location != null) {

						globalLocationSearch.g_curr_marked_node_id = location.id; // 已经标记的当前Node对象Id
						console.log("-=-=-=-=-=-=-=-=-=-=-=-=-=-="+globalLocationSearch.g_curr_marked_node_id);
						var traceIds = location.traceIds;
						idArr2 = searchCallback.parseTraceIds(traceIds);

						console.log("=======idArr2========="+JSON.stringify(idArr2));

						searchCallback.markNodeInTree(0); // 在Tree中标记结点

					}

					// reAsyncChildNodes(location.id);//根据地理位置属性ID，异步加载子节点

				} else {
					// dialog type: success warning info
					// error,默认为info
					util.message(res.result_msg, null, "warning");
				}
			}
		});
	}

}
/**
 * 通用查询地理位置回调
 */
function GlobalSearchCallback(ztreeObjx) {
	
	var ztreeOjb = ztreeObjx;
	
	// ------------------------------ 地理位置查询回调 ------------------------------
	// 根据地理位置属性ID，异步加载子节点
	reAsyncChildNodes = function(traceIds) {

		// var treeObj = $.fn.zTree.getZTreeObj("global-location-ztree");
		var treeObj = ztreeOjb;
		// var node = treeObj.getNodeByParam("isHidden", true);
		// treeObj.showNode(node);
		// 异步刷新某节点
		var nodes = treeObj.getNodesByParam("traceIds", traceIds);
		console.log(JSON.stringify(nodes));
		if (nodes.length > 0) {
			var node = nodes[0];
			treeObj.reAsyncChildNodes(node, "refresh");
		}
		// $("#global-location-ztree_2_switch").click();
	}

	// 在树中标记结点
	// startIdx:自路径跟踪数组的哪个节点开始
	this.markNodeInTree = function(startIndex) {
		var idArr2 = globalLocationSearch.idArr2;
		// 生成traceArr用于保存已经找到的结点索引
		var traceArr = new Array();

		var node = null;
		// var treeObj = $.fn.zTree.getZTreeObj("global-location-ztree");
		var treeObj = ztreeOjb;

		for (var i=startIndex; i<idArr2.length; i++) {

			var traceIds = idArr2[i];

			// find node in tree

			var nodes = treeObj.getNodesByParam("traceIds", traceIds);
			console.log("===========traceIds======="+traceIds);
			console.log("===========nodes======="+JSON.stringify(nodes));
			// 如果找到且是需要标记的目标节点,标记之
			if (nodes.length>0) {
				node = nodes[0];
				// treeObj.reAsyncChildNodes(node, "refresh");
				if (i==idArr2.length-1) {

					treeObj.selectNode(node); // 标记结点.change color or select
												// the node?
					globalLocation.G_SELECTED_TREE_NODE = node;// 当前选择的地理位置节点

					globalLocationSearch.searchMode = 0; // 查询模式结束
				} else {
					traceArr[i] = node.traceIds;
				}
			} else {
				if (traceArr.length > 0) {
					notFoundNodeIdx = i;
					reAsyncChildNodes(traceArr[traceArr.length - 1]); // 异步加载在Tree中可以找到的最近的父节点的子节点.

				} else {
					globalLocationSearch.searchMode = 0; // 查询模式结束,此时说明未在树中查询到最根节点,直接返回即可.
				}
				break;
			}

		}

		if (node != null) {
			treeObj.selectNode(node); // 标记结点.change color or select the node?
			globalLocation.G_SELECTED_TREE_NODE = node;// 当前选择的地理位置节点
		}

	}

	// 解析traceIds
	// 返回idArr2
	this.parseTraceIds = function(traceIds) {
		// 分解traceIds--->idArr1
		var idArr1 = traceIds.split("-");
		// 生成traceArr用于保存已经找到的结点索引
		var traceArr = new Array();

		// 生成idArr2 用于保存查询到的结点路径.
		// 如果returnNode的traceIds="1-2-3"
		// 此时idArr1中的内容为: 0--->1; 1--->2; 3--->3
		// idArr2中的内容为: 0--->1; 2--->1-2; 3--->1-2-3
		var idArr2 = new Array(); // 生成用于保存结点路径的数组
		for (var i=0; i<idArr1.length; i++) {
			var tempStr = "";
			for (var j=0; j<=i; j++) {
				if (j==0) {
					tempStr = idArr1[j]
				} else {
					tempStr = tempStr+'-'+idArr1[j];
				}
			}
			idArr2[i] = tempStr
		}
		return idArr2;
	}
}

/**
 * 查询面板 
 * @param treeId
 * @param globalLocationSearch
 * @returns
 */
function SearchPanel(treeId, globalLocationSearch){
	
	/**
	 * 	绑定查询面板中查询按钮的click事件
	 */
	$("#"+treeId+" .search-btn").on("click", function(){
		console.log("search");
		var searchCond = $("#"+treeId+" .search-cond").val();//查询条件
		if(searchCond==null || searchCond==""){
			//$.fn.zTree.init($("#location-ztree"), setting);
			return;
		}
		
		//置查询模式
		globalLocationSearch.searchMode=globalLocationSearch.G_SEARCH_MODE_YES;
		
		console.log("search start");
		//查询条件 
		var parms = new Object();
		parms.searchCond = searchCond;
		parms.id = null;
		parms.action = globalLocationSearch.G_ACTION_SEARCH;
		
		globalLocationSearch.searchLocation(parms);//根据条件查询地理位置并展开对应节点
		
	});
	/**
	 * 	绑定清除按钮的click事件，清除查询输入框内容，且输入框获得焦点
	 */
	$("#"+treeId+" .search-clear-btn").on("click", function(){
		//清除查询输入框内容，且输入框获得焦点
		$("#"+treeId+" .search-cond").val("");
		$("#"+treeId+" .search-cond").focus();//获得焦点
		globalLocationSearch.g_curr_marked_node_id=null;
	});

	/*
		绑定查询条件输入框的change事件,当查询条件变化时,重新查询.
	*/
	$("#"+treeId+" .search-cond").on("change", function(){
		globalLocationSearch.g_curr_marked_node_id=null;
	});

	/**
	 * 	绑定input输入框的键盘事件,键盘上按下回车键时,自动执行click
	 */
	$("#"+treeId+" .search-cond").on('keypress',function(event){
	    if(event.keyCode=="13") {
	    	$(""#"+treeId+" .search-btn").click();
	    }
	});


	/**
	 * 	绑定查询面板中查询上一个按钮的click事件
	 */
	$("#"+treeId+" .search-prev-btn").on("click", function(){
		
		var searchCond = $("#"+treeId+" .search-cond").val();//查询条件
		if(searchCond==null || searchCond==""){
			//$.fn.zTree.init($("#location-ztree"), setting);
			return;
		}
		
		//置查询模式
		globalLocationSearch.searchMode=globalLocationSearch.G_SEARCH_MODE_YES;
		
		//查询条件 
		var parms = new Object();
		parms.searchCond = searchCond;
		parms.id = globalLocationSearch.g_curr_marked_node_id;
		parms.action = globalLocationSearch.G_ACTION_PREV;
		
		globalLocationSearch.searchLocation(parms);//根据条件查询地理位置并展开对应节点
		
	});

	/**
	 * 	绑定查询面板中查询下一个按钮的click事件
	 */
	$("#"+treeId+" .search-next-btn").on("click", function(){
		
		var searchCond = $("#"+treeId+" .search-cond").val();//查询条件
		if(searchCond==null || searchCond==""){
			return;
		}
		
		//置查询模式
		globalLocationSearch.searchMode=globalLocationSearch.G_SEARCH_MODE_YES;
		
		//查询条件 
		var parms = new Object();
		parms.searchCond = searchCond;
		parms.id = globalLocationSearch.g_curr_marked_node_id;
		parms.action = globalLocationSearch.G_ACTION_NEXT;
		
		globalLocationSearch.searchLocation(parms);//根据条件查询地理位置并展开对应节点
		
	});
}
