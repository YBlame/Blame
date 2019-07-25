<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html style="overflow-x: auto; overflow-y: auto;">
<head>
<title>菜单管理</title>

<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta name="author" content="xingdongke/" />
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=8,IE=9,IE=10" />
<meta http-equiv="Expires" content="0">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Cache-Control" content="no-store">
<script src="statics/menuimages/jquery-1.8.3.js" type="text/javascript"></script>

<link href="statics/menuimages/bootstrap.min.css" type="text/css"
	rel="stylesheet" />
<link href="statics/menuimages/bootstrap-responsive.min.css"
	type="text/css" rel="stylesheet" />

<link href="statics/menuimages/bootstrap-table.css" rel="stylesheet" />
<script src="statics/menuimages/bootstrap-table.js"></script>
<script src="statics/menuimages/bootstrap-table-zh-CN.min.js"></script>

<script src="statics/menuimages/bootstrap.min.js" type="text/javascript"></script>

<!--[if lte IE 7]>
<link href="menuimages/font-awesome-ie7.min.css" type="text/css" rel="stylesheet" />
<![endif]-->
<!--[if lte IE 6]>
<link href="menuimages/bootstrap-ie6.min.css" type="text/css" rel="stylesheet" />
<script src="menuimages/bootstrap-ie.min.js" type="text/javascript"></script>
<![endif]-->

<link href="statics/menuimages/select2.min.css" rel="stylesheet" />
<script src="statics/menuimages/select2.min.js" type="text/javascript"></script>


<link href="statics/menuimages/jquery.validate.min.css" type="text/css"
	rel="stylesheet" />
<script src="statics/menuimages/jquery.validate.min.js"
	type="text/javascript"></script>
<script src="statics/menuimages/additional-methods.min.js"
	type="text/javascript"></script>

<link href="statics/menuimages/jbox.min.css" rel="stylesheet" />
<script src="statics/menuimages/jquery.jBox-2.3.min.js"
	type="text/javascript"></script>

<script src="statics/menuimages/WdatePicker.js" type="text/javascript"></script>
<script src="statics/menuimages/mustache.js" type="text/javascript"></script>

<!--jqGrid-->
<link href="statics/menuimages/ui.jqgrid.css" type="text/css"
	rel="stylesheet" />
<script src="statics/menuimages/grid.locale-cn.js"
	type="text/javascript"></script>
<script src="statics/menuimages/jquery.jqGrid.min.js"
	type="text/javascript"></script>
<script src="statics/menuimages/jquery.jqGrid.extend.min.js"
	type="text/javascript"></script>

<script src="statics/menuimages/layer.js"></script>
<link href="statics/menuimages/console.css" type="text/css"
	rel="stylesheet" />
<script src="statics/menuimages/console.js" type="text/javascript"></script>



<meta name="decorator" content="default" />
<link href="statics/menuimages/treeTable.min.css" rel="stylesheet"
	type="text/css" />
<script src="statics/menuimages/jquery.treeTable.min.js"
	type="text/javascript"></script>
<script src="statics/layui/layui.js"></script>
<link href="statics/layui/css/layui.css" rel="stylesheet"
	type="text/css" />
<script type="text/javascript">
	$(document).ready(function() {
		var guid = "73c2efa3c34f4904ae0eee4ab31dfa79"
		toMenuIndex(guid)//加载菜单列表
	});
	function toMenuIndex(guid) {
		$.post("toMenuIndex", {
			guid : guid
		}, function(result) {
			document.getElementById('tbody').innerHTML = result;
			$("#treeTable").treeTable({
				expandLevel : 10
			}).show();
		});
	}
	function updateSort() {
		$("#listForm").attr("action", "updateSort");
		$("#listForm").submit();
	}
</script>

</head>
<fieldset class="layui-elem-field layui-field-title" style="width: 100%; height: 100%;">
	<legend>菜单列表</legend>
</fieldset>
</li>
<form id="command" style=" width: 100%; height: 100%;padding: 0 10px 10px"
	class=" form-search form_user" action="/gene/sys/menu/" method="post">
	<ul class="ul-form">
		<button type="button" class="layui-btn layui-btn-primary layui-btn-sm"
			onclick="toAddMenu()" lay-event="add">添加一级菜单</button>
	</ul>
	<script type="text/javascript">
		//去添加页面
		function toAddMenu() {
			var guid = $("#guid").val();
			window.location.href = "toAddDataJsp?guid=" + guid
		}
		function deleteDoc(id, guid, name) {
			var msg = "您要删除'" + name + "'及所有子菜单项吗？";
			if (confirm(msg) == true) {
				$.post("doDeleteMenu", {
					id : id,
					guid : guid
				}, function(result) {
					if (result == "delFinish") {
						alert("删除成功");
						window.location.href = "toMenu";
					} else {
						alert("删除失败")
					}
				});
				return true;
			} else {
				return false;
			}
		}
	</script>
</form>
<form id="listForm" method="post">
	<input id="guid" name="guid" value="${guid }" style="display: none;width: 100%; height: 100%" />
	<div class="table_div">
		<table id="treeTable"
			class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th>名称</th>
					<th>链接</th>
					<th style="text-align: center;">排序</th>
					<th style="text-align: center;">可见</th>
					<th>权限标识</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody id="tbody">
	
			</tbody>
		</table>


		<div class="form-actions pagination-left" style="background: #fff;">
			<input id="btnSubmit" class="btn btn-primary" type="button"
				value="保存排序" onclick="updateSort();" />
		</div>

	</div>
</form>
</html>