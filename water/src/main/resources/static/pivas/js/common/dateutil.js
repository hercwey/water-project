/**
 * 在原有日期基础上，增加days天数，默认增加1天 
 * @param date  指定的日期
 * @param days	需要增加的天数
 * @returns 返回的日期格式:	YYYY-MM-DD
 */
function addDate(date, days) {
    if (days == undefined || days == '') {
        days = 1;
    }
    var datex = new Date(date);
    datex.setDate(datex.getDate() + days);
    var month = datex.getMonth() + 1;
    var day = datex.getDate();
    return datex.getFullYear() + '-' + getFormatDate(month) + '-' + getFormatDate(day);
}

/*function addMonth(date, num) {
    num = parseInt(num);
    var sDate = dateToDate(date);

    var sYear = sDate.getFullYear();
    var sMonth = sDate.getMonth() + 1;
    var sDay = sDate.getDate();

    var eYear = sYear;
    var eMonth = sMonth + num;
    var eDay = sDay;
    while (eMonth > 12) {
      eYear++;
      eMonth -= 12;
    }

    var eDate = new Date(eYear, eMonth - 1, eDay);

    while (eDate.getMonth() != eMonth - 1) {
      eDay--;
      eDate = new Date(eYear, eMonth - 1, eDay);
    }

    return eDate;
  }*/

//在当前月份的基础上增加或减少月份
//date 当前日期
// offset月份数  正数或是负数均可.
//以下函数如offset为负数时错误.
function addMonth(date, offset) {
    if (date instanceof Date && !isNaN(offset)) {
        let givenMonth = date.getMonth();
        let newMonth = givenMonth + offset;
        date.setMonth(newMonth);
        return date;
    }
    else{
    	//throw Error('argument type error');
    	return null;
    }
    
}


function dateToDate(date) {
    var sDate = new Date();
    if (typeof date == 'object'   && typeof new Date().getMonth == "function"  ) {
      sDate = date;
    }
    else if (typeof date == "string") {
      var arr = date.split('-')
      if (arr.length == 3) {
        sDate = new Date(arr[0] + '-' + arr[1] + '-' + arr[2]);
      }
    }

    return sDate;
  }

/**
 * 返回当前系统日期
 * 日期格式:
 * 		YYYY-MM-DD
 */
function today() {
	var today = new Date();
	var h = today.getFullYear();
	var m = today.getMonth() + 1;
	var d = today.getDate();
	m = m < 10 ? "0" + m : m; //  这里判断月份是否<10,如果是在月份前面加'0'
	d = d < 10 ? "0" + d : d; //  这里判断日期是否<10,如果是在日期前面加'0'
	return h + "-" + m + "-" + d;
}


/**
 * 日期时间格式化处理
 * @param fmt
 * @param date
 * @returns
 */
function dateFtt(fmt,date)   
{ // author: meizz
  var o = {   
    "M+" : date.getMonth()+1,                 // 月份
    "d+" : date.getDate(),                    // 日
    "h+" : date.getHours(),                   // 小时
    "m+" : date.getMinutes(),                 // 分
    "s+" : date.getSeconds(),                 // 秒
    "q+" : Math.floor((date.getMonth()+3)/3), // 季度
    "S"  : date.getMilliseconds()             // 毫秒
  };   
  if(/(y+)/.test(fmt))   
    fmt=fmt.replace(RegExp.$1, (date.getFullYear()+"").substr(4 - RegExp.$1.length));   
  for(var k in o)   
    if(new RegExp("("+ k +")").test(fmt))   
  fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
  return fmt;   
} 

/**
 * 返回中文格式日期
 * @param date  日期 2017-07-21
 * @returns  返回  2017年07月21日
 */
function getCNFormatDate(date){
	var datex = new Date(date);
    /*date.setDate(date.getDate() + days);*/
    var month = datex.getMonth() + 1;
    var day = datex.getDate();
    return datex.getFullYear() + '年' + getFormatDate(month) + '月' + getFormatDate(day)+'日';
}

/**
 * 日期月份/天的显示，如果是1位数，则在前面加上'0'
 * @param arg
 * @returns
 */
function getFormatDate(arg) {
    if (arg == undefined || arg == '') {
        return '';
    }

    var re = arg + '';
    if (re.length < 2) {
        re = '0' + re;
    }

    return re;
}