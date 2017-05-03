$(function() {
    //初始化数据表格
    var sdate=$("#sdate").datebox('getValue');
    var edate=$("#edate").datebox('getValue');
    var optrType=$("#optrtype").combobox('getValue');
    $("#grid1").datagrid({
        url:"getAllOptrLog.action",
        rownumbers:true,
        singleSelect:true,
		method:'get',
        toolbar:'#toolbar1',
        fit:true,
        loadMsg:'正在加载日志，请稍候',
        pagination:true,
        pageSize:50,
        queryParams:{
            sdate:sdate,
            edate:edate,
            optrtype:optrType
        } 
    });
    
    /**
     * 检查查询条件是否符合要求
     */
    var checkParams=function(sdate, edate, optrType) {
        if (sdate=="") {
            alert("请选择查询开始日期！");
            return false;
        }
        if (edate=="") {
            alert("请选择查询结束日期！");
            return false;
        }
        //比较日期大小，结束日期不能早于开始日期
        var date1=new Date(sdate);
        var date2=new Date(edate);
        if (date1>date2) {
            alert("结束日期不能早于开始日期！");
            return false;
        }
        if (optrType=="") {
            alert("请选择日志类型！");
            return false;
        }
        return true;
    };
    
	$("#btnsearch").click(function(evt) {
        var sdate=$("#sdate").datebox('getValue');
        var edate=$("#edate").datebox('getValue');
        var optrType=$("#optrtype").combobox('getValue');
        if (checkParams(sdate, edate, optrType)) {
            $("#grid1").datagrid("load",{
                sdate:sdate,
                edate:edate,
                optrtype:optrType
            });
        }
    });
    
    $("#btnclear").click(function(evt) {
        var sdate=$("#sdate").datebox('getValue');
        var edate=$("#edate").datebox('getValue');
        var optrType=$("#optrtype").combobox('getValue');
        if (checkParams(sdate, edate, optrType)) {
            $.messager.confirm('提示', "是否清除在 "+sdate+" 与 "+edate+" 之间的日志？", function(r){
				if (r){
					$.post("deleteLog.action", {
                        sdate:sdate,
                        edate:edate,
                        rnum1:Math.random()
                    },function(data) {
                        if (data.success) {
                            $("#grid1").datagrid("load",{
                                sdate:sdate,
                                edate:edate,
                                optrtype:optrType
                            });
                        } else {
                            alert(data.msg);
                        }
                    });
				}
			});
        }
    });
});

function gridLogTypeFmt(val,row) {
    var text="";
    switch (val) {
        case "appacl":
            text="应用库权限";
            break;
        case "appact":
            text="应用库";
            break;
        case "docact":
            text="文档修改";
            break;
        case "docacl":
            text="文档权限";
            break;
        case "orgact":
            text="组织库";
            break;
        case "formact":
            text="表单修改";
            break;
        case "loginact":
            text="系统登录";
            break;
        case "bpmrouting":
            text="流程路由";
            break;
        case "bpminstance":
            text="流程实例";
            break;
        default:
            text=val;
            break;
    }
    return text;
}

