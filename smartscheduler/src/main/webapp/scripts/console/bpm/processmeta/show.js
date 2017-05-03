function openProcessmetaDlg() {
	$('#processmeta_dlg').dialog('open').dialog('setTitle', ' ');
	$('#fm').form('clear');
	$("#fm_workflowCataId").val($("#workflowCataId").val())
}

function updateProcessmetaDlg(row) {
	if (row) {
		$('#processmeta_dlg').dialog('open').dialog('setTitle', ' ');
		$('#fm').form('clear');
		$('#fm').form('load', row);
		//$("#fm_workflowCataId").val($("#workflowCataId").val())
	}
}

function createOrUpdateProcessmeta() {
	var values = $("#fm").serialize();
	if ($("#fm").form('validate')) {
		$("#process_meta_dlg_btnsave").linkbutton("disable");
		$.ajax({
			url : $("#basePath").val()
					+ "/console/bpm/processmeta/createOrUpdate.action",
			type : "post",
			data : values,
			dataType : "json",
			success : function(result) {
				$("#process_meta_dlg_btnsave").linkbutton("enable");
				if (result.state == false) {
					$.messager.show({
						title : '温馨提示',
						msg : result.msg
					});
				} else {
					$('#datagrid').datagrid('reload');
					$('#processmeta_dlg').dialog('close');
				}
			},
			error : function() {
				$("#process_meta_dlg_btnsave").linkbutton("enable");
			}
		});
	}
}

function datagrid(workflowName) {
	$('#datagrid').datagrid(
			{
				height : '95%',
				fit : true,
				url : $("#basePath").val()
						+ '/console/bpm/processmeta/findByPage.action',
				method : 'POST',
				queryParams : {
					"workflowCataId" : $("#workflowCataId").val(),
					"workflowName" : workflowName
				},
				striped : true,
				nowrap : true,
				pageSize : 17,
				pageNumber : 1,
				pageList : [ 17 ],
				showFooter : true,
				loadMsg : '数据加载中请稍候……',
				pagination : true,
				toolbar : "#toolbar",
				singleSelect : false,
				checkOnSelect : false,
				selectOnCheck : false,
				columns : [ [ {
					field : 'ck',
					checkbox : true
				}, {
					field : 'workflowId',
					title : 'workflowId',
					width : 100,
					align : 'center',
					hidden : true
				}, 
				 {
					field : 'workflowCataId',
					title : 'workflowCataId',
					width : 100,
					align : 'center',
					hidden : true
				}, 
				{
					field : 'workflowName',
					title : '流程名称',
					width : 250,
					align : 'center'
				}, {
					field : 'processAppId',
					title : '流程应用库id',
					width : 300,
					align : 'center'
				}, {
					field : 'bpdId',
					title : '流程图id',
					width : 300,
					align : 'center'
				}, {
					field : 'workflowCode',
					title : '流程编号',
					width : 200,
					align : 'center'
				}, {
					field : 'formPath',
					title : '表单页面路径',
					width : 200,
					align : 'center'
				}, {
					field : 'creatorName',
					title : '创建者',
					width : 100,
					align : 'center'
				}, {
					field : 'createTime',
					title : '创建时间',
					width : 200,
					align : 'center',
					formatter : function(value, rec, index) {
						var s = new Date(value).pattern("yyyy-MM-dd hh:mm:ss");
						return s;
					}
				}, {
					field : 'updateByName',
					title : '更新者',
					width : 100,
					align : 'center'
				}, {
					field : 'updateTime',
					title : '更新时间',
					width : 200,
					align : 'center',
					formatter : function(value, rec, index) {
						var s = new Date(value).pattern("yyyy-MM-dd hh:mm:ss");
						return s;
					}
				} ] ],
				onSelect : function(rowData) {
					$('#datagrid').datagrid("unselectAll");
				},
				onDblClickRow : function(rowIndex, rowData) {
					updateProcessmetaDlg(rowData);
				}
			});
}

function searchByName() {
	datagrid($("#name").val());
}


function destroyProcessmeta() {
	var rows = $('#datagrid').datagrid('getChecked');
	  if (rows.length > 0) {
		  $.messager.confirm(
						'温馨提示',
						'你真的要删除么?',
						function(r) {
							if (r) {
								var jsonData = {
										workflowIds : []
								};
								for (var i = 0; i < rows.length; i++) {
									jsonData.workflowIds.push(rows[i].workflowId);
								}
								$.ajax({
											url : $("#basePath").val()
											+ "/console/bpm/processmeta/delete.action",
											type : "post",
											data : jsonData,
											dataType : "json",
											success : function(result) {
												if (result.state == true) {
													$('#datagrid').datagrid(
															'reload');
												} else {
													$.messager.show({ // show error message
														title : '温馨提示',
														msg : result.msg
													});

												}
											}
										});
							}
						});
	}
}


$(function() {
	datagrid("");
});