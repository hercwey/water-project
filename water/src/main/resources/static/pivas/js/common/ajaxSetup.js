//$(function(){
	/*
	 * ajax请求通用部分
	 */
	$.ajaxSetup({
		contentType:"application/x-www-form-urlencoded;charset=utf-8",
		beforeSend: function(XMLHttpRequest){
			console.log("show loading");
			window.parent.Loading.show();
		},
		complete: function(XMLHttpRequest,textStatus){
			console.log("hide loading");
			window.parent.Loading.hide();
//			var sessionstatus=XMLHttpRequest.getResponseHeader("sessionstatus"); //通过XMLHttpRequest取得响应头，sessionstatus，
//			if(sessionstatus!=null && sessionstatus=="timeout"){
//				console.log("登录超时，重新登录！");
//				//关闭所有model
////				$('.modal').map(function() {
////				    $(this).modal('hide');
////				});
//				window.location.reload();
//				//提示，并自动跳转
//				//util.message('登录超时，请重新登录！','goLogin');
//			}
		},
		error: function(){
			console.log("ajax request error !");
		}
	 });
//});