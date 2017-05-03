$(function() {
	//在页面加载后，判断是否保存成功
	var saveResult=$("#saveResult").val();
	if (saveResult=="true") {
		$.messager.alert({ 
			title : '提示',
			msg : "网关环节配置更新成功！",
			icon:"question"
		});
	} else if (saveResult=="false") {
		$.messager.alert({ 
			title : '提示',
			msg : "网关环节配置更新失败！原因："+$("#saveResultMsg").val(),
			icon:"error"
		});
	}
	
	//点选网关列表项
	$("#gatewayList td").click(function(evt) {
		$("#gatewayList td").removeClass("selected");
		$(this).addClass("selected");
		var index=$(this).parent().index();
		$("#gatewayDetails .conditionTable").hide();
		$("#gatewayDetails .conditionTable:eq("+index+")").show();
	});
	
	//保存网关环节配置信息
	$("#btnSave").click(function(evt) {
		var params=[];
		var gatewayCount=$("#metasSize").val();
		gatewayCount=(gatewayCount=="") ? 0 : gatewayCount;
		for (var i=0; i<gatewayCount; i++) {
			var meta={};
			var activityName=$("#activityName_"+i).val();
			meta.activityId=$("#activityId_"+i).val();
			meta.sortNum=$("#sortNum_"+i).val();
			//获取网关中已选中的路由变量名称
			var gatewayRouteVarName=[];
			$(":checkbox[name=routeVarName_"+i+"]:checked").each(function() {
				gatewayRouteVarName.push($(this).val());
			});
			meta.routeVarName=gatewayRouteVarName;
			//将路由条件表格中的数据转换为JSON数据
			meta.routeConditons=[];
			$("#conditionGrid_"+i+" tbody tr").each(function(index) {
				var tdcells=$(this).children();
				var routeVarName=$(tdcells[0]).text();
				var fieldName=$(tdcells[1]).text();
				var compareOptr=$(tdcells[2]).text();
				var fieldVal=$(tdcells[3]).text();
				var fieldValType=$(tdcells[4]).text();
				var conditionOptr=$(tdcells[5]).text();
				var priority=$(tdcells[6]).text();
				var rowCond={};
				rowCond.routeVarName=routeVarName;
				rowCond.fieldName=fieldName;
				rowCond.compareOptr=compareOptr;
				rowCond.fieldVal=fieldVal;
				rowCond.fieldValType=fieldValType;
				rowCond.conditionOptr=conditionOptr;
				rowCond.priority=priority;
				meta.routeConditons.push(rowCond);
			});
			params.push(meta);
		}
		if (params.length>0) {
			var jdata=JSON.stringify(params);
			$("#params").val(jdata);
		}
		$("#form1").submit();
	});
	
	$(":checkbox[name^='routeVarName_']").click(function() {
		var chk=$(this).prop("checked");
		if (!chk) {
			var ctrlName=$(this).attr("name");
			var ctrlIndex=ctrlName.substring(ctrlName.lastIndexOf("_")+1);
			var ctrlVal=$(this).val();
			$("#conditionGrid_"+ctrlIndex+" tbody tr").each(function() {
				var tdcell=$(this).children("td:first");
				if (tdcell.text()==ctrlVal) {
					tdcell.text("");
				}
			});
		}
	});
});

//打开新建路由条件对话框
/**
 * 打开新建路由条件对话框
 * @param index 表格序号
 * @param rowIndex 条件行序号
 */
