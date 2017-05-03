$(function() {
	var postUrl="";
	$("#grid1").datagrid({
		url:"getallmime.action?r1="+Math.random(),
		toolbar:"#toolbar",
		pagination:"true",
		pageSize:"50",
		rownumbers:"true",
		method:"get",
		fitColumns:"true",
		fit:"true",
		onDblClickRow: function (rowIndex, rowData) {
			postUrl="updatemime.action";
			$("#dlgDatMime").dialog("open").dialog("setTitle", "编辑附件MIME类型");
			$('#form1').form('load', rowData);
		}
	});
	
	//新建MIME类型
	$("#btnnew").click(function(evt) {
		postUrl="createdatmime.action";
		$('#form1').form('clear');
    	$("#dlgDatMime").dialog("open").dialog("setTitle", "新建附件MIME类型"); 
    });
	
	//删除MIME类型
	$("#btndel").click(function() {
		var selRows = $('#grid1').datagrid('getChecked');  
		if (selRows.length>0) {
			var mimeids=[];
			$(selRows).each(function(index, item) {
				mimeids[index]=item.mimeId;
			});
			$.messager.confirm('提示', "是否删除选中的 "+selRows.length+" 个MIME类型？", function(r){
				if (r){
					$.post("deletedatmime.action", {mimeIds:mimeids}, function(data) {
						if (data.success) {
							alert("删除MIME类型成功！");
							$("#grid1").datagrid("load",{r2:Math.random()});
						} else {
							alert("删除MIME类型失败！");
						}
					});
				}
			});
		}
	});
	
	//对话框“保存”按钮点击事件
	$("#dlg_btnsave").click(function(evt) {
		var paramObj={};
		var values=$("#form1").serializeArray();
		//将数组转换为对象
		$(values).each(function(){  
			paramObj[this.name]=this.value;  
        }); 
		//表单验证成功后提交请求
		if ($("#form1").form('validate')) {
			$("#dlg_btnsave").linkbutton("disable");
			$.post(postUrl, paramObj, function(data) {
				$("#dlg_btnsave").linkbutton("enable");
				if (data.success) {
					alert("MIME类型保存成功！");
					$("#dlgDatMime").dialog("close");
					$("#grid1").datagrid("load", {r2:Math.random()});
				} else {
					alert("MIME类型保存失败，原因："+data.msg);
				}
			});
		}
	});
	
	//对话框“取消”按钮点击事件
	$("#dlg_btncancel").click(function(evt) {
		$('#dlgDatMime').dialog('close');
	});
});