/**
 * 打开创建流程界面
 */
function createProcessCata() {
	var treeObj = $.fn.zTree.getZTreeObj("processCata");
	var nodes = treeObj.getCheckedNodes(true);
	if (nodes.length == 1) {
		$('#process_cata_dlg').dialog('open').dialog('setTitle', ' ');
		$('#fm').form('clear');
		var pid = nodes[0].id;
		var pname = nodes[0].name;
		$("#parentCataId").val(pid);
		$("#parentCataName").val(pname);
	} else {
		$.messager.show({
			title : '温馨提示',
			msg : '请选择一个分类目录'
		});
	}
}

/**
 * 打开更新流程分类界面
 */
function updateProcessCata() {
	var treeObj = $.fn.zTree.getZTreeObj("processCata");
	var nodes = treeObj.getCheckedNodes(true);
	if (nodes.length == 1) {
		$('#process_cata_dlg').dialog('open').dialog('setTitle', ' ');
		$('#fm').form('clear');
		var pid = nodes[0].id;
		var pname = nodes[0].name;
		$("#cataId").val(pid);
		 $("#cataName").textbox('setValue',pname);
	} else {
		$.messager.show({
			title : '温馨提示',
			msg : '请选择一个流程分类目录'
		});
	}
}

/**
 * 创建或更新流程分类
 */
function createOrUpdateProcessCata() {
	var values = $("#fm").serialize();
	if ($("#fm").form('validate')) {
		$("#process_cata_dlg_btnsave").linkbutton("disable");
		$.ajax({
			url : $("#basePath").val()
					+ "/console/bpm/processcata/createOrUpdate.action",
			type : "post",
			data : values,
			dataType : "json",
			success : function(result) {
				$("#process_cata_dlg_btnsave").linkbutton("enable");
				if (result.state == false) {
					$.messager.show({
						title : '温馨提示',
						msg : result.msg
					});
				} else {
					getProcessCata();
					$('#process_cata_dlg').dialog('close');
				}
			},
			error : function() {
				$("#process_cata_dlg_btnsave").linkbutton("enable");
			}
		});
	}
}

/**
 * 删除流程分类
 */
function deletBpmProcessCata() {
	var treeObj = $.fn.zTree.getZTreeObj("processCata");
	var nodes = treeObj.getCheckedNodes(true);
	if (nodes.length == 1) {
		$.ajax({
			url : $("#basePath").val()
					+ "/console/bpm/processcata/deletBpmProcessCata.action",
			type : "post",
			data : {
				"cataId" : nodes[0].id
			},
			dataType : "json",
			success : function(result) {
				if (result.state == false) {
					$.messager.show({
						title : '温馨提示',
						msg : result.msg
					});
				} else {
					getProcessCata();
				}
			}
		});
	} else {
		$.messager.show({
			title : '温馨提示',
			msg : "请选择一个要删除的流程分类"
		});
	}
}

// 绑定鼠标右键事件
function OnRightClick(e, treeId, treeNode) {
	e.preventDefault();
	$("#tabsMenu").menu('show', {
		left : e.pageX,
		top : e.pageY
	});
}

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
		onRightClick : OnRightClick,
		/** 回调函数的设置，随便写了两个* */
		beforeClick : function() {
		},
		onClick : function(event, treeId, treeNode) {
			var treeObj = $.fn.zTree.getZTreeObj("processCata");
			treeObj.checkNode(treeNode, true, true);
			treeNode.checked = true;
			treeObj.updateNode(treeNode);
			var id = treeNode.id;
			if (id == "0") {
				document.getElementById("iframe_processmeta").src =  $("#basePath").val()+"/console/bpm/processmeta/show.xsp";
			} else {
				document.getElementById("iframe_processmeta").src =  $("#basePath").val()+"/console/bpm/processmeta/show.xsp?workflowCataId="
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
