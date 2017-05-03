<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<jsp:include page="../resources.jsp"></jsp:include>
<title>Insert title here</title>
</head>
<body>
	<jsp:include page="./dataGrid.jsp"></jsp:include>
	<div id="toolbar">
		<div>
			<a href="javascript:void(0)" class="easyui-linkbutton " plain="true"
				onclick="openDepartmentUserDlg()"> 添加用户 </a> <a
				href="javascript:void(0)" class="easyui-linkbutton " plain="true"
				onclick="removeDepartmentUser()"> 移除用户 </a> <a
				href="javascript:void(0)" class="easyui-linkbutton " plain="true">
				姓名/账号:</a><input type="text" id="name" style="width: 90px;">
			 <a href="javascript:void(0)" class="easyui-linkbutton " plain="true"
				onclick="searchByName('${deparmentId}')"> 查询 </a> <!-- a
				href="javascript:void(0)" class="easyui-linkbutton " plain="true"
				onclick="sync()"> 手动同步ldap用户 </a> -->
				<a href="javascript:void(0)" class="easyui-linkbutton " plain="true"
				onclick="open_permission_dlg()"> 权限 </a>
		</div>
	</div>
	<jsp:include page="./addOrganizeUser.jsp"></jsp:include>
	<jsp:include page="./permission.jsp"></jsp:include>
	<script type="text/javascript">
		function removeDepartmentUser() {
			var arr = $('#datagrid').datagrid('getChecked');
			if (arr.length > 0) {
				var ids = [];
				for (var i = 0; i < arr.length; i++) {
					ids.push(arr[i].empNum);
				}
				$.ajax({
							url : "${pageContext.request.contextPath}/console/user/removeDepartmentUser.action",
							type : "post",
							data : {
								ids : ids
							},
							dataType : "json",
							success : function(result) {
								if (!result.state) {
									$.messager.show({
										title : '温馨提示',
										msg : result.msg
									});
								} else {
									$('#datagrid').datagrid('reload');
								}
							}
						});
			}
		}

		//TODO   初始化加载开始
		$(function() {
			datagrid("", "${deparmentId}");
		});
		//TODO 初始化加载结束
	</script>

</body>
</html>