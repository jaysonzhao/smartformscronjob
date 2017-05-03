

var theAutoCreateDialog;

//打开选流程对话框
function openWorkflowList(){
	var autoDestroy = true;
	$('#iframe_processmeta').attr("src",$("#basePath").val()+"/workflow/bpm/AuthorizationRule/process.xsp?authorizationRuleId="+$("#authorizationRuleId").val());  
	parent.openWorkFlow();
}
//数据格式化
function getNowFormatDate(year) {
    var date = new Date();
    var seperator1 = "-";
    var seperator2 = ":";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate =""
    if(""!=year&&year!=null&&year!=undefined){
    	currentdate = date.getFullYear()+year + seperator1 + month + seperator1 + strDate ;
    }else{
    	currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate ;
    }
    return currentdate;
} 

//打开选择公司对话框
function openCompany(){
	parent.openCompany();
}
//打开选择部门对话框
function openDepartment(){
	parent.openDepartment();
}
//打开高级授权对话框
function openAdvanceDlg(){
	var selectedWorkflowIds=$("#selectedWorkflowIds").val();
	var ruleId=$("#ruleId").val();
	if(selectedWorkflowIds==""){
		$.messager.show({ 
			title : '温馨提示',
			msg :'请选选择要授权的流程'
		});
	}else{
		parent.openAdvanceDlg(selectedWorkflowIds,ruleId);
	}
}
$(function() {
	$("#saveAuthorizationRule").click(function() {
		saveAuthorizationRule();
	});
	
	$("#effectTime").val(getNowFormatDate());
	$("#invalidTime").val(getNowFormatDate(1));
	
	$("[name='ruleType']").change(function(){
		var selected = $("input[name='ruleType']:checked").val();
		if("onTime"==selected){
			$("[id^='effectiveTime'").css("display","");
			$("#effectTime").val($("#beforeEffectTime").val());
			$("#invalidTime").val($("#beforeInvalidTime").val());
		}else{
			
			$("[id^='effectiveTime'").css("display","none");
			$("#effectTime").val(getNowFormatDate());
			$("#invalidTime").val(getNowFormatDate(1));
		}
	});
	$("[name='ruleMode']").change(function(){
		var selected = $("input[name='ruleMode']:checked").val();
		if(selected=="advanced"){
			$("[id^='advancedTr'").css("display","");
		}else{
			$("[id^='advancedTr'").css("display","none");
		}
	});
	
});