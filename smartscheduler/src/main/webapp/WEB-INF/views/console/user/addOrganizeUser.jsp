<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<script>
	function departmentUser(name) {
      $('#departmentUser').datagrid(
                        {
							height : '100%',
							fit : true,
							url : '${pageContext.request.contextPath}/console/user/unDepartment.action',
							method : 'POST',
							queryParams : {
								"name" : name,
								"deparmentId" : "${deparmentId}"
							},
							striped : true,
							nowrap : true,
							pageSize : 10,
							pageNumber : 1,
							pageList : [ 10 ],
							showFooter : true,
							loadMsg : '数据加载中请稍候……',
							pagination : true,
							toolbar : "#departmentUserToolbar",
							singleSelect : false,
							checkOnSelect : false,
							selectOnCheck : false,
							columns : [ [ {
								field : 'ck',
								checkbox : true
							}, {
								field : 'empNum',
								title : '员工id',
								width : 100,
								align : 'center',
								hidden : true
							}, {
								field : 'empName',
								title : '登录账号',
								width : 200,
								align : 'center'
							}, {
								field : 'ldapUid',
								title : 'LDAP UID',
								width : 200,
								align : 'center'
							}, {
								field : 'nickName',
								title : '姓名',
								width : 200,
								align : 'center'
							}/* , {
								field : 'empPosition',
								title : '职位ID',
								width : 100,
								align : 'center'
							} */, {
								field : 'email',
								title : '邮件',
								width : 300,
								align : 'center'
							}/* , {
								field : 'mobile',
								title : '手机号码',
								width : 100,
								align : 'center'

							}  */] ],
							onSelect : function(rowData) {
								$('#datagrid').datagrid("unselectAll");
							}
						});
	}

	function openDepartmentUserDlg() {
		$('#addDepartmentUser_dlg').dialog('open').dialog('setTitle', ' ');
		departmentUser("");
	}

	function departmentUserSearchByName() {
		var name = $("#departmentUserName").val();
			departmentUser(name);
		
	}
	


	function addDepartmentUser(){
	 var arr = $('#departmentUser').datagrid('getChecked');
		if(arr.length>0){
		 var ids =[];
		for(var i =0 ;i<arr.length;i++){
		ids.push(arr[i].empNum);
		}
		
		$.ajax({
		url:"${pageContext.request.contextPath}/console/user/employeeBelongDep.action",
		type:"post",
		data:{
		departmentId:"${deparmentId}",
		ids:ids
		},
		dataType:"json",
		success:function(result){
			   if (!result.state){
	                $.messager.show({
	                    title: '温馨提示',
	                    msg: result.msg
	                });
	            } else {
	                 // $('#dlg').dialog('close');        // close the dialog
	                $('#departmentUser').datagrid('reload');    // reload the user data
	            }
		   }
	        });
	}			
	}				
	
	function closeAddOrganizeDlg(){
	   $('#addDepartmentUser_dlg').dialog('close');   
	   $('#datagrid').datagrid('load');
	}
</script>
<div id="addDepartmentUser_dlg" class="easyui-dialog"
	style="width: 750px; height: 400px;" closed="true"
	buttons="#addDepartmentUser_dlg_btn">
	<div id="departmentUser"></div>
</div>

<div id="departmentUserToolbar">
	<div>
		<a href="javascript:void(0)" class="easyui-linkbutton " plain="true"
			onclick="addDepartmentUser()"> 添加 </a> <!-- <a
			href="javascript:void(0)" class="easyui-linkbutton " plain="true"
			onclick="openDepartmentUserDlg()"> 移除 </a> --> <a
			href="javascript:void(0)" class="easyui-linkbutton " plain="true">
			姓名/账号:</a> <input type="text" id="departmentUserName" style="width: 90px;">
		<a href="javascript:void(0)" class="easyui-linkbutton " plain="true"
			onclick="departmentUserSearchByName()"> 查询 </a>
	</div>
</div>


<div id="addDepartmentUser_dlg_btn">
	<a href="javascript:void(0)" class="easyui-linkbutton c6"
		iconCls="icon-ok" onclick="closeAddOrganizeDlg()" style="width: 90px">关闭</a>
</div>

