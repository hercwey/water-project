//============加载页面通用部分===============
/* 
	功能描述：加载页
	参数说明：containerId 窗口id
		   url:请求加载的页面地址
		   parms:参数
		   callbackFunc:加载成功后的回调函数
 */
function loadPage(containerId, url, parms, callbackFunc) {
	var parms = parms;
	url = BASE_CONTEXT_PATH + url;
	if (callbackFunc == null) {
		callbackFunc = defaultCallbackFunc();
	}	
	//console.log("loadpage---url:"+url);
	//console.log("ID:"+containerId+"; URL:"+url);
	$(containerId).load(url, parms, callbackFunc);
}

/* 默认的回调函数 */
function defaultCallbackFunc() {
	//console.log("execute default callback function()!");
}

function clearForm(formId){
	$(':input','#'+formId)  
	 .not(':button, :submit, :reset, :hidden')  
	 .val('')  
	 .removeAttr('checked')  
	 .removeAttr('selected');  
	
}

/**
 * 闭包节流函数方法（可传参数）
 * @param Function fn 延时调用函数
 * @param Number delay 延迟多长时间
 * @return Function 延迟执行的方法
 */
var throttle = function (fn, delay) {
    var timer = null;
    return function () {
        var args = arguments; //参数集合
        clearTimeout(timer);
        timer = setTimeout(function () {
            fn.apply(this, args);
        }, delay);
    }
} 
