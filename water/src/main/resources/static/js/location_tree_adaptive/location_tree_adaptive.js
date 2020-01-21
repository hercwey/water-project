//地理位置树自适应
var LocationTreeAdaptive=function(){
	
}


/*
 * 	打开(Open)对话框
 */
LocationTreeAdaptive.adaptive=function(tableContainerId) {
	/*
		自动调整树的高度.
	*/
	$(window).on('resize',function(){
		const TREE_MIN_HEIGHT=450;
		const TABLE_CONTAINER_ID="#"+tableContainerId;
		const TREE_SELECTOR="ul.ztree";
		console.log("-------------resize triggered-------");
		//右侧树的高度(也可以参考table容器的高度)
		var tableHeight=$(TABLE_CONTAINER_ID).height()-35;
		if(tableHeight>TREE_MIN_HEIGHT){
			console.log("--------set height using table's height-------");
			$(TREE_SELECTOR).height(tableHeight);	
		}
		else{
			console.log("----------set location tree height--fixed height 400-------");
			$(TREE_SELECTOR).height(TREE_MIN_HEIGHT);
		}				
	});

}

