/**
 * meter tree class
 */

function LocationTree2(treeIdx) {
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
	this.renameOuterFunc=null;  			//node修改名称时调用的外部函数
	this.removeOuterFunc=null;				//remove node时调用的外部函数
	
	var idArr2=null;  
	 
}

/**
	初始化Tree
*/
LocationTree2.prototype.initTree=function(){
/**
 * 通用地理位置ztree配置对象
 */
	var zTreeSetting = {
		edit: {
            enable: true,
            showRemoveBtn: this.showRemoveBtn,
            showRenameBtn: this.showRenameBtn,
            drag: {
                isCopy: false,//所有操作都是move
                isMove: true,
                prev: true,
                next: true,
                inner: true
            }
        },
			
		async : {
			enable : true,
			url : "/psmeter/get-nodes",
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
			beforeAsync : this.zTreeBeforeAsync,	//用于捕获异步加载之前的事件回调函数
			onAsyncError : this.zTreeOnAsyncError,	//用于捕获异步加载正常结束的事件回调函数	
			
			beforeDrag: this.beforeDrag,  		//before drag			
            beforeDrop: this.beforeDrop,		//before drop
            onDrop: this.onDrop,				//on drop
            //beforeRemove: this.beforeRemove,  //before remove            
            beforeRename: this.beforeRename,  	//before rename
            //onRemove: this.onRemove,			//on remove
            //onRename: this.onRename  			//on rename			
		},
	};
	// console.log("start init");
	console.log("location tree id is:"+this.locTreeId);
	var ztreeId="#"+"location-tree-"+this.locTreeId;		
	console.log("tree selector is:"+ztreeId);
	
	//gThat=this;   // ref of instance	
		
	this.ztreeObj = $.fn.zTree.init($(ztreeId), zTreeSetting);  //init zTree	
	this.initSearchPanel(this.locTreeId,this.ztreeObj,this);  //init search panel
	this.initTreeEventListener(this.locTreeId,this.ztreeObj,this);  //init node click
	
}

/**
 * before drag
 */
LocationTree2.prototype.beforeDrag=function(treeId, treeNodes){
	console.log("before drag---in meter tree");
    /*for (var i=0,l=treeNodes.length; i<l; i++) {
        if (treeNodes[i].drag === false) {
            return false;
        }
    }*/
    return true;
}

/**
 * before drop
 * 用于捕获节点拖拽操作结束之前的事件回调函数，并且根据返回值确定是否允许此拖拽操作
 * 默认值 null 
 */
LocationTree2.prototype.beforeDrop=function(treeId, treeNodes, targetNode, moveType) {
	console.log("before drop in meter tree");
    return targetNode ? targetNode.drop !== false : true;
}

/**
 * 功能:
 * 	用于捕获节点拖拽操作结束的事件回调函数  默认值： null
 * 参数说明:
 * 	event 	js event 对象
			标准的 js event 对象
	treeId	String
		目标节点 targetNode 所在 zTree 的 treeId，便于用户操控
	treeNodes		Array(JSON)
		被拖拽的节点 JSON 数据集合
		如果拖拽操作为 移动，treeNodes 是当前被拖拽节点的数据集合。
		如果拖拽操作为 复制，treeNodes 是复制后 clone 得到的新节点数据。
	targetNode	JSON
		成为 treeNodes 拖拽结束的目标节点 JSON 数据对象。
		如果拖拽成为根节点，则 targetNode = null
	moveType		String
		指定移动到目标节点的相对位置
		"inner"：成为子节点，"prev"：成为同级前一个节点，"next"：成为同级后一个节点
		如果 moveType = null，表明拖拽无效
	isCopy	Boolean
		拖拽节点操作是 复制 或 移动
		true：复制；false：移动
 
 */
