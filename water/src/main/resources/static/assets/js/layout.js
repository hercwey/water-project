/*
 * @Description: 后台
 * @Author: Wang Rfan
 * @Date: 2019-07-25 23:38:50
 */

$(function () {

  $(".left-menu:first-child").css("display", "flex");
  // scroll init
  $(".left-menu-wrapper").niceScroll();
  $(".left-menu li").on('click', function () {
    $(".left-menu-wrapper").getNiceScroll().resize();
  })
  $(".page-container").niceScroll();

  // nav init
  $('.left-menu').metisMenu()
  .on('show.metisMenu', function (event) {
    console.log('show')
    var node = $(event.target);
    node.find('li').removeClass('mm-active');
    node.find('.mm-show').siblings('a').attr('aria-expanded', 'false');
    node.find('ul').removeClass('mm-show');
  }).on('shown.metisMenu', function (event) {
    $(".left-menu-wrapper").getNiceScroll().resize();
  }).on('hidden.metisMenu', function (event) {
    $(".left-menu-wrapper").getNiceScroll().resize();
  });
  
  Layout.headerNavSelect($("#headerNav  li.active")[0], 0);
  Layout.addNavClick();

  Layout.initTime();
})

var Layout = {
  initTime: function () {
    $("#date").text(moment().format("YYYY-MM-DD HH:mm:ss dddd"));
    setInterval(function () {
      $("#date").text(moment().format("YYYY-MM-DD HH:mm:ss dddd"));
    }, 1000);
  },

  // 顶部导航切换
  headerNavSelect: function (node, id) {
    $(node).addClass("active").siblings().removeClass("active");

    $("#menu" + id).siblings().hide();
    $("#menu" + id).css('display', 'flex');

    $("#topBreadcrumb").html("");
    $("#topBreadcrumb").prepend('<li>' + $("#headerNav li.active>a").text() + '</li>');
    $("#topBreadcrumb").prepend('<li>首页</li>');
    $("#topBreadcrumb  li").addClass("breadcrumb-item");
  },

  // 隐藏侧边栏
  toggleSidebar: function () {
    $("#contentWrapper").toggleClass("nav-collapse");
  },

  // 侧边导航初始化
  activeFirstNav: function () {
    // $(".left-menu ul.mm-show").siblings('a').attr('aria-expanded', 'false');
    $(".left-menu ul").removeClass("mm-show");
    $(".left-menu li").removeClass("mm-active");

    var firstLevel = $(".left-menu:visible>li:first-child");
    var node = firstLevel.children("a");
    firstLevel.addClass("mm-active");

    var secondUl = firstLevel.children("ul");
    if (secondUl.length > 0) {
      var secondLevel = secondUl.children("li:first-child");
      var thirdUl = secondLevel.children("ul");
      node = secondLevel.children("a");
      secondUl.addClass('mm-show').siblings('a').attr('aria-expanded', 'true');
      secondLevel.addClass("mm-active");
      if (thirdUl.length) {
        var thirdLevel = thirdUl.children("li:first-child");
        node = thirdLevel.children("a");
        thirdUl.addClass('mm-show').siblings('a').attr('aria-expanded', 'true');
        thirdLevel.addClass("mm-active");
      }
    }
    Layout.breadcrumbInit(node);
    Layout.addNavClick();
  },

  // 初始化面包屑
  breadcrumbInit: function (node) {
    $("#topBreadcrumb").html("");
    $("#topBreadcrumb").append('<li>' + node.text() + '</li>');
    node.parents('ul').each(function () {
      if (!$(this).hasClass('metismenu')) {
        $("#topBreadcrumb").prepend('<li>' +
          $(this).siblings('a').children(":not('.iconfont')").text() +
          '</li>');
      }
    });
    $("#topBreadcrumb").prepend('<li>' + $("#headerNav li.active>a").text() + '</li>');
    $("#topBreadcrumb").prepend('<li>首页</li>');
    $("#topBreadcrumb  li").addClass("breadcrumb-item");
  },

  // 增加侧边导航点击事件
  addNavClick: function () {
    $(".left-menu a").on("click", function (e) {
      var node = $(e.target);
      if(node.siblings('ul').length<=0){
        node.parent().addClass("mm-active");
      }
      Layout.breadcrumbInit($(this));
    })
  }
}