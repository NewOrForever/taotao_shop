<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div>
	<a class="easyui-linkbutton" onclick="importIndex()">一键导入商品数据到索引库</a>
</div>
<script type="text/javascript">
function importIndex() {

	$.ajax({
		type: "post",
		url: "/solrIndex/import",
		dataType: "json",
		success: function(data){
			if(data.status == 200){
				$.messager.aler("提示！","导入索引库成功！");
			}else{
				$.messager.aler("提示！","导入索引库失败！");
			}
		},
		error: function(){
			$.messager.alert("提示！","导入索引库失败！");
		}
	});



	/* $.post("/index/import",function(data){
		if(data.status == 200){
			$.messager.alert('提示','导入索引库成功！！！！');
		}else{
			$.messager.alert('提示','导入索引库失败~~~~~');
		}
	}); */
}
</script>