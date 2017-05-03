/**
 * 创建子表单页面脚本
 */
var formContentEditor="";
$(function() {
    var ctxPath=$("#ctxPath").val();
    var nspath=ctxPath+"/console/detsubform";  //命名空间路径
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
            var datAppId=$("#datAppId").val();
            formDef["datApplication.appId"]=datAppId;
            formDef.subformName=$("#subformName").textbox('getValue');
            formDef.description=$("#description").textbox('getValue');
            formDef.subformContent=formContentEditor.getValue();
            formDef.htmlHead=$("#htmlHead").val();
            formDef.importPack=$("#importPack").val();
            formDef.refScript=$("#refScript").val();
            $.post(nspath+"/createform/"+datAppId+".action", formDef, function(data) {
                if (data.success) {
                    alert("子表单保存成功！");
                    window.parent.$("#grid1").datagrid("load", {r2:Math.random()});
                    window.parent.$("#dlgFormDef").window("close");
                } else {
                    alert("子表单保存失败！原因："+data.msg);
                }
            });
        }
    });
});