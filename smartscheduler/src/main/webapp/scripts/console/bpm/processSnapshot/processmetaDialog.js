function _openProcessMetaDlg() {
	$('#processMetaDlg').dialog('open').dialog('setTitle', ' ');
}

function saveCheckedMeta() {
	var rows = document.getElementById("iframe_processmeta").contentWindow.$(
			"#datagrid").datagrid('getChecked');
	if (rows.length > 0) {
		var bpdId = rows[0].bpdId;
		var processAppId = rows[0].processAppId;
		var workflowName = rows[0].workflowName;
		 $("#bpdId").textbox('setValue',bpdId);
		 $("#processAppId").textbox('setValue',processAppId);
		 $("#bpdName").textbox('setValue',workflowName);
		 $('#processMetaDlg').dialog('close');
	} else {
		$.messager.show({
			title : '温馨提示',
			msg : '请选择一个流程'
		});
	}
}