function openConditionWnd(index, sender) {
	var dlg = layer.open({
		title : '路由条件信息',
		type : 1,
		skin : 'layui-layer-rim', // 加上边框
		area : [ '700px', '450px' ], // 宽高
		content : $("#dlgCondition_"+index),
		btn : [ '保存', '取消' ],
		yes : function(idx, layero) {
			if (saveConditionRow(index, sender)) {
				layer.close(dlg);
			}
		},
		btn2 : function(index, layero) {
		}
	});
	
	//获取选中的路由变量名称，然后填充到对话框的下拉选择框中
	$("#selRouteVarName_"+index).empty();
	$(":checkbox[name=routeVarName_"+index+"]:checked").each(function() {
		var optval=$(this).val();
		var option="<option value='"+optval+"'>"+optval+"</option>";
		$("#selRouteVarName_"+index).append(option);
	});
	
	//判断是否打开编辑，若是的话就给窗口中的控件赋值
	if (sender) {
		//编辑赋值
		var tdcell=$(sender).children();
		var routeVarName=$(tdcell[0]).text();
		var fieldName=$(tdcell[1]).text();
		var compareOptr=$(tdcell[2]).text();
		var fieldVal=$(tdcell[3]).text();
		var fieldValType=$(tdcell[4]).text();
		var conditionOptr=$(tdcell[5]).text();
		var priority=$(tdcell[6]).text();
		$("#selRouteVarName_"+index).val(routeVarName);
		$("#leftValue_"+index).val(fieldName);
		$("#compareOptr_"+index).val(compareOptr);
		$("#rightValue_"+index).val(fieldVal);
		$("#rightValueType_"+index).val(fieldValType);
		$("#conditionOptr_"+index).val(conditionOptr);
		$("#priority_"+index).val(priority);
	} else {
		//新建初始化控件
		$("#selRouteVarName_"+index+" :first").prop("selected",true);
		$("#leftValue_"+index+" :first").prop("selected",true);
		$("#compareOptr_"+index+" :first").prop("selected",true);
		$("#rightValue_"+index).val("");
		$("#rightValueType_"+index+" :first").prop("selected",true);
		$("#conditionOptr_"+index+" :first").prop("selected",true);
		$("#priority_"+index).val("");
	}
}

//删除路由条件对话框行
function deleteConditionRow(index) {
	$("#conditionGrid_"+index+" tbody td.selected").parent().remove();
}

//将路由条件对话框的信息保存的表格行中
function saveConditionRow(index, sender) {
	var selRouteVarName=$("#selRouteVarName_"+index).val();
	selRouteVarName=(selRouteVarName==null) ? "" : selRouteVarName;
	var leftValue=$("#leftValue_"+index).val();
	var compareOptr=$("#compareOptr_"+index).val();
	var rightValue=$("#rightValue_"+index).val();
	var rightValueType=$("#rightValueType_"+index).val();
	if (rightValueType=="int") {
		var ival=parseInt(rightValue);
		if (isNaN(ival)) {
			alert("请输入正确的数字格式！");
			return false;
		} else {
			rightValue=ival;
		}
	}
	var conditionOptr=$("#conditionOptr_"+index).val();
	var priority=$("#priority_"+index).val();
	if (!sender) {
		//新建
		var trrow="<tr ondblclick=\"openConditionWnd("+index+", this)\""+
			" onclick=\"selectConditionRow("+index+", this)\">";
		trrow+="<td>"+selRouteVarName+"</td>";
		trrow+="<td>"+leftValue+"</td>";
		trrow+="<td>"+compareOptr+"</td>";
		trrow+="<td>"+rightValue+"</td>";
		trrow+="<td>"+rightValueType+"</td>";
		trrow+="<td>"+conditionOptr+"</td>";
		trrow+="<td>"+priority+"</td>";
		trrow+="</tr>";
		$("#conditionGrid_"+index+" tbody").append(trrow);
	} else {
		//编辑
		var tdcell=$(sender).children();
		$(tdcell[0]).text(selRouteVarName);
		$(tdcell[1]).text(leftValue);
		$(tdcell[2]).text(compareOptr);
		$(tdcell[3]).text(rightValue);
		$(tdcell[4]).text(rightValueType);
		$(tdcell[5]).text(conditionOptr);
		$(tdcell[6]).text(priority);
	}
	return true;
}

//单击选中路由条件
function selectConditionRow(index, sender) {
	//单击选中路由条件表格中的行数据
	$(sender).siblings().children().removeClass("selected");
	$(sender).children().addClass("selected");
}