LocationTree2.prototype.onDrop=function(event, treeId, treeNodes, targetNode,moveType,isCopy) {
	const MOVE_TYPE_INNER="inner";  //成为子节点
	const MOVE_TYPE_PREV="prev";	//成为同级前一个节点
	const MOVE_TYPE_NEXT="next";	//成为同级后一个节点
	const IS_COPY_YES=true;  		//复制结点
	const IS_COPY_NO=false;  		//移动
	
	console.log("on drop--------- in meter tree ");
	
	//console.log("on drop in meter tree----event object is:"+JSON.stringify(event));	
	//event.preventDefault();
	
	 
	 console.log("received data is :"+treeId);
	 
	 console.log("drop nodes------resource node is:"+JSON.stringify(treeNodes));
	 console.log("drop nodes------target node is:"+JSON.stringify(targetNode));
	 console.log("drop nodes------movetype is:"+moveType);
	 
	 //拖曳无效
	 if(moveType==null){
		 return false;
	 }
	 
	 //如果拖拽成为根节点，则 targetNode = null
	 if(moveType!=null && targetNode==null){
		 console.log("结点被拖放成为根节点")		 
	 }		 
	 
	
    //拖拽放下时，修改被拖拽节点的pid	
    /*console.log(event)
    console.log(treeId +'11111')
    console.log(treeNodes )
    console.log(treeNodes[0].parentCode)
    console.log(targetNode)
    console.log(moveType)*/
	 //生成请求参数
	var params=new Object();
	params.nodeId=treeNodes[0].id;	
	if(targetNode!=null){
		params.targetNodeId=targetNode.id;		
	}
	else{
		params.targetNodeId=null;		
	}	
	params.moveType=moveType;
	
	 
	console.log("向服务器发送请求......");
	 
    $.ajax({
        type:'post',
        url: '/psmeter/moveMeterTreeNode',
        dataType: "json",
        async: false,
        data:params,
        success: function (data) {
        },
        error: function (msg) {
        }
    });    
    
}


/**
 * 重命名之前执行的函数 
 */
LocationTree2.prototype.beforeRename=function(treeId, treeNode, newName, isCancel) {
	var self=this;
	console.log('before rename');
    //className = (className === "dark" ? "":"dark");
	//showLog((isCancel ? "<span style='color:red'>":"") + "[ beforeRename ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.orgName + (isCancel ? "</span>":""));
    if (newName.length == 0) {
        setTimeout(function() {
            //var zTree = $.fn.zTree.getZTreeObj("treeDemo");
        	var zTree=self.ztreeObj;
            zTree.cancelEditName();
            alert("节点名称不能为空.");
        }, 0);
        return false;
    }
    return true;
}


/**
 * 是否显示remove button(显示前判定)
 */
LocationTree2.prototype.showRemoveBtn=function(treeId, treeNode) {
	//console.log('show remove btn');
    //return !treeNode.isFirstNode;
	return true;
}

/**
 * 是否显示rename button(显示前判定)
 */
LocationTree2.prototype.showRenameBtn=function(treeId, treeNode) {
	//console.log('show rename btn');
    //return !treeNode.isLastNode;
	return true;
}

/**
 * 初始化树节点click事件-listener
 */
