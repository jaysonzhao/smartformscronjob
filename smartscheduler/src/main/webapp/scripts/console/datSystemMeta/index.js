/**
 * 授权
 * @returns
 */
function bindOwners(){
	var treeObj = $.fn.zTree.getZTreeObj("systemMetaCata");
	var nodes = treeObj.getCheckedNodes(true);
	if(nodes.length==1){
		$("#iframe_systemMeta")[0].contentWindow.openOwnerDlg();
	}else{
		alert("请选择一个分类");
	}
}
/**
 * 创建系统元数据分类页面
 */
function createSystemMetaCata() {
	var treeObj = $.fn.zTree.getZTreeObj("systemMetaCata");
	var nodes = treeObj.getCheckedNodes(true);
	if (nodes.length == 1) {
		$('#system_meta_cata_dlg').dialog('open').dialog('setTitle', '创建分类');
		$('#fm').form('clear');
		var pid = nodes[0].id;
		var pname = nodes[0].name;
		$("#parentCataId").val(pid);
	} else {
		$.messager.show({
			title : '温馨提示',
			msg : '请选择一个分类目录'
		});
	}
}
/**
 * 更新系统元数据分类页面
 */
function updateSystemMetaCata() {
	var treeObj = $.fn.zTree.getZTreeObj("systemMetaCata");
	var nodes = treeObj.getCheckedNodes(true);
	if (nodes.length == 1) {
		$('#system_meta_cata_dlg').dialog('open').dialog('setTitle', '更新分类');
		$('#fm').form('clear');
		var pid = nodes[0].id;
		var pname = nodes[0].name;
		var description = nodes[0].description;
		$("#cataId").val(pid);
		$("#cataName").textbox('setValue',pname);
		$("#description").textbox('setValue',description);
	} else {
		$.messager.show({
			title : '温馨提示',
			msg : '请选择一个分类目录'
		});
	}
}
/**
 * 创建或更新系统元数据分类
 */
function createOrUpdate() {
	var values = $("#fm").serialize();
	if ($("#fm").form('validate')) {
		$("#system_meta_cata_dlg_btnsave").linkbutton("disable");
		$.ajax({
			url : $("#basePath").val()
			+ "/console/systemMetaCata/createOrUpdate.action",
			type : "post",
			data : values,
			dataType : "json",
			success : function(result) {
				$("#system_meta_cata_dlg_btnsave").linkbutton("enable");
				if (result.state == false) {
					$.messager.show({
						title : '温馨提示',
						msg : result.msg
					});
				} else {
					getSystemMetaCatas();
					$('#system_meta_cata_dlg').dialog('close');
				}
			},
			error : function() {
				$("#system_meta_cata_dlg_btnsave").linkbutton("enable");
			}
		});
	}
}
/**
 * 删除系统元数据分类
 */
function deleteSystemMetaCata() {
	var treeObj = $.fn.zTree.getZTreeObj("systemMetaCata");
	var nodes = treeObj.getCheckedNodes(true);
	if (nodes.length == 1) {
		$.ajax({
			url : $("#basePath").val()
			+ "/console/systemMetaCata/delete.action",
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
					getSystemMetaCatas();
				}
			}
		});
	} else {
		$.messager.show({
			title : '温馨提示',
			msg : "系统元数据不支持批量删除"
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
/**
 * 类目树
 */
var treeSetting = {
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
			idKey : "id", 
			pIdKey : "pid",
			rootPId : 0
		// 根节点
		}
	},
	callback : {
		onRightClick : OnRightClick,
		beforeClick : function() {
		},
		onClick : function(event, treeId, treeNode) {
			var treeObj = $.fn.zTree.getZTreeObj("systemMetaCata");
			treeObj.checkNode(treeNode, true, true);
			treeNode.checked = true;
			treeObj.updateNode(treeNode);
			var id = treeNode.id;
			var name = treeNode.name;
			var url='';
			if (id == "0") {
				url=$("#basePath").val()+"/console/systemMeta/getSystemMetas.xsp";
				document.getElementById("iframe_systemMeta").src = encodeURI(url) ;
			} else {
				url=$("#basePath").val()+"/console/systemMeta/getSystemMetas.xsp?metaCataId="+ id+"&cataName="+name;
				document.getElementById("iframe_systemMeta").src =  encodeURI(url);
			}
		}
	}
};
/*
 * 获取所有的分类数据 以树状图的形式展示
 */
function getSystemMetaCatas() {
	$.ajax({
		type : 'post',
		url : $("#basePath").val()
				+ '/console/systemMetaCata/getSystemMetaCatas.action',
		dataType : 'json',
		success : function(result) {
			if (result.state == true) {
				var ztrees = $.fn.zTree.init($("#systemMetaCata"), treeSetting,
						result.data);
				ztrees.expandAll(true);// 全部展开
			}
		}
	});

}

$(function() {
	getSystemMetaCatas();
});
