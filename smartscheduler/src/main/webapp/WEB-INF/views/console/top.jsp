<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="now" class="java.util.Date" />  

<link href="${pageContext.request.contextPath}/images/login/logo.ico" rel="shortcut icon" type="image/x-icon" />

<link rel="icon" href="${pageContext.request.contextPath}/images/login/logo.ico" type="image/x-icon" />
<link rel="shortcut icon" href="${pageContext.request.contextPath}/images/login/logo.ico" type="image/x-icon" />  
<title>智能表单</title>
    
    <style>

 .logos{
    width:700px;
    height: 53px;
    overflow: hidden;
    float: left;
    line-height: 63px;
    margin-top: 1%;
    margin-left: 4px;
} 
.logos h1{color: #333;font-size: 24px;font-weight: 400;}
 .head_right{float: right;width: 270px;height: 50px;overflow: hidden;}
.head_right dt{width: 38px;height:inherit;float: left;}
.head_right dd{float: left;width: 230px;padding: 0;margin: 0;line-height: 20px;height: 20px;text-indent: 10px;}
.head_right dd a{margin-left:10px;color: #c70900;}
.tree-node{padding-left: 10px;height: 23px;} 



</style>


<div data-options="region:'north',border:false" style="height:83px;background:#eb5606;">
<div class="logos">
	<img src="${pageContext.request.contextPath}/images/logo2.png">  
</div>
<div class="head_right" style="margin-top:18px">
    <dl style="color:#FAF9F8;">
        <dt><img src="${pageContext.request.contextPath}/images/head_03.png"></dt>
        <dd>${_currUserName}&nbsp;&nbsp;<a href="${pageContext.request.contextPath}/console/user/logout.action">退出登录</a></dd>
        <dd>登录时间：<fmt:formatDate value="${now}" type="date" dateStyle="full"/></dd>
    </dl>
</div></div>
