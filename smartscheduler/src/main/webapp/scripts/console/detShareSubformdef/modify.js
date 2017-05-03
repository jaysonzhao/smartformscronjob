/**
 * 修改子表单页面脚本
 */
var formContentEditor="";
$(function() {
    var postUrl="";
    var ctxPath=$("#ctxPath").val();
    var nsSubformPath=ctxPath+"/console/detShareSubform";  //子表单定义命名空间路径
    var datAppId=$("#datAppId").val();
    var formId=$("#subformId").val();
    var mixedMode = {
            name: "htmlmixed",
            scriptTypes: [{matches: /\/x-handlebars-template|\/x-mustache/i,
                           mode: null},
                          {matches: /(text|application)\/(x-)?vb(a|script)/i,
                           mode: "vbscript"}]
          };
          formContentEditor = CodeMirror.fromTextArea(document.getElementById("formContentEditor"), {
            mode: mixedMode,
            lineNumbers: true,
            selectionPointer: true
          });
    formContentEditor.setValue($("#hidFormContent").text());

    //点击“关闭”
	$("#btnclose").click(function() {
        window.parent.$("#dlgFormDef").window("close");
    });

    //点击“保存”
    $("#btnsave").click(function() {
        var subformName=$("#subformName").val();
        if ($.trim(subformName)=="") {
            alert("请输入子表单名称！");
            $("#tabpanel").tabs("select",3);
            $('#subformName').next('span').find('input').focus();
        } else if ($("#form1").form('validate')) {
            var formDef={};
            formDef.subformId=$("#subformId").val();
            formDef.subformName=$("#subformName").textbox('getValue');
            formDef.description=$("#description").textbox('getValue');
            formDef.subformContent=formContentEditor.getValue();
            formDef.htmlHead=$("#htmlHead").val();
            formDef.importPack=$("#importPack").val();
            formDef.refScript=$("#refScript").val();
            $.post(nsSubformPath+"/updateform.action", formDef, function(data) {
                if (data.success) {
                    alert("子表单保存成功！");
                    window.parent.$("#grid1").datagrid("load", {r2:Math.random()});
                } else {
                    alert("子表单保存失败！原因："+data.msg);
                }
            });
        }
    });
});

