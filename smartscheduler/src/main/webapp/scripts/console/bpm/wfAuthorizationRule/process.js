function datagrid() {
	$('#datagrid').datagrid(
			{
				fit : true,
				singleSelect:true,
				url : $("#basePath").val()
						+ '/workflow/bpm/AuthorizationRule/processData.action',
				method : 'POST',
				fitColumns:true,
				height:"50%",
				queryParams : {
					"workflowCataId" : $("#workflowCataId").val(),
					"authorizationRuleId":$("#authorizationRuleId").val()
				},
				loadMsg : '数据加载中请稍候……',
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
					field : 'state',
					title : 'state',
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
				}
				] ],
				onSelect : function(rowData) {
				//	$('#datagrid').datagrid("unselectAll");
				},
				onDblClickRow : function(rowIndex, rowData) {
				},
				onLoadSuccess:function(data) {
				    var rowData = data.rows;
				    $.each(rowData, function (idx, val) {
				        if(val.state=="1"){
				           $("#datagrid").datagrid("selectRow", idx);
				        }
				        
				    });
				}
			});
}

$(function(){
	
	 datagrid()
});