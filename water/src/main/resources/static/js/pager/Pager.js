/*
			分页组件ID
			组件id:
				pageComponentId HTML分页组件ID
 */
function Pager(pageComponentId) {
	var pageNum = 0; // 页号
	var pageSize = 0; // 页大小
	var searchCond = ""; // 查询条件
	var pagerId = "#" + pageComponentId; // 分页组件ID 对应于分页组件(HTML)
	var searchFunc = null;

	/*
	 * 功能: 分页对象初始化 参数: outSearchFunc 外部查询函数 当点击分页组件的页号部分时会调用此函数.
	 * 此函数的签名如下(参数部分):outSearchFunc(searchCond, pageNum, pageSize)
	 * 
	 */
	this.init = function(outSearchFunc, outSearchCond) {
		console.log("init function called!");
		searchFunc = outSearchFunc; // 外部查询函数
		searchCond = outSearchCond; // 查询条件
		setPageNoClickListener(); // 页号click-listener
	}

	/**
	 * 设置页号click事件监听器
	 */
	setPageNoClickListener = function() {
		console.log("into init pageno click listener!");
		console.log("css selector is:" + pagerId + " .pagination li a");
		$(pagerId + " .pagination li a").on('click', function(e) {
			console.log("page no clicked");
			var dataBind = $(this).attr("data-bind");
			// 当dataBind有数据时处理
			if (dataBind != "") {
				var pageArr = new Array();
				pageArr = dataBind.split("-");
				// 置分页数据
				pageNum = pageArr[0];
				pageSize = pageArr[1];
				// 加载页
				loadPage();
			}
		});
	}

	/*
	 *    此函数是否对外部可见?   覆盖全局函数?
	 */
	loadPage = function() {
		console.log("into page search");
		searchFunc(searchCond, pageNum, pageSize); // 外部函数
	}
}