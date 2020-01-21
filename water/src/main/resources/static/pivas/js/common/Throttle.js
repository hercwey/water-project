/**
 * 防止重复提交
 */
function Throttle(fn){
	this.fn = fn;
}
/**
 * 闭包节流函数方法（可传参数）
 * @param Function fn 延时调用函数
 * @param Number delay 延迟多长时间
 * @return Function 延迟执行的方法
 */
Throttle.prototype.throttle = function (fn, delay) {
    var timer = null;
    return function () {
        var args = arguments; //参数集合
        clearTimeout(timer);
        timer = setTimeout(function () {
            fn.apply(this, args);
        }, delay);
    }
}

Throttle.prototype.submit = function (parms) {
	console.log("----------Throttle submit");
	var self = this;
	var postFun = self.fn;
	self.throttle(postFun, 200);
}
