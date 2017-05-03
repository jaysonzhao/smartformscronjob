
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="easyui-accordion" data-options="fit:true,border:false">
	<div id="organizeTree"></div>
	<script type="text/javascript">
		$('#organizeTree')
				.tree(
						{
							url : '${pageContext.request.contextPath}/console/department/parentOrgDepartmentTree.action?id=0',
							onBeforeExpand : function(node, param) { //${menu.menuId}
								$('#organizeTree').tree('options').url = "${pageContext.request.contextPath}/console/department/parentOrgDepartmentTree.action";
							},
							onClick : function(node) {
								var id = node.id;
								if (id == "0") {
									document.getElementById("userIframeId").src = "${pageContext.request.contextPath}/console/user/show.xsp?deparmentId="
											+ id;
								} else {
									document.getElementById("userIframeId").src = "${pageContext.request.contextPath}/console/user/organizeUser.xsp?deparmentId="
											+ id;
								}

							}
						});
	</script>
</div>