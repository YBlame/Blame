//配置选项（select/checkbox/radio）
layui.use('form', function() {
	var form = layui.form;
	form.render();
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

//获取选项 HTML
function GetOption() {
  var option = '<div class="option">';
  for (var i = 0; i < 2; i++) {
      option += '<div class="option-item"><span class="option-input-wrap"><input type="text" class="option-input-option"  placeholder="选项名"/></span>';
      option += '<span class="option-sign-wrap" onclick="DelOption(this)">×</span></div>';
  }
  option += '<div class="add_option" onclick="AddOption(this)"><div class="add_option_sign">+</div><div class="add_option_words">添加选项</div></div>';
  option += '<div><a class="save_button" href="javascript:save_button()">保存操作</a></div>';
  
  option += '<div style="clear:both"></div></div>';
  return option;
}

//获取到添加的（select/checkbox/radio）的值   注：option-input-option
function save_button(){
	var result='';
	$(".option-input-option").each(function(){
    	result=result+ $(this).val()+',';
	});
	result = result.slice(0,result.length-1)
	document.getElementById("initVal").value=result;
	var oInputFocus = document.getElementById("initVal")
	oInputFocus.focus();
}

//增加选项
function AddOption(e) {
    if ($(e).parent().find('.option-item').length > 100) {
        alert('选项最多不超过 100 个');
        return;
    }
    var option = '<div class="option-item"><span class="option-input-wrap"><input type="text" class="option-input-option"   placeholder="选项名"/></span> <span class="option-sign-wrap" onclick="DelOption(this)">×</span></div>';
    $(e).parent().find('.option-item').last().after(option);
    $(e).parent().find('.option-item').last().find('input').focus();
}
//删除选项
function DelOption(e) {
    var length = $(e).parent().parent().find('.option-item').length;
    if (length > 2) {
        $(e).parent().remove();
    } else {
        alert('最少保留两个');
    }
}

