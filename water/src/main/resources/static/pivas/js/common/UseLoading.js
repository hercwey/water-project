/**
 * 显示/隐藏正在加载阴影
 */
var UseLoading = {
	show: function(){
		console.log("show loading");
		window.parent.Loading.show();
	},
	hide: function(){
		console.log("hide loading");
		window.parent.Loading.hide();
	},
		
}

