//通用打开/关闭对话框工具类
var DialogUtil=function(){
	
}
/*
 * 	打开(Open)对话框
 */
DialogUtil.openDialogFn=function(dialogId) {
	var dialog="#"+dialogId;
	
	var content=dialog+"  .modal-content";		
	var contentWidth=$(content).width();
	
	var mWidth=contentWidth+2-500;
	var left="-"+mWidth+"px";	
	console.log("----------对话框偏移量："+left);
	$(dialog).css({"margin-left":left});  //设置偏移量
	
	//可拖拽
	$("#"+dialogId).draggable({
		cursor : "move",
		handle : '.modal-header'
	});
	//点击对话框外部,不关闭对话框 
	$("#"+dialogId).modal({
		backdrop : 'static',
		keyboard : false
	});
}

/* 
 *	关闭(CLOSE)对话框
 */
DialogUtil.closeDialogFn=function(dialogId) {
	$("#"+dialogId).modal("hide");
}