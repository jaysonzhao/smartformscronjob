function openContentTemplateDlg() {
	$('#contentTemplate_dlg').dialog('open').dialog('setTitle', '添加模板');
	$('#fm').form('clear');
	$("#creator").text("");
	$("#updateBy").text("");
	$("#createTime").text("");
	$("#updateTime").text("");
}

function createOrUpdate() {
	var values = $("#fm").serialize();
	if ($("#fm").form('validate')) {
		$("#contentTemplate_dlg_btnsave").linkbutton("disable");
		$.ajax({
			url : $("#basePath").val()
					+ "/console/contentTemplate/createOrUpdate.action",
			type : "post",
			data : values,
			dataType : "json",
			success : function(result) {
				$("#contentTemplate_dlg_btnsave").linkbutton("enable");
				if (result.state == false) {
					$.messager.show({
						title : '温馨提示',
						msg : result.msg
					});
				} else {
					$('#datagrid').datagrid('reload');
					$('#contentTemplate_dlg').dialog('close');
				}
			},
			error : function() {
				$("#contentTemplate_dlg_btnsave").linkbutton("enable");
			}
		});
	}
}

function datagrid(ctplName, ctplCode) {
	$('#datagrid').datagrid(
			{
				height : '95%',
				fit : true,
				url : $("#basePath").val()
						+ '/console/contentTemplate/findByPage.action',
				method : 'POST',
				queryParams : {
					"ctplName" : ctplName,
					"ctplCode" : ctplCode
				},
				striped : true,
				nowrap : true,
				pageSize : 10,
				pageNumber : 1,
				pageList : [ 10,20,30,40,50 ],
				showFooter : true,
				loadMsg : '数据加载中请稍候……',
				pagination : true,
				toolbar : "#toolbar",
				singleSelect : false,
				checkOnSelect : false,
				selectOnCheck : false,
				columns : [ [ {
					field : 'ck',
					checkbox : true
				}, {
					field : 'ctplName',
					title : '模板名称',
					width : 250,
					align : 'center'
				}, {
					field : 'ctplCode',
					title : '模板编号',
					width : 300,
					align : 'center'
				}, {
					field : 'title',
					title : '标题',
					width : 300,
					align : 'center'
				}, {
					field : 'description',
					title : '描述',
					width : 150,
					align : 'center'
				}, {
					field : 'creator',
					title : '创建者',
					width : 100,
					align : 'center'
				}, {
					field : 'createTime',
					title : '创建时间',
					width : 200,
					align : 'center',
					formatter : function(value, rec, index) {
						var s = new Date(value).pattern("yyyy-MM-dd hh:mm:ss");
						return s;
					}
				}, {
					field : 'updateBy',
					title : '更新者',
					width : 100,
					align : 'center'
				}, {
					field : 'updateTime',
					title : '更新时间',
					width : 200,
					align : 'center',
					formatter : function(value, rec, index) {
						var s = new Date(value).pattern("yyyy-MM-dd hh:mm:ss");
						return s;
					}
				} ] ],
				onSelect : function(rowData) {
					$('#datagrid').datagrid("unselectAll");
				},
				onDblClickRow : function(rowIndex, rowData) {
					if (rowData) {
						$('#contentTemplate_dlg').dialog('open').dialog('setTitle', '修改模板');
						$('#fm').form('clear');
						$('#fm').form('load', rowData);
						$("#createTime").text(timestampFormat(rowData.createTime));
						$("#updateTime").text(timestampFormat(rowData.updateTime));
						$("#creator").text(rowData.creator);
						$("#updateBy").text(rowData.updateBy);
					}
				}
			});
}

function searchByName() {
	datagrid($("#name").val(), $("#ctplCode").val());
}

function destroy() {
	var rows = $('#datagrid').datagrid('getChecked');
	if (rows.length > 0) {
		$.messager.confirm('温馨提示', '你真的要删除么?', function(r) {
			if (r) {
				var jsonData = {
					ctplIds : []
				};
				for (var i = 0; i < rows.length; i++) {
					jsonData.ctplIds.push(rows[i].ctplId);
				}
				$.ajax({
					url : $("#basePath").val()
							+ "/console/contentTemplate/delete.action",
					type : "post",
					data : jsonData,
					dataType : "json",
					success : function(result) {
						if (result.state == true) {
							$('#datagrid').datagrid('reload');
						} else {
							$.messager.show({ // show error message
								title : '温馨提示',
								msg : result.msg
							});

						}
					}
				});
			}
		});
	}
}

/** 获取光标位置插件 */
(function($) {
	$.fn.extend({
		insertAtCaret : function(myValue) {
			var $t = $(this)[0];
			// IE
			if (document.selection) {
				this.focus();
				sel = document.selection.createRange();
				sel.text = myValue;
				this.focus();
			} else
			// !IE
			if ($t.selectionStart || $t.selectionStart == "0") {
				var startPos = $t.selectionStart;
				var endPos = $t.selectionEnd;
				var scrollTop = $t.scrollTop;
				$t.value = $t.value.substring(0, startPos) + myValue
						+ $t.value.substring(endPos, $t.value.length);
				this.focus();
				$t.selectionStart = startPos + myValue.length;
				$t.selectionEnd = startPos + myValue.length;
				$t.scrollTop = scrollTop;
			} else {
				this.value += myValue;
				this.focus();
			}
		}
	})
})(jQuery);

$(function() {
	datagrid("");

	$.ajax({
		url : $("#basePath").val()
				+ "/console/placeholder/getPlaceholders.action",
		type : "post",
		dataType : "json",
		success : function(result) {
			if (result.state == true) {
				$.each(result.placeholders,function(index,element){
					$("#placeholder_selector").append("<option value='"+element.formula+"'>"+element.placeHolderName+"</option>");
				});
			}
		}
	});
	
	$("#placeholder_selector").bind({
		change:function(){
			$("#textContent").insertAtCaret($(this).val());
		}
	});
});