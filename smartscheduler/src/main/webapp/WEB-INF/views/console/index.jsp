<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
 <link href="<%=basePath %>images/LOGO.ico" rel="shortcut icon" type="image/x-icon" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<jsp:include page="./resources.jsp"></jsp:include>
</head>
<body class="easyui-layout">
	<div data-options="region:'north',border:false" class="north" style="height:85px">
		<jsp:include page="./top.jsp" />
	</div>
	<div data-options="region:'west',split:true,title:'  '  " class="west">
		<jsp:include page="./leftmenu.jsp" />
	</div>
	<div data-options="region:'center'" class="center">
		<div id="tabs" class="easyui-tabs" fit="true" border="false">
			<div title="Home">
				<div class="cs-home-remark">
					
					<br>
				</div>
			</div>
		</div>
	</div>
	<div data-options="region:'south'">
		<p>版本：v1.0 beta  &nbsp; 更新日期：2016-8-18</p>
	</div>

	<div id="mm" class="easyui-menu cs-tab-menu">
		<div id="mm-tabupdate">刷新</div>
		<div class="menu-sep"></div>
		<div id="mm-tabclose">关闭</div>
		<div id="mm-tabcloseother">关闭其他</div>
		<div id="mm-tabcloseall">关闭全部</div>
	</div>
</body>

</html>