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
		// onRightClick : OnRightClick,
		/** 回调函数的设置，随便写了两个* */
		beforeClick : function() {
		},
		onClick:function(event, treeId, treeNode) {
			var treeObj = $.fn.zTree.getZTreeObj("processCata");
			treeObj.checkNode(treeNode, true, true);
			treeNode.checked = true;
			treeObj.updateNode(treeNode);
		},
		 onDblClick: function(event, treeId, treeNode) {
			var treeObj = $.fn.zTree.getZTreeObj("processCata");
			treeObj.checkNode(treeNode, true, true);
			treeNode.checked = true;
			treeObj.updateNode(treeNode);
			var id = treeNode.id;
			if(id!=0){
			var select = document.getElementById("_checkedUser");
			var flag = true;
			for (var b = 0; b < select.length; b++) {
				if (select[b].value ==id) {
					flag = false;
				}
			}
			var opts = "<option value='" +id + "'>" +treeNode.name
					+ "</option>";
			if (flag) {
				$("#_checkedUser").append(opts);
			}
		}
		 }
	}
};

function getQueryString(key) {
	var reg = new RegExp("(^|&)" + key + "=([^&]*)(&|$)");
	var result = window.location.search.substr(1).match(reg);
	return result ? decodeURIComponent(result[2]) : null;
}

function _getOrgDepartmentTree() {
	$.ajax({
		type : 'post',
		url : $("#basePath").val()
				+ '/console/department/getOrgDepartmentTree.action',
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

function _getAddCheckedUsers() {
	 var treeObj=$.fn.zTree.getZTreeObj("processCata");
     nodes=treeObj.getCheckedNodes(true);
     for(var i=0;i<nodes.length;i++){
         var name=nodes[i].name;
          var id= nodes[i].id; 
      	var select = document.getElementById("_checkedUser");
		var flag = true;
		if(id!=0){
		for (var b = 0; b < select.length; b++) {
			if (select[b].value ==id) {
				flag = false;
			}
		}
		var opts = "<option value='" +id + "'>" +name
				+ "</option>";
		if (flag) {
			$("#_checkedUser").append(opts);
		}
		}
      }
}
function _romoveCheckedUsers() {
	$("#_checkedUser").find("option:selected").remove();
}

function _saveCheckedUsers() {
	var empNums = "";
	var empNames = "";
	var alreadSelect = document.getElementById("_checkedUser");
	for (var i = 0; i < alreadSelect.length; i++) {
		empNums += alreadSelect[i].text + "(" + alreadSelect[i].value + ");";
		empNames += alreadSelect[i].text + ";";
	}
	var id =getQueryString("id");
	try {
		window.opener.$("#" + id + "_num").val(empNums);
		window.opener.$("#" + id).val(empNames);
	} catch (e) {
	}
	window.close();
}

$(function() {
	_getOrgDepartmentTree();
});
