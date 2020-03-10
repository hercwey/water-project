/**
 * 主页面JS
 * @returns
 */
$(function() {

	Layout.initTime();
	Layout.initShowSecondMenu();
});

var Layout = {
		
	/**
	 * 初始化主页面右上角时间
	 */
	initTime : function() {
		$(".welcome #showalltime").text(
				moment().format("YYYY-MM-DD HH:mm:ss dddd"));
		setInterval(function() {
			$(".welcome #showalltime").text(
					moment().format("YYYY-MM-DD HH:mm:ss dddd"));
		}, 1000);
	},

	/**
	 * 初始化显示二级菜单
	 */
	initShowSecondMenu : function() {
		var firstMenuId = $(".first-menu ul").find(".active").attr("data-first-menu-id");// 获取当前选中一级菜单的ID
		$(".second-menu-nav").hide();
		
		var secendMenuNavId = "second-menu-nav-" + firstMenuId;
		$("#"+secendMenuNavId).show();
		// 初始化二级菜单
		Layout.initSecondMenuNav(secendMenuNavId);
	},
	/**
	 * 初始化二级菜单 EasyUI Accordion 折叠面板
	 */
	initSecondMenuNav : function(secondMenuNavId) {
		// 初始化二级菜单
		$("#" + secondMenuNavId).accordion({
			fillSpace : true,
			fit : true,
			border : false,
			animate : false
		});
	},

	// 顶部导航切换
	headerNavSelect : function(node, id) {
		$(node).addClass("active").siblings().removeClass("active");
		Layout.initShowSecondMenu();

	},

}