<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="decorator" content="none">
<title>403-无权限访问</title>

<style>
body, div {
    margin: 0;
    padding: 0;
}
body {
    background: url("../../images/error/error_bg.jpg") repeat-x scroll 0 0 #67ACE4;
}
#container {
    margin: 0 auto;
    padding-top: 50px;
    text-align: center;
    width: 560px;
}
#container img {
    border: medium none;
    margin-bottom: 50px;
}
#container .error {
    height: 200px;
    position: relative;
}
#container .error img {
    bottom: -50px;
    position: absolute;
    right: -50px;
}
#container .msg {
    margin-bottom: 65px;
}
#cloud {
    background: url("../../images/error/error_cloud.png") repeat-x scroll 0 0 transparent;
    bottom: 0;
    height: 170px;
    position: absolute;
    width: 100%;
}


</style>
</head>
<body>
<!-- 代码 开始 -->
<div id="container">
<img class="png" src="../../images/error/403.png"> 
<img class="png msg" src="../../images/error/403_msg.png">
  <p>
  <a href="${pageContext.request.contextPath}/console/index.action.action" target="_blank">
  <img class="png" src="../../images/error/403_to_index.png"></a> 
  </p>
</div>
<div id="cloud" class="png"></div>
<!-- 代码 结束 -->

</body></html>