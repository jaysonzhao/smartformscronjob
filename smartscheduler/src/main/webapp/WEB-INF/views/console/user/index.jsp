<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="<%=basePath%>images/LOGO.ico" rel="shortcut icon"
	type="image/x-icon" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<jsp:include page="../resources.jsp"></jsp:include>
</head>
<body class="easyui-layout">
	<%-- <div data-options="region:'west',split:true,title:'    部门导航'  "  class="west">
		<jsp:include page="./organization.jsp" />
	</div> --%>
	<div data-options="region:'center'" class="center">
		<div id="tabs" class="easyui-tabs" fit="true" border="false">
			<iframe id="userIframeId" src="${pageContext.request.contextPath}/console/user/show.xsp?deparmentId=0" 
		   allowTransparency="true" scrolling="auto" width="100%" height="98%"
				frameBorder="0" name=""></iframe>
		</div>
	</div>


</body>

</html>