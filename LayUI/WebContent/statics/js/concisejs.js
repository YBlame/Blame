
//自定义js库 功能较少，后期按功能划分对象或模块化

var cj = {};
cj = {

    //取queryString
    queryString: function (strName) {
        var strHref = window.document.location.href;
        var intPos = strHref.indexOf("?");
        var strRight = strHref.substr(intPos + 1);

        var arrTmp = strRight.split("&");
        for (var i = 0; i < arrTmp.length; i++) {
            var arrTemp = arrTmp[i].split("=");

            if (arrTemp[0].toUpperCase() == strName.toUpperCase()) return arrTemp[1];
        }
        return "";
    },

    //Ajax
    doAjax: function (param) {
        var type = param.type || "POST";
        var url = param.url || "";
        var data = param.data || "";
        var contentType = param.contentType || "application/json; charset=utf-8";
        var dataType = param.dataType || "json";
    },

    //添加cookie 参数为json 后期添加重载
    addCookie: function (param) {
        //json参数
        var name = param.name || "{E1BCC3FE-0670-4F0F-9842-7BB6F0ECEA72}";
        var value = param.value || "null";
        var days = param.days || 1;

        var expiresHours = param.expiresHours || 24;
        //局部变量
        var cookieString = name + "=" + escape(value);

        if (days > 0) {
            var date = new Date();
            date.setDate(date.getDate() + days);
            cookieString = cookieString + ";expires=" + date.toGMTString();
        }

        cookieString = cookieString + ";path=/";
        document.cookie = cookieString;
    },

    //删除cookie
    removeCookie: function (name) {

        var date = new Date();

        date.setDate(date.getDate() - 1);

        document.cookie = name + "=null;expires=" + date.toGMTString();
    },

    //获取cookie
    getCookie: function (name) {
        var rlt = "";
        var cookieString = document.cookie;
        var len = cookieString.length;
        if (len > 0) {
            var start = cookieString.indexOf(name + "=");
            if (start != -1) {
                start = start + name.length + 1;
                var end = cookieString.indexOf(";", start);
                if (end == -1) {
                    end = len;
                }
                rlt = decodeURI(cookieString.substring(start, end));
            }
        }

        return rlt;

    },

    setCookie: function (key, value, expiredays) {
        var exdate = new Date();
        exdate.setDate(exdate.getDate() + expiredays);
        document.cookie = key + "=" + escape(value) + ((expiredays == null) ? "" : ";expires=" + exdate.toGMTString());
    },

    //获取所有cookie
    getAllCookies: function () {

        var rlt = [];
        var cookieString = document.cookie;
        var cookielen = cookieString.length;
        if (cookielen > 0) {
            var cookieArr = cookieString.split(';');
            for (var i = 0, len = cookieArr.length; i < len; i++) {

                var arr = cookieArr[i].split('=');
                rlt.join();

            }
        }
    },

    //元素是否在数组中存在
    arrayContains: function (arr, curr, isIgnoreCase) {

        var boo = false;
        if (arr.length > 0) {
            if (isIgnoreCase) { //是否忽略大小写
                curr = curr.toLowerCase();
                for (var i = 0, len = arr.length; i < len; i++) {

                    arr[i] = arr[i].toLowerCase();
                    if (arr[i] == curr) {
                        boo = true;
                        break;
                    }
                }
            } else {
                for (var i = 0, len = arr.length; i < len; i++) {

                    if (arr[i] == curr) {
                        boo = true;
                        break;
                    }
                }
            }
        }

        return boo;
    },
    ///毫米转换成像素
    mm2px: function (mm) {
        var px = Math.ceil(mm * 3);

        return px;
    },
    closeWin: function () {

        if (navigator.userAgent.indexOf("MSIE") > 0) {
            if (navigator.userAgent.indexOf("MSIE 6.0") > 0) {
                window.opener = null; window.close();
            }
            else {
                window.open('', '_top'); window.top.close();
            }
        }
        else {
            window.opener = null;
            window.open('', '_self', '');
            window.close();
        }
    },
    addLog: function (str, cz) {

        var ticks = new Date().getMilliseconds();
        $.ajax({
            type: "GET",
            url: "/Tools/AddLog?sj=" + str + "&cz=" + cz + "&ticks=" + ticks,
            cache: false,
            dataType: "json"
        });
    },
    button_th_length: 40,
    dateFormat: function (date, format) {
        var args = {
            "M+": date.getMonth() + 1,
            "d+": date.getDate(),
            "h+": date.getHours(),
            "m+": date.getMinutes(),
            "s+": date.getSeconds(),
        };
        if (/(y+)/.test(format))
            format = format.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var i in args) {
            var n = args[i];
            if (new RegExp("(" + i + ")").test(format))
                format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? n : ("00" + n).substr(("" + n).length));
        }
        return format;
    },
    frameDownload: function (iframeUrl) {
        var body = document.getElementsByTagName("body")[0];
        var downloadIframe = document.createElement("iframe");
        downloadIframe.setAttribute("src", iframeUrl);
        downloadIframe.setAttribute("style", "display:none;");
        body.appendChild(downloadIframe);
    }
};