<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
 <link href="${pageContext.request.contextPath}/images/LOGO.ico" rel="shortcut icon" type="image/x-icon" />
<link href="${pageContext.request.contextPath}/styles/common.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/scripts/jquery/easyui/themes/default/easyui.css"> 
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/frameset.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/treeicon/icon.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/scripts/jquery/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/scripts/webuploader-0.1.5/webuploader.css">

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery/jquery-1.11.1.min.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/json2.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/frameset.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/comonfn.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/extprotype.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/webuploader-0.1.5/webuploader.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ajaxRebuild.js"></script>
<script type="text/javascript">
 /**
 **用于当datagrid的rownumbers:true时，将行号列宽自适应
 ** 同时需要在loadSuccess增加 $(this).datagrid("fixRownumber");
 ** onLoadSuccess:function(){
 **  $(this).datagrid("fixRownumber");
 ** }
 */
 $.extend($.fn.datagrid.methods, {
    fixRownumber : function (jq) {
        return jq.each(function () {
            var panel = $(this).datagrid("getPanel");
            //获取最后一行的number容器,并拷贝一份
            var clone = $(".datagrid-cell-rownumber", panel).last().clone();
            //由于在某些浏览器里面,是不支持获取隐藏元素的宽度,所以取巧一下
            clone.css({
                "position" : "absolute",
                left : -1000
            }).appendTo("body");
            var width = clone.width("auto").width();
            //默认宽度是25,所以只有大于25的时候才进行fix
            if (width > 25) {
                //多加5个像素,保持一点边距
                $(".datagrid-header-rownumber,.datagrid-cell-rownumber", panel).width(width + 5);
                //修改了宽度之后,需要对容器进行重新计算,所以调用resize
                $(this).datagrid("resize");
                //一些清理工作
                clone.remove();
                clone = null;
            } else {
                //还原成默认状态
                $(".datagrid-header-rownumber,.datagrid-cell-rownumber", panel).removeAttr("style");
            }
        });
    }
});
</script>
 
 