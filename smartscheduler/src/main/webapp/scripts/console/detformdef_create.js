/**
 * 创建表单页面脚本
 */
var formContentEditor="";
$(function() {
    var ctxPath=$("#ctxPath").val();
    var nspath=ctxPath+"/console/detform";  //命名空间路径
   /* var mixedMode = {
            name: "htmlmixed",
            scriptTypes: [{matches: /\/x-handlebars-template|\/x-mustache/i,
                           mode: null},
                          {matches: /(text|application)\/(x-)?vb(a|script)/i,
                           mode: "vbscript"}]
          };*/
          formContentEditor = CodeMirror.fromTextArea(document.getElementById("formContentEditor"), {
        	  mode: "text/html",
            lineNumbers: true,
            width: '100%',
            height: '615'
          });
    //初始化编辑器
   // var formContentEditor = ace.edit("formContentEditor");
	//formContentEditor.setTheme("ace/theme/twilight");
	//formContentEditor.session.setMode("ace/mode/html");
    // var bodyEditor = UE.getEditor('bodyEditor',{
    //     toolbars: [
    //         ['fullscreen']
    //     ],
    //     enterTag:'',
    //     textarea:'bodyContent',
    //     initialContent:"<div>表单内容</div>"
    // });
    /*
    bodyEditor.addListener("ready", function () {
        bodyEditor.setContent("<div>表单内容</div>");

    });
    */

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
            formDef["datApplication.appId"]=datAppId;
            formDef.formName=$("#formName").textbox('getValue');
            formDef.formTitle=$("#formTitle").textbox('getValue');
            formDef.formType=$("#formType").combobox('getValue');
            formDef.description=$("#description").textbox('getValue');
            //var bodyContent=UE.getEditor('bodyEditor').getContent();
            //formDef.formContent=$("#formContent").val();
           // var formContentEditor=ace.edit("formContentEditor");
            formDef.formContent=formContentEditor.getValue();
            formDef.htmlHead=$("#htmlHead").val();
            formDef.importPack=$("#importPack").val();
            formDef.refScript=$("#refScript").val();
            $('input[name="aloneForm"]:checked').each(function(){ 
             formDef.aloneForm=$(this).val(); 
            }); 
           
            formDef.queryOpenEventCls=$("#queryOpenEventCls").textbox('getValue');
            formDef.querySaveEventCls=$("#querySaveEventCls").textbox('getValue');
            formDef.querySaveEventAfterCls=$("#querySaveEventAfterCls").textbox('getValue');
            $.post(nspath+"/createform/"+datAppId+".action", formDef, function(data) {
                if (data.success) {
                    alert("表单保存成功！");
                    try{
                	window.opener.$("#grid1").datagrid("load", {r2:Math.random()});
                      }catch(e){}
                    	window.close();
                } else {
                    alert("表单保存失败！原因："+data.msg);
                }
            });
        }
    });
});