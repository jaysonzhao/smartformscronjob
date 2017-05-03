//由于选人对话框会反向调用父窗口的函数，所以要添加下面的空函数。
function _tbActivityOwner_getUserDetail(){}

$(function() {
	var ctxPath=$("#ctxPath").val();
	$('#grid1').datagrid({
		url:ctxPath+"/console/bpm/instanceadmin/listallinst.action?r1="+Math.random(),
		toolbar:"#toolbar",
		pagination:"true",
		pageSize:"50",
		rownumbers:"true",
		method:"get",
		fitColumns:"true",
		fit:"true"
	});

	$("#btnPause").click(function(evt) {
		evt.preventDefault();
		$("#taReason").val("");
		var rows = $('#grid1').datagrid('getChecked');
		if (rows.length > 0) {
			$('#dlgReason').dialog({
			    title: '暂停流程实例原因',
			    width: 400,
			    height: 300,
			    closed:false,
			    modal: true,
			    buttons:[{
			    	text:"确定",
			    	iconCls:"icon-ok", 
			    	handler:function() {
			    		var reason=$("#taReason").val();
			    		$.messager.progress({ 
						    title: '提示', 
						    msg: '请稍等，正在暂停流程实例...',
						    text:'处理中'
						});
						var instIds=[];
						$(rows).each(function(index, item) {
							instIds[index]=item.piid;
						});
						$.post(ctxPath+"/console/bpm/instanceadmin/pause.action", {
								instanceIds: instIds, 
								reason: reason
							}, 
							function(data) {
								$.messager.progress('close');
								$('#grid1').datagrid('reload');
								if (data.success) {
									$.messager.alert({ 
										title : '提示',
										msg : "指定的流程实例已全部暂停！",
										icon:"question",
										fn:function() {
											$('#dlgReason').dialog('close');
										}
									});
								} else {
									$.messager.alert({ 
										title : '提示',
										msg : "有部分流程实例（piid:"+data.errorInstId+
												"）无法暂停！原因："+data.error.msg,
										icon:"error"
									});
								}
							}
						);
			    	}
			    }, {
			    	text:"取消",
			    	iconCls:"icon-cancel",
			    	handler:function() {
			    		$('#dlgReason').dialog('close');
			    	}
			    }]
			});
		}
	});

	$("#btnResume").click(function(evt) {
		evt.preventDefault();
		$("#taReason").val("");
		var rows = $('#grid1').datagrid('getChecked');
		if (rows.length > 0) {
			$('#dlgReason').dialog({
			    title: '恢复流程实例原因',
			    width: 400,
			    height: 300,
			    closed:false,
			    modal: true,
			    buttons:[{
			    	text:"确定",
			    	iconCls:"icon-ok",
			    	handler:function() {
			    		var reason=$("#taReason").val();
			    		$.messager.progress({ 
						    title: '提示', 
						    msg: '请稍等，正在恢复流程实例...',
						    text:'处理中'
						});
						var instIds=[];
						$(rows).each(function(index, item) {
							instIds[index]=item.piid;
						});
						$.post(ctxPath+"/console/bpm/instanceadmin/resume.action", {
								instanceIds: instIds,
								reason: reason
							}, 
							function(data) {
								$.messager.progress('close');
								$('#grid1').datagrid('reload');
								if (data.success) {
									$.messager.alert({ 
										title : '提示',
										msg : "指定的流程实例已全部恢复！",
										icon:"question",
										fn:function() {
											$('#dlgReason').dialog('close');
										}
									});
								} else {
									$.messager.alert({ 
										title : '提示',
										msg : "有部分流程实例（piid:"+data.errorInstId+"）无法恢复！原因："+data.error.msg,
										icon:"error"
									});
								}
							}
						);
			    	}
			    }, {
			    	text:"取消",
			    	iconCls:"icon-cancel",
			    	handler:function() {
			    		$('#dlgReason').dialog('close');
			    	}
			    }]
			});
		}
	});

	$("#btnTerminate").click(function(evt) {
		evt.preventDefault();
		$("#taReason").val("");
		var rows = $('#grid1').datagrid('getChecked');
		if (rows.length > 0) {
			$('#dlgReason').dialog({
			    title: '停止流程实例原因',
			    width: 400,
			    height: 300,
			    closed:false,
			    modal: true,
			    buttons:[{
			    	text:"确定",
			    	iconCls:"icon-ok",
			    	handler:function() {
			    		var reason=$("#taReason").val();
			    		$.messager.progress({ 
						    title: '提示', 
						    msg: '请稍等，正在停止流程实例...',
						    text:'处理中'
						});
						var instIds=[];
						$(rows).each(function(index, item) {
							instIds[index]=item.piid;
						});
						$.post(ctxPath+"/console/bpm/instanceadmin/terminate.action", {
								instanceIds: instIds,
								reason: reason
							}, 
							function(data) {
								$.messager.progress('close');
								$('#grid1').datagrid('reload');
								if (data.success) {
									$.messager.alert({ 
										title : '提示',
										msg : "指定的流程实例已全部停止！",
										icon:"question",
										fn:function() {
											$('#dlgReason').dialog('close');
										}
									});
								} else {
									$.messager.alert({ 
										title : '提示',
										msg : "有部分流程实例（piid:"+data.errorInstId+"）无法停止！原因："+data.error.msg,
										icon:"error"
									});
								}
							}
						);
			    	}
			    }, {
			    	text:"取消",
			    	iconCls:"icon-cancel",
			    	handler:function() {
			    		$('#dlgReason').dialog('close');
			    	}
			    }]
			});
		}
	});
	
	//重新路由按钮点击事件
	$("#btnReroute").click(function(evt) {
		evt.preventDefault();
		var instanceId="";
		var row = $('#grid1').datagrid('getChecked');
		if (row.length > 1) {
			$.messager.alert({ 
			    title: '警告', 
			    msg: '只能选择一个流程实例进行重新路由！',
			    icon:'warning'
			});
			return false;
		} else if (row.length==1) {
			$("#rerouteInstId").val(row[0].piid);
			$('#rerouteForm').form('clear');
			$("#dlgReroute").dialog("open").dialog("setTitle", "重新路由实例");
			$("#cbRerouteActivity").combobox({
				valueField: 'activityBpdId',
				textField: 'activityName',
				url: ctxPath+"/console/bpmtag/routingtoolbar/getAllHumanActivity/"+
					row[0].piid+".action",
				loadFilter:function(data) {
					return data.activityMetas;
				}
			});
		}
	});

	//重新分派按钮点击事件
	$("#btnReassign").click(function(evt) {
		evt.preventDefault();
		var instanceId="";
		var row = $('#grid1').datagrid('getChecked');
		if (row.length > 1) {
			$.messager.alert({ 
			    title: '警告', 
			    msg: '只能选择一个流程实例进行重新分派！',
			    icon:'warning'
			});
			return false;
		} else if (row.length==1) {
			$("#reassignInstId").val(row[0].piid);
			$('#reassignForm').form('clear');
			$("#dlgReassign").dialog("open").dialog("setTitle", "任务重新分派");
			$("#cbTasks").combobox({
				valueField: 'activityBpdId',
				textField: 'activityName',
				url: ctxPath+"/console/bpmtag/routingtoolbar/getAllHumanActivity/"+
					row[0].piid+".action",
				loadFilter:function(data) {
					return data.activityMetas;
				}
			});
		}
	});

	$("#btnShowSearch").click(function(evt) {
		evt.preventDefault();
		$('#searchForm').form('clear');
		$("#dlgFind").dialog("open").dialog("setTitle", "查找流程实例"); 
	});

	//查找流程实例对话框“取消”按钮点击事件
	$("#dlg_btncancel").click(function(evt) {
		$('#dlgFind').dialog('close');
	});
	
	//查找流程实例对话框“重置”按钮点击事件
	$("#dlg_btnreset").click(function(evt) {
		$('#searchForm').form('clear');
	});

	//查找流程实例对话框“确定”按钮点击事件
	$("#dlg_btnsearch").click(function(evt) {
		$("#grid1").datagrid("load", {
			instanceName:$.trim($("#tbFindInstName").textbox("getValue")),
			instanceId:$.trim($("#tbFindInstId").textbox("getValue")),
			datDocId:$.trim($("#tbFindDocId").textbox("getValue")),
			startDate:$("#ddFindStartDate").datebox("getValue"),
			endDate:$("#ddFindEndDate").datebox("getValue")
		});
		$('#dlgFind').dialog('close');
	});
	
	//查找实例名称文本框按回车触发操作
	$("#tbFindInstName").textbox('textbox').keydown(function(e) {
		if (e.keyCode == 13) {
			$("#dlg_btnsearch").click();
		}
	});
	
	//重新路由实例对话框“取消”按钮点击事件
	$("#dlg_btncancel_reroute").click(function(evt) {
		$('#dlgReroute').dialog('close');
	});
	
	//重新路由实例对话框“重置”按钮点击事件
	$("#dlg_btnreset_reroute").click(function(evt) {
		$('#rerouteForm').form('clear');
	});
	
	//重新路由实例对话框“确定”按钮事件
	$("#dlg_btnok_reroute").click(function(evt) {
		var instanceId=$("#rerouteInstId").val();
		var rerouteActyId=$("#cbRerouteActivity").combobox("getValue");
		var actyOwner=$("#tbActivityOwner_num").val();
		var reason=$("#taRerouteReason").val();
		if ($.trim(rerouteActyId)=="") {
			$.messager.alert({ 
				title : '警告',
				msg : "请指定重新路由的活动环节！",
				icon:"warning"
			});
			return false;
		}
		if ($.trim(actyOwner)=="") {
			$.messager.alert({ 
				title : '警告',
				msg : "请指定活动环节处理人！",
				icon:"warning"
			});
			return false;
		}
		if ($.trim(reason)=="") {
			$.messager.alert({ 
				title : '警告',
				msg : "请填写重新路由的原因！",
				icon:"warning"
			});
			return false;
		}
		$.messager.confirm('询问','重新路由实例会关闭当前所有进行中的审批任务，然后跳转到制定环节，是否继续？',function(r){
			if (r){
				$.messager.progress({ 
				    title: '提示', 
				    msg: '请稍等，正在重新路由流程实例...',
				    text:'处理中'
				});
				$.post(ctxPath+"/console/bpm/instanceadmin/reroute.action", {
					rnum:Math.random(), 
					instanceId: instanceId,
					targetNodeId: rerouteActyId,
					activityOwners: actyOwner,
					reason: reason
				}, function(data) {
					$.messager.progress('close');
					$('#grid1').datagrid('reload');
					if (data.success) {
						$.messager.alert({ 
							title : '提示',
							msg : "流程实例已重新路由！",
							icon:"question",
							fn:function() {
								$('#dlgReroute').dialog('close');
							}
						});
					} else {
						$.messager.alert({ 
							title : '提示',
							msg : "流程实例（piid:"+instanceId+"）重新路由失败！原因："+data.msg,
							icon:"error"
						});
					}
				});
			}
		});
	});
	
	//重新路由实例对话框“选人”按钮事件
	$("#btnSelectRerouteOwner").click(function(evt) {
		evt.preventDefault();
		var url = ctxPath+"/console/tag/user/index.xsp?id=tbActivityOwner"
			+ "&documentId=&singleSelect=false";
		window.open(encodeURI(url));
	});
	
	//重新分派对话框“取消”按钮点击事件
	$("#dlg_btncancel_reassign").click(function(evt) {
		$('#dlgReroute').dialog('close');
	});
	
	//重新分派对话框“确定”按钮事件
	$("#dlg_btnok_reassign").click(function(evt) {
		var instanceId=$("#reassignInstId").val();
		var rerouteActyId=$("#cbRerouteActivity").combobox("getValue");
		var actyOwner=$("#tbActivityOwner_num").val();
		var reason=$("#taReason").val();
		if ($.trim(rerouteActyId)=="") {
			$.messager.alert({ 
				title : '警告',
				msg : "请指定重新路由的活动环节！",
				icon:"warning"
			});
			return false;
		}
		if ($.trim(actyOwner)=="") {
			$.messager.alert({ 
				title : '警告',
				msg : "请指定活动环节处理人！",
				icon:"warning"
			});
			return false;
		}
		if ($.trim(reason)=="") {
			$.messager.alert({ 
				title : '警告',
				msg : "请填写重新路由的原因！",
				icon:"warning"
			});
			return false;
		}
		$.messager.confirm('询问','重新路由实例会关闭当前所有进行中的审批任务，然后跳转到制定环节，是否继续？',function(r){
			if (r){
				$.messager.progress({ 
				    title: '提示', 
				    msg: '请稍等，正在重新路由流程实例...',
				    text:'处理中'
				});
				$.post(ctxPath+"/console/bpm/instanceadmin/reroute.action", {
					rnum:Math.random(), 
					instanceId: instanceId,
					targetNodeId: rerouteActyId,
					activityOwners: actyOwner,
					reason: reason
				}, function(data) {
					$.messager.progress('close');
					$('#grid1').datagrid('reload');
					if (data.success) {
						$.messager.alert({ 
							title : '提示',
							msg : "流程实例已重新路由！",
							icon:"question",
							fn:function() {
								$('#dlgReroute').dialog('close');
							}
						});
					} else {
						$.messager.alert({ 
							title : '提示',
							msg : "流程实例（piid:"+instanceId+"）重新路由失败！原因："+data.msg,
							icon:"error"
						});
					}
				});
			}
		});
	});
	
	//重新分派对话框“选人”按钮事件
	$("#btnSelectReassignOwner").click(function(evt) {
		evt.preventDefault();
		var url = ctxPath+"/console/tag/user/index.xsp?id=tbActivityOwner"
			+ "&documentId=&singleSelect=false";
		window.open(encodeURI(url));
	});
});
