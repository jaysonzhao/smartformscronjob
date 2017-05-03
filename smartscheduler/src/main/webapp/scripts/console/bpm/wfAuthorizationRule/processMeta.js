/** ztree的参数配置，setting主要是设置一些tree的属性，是本地数据源，还是远程，动画效果，是否含有复选框等等* */
var _deptSetting = {
	check : {
		enable : true,
		chkStyle : "radio",
		radioType : "all"
	},
	view : {
		// dblClickExpand: false,
		expandSpeed : 300
	// 设置树展开的动画速度，IE6下面没效果，
	},
	data : {
		simpleData : { // 简单的数据源，一般开发中都是从数据库里读取，API有介绍，这里只是本地的
			enable : true,
			idKey : "id", // id和pid，这里不用多说了吧，树的目录级别
			pIdKey : "pid",
			rootPId : 0
		// 根节点
		}
	},
	callback : {
		onRightClick : function(){},
		/** 回调函数的设置，随便写了两个* */
		beforeClick : function() {
		},
		onClick : function(event, treeId, treeNode) {
			var contents = $("#iframe_config").contents();
			var treeObj = $.fn.zTree.getZTreeObj("processCata");
			treeObj.checkNode(treeNode, true, true);
			treeNode.checked = true;
			treeObj.updateNode(treeNode);
			var id = treeNode.id;
			if (id == "0") {
				document.getElementById("iframe_processmeta").src =  $("#basePath").val()+"/workflow/bpm/AuthorizationRule/process.xsp?authorizationRuleId="+contents.find("#authorizationRuleId").val();
			} else {
				document.getElementById("iframe_processmeta").src =  $("#basePath").val()+"/workflow/bpm/AuthorizationRule/process.xsp?authorizationRuleId="+contents.find("#authorizationRuleId").val()+"&workflowCataId="
						+ id;
			}
		}
	}
};

/*
 * 获取所有的流程分类数据 以树状图的形式展示
 */
function getProcessCata() {
	$.ajax({
		type : 'post',
		url : $("#basePath").val()
				+ '/console/bpm/processcata/getBpmProcessCatas.action',
		dataType : 'json',
		success : function(result) {
			if (result.state == true) {
				var ztrees = $.fn.zTree.init($("#processCata"), _deptSetting,
						result.data);
				ztrees.expandAll(true);// 全部展开
			}
		}
	});
	
	

}

$(function() {
	getProcessCata();
});