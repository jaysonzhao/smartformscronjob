/**
 * 根据输入的分类去查询元数据信息
 * 
 * @param metaType
 * @returns
 */
function metaTypeDatagrid(metaType, metaCataId) {
	$('#metaTypeDatagrid').datagrid({
		height : '100%',
		fit : true,
		url : $("#basePath").val() + '/console/systemMeta/findByPage.action',
		method : 'POST',
		queryParams : {
			"metaType" : metaType,
			"metaCataId" : metaCataId
		},
		striped : true,
		nowrap : true,
		pageSize : 10,
		pageNumber : 1,
		pageList : [ 10, 20, 30, 40, 50, 100, 200, 300 ],
		showFooter : true,
		loadMsg : '数据加载中请稍候……',
		pagination : true,
		singleSelect : false,
		checkOnSelect : false,
		selectOnCheck : false,
		columns : [ [ {
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
			width : 199,
			align : 'center'
		}, {
			field : 'metaValue',
			title : '系统元数据值',
			width : 199,
			align : 'center'
		}, {
			field : 'metaType',
			title : '系统元数据类型',
			width : 199,
			align : 'center'
		} ] ],
		onSelect : function(rowData) {
			$('#metaTypeDatagrid').datagrid("unselectAll");
		}
	});
}

/**
 * 添加或更新管理员
 * 
 * @returns
 */
function createOrUpdateOwner() {
	var exit = false;
	// 验证是否有授权资格
	$("#system_meta_owner_dlg_btnsave").linkbutton("disable");
	$.ajax({
		url : $("#basePath").val()
				+ "/console/systemMetaOwner/verification.action",
		type : "post",
		data : {
			metaType : $("#_type").val()
		},
		dataType : "json",
		async : false,
		success : function(result) {
			if (result.state == false) {
				$.messager.show({
					title : '温馨提示',
					msg : result.msg
				});
			} else {
				if (result.isPass == true) {
					$("#system_meta_owner_dlg_btnsave").linkbutton("enable");
				} else {
					alert("你没有修改权限");
					exit = true;
				}
			}
		}
	});

	if (exit)
		return;

	var values = $("#ownerFm").serialize();
	if ($("#ownerFm").form('validate')) {
		$("#system_meta_owner_dlg_btnsave").linkbutton("disable");
		$.ajax({
			url : $("#basePath").val()
					+ "/console/systemMetaOwner/createOrUpdate.action",
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
	var rows = $('#datagrid').datagrid('getChecked');
	if (rows.length == 1) {
		$('#systemMetaOwner_dlg').dialog('open').dialog('setTitle',
				'系统元数据管理员配置');
		$("#ownerFm").form('clear');
		$("#_type").val(rows[0].metaType);
		$("#_type").prop("readonly", true);
		metaTypeDatagrid($("#_type").val(), '');
		$.ajax({
			url : $("#basePath").val()
					+ "/console/systemMetaOwner/getOwnersData.action",
			type : "post",
			data : {
				metaType : $("#_type").val()
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

			}
		});
	}else{
		alert("请选择一种类型授权");
	}
}

function searchMetas() {
	datagrid($("#metaName").val(), $("#metaType").val());
}

function datagrid(metaName, metaType) {
	$('#datagrid').datagrid(
			{
				height : '95%',
				fit : true,
				url : $("#basePath").val()
						+ '/console/systemMetaOwner/findByPage.action',
				method : 'POST',
				queryParams : {
					"metaName" : metaName,
					"metaType" : metaType
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
					field : 'metaType',
					title : '系统元数据类型',
					width : 300,
					align : 'center'
				}, {
					field : 'description',
					title : '元数据描述',
					width : 200,
					align : 'center'
				}, {
					field : 'metaTypeDescr',
					title : '类型描述',
					width : 200,
					align : 'center'
				}, {
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

				}
			});
}

$(function() {
	datagrid("", "");
});