<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript">
	function datagrid(name, deparmentId) {
		$('#datagrid')
				.datagrid(
						{
							height : '100%',
							fit : true,
							url : '${pageContext.request.contextPath}/console/user/shows.action',
							method : 'POST',
							queryParams : {
								"name" : name,
								"deparmentId" : deparmentId
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
							columns : [ [
							 {
								field : 'ck',
								checkbox : true
							},
									{
										field : 'empNum',
										title : '员工id',
										width : 100,
										align : 'center',
										hidden : true
									},
									{
										field : 'empName',
										title : '登录账号',
										width : 200,
										align : 'center'
									},
										{
										field : 'jobNumber',
										title : '工号',
										width : 200,
										align : 'center'
									},
									{
										field : 'gender',
										title : '性别',
										width : 50,
										align : 'center'
									},
									
									{
										field : 'ldapUid',
										title : 'LDAP UID',
										width : 200,
										align : 'center'
									},
									{
										field : 'nickName',
										title : '姓名',
										width : 200,
										align : 'center'
									},
										{
										field : 'namePinyin',
										title : '姓名拼音',
										width : 200,
										align : 'center'
									},
									{
										field : 'formerName',
										title : '曾用姓名',
										width : 200,
										align : 'center'
									},
									{
										field : 'empEnName',
										title : '英文名',
										width : 200,
										align : 'center'
									},
									{
										field : 'empPosition',
										title : '职位ID',
										width : 100,
										align : 'center'
									},
									{
										field : 'email',
										title : '邮件',
										width : 250,
										align : 'center'
									},
									{
										field : 'mobile',
										title : '手机号码',
										width : 100,
										align : 'center'
									},
									 {
										field : 'nation',
										title : '民族',
										width : 100,
										align : 'center'
									}, 
									 {
										field : 'state',
										title : '状态',
										width : 100,
										align : 'center'
									}, 
									 {
										field : 'identityCard',
										title : '身份证',
										width : 200,
										align : 'center'
									}, 
									
									 {
										field : 'companyNum',
										title : '公司编码',
										width : 100,
										align : 'center'
									}, 
									{
										field : 'datesEmployed',
										title : '入职时间',
										width : 100,
										align : 'center'
									}, 
									{
										field : 'telephone',
										title : '固话',
										width : 100,
										align : 'center'
									}, 
									{
										field : 'departureTime',
										title : '离职时间',
										width : 100,
										align : 'center'
									}, 
									{
										field : 'ldapName',
										title : 'LDAP DN NAME',
										width : 300,
										align : 'center'
									},
										{
										field : 'address',
										title : '地址',
										width : 300,
										align : 'center'
									},
								/* 	{
										field : 'creator',
										title : '创建者',
										width : 100,
										align : 'center'
									}, */
									{
										field : 'createTime',
										title : '创建时间',
										width : 130,
										align : 'center',
										formatter : function(value, rec, index) {
											var s = new Date(value)
													.pattern("yyyy-MM-dd hh:mm:ss");
											return s;
										}
									},
									/* {
										field : 'updateBy',
										title : '更新者',
										width : 100,
										align : 'center'
									}, */
									{
										field : 'updateTime',
										title : '更新时间',
										width : 130,
										align : 'center',
										formatter : function(value, rec, index) {
											var s = new Date(value)
													.pattern("yyyy-MM-dd hh:mm:ss");
											return s;
										}
									} ] ],
							onSelect : function(rowData) {
								$('#datagrid').datagrid("unselectAll");
							}
						});
	}

	function searchByName(departmentId) {
		var name = $('#name').val();
			datagrid(name, departmentId);
		
	}

	function sync() {
		$
				.ajax({
					type : 'POST',
					url : "${pageContext.request.contextPath}/console/user/sync.action",
					success : function() {
					}
				});
		$.messager.show({ // show error message
			title : '温馨提示',
			msg : '后台程序已经开始同步,请稍等几分钟。。。'
		});
	}
</script>
<div id="datagrid" style="width: 100%; height: 100%"></div>
