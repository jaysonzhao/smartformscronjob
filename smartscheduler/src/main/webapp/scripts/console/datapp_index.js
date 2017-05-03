$(function() {
	var postUrl="";
	$("#grid1").datagrid({
		url:"getalldatapps.action?r1="+Math.random(),
		toolbar:"#toolbar",
		pagination:"true",
		pageSize:"50",
		rownumbers:"true",
		method:"get",
		fitColumns:"true",
		fit:"true",
		onDblClickRow: function (rowIndex, rowData) {
			postUrl="updatedatapp.action";
			$("#dlgDatApp").dialog("open").dialog("setTitle", "编辑应用库");
			$('#form1').form('load', rowData);
			$("#appName").textbox("readonly",true);
		}
	});

	//新建应用库
	$("#btnnew").click(function(evt) {
		postUrl="createdatapp.action";
		$('#form1').form('clear');
    	$("#dlgDatApp").dialog("open").dialog("setTitle", "新建应用库"); 
		$("#appName").textbox("readonly",false);
    });
	
	
	$("#btnExport").click(function() {
		var selRows = $('#grid1').datagrid('getChecked');  
		var appids=[];
		if (selRows.length>0) {
			$(selRows).each(function(index, item) {
				appids[index]=item.appId;
			});
	     var form=$("<form>");//定义一个form表单
		  form.attr("style","display:none");
		  form.attr("target","");
		  form.attr("method","post");
		  form.attr("action","export.action");
		  var input1=$("<input>");
		  input1.attr("type","hidden");
		  input1.attr("name","appId");
		  input1.attr("value",JSON.stringify(appids));
		  $("body").append(form);//将表单放置在web中
		  form.append(input1);
		  form.submit();//表单提交 
	   }else{
		 $.messager.show({
             title: '温馨提示',
             msg: '请选择要导出的应用库'
         });
	}
	});
			
	//删除应用库
	$("#btndel").click(function() {
		var selRows = $('#grid1').datagrid('getChecked');  
		if (selRows.length>0) {
			var appids=[];
			$(selRows).each(function(index, item) {
				appids[index]=item.appId;
			});
			var strAppIds=JSON.stringify(appids);
			$.messager.confirm('提示', "是否删除选中的 "+selRows.length+" 个应用库？", function(r){
				if (r){
					$.post("deletedatapp.action", {appids:appids}, function(data) {
						if (data.success) {
							alert("删除应用库成功！");
							$("#grid1").datagrid("load",{r2:Math.random()});
						} else {
							alert("删除应用库失败！");
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
					alert("应用库保存成功！");
					$("#dlgDatApp").dialog("close");
					$("#grid1").datagrid("load", {r2:Math.random()});
				} else {
					alert("应用库保存失败，原因："+data.msg);
				}
			});
		}
	});
	
	//对话框“取消”按钮点击事件
	$("#dlg_btncancel").click(function(evt) {
		$('#dlgDatApp').dialog('close');
	});
});

//“设计”列格式转换
function rowDesignFmt(val,row) {
	return "<a href='designer/"+row.appId+".xsp' target='_blank'>设计</a>";
}

//“打开”列格式转换
function rowRunFmt(val,row) {
	return "<a href='open/"+row.appId+".xsp' target='_blank'>打开</a>";
}