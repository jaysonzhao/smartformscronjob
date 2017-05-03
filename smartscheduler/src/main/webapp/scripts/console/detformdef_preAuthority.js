function read_authority_datagrid() {
	$('#read_authority_datagrid').datagrid({
		fit : true,
		url :$("#basePath").val() + '/console/detFormRightSet/shows.action',
		method : 'POST',
		striped : true,
		nowrap : true,
		pageSize : 17,
		queryParams : {
			"name" : "read",
		},
		pageNumber : 1,
		pageList : [ 17 ],
		showFooter : true,
		loadMsg : '数据加载中请稍候……',
		pagination : true,
		toolbar : "#read_authority_datagrid_toolbar",
		singleSelect : false,
		checkOnSelect : false,
		selectOnCheck : false,
		columns : [ [ {
			field : 'ck',
			checkbox : true
		}, {
			field : 'rightSetId',
			title : 'id',
			width : 100,
			align : 'center',
			hidden : true
		}, {
			field : 'rightObjectType',
			title : '类型',
			width : 170,
			align : 'center',
			formatter : function(value, rec, index) {
				if (value == "employee") {
					return "人员";
				} else if (value == "department") {
					return "部门";
				} else if (value == "role") {
				  return "角色";
				}else{
					return value;
				}
			}
		}, {
			field : 'rightObjectIdName',
			title : '名称',
			width : 220,
			align : 'center'
		} ] ],
		onSelect : function(rowData) {
			$('#read_authority_datagrid').datagrid("unselectAll");
		}
	});
}

function write_authority_datagrid() {
	$('#write_authority_datagrid').datagrid({
		fit : true,
		url : $("#basePath").val() +'/console/detFormRightSet/shows.action',
		method : 'POST',
		striped : true,
		nowrap : true,
		pageSize : 17,
		pageNumber : 1,
		pageList : [ 17 ],
		queryParams : {
			"name" : "write",
		},
		showFooter : true,
		loadMsg : '数据加载中请稍候……',
		pagination : true,
		toolbar : "#write_authority_datagrid_toolbar",
		singleSelect : false,
		checkOnSelect : false,
		selectOnCheck : false,
		columns : [ [ {
			field : 'ck',
			checkbox : true
		}, {
			field : 'rightSetId',
			title : 'id',
			width : 100,
			align : 'center',
			hidden : true
		}, {
			field : 'rightObjectType',
			title : '类型',
			width : 170,
			align : 'center',
			formatter : function(value, rec, index) {
				if (value == "employee") {
					return "人员";
				} else if (value == "department") {
					return "部门";
				} else if (value == "role") {
				  return "角色";
				}else{
					return value;
				}
			}
		}, {
			field : 'rightObjectIdName',
			title : '名称',
			width : 220,
			align : 'center'
		} ] ],
		onSelect : function(rowData) {
			$('#write_authority_datagrid').datagrid("unselectAll");
		}
	});
}

function openPreAuthorityDlg(type) {
	$('#add_preAuthority_dlg').dialog('open').dialog('setTitle', ' ');
	$("#preAuthority_dept").hide();
	$("#preAuthority_role").hide();
	$("#preAuthority_user").show();
	$("#owners_people_value").val("");
	$("#owners_people_value_num").val("");
	$("#preAuthority_wr").val(type);
}

function _openUserDlg() {
	var id = "owners_people_value";
	var url = $("#basePath").val() + "/console/tag/user/index.xsp?id=" + id
			+ "&documentId=&singleSelect=false";
	window.open(encodeURI(url));
}

function _openDeptDlg() {
	var id = "owners_dept_value";
	var url = $("#basePath").val()
			+ "/console/department/tag/dept/index.xsp?id=" + id
			+ "&documentId=&singleSelect=false";
	window.open(encodeURI(url));
}

function _openRoleDlg() {
	var id = "owners_role_value";
	var url = $("#basePath").val() + "/console/role/tag/index.xsp?id=" + id
			+ "&documentId=&singleSelect=false";
	window.open(encodeURI(url));
}
function preAuthority_type() {
	var preAuthority_type = $("#preAuthority_type").val();
	$("#preAuthority_user").hide();
	$("#preAuthority_dept").hide();
	$("#preAuthority_role").hide();
	if (preAuthority_type == "employee") {
		$("#preAuthority_user").show();
	} else if (preAuthority_type == "department") {
		$("#preAuthority_dept").show();
	} else if (preAuthority_type == "role") {
		$("#preAuthority_role").show();
	}

}

function save_preAuthority() {
	var preAuthority_wr = $("#preAuthority_wr").val();
	var preAuthority_type = $("#preAuthority_type").val();
	var preAuthority_values = "";
	var rightPassOn="";
	if (preAuthority_type == "employee") {
		preAuthority_values = $("#owners_people_value_num").val();
	} else if (preAuthority_type == "department") {
		preAuthority_values = $("#owners_dept_value_num").val();
	} else if (preAuthority_type == "role") {
		preAuthority_values = $("#owners_role_value_num").val();
	}
	
	if(preAuthority_wr=="read"){
		 $("input[name=rRightPassOn]:checked").each(function(){ 
		      	rightPassOn= $(this).val(); //获取被选中的值
		 });
  	}else if(preAuthority_wr=="write"){
			 $("input[name=wRightPassOn]:checked").each(function(){ 
			      	rightPassOn= $(this).val(); //获取被选中的值
			 });
  	}
	
	$.ajax({
		url:$("#basePath").val()+"/console/detFormRightSet/save.action",
		type:"post",
		data:{
		type:preAuthority_wr,
		values:preAuthority_values,
		formId:$("#formId").val(),
		rightPassOn:rightPassOn,
		rightObjectType:preAuthority_type
		},
		dataType:"json",
		success:function(result){
			   if (!result.state){
	                $.messager.show({
	                    title: '温馨提示',
	                    msg: result.msg
	                });
	            } else {
	                  $('#add_preAuthority_dlg').dialog('close');        // close the dialog
	                  if(preAuthority_wr=="read"){
	                	  $('#read_authority_datagrid').datagrid('reload'); 
	               	}else if(preAuthority_wr=="write"){
	               	   $('#write_authority_datagrid').datagrid('reload');    // reload the user data
	               	}
	               
	                
	            }
		   }
	        });
}


function destroy(id) {
	var rows = $('#'+id).datagrid('getChecked');
	if (rows.length > 0) {
		$.messager
				.confirm(
						'温馨提示',
						'你真的要删除么?',
						function(r) {
							if (r) {
								var jsonData = {
										rightSetIds : []
								};
								for (var i = 0; i < rows.length; i++) {
									jsonData.rightSetIds
											.push(rows[i].rightSetId);
								}
								$
										.ajax({
											url : $("#basePath").val()
													+ '/console/detFormRightSet/delete.action',
											type : "post",
											data : jsonData,
											dataType : "json",
											success : function(result) {
												if (result.state == true) {
													$('#'+id).datagrid(
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
$(function() {
	read_authority_datagrid();
	write_authority_datagrid();
});