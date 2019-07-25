<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1">

<link rel="stylesheet" href="../statics/layui/css/layui.css" media="all">
</head>
			<fieldset class="layui-elem-field layui-field-title"
				style="margin-top: 20px;">
				<legend>展会添加</legend>
			</fieldset>
			<div class="site-text site-block">
				<form class="layui-form" action="bmodel_doAdd">
					<div class="layui-form-item">
						<label class="layui-form-label">展会名称</label>
						<div class="layui-input-inline">
							<input type="text" id="zhmc" name="zhmc" required
								lay-verify="required" placeholder="请输入展会名称" autocomplete="off"
								class="layui-input">
						</div>
						<div class="layui-form-mid layui-word-aux"></div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">展会地址</label>
						<div class="layui-input-inline">
							<input type="text" id="zhdz" name="zhdz" required
								lay-verify="required" placeholder="请输入展会地址" autocomplete="off"
								class="layui-input">
						</div>
						<div class="layui-form-mid layui-word-aux"></div>
					</div>

				<div class="layui-form-item">
				    <div class="layui-inline">
				      <label class="layui-form-label">开始日期</label>
				      <div class="layui-input-inline">
				         <input type="text" class="layui-input" lay-key="1" id="zhksrq" name="zhksrq" placeholder="yyyy-MM-dd HH:mm:ss"></div>
				    </div>
				  </div>
				  <div class="layui-form-item">
				    <div class="layui-inline">
				      <label class="layui-form-label">结束日期</label>
				      <div class="layui-input-inline">
				         <input type="text" class="layui-input" lay-key="2" id="zhjsrq" name="zhjsrq" placeholder="yyyy-MM-dd HH:mm:ss"></div>
				    </div>
				  </div>
					<div class="layui-form-item">
						<div class="layui-input-block">
							<button class="layui-btn" lay-submit lay-filter="formDemo"
								onclick="toSubmit()">立即提交</button>
							&nbsp;<button type="reset" class="layui-btn layui-btn-primary" onclick="toBack()">返回</button>
						</div>
					</div>
					<script src="../statics/layui/layui.js"></script>
					<script type="text/javascript">
					layui.use('laydate', function(){
						 var laydate = layui.laydate;
						 laydate.render({
						    elem: '#zhksrq'
						    ,type: 'datetime',
						    trigger: 'click'
						  });
						 laydate.render({
							    elem: '#zhjsrq'
							    ,type: 'datetime',
							    trigger: 'click'
							  });
				   	});
					function toBack(){
						window.history.back(-1);
					}
					</script>
				</form>
			</div>
</html>
