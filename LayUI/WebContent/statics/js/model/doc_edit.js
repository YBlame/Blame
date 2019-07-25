$(document).ready(function(){
				var flag =$("#flag").val();
				if (flag == -404) {
					layui.use("layer", function() {
						layer.msg('数据修改异常..请重试..');
					})
				} 
				var guid = $("#guid").val();//数据GUID
				var guidBmodel =$("#guidBmodel").val();//描述表字段
				//post方法获取数据表数据
				 $.post("toUpdateData",{
					 guid : guid,
					 guidBmodel :guidBmodel
					},function(data){
						//获取字段拼接表单
						 $.post("returnZdmList",{
							 	guidBmodel :guidBmodel
							 	},function(result){
							 		 var fromInput ="";
									 var isform = "";
									 var isedit= ""
									 var jsdm="";
									 for(var i=0;i<result.length;i++){
									 	var strOptionC = result[i].initval;
									 	var strsC= new Array(); //定义一数组 
								     	strsC=strOptionC.split(","); //字符分割 
										if (result[i].jsdmFlag!=0) {
											jsdm = "readonly  placeholder='请单击选择'  onclick='findJSDM(\""+result[i].api+"\",\""+result[i].guid+"\",this)' ";
										}else{
											if (result[i].zdm=='parentMenu') {
												jsdm = "   placeholder='请单击选择' readonly  onclick='findParentMenu(\""+result[i].guid+"\",this)' ";
											}else{
												jsdm = "";
											}
										}
										 if (result[i].isedit==1) {
											 isedit = "";
										 }else{
											 isedit="readonly";
										 }
										 if(result[i].isform==0){
									    		isform = "";
									    	}else{
									    		isform = "<span class='isform' style='color: red;font-size: 25px'>*</span>";
									    	}
										 var dataZdm=data[result[i].zdm];
										 if (typeof(dataZdm)=='undefined') {
											 dataZdm="";
										}
										 var dataName = data.parentName
										if (typeof(dataName)=='undefined') {
											dataName="";
										}
										 
										 var input = "";
										 switch (result[i].formtypes){
										    case "text"://文本类型
										    	if (result[i].zdm=='parentMenu') {
										    		input+="<input type='text' id='"+result[i].zdm+"' "+jsdm+" style='width:"+result[i].width+"px;' "+isedit+"  value='"+dataName+"'   placeholder='请输入"+ result[i].zdmc+"' autocomplete='off' class='layui-input'>";
										    		input+="<input type='hidden' id='hiddenInput' name='"+ result[i].zdm+"' value='"+data[result[i].zdm]+"'>"
										    	}else{
										    		input+="<input type='text' id='"+result[i].zdm+"' name='"+ result[i].zdm+"' "+jsdm+" style='width:"+result[i].width+"px;' "+isedit+"  value='"+dataZdm+"'   placeholder='请输入"+ result[i].zdmc+"' autocomplete='off' class='layui-input'>";
										    	}
										    	fromInput +="<div class='layui-form-item'>"
													+ "<label class='layui-form-label'>"+result[i].zdmc+isform+"</label>"
													+ "<div class='layui-input-inline'>"
													+ ""+input+"</div>"
															+ "<div class='layui-form-mid layui-word-aux'></div></div>";
										        break;
										   case "select"://下拉框类型
											   var selectDisabled ="";
										    	var strOption = result[i].initval;
										    	var strs= new Array(); //定义一数组 
										    	var option ="  ";
										    	strs=strOption.split(","); //字符分割 
										    	if (result[i].isedit==1) {
													selectDisabled="";
												}else{
													selectDisabled="disabled"
												}
										    	if(data[result[i].zdm]=="null"){
									    			option += "<option value='null' selected ='selected' >请选择</option> ";
									    		}
										    	for (a=0;a<strs.length ;a++ ) 
										    	{ 	
										    		if(data[result[i].zdm]==strs[a].replace("|$|",",")){
										    			option += "<option value='"+strs[a].replace("|$|",",")+"' selected = 'selected'>"+strs[a].replace("|$|",",")+"</option> ";
										    		}
										    		if(data[result[i].zdm]!=strs[a].replace("|$|",",")){
										    			option += "<option value='"+strs[a].replace("|$|",",")+"'>"+strs[a].replace("|$|",",")+"</option> ";
										    		}
										    	} 
										    	fromInput +="<div class='layui-form-item'>"
													+ "<label class='layui-form-label'>"+result[i].zdmc+isform+"</label>"
													+ "<div class='layui-input-inline'  style='width:"+result[i].width+"px;' >"
													+ "<select name='"+result[i].zdmc+"' id='"+result[i].zdmc+"'   "+selectDisabled+"  >"
															+ ""+option+""
															+ "</select></div>"
															+ "<div class='layui-form-mid layui-word-aux'></div></div>";
										    	break;
										   case "textarea"://文本域
						            	        fromInput +="<div class='layui-form-item layui-form-text'><label class='layui-form-label'>"+result[i].zdmc+isform+"</label><div class='layui-input-block'><textarea id='"+result[i].zdm+"'  name='"+result[i].zdm+"' placeholder='请输入"+result[i].zdmc+"' "+isedit+"  style='width:"+result[i].width+"px;height:"+result[i].height+"' class='layui-textarea'>"+data[result[i].zdm]+"</textarea></div></div>";
										        break;
										    case "number"://数字
										    
										    	fromInput +=" <div class='layui-form-item'><label class='layui-form-label'>"+result[i].zdmc+isform+"</label><div class='layui-input-inline'><input type='number' id='"+result[i].zdm+"' name='"+result[i].zdm+"' "+jsdm+" "+isedit+" style='width:"+result[i].width+"px;'  placeholder='仅限输入数字' lay-verify='required|number' value='"+data[result[i].zdm]+"'  autocomplete='off' class='layui-input'></div><div class='layui-form-mid layui-word-aux'></div></div>"
										    	break;
										   case "datetime"://时间
											   if (result[i].isedit==1) {
										   			fromInput +="<div class='layui-form-item'> <div class='layui-inline'><label class='layui-form-label'>"+result[i].zdmc+isform+"</label><div class='layui-input-inline'><input type='text' class='layui-input' name='"+result[i].zdm+"' "+jsdm+" id='test5' style='width:"+result[i].width+"px;' value='"+data[result[i].zdm]+"' readonly placeholder='yyyy-MM-dd HH:mm:ss'></div></div></div>";
											    	
												}else{
													fromInput +="<div class='layui-form-item'> <div class='layui-inline'><label class='layui-form-label'>"+result[i].zdmc+isform+"</label><div class='layui-input-inline'><input type='text' class='layui-input' name='"+result[i].zdm+"' "+jsdm+" readonly style='width:"+result[i].width+"px;' value='"+data[result[i].zdm]+"' placeholder='yyyy-MM-dd HH:mm:ss'></div></div></div>";
												}
										    	break;
										    case "checkbox"://多选
										    	var strOptionC = result[i].initval;
										    	var strsC= new Array(); //定义一数组 
										    	strsC=strOptionC.split(","); //字符分割 
										    	var optionC='';
										    	var valueC = data[result[i].zdm];
										    	var valC = new Array();
										    	valC = valueC.split(","); //字符分割 
										    	var cheack = "";
										    	for (f=0;f<strsC.length ;f++ ){ 
										    		if (result[i].isedit==1) {
										    			if(valC==strsC[f]){
										    				optionC += "<input type='checkbox' class='"+result[i].zdm+"' lay-skin='primary' checked  title='"+strsC[f]+"' value='"+strsC[f]+"'>";
														}else{
															optionC += "<input type='checkbox' class='"+result[i].zdm+"' lay-skin='primary' title='"+strsC[f]+"' value='"+strsC[f]+"'>";
														}
										    		}else{
										    			if(valC==strsC[f]){
										    				optionC += "<input type='checkbox' class='"+result[i].zdm+"' lay-skin='primary' disabled checked title='"+strsC[f]+"' value='"+strsC[f]+"'>";
														}else{
															optionC += "<input type='checkbox' class='"+result[i].zdm+"' lay-skin='primary'disabled title='"+strsC[f]+"' value='"+strsC[f]+"'>";
														}
									    			}
										    	} 
										    	optionC+="<input id='"+result[i].zdm+"' name='"+result[i].zdm+"' style='display: none'  >" 
										    	fromInput +=" <div class='layui-form-item'> <label class='layui-form-label'>"+result[i].zdmc+isform+"</label><div class='layui-input-block'>"+optionC+"</div></div>";
										    	break; 
										    case "radio"://单选
										    	var strOptionR = result[i].initval;
										    	var strsR= new Array(); //定义一数组 
										    	var optionR =" ";
										    	strsR=strOptionR.split(","); //字符分割 
										    	for (e=0;e<strsR.length;e++ ) 
										    	{ 	
										    		if (result[i].isedit==1) {
										    			if(data[result[i].zdm]==strsR[e]){
												    		optionR += "<input type='radio' name='"+result[i].zdm+"' value='"+strsR[e]+"' title='"+strsR[e]+"' checked>";
												    		}
												    		if(data[result[i].zdm]!=strsR[e]){
													    		optionR += "<input type='radio' name='"+result[i].zdm+"' value='"+strsR[e]+"' title='"+strsR[e]+"'>";
													    		}
													}else{
														if(data[result[i].zdm]==strsR[e]){
												    		optionR += "<input type='radio' name='"+result[i].zdm+"' value='"+strsR[e]+"' title='"+strsR[e]+"' checked>";
												    		}
												    		if(data[result[i].zdm]!=strsR[e]){
													    		optionR += "<input type='radio' name='"+result[i].zdm+"' disabled value='"+strsR[e]+"' title='"+strsR[e]+"'>";
													    		}
													}
										    		
										    	} 
										    	fromInput +=" <div class='layui-form-item'><label class='layui-form-label'>"+result[i].zdmc+isform+"</label><div class='layui-input-block'>"+optionR+"</div></div>";
										    	break;
										 }
					            	 }
									 fromInput+="<input id='guid' name='guid' style='display: none' value='${guid}'  required lay-verify='required' autocomplete='off' class='layui-input'>";
									 fromInput+="<input id='guidBmodel' name='guidBmodel' style='display: none' value='${guidBmodel}'  >";
									 fromInput+="<div class='layui-form-item'><div class='layui-input-block'><button class='layui-btn' onclick='toSubmit()' >立即提交</button><button type='button' onclick='bakcButton()'  class='layui-btn layui-btn-primary'>返回</button></div></div>"
									 var formtypes =  document.getElementById("layui-form");
								     formtypes.innerHTML = fromInput;
								     layui.use('form', function(){
											//初始化layUI
											var form = layui.form; 
											form.render();
											
										});
								   	//初始化时间
									  laydate.render({
									    elem: '#test5'
									    ,type: 'datetime'
									  });
							  });
				});
			  })
			  function bakcButton(){
					window.history.go(-1);
				}
			//查询菜单表的上级菜单
			function findParentMenu(guid,data){
		        $('#fatherVal').remove();
				var dataName= data.id;
				$.post("findParentMenuDes",{
					guid : guid
					 },function(rs){
							 $.post("findParentMenu",{
									guid : guid
									 },function(result){
										 if (result=="error") {
											 layui.use("layer", function() {
													layer.msg('数据获取异常..');
												})
										}else if(result=="empty"){
											layui.use("layer", function() {
												layer.msg('数据为空..');
											})
										}else{
										 var getValueMap= new Map(); 
										 var rsLength= rs.length;
										 var resultLength = result.length;
										 var trBody="";
										 var tdBody="";
										 var mapJson = "";
										 for (var f = 0; f < rs.length; f++) {
											 for(var i=0;i<resultLength;i++){
												 tdBody =""+result[i].name;
												 tdValue = ""+result[i].id;
												 trBody += " <li onclick='getValueMenu(\""+tdBody+"\",\""+dataName+"\",this)' value='"+tdValue+"'><div type=\'text\' readonly=\'readOnly\'  name='"+rs[f].zdm+"List' class=\'kwli\'>"+tdBody+"</div></li>"
											 }
											var width =rs[f].width;
											if (rs[f].zdm=="parentMenu") {
												mapJson=JSON.stringify(getValueMap);
												width =width-2;
												 var s = "<ul id=\'ulList\' class='ulList' style='position:absolute; height:150px; width:"+width+"px;  overflow:auto; z-index:99;'>"+trBody+"</ul>";
												 var fatherValue = "<input type='text' style='display:none;' id='fatherVal' name="+dataName+" value='"+tdValue+"'>"
											     $("input[id='"+rs[f].zdm+"']").after(s);  
												 $("input[id='"+rs[f].zdm+"']").after(fatherValue);  
											}
										}
								}
							});
					 });
				
			}
			//JSDM关联字段的值放入目标字段中;关闭列表
			function getValueMenu(data,father,dq){
				var guid = $(dq).attr("value");
				$("#"+father+"").removeAttr("name");
				document.getElementById(""+father+"").value=data;
				document.getElementById("fatherVal").value=guid;
				hideDiv();
			}
			//查找JSDM关联字段
			function findJSDM(api,guid,data){
				hideDiv();
				var guid = guid;
				var apiVal = "";
				if (api==".") {
					apiVal ="";
				}else{
					apiVal = $("#"+api).val();
				}
				//根据zdmc，guid查找对应jsdm在后台得到对应一行数据
				$.post("findJSDM",{
					api : api,
					apiVal : apiVal,
					zdmc : data.name,
					guid : guid
					 },function(result){
						 if (result.length==0) {
							 layui.use("layer", function() {
									layer.msg('未找到JSDM关联字段...');
								})
						}else if(result=="error") {
							layui.use("layer", function() {
								layer.msg('未找到JSDM关联的数据库或表...');
							})
						}else if(result=="errorJSDM") {
							layui.use("layer", function() {
								layer.msg('JSDM格式有误...');
							})
						}else if(result=="nullJSDM") {
							layui.use("layer", function() {
								layer.msg('该标签为级联标签请选择父级...');
							})
						}else{
							//得到后台对应jsdm将字段传到前台
							$.post("findJSDMZdm",{
								zdmc : data.name,
								guid : guid
								},function(jsdm){
									var jsdmVal ="";
									var fromVal ="";
									for(var i=0;i<jsdm.length;i++){
										 jsdmVal = jsdm[0];
										 fromVal = jsdm[1];
									}
									var parts =new Array();
									parts=jsdmVal.split(","); //字符分割 
									var froms =new Array();
									froms=fromVal.split(","); //字符分割 
									var trBody = "";
									var tdBody = "";
									var getValueMap= new Map(); 
									var mapJson = "";
									var ulShow = "";
									var name = "";
									var resultLength = result.length;
									var partsLen =parts.length;
									var fromsLen =froms.length;
									for(var i=0;i<resultLength;i++){
										tdBody = "";
										for (var a = 0; a <partsLen ; a++) {
											tdBody += ""+result[i][parts[a]]+",";
										}
										for (var b = 0; b <fromsLen ; b++) {
											for (var y = 0; y <partsLen ; y++) {
												name = froms[b];
												getValueMap[froms[b++]] =result[i][parts[y]];
											}
											b--;//自增值减一返回初始值
										}
										
										mapJson=JSON.stringify(getValueMap);
										tdBody = tdBody.slice(0,tdBody.length-1)
										trBody += " <li onclick='getValue("+mapJson+")'><div type=\'text\' readonly=\'readOnly\' name=\'"+name+"\' class=\'kwli\'>"+tdBody+"</div></li>"
									}
									var s = "<ul id=\'ulList\' class='ulList' style='position:absolute; height:150px; overflow:auto; z-index:99;'>"+trBody+"</ul>";
							    	$("input[name='"+data.name+"']").after(s);  
							  });
							
						}
					  });
			}
			$('html').bind('click', function(event) {
			    // IE支持 event.srcElement ， FF支持 event.target    
			    var evt = event.srcElement ? event.srcElement : event.target;    
			    if(evt.id == 'ulList' ) return; // 如果是元素本身，则返回
			    else {
			        $('#ulList').remove(); // 如不是则隐藏元素
			    }
			    
			});
			//JSDM关联字段的值放入目标字段中;关闭列表
			function getValue(map){
				//如果遍历map
				for(var prop in map){
				    if(map.hasOwnProperty(prop)){
				        $("input[name='"+prop+"']").val(map[prop]);
				    }
				}
				hideDiv();
			}
			//关闭列表
			function hideDiv(){
				$('#ulList').remove(); // 如不是则隐藏元素
			}

			
			//提交
			function fomrSubmit(form){
				 var guid= $("#guidBmodel").val();
				 var flag = false;
				 $.ajaxSetup ({
					async: false
				}); 
				 $.post("toAddData",{
						 	guid:guid
						},function(result){
							 for(var i=0;i<result.length;i++){
								 if(result[i].isform!=0){
									if (result[i].formtypes=="checkbox") {
										 var obj = document.getElementsByClassName(""+result[i].zdm+"");
										    var check_val = [];
										    for(k in obj){
										        if(obj[k].checked)
										            check_val.push(obj[k].value);
										    }
										   $("#"+result[i].zdm+"").val(check_val);
									}
									if (result[i].formtypes=="textarea") {
										if ($("#"+result[i].zdm+"").val()!="") {
											flag = true;
										}else{
											layui.use("layer", function() {
												layer.msg(result[i].tips+""+result[i].zdmc);
											})
											$("#"+result[i].zdm+"").focus();
											flag = false;
											return false;
										}
									}
									if($("input[name='"+result[i].zdm+"']").val()!="" ){
										flag = true;
									}else{
										layui.use("layer", function() {
											layer.msg(result[i].tips+""+result[i].zdmc);
										})
										$("input[name='"+result[i].zdm+"']").focus();
										flag = false;
										return false;
									}
								 }
							 } 
						})
				 return flag;
			 }