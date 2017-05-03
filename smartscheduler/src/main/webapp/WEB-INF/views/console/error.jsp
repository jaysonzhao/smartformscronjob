<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/console/error500.css" />
<title>应用程序错误</title>
</head>
<body>
	<div id="baseContainer">
		<table class="errtable">
			<tr>
				<td class="errimg"><img src="${pageContext.request.contextPath}/images/error/tusiji.png" /></td>
				<td class="errmsg"><h2>${error}</h2></td>
			</tr>
		</table>
	</div>
</body>
</html>