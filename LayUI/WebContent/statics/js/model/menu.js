//去添加页面
		function toAddMenu(){
			var guid = $("#guid").val();
			window.location.href="toAddDataJsp?guid="+guid
		}
		function deleteDoc(id,guid,name){
			var msg = "您要删除'"+name+"'及所有子菜单项吗？"; 
			 if (confirm(msg)==true){ 
				 $.post("doDeleteMenu",
                 		{
					 	id : id,
                  		guid:guid
                 		},function(result){
                 			if (result=="delFinish") {
                 				alert("删除成功");
                 				window.location.href="toMenu";
							}else{
								alert("删除失败")
							}
	              });
			  return true; 
			 }else{ 
			  return false; 
			 } 
		}
		$(document).ready(function() {
        	var guid = "73c2efa3c34f4904ae0eee4ab31dfa79"
			toMenuIndex(guid)//加载菜单列表
        });
        function toMenuIndex(guid){
                   $.post("toMenuIndex",
                  		{
                   		guid:guid
                  		},function(result){
                  			document.getElementById('tbody').innerHTML=result; 
                  			$("#treeTable").treeTable({expandLevel : 3}).show();
                });
        }
        function updateSort() {
            $("#listForm").attr("action", "updateSort");
            $("#listForm").submit();
        }