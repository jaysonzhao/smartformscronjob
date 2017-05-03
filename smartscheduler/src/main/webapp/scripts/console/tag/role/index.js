

function getQueryString(key) {
	var reg = new RegExp("(^|&)" + key + "=([^&]*)(&|$)");
	var result = window.location.search.substr(1).match(reg);
	return result ? decodeURIComponent(result[2]) : null;
}


function getUnSelectRoleData() {
	var unSelect = document.getElementById("unSelectRole");
	var alreadSelectRole = document.getElementById("alreadSelectRole");
	var opts = "";
	for (var i = 0; i < unSelect.length; i++) {
		var flag = true;
		if (unSelect[i].selected == true) {
			for (var b = 0; b < alreadSelectRole.length; b++) {
				if (unSelect[i].value == alreadSelectRole[b].value) {
					flag = false;
				}
			}
			if (flag) {
				opts += "<option value='" + unSelect[i].value + "'>"
						+ unSelect[i].text + "</option>";
			}
		}
	}
	$("#alreadSelectRole").append(opts);
}

function delSelectRoleData() {
	$("#alreadSelectRole").find("option:selected").remove();
}

function saveAlreadSelectRoleData() {
	var alreadSelectBackLink = document.getElementById("alreadSelectRole");
	var value = "";
	var name = "";
	for (var i = 0; i < alreadSelectBackLink.length; i++) {
		value += alreadSelectBackLink[i].text + "("
				+ alreadSelectBackLink[i].value + ");";
		name += alreadSelectBackLink[i].text + ";";
	}
	var id =getQueryString("id");
	try {
		window.opener.$("#" + id + "_num").val(value);
		window.opener.$("#" + id).val(name);
	} catch (e) {
	}
	window.close();
}



$(function() {
	_getOrgDepartmentTree();
});
