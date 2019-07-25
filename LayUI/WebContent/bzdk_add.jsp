<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1">
<meta name="renderer" content="webkit">
<link rel="stylesheet" href="statics/css/exseen.css">
<link rel="stylesheet" href="statics/layui/css/layui.css">
</head>
<%String user = request.getSession().getAttribute("user").toString();%>  
<%String role = request.getSession().getAttribute("role").toString();%>  
		
			<fieldset class="layui-elem-field layui-field-title"
				style="margin-top: 20px;">
				<legend>字段编辑
				&nbsp;<button type="reset" class="layui-btn layui-btn-primary" onclick="toBack()">返回</button></legend>
			</fieldset>
			<script type="text/javascript">
				function toBack(){
					window.history.back(-1);
				}
			</script>
			<div class="site-text site-block">
				<form class="layui-form" action="doAdd" method="post">
					<input id="guid" name="guid" style="display: none"
						value="<%=request.getParameter("guid")%>" required
						lay-verify="required" autocomplete="off" class="layui-input">
					<div class="layui-form-item">
						<label class="layui-form-label">字段名</label>
						<div class="layui-input-inline">
							<input type="text" id="zdm" name="zdm" required
								lay-verify="required" placeholder="请输入字段名" autocomplete="off"
								class="layui-input">
						</div>
						(例：MC)
						<div class="layui-form-mid layui-word-aux"></div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">项目名</label>
						<div class="layui-input-inline">
							<input type="text" id="zdmc" name="zdmc" required value="${msg }"
								 placeholder="请输入项目名" 
								class="layui-input">
						</div>
						(例：姓名)
						<div class="layui-form-mid layui-word-aux"></div>
					</div>
					
					<div class="layui-form-item">
						<label class="layui-form-label">字段类型</label>
						<div class="layui-input-inline">
							<select name="types" id="types">
								<option value="varchar">字符型</option>
								<option value="int">整型</option>
								<option value="datetime">时间型</option>
								<option value="text">文本型</option>
							</select>
						</div>
						<div class="layui-form-mid layui-word-aux"></div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">字段宽度</label>
						<div class="layui-input-inline">
							<input type="text" id="zlong" name="zlong" required
								lay-verify="required" placeholder="仅限输入数字" value="30"
								oninput="value=value.replace(/[^\d]/g,'')"
								onfocus="if (value =='30'){value =''}"
								onblur="if (value ==''){value='30'}" autocomplete="off"
								class="layui-input">
						</div>
						<div class="layui-form-mid layui-word-aux"></div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">浏览序号</label>
						<div class="layui-input-inline">
							<input type="text" id="lisnum" name="lisnum" required
								lay-verify="required|number"
								oninput="value=value.replace(/[^\d]/g,'')" placeholder="仅限输入整数"
								value="1" onfocus="if (value =='1'){value =''}"
								onblur="if (value ==''){value='1'}" autocomplete="off"
								class="layui-input">
						</div>
						(例：10)
						<div class="layui-form-mid layui-word-aux"></div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">显示符</label>
						<div class="layui-input-block">
							<input type="radio" name="isshow" value="1" title="显示" checked>
							<input type="radio" name="isshow" value="0" title="不显示">
						</div>
						<div class="layui-form-mid layui-word-aux"></div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">提示信息</label>
						<div class="layui-input-inline">
							<input type="text" id="tips" name="tips" required
								lay-verify="required" placeholder="请输入提示信息" autocomplete="off"
								value="请您输入" onfocus="if (value =='请您输入'){value =''}"
								onblur="if (value ==''){value='请您输入'}" class="layui-input">
						</div>
						<div class="layui-form-mid layui-word-aux"></div>
					</div>

					<div class="layui-form-item">
						<label class="layui-form-label">表单类型</label>
						<div class="layui-input-inline">
							<select name="formtypes" id="formtypes" lay-filter="formtypes" lay-search>
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
								 placeholder="select标签等的值(不用输入)" value="0"
							class="layui-input">
						</div>
						<div class="layui-form-mid layui-word-aux"></div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">必填项</label>
						<div class="layui-input-block">
							<input type="radio" name="isform" value="1" title="是" checked>
							<input type="radio" name="isform" value="0" title="否">
						</div>
						<div class="layui-form-mid layui-word-aux"></div>
					</div>

					<div class="layui-form-item">
						<label class="layui-form-label">可编辑性</label>
						<div class="layui-input-block">
							<input type="radio" name="isedit" value="1" title="可编辑" checked>
							<input type="radio" name="isedit" value="0" title="不可编辑">
						</div>
						<div class="layui-form-mid layui-word-aux"></div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">浏览宽度</label>
						<div class="layui-input-inline">
							<input type="text" id="width" name="width" required
								lay-verify="required" placeholder="仅限输入数字" value="180"
								oninput="value=value.replace(/[^\d]/g,'')" autocomplete="off"
								onfocus="if (value =='180'){value =''}"
								onblur="if (value ==''){value='180'}" class="layui-input">
						</div>
						<div class="layui-form-mid layui-word-aux"></div>
					</div>

					

					<div class="layui-form-item">
						<label class="layui-form-label">关系式</label>
						<div class="layui-input-inline">
							<input type="text" id="jsdm" name="jsdm" required
								lay-verify="required" placeholder="请输入关系式" value="."
								autocomplete="off" onfocus="if (value =='.'){value =''}"
								onblur="if (value ==''){value='.'}" class="layui-input">
						</div>
						格式：(数量|数据库|源字段|本表目标字段(||级联字段))
						<div class="layui-form-mid layui-word-aux"></div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">查询选项</label>
						<div class="layui-input-inline">
						<input type="text" id="isselect" name="isselect" required
								lay-verify="required" placeholder="请输入查询选项" 
								value="0"
								autocomplete="off" onfocus="if (value =='0'){value =''}"
								onblur="if (value ==''){value='0'}"
								autocomplete="off" class="layui-input">
						</div>
						<div class="layui-form-mid layui-word-aux"></div>
					</div>

					<div class="layui-form-item">
						<label class="layui-form-label">sql条件</label>
						<div class="layui-input-inline">
							<input type="text" id="sqlrale" name="sqlrale" required
								lay-verify="required" placeholder="请输入sql条件"
								value="session('user')='<%=user%>' and session('role')='<%=role%>' "
								autocomplete="off" onfocus="if (value =='1=1'){value =''}"
								onblur="if (value ==''){value='1=1'}" class="layui-input">
						</div>
						<div class="layui-form-mid layui-word-aux"></div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">添加时保留原数</label>
						<div class="layui-input-block">
							<input type="radio" name="iskeep" value="1" title="保留" checked>
							<input type="radio" name="iskeep" value="0" title="不保留">
						</div>
						<div class="layui-form-mid layui-word-aux"></div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">字体</label>
						<div class="layui-input-inline">
							<input type="text" id="fontfamilly" name="fontfamilly" required
								value="宋体" lay-verify="required"
								onfocus="if (value =='宋体'){value =''}"
								onblur="if (value ==''){value='宋体'}" placeholder="请输入字体"
								autocomplete="off" class="layui-input">
						</div>
						(例：宋体)
						<div class="layui-form-mid layui-word-aux"></div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">字体大小</label>
						<div class="layui-input-inline">
							<input type="text" id="fontsize" name="fontsize" required
								value="10" lay-verify="required" placeholder="仅限输入数字"
								oninput="value=value.replace(/[^\d]/g,'')" autocomplete="off"
								onfocus="if (value =='10'){value =''}"
								onblur="if (value ==''){value='10'}" class="layui-input">
						</div>
						<div class="layui-form-mid layui-word-aux"></div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">左边距</label>
						<div class="layui-input-inline">
							<input type="text" id="marleft" name="marleft" required value="1"
								lay-verify="required" placeholder="仅限输入数字"
								oninput="value=value.replace(/[^\d]/g,'')" autocomplete="off"
								onfocus="if (value =='1'){value =''}"
								onblur="if (value ==''){value='1'}" class="layui-input">
						</div>
						<div class="layui-form-mid layui-word-aux"></div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">上边距</label>
						<div class="layui-input-inline">
							<input type="text" id="martop" name="martop" value="1" required
								lay-verify="required" placeholder="仅限输入数字"
								oninput="value=value.replace(/[^\d]/g,'')" autocomplete="off"
								onfocus="if (value =='1'){value =''}"
								onblur="if (value ==''){value='1'}" class="layui-input">
						</div>
						<div class="layui-form-mid layui-word-aux"></div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">备注</label>
						<div class="layui-input-inline">
							<input type="text" id="beizhu" name="beizhu" value="1" required
								lay-verify="required" onfocus="if (value =='1'){value =''}"
								onblur="if (value ==''){value='1'}" placeholder="请输入备注"
								autocomplete="off" class="layui-input">
						</div>
						<div class="layui-form-mid layui-word-aux"></div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">高度</label>
						<div class="layui-input-inline">
							<input type="text" id="height" name="height" value="1" required
								lay-verify="required" placeholder="仅限输入数字"
								oninput="value=value.replace(/[^\d]/g,'')" autocomplete="off"
								onfocus="if (value =='1'){value =''}"
								onblur="if (value ==''){value='1'}" class="layui-input">
						</div>
						<div class="layui-form-mid layui-word-aux"></div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">条件字段</label>
						<div class="layui-input-inline">
							<input type="text" id="api" name="api" value="." required
								lay-verify="required" onfocus="if (value =='.'){value =''}"
								onblur="if (value ==''){value='.'}" placeholder="请输入条件字段"
								autocomplete="off" class="layui-input">
						</div>
						<div class="layui-form-mid layui-word-aux"></div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">小数位</label>
						<div class="layui-input-inline">
							<input type="text" id="omit" name="omit" required
								lay-verify="required" placeholder="仅限输入数字" autocomplete="off"
								oninput="value=value.replace(/[^\d]/g,'')" value="1"
								onfocus="if (value =='1'){value =''}"
								onblur="if (value ==''){value='1'}" class="layui-input">
						</div>
						<div class="layui-form-mid layui-word-aux"></div>
					</div>

					<div class="layui-form-item">
						<div class="layui-input-block">
							<button class="layui-btn" lay-submit lay-filter="formDemo" onclick="toSubmit">立即提交</button>
							<button type="reset" class="layui-btn layui-btn-primary">重置</button>
						</div>
					</div>
				</form>
			</div>
		<script type="text/javascript" src="statics/layui/layui.js"></script>
		<script type="text/javascript" src="statics/js/jquery-1.8.0.js"></script>
		
		<script src="statics/js/exseen.js"></script>
		<script src="statics/js/model/bzdk_add.js"></script>
		
</html>
