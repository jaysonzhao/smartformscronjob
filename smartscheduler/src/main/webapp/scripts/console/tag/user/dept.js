/** ztree的参数配置，setting主要是设置一些tree的属性，是本地数据源，还是远程，动画效果，是否含有复选框等等* */
var dataGlobal = new Array();
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
		return $("#basePath").val()+"/console/department/getOrgDepartmentTreeById.action?id="
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
		var zTree = $.fn.zTree.getZTreeObj("processCata"), totalCount = treeNode.count;
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
		var zTree = $.fn.zTree.getZTreeObj("processCata");
		alert("异步获取数据出现异常。");
		//	treeNode.icon = "";
		zTree.updateNode(treeNode);
	}
	function ajaxGetNodes(treeNode, reloadType) {
		var zTree = $.fn.zTree.getZTreeObj("processCata");
		if (reloadType == "refresh") {
			//treeNode.icon = "../../../css/zTreeStyle/img/loading.gif";
			zTree.updateNode(treeNode);
		}
		zTree.reAsyncChildNodes(treeNode, reloadType, true);
	}
	
function getQueryString(key) {
	var reg = new RegExp("(^|&)" + key + "=([^&]*)(&|$)");
	var result = window.location.search.substr(1).match(reg);
	return result ? decodeURIComponent(result[2]) : null;
}

/*function _getOrgDepartmentTree() {
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

}*/

function _getAddCheckedUsers() {
	var singleSelect=getQueryString("singleSelect");
	var dispName=getQueryString("dispName");
	//console.log("singleSelect:"+singleSelect);
	if(singleSelect!=""&&singleSelect=="true"){
		$("#_checkedUser").empty();
		$("#_checkedUser").html("");
		//console.log("clear empty html");
		
	}
	//console.log("dispName:"+dispName);
	var rows = document.getElementById("userIframeId").contentWindow.$(
			"#datagrid").datagrid('getChecked');
	for (var i = 0; i < rows.length; i++) {
		var select = document.getElementById("_checkedUser");
		var flag = true;
		for (var b = 0; b < select.length; b++) {
			if (select[b].value == rows[i].empNum) {
				flag = false;
			}
		}
		var rowValue="";
		if(dispName==""||dispName==null){
			rowValue=rows[i]["nickName"];
		}else{
			rowValue=rows[i][dispName];
		}
		var opts = "<option value='" + rows[i].empNum + "'>" +rowValue
				+ "</option>";
		if (flag) {
			dataGlobal.push(rows[i]); 
			$("#_checkedUser").append(opts);
		}
	}
	$("#_checkedUser").attr("selected",true);
	//$("#_checkedUser").focus();
}


function _romoveCheckedUsers() {
	$("#_checkedUser").find("option:selected").remove();
}
function _romoveAllCheckedUsers(){
	$("#_checkedUser").empty();
}
function _romoveOptionCheckedUsers(empNums){
	  for(var i=0;i<empNums.length;i++){
    	    $("#_checkedUser option[value='"+empNums[i]+"']").remove();
        }
}

function _saveCheckedUsers() {
	var id = getQueryString("id");
	var empNums = "";
	var empNames = "";
	var returnField="";
	var alreadSelect = document.getElementById("_checkedUser");
	for (var i = 0; i < alreadSelect.length; i++) {
		empNums += alreadSelect[i].text + "(" + alreadSelect[i].value + ");";
		empNames += alreadSelect[i].text + ";";
		returnField+="'"+alreadSelect[i].value+"',";
	}
	
	try {
		window.opener.$("#" + id + "_num").val(empNums);
		window.opener.$("#" + id).val(empNames);
		window.opener.$("#"+id+"_returnField").val(returnField);
	var f="_"+id+"_getUserDetail()";
	//console.log(f);
	//console.log(f);
	window.opener.eval(f);
			
		
	} catch (e) {
	}
   window.close();
}

function _cancelCheckedUsers(){
   window.close();
}

$(function() {
	$("#_getAddCheckedUsers").on("click",function(){
		_getAddCheckedUsers();
		});
	dataGlobal = new Array();
	$.ajax({
		type : 'post',
		url : $("#basePath").val()+'/console/department/getOrgDepartmentTreeById.action?id=-1',
		dataType : 'json',
		success : function(result) {
			var ztrees = $.fn.zTree
					.init($("#processCata"), setting,
							result);
			ztrees.expandAll(true);// 全部展开
			var nodes = ztrees.getNodes();
			for (var i = 0; i < nodes.length; i++) { //设置节点展开
				ztrees.expandNode(nodes[i], true,
						true, true);
			}
		}
	});
	//_getOrgDepartmentTree();
	var data =window.opener.$("#"+getQueryString("id")+"_num").val();
	$("#_checkedUser").html("");
	if ("undefined"!=typeof(data)&&data != "") {
		var dataArray = new Array(); // 定义一数组
		dataArray = data.split(");"); // 字符分割
		for (i = 0; i < dataArray.length; i++) {
			var obj = dataArray[i];
			if (obj != "") {
				var objArray = obj.split("(");
				if (objArray.length == 2) {
					var opts = "<option value='" + objArray[1] + "'>"
							+ objArray[0] + "</option>";
					$("#_checkedUser").append(opts);
				}
			}
		}
	}
	

});
