/**
 * 保存选中分类
 * @returns
 */
function saveCata(){
	var node=$("#cata").tree('getSelected');
	if(node){
		$("#metaCataId").textbox("setValue",node.id);
		$("#metaCataId").textbox("setText",node.text);
		$('#cataTreeDlg').dialog('close');
	}
}

/**
 * 打开分类选择窗口
 * @returns
 */
function openCataTree(){
	$('#cataTreeDlg').dialog('open').dialog('setTitle', '选择分类');
	$.ajax({
		url : $("#basePath").val()
				+ "/console/systemMetaCata/getSystemMetaCataTree.action",
		type : "post",
		dataType : "json",
		success : function(result) {
			if (result.state == false) {
				$.messager.show({
					title : '温馨提示',
					msg : result.msg
				});
			} else {
				$("#cata").tree({
					data:result.data
				});
				var node=$("#cata").tree('find',$("#metaCataId").val());
				$("#cata").tree('select',node.target);
			}
		},
		error : function() {
		}
	});
	
}

/**
 * 删除资源实体
 * @returns
 */
function deleteSystemMetaResourceDlg(){
	var rows = $('#datagrid').datagrid('getChecked');
	if (rows.length > 0) {
		$.messager.confirm('温馨提示', '你真的要删除么?', function(r) {
			if (r) {
				var jsonData = {
					metaResIds : []
				};
				for (var i = 0; i < rows.length; i++) {
					jsonData.metaResIds.push(rows[i].metaResId);
				}
				$.ajax({
					url : $("#basePath").val()
							+ "/console/systemMetaResource/delete.action",
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

/**
 * 新建系统元数据资源实体
 * @returns
 */
function createOrUpdate() {
	var values = $("#fm").serialize();
	if ($("#fm").form('validate')) {
		$("#system_meta_resource_dlg_btnsave").linkbutton("disable");
		$.ajax({
			url : $("#basePath").val()
					+ "/console/systemMetaResource/createOrUpdate.action",
			type : "post",
			data : values,
			dataType : "json",
			success : function(result) {
				$("#system_meta_resource_dlg_btnsave").linkbutton("enable");
				if (result.state == false) {
					$.messager.show({
						title : '温馨提示',
						msg : result.msg
					});
				} else {
					$('#datagrid').datagrid('reload');
					$('#systemMetaResource_dlg').dialog('close');
				}
			},
			error : function() {
				$("#system_meta_resource_dlg_btnsave").linkbutton("enable");
			}
		});
	}
}

/**
 * 打开新建窗口
 * @returns
 */
function openSystemMetaResourceDlg(){
	$('#systemMetaResource_dlg').dialog('open').dialog('setTitle', '新建');
	$('#fm').form('clear');
}
/**
 * 修改窗口
 * @param row
 * @returns
 */
function updateSystemMetaResourceDlg(row) {
	if (row) {
		$('#systemMetaResource_dlg').dialog('open').dialog('setTitle', '更新');
		$('#fm').form('clear');
		$('#fm').form('load', row);
		$.ajax({
			url : $("#basePath").val()
					+ "/console/systemMetaCata/getSystemMetaCataTree.action",
			type : "post",
			dataType : "json",
			success : function(result) {
				if (result.state == false) {
					$.messager.show({
						title : '温馨提示',
						msg : result.msg
					});
				} else {
					$("#cata").tree({
						data:result.data
					});
					var node=$("#cata").tree('find',$("#metaCataId").val());
					$("#metaCataId").textbox("setValue",node.id);
					$("#metaCataId").textbox("setText",node.text);
				}
			},
			error : function() {
			}
		});
	}
}
/**
 * 模糊搜索
 * @returns
 */
function searchByName() {
	datagrid($("#name").val());
}

function datagrid(resourceName) {
	$('#datagrid').datagrid({
		height : '95%',
		fit : true,
		url : $("#basePath").val() + '/console/systemMetaResource/findByPage.action',
		method : 'POST',
		queryParams : {
			"resourceName" : resourceName
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
			field : 'metaResId',
			title : 'metaResId',
			width : 100,
			align : 'center',
			hidden : true
		},  {
			field : 'resourceName',
			title : '资源实体名称',
			width : 250,
			align : 'center'
		}, {
			field : 'description',
			title : '描述',
			width : 200,
			align : 'center'
		},
		{
			field : 'metaCataId',
			title : 'metaCataId',
			width : 100,
			align : 'center',
			hidden : true
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
			updateSystemMetaResourceDlg(rowData);
		}
	});
}

$(function(){
	datagrid("");
});