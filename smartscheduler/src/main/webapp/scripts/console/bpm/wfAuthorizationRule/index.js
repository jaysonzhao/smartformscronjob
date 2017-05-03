

function saveSelectedWorkFlow(){
	var workflowIds = [];
	var workflowNames = [];
	var rows = document.getElementById("iframe_processmeta").contentWindow.$(
	"#datagrid").datagrid('getChecked');
	if (rows.length > 0) {
		for (var i = 0; i < rows.length; i++) {
			var workflowId = rows[i].workflowId;
			var workflowName = rows[i].workflowName;
			workflowIds.push(workflowId);
			if(workflowNames.length!=0){
				workflowNames.push("\n"+workflowName);
			}else{
				workflowNames.push(workflowName);
			}
		}
		$("#iframe_config").contents().find("#selectedWorkflow").val(workflowNames);
		$("#iframe_config").contents().find("#selectedWorkflowIds").val(workflowIds);
		 $('#workFlow').window('close');          
	} else {
		layer.msg('至少选择一个要授权的流程');
	}
}


function openWorkFlow(){
	var contents = $("#iframe_config").contents();
	var authorizationRuleId = contents.find("#authorizationRuleId").val();
	$('#iframe_processmeta').attr("src",$("#basePath").val()+"/workflow/bpm/AuthorizationRule/process.xsp?authorizationRuleId="+authorizationRuleId);  
	$("#workFlow").dialog('open');  
}

function openAuthorizationRuleDlg(authorizationRuleId) {
	$('#iframe_config').attr("src",$("#basePath").val()+ "/workflow/bpm/AuthorizationRule/config.xsp?authorizationRuleId="+authorizationRuleId);  
	$("#agentRules").dialog('open');  
	/*var url = $("#basePath").val()
			+ "/console/bpm/AuthorizationRule/config.xsp";
	if (authorizationRuleId != "") {
		url += "?authorizationRuleId=" + authorizationRuleId;
		$.ajax({
			url : url,
			type : "post",
			dataType : "json",
			success : function(data) {
				$("#authorizationRuleId").val(data.authorizationRuleId);
				$("#ruleName").val(data.wfAuthorizationRule.ruleName);
				$("#effectTime").val(data.wfAuthorizationRule.effectTimeFormat);
				$("#invalidTime").val(data.wfAuthorizationRule.invalidTimeFormat);
				$("#licensedTo").val(data.wfAuthorizationRule.licensedToName);
				
				console.log(data);
			},
			error : function(result) {
				console.log(result);
				console.log("error");
			}
		
		});
	}*/
}

function closeAutorizationRuleDla(){
    $('#agentRules').window('close');          
}

function closeworkFlowDla(){
    $('#workFlow').window('close');          
}

function datagrid(ruleName) {
	$('#datagrid').datagrid(
			{
				height : '95%',
				fit : true,
				url : $("#basePath").val()
						+ '/workflow/bpm/AuthorizationRule/findByPage.action',
				method : 'POST',
				queryParams : {
					"ruleName" : ruleName
				},
				striped : true,
				nowrap : true,
				pageSize : 17,
				pageNumber : 1,
				pageList : [ 17 ],
				showFooter : true,
				loadMsg : '数据加载中请稍候……',
				pagination : true,
				toolbar : "#toolbar",
				singleSelect : false,
				checkOnSelect : false,
				selectOnCheck : false,
				columns : [ [ {
					field : 'ck',
					checkbox : true
				}, {
					field : 'authorizationRuleId',
					title : 'authorizationRuleId',
					width : 100,
					align : 'center',
					hidden : true
				}, {
					field : 'ruleName',
					title : '授权规则名称',
					width : 200,
					align : 'center'
				}, {
					field : 'authorizerName',
					title : '授权者',
					width : 130,
					align : 'center'
				}, {
					field : 'licensedToName',
					title : '授权给',
					width : 130,
					align : 'center'
				}, {
					field : 'state',
					title : '状态',
					width : 100,
					align : 'center',
					formatter : function(value, rec, index) {
						if (value == "1") {
							return "启用";
						} else if (value == "0") {
							return "停用";
						} else {
							return value;
						}
					}
				}, {
					field : 'effectTime',
					title : '生效时间',
					width : 200,
					align : 'center',
					formatter : function(value, rec, index) {
						var s = new Date(value)
								.pattern("yyyy-MM-dd");
						return s;
					}
				}, {
					field : 'invalidTime',
					title : '失效时间',
					width : 200,
					align : 'center',
					formatter : function(value, rec, index) {
						var s = new Date(value)
								.pattern("yyyy-MM-dd");
						return s;
					}
				}, {
					field : 'creatorName',
					title : '创建者',
					width : 130,
					align : 'center'
				}, {
					field : 'createTime',
					title : '创建时间',
					width : 200,
					align : 'center',
					formatter : function(value, rec, index) {
						var s = new Date(value).pattern("yyyy-MM-dd hh:mm:ss");
						return s;
					}
				}, {
					field : 'updateByName',
					title : '更新者',
					width : 130,
					align : 'center'
				}, {
					field : 'updateTime',
					title : '更新时间',
					width : 200,
					align : 'center',
					formatter : function(value, rec, index) {
						var s = new Date(value).pattern("yyyy-MM-dd hh:mm:ss");
						return s;
					}
				} ] ],
				onSelect : function(rowData) {
					$('#datagrid').datagrid("unselectAll");
				},
				onDblClickRow : function(rowIndex, rowData) {
					openAuthorizationRuleDlg(rowData.authorizationRuleId);
				}
			});
}

function searchByName() {
	datagrid($("#name").val());
}

function destroyAuthorizationRule() {
	var rows = $('#datagrid').datagrid('getChecked');
	if (rows.length > 0) {
		$.messager
				.confirm(
						'温馨提示',
						'你真的要删除么?',
						function(r) {
							if (r) {
								var jsonData = {
									authorizationRuleIds : []
								};
								for (var i = 0; i < rows.length; i++) {
									jsonData.authorizationRuleIds
											.push(rows[i].authorizationRuleId);
								}
								$
										.ajax({
											url : $("#basePath").val()
													+ "/workflow/bpm/AuthorizationRule/deletWfAuthorizationRule.action",
											type : "post",
											data : jsonData,
											dataType : "json",
											success : function(result) {
												if (result.state == true) {
													$('#datagrid').datagrid(
															'reload');
												} else {
													$.messager.show({ // show
														// error
														// message
														title : '温馨提示',
														msg : result.msg
													});

												}
											}
										});
							}
						});
	}
}

function startWfAuthorizationRule() {
	var rows = $('#datagrid').datagrid('getChecked');
	if (rows.length > 0) {
		var jsonData = {
			authorizationRuleIds : []
		};
		for (var i = 0; i < rows.length; i++) {
			jsonData.authorizationRuleIds.push(rows[i].authorizationRuleId);
		}
		$.ajax({
					url : $("#basePath").val()
							+ "/workflow/bpm/AuthorizationRule/startWfAuthorizationRule.action",
					type : "post",
					data : jsonData,
					dataType : "json",
					success : function(result) {
						if (result.state == true) {
							$('#datagrid').datagrid('reload');
						} else {
							$.messager.show({ // show
								// error
								// message
								title : '温馨提示',
								msg : result.msg
							});

						}
					}
				});

	}
}

function stopWfAuthorizationRule() {
	var rows = $('#datagrid').datagrid('getChecked');
	if (rows.length > 0) {
		var jsonData = {
			authorizationRuleIds : []
		};
		for (var i = 0; i < rows.length; i++) {
			jsonData.authorizationRuleIds.push(rows[i].authorizationRuleId);
		}
		$.ajax({
					url : $("#basePath").val()
							+ "/workflow/bpm/AuthorizationRule/stopWfAuthorizationRule.action",
					type : "post",
					data : jsonData,
					dataType : "json",
					success : function(result) {
						if (result.state == true) {
							$('#datagrid').datagrid('reload');
						} else {
							$.messager.show({ // show
								// error
								// message
								title : '温馨提示',
								msg : result.msg
							});

						}
					}
				});

	}
}

//保存授权规则
function saveAuthorizationRule() {
	var contents = $("#iframe_config").contents();
	var ruleName = contents.find("#ruleName").val();
	var licensedTo = contents.find("#licensedTo_num").val();
	var effectTime = contents.find("#effectTime").val();
	var invalidTime = contents.find("#invalidTime").val();
	var authorizationRuleId = contents.find("#authorizationRuleId").val();
	var workflowIds = [];
	
    /*var district= iframe_config.window.$("#district").val();
    var companyNum=iframe_config.window.$("#companyNum").textbox('getValue');
    var companyName= iframe_config.window.$("#companyName").val();
    var deptNum=iframe_config.window.$("#deptNum").textbox('getValue');
    var deptName= iframe_config.window.$("#deptName").val();*/
    var advancedConditions= iframe_config.window.$("#advancedConditions").val();
    var datRuleId= iframe_config.window.$("#ruleId").val();
   // var advancedConditionValue= iframe_config.window.$("#advancedConditionValue").val();
	if (ruleName == ""||ruleName == null) {
		layer.msg('规则名称不能为空');
		return false;
	} else if (licensedTo == ""|| licensedTo==null) {
		layer.msg('授权给谁不能为空');
		return false;
	}else if (effectTime == ""|| effectTime==null) {
		layer.msg('启用时间不能为空');
		return false;
	} else if(""== invalidTime || invalidTime==null) {
		layer.msg('失效时间不能为空');
		return false;
	}  
	if(licensedTo!=""){
		var licensedToArray = new Array(); // 定义一数组
		licensedToArray = licensedTo.split("("); // 字符分割
         if(licensedToArray.length==2){
        	 licensedTo= licensedToArray[1];
        	 licensedTo=licensedTo.replace(");","");
         }
	}
/*	var rows = document.getElementById("iframe_processmeta").contentWindow.$(
			"#datagrid").datagrid('getChecked');
	if (rows.length > 0) {
		for (var i = 0; i < rows.length; i++) {
			var workflowId = rows[i].workflowId;
			workflowIds.push(workflowId);
		}
	} else {*/
		//如果没有重新选择流程，则保存原来的流程
		var workflowCataId  = contents.find("#selectedWorkflowIds").val();
		if(""!=workflowCataId&&null!=workflowCataId){
			workflowIds.push(workflowCataId);
		}else{
			layer.msg('至少选择一个要授权的流程');
			return false;
		}
	//}

	var loadTip = layer.open({
		type : 3
	});
	$.ajax({
		url : $("#basePath").val()
				+ "/workflow/bpm/AuthorizationRule/createOrUpdate.action",
		type : "post",
		data : {
			ruleName : ruleName,
			licensedTo : licensedTo,
			effectTime : effectTime,
			invalidTime : invalidTime,
			authorizationRuleId : authorizationRuleId,
			ruleType:contents.find('input[name=ruleType]:checked').val(),
			ruleMode:contents.find('input[name=ruleMode]:checked').val(),
			workflowIds : workflowIds,
			/* district:district,
		     companyNum:companyNum,
		     companyName:companyName,
		     deptNum:deptNum,
		     deptName:deptName,*/
		     advancedConditions:advancedConditions,
		     //advancedConditionValue:advancedConditionValue
		     datRuleId:datRuleId
		},
		dataType : "json",
		success : function(result) {
			layer.close(loadTip);
			if (result.state == false) {
				layer.msg(result.msg);
			} else {
				//如果选择立即生效，则保存成功后自动启动
				var selected = contents.find("input[name='effectiveMode']:checked").val();
				if("immediately"==selected){
					startAfterSave(authorizationRuleId);
				}
				layer.msg('保存成功');
				closeAutorizationRuleDla();
				location.reload();
			}
		},
		error : function() {
			layer.close(loadTip);
		}
	});
}

function startAfterSave(authorizationRuleId){
	var jsonData = {
			authorizationRuleIds : []
		};
		jsonData.authorizationRuleIds.push(authorizationRuleId);
		$.ajax({
					url : $("#basePath").val()
							+ "/workflow/bpm/AuthorizationRule/startWfAuthorizationRule.action",
					type : "post",
					data : jsonData,
					dataType : "json",
					success : function(result) {
						if (result.state == true) {
							$('#datagrid').datagrid('reload');
						} else {
							$.messager.show({ // show
								// error
								// message
								title : '温馨提示',
								msg : result.msg
							});

						}
					}
		});
}

$(function() {
	datagrid($("#activityMetaCfg").val());
});