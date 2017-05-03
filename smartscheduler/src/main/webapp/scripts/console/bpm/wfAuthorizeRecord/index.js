function datagrid(ruleName) {
	$('#datagrid')
			.datagrid(
					{
						height : '95%',
						fit : true,
						url : $("#basePath").val()
								+ '/workflow/bpm/wfAuthorizeRecord/findByPage.action',
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
						columns : [ [
								{
									field : 'ck',
									checkbox : true
								},
								{
									field : 'authorizationRuleId',
									title : 'authorizationRuleId',
									width : 100,
									align : 'center',
									hidden : true
								},
								{
									field : 'ruleName',
									title : '授权规则名称',
									width : 200,
									align : 'center'
								},
								{
									field : 'authorizerName',
									title : '授权者',
									width : 130,
									align : 'center'
								},
								{
									field : 'licensedToName',
									title : '授权给',
									width : 130,
									align : 'center'
								},
								{
									field : 'effectTime',
									title : '生效时间',
									width : 200,
									align : 'center',
									formatter : function(value, rec, index) {
										var s = new Date(value)
												.pattern("yyyy-MM-dd hh:mm:ss");
										return s;
									}
								},
								{
									field : 'documentId',
									title : '审批文档',
									width : 200,
									align : 'center',
									formatter : function(value, rec, index) {
										var appId = rec.appId;
										var result = "<a href=\""
												+ $("#basePath").val()
												+ "/console/template/engine/opendocument/"
												+ appId
												+ "/"
												+ value
												+ ".xsp\" target=\"_blank\">详情</a>";
										return result;
									}
								},
								{
									field : 'createTime',
									title : '创建时间',
									width : 200,
									align : 'center',
									formatter : function(value, rec, index) {
										var s = new Date(value)
												.pattern("yyyy-MM-dd hh:mm:ss");
										return s;
									}
								},
								{
									field : 'updateTime',
									title : '更新时间',
									width : 200,
									align : 'center',
									formatter : function(value, rec, index) {
										var s = new Date(value)
												.pattern("yyyy-MM-dd hh:mm:ss");
										return s;
									}
								} ] ],
						onSelect : function(rowData) {
							$('#datagrid').datagrid("unselectAll");
						},
						onDblClickRow : function(rowIndex, rowData) {
							
						}
					});
}

function searchByName() {
	datagrid($("#name").val());
}

$(function() {
	searchByName();
});