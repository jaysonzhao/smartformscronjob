function getRuleCondition(){
$("#dg").datagrid({
						url: $("#basePath").val()+"/console/datRule/getRuleConditions.action?ruleId="+$("#ruleId").val(),
						toolbar : [ {
							text : '添加',
							iconCls : 'icon-add',
							handler : function() {
								addRow();
							}
						}, '-', {
							text : '删除',
							iconCls : 'icon-remove',
							handler : function() {
								delRow();
							}
						} ],
						columns : [ [ {
							field : "leftValue",
							title : "左边参数",
							width : 60,
							editor : {
								type : "text",
								options : {
									required : true
								}
							}
						},

						{
							field : "valueOperator",
							title : "运算符",
							width : 20,
							editor : {
								type : "combobox",
								options : {
									valueField : "id",
									textField : "text",
									data : [
									 {
										id :"==",
										text : "=="
									},
									 {
										id :">",
										text : ">"
									}, {
										id : "<",
										text : "<"
									}, {
										id : "=<",
										text : "=<"
									}
									, {
										id : ">=",
										text : ">="
									}
									 ],
									panelHeight : 150,
									required : true
								}
							}
						}, {
							field : "rightValue",
							title : "右边值",
							width : 60,
							editor : {
								type : "text",
								options : {
									required : true
								}
							}
						}, 
						{
							field : "rightValueType",
							title : "值类型",
							width : 60,
							editor : {
								type : "combobox",
								options : {
									valueField : "id",
									textField : "text",
									data : [
									 {
										id :"String",
										text : "String"
									},
									 {
										id :"int",
										text : "int"
									}
									 ],
									panelHeight : 50,
									required : true
								}
							}
						}
						] ],
						fitColumns : true,
						width : 880,
						height : 330,
						onClickRow : onClickRow
					});
				}
				// 判断时候存在编辑中的行
				var editIndex = null;
				function endEditing() {
					if (editIndex == null) {
						return true
					}
					if ($('#dg').datagrid('validateRow', editIndex)) {
						$('#dg').datagrid('endEdit', editIndex);
						editIndex = null;
						return true;
					} else {
						return false;
					}
				}
				// datagrid行点击事件
				function onClickRow(index, row) {
					if (editIndex != index) {
						if (endEditing()) {
							$("#dg").datagrid("selectRow", index).datagrid(
									"beginEdit", index);
							editIndex = index;
						} else {
							$("#dg").datagrid("selectRow", editIndex);
							//$("#dg").datagrid("selectRow", index).datagrid(
								//	"beginEdit", index);
						}
					}
					//$('#dg').datagrid('endEdit', editIndex);  
				}
				// 添加一行
				function addRow() {
					
					if (endEditing()) {
						//$("#dg").datagrid("appendRow");
						$("#dg").datagrid("appendRow", {
							leftValue : "",
							valueOperator : "==",
							rightValue : "",
							rightValueType:"String"
						});
						editIndex = $("#dg").datagrid("getRows").length - 1;
						$("#dg").datagrid("selectRow", editIndex).datagrid(
								"beginEdit", editIndex);
					}
				}
				// 删除一行
				function delRow() {
					if (editIndex == null) {
						return
					}
					$('#dg').datagrid('cancelEdit', editIndex).datagrid(
							'deleteRow', editIndex);
					editIndex = null;
				}

function openDatRuleDlg(authorizationRuleId) {
	getRuleCondition();
	$('#openDatRuleDlg').dialog('open').dialog('setTitle', ' ');
	$("#ruleId").val("");
	$("#ruleName").val("");
	$('#ruleType').val("PARAMS");
	$('#isActivate').val("on");
	var date=new Date();
	$("#effectTime").val(date.pattern("yyyy-MM-dd"));
	date.setDate(date.getDate()+10);
	$("#invalidTime").val(date.pattern("yyyy-MM-dd"));
	$("#retrunType").val("")
	$("#ruleProcess").val("");
}

function closedDatRuleDlg(){
	$('#openDatRuleDlg').dialog('close');
}

