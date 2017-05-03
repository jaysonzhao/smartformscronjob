$(function(){
    var postUrl="";
	var ctxPath=$("#ctxPath").val();
    var nsColumnPath=ctxPath+"/console/detviewcol";  //命名空间路径
    var nsViewPath=ctxPath+"/console/detview";  //命名空间路径
    var datAppId=$("#datAppId").val();
    var viewId=$("#viewId").val();

    $("#columnGrid").datagrid({
        url:nsColumnPath+"/getcolumns/"+datAppId+"/"+viewId+".action?r1="+Math.random(),
		toolbar:"#toolbar",
        singleSelect: true,
		rownumbers:"true",
		method:"get",
		fitColumns:"true",
		fit:"true",
        onDblClickRow: function (rowIndex, rowData) {
			postUrl=nsColumnPath+"/updatecolumn/"+datAppId+"/"+viewId+".action?r1="+Math.random();
			$("#dlgColumn").dialog("open").dialog("setTitle", "编辑视图列");
			$('#form2').form('load', rowData);
		}
	});

    //点击“关闭”
	$("#btnclose").click(function() {
        window.parent.$("#dlgEditView").window("close");
    });

    //新建视图列
    $("#btnnew").click(function(evt) {
        postUrl=nsColumnPath+"/createcolumn/"+datAppId+"/"+viewId+".action?r1="+Math.random();
		$('#form2').form('clear');
    	$("#dlgColumn").window("open").window("setTitle", "新建视图列"); 
    });
	
    //点击“保存”，保存视图
    $("#btnsave").click(function() {
        var viewName=$("#viewName").val();
        if ($.trim(viewName)=="") {
            alert("请输入视图名称！");
            $("#tabpanel").tabs("select",1);
            $('#viewName').next('span').find('input').focus();
        } else {
            var detView={};
            detView.viewId=viewId;
            detView["datApplication.appId"]=datAppId;
            detView.viewName=$("#viewName").textbox('getValue');
            detView.viewTitle=$("#viewTitle").textbox('getValue');
            detView.description=$("#description").textbox('getValue');
            detView.queryFilter=$("#queryFilter").val();
            detView.viewHead=$("#viewHead").val();
            detView.pageSize=$("#pageSize").textbox('getValue');
            $.post(nsViewPath+"/updatedetview/"+datAppId+".action", detView, function(data) {
                if (data.success) {
                    alert("视图保存成功！");
                    window.parent.$("#grid1").datagrid("load", {r2:Math.random()});
                    window.parent.$("#dlgEditView").window("close");
                } else {
                    alert("视图保存失败！");
                }
            });
        }
    });

    //数据列对话框“保存”按钮点击事件
	$("#dlg_btnsave").click(function(evt) {
		var paramObj={};
		var values=$("#form2").serializeArray();
		//将数组转换为对象
		$(values).each(function(){  
			paramObj[this.name]=this.value;  
        }); 
        paramObj["detDataView.viewId"]=$("#viewId").val();
		//表单验证成功后提交请求
		if ($("#form2").form('validate')) {
			$.post(postUrl, paramObj, function(data) {
				if (data.success) {
					$("#dlgColumn").dialog("close");
					$("#columnGrid").datagrid("load", {r2:Math.random()});
				} else {
					alert("视图列保存失败，原因："+data.msg);
				}
			});
		}
	});
	
	//数据列对话框“取消”按钮点击事件
	$("#dlg_btncancel").click(function(evt) {
		$('#dlgColumn').dialog('close');
	});
});

//视图数据列删除链接
function gridDeleteLinkFmt(val,row) {
    var ctxPath=$("#ctxPath").val();
    var html="<a href='javascript:void(0)' name='deleteColumnLink' onclick=\"deleteColumn('"+row.columnId+"')\">"+
        "<img src='"+ctxPath+"/images/exticons/cross.png' alt='删除' /></a>";
    return html;
}

function deleteColumn(columnId) {
    var datAppId=$("#datAppId").val();
    var viewId=$("#viewId").val();
    var columnIds=[columnId];
    var ctxPath=$("#ctxPath").val();
    var nspath=ctxPath+"/console/detviewcol";  //命名空间路径
    var rows=$("#columnGrid").datagrid("getRows");
    if (rows && rows.length>=2) {
        $.messager.confirm('提示', "是否删除视图列？", function(r){
            if (r) {
                $.post(nspath+"/deletecolumn/"+datAppId+"/"+viewId+".action", {columnIds:columnIds}, function(data) {
                    if (data.success) {
                        $("#columnGrid").datagrid("load", {r2:Math.random()});
                    } else {
                        alert("删除视图列失败！");
                    }
                });
            }
        });
    } else {
        alert("数据视图最少具备一个视图列！");
    }
}