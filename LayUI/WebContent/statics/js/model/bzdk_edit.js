//layui初始化
layui.use('form', function(){
				var form = layui.form; 
				form.render();
				});
			//配置选项（select/checkbox/radio）
			layui.use('form', function() {
				var form = layui.form;
				form.render();
				debugger;
				form.on('select(formtypes)', function(e) {
					var val = e.value;
					$(e).parent().find('.option').remove();
				    if (val === 'select' || val === 'checkbox' || val === 'radio') {
				        var option = GetOption();
				        var formtypes =  document.getElementById("formtypesOption");
				         formtypes.innerHTML = option;
				    }else{
				    	$('.option').remove();
				    }
				});
			});
			
//窗体加载事件
$(document).ready(function(){
				//由于layui，将后台值传入前台，并且赋值给下拉框
				 var x = document.getElementById("typesStr").value;
		         $("#types").val(x);
		         var y = document.getElementById("formtypesStr").value;
		         $("#formtypes").val(y);
				var flag = "<%=request.getParameter('flag')%>";
				if (flag==-404) {
					layui.use("layer",function(){
						var layer = layui.layer;  //layer初始化
						layer.msg('数据字段已存在..'); 
					})
				}
				//判断表单类型
				//生成出对应的详细列表
				 if(y=="radio"||y=="checkbox"||y=="select"){
					var initVal = $("#initVal").val();
					var strs= new Array(); //定义一数组 
				    strs=initVal.split(","); //字符分割 
				 	var option = '<div class="option">';
				 	for (a=0;a<strs.length ;a++ ){ 
				    	    option += '<div class="option-item"><span class="option-input-wrap"><input type="text" class="option-input-option" value="'+strs[a]+'"  placeholder="选项名"/></span>';
				    	    option += '<span class="option-sign-wrap" onclick="DelOption(this)">×</span></div>';
				 	}
				    	option += '<div class="add_option" onclick="AddOption(this)"><div class="add_option_sign">+</div><div class="add_option_words">添加选项</div></div>';
				    	option += '<div><a class="save_button" href="javascript:save_button()">保存操作</a></div>';
				    	  
				    	option += '<div style="clear:both"></div></div>';
				    	var formtypes =  document.getElementById("formtypesOption");
					    formtypes.innerHTML = option;
				    	
				}
			  })
//返回
function toBack(){
				window.history.back(-1);
			}