LocationTree2.prototype.initTreeEventListener=function(locTreeId,ztreeObj,that){
	
	/*
	 * 树节点click事件-处理函数
	 */
	function treeNodeClickCallback(event, treeId, treeNode){
		that.currSelectedTreeNode = treeNode;
		/*console.log("......currSelectedTreeNode:"+that.currSelectedTreeNode);
		console.log("..................test................");
		console.log("..........treeId........"+treeId);
		console.log("..........that........"+that);*/
		if(that.nodeClickOuterFunc!=null){
			that.nodeClickOuterFunc(treeNode);  //调用外部业务函数
		}
		else{
			console.log("click business function is not set.....");
		}
		
	}
	
	/**
	 * 树节点重命名时调用外部业务函数
	 */
	function treeNodeRename(event, treeId, treeNode, isCancel){
		//自结点中获取相关的属性
		var nodeId=treeNode.id;
		var newName=treeNode.name;
		
		if(!isCancel){
			if(that.renameOuterFunc!=null){
				that.renameOuterFunc(nodeId,newName);  //调用外部函数
			}
			else{
				console.log("rename is not set......");
			}
		}
		else{
			console.log("rename is canceled..........");
		}				
	}
	
	/**
	 * call back function: remove tree node
	 */
	function treeNodeRemove(event, treeId, treeNode){
		console.log("into remove function  of meter tree ");
		var nodeId=treeNode.id
		//var traceIds=treeNode.traceIds;
		if(that.removeOuterFunc!=null){
			that.removeOuterFunc(nodeId);  //调用外部函数
		}
		else{
			console.log("remove function is not set");
		}
	}
	
	/*
	* 事件回调-异步加载成功 
	*/
	function zTreeOnAsyncSuccess (event, treeId, treeNode, msg) {
		console.log("async success is called!");
		if (treeNode == null || typeof (treeNode) == "undefined") {
			LocationTree2.prototype.locationZTreeLoaded(treeId);// ztree加载完成
		}
		
		// 如果是查询模式
		//consoloe.log("global Location search obj is:"+this.globalLocationSearch);
		if (that.searchMode== that.G_SEARCH_MODE_YES) {
			LocationTree2.prototype.markNodeInTree(that.notFoundNodeIdx, that.idArr2, ztreeObj,that);
		}

	};
	
	 
	/**
	 * 删除节点之前执行的函数 
	 */
	function beforeRemove(treeId, treeNode) {
		
	    console.log('before remove');
	    //className = (className === "dark" ? "":"dark");
	    //var zTree = $.fn.zTree.getZTreeObj("treeDemo");
	    var zTree=that.ztreeObj;
	    zTree.selectNode(treeNode);
	    return confirm("确认删除 节点 -- " + treeNode.name + " 吗？");
	}

	//设置回调函数
	ztreeObj.setting.callback.onClick=treeNodeClickCallback;		//treenode click
	ztreeObj.setting.callback.onAsyncSuccess=zTreeOnAsyncSuccess;   //sync load node 用于捕获异步加载出现异常错误的事件回调函数	
	ztreeObj.setting.callback.beforeRemove=beforeRemove;  			//before remove 开始移动
	ztreeObj.setting.callback.onRename=treeNodeRename;  			//rename时调用外部业务函数
	ztreeObj.setting.callback.onRemove=treeNodeRemove;  			//remove节点时调用外部业务函数
	
}



/**
 * 设置节点-CLICK调用的外部业务函数
 * outerFunc
 * 函数签名为:    
 * 		xxx(treeNode)
 * 		treeNode:当前点击的Tree node
 */
LocationTree2.prototype.setClickOuterFunction=function(outerFunc){
	this.nodeClickOuterFunc=outerFunc;
}

LocationTree2.prototype.refreshTree=function(){
	this.ztreeObj.reAsyncChildNodes(null, "refresh");
}


/**
 * 功能:
 * 		设置节点-rename调用的外部业务函数
 * 参数:
 * 		outerFunc 外部业务函数
 * 		函数签名:outerFunc(nodeId,newName)
 */
LocationTree2.prototype.setRenameOuterFunction=function(outerFunc){
	this.renameOuterFunc=outerFunc;
}

/**
 * 功能:
 * 	 设置节点-remove调用的外部业务函数
 * 参数:
 * 		outerFunc 外部业务函数
 * 		函数签名:outerFunc(nodeId,traceIds)
 */
LocationTree2.prototype.setRemoveOuterFunction=function(outerFunc){
	this.removeOuterFunc=outerFunc;
}


/**
 * 显示ztree正在加载中
 * ztree加载中
 *  treeContainerId   tree容器ID   不带#号
 */
LocationTree2.prototype.locationZTreeLoading = function(treeContainerId) {
	const infoClass="loadinginfo";
	console.log("container id is:"+treeContainerId);	
 	$("#"+treeContainerId).html("<li class='"  +  infoClass + "'>正在加载中，请稍等...</li>");
 	console.log("正在加载中，请稍等...");
}

