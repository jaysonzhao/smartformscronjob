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
<title>选择人员</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/styles/console/reset.css" />
<link
	href="${pageContext.request.contextPath}/styles/bootstrap/2.2.2/css/bootstrap.min.css"
	rel="stylesheet">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/styles/console/bpm/activitymeta/node.css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/scripts/ztree/zTreeStyle/zTreeStyle.css"
	type="text/css">

<script type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/jquery/jquery-1.11.1.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/json2.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/layer/layer.js"></script>


<script type="text/javascript"
	src="${pageContext.request.contextPath}/styles/bootstrap/2.2.2/js/bootstrap.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/ztree/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/ztree/js/jquery.ztree.excheck-3.5.js"></script>

<style type="text/css">
.persoNLf {
	width: 800px;
	float: left;
	border: 1px solid #ccc;
}

.persoN {
	clear: both;
	height: 400px;
	margin: 2%;
	min-width: 1200px;
}
</style>

<SCRIPT type="text/javascript">
	var setting = {
		async : {
			enable : true,
			url : getUrl
		},
		data : {
			simpleData : { // 简单的数据源，一般开发中都是从数据库里读取，API有介绍，这里只是本地的
				enable : true,
				idKey : "id", // id和pid，这里不用多说了吧，树的目录级别
				pIdKey : "pid",
				rootPId : "0"
			}
		},
		callback : {
			beforeExpand : beforeExpand,
			onAsyncSuccess : onAsyncSuccess,
			onAsyncError : onAsyncError,
			onClick : function(event, treeId, treeNode) {
				var treeObj = $.fn.zTree.getZTreeObj("processCata");
				treeObj.checkNode(treeNode, true, true);
				treeNode.checked = true;
				treeObj.updateNode(treeNode);
				var id = treeNode.id;
				if (id == "0") {
					document.getElementById("userIframeId").src = $("#basePath")
							.val()
							+ "/console/tag/user/show.xsp?deparmentId="
							+ id
							+ "&singleSelect=" + $("#singleSelect").val();
				} else {
					document.getElementById("userIframeId").src = $("#basePath")
							.val()
							+ "/console/tag/user/organizeUser.xsp?deparmentId="
							+ id + "&singleSelect=" + $("#singleSelect").val();
				}

			}
		}
	};

	var log, className = "dark", startTime = 0, endTime = 0, perCount = 100, perTime = 100;
	function getUrl(treeId, treeNode) {
		return "${pageContext.request.contextPath}/console/department/getOrgDepartmentTreeById.action?id="
				+ treeNode.id;
	}
	function beforeExpand(treeId, treeNode) {
		if (!treeNode.isAjaxing) {
			startTime = new Date();
			treeNode.times = 1;
			ajaxGetNodes(treeNode, "refresh");
			return true;
		} else {
			alert("zTree 正在下载数据中，请稍后展开节点。。。");
			return false;
		}
	}
	function onAsyncSuccess(event, treeId, treeNode, msg) {
		if (!msg || msg.length == 0) {
			return;
		}
		var zTree = $.fn.zTree.getZTreeObj("treeDemo"), totalCount = treeNode.count;
		if (treeNode.children.length < totalCount) {
			setTimeout(function() {
				ajaxGetNodes(treeNode);
			}, perTime);
		} else {
			treeNode.icon = "";
			zTree.updateNode(treeNode);
			zTree.selectNode(treeNode.children[0]);
		}
	}
	function onAsyncError(event, treeId, treeNode, XMLHttpRequest, textStatus,
			errorThrown) {
		var zTree = $.fn.zTree.getZTreeObj("treeDemo");
		alert("异步获取数据出现异常。");
		//	treeNode.icon = "";
		zTree.updateNode(treeNode);
	}
	function ajaxGetNodes(treeNode, reloadType) {
		var zTree = $.fn.zTree.getZTreeObj("treeDemo");
		if (reloadType == "refresh") {
			//treeNode.icon = "../../../css/zTreeStyle/img/loading.gif";
			zTree.updateNode(treeNode);
		}
		zTree.reAsyncChildNodes(treeNode, reloadType, true);
	}

	$(document)
			.ready(
					function() {
						$.ajax({
									type : 'post',
									url : '${pageContext.request.contextPath}/console/department/getOrgDepartmentTreeById.action?id=-1',
									dataType : 'json',
									success : function(result) {
										var ztrees = $.fn.zTree
												.init($("#treeDemo"), setting,
														result);
										ztrees.expandAll(true);// 全部展开
										var nodes = ztrees.getNodes();
										for (var i = 0; i < nodes.length; i++) { //设置节点展开
											ztrees.expandNode(nodes[i], true,
													true, true);
										}
									}
								});
					});
</SCRIPT>



</head>
<body>
	zxcz
	<div style="float: left; width: 300px; height: 500px; overflow: auto;">
		<ul id="treeDemo" class="ztree"></ul>
	</div>

</body>

</html>