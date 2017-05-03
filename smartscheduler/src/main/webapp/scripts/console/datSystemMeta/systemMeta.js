/**
 * 打开选人框
 * 
 * @returns
 */
function _openUserDlg() {
	var id = "owners_people_value";
	var url = $("#basePath").val() + "/console/tag/user/index.xsp?id=" + id
			+ "&documentId=&singleSelect=false";
	window.open(encodeURI(url));
}
/**
 * 打开选角色框
 * 
 * @returns
 */
function _openRoleDlg() {
	var id = "owners_role_value";
	var url = $("#basePath").val() + "/console/role/tag/index.xsp?id=" + id
			+ "&documentId=&singleSelect=false";
	window.open(encodeURI(url));
}

/**
 * 打开管理员配置窗口
 * 
 * @returns
 */
function openOwnerDlg() {
	$('#systemMetaOwner_dlg').dialog('open').dialog('setTitle', '系统元数据授权');
	$("#ownerFm").form('clear');
	$("#ownerFm_cataName").val($("#cataName").val());
	$.ajax({
		url : $("#basePath").val()
				+ "/console/systemMetaOwner/getOwnersData.action",
		type : "post",
		data : {
			cataId : $("#metaCataId").val()
		},
		dataType : "json",
		success : function(result) {
			$("#system_meta_owner_dlg_btnsave").linkbutton("enable");
			if (result.state == false) {
				$.messager.show({
					title : '温馨提示',
					msg : result.msg
				});
			} else {
				if (result.userIds)
					$("#owners_people_value_num").val(result.userIds);
				if (result.userNames)
					$("#owners_people_value").val(result.userNames);
				if (result.roleIds)
					$("#owners_role_value_num").val(result.roleIds);
				if (result.roleNames)
					$("#owners_role_value").val(result.roleNames);
			}
		},
		error : function() {
			alert("no no no ~");
		}
	});
}

/**
 * 添加或更新管理员
 * 
 * @returns
 */
function createOrUpdateOwner() {
	var values = $("#ownerFm").serialize();
	url=$("#basePath").val()+ "/console/systemMetaOwner/createOrUpdate.action?metaCataId="+$("#metaCataId").val();
	if ($("#ownerFm").form('validate')) {
		$("#system_meta_owner_dlg_btnsave").linkbutton("disable");
		$.ajax({
			url : encodeURI(url),
			type : "post",
			data : values,
			dataType : "json",
			success : function(result) {
				$("#system_meta_owner_dlg_btnsave").linkbutton("enable");
				if (result.state == false) {
					$.messager.show({
						title : '温馨提示',
						msg : result.msg
					});
				} else {
					$('#datagrid').datagrid('reload');
					$('#systemMetaOwner_dlg').dialog('close');
				}
			},
			error : function() {
				$("#system_meta_owner_dlg_btnsave").linkbutton("enable");
			}
		});
	}
}

/**
 * excel导出
 * 
 * @returns
 */
function exprotExcel() {
	var rows = $('#datagrid').datagrid('getChecked');
	var metaIds = [];
	var metaCataId = $("#metaCataId").val();
	if (rows.length > 0) {
		for (var i = 0; i < rows.length; i++) {
			metaIds.push(rows[i].metaId);
		}
		location.href = $("#basePath").val()
				+ "/console/systemMeta/export.action?metaIds[]=" + metaIds
				+ "&metaCataId=" + metaCataId;
	} else {
		alert("请选中要导出的数据");
	}

}
/**
 * excel导入
 * 
 * @returns
 */
function fileImport() {
	if (!/[xlsx|xls]$/.test($("#file").val())) {
		alert('上传文件类型必须是xls或者xlsx格式');
		return false;
	} else {
		$("#importMetaCataId").val($("#metaCataId").val());
		var formData = new FormData($("#uploadFm")[0]);
		$.ajax({
			url : $("#basePath").val() + '/console/systemMeta/import.action',
			type : 'POST',
			data : formData,
			dataType:'json',
			async : false,
			cache : false,
			contentType : false,
			processData : false,
			success : function(data) {
				if (data.state) {
					$.messager.show({ 
						title : '温馨提示',
						msg : data.msg
					});
					$('#upload_dlg').dialog('close');
					$('#datagrid').datagrid('reload');
				}else{
					$.messager.show({ // show error message
						title : '温馨提示',
						msg : data.msg
					});
				}
			},
			error : function(result) {
				alert("上传文件时出现错误，请联系管理员");
			}
		});
	}
}
/**
 * 打开上传窗口
 * 
 * @returns
 */
