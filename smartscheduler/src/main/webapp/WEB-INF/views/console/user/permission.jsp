<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/scripts/ztree/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ztree/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ztree/js/jquery.ztree.excheck-3.5.js"></script>
    <script type="text/javascript">


/**ztree的参数配置，setting主要是设置一些tree的属性，是本地数据源，还是远程，动画效果，是否含有复选框等等**/    
var setting = {  
 check: { /**复选框**/  
  enable: true,  
 // autoCheckTrigger: true,
  chkboxType: {"Y":"ps", "N":"s"}  
 },  
 view: {                                    
  //dblClickExpand: false,  
  expandSpeed: 300 //设置树展开的动画速度，IE6下面没效果，  
 },                            
 data: {                                    
  simpleData: {   //简单的数据源，一般开发中都是从数据库里读取，API有介绍，这里只是本地的                           
   enable: true,  
   idKey: "id",  //id和pid，这里不用多说了吧，树的目录级别  
   pIdKey: "pId",  
   rootPId: 0   //根节点  
  }                            
 },                           
 callback: {     /**回调函数的设置，随便写了两个**/  
  beforeClick: beforeClick,                                    
  onCheck: onCheck                            
 }  
};  
function beforeClick(treeId, treeNode) {  
// alert("beforeClick    "+treeId+"    "+treeNode);  
}  
function onCheck(e, treeId, treeNode) { 
// alert("onCheck     "+treeId+"    "+treeNode); 
}       
  

  
function init(){  
	
		
	
			
	 
 
   //zTree = $("#cityTree").zTree(setting, citynodes); 
}

function open_permission_dlg(){
var rows = $('#datagrid').datagrid('getChecked');
		if (rows.length != 1) {
		$.messager.show({ // show error message
		    title : '温馨提示',
			msg : "请选择一个用户"
			});
		}else{
   $("#checkedEmployId").val(rows[0].empNum);
   $('#dlg_permission').dialog('open');
  $.ajax({
	  type: 'post' ,
	  url: '${pageContext.request.contextPath}/console/user/getUserMenu.action' ,
	  dataType:'json' ,
	 	data:{
	 	employId:rows[0].empNum
	 	},
	  success:function(result){
			if(result.success=="1"){
				 var ztrees= $.fn.zTree.init($("#cityTree"),setting,result.menu); 
                  ztrees.expandAll(true);//全部展开  	
			}else{
			   $.messager.show({
				title:"温馨提示", 
				msg:result.errorMsg
			});
							
		   }
			} ,
		   error:function(result){
			  $.meesager.show({
				title:'温馨提示' , 
				msg:result.errorMsg
				});
			}			
		});
		}
}

//保存用户菜单权限
	  function save_permission(){
	      var treeObj = $.fn.zTree.getZTreeObj("cityTree");
          var nodes = treeObj.getCheckedNodes(true);
          var ids =[];  
           ids.push("0");
          for(var i=0;i<nodes.length;i++){
             ids.push(nodes[i].id);
          }
          $.ajax({    
            type: 'POST',
            data:{
            "menuItemIds":ids,
            "employId":$("#checkedEmployId").val()
            },  
            dataType : "json",  
            url: "${pageContext.request.contextPath}/console/user/addUserMenuItem.action",  
            error: function () {  
                $.messager.alert('提示',"保存失败");  
            },  
            success:function(data){  
                if(data.state){
                   $.messager.alert('提示',"保存成功!");
                }else{
                   $.messager.alert('提示',data.msg);
                }
            }  
       }); 
	}
</script>
<div id="dlg_permission" class="easyui-dialog"
		style="width: 580px; height: 400px; " closed="true"
		buttons="#dlg_permission_buttons"  modal="true" title="">
<div class="easyui-tabs" fit="true" border="false" >
	<div title="权限分配">
	<input type="hidden" id="checkedEmployId" >
		 <ul id="cityTree" class="ztree"></ul>
		</div>
	</div>
	<div id="dlg_permission_buttons">
		<a href="javascript:void(0)" class="easyui-linkbutton c6"
			iconCls="icon-ok"  onclick="return save_permission();"style="width: 90px">保存</a> 
			<a
			href="javascript:void(0)" class="easyui-linkbutton"
			iconCls="icon-cancel" onclick="javascript:$('#dlg_permission').dialog('close')"
			style="width: 90px">关闭</a>
	</div>