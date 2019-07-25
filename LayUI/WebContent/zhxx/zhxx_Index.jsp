<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<link rel="stylesheet" href="statics/layui/css/layui.css" media="all">
</head>
		<script src="statics/layui/layui.js"></script>
		<script src="statics/js/concisejs.js"></script>
		<script type="text/javascript" src="statics/js/jquery-1.8.0.js"></script>
			<input id="guidBmodel" name="guidBmodel" style="display: none"
				value="<%=request.getParameter("guidBmodel")%>" >
			<input id="guid" name="guid" style="display: none"
				value="<%=request.getParameter("guid")%>">
			<!-- 表名称 -->
			<input type="hidden" id="bmc" value="<%=request.getParameter("bmc")%>">
			<!-- flag判断值 -->
			<input type="hidden" id="flag" value="<%=request.getParameter("flag")%>">
			<fieldset class="layui-elem-field layui-field-title"
				style="margin-top: 20px;">
				<legend><%=request.getParameter("bmc")%></legend>
			</fieldset>
			<form class='layui-form' id="vform" onsubmit="return false" >
				<div id="demoTable">
					<div id='button'  style="display: none" >
							&nbsp;&nbsp;&nbsp;<button type='button' id='reload_btn' class='layui-btn layui-inline' data-type='reload'>搜索</button>
							&nbsp;<button type="reset" id='reset' class="layui-btn layui-btn-primary">重置</button>
					</div>
				</div>
			</form>
			<table class="layui-hide" style="width: 98%;height: 99%" id="demo" lay-filter="test"></table>
			<script type="text/html" id="barDemo">
					<a class="layui-btn layui-btn-primary layui-btn-xs" lay-event="detail" >添加栏目</a>
  					<a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>
  					<a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a>
			</script>
			<script type="text/html" id="toolbarDemo">
  				<div class="layui-btn-container">
    				<button type="button" class="layui-btn layui-btn-primary layui-btn-sm" lay-event="add">添加</button>
				</div>
			</script>
			<script>
			$(document).ready(function() {
				//对查询条件的加载
		        var guid =$("#guid").val();
	        	$.ajax({
					    url:"queryCondition",//请求的url地址
					    dataType:"json",   //返回格式为json
					 	async : false,//请求是否异步，默认为异步，这也是ajax重要特性
					    data:{guid : guid},    //参数值
					    type:"POST",   //请求方式
					    success:function(con){
					        //请求成功时处理
							var divFrame="";//外层div
							var inputTitle="";//文本框标题
							var inputValue="";//文本框
							var inputKey="";//文本框对应
							var inputType="";//数据类型
							var inputHidden="";//隐藏域
							var input ="";//文本框最终定格
							var select = "";//select标签
							var strs =new Array();//创建数组
							var option="";//选项
							var selectTitle="";
							
							for (var i = 0; i < con.length; i++) {
								//根据单选，多选，下拉获取到初始值并且赋值给当前查询条件
								if (con[i].formtypes=='radio'||con[i].formtypes=='select'||con[i].formtypes=='checkbox') {
									var selectValues=con[i].initval;
									selectValues=selectValues+"";
									strs=selectValues.split(","); //字符分割 ;
									option +="<option value='请选择'>请选择"+con[i].zdmc+"</option>"
									for (a=0;a<strs.length ;a++ ) 
							    	{ 	
										selectTitle=con[i].zdmc;
							    		option += "<option value='"+strs[a].replace("|$|",",")+"'>"+strs[a].replace("|$|",",")+"</option> ";
							    	}
									input+="&nbsp;&nbsp;&nbsp;&nbsp;<div class='layui-input-inline' style='width:"+con[i].width+"px;'><select name='"+con[i].zdm+"'  id='"+con[i].zdm+"' >"+option+"</select></div>";
									option="";
								}
								if (con[i].formtypes=='number') {
									//inputValue的值会被存到一个隐藏的input标签里，标签name为所有文本框的名字和数据类型
									input+="&nbsp;&nbsp;&nbsp;&nbsp;<div class='layui-input-inline'><input type='number' id='keywords' name='"+con[i].zdm+"' placeholder='请输入"+con[i].zdmc+"' style='width:"+con[i].width+"px;' class='layui-input'></div>";
								}
								if (con[i].formtypes=='text'||con[i].formtypes=='textarea') {
									//inputValue的值会被存到一个隐藏的input标签里，标签name为所有文本框的名字和数据类型
									input+="&nbsp;&nbsp;&nbsp;&nbsp;<div class='layui-input-inline'><input type='text' id='keywords' name='"+con[i].zdm+"' placeholder='请输入"+con[i].zdmc+"' style='width:"+con[i].width+"px;' class='layui-input'></div>";
								}
								if (con[i].formtypes=='date') {
									
								}
							}
							inputKey = inputKey.substr(0, inputKey.length - 1); //截取关键词
							var br = "<br>";
							if(!input){
								br=""//拼接到标签
							}
							divFrame=""+br+"<div class='inputFrame'>"+input+"</div>"+br+""//拼接到标签
							$("#demoTable").prepend(divFrame); 
							var frame = $(".inputFrame").html();
							if (frame==null ||frame.length == 0) {
								
							}else{
								
								document.getElementById("button").style.display='block';
							}											    
						}
					});
			});
				layui.config({
					version : '1554901097999' //为了更新 js 缓存，可忽略
				});
				layui.use([ 'laydate', 'laypage', 'layer', 'table',
							'carousel', 'upload', 'element',
							'slider' ],function() {
									var form = layui.form;
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
									$(document).ready(function() {
										var guid =$("#guid").val();
										//获取表头并且获取数据
											var num =1;
											$.ajax({
											    url:"findDoc",//请求的url地址
											    dataType:"json",   //返回格式为json
											    async : false,//请求是否异步，默认为异步，这也是ajax重要特性
											    data:{guid:guid,num:num},    //参数值
											    type:"POST",   //请求方式
											    success:function(data){
								                    var doclist = data;
													var cols = [];
													var types ={
															type : 'checkbox',
															fixed : 'left'
													}
													cols.push(types);
													//遍历表头数据, 添加到数组中
								                    for (var k = 0; k < doclist.length; k++) {
								                        var zdm = doclist[k].zdm;
								                        var zdmc = doclist[k].zdmc;
								                        var width = doclist[k].width;
								                      	
								                       /* 	if ( doclist[k].formtypes=="date") {
								                       		var t ={
										                            field: zdm,
										                            title: zdmc,
										                            width : width,
										                            sort: true,
										                            templet: function (d) {
										                                var stamp = d.ZHKSRQ.split('(')[1].split(')')[0];
										                                var dateStr = cj.dateFormat(new Date(parseInt(stamp)), 'yyyy-MM-dd');
										                                return dateStr;
										                            }
										                        };
														}else if ( doclist[k].formtypes=="datetime"){
															
														}else{ */
															var t ={
										                            field: zdm,
										                            title: zdmc,
										                            width : width,
										                            sort: true,
										                            
										                        };
														/* } */
								                        
								                        cols.push(t);
								                    }
								                    var id={
								                    		field: 'guid',
															title : 'guid',
															hide : true
								                        }
								                    cols.push(id);
								                    var bar={
															title : '操作',
															width:200,
															fixed: 'right',
															align:'center',
															toolbar : '#barDemo'
								                        }
								                    cols.push(bar);
								                    // 然后开始渲染表格
								                    table.render({
								                    	elem : '#demo',
														height : 420,		//描述
														url : 'findDocTable?guid='+guid+"&num="+num//数据接口
														,
														title : '记录表',
														page : {
															layout: ['prev', 'page', 'next', 'skip', 'count'] //自定义分页布局
															,first: false //不显示首页
												            ,last: false //不显示尾页
														}, //开启分页
														limit: 7,
														limits: [3,5,10,20,50],
														done:function (res, curr, count) {
															this.where={};
										                    layer.close(index) //加载完数据
										                }
														,toolbar : '#toolbarDemo' //开启工具栏，此处显示默认图标，可以自定义模型，详见文档
														,
														cols : [cols]
										               
													}); 
												}
											});
									})
									//监听头工具栏事件
									table.on('toolbar(test)',
													function(obj) {
														var checkStatus = table
																.checkStatus(obj.config.id), data = checkStatus.data; //获取选中的数据
														switch (obj.event) {
														case 'add':
															var guidBmodel = $("#guidBmodel").val();
															if (guidBmodel==null||guidBmodel=="null"||guidBmodel==undefined||guidBmodel=="") {
																var guid = $("#guid").val();
																window.location.href = "toAddDataJsp?guid="+guid;
															}else{
																window.location.href = "toAddDataJsp?guid="+guidBmodel;
															}
															
															break;
														case 'delete':
															window.history.back(-1);
															break;
														}
														;
													});

									//监听行工具事件
									table.on('tool(test)', function(obj) { //注：tool 是工具条事件名，test 是 table 原始容器的属性 lay-filter="对应的值"
										var data = obj.data //获得当前行数据
										, layEvent = obj.event; //获得 lay-event 对应的值
										if (layEvent === 'detail') {
											var guid = data['guid'];
											openDetail(guid);
										} else if (layEvent === 'del') {
											layer.confirm('真的删除该行么', function(index) {
												var guid = data['guid'];
												var guidBmodel = $("#guid").val();
												layer.close(index);
												$.post("deleteDoc", {
													guid : guid,
													guidBmodel :guidBmodel
												}, function(result) {
													if (result=="delFinish") {
														layer.msg("删除成功...");
														obj.del();
													}else{
														layer.msg("删除成功...");
													}
												});
											});
										} else if (layEvent === 'edit') {
											var guid = data['guid'];//拿到一行数据中的guid
											var guidBmodel =$("#guid").val();//拿到模型表中的guid
											//方便显示表单
											window.location.href = "toUpdateDoc?guid="+guid+"&guidBmodel="+guidBmodel;
										}
									});
									// 打开查看按钮
									function openDetail(guid) {
										window.location = "findMenu?guid="+guid/* 该guid为当前展会guid */
									}
									//表格重载
									var active = {
								            reload: function(){
								               	var demoReload = $('#keywords').val();
								            	var keyName = $("#keywords").attr("name")
								            	var guid =$("#guid").val();
								            	var index = layer.msg("查询中,请稍等...",{icon:16,time:false,shade:0})
								            	
								            	var postData = $("#vform").serialize();
											    var tmpDic={};
											    for(var i in postData.split("&")){
											        var row=postData.split("&")[i];
											        tmpDic[row.split("=")[0]]=decodeURIComponent(row.split("=")[1]);
											    }
											    postData = decodeURIComponent(postData,true);
								                setTimeout(function(){
								                	table.reload('demo',{
									                    where: {
									                    	postData : postData
									                    },page:{
									                    	  curr:1
									                    }
									                });
								                	layer.close(index)
								                },300)
								            }
								        };
									//单击条件查询按钮
									$('#demoTable #reload_btn').on('click', function(){
										var type = $(this).data('type');
							            active[type] ? active[type].call(this) : '';
							        });
								});
				
			</script>
</html>