/**
 * ztree加载完毕 
 */
LocationTree2.prototype.locationZTreeLoaded = function(treeContainerId) {
	
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
LocationTree2.prototype.getSelectedTreeNode = function() {
	console.log(this.currSelectedTreeNode);
	return this.currSelectedTreeNode;
}

/**
 *  获取当前结节的traceId
 *  traceId形式（ID-ID-ID-ID）
 */
 LocationTree2.prototype.getTraceIds = function() {
	if (this.currSelectedTreeNode != null) {
		return this.currSelectedTreeNode.traceIds;
	}
	return null;
}

/**
 * 	取消选中的节点
 */
 LocationTree2.prototype.cancelAllSelectedNode = function() {	
	var treeObj = this.ztreeObj;
	treeObj.cancelSelectedNode();	// 取消所有选中的节点
	this.currSelectedTreeNode = null;// 设置当前选中的节点为空
}


//====================事件回调函数==================

 /*
 * 事件回调-异步加载之前
 * treeId   界面中Tree UI 组件ID
 */
 LocationTree2.prototype.zTreeBeforeAsync = function(treeId, treeNode) {
 	console.log("before async is called!");
 	console.log("before async     treeId is:"+treeId);
 	console.log("before async     treeNode is:"+treeNode);
 	//var ztreeId="#"+treeId+" .location-ztree";
 	if (treeNode == null || typeof (treeNode) == "undefined") {
 		LocationTree2.prototype.locationZTreeLoading(treeId);	// ztree加载中
 		
 	}
 	return true;
 };


	
/*
* 事件回调-异步加载出现异常
*/
LocationTree2.prototype.zTreeOnAsyncError = function(event, treeId, treeNode, XMLHttpRequest,textStatus, errorThrown) {
	console.log("async error is called!");
	if (treeNode == null || typeof (treeNode) == "undefined") {		
		LocationTree2.prototype.locationZTreeLoaded(treeId);// 删除 ztree加载时的提示信息
	}

	console.log("treeId:" + treeId + ", treeNode:" + treeNode);
	console.log("textStatus:" + textStatus);
	console.log("errorThrown:" + errorThrown);
	console.log("XMLHttpRequest:" + XMLHttpRequest);
};

LocationTree2.prototype.parseTraceIds = function(traceIds) {
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
 * 		that :   LocationTree2组件引用   
 * 		ztreeObj:ztree组件对象引用(是that的一部分)
 */
LocationTree2.prototype.searchLocation = function(parms,ztreeObj,that) {
	// zTreeLoading();//ztree加载中
	var url = BASE_CONTEXT_PATH + "/psmeter/search";// 需要提交的url
	
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
					that.idArr2 = LocationTree2.prototype.parseTraceIds(traceIds);					
					console.log("=======idArr2========="+JSON.stringify(that.idArr2));
					LocationTree2.prototype.markNodeInTree(0,that.idArr2,ztreeObj,that); // 在Tree中标记结点
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

LocationTree2.prototype.reAsyncChildNodes = function(traceIds,ztreeObj) {

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
LocationTree2.prototype.markNodeInTree = function(startIndex,idArr2x,ztreeObj,that) {
	
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
				
				LocationTree2.prototype.reAsyncChildNodes(traceArr[traceArr.length - 1],ztreeObj); // 异步加载在Tree中可以找到的最近的父节点的子节点.

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
 *   treeId  LocationTree2'id  与初始化函数中的treeId相同  
 *   ztreeObj ztreeObj引用  
 * 
 */
LocationTree2.prototype.initSearchPanel=function(treeId,ztreeObj,that){
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
		LocationTree2.prototype.searchLocation(parms,ztreeObj,that);//根据条件查询地理位置并展开对应节点		
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
		
		LocationTree2.prototype.searchLocation(parms,ztreeObj,that);//根据条件查询地理位置并展开对应节点
		
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
		
		LocationTree2.prototype.searchLocation(parms,ztreeObj,that);//根据条件查询地理位置并展开对应节点
		
	});
}
