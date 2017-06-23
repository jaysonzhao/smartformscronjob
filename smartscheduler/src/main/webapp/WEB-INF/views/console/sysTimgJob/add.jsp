<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/WEB-INF/tlds/formCtrlsTag.tld" prefix="sot"%>
<style>
.fitem {
	margin-bottom: 15px;
}
</style>
<script>
	function getForms() {
		$('#formId').empty();
		$
				.ajax({
					url : "${pageContext.request.contextPath}/console/detform/getforms/getAllFormNames.action",
					type : "post",
					data : {
						"datappid" : $("#appId").val()
					},
					dataType : "json",
					success : function(result) {
						if (result.state == false) {
							$.messager.show({
								title : '温馨提示',
								msg : result.msg
							});
						} else {
							var datas = result.datas;
							$('#formId').combobox({
								valueField : 'id',
								data : datas,
								textField : 'text',
								editable : true,
								multiple : "true"
							});
						}
					}
				});
	}
	/*  打开新增任务信息的对话框 */
	function openDlg() {
		$('#sysTimeJob_dlg').dialog('open').dialog('setTitle', ' ');
		$('#fm').form('clear');
		//$("#errorStop").attr("checked", "checked");
		//$("#errorNotify").attr("checked", "checked");
	//	$("#jobType").val("custom");

		//$("#runDuration").textbox('setValue', "-1");
		$("#schedulerName").val("");
		$("#appId").val("");
		//getForms();

	}

	function jobTypeChange() {
		if ($("#jobType").val() == "collection") {
			$("#schedulerName").val("dataSysTask");
		} else {
			$("#schedulerName").val("");
			$("#schedulerName").attr("readonly", false);
		}
	}

	function saveSysTimeJob() {
		var values = $("#fm").serialize();
		if ($("#fm").form('validate')) {
			/* if ($("#jobType").val() == "collection") {
				var formId = $('#formId').combobox('getValues')
				if (formId == "") {
					alert("数据采集至少需要选择一个表单");
					return false;
				}
			} */
		/* 	var errorNotify = $('input[name="errorNotify"]:checked ').val();
			console.log("errorNotify:" + errorNotify)
			if (errorNotify == "enabled") {
				var notifyEmail = $("#notifyEmail").val();
				if (notifyEmail == "") {
					alert("请填写邮件地址");
					return false;
				}
			} */

			$("#dlg_btnsave").linkbutton("disable");
			$
					.ajax({
						url : "${pageContext.request.contextPath}/console/quartz/saveOrUpdate.action",
						type : "post",
						data : values,
						dataType : "json",
						success : function(result) {
							$("#dlg_btnsave").linkbutton("enable");
							if (result.state == false) {
								$.messager.show({
									title : '温馨提示',
									msg : result.msg
								});
							} else {
								$('#sysTimeJob_dlg').dialog('close'); // close the dialog
								$('#datagrid').datagrid('reload'); // reload the user data
							}
						},
						error : function() {
							$("#dlg_btnsave").linkbutton("enable");
						}
					});
		}
	}
</script>

<div id="sysTimeJob_dlg" class="easyui-dialog"
	style="width: 550px; height: 500px;" closed="true"
	buttons="#sysTimeJob_dlg_buttons" modal="true">

	<div id="tabs" class="easyui-tabs" fit="true" border="false">
		<div title="任务配置信息">
			<form id="fm" method="post">
				<br>
				<div class="fitem">
					<label>任务名称</label> <input class="easyui-textbox"
						style="width: 280px;" required="true" name="jobName" />
				</div>
				<div class="fitem">
					<label>执行时间</label> <input type="hidden" name="jobId"> <input
						class="easyui-textbox" style="width: 280px;" required="true"
						name="cronExpression" />
				</div>
			<!-- 	<div class="fitem">
					<label>任务类型</label> <select onchange="return jobTypeChange();"
						id="jobType" name="jobType"
						style="-webkit-border-radius: 6px 6px 6px 6px; border-radius: 6px 6px 6px 6px; margin-left: 0px; margin-right: 0px; padding-top: 3px; padding-bottom: 3px; width: 280px;">
						<option value="custom" selected="selected">自定义</option>
						<option value="collection">数据采集</option>
					</select>
				</div>
				<div class="fitem">
					<label>运行时长</label> <input style="width: 280px;"
						class="easyui-numberbox" precision="0" id="runDuration"
						name="runDuration" min="-1" required="true" /> <span
						style="color: red"> -1 时间无限制 </span>
				</div>
				<div class="fitem">
					<label>邮件地址</label> <input class="easyui-validatebox"
						validType="email" invalidMessage="请填写正确的邮件格式"
						style="width: 280px;" name="notifyEmail" id="notifyEmail" />
				</div> -->
			<%-- 	<div class="fitem">
					<label>应用库</label>
					<sot:combobox id="appId" name="appId"
						cssstyle="-webkit-border-radius: 6px 6px 6px 6px; border-radius: 6px 6px 6px 6px;margin-left: 0px;margin-right: 0px;padding-top: 3px;padding-bottom: 3px;width: 280px;"
						onchange="getForms()"
						data="select app_Name text ,app_Id  value from DAT_APPLICATION "
						staticvalue="|请选择一个应用库"></sot:combobox>
				</div>

				<div class="fitem">
					<label>表单</label> <select id="formId" name="formId"
						class="easyui-combobox" style="width: 280px;">

					</select>
				</div> --%>

				<div class="fitem">
					<label>调度任务</label> <select id="schedulerName" name="schedulerName"
						style="-webkit-border-radius: 6px 6px 6px 6px; border-radius: 6px 6px 6px 6px; margin-left: 0px; margin-right: 0px; padding-top: 3px; padding-bottom: 3px; width: 280px;">
						<option value="" selected="selected">请选择一个调度任务</option>
						<c:forEach var="name" items="${tasks}">
							<option value="${name}">${name}</option>
						</c:forEach>
					</select>
				</div>

			<!-- 	<div class="fitem">
					<label>异常通知</label> <input id="errorNotify" name="errorNotify"
						type="radio" value="disabled" />关闭 <input name="errorNotify"
						type="radio" value="enabled">启用
				</div>

				<div class="fitem">
					<label>异常停止</label> <input id="errorStop" name="errorStop"
						type="radio" value="disabled" />关闭 <input name="errorStop"
						type="radio" value="enabled" />启用
				</div> -->


			</form>
		</div>

	</div>
</div>
<div id="sysTimeJob_dlg_buttons">
	<a id="dlg_btnsave" href="javascript:void(0)"
		class="easyui-linkbutton c6" iconCls="icon-ok"
		onclick="saveSysTimeJob()" style="width: 90px">保存</a> <a
		href="javascript:void(0)" class="easyui-linkbutton"
		iconCls="icon-cancel"
		onclick="javascript:$('#sysTimeJob_dlg').dialog('close')"
		style="width: 90px">取消</a>
</div>




