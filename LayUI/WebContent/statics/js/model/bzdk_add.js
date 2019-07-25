$(document).ready(function() {
				var flag ="<%=request.getParameter('flag')%>";
				if (flag == -404) {
					layui.use("layer", function() {
						var layer = layui.layer; //layer初始化
						layer.msg('数据字段已存在..');
					})
				}
})