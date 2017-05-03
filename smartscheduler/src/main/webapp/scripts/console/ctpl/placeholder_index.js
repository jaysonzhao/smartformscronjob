
function openPlaceholderDlg() {
	$('#placeholder_dlg').dialog('open').dialog('setTitle', '添加占位符');
	$('#fm').form('clear');
}

function updatePlaceholderDlg(row) {
	if (row) {
		$('#placeholder_dlg').dialog('open').dialog('setTitle', '修改占位符');
		$('#fm').form('clear');
		$('#fm').form('load', row);
	}
}

function createOrUpdate() {
	var values = $("#fm").serialize();
	if ($("#fm").form('validate')) {
		$("#placeholder_dlg_btnsave").linkbutton("disable");
		$.ajax({
			url : $("#basePath").val()
					+ "/console/placeholder/createOrUpdate.action",
			type : "post",
			data : values,
			dataType : "json",
			success : function(result) {
				$("#placeholder_dlg_btnsave").linkbutton("enable");
				if (result.state == false) {
					$.messager.show({
						title : '温馨提示',
						msg : result.msg
					});
				} else {
					$('#datagrid').datagrid('reload');
					$('#placeholder_dlg').dialog('close');
				}
			},
			error : function() {
				$("#placeholder_dlg_btnsave").linkbutton("enable");
			}
		});
	}
}

function datagrid(placeHolderName,formula) {
	$('#datagrid').datagrid(
			{
				height : '95%',
				fit : true,
				url : $("#basePath").val()
						+ '/console/placeholder/findByPage.action',
				method : 'POST',
				queryParams : {
					"placeHolderName" : placeHolderName,
					"formula" : formula
				},
				striped : true,
				nowrap : true,
				pageSize : 10,
				pageNumber : 1,
				pageList : [ 10,20,30,40,50 ],
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
					field : 'placeHolderName',
					title : '占位符名称',
					width : 250,
					align : 'center'
				}, {
					field : 'formula',
					title : '占位符公式',
					width : 300,
					align : 'center'
				}, {
					field : 'description',
					title : '占位符描述',
					width : 300,
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
					updatePlaceholderDlg(rowData);
				}
			});
}

function searchByName() {
	datagrid($("#name").val(),$("#ctplCode").val());
}

function destroy() {
	var rows = $('#datagrid').datagrid('getChecked');
	if (rows.length > 0) {
		$.messager.confirm('温馨提示', '你真的要删除么?', function(r) {
			if (r) {
				var jsonData = {
						placeHolderIds : []
				};
				for (var i = 0; i < rows.length; i++) {
					jsonData.placeHolderIds.push(rows[i].placeHolderId);
				}
				$.ajax({
					url : $("#basePath").val()
							+ "/console/placeholder/delete.action",
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