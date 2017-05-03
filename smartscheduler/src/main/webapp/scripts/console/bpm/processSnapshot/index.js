function openProcessSnapshotDlg() {
	$('#bpmProcessSnapshot_dlg').dialog('open').dialog('setTitle', ' ');
	$('#fm').form('clear');
}

function updateProcessSnapshotDlg(row) {
	if (row) {
		$('#bpmProcessSnapshot_dlg').dialog('open').dialog('setTitle', ' ');
		$('#fm').form('clear');
		$('#fm').form('load', row);
	}
}

function createOrUpdateProcessSnapshot() {
	var values = $("#fm").serialize();
	if ($("#fm").form('validate')) {
		$("#bpmProcessSnapshot_dlg_btnsave").linkbutton("disable");
		$.ajax({
			url : $("#basePath").val()
					+ "/console/bpm/processSnapshot/createOrUpdate.action",
			type : "post",
			data : values,
			dataType : "json",
			success : function(result) {
				$("#bpmProcessSnapshot_dlg_btnsave").linkbutton("enable");
				if (result.state == false) {
					$.messager.show({
						title : '温馨提示',
						msg : result.msg
					});
				} else {
					$('#datagrid').datagrid('reload');
					$('#bpmProcessSnapshot_dlg').dialog('close');
				}
			},
			error : function() {
				$("#bpmProcessSnapshot_dlg_btnsave").linkbutton("enable");
			}
		});
	}
}

function datagrid(snapshotName) {
	$('#datagrid').datagrid(
			{
				height : '95%',
				fit : true,
				url : $("#basePath").val()
						+ '/console/bpm/processSnapshot/findByPage.action',
				method : 'POST',
				queryParams : {
					"snapshotName" : snapshotName
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
					field : 'snapshotId',
					title : 'snapshotId',
					width : 100,
					align : 'center',
					hidden : true
				}, {
					field : 'snapshotName',
					title : '快照名称',
					width : 250,
					align : 'center'
				}, {
					field : 'bpdName',
					title : '流程图名称',
					width : 250,
					align : 'center'
				}, {
					field : 'bpdId',
					title : '流程图id',
					width : 300,
					align : 'center'
				}, {
					field : 'snapshotBpdId',
					title : '流程图快照ID',
					width : 300,
					align : 'center'
				}, {
					field : 'processAppId',
					title : '流程图应用库id',
					width : 300,
					align : 'center'
				}, {
					field : 'snapshotCreateTime',
					title : '快照创建时间',
					width : 150,
					align : 'center'
				}, {
					field : 'formPath',
					title : '表单页面路径',
					width : 200,
					align : 'center'
				},

				{
					field : 'creator',
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
					field : 'updateBy',
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
					updateProcessSnapshotDlg(rowData);
				}
			});
}

function searchByName() {
	datagrid($("#name").val());
}

function destroyProcessSnapshot() {
	var rows = $('#datagrid').datagrid('getChecked');
	if (rows.length > 0) {
		$.messager.confirm('温馨提示', '你真的要删除么?', function(r) {
			if (r) {
				var jsonData = {
					snapshotIds : []
				};
				for (var i = 0; i < rows.length; i++) {
					jsonData.snapshotIds.push(rows[i].snapshotId);
				}
				$.ajax({
					url : $("#basePath").val()
							+ "/console/bpm/processSnapshot/delete.action",
					type : "post",
					data : jsonData,
					dataType : "json",
					success : function(result) {
						if (result.state == true) {
							$('#datagrid').datagrid('reload');
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

function sysncActivityMeta() {
	var rows = $('#datagrid').datagrid('getChecked');
	if (rows.length > 0) {
		$.messager.confirm('温馨提示', '环节同步将会重新同步环节信息,是否继续？', function(r) {
			if (r) {
				//开启遮罩层
				$.messager.progress({ 
					title: '正在同步...', 
					msg: '正在同步流程图环节，请稍等......', 
					text: '处理中...' 
				});
				var jsonData = {
					snapshotIds : []
				};
				for (var i = 0; i < rows.length; i++) {
					jsonData.snapshotIds.push(rows[i].snapshotId);
				}
				$.ajax({
					url : $("#basePath").val()
							+ "/console/bpm/processSnapshot/sysncActivityMeta.action",
					type : "post",
					data : jsonData,
					dataType : "json",
					success : function(result) {
						$.messager.progress('close');
						if (result.state == true) {
							$.messager.show({ // show error message
								title : '温馨提示',
								msg : '数据同步完成'
							});
						} else {
							$.messager.show({ // show error message
								title : '温馨提示',
								msg : result.msg
							});
						}
					},
					error: function() {
						$.messager.progress('close');
						alert("网络故障，无法同步流程环节！\r\n");
					}
				});
			}});
	}
}

function activityMetaCfg() {
	var rows = $('#datagrid').datagrid('getChecked');
	if (rows.length == 1) {
		var url = $("#basePath").val()
		+ '/console/bpm/activitymeta/node.xsp?snapshotId='
		+rows[0].snapshotId
		+"&bpdId="+rows[0].bpdId
		+"&processAppId="+rows[0].processAppId
		+"&snapshotBpdId="+rows[0].snapshotBpdId;
		window.open(url); 
	} else {
		$.messager.show({ // show error message
			title : '温馨提示',
			msg : '请选择一个流程快照'
		});
	}
}
function startPerssion(){
	var rows = $('#datagrid').datagrid('getChecked');
	if (rows.length == 1) {
		var url = $("#basePath").val()
		+ '/console/startPermissions/index.xsp?snapshotId='
		+rows[0].snapshotId;
		window.open(url); 
	} else {
		$.messager.show({ // show error message
			title : '温馨提示',
			msg : '请选择一个流程快照'
		});
	}
}
$(function() {
	datagrid("");
});