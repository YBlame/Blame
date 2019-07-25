layui.use('element', function () {
    var $ = layui.jquery
        , element = layui.element;          
});
function toIndex(){//去首页
	$("#demoAdmin").attr("src", "bmodel_Index.jsp")
}
function toSystem(){//系统管理
	$("#demoAdmin").attr("src", "toMenu")
}
function toRule(){//角色管理
	$("#demoAdmin").attr("src", "toRuleIndex")
}
function findZhxx(){//添加展会
	$("#demoAdmin").attr("src", "findZhxx")
}

      