function updateDatRule(rowData){	
	
$('#openDatRuleDlg').dialog('open').dialog('setTitle', ' ');
$("#ruleId").val(rowData.ruleId);
$("#ruleName").val(rowData.ruleName);
$('#ruleType').val(rowData.ruleType);
$('#isActivate').val(rowData.isActivate);
$("#effectTime").val(new Date(rowData.startTime).pattern("yyyy-MM-dd"));
$("#invalidTime").val(new Date(rowData.endTime).pattern("yyyy-MM-dd"));
$("#retrunType").val(rowData.retrunType)
if(rowData.editMode=="ADV"){
$("#ruleProcess").val(rowData.ruleProcess);
}else{
	$("#ruleProcess").val("");
}
getRuleCondition();
}
function datagrid(ruleName) {
	$('#datagrid').datagrid(
			{
				height : '95%',
				fit : true,
				url : $("#basePath").val()
						+ '/console/datRule/findByPage.action',
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
					field : 'ruleId',
					title : 'ruleId',
					width : 100,
					align : 'center',
					hidden : true
				}, {
					field : 'ruleName',
					title : '规则名称',
					width : 200,
					align : 'center'
				}, {
					field : 'isActivate',
					title : '状态',
					width : 100,
					align : 'center',
					formatter : function(value, rec, index) {
						if (value == "on") {
							return "激活";
						} else if (value == "off") {
							return "停用";
						} else {
							return value;
						}
					}
				}, {
					field : 'startTime',
					title : '生效时间',
					width : 200,
					align : 'center',
					formatter : function(value, rec, index) {
						var s = new Date(value)
								.pattern("yyyy-MM-dd");
						return s;
					}
				}, {
					field : 'endTime',
					title : '失效时间',
					width : 200,
					align : 'center',
					formatter : function(value, rec, index) {
						var s = new Date(value)
								.pattern("yyyy-MM-dd");
						return s;
					}
				}, {
					field : 'editMode',
					title : '编辑模式',
					width : 130,
					align : 'center',
						formatter : function(value, rec, index) {
							if (value == "STD") {
								return "标准";
							} else if (value == "ADV") {
								return "高级";
							} else {
								return value;
							}
						}
					}, {
					field : 'createTime',
					title : '创建时间',
					width : 200,
					align : 'center',
					formatter : function(value, rec, index) {
						var s = new Date(value).pattern("yyyy-MM-dd hh:mm:ss");
						return s;
					}
				}
				] ],
				onSelect : function(rowData) {
					$('#datagrid').datagrid("unselectAll");
				},
				onDblClickRow : function(rowIndex, rowData) {
					updateDatRule(rowData);
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
										ruleIds : []
								};
								for (var i = 0; i < rows.length; i++) {
									jsonData.ruleIds
											.push(rows[i].ruleId);
								}
								$
										.ajax({
											url : $("#basePath").val()
													+ "/console/datRule/deletDatRule.action",
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


function saveDatRule(){
	if(editIndex!=null){
	$('#dg').datagrid('endEdit', editIndex);  
	}
	var ruleId=$("#ruleId").val();
	var ruleName = $("#ruleName").val();
	var ruleType=$('#ruleType').val();
	var isActivate=$('#isActivate').val();
	var effectTime=$("#effectTime").val();
	var invalidTime=$("#invalidTime").val();
	var retrunType = $("#retrunType").val();
	var data = $('#dg').datagrid('getData');
    var ruleProcess=$("#ruleProcess").val();
    $.ajax({
		url : $("#basePath").val()
				+ "/console/datRule/saveOrUpdate.action",
		type : "post",
		data : {
			ruleId:ruleId,
			ruleName:ruleName,
			ruleType:ruleType,
			isActivate:isActivate,
			effectTime:effectTime,
			invalidTime:invalidTime,
			retrunType:retrunType,
			ruleProcess:ruleProcess,
			data:JSON.stringify(data)
		},
		dataType : "json",
		success : function(result) {
			if (result.state == true) {
				$('#openDatRuleDlg').dialog('close');
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
	datagrid("");
});