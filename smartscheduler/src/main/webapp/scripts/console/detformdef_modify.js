/**
 * 修改表单页面脚本
 */
    var formContentEditor="";
$(function() {
	$(window).resize(function(){
		/*$('#tabpanel').tabs({ 
			width: $("#tabpanel").parent().width(), 
			height: "auto" 
			});*/
		/*$('#tabpanel').tabs('resize',{ 
			height: $("#tabpanel").parent().height()
			});*/
		
	});
	
    var postUrl="";
    var ctxPath=$("#ctxPath").val();
    var nsFormPath=ctxPath+"/console/detform";  //表单定义命名空间路径
    var nsFieldPath=ctxPath+"/console/detformfield";  //表单字段命名空间路径
    var datAppId=$("#datAppId").val();
    var formId=$("#formId").val();
  /*  var mixedMode = {
            name: "htmlmixed",
            scriptTypes: [
                          {matches: /\/x-handlebars-template|\/x-mustache/i,
                           mode: null},
                          {
                        	   matches: /(text|application)\/(x-)?vb(a|script)/i,
                                mode: "vbscript"}
                           ]
          };*/
          formContentEditor = CodeMirror.fromTextArea(document.getElementById("formContentEditor"), {
            //mode: mixedMode,
        	    mode: "text/html",
        	    width: '100%',
                height: '100%',
        	  lineNumbers: true
            /*selectionPointer: true*/
          });
    //初始化编辑器
/*    var formContentEditor = ace.edit("formContentEditor");
	formContentEditor.setTheme("ace/theme/twilight");
	formContentEditor.session.setMode("ace/mode/html");
    formContentEditor.setValue($("#hidFormContent").text());
*/
    formContentEditor.setValue($("#hidFormContent").text());
    $("#fieldGrid").datagrid({
        url:nsFieldPath+"/getfields/"+datAppId+"/"+formId+".action?r1="+Math.random(),
		toolbar:"#toolbar",
        singleSelect: true,
		rownumbers:"true",
		method:"get",
		fitColumns:"true",
		fit:"true",
        onDblClickRow: function (rowIndex, rowData) {
			postUrl=nsFieldPath+"/updatefield/"+datAppId+"/"+formId+".action?r1="+Math.random();
			$("#dlgField").dialog("open").dialog("setTitle", "编辑字段");
			$('#form2').form('load', rowData);
            if (rowData.multiValue=="yes") {
                $("#multiSeparator").combobox("enable");
            } else {
                $("#multiSeparator").combobox("disable");
            }            
		}
	});

    //点击“关闭”
	$("#btnclose").click(function() {
       // window.parent.$("#dlgFormDef").window("close");
		window.close();
    });

    //点击“保存”
    $("#btnsave").click(function() {
        var formName=$("#formName").val();
        if ($.trim(formName)=="") {
            alert("请输入表单名称！");
            $("#tabpanel").tabs("select",3);
            $('#formName').next('span').find('input').focus();
        } else if ($("#form1").form('validate')) {
            var formDef={};
            var datAppId=$("#datAppId").val();
            formDef.formId=$("#formId").val();
            formDef["datApplication.appId"]=datAppId;
            formDef.formName=$("#formName").textbox('getValue');
            formDef.formTitle=$("#formTitle").textbox('getValue');
            formDef.formType=$("#formType").combobox('getValue');
            formDef.description=$("#description").textbox('getValue');
            //var bodyContent=UE.getEditor('bodyEditor').getContent();
            //formDef.formContent=$("#formContent").val();
           // var formContentEditor=ace.edit("formContentEditor");
            formDef.formContent=formContentEditor.getValue();
            $('input[name="aloneForm"]:checked').each(function(){ 
                formDef.aloneForm=$(this).val(); 
               }); 
            formDef.htmlHead=$("#htmlHead").val();
            formDef.importPack=$("#importPack").val();
            formDef.refScript=$("#refScript").val();
            formDef.queryOpenEventCls=$("#queryOpenEventCls").textbox('getValue');
            formDef.querySaveEventCls=$("#querySaveEventCls").textbox('getValue');
            formDef.querySaveEventAfterCls=$("#querySaveEventAfterCls").textbox('getValue');
            $.post(nsFormPath+"/updateform/"+datAppId+".action", formDef, function(data) {
                if (data.success) {
                   alert("表单保存成功！");
                   // window.parent.$("#grid1").datagrid("load", {r2:Math.random()});
                   // window.parent.$("#dlgFormDef").window("close");
                    //if(window.confirm('表单保存成功,是否要关闭当前窗口？')){
                    	//try{
                    	// window.opener.$("#grid1").datagrid("load", {r2:Math.random()});
                     //    }catch(e){}
                     //	window.close();
                        //alert("确定");
                       // return true;
                    // }else{
                    	// try{
                    	// window.opener.$("#grid1").datagrid("load", {r2:Math.random()});
                    	// }catch(e){}
                        //alert("取消");
                       // return false;
                  //  }
                   
                } else {
                    alert("表单保存失败！原因："+data.msg);
                }
            });
        }
    });

    //新建字段
    $("#btnnew").click(function(evt) {
        postUrl=nsFieldPath+"/createfield/"+datAppId+"/"+formId+".action?r1="+Math.random();
		$('#form2').form('clear');
    	$("#dlgField").window("open").window("setTitle", "新建字段"); 
        //设置字段缺省类型为文本类型
        $("#fieldType").combobox("select", "text");
        $("#multiSeparator").combobox("disable");
        $("#roleName").textbox('setValue',"");
    	$("#roleNum").val("");
    });

    //字段对话框“取消”按钮点击事件
	$("#dlg_btncancel").click(function(evt) {
		$('#dlgField').dialog('close');
	});

    //字段对话框“保存”按钮点击事件
	$("#dlg_btnsave").click(function(evt) {
		var paramObj={};
		var values=$("#form2").serializeArray();
		//将数组转换为对象
		$(values).each(function(){  
			paramObj[this.name]=this.value;  
        }); 
        paramObj["detFormDefine.formId"]=formId;
		//表单验证成功后提交请求
		if ($("#form2").form('validate')) {
			$.post(postUrl, paramObj, function(data) {
				if (data.success) {
					$("#dlgField").dialog("close");
					$("#fieldGrid").datagrid("load", {r2:Math.random()});
				} else {
					alert("字段保存失败，原因："+data.msg);
				}
			});
		}
	});

    //是否允许编辑多值分隔符文本框
    $("#multiValue").change(function(evt) {
        var checked=$(this).prop("checked");
        if (!checked) {
            $("#multiSeparator").combobox("disable");
        } else {
            $("#multiSeparator").combobox("enable");
        }
    });

    $("#fieldType").combobox({
        //字段类型下拉框change事件
        onChange: function(newValue, oldValue){
            //判断是否字符串类型，若是字符串类型，必须指定长度，其他类型长度为0
            if (newValue=="text") {
                $("#fieldLenRow").show();
                $("#fieldLength").numberbox("setValue",255);
            } else {
                $("#fieldLenRow").hide();
                $("#fieldLength").numberbox("setValue",0);
            }
        }
    });
});

//表单字段删除链接
function gridDeleteLinkFmt(val,row) {
    var ctxPath=$("#ctxPath").val();
    var html="<a href='javascript:void(0)' name='deleteFieldLink' onclick=\"deleteField('"+row.fieldId+"')\">"+
        "<img src='"+ctxPath+"/images/exticons/cross.png' alt='删除' /></a>";
    return html;
}

function deleteField(fieldId) {
    var datAppId=$("#datAppId").val();
    var formId=$("#formId").val();
    var fieldIds=[fieldId];
    var ctxPath=$("#ctxPath").val();
    var nspath=ctxPath+"/console/detformfield";  //命名空间路径
    var rows=$("#fieldGrid").datagrid("getRows");
    if (rows) {
        $.messager.confirm('提示', "若为输出字段，独立表单相应的列及数据都会删除，是否继续执行删除？", function(r){
            if (r) {
                $.post(nspath+"/deletefield/"+datAppId+"/"+formId+".action", {fieldIds:fieldIds}, function(data) {
                    if (data.success) {
                        $("#fieldGrid").datagrid("load", {r2:Math.random()});
                    } else {
                        alert("删除字段失败！");
                    }
                });
            }
        });
    }
}