function openUploadDlg() {
	$('#upload_dlg').dialog('open').dialog('setTitle', 'excel导入');
	$("#uploadFm").form('clear');
}

/**
 * 打开新建窗口
 * 
 * @returns
 */
function openSystemMetaDlg() {
	$('#systemMeta_dlg').dialog('open').dialog('setTitle', '新建');
	$('#fm').form('clear');

	$("#fm_metaCataId").val($("#metaCataId").val())
}
/**
 * 打开更新窗口
 * 
 * @param row
 * @returns
 */
function updateSystemMeta(row) {
	if (row) {
		$('#systemMeta_dlg').dialog('open').dialog('setTitle', '更新');
		$('#fm').form('clear');
		$('#fm').form('load', row);
	}
}
/**
 * 添加或更新系统元数据
 * 
 * @returns
 */
function createOrUpdate() {
	var values = $("#fm").serialize();
	if ($("#fm").form('validate')) {
		$("#system_meta_dlg_btnsave").linkbutton("disable");
		$.ajax({
			url : $("#basePath").val()
					+ "/console/systemMeta/createOrUpdate.action",
			type : "post",
			data : values,
			dataType : "json",
			success : function(result) {
				$("#system_meta_dlg_btnsave").linkbutton("enable");
				if (result.state == false) {
					$.messager.show({
						title : '温馨提示',
						msg : result.msg
					});
				} else {
					$('#datagrid').datagrid('reload');
					$('#systemMeta_dlg').dialog('close');
				}
			},
			error : function() {
				$("#system_meta_dlg_btnsave").linkbutton("enable");
			}
		});
	}
}

/**
 * 系统元数据列表
 * 
 * @param metaName
 * @returns
 */
function datagrid(metaName) {
	$('#datagrid').datagrid({
		height : '95%',
		fit : true,
		url : $("#basePath").val() + '/console/systemMeta/findByPage.action',
		method : 'POST',
		queryParams : {
			"metaCataId" : $("#metaCataId").val(),
			"metaName" : metaName
		},
		striped : true,
		nowrap : true,
		pageSize : 10,
		pageNumber : 1,
		pageList : [ 10, 20, 30, 40, 50 ],
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
			field : 'metaId',
			title : 'metaId',
			width : 100,
			align : 'center',
			hidden : true
		}, {
			field : 'metaCataId',
			title : 'metaCataId',
			width : 100,
			align : 'center',
			hidden : true
		}, {
			field : 'metaName',
			title : '系统元数据名称',
			width : 250,
			align : 'center'
		}, {
			field : 'metaValue',
			title : '系统元数据值',
			width : 300,
			align : 'center'
		}, {
			field : 'description',
			title : '元数据描述',
			width : 200,
			align : 'center'
		},{
			field : 'extMeta',
			title : '扩展信息',
			width : 200,
			align : 'center'
		}, {
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
			updateSystemMeta(rowData);
		}
	});
}
/**
 * 条件搜索
 * 
 * @returns
 */
function searchByName() {
	datagrid($("#name").val());
}

function deleteSystemMeta() {
	var rows = $('#datagrid').datagrid('getChecked');
	if (rows.length > 0) {
		$.messager.confirm('温馨提示', '你真的要删除么?', function(r) {
			if (r) {
				var jsonData = {
					metaIds : []
				};
				for (var i = 0; i < rows.length; i++) {
					jsonData.metaIds.push(rows[i].metaId);
				}
				$.ajax({
					url : $("#basePath").val()
							+ "/console/systemMeta/delete.action",
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

$(function() {
	datagrid("");
});
