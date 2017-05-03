/**
 * 获取资源文件信息
 * 
 * @param type
 */
function datagrid(name) {
	$('#datagrid').datagrid(
			{
				height : '95%',
				fit : true,
				url : $("#basePath").val()
						+ '/console/detInternationalization/shows.action',
				method : 'POST',
				queryParams : {
					"type" : $("#type").val(),
					"name":name
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
					field : 'id',
					title : 'id',
					width : 100,
					align : 'center',
					hidden : true
				}, {
					field : 'key',
					title : 'key',
					width : 200,
					align : 'center'
				}, {
					field : 'value',
					title : 'value值',
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
					editInternationalization(rowData);
				}
			});
}

function openDlg() {
	$('#internationalization_dlg').dialog('open').dialog('setTitle', ' ');
	$('#fm').form('clear');
	$("#resourceType").val($("#type").val());
	$("#key").textbox({disabled: false});  
}

function saveInternationalization() {
	var values = $("#fm").serialize();
	if ($("#fm").form('validate')) {
		$("#dlg_btnsave").linkbutton("disable");
		$.ajax({
			url : $("#basePath").val()
					+ '/console/detInternationalization/saveOrUpdate.action',
			type : "post",
			data : values,
			dataType : "json",
			success : function(result) {
				$("#dlg_btnsave").linkbutton("enable");
				if (result.state == false) {
					$.messager.show({
						title : '温馨提示',
						msg : result.msg
					});
				} else {
					$('#internationalization_dlg').dialog('close'); // close the
					// dialog
					$('#datagrid').datagrid('reload'); // reload the user data
				}
			}
		});
	}
}

function editInternationalization(row) {
	if (row) {
		$('#internationalization_dlg').dialog('open').dialog('setTitle', ' ');
		$('#fm').form('clear');
		$('#fm').form('load', row);
		//$("#key").textbox({disabled: true});  
	}
}

function destroyInternationalization() {
	var rows = $('#datagrid').datagrid('getChecked');
	if (rows.length > 0) {
		$.messager
				.confirm(
						'温馨提示',
						'你真的要删除么?',
						function(r) {
							if (r) {
								var jsonData = {
									internationalizationIds : []
								};
								for (var i = 0; i < rows.length; i++) {
									jsonData.internationalizationIds
											.push(rows[i].id);
								}
								$
										.ajax({
											url : $("#basePath").val()
													+ '/console/detInternationalization/destroyInternationalization.action',
											type : "post",
											data : jsonData,
											dataType : "json",
											success : function(result) {
												if (result.state == true) {
													$('#datagrid').datagrid(
															'reload');
												} else {
													$.messager.show({ // show
																		// error
																		// message
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

document.onkeydown=function(event){
    var e = event || window.event || arguments.callee.caller.arguments[0];       
     if(e && e.keyCode==13){ // enter 键
      var name = $('#name').val();
      datagrid(name);
    }
}; 

function searchByName(departmentId) {
	var name = $('#name').val();
		datagrid(name);
	
}

$(function() {
	datagrid("");
});