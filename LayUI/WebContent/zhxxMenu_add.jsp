<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1">

<link rel="stylesheet" href="statics/layui/css/layui.css" media="all">
</head>
		<script src="statics/layui/layui.js"></script>
			<fieldset class="layui-elem-field layui-field-title"
				style="margin-top: 20px;">
				<legend>栏目添加</legend>
			</fieldset>
			<div class="site-text site-block">
				<form class="layui-form" action="sbmodel_doAdd">
					<div class="layui-form-item">
						<label class="layui-form-label">表名称</label>
						<div class="layui-input-inline">
							<input type="text" id="bmc" name="bmc" required
								lay-verify="required" placeholder="请输入表名称" autocomplete="off"
								class="layui-input">
						</div>
						<div class="layui-form-mid layui-word-aux"></div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">表名</label>
						<div class="layui-input-inline">
							<input type="text" id="bm" name="bm" required
								lay-verify="required" placeholder="请输入表名" autocomplete="off"
								class="layui-input">
						</div>
						<div class="layui-form-mid layui-word-aux"></div>
					</div>

					<div class="layui-form-item">
						<label class="layui-form-label">排序</label>
						<div class="layui-input-inline">
							<input type="text" id="orders" name="orders" required
								lay-verify="required" placeholder="请输入序列" autocomplete="off"
								value="0" onfocus="if (value =='0'){value =''}"
								onblur="if (value ==''){value='0'}" class="layui-input">
						</div>
						<div class="layui-form-mid layui-word-aux"></div>
					</div>
					<div class="layui-form-item">
						<div class="layui-input-block">
							<button class="layui-btn" lay-submit lay-filter="formDemo"
								onclick="toSubmit()">立即提交</button>
							&nbsp;<button type="reset" class="layui-btn layui-btn-primary" onclick="toBack()">返回</button>
						</div>
					</div>
					<script type="text/javascript">
					function toBack(){
						window.history.back(-1);
					}
					</script>
				</form>
			</div>
</html>
