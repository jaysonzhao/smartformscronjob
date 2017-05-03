$(function(){
    var ctxPath=$("#ctxPath").val();
	var nspath=ctxPath+"/console/detview";  //命名空间路径
    var datAppId=$("#datAppId").val();

	$("#grid1").datagrid({
        url:nspath+"/getviews/"+datAppId+".action?r1="+Math.random(),
		toolbar:"#toolbar",
		pagination:"true",
		pageSize:"50",
		rownumbers:"true",
		method:"get",
		fitColumns:"true",
		fit:"true",
		onDblClickRow: function (rowIndex, rowData) {
			$("#iframe1").attr("src", nspath+"/modify/"+datAppId+"/"+rowData.viewId+".xsp?r1="+Math.random());
			$("#dlgEditView").window("open").window("setTitle", "编辑视图"); 
		}
	});

	//新建视图
	$("#btnnew").click(function(evt) {
		$('#form1').form('clear');
		$("#iframe1").attr("src", nspath+"/create/"+datAppId+".xsp?r1="+Math.random());
    	$("#dlgNewView").window("open").window("setTitle", "新建视图"); 
    });

	//删除视图
	$("#btndel").click(function(evt) {
		var selRows = $('#grid1').datagrid('getChecked');  
		if (selRows.length>0) {
			var viewIds=[];
			$(selRows).each(function(index, item) {
				viewIds[index]=item.viewId;
			});
			$.messager.confirm('提示', "是否删除选中的 "+selRows.length+" 个视图？", function(r){
				if (r){
					$.post(nspath+"/deleteview/"+datAppId+".action", {viewIds:viewIds}, function(data) {
						if (data.success) {
							alert("删除视图成功！");
							$("#grid1").datagrid("load",{r2:Math.random(), datappid:datAppId});
						} else {
							alert("删除视图失败！");
						}
					});
				}
			});
		}
	});

	//对话框“保存”按钮点击事件
	$("#dlg_btnsave").click(function(evt) {
		var viewObj={};
		viewObj["datApplication.appId"]=datAppId;
		viewObj.viewName=$("#viewName").textbox('getValue');
		viewObj.viewTitle=$("#viewTitle").textbox('getValue');
		//表单验证成功后提交请求
		if ($("#form1").form('validate')){
			$.post(nspath+"/createdetview/"+datAppId+".action", viewObj, function(data) {
				if (data.success) {
					alert("视图保存成功！");
					$("#dlgNewView").dialog("close");
					$("#grid1").datagrid("load", {r2:Math.random()});
				} else {
					alert("视图保存失败，原因："+data.msg);
				}
			});
		}
	});
	
	//对话框“取消”按钮点击事件
	$("#dlg_btncancel").click(function(evt) {
		$('#dlgNewView').dialog('close');
	});
});

//“打开”列格式转换
function gridOpenLinkFmt(val,row) {
	var ctxPath=$("#ctxPath").val();
	var nspath=ctxPath+"/console/detview";  //命名空间路径
	var datapp=row.datApplication;
	if (datapp) {
		return "<a href='"+nspath+"/open/"+datapp.appId+"/"+row.viewId+".xsp' target='_blank'>打开</a>";
	} else {
		return "";
	}
}