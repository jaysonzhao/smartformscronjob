<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="decorator" content="none" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>登录</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/console/login.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/console/login.js"></script>
</head>
<body>
	<input type="hidden" id="ctxpath" value="${pageContext.request.contextPath}" />
	<input  type="hidden" value="_#####_loging_####_"/>
	<table id="baseContainer">
		<tr>
			<td>
				<form id="form1" action="${pageContext.request.contextPath}/console/user/login.action" method="post">
					<div><img src="${pageContext.request.contextPath}/images/login/login_top.png" /></div>
					<div id="login_body">
						<div id="login_logo"><img src="${pageContext.request.contextPath}/images/login/login_logo.png" /></div>
						<div id="login_wnd">
							<div class="wndrow">
								<div class="r1"></div>
								<div class="r2"><input class="textfield" type="text" id="username" name="username"/></div>
								<div class="cls"></div>
							</div>
							<div class="wndrow">
								<div class="r3"></div>
								<div class="r2"><input class="textfield" type="password" id="password" name="password"/></div>
								<div class="cls"></div>
							</div>
							<div class="wndrow"><input type="submit" id="submitbtn" value="登 录"/></div>
							<div id="msg" class="wndrow">${msg}</div>
						</div>
						<div class="cls"></div>
					</div>
					<div class="cls"></div>
				</form>
				<div id="bottom_info">
					<p>© 2016 广州续日计算机科技有限公司 &nbsp;&nbsp;&nbsp;&nbsp; 版本：v1.0</p>
				</div>
			</td>
		</tr>
	</table>
</body>

</html>