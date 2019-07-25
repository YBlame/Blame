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
			<fieldset class="layui-elem-field layui-field-title"
				 style="margin-top: 30px;">
				<legend>栏目列表</legend>
				<input id="zhxxGuid" name="zhxxGuid" style="display: none"
				value="<%=request.getParameter("zhxxGuid")%>">
				<table class="layui-hide" id="demo" lay-filter="test"></table>
			</fieldset>
			<script type="text/html" id="barDemo">
  				<a class="layui-btn layui-btn-primary layui-btn-xs" lay-event="detail" >字段管理</a>
				<a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a>
			</script>
			<script src="statics/layui/layui.js"></script>
			<script type="text/javascript" src="statics/js/jquery-1.8.0.js"></script>
			<script type="text/html" id="toolbarDemo">
  				
			</script>
			<script>
				layui.config({
					version : '1554901097999' //为了更新 js 缓存，可忽略
				});

				layui.use([ 'laydate', 'laypage', 'layer', 'table',
										'carousel', 'upload', 'element',
										'slider' ],
								function() {
									var laydate = layui.laydate //日期
									, laypage = layui.laypage //分页
									, layer = layui.layer //弹层
									, table = layui.table //表格
									, carousel = layui.carousel //轮播
									, upload = layui.upload //上传
									, element = layui.element //元素操作
									, slider = layui.slider //滑块
									, index  = layer.load(1)
									//监听Tab切换
									element.on('tab(demo)', function(data) {
										layer.tips('切换了 ' + data.index + '：'
												+ this.innerHTML, this, {
											tips : 1
										});
									});
									setTimeout(function () {
										//执行一个 table 实例
										table.render({
											guid : 'guid',
											elem : '#demo',
											height : 420,
											url : 'sbmodel_findAll' //数据接口
											,
											title : '用户表',
											page : {
												layout: ['prev', 'page', 'next', 'skip', 'count'] //自定义分页布局
												,first: false //不显示首页
									            ,last: false //不显示尾页
											}, //开启分页
											limits: [3,5,10,20,50],
											limit: 10,
											toolbar : '#toolbarDemo' //开启工具栏，此处显示默认图标，可以自定义模型，详见文档
											,
											done:function () {
								                   layer.close(index) //加载完数据
								            },
											cols : [ [ //表头
											{
												type : 'checkbox',
												fixed : 'left'
											}, {
												field : 'bmc',
												title : '表名称',
												width : 190,
												sort: true
											}, {
												field : 'bm',
												title : '表名',
												width : 190,
												sort: true
											}, {
												field : 'orders',
												title : '序列',
												width : 190,
												sort: true
											}, {
												field : 'guid',
												title : '编号',
												width : 0,
												hide : true,
												sort: true
											}, {
												fixed : 'right',
												title : '操作',
												width : 280,
												align : 'center',
												toolbar : '#barDemo'
											}
											/* ,{field: 'sex', title: '性别', width:80, sort: true}
											,{field: 'score', title: '评分', width: 80, sort: true, totalRow: true}
											,{field: 'city', title: '城市', width:150} 
											,{field: 'sign', title: '签名', width: 200}
											,{field: 'classify', title: '职业', width: 100}
											,{field: 'wealth', title: '财富', width: 135, sort: true, totalRow: true}
											,{fixed: 'right', width: 165, align:'center', toolbar: '#barDemo'} */
											] ]
										});
									},50)

									//监听头工具栏事件
									table.on('toolbar(test)',
													function(obj) {
														var checkStatus = table
																.checkStatus(obj.config.id), data = checkStatus.data; //获取选中的数据
														switch (obj.event) {
														case 'add':
															window.location.href = "bmodel_add.jsp";
															break;
														};
													});
 										//监听行工具事件
									table.on('tool(test)', function(obj) { //注：tool 是工具条事件名，test 是 table 原始容器的属性 lay-filter="对应的值"
										var data = obj.data //获得当前行数据
										, layEvent = obj.event; //获得 lay-event 对应的值
										if (layEvent === 'detail') {
											var guid = data['guid'];
											openDetail(guid);
										} else if (layEvent === 'del') {
											layer.confirm('真的删除行么', function(
													index) {
												var guid = data['guid'];
												$.post("sbmodel_del",{
															guid: guid
														},function(result){
															if (result == "finish") {
																layui.use("layer", function() {
																	layer.msg('数据删除成功..');
																	obj.del(); //删除对应行（tr）的DOM结构
																})
															}else{
																layui.use("layer", function() {
																	layer.msg('数据删除失败..');
																})
															}
														});
												//向服务端发送删除指令
											});
										} else if (layEvent === 'edit') {
											var guid = data['guid'];
											var bmc =data['bmc']
											window.location = "doc_Index.jsp?guid="+ guid+"&bmc="+bmc;
										}
									});
									// 打开查看按钮
									function openDetail(guid) {
										window.location = "bzdk_Index.jsp?guid="+ guid;
									}
									//执行一个轮播实例
									carousel.render({
										elem : '#test1',
										width : '100%' //设置容器宽度
										,
										height : 200,
										arrow : 'none' //不显示箭头
										,
										anim : 'fade' //切换动画方式
									});
									//底部信息
									var footerTpl = lay('#footer')[0].innerHTML;
									lay('#footer').html(
											layui.laytpl(footerTpl).render({}))
											.removeClass('layui-hide');
								});
			</script>
</html>
