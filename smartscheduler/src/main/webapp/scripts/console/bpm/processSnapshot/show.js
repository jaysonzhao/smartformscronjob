
function datagrid(workflowName) {
	$('#datagrid').datagrid(
			{
				height : '95%',
				fit : true,
				url : $("#basePath").val()
						+ '/console/bpm/processmeta/findByPage.action',
				method : 'POST',
				queryParams : {
					"workflowCataId" : $("#workflowCataId").val(),
					"workflowName" : workflowName
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
				singleSelect : true,
				columns : [ [ {
					field : 'ck',
					checkbox : true
				}, {
					field : 'workflowId',
					title : 'workflowId',
					width : 100,
					align : 'center',
					hidden : true
				}, 
				 {
					field : 'workflowCataId',
					title : 'workflowCataId',
					width : 100,
					align : 'center',
					hidden : true
				}, 
				{
					field : 'workflowName',
					title : '流程名称',
					width : 250,
					align : 'center'
				}, {
					field : 'processAppId',
					title : '应用库id',
					width : 200,
					align : 'center'
				}, {
					field : 'bpdId',
					title : '流程图id',
					width : 200,
					align : 'center'
				}
				] ],
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
	datagrid("");
});