<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">

<link rel="stylesheet" href="statics/css/exseen.css">
<link rel="stylesheet" href="statics/layui/css/layui.css" media="all">

</head>
			<fieldset class="layui-elem-field layui-field-title"
				style="margin-top: 20px;">
				<legend>字段编辑&nbsp;<button type="reset" class="layui-btn layui-btn-primary" onclick="toBack()">返回</button></legend>
			</fieldset>
			<div class="site-text site-block">
				<form class="layui-form" action="doUpdate" method="post">
					<div class="layui-form-item">
						<label class="layui-form-label">字段名</label>
						<div class="layui-input-inline">
							<input type="text" id="zdm" name="zdm" required  value="${map.zdm}"
								lay-verify="required" placeholder="请输入字段名" autocomplete="off"
								class="layui-input">
						</div>
						<div class="layui-form-mid layui-word-aux"></div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">项目名</label>
						<div class="layui-input-inline">
							<input type="text" id="zdmc" name="zdmc" required value="${map.zdmc}"
								lay-verify="required" placeholder="请输入项目名" autocomplete="off"
								class="layui-input">
						</div>
						<div class="layui-form-mid layui-word-aux"></div>
					</div>
					<input type="hidden" value="${map.types}" id="typesStr"/>
					<div class="layui-form-item">
						<label class="layui-form-label">字段类型</label>
						<div class="layui-input-inline">
							<select name="types" id="types"  >
								<option value="varchar">字符型</option>
								<option value="int">整型</option>
								<option value="datetime">时间型</option>
								<option value="text">文本型</option>
							</select>
						</div>
						<div class="layui-form-mid layui-word-aux">数据库中的类型</div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">字段宽度</label>
						<div class="layui-input-inline">
							<input type="text" id="zlong" name="zlong" required
								lay-verify="required" onfocus="if (value =='30'){value =''}"
								onblur="if (value ==''){value='30'}"  placeholder="仅限输入数字" value="${map.zlong }"
								oninput = "value=value.replace(/[^\d]/g,'')"
								 class="layui-input">
						</div>
						<div class="layui-form-mid layui-word-aux">数据库中的长度</div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">浏览序号</label>
						<div class="layui-input-inline">
							<input type="text" id="lisnum" name="lisnum" required 
								lay-verify="required|number" 
								oninput = "value=value.replace(/[^\d]/g,'')"
								placeholder="仅限输入整数"  value="${map.lisnum}" 
								autocomplete="off" class="layui-input">
						</div>
						<div class="layui-form-mid layui-word-aux"></div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">显示符</label>
						<div class="layui-input-block">
						<c:if test="${map.isshow =='1' }">
							<input type="radio" name="isshow" value="1" title="显示" checked>
							<input type="radio" name="isshow" value="0" title="不显示" >
						</c:if>
						<c:if test="${map.isshow =='0' }">
							<input type="radio" name="isshow" value="1" title="显示" >
							<input type="radio" name="isshow" value="0" title="不显示" checked>
						</c:if>
							
						</div>
						<div class="layui-form-mid layui-word-aux"></div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">提示信息</label>
						<div class="layui-input-inline">
							<input type="text" id="tips" name="tips" required
								lay-verify="required" onfocus="if (value =='请输入提示信息'){value =''}"
								onblur="if (value ==''){value='请输入提示信息'}"   placeholder="请输入提示信息" autocomplete="off"
								 value="${map.tips }"  class="layui-input">
						</div>
						<div class="layui-form-mid layui-word-aux"></div>
					</div>
					<input type="hidden" value="${map.formtypes}" id="formtypesStr"/>
					<div class="layui-form-item">
						<label class="layui-form-label">表单类型</label>
						<div class="layui-input-inline">
							<select name="formtypes" id="formtypes" lay-filter="formtypes">
								<option value="text">文本</option>
								<option value="textarea">多行文本</option>
								<option value="number">数字</option>
								<option value="date">日期</option>
								<option value="datetime">日期+时间</option>
								<option value="pic">图片</option>
								<option value="file">附件</option>
								<option value="select">下拉列表</option>
								<option value="radio">单项选择</option>
								<option value="checkbox">多项选择</option>
								<option value="country">国家</option>
								<option value="city">省市</option>

							</select>
						</div>
					</div>
					<div class="layui-form-item" id="formtypesOption"></div>
					<div class="layui-form-item">
						<label class="layui-form-label">初始值</label>
						<div class="layui-input-inline">
							<input type="text" id="initVal" name="initVal" 
								 placeholder="select标签等的值(不用输入)" value="${map.initVal }"
							class="layui-input">
						</div>
						<div class="layui-form-mid layui-word-aux"></div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">必填项</label>
						<div class="layui-input-block">
						<c:if test="${map.isform =='1' }">
							<input type="radio" name="isform" value="1" title="是" checked>
							<input type="radio" name="isform" value="0" title="否" >
						</c:if>
						<c:if test="${map.isform =='0' }">
						<input type="radio" name="isform" value="1" title="是" >
							<input type="radio" name="isform" value="0" title="否" checked>
						</c:if>
						
						</div>
						<div class="layui-form-mid layui-word-aux"></div>
					</div>

					<div class="layui-form-item">
						<label class="layui-form-label">可编辑性</label>
						<div class="layui-input-block">
						<c:if test="${map.isedit =='1' }">
							<input type="radio" name="isedit" value="1" title="可编辑" checked>
							<input type="radio" name="isedit" value="0" title="不可编辑" >
						</c:if>
						<c:if test="${map.isedit =='0' }">
							<input type="radio" name="isedit" value="1" title="可编辑" >
							<input type="radio" name="isedit" value="0" title="不可编辑" checked>
						</c:if>
							
						</div>
						<div class="layui-form-mid layui-word-aux"></div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">浏览宽度</label>
						<div class="layui-input-inline">
							<input type="text" id="width" name="width" required 
								lay-verify="required" onfocus="if (value =='180'){value =''}"
								onblur="if (value ==''){value='180'}"   placeholder="仅限输入数字" value="${map.width }"
								oninput = "value=value.replace(/[^\d]/g,'')"
								autocomplete="off" class="layui-input">
						</div>
						<div class="layui-form-mid layui-word-aux"></div>
					</div>
					

					<div class="layui-form-item">
						<label class="layui-form-label">关系式</label>
						<div class="layui-input-inline">
							<input type="text" id="jsdm" name="jsdm" required
								lay-verify="required" onfocus="if (value =='.'){value =''}"
								onblur="if (value ==''){value='.'}"    placeholder="请输入关系式" value="${map.jsdm }"
								autocomplete="off" class="layui-input">
						</div>
						
						<div class="layui-form-mid layui-word-aux">若无请设置为：点(.)   &nbsp; &nbsp;格式：(数量|数据库|源字段|本表目标字段(||级联字段))</div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">查询选项</label>
						<div class="layui-input-inline">
						<input type="text" id="isselect" name="isselect" required
								lay-verify="required" placeholder="请输入查询选项" value="${map.isselect }"
								autocomplete="off" class="layui-input">
						</div>
						
						<div class="layui-form-mid layui-word-aux"></div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">sql条件</label>
						<div class="layui-input-inline">
							<input type="text" id="sqlrale" name="sqlrale" required
							
								lay-verify="required" placeholder="请输入sql条件" value="${map.sqlrale }"
								autocomplete="off" class="layui-input">
						</div>
						<div class="layui-form-mid layui-word-aux"></div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">添加时保留原数</label>
						<c:if test="${map.iskeep =='1' }">
							<input type="radio" name="iskeep" value="1" title="保留" checked>
							<input type="radio" name="iskeep" value="0" title="不保留" >
						</c:if>
						<c:if test="${map.iskeep =='0' }">
						<input type="radio" name="iskeep" value="1" title="保留" >
							<input type="radio" name="iskeep" value="0" title="不保留" checked>
						</c:if>
						<div class="layui-form-mid layui-word-aux"></div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">字体</label>
						<div class="layui-input-inline">
							<input type="text" id="fontfamilly" name="fontfamilly" required  value="${map.fontfamilly }"
								lay-verify="required" onfocus="if (value =='宋体'){value =''}"
								onblur="if (value ==''){value='宋体'}"   placeholder="请输入字体" autocomplete="off"
								class="layui-input">
						</div>
						
						<div class="layui-form-mid layui-word-aux">(例：宋体)</div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">字体大小</label>
						<div class="layui-input-inline">
							<input type="text" id="fontsize" name="fontsize" required  value="${map.fontsize }"
								lay-verify="required" onfocus="if (value =='10'){value =''}"
								onblur="if (value ==''){value='10'}"   placeholder="仅限输入数字"
								oninput = "value=value.replace(/[^\d]/g,'')"
								autocomplete="off" class="layui-input">
						</div>
						<div class="layui-form-mid layui-word-aux"></div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">左边距</label>
						<div class="layui-input-inline">
							<input type="text" id="marleft" name="marleft" required  value="${map.marleft }"
								lay-verify="required" onfocus="if (value =='1'){value =''}"
								onblur="if (value ==''){value='1'}"   placeholder="仅限输入数字"
								oninput = "value=value.replace(/[^\d]/g,'')"
								autocomplete="off" class="layui-input">
						</div>
						<div class="layui-form-mid layui-word-aux"></div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">上边距</label>
						<div class="layui-input-inline">
							<input type="text" id="martop" name="martop"  value="${map.martop }" required
								lay-verify="required" onfocus="if (value =='1'){value =''}"
								onblur="if (value ==''){value='1'}"   oninput = "value=value.replace(/[^\d]/g,'')" placeholder="仅限输入数字" autocomplete="off"
								class="layui-input">
						</div>
						<div class="layui-form-mid layui-word-aux"></div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">备注</label>
						<div class="layui-input-inline">
							<input type="text" id="beizhu" name="beizhu"  value="${map.beizhu }"
								required lay-verify="required" onfocus="if (value =='1'){value =''}"
								onblur="if (value ==''){value='1'}"   placeholder="请输入备注"
								autocomplete="off" class="layui-input">
						</div>
						<div class="layui-form-mid layui-word-aux"></div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">高度</label>
						<div class="layui-input-inline">
							<input type="text" id="height" name="height"  value="${map.height }" required
								lay-verify="required" onfocus="if (value =='1'){value =''}"
								onblur="if (value ==''){value='1'}"  oninput = "value=value.replace(/[^\d]/g,'')" placeholder="仅限输入数字" autocomplete="off"
								class="layui-input">
						</div>
						<div class="layui-form-mid layui-word-aux"></div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">条件字段</label>
						<div class="layui-input-inline">
							<input type="text" id="api" name="api"  value="${map.api }" required
								onfocus="if (value =='.'){value =''}"
								onblur="if (value ==''){value='.'}" lay-verify="required" placeholder="请输入条件字段" autocomplete="off"
								class="layui-input">
						</div>
						若无请设置为：.
						<div class="layui-form-mid layui-word-aux"></div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">小数位</label>
						<div class="layui-input-inline">
							<input type="text" id="omit" name="omit" required
								lay-verify="required" placeholder="仅限输入数字" autocomplete="off"
								oninput = "value=value.replace(/[^\d]/g,'')"
								 value="${map.omit }" onfocus="if (value =='1'){value =''}"
								onblur="if (value ==''){value='1'}" class="layui-input">
						</div>
						<div class="layui-form-mid layui-word-aux"></div>
					</div>
					
					<input id="guid" name="guid" style="display: none"
								value="${map.guid}" required lay-verify="required"
								 autocomplete="off" class="layui-input">
					<input id="id" name="id" style="display: none"
						value="${map.id}" required lay-verify="required"
						 autocomplete="off" class="layui-input">
					<div class="layui-form-item">
						<div class="layui-input-block">
							<button class="layui-btn" >立即提交</button>
							<button type="reset" class="layui-btn layui-btn-primary">重置</button>
						</div>
					</div>
				</form>
			</div>
		<script src="statics/layui/layui.js"></script>
		<script type="text/javascript" src="statics/js/jquery-1.8.0.js"></script>
		<script src="statics/js/model/bzdk_edit.js"></script>
		<script src="statics/js/exseen.js"></script>
</html>
