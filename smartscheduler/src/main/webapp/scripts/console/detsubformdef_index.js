$(function() {
    var ctxPath=$("#ctxPath").val();
	var nspath=ctxPath+"/console/detsubform";  //命名空间路径
    var datAppId=$("#datAppId").val();

    $("#grid1").datagrid({
		url:nspath+"/getforms/"+datAppId+".action?r1="+Math.random(),
        //queryParams: {
        //    datappid: datAppId
        //},
		toolbar:"#toolbar",
		pagination:"true",
		pageSize:"50",
		rownumbers:"true",
		method:"get",
		fitColumns:"true",
		fit:"true",
		onDblClickRow: function (rowIndex, rowData) {
			var appid=$("#datAppId").val();
		//$("#iframe1").attr("src", nspath+"/modify/"+appid+"/"+rowData.subformId+".xsp?r1="+Math.random());
		//$("#dlgFormDef").window("open").window("setTitle", "编辑子表单"); 
			window.open(nspath+"/modify/"+appid+"/"+rowData.subformId+".xsp?r1="+Math.random());   
		}
	});

    //新建子表单
	$("#btnnew").click(function(evt) {
		var appid=$("#datAppId").val();
		//$("#iframe1").attr("src",);
    	//$("#dlgFormDef").window("open").window("setTitle", "新建子表单"); 
		window.open(nspath+"/create/"+appid+".xsp?r1="+Math.random());    
    });
	
	
	//子表单导出zip包数据
	$("#subFormExport").click(function(evt) {
	   var selRows = $('#grid1').datagrid('getChecked');  
		if (selRows.length>0) {
			 var formIds=[];
			  $(selRows).each(function(index, item) {
				 formIds[index]=item.subformId;
			 });
			  var form=$("<form>");//定义一个form表单
			  form.attr("style","display:none");
			  form.attr("target","");
			  form.attr("method","post");
			  form.attr("action",nspath+"/export.action");
			  var input1=$("<input>");
			  input1.attr("type","hidden");
			  input1.attr("name","subformId");
			  input1.attr("value",JSON.stringify(formIds));
			  $("body").append(form);//将表单放置在web中
			  form.append(input1);
			  form.submit();//表单提交 
		}else{
			 $.messager.show({
                 title: '温馨提示',
                 msg: '请选择要导出的子表单'
             });
		}
		});
	//删除子表单
	$("#btndel").click(function(evt) {
		var appid=$("#datAppId").val();
		var selRows = $('#grid1').datagrid('getChecked');  
		if (selRows.length>0) {
			var formIds=[];
			$(selRows).each(function(index, item) {
				formIds[index]=item.subformId;
			});
			$.messager.confirm('提示', "是否删除选中的 "+selRows.length+" 个子表单？", function(r){
				if (r){
					$.post(nspath+"/deleteform/"+appid+".action", {formIds:formIds}, function(data) {
						if (data.success) {
							alert("删除子表单成功！");
							$("#grid1").datagrid("load",{r2:Math.random(), datappid:datAppId});
						} else {
							alert("删除子表单失败！");
						}
					});
				}
			});
		}
	});
});
