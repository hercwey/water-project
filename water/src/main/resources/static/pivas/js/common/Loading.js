/**
 * 显示/隐藏正在加载阴影
 */
var Loading = {
	show: function(){
			var html = 
				'<div class="loading" style="position: absolute; left: 0; top: 0; right: 0; bottom: 0; display: -webkit-box; z-index:99999;">'+
				'	<div style="width:100%; height:100%; text-align:center; backgroud: rgba(0, 0, 0, 0.5);">'+
				'		<img style="width:100px; height:100px; margin-top:15%;" src="../images/loading.gif" title="正在努力加载...">'+
				'	</div>'+
				'</div>';
			$("html body").append(html);
	},
	hide: function(){
		$(".loading").remove();
	},
		
}

