var getServerHttp = function (path) {
	var host = document.domain;
	console.log(host)
	if(host.indexOf('localhost')>=0 || host.indexOf('192.168.1.52')>=0){
		return 'http://localhost:18201/oa-auth'
	}
	return 'http://yun.eqbidding.com/oa-auth';
}

var createVueTemplate = function (str) {
    str = str.toString().match(/\/\*!?(?:\@preserve)?[ \t]*(?:\r\n|\n)([\s\S]*?)(?:\r\n|\n)\s*\*\//)[1];
    return str;
};

//获取地址栏参数值
var getUrlParam = function (name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); // 构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg); // 匹配目标参数
    if (r != null) {
        return decodeURIComponent(r[2]);
    }
    return null; //返回参数值
};

var dateFormat = function (fmt, date) {
    let ret;
    let opt = {
        "Y+": date.getFullYear().toString(), // 年
        "m+": (date.getMonth() + 1).toString(), // 月
        "d+": date.getDate().toString(), // 日
        "H+": date.getHours().toString(), // 时
        "M+": date.getMinutes().toString(), // 分
        "S+": date.getSeconds().toString() // 秒
        // 有其他格式化字符需求可以继续添加，必须转化成字符串
    };
    for (let k in opt) {
        ret = new RegExp("(" + k + ")").exec(fmt);
        if (ret) {
            fmt = fmt.replace(ret[1], (ret[1].length == 1) ? (opt[k]) : (opt[k].padStart(ret[1].length, "0")))
        };
    };
    return fmt;
};

(function () {
    var host = document.domain;
    var port = location.port;
    document.onkeydown = function (e) {
        if (e.keyCode === 13 && e.ctrlKey) {
            var url = document.cookie.split(";")[0].split("=")[1];
            window.open('http://' + host + ':' + port + url);
            console.log('http://' + host + ':' + port + url)
        }
    };
})();


Array.prototype.indexOf = function (val) {
    for (var i = 0; i < this.length; i++) {
        if (this[i] == val) return i;
    }
    return -1;
};

Array.prototype.remove = function (val) {
    var index = this.indexOf(val);
    if (index > -1) {
        this.splice(index, 1);
    }
};

// 转换时间格式
// 方法一
var dateForm = function (str) {
    var date = '';
    str += '';
    date += str.substring(0, 4) + '-';
    date += str.substring(4, 6) + '-';
    date += str.substring(6, 8) + ' ';
    date += str.substring(8, 10) + ':';
    date += str.substring(10, 12);
    return date
}
// 方法二
function writeCurrentDate(date) {
    var now = date || new Date();
    var year = now.getFullYear(); //得到年份
    var month = now.getMonth();//得到月份
    var date = now.getDate();//得到日期
    var day = now.getDay();//得到周几
    var hour = now.getHours();//得到小时
    var minu = now.getMinutes();//得到分钟
    var sec = now.getSeconds();//得到秒       
    var week;
    month = month + 1;
    // if (month < 10) month = "0" + month;
    // if (date < 10) date = "0" + date;
    // if (hour < 10) hour = "0" + hour;
    // if (minu < 10) minu = "0" + minu;
    // if (sec < 10) sec = "0" + sec;
    var arr_week = new Array("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六");
    week = arr_week[day]; 
    var time = {
        year,
        month,
        date,
        day: week,
        hour,
        minu,
        sec
    };
    return time;
}

// 获取cookie
var getCookie = function (name) {
    var arr;
    var reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
    if (arr = document.cookie.match(reg))
        return unescape(arr[2]);
    else
        return null;
};

// 登录拦截
function interceptor() {
    if(window.location.pathname === '/filem/web-admin/login.html' || window.location.pathname === '/web-admin/login.html') {
        return false;
    }
    var ADMINSESSION = getCookie('ADMINSESSION');
    if (ADMINSESSION) {
        return true;
    };
    window.top.location.href = '/filem/web-admin/login.html';
}
//interceptor();