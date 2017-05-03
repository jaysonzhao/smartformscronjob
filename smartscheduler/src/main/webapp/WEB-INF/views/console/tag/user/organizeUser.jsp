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
<jsp:include page="../../resources.jsp"></jsp:include>
<title>Insert title here</title>
</head>
<body>
	<jsp:include page="./dataGrid.jsp"></jsp:include>
	<div id="toolbar">
		<div>
			
				&nbsp;&nbsp;姓名/别名:<input type="text" id="name" style="width: 150px;">
			<a href="javascript:void(0)" class="easyui-linkbutton " plain="true"
				onclick="searchByName('${deparmentId}')"> 查询 </a> 
		</div>
	</div>
	
	<script type="text/javascript">
		

		//TODO   初始化加载开始
		$(function() {
			datagrid("", "${deparmentId}");
		});
		//TODO 初始化加载结束
	</script>

</body>
</html>