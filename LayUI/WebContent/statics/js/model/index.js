layui.use('element', function () {
    var $ = layui.jquery
        , element = layui.element;          
});
function toIndexSystem(){//去开发人员的系统表
	$("#demoAdmin").attr("src", "bmodel_Index.jsp")
}
function toIndexAdmin(){//去系统中栏目列表
	$("#demoAdmin").attr("src", "zhxxMenu_Index.jsp")
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

      