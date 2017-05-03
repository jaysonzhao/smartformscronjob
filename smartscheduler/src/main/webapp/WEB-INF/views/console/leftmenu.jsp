
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="easyui-accordion" data-options="fit:true,border:false">
	<div id="leftmenuTree"></div>
	<script type="text/javascript">
		   $('#leftmenuTree').tree({   //${menu.menuId}
		   data: [{
		       text: '任务调度',
		       state: 'open',
		       children: [{
			         text: '任务列表'
		            }]
		       }],
		    	onClick : function(node) {
					addTab("任务列表", "${pageContext.request.contextPath}/console/quartz/index.xsp");
				 }
				});
			</script>
</div>