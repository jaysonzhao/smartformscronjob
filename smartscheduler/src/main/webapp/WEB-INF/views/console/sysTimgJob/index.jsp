<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<jsp:include page="../resources.jsp"></jsp:include>


<script type="text/javascript">
    function updateForm(row){
  $('#formId').empty();
  $.ajax({
        		url:"${pageContext.request.contextPath}/console/detform/getforms/getAllFormNames.action",
        		type:"post",
        		 data:{"datappid": $("#appId").val()}, 
        		dataType:"json",
        		success:function(result){
        			   if (result.state==false){
        	                $.messager.show({
        	                    title: '温馨提示',
        	                    msg: result.msg
        	                });
        	            } else {
        	                var datas=result.datas;
        	                $('#formId').combobox({
                                  valueField: 'id',
                                   data:datas,
                                   textField: 'text',
                                   editable : true,
                                   multiple:"true"
                                });
                                $('#formId').combobox('setValues',row.formId)
        	            }
        		}
        	}); 
}
		function editSysTimeJob(row) {
		if (row) {
			$('#sysTimeJob_dlg').dialog('open').dialog('setTitle', '');
			$('#fm').form('clear');
			$('#fm').form('load', row);
			 updateForm(row);
			 
		}
	}
function datagrid(){
$('#datagrid').datagrid({
	   	 		height: '100%',
	    		fit:true,
	    		url: '<%=basePath%>console/quartz/getAllScheduleJob.action',
	    		method: 'POST',
	    		striped: true,
			    nowrap: true,
			    pageSize: 17,
			    pageNumber:1, 
			    pageList: [17],
			    showFooter: true,
				loadMsg : '数据加载中请稍候……',
				pagination : true,
			    toolbar:"#comToolbar",
			    singleSelect: false,
	            checkOnSelect: false, 
	             selectOnCheck: false,
			    columns: [[
			      { field: 'ck', checkbox: true },
			        { field: 'jobId', title: '任务ID', width: 300,align:'center',hidden:true},
			        { field: 'jobName', title: '任务名称', width: 300,align:'center'},
			        {field:'cronExpression',title:'任务执行时间',width:200,align:'center'},
			         {field:'schedulerName',title:'任务',width:100,align:'center'},
			        {field:'jobStatus',title:'任务状态',width:100,align:'center',
                        formatter : function(value, rec, index) {
                         if(value=="1"){
                          return "运行中"
                         }else if(value==2){
                            return "停止"
                         }else if(value=="3"){
                           return "暂停";
                         }else{
                           return value;
                         }		
						}
					 },
			        {field:'jobType',title:'任务类型',width:100,align:'center',
                        formatter : function(value, rec, index) {
                         if(value=="custom"){
                          return "自定定义任务"
                         }else if(value=="collection"){
                            return "数据采集任务"
                         }else{
                           return value;
                         }		
						}
					 },
			       {field:'runDuration',title:'运行时长',width:100,align:'center'},
			        {field:'errorNotify',title:'异常通知',width:100,align:'center',
                        formatter : function(value, rec, index) {
                         if(value=="enabled"){
                          return "启用"
                         }else if(value=="disabled"){
                            return "关闭"
                         }else{
                           return value;
                         }		
						}
					 },
			        {field:'notifyEmail',title:'异常通知人邮件地址',width:200,align:'center'},
			      {field:'errorStop',title:'异常停止',width:100,align:'center',
                        formatter : function(value, rec, index) {
                         if(value=="enabled"){
                          return "启用"
                         }else if(value=="disabled"){
                            return "关闭"
                         }else{
                           return value;
                         }		
						}
					 },
					  {field:'startRunTime',title:'最后一次运行开始时间',width:150,align:'center'},
					  {field:'lastRunTime',title:'最后一次运行结束时间',width:150,align:'center'}
					  /*  {field:'source',title:'源表',width:100,align:'center'},
					   {field:'target',title:'目标表',width:100,align:'center'},
					   {
										field : 'creator',
										title : '创建者',
										width : 100,
										align : 'center'
									},
									{
										field : 'createTime',
										title : '创建时间',
										width : 150,
										align : 'center',
										formatter : function(value, rec, index) {
											var s = new Date(value)
													.pattern("yyyy-MM-dd hh:mm:ss");
											return s;
										}
									},
									{
										field : 'updateBy',
										title : '更新者',
										width :100,
										align : 'center'
									},
									{
										field : 'updateTime',
										title : '更新时间',
										width : 150,
										align : 'center',
										formatter : function(value, rec, index) {
											var s = new Date(value)
													.pattern("yyyy-MM-dd hh:mm:ss");
											return s;
										}
									}  */
			    ]],
			   onSelect: function (rowData) {
	    	   $('#datagrid').datagrid("unselectAll");
	          } ,
	    		onDblClickRow :function(rowIndex,rowData){
				editSysTimeJob(rowData);
	   			}
			});


}

function startTasks(){
	var rows =   $('#datagrid').datagrid('getChecked');
		if(rows.length>0){
			var taskNames=new Array();
			for(var i=0; i<rows.length; i++){
        		taskNames.push(rows[i].jobId);
			}
	  $.ajax({
		url:"<%=basePath%>console/quartz/startTasks.action",
		type:"post",
		data:{
		jobsId: taskNames
		},
		dataType:"json",
		success:function(result){
			   if (result.success){
	                 $.messager.show({
	                    title: '温馨提示',
	                    msg:"任务启动成功"
	                });
	                 $('#datagrid').datagrid('reload');    // reload the user data
	            } else {
	                $.messager.show({
	                    title: '温馨提示',
	                    msg: result.msg
	                });
	            }
		}
	});
      }else{
               $.messager.show({    // show error message
		         title: '温馨提示',
		         msg: '请选择要启动的任务'
		         });
             }
}

function stopTasks(url,msg,warm){
	var rows =   $('#datagrid').datagrid('getChecked');
		if(rows.length>0){
			var taskNames=new Array();
			for(var i=0; i<rows.length; i++){
        		taskNames.push(rows[i].jobId);
			}
	  $.ajax({
		url:url,
		type:"post",
		data:{
		jobsId: taskNames
		},
		dataType:"json",
		success:function(result){
			   if (result.success){
	                 $.messager.show({
	                    title: '温馨提示',
	                    msg:msg
	                });
	                 $('#datagrid').datagrid('reload');    // reload the user data
	            } else {
	                $.messager.show({
	                    title: '温馨提示',
	                    msg: result.msg
	                });
	            }
		}
	});
      }else{
               $.messager.show({    // show error message
		         title: '温馨提示',
		         msg:warm
		         });
             }
}

function pauseTasks(){
	var rows =   $('#datagrid').datagrid('getChecked');
		if(rows.length>0){
			var taskNames=new Array();
			for(var i=0; i<rows.length; i++){
        		taskNames.push(rows[i].jobId);
			}
	  $.ajax({
		url:"<%=basePath%>console/quartz/pauseTasks.action",
		type:"post",
		data:{
		jobsId: taskNames
		},
		dataType:"json",
		success:function(result){
			   if (result.success){
	                 $.messager.show({
	                    title: '温馨提示',
	                    msg:"任务暂停成功"
	                });
	                 $('#datagrid').datagrid('reload');    // reload the user data
	            } else {
	                $.messager.show({
	                    title: '温馨提示',
	                    msg: result.msg
	                });
	            }
		}
	});
      }else{
               $.messager.show({    // show error message
		         title: '温馨提示',
		         msg: '请选择要暂停的任务'
		         });
             }
}

function triggerTasks(){
	var rows =   $('#datagrid').datagrid('getChecked');
		if(rows.length>0){
			var taskNames=new Array();
			for(var i=0; i<rows.length; i++){
        		taskNames.push(rows[i].jobId);
			}
	  $.ajax({
		url:"<%=basePath%>console/quartz/triggerTasks.action",
		type:"post",
		data:{
		jobsId: taskNames
		},
		dataType:"json",
		success:function(result){
			   if (result.success){
	                 $.messager.show({
	                    title: '温馨提示',
	                    msg:"任务启动成功"
	                });
	                 $('#datagrid').datagrid('reload');    // reload the user data
	            } else {
	                $.messager.show({
	                    title: '温馨提示',
	                    msg: result.msg
	                });
	            }
		}
	});
      }else{
               $.messager.show({    // show error message
		         title: '温馨提示',
		         msg: '请选择要启动的任务'
		         });
             }
}

function resumeTasks(){
	var rows =   $('#datagrid').datagrid('getChecked');
		if(rows.length>0){
			var taskNames=new Array();
			for(var i=0; i<rows.length; i++){
        		taskNames.push(rows[i].jobId);
			}
	  $.ajax({
		url:"<%=basePath%>console/quartz/resumeTasks.action",
		type:"post",
		data:{
		jobsId: taskNames
		},
		dataType:"json",
		success:function(result){
			   if (result.success){
	                 $.messager.show({
	                    title: '温馨提示',
	                    msg:"任务恢复成功"
	                });
	                 $('#datagrid').datagrid('reload');    // reload the user data
	            } else {
	                $.messager.show({
	                    title: '温馨提示',
	                    msg: result.msg
	                });
	            }
		}
	});
      }else{
               $.messager.show({    // show error message
		         title: '温馨提示',
		         msg: '请选择要恢复的任务'
		         });
             }
}

$(function(){
datagrid();

});

</script>
</head>


<body>
<div id="datagrid"></div>
<div id="comToolbar">
		<div>
		    <a href="javascript:void(0)"   class="easyui-linkbutton "  iconCls="icon-add"   plain="true"  onclick="openDlg()">
              创建任务
	         </a> 
	         
	         <a href="javascript:void(0)"   class="easyui-linkbutton "  iconCls="icon-remove"   plain="true"  onclick='stopTasks("<%=basePath%>console/quartz/deletTasks.action","任务删除成功","请选择要删除的任务")'>
              删除任务
	         </a> 
			<a href="javascript:void(0)"  class="easyui-linkbutton "  iconCls="icon-reload"   plain="true"  onclick="startTasks()">
             启动任务
	         </a> 
			<a href="javascript:void(0)"  class="easyui-linkbutton "  iconCls="icon-undo"   plain="true"  onclick='stopTasks("<%=basePath%>console/quartz/stopTasks.action","任务停止成功","请选择要停止的任务")'>
             停止任务
	         </a> 
	         <a href="javascript:void(0)"  class="easyui-linkbutton "  iconCls="icon-back"   plain="true"  onclick="resumeTasks()">
             恢复任务
	         </a> 
	         <a href="javascript:void(0)"   class="easyui-linkbutton "  iconCls="icon-no"   plain="true"  onclick="pauseTasks()">
            暂停任务
	         </a> 
	         <a href="javascript:void(0)"  class="easyui-linkbutton"   iconCls="icon-lock"   plain="true"  onclick="triggerTasks()">
            立即执行一次
	         </a> 
			</div>
	</div>
<jsp:include page="./add.jsp"></jsp:include>

</body>
</html>