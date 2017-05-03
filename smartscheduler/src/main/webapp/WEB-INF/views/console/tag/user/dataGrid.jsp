<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript">





 document.onkeydown=function(event){
            var e = event || window.event || arguments.callee.caller.arguments[0];       
             if(e && e.keyCode==13){ // enter 键
              var name = $('#name').val();
              var departmentId="${deparmentId}";
              if(departmentId==""){
              departmentId="0";
              }
			datagrid(name, departmentId);
            }
        }; 
        
        
        
	function datagrid(name, deparmentId) {
		$('#datagrid').datagrid(
						{
							height : '95%',
							fit : true,
							url : '${pageContext.request.contextPath}/console/user/shows.action',
							method : 'POST',
							queryParams : {
								"name" : name,
								"deparmentId" : deparmentId,
								"showcurrent":"${showcurrent}"
							},
							striped : true,
							nowrap : true,
							pageSize : 17,
							pageNumber : 1,
							pageList : [ 17,20,30,40,50,60,70,80,90,100 ],
							showFooter : true,
							loadMsg : '数据加载中请稍候……',
							pagination : true,
							toolbar : "#toolbar",
							singleSelect : ${singleSelect},
							checkOnSelect : ${singleSelect},
							selectOnCheck : ${singleSelect},
							columns : [ [
							 {
								field : 'ck',
								checkbox : true
							},
									{
										field : 'empNum',
										title : '员工id',
										width : 100,
										align : 'center',
										hidden : true
									},
									{
										field : 'empName',
										title : '姓名',
										width : 150,
										align : 'center'
									},
									{
										field : 'ldapUid',
										title : 'LDAP UID',
										width : 150,
										align : 'center'
									},
									{
										field : 'nickName',
										title : '账号别名',
										width : 150,
										align : 'center'
									}
									] ],
							onSelect : function(rowData) {
							$('#datagrid').datagrid("unselectAll");
							//alert(1);
							window.parent._getAddCheckedUsers();
							 // alert(1);
								//
							},
							onCheck:function(index,row){
							window.parent._getAddCheckedUsers();
							},
							onCheckAll:function(rows){
							window.parent._getAddCheckedUsers();
							},
							onUncheckAll:function(rows){
							  var empNums=new Array()		
							  for(var i=0;i<rows.length;i++){
                              	    empNums[i]=rows[i].empNum;
                                  }
                               window.parent._romoveOptionCheckedUsers(empNums);
							},
							onUncheck:function(index,row){
							var empNums=new Array();
							  empNums[0]=row.empNum;
							  window.parent._romoveOptionCheckedUsers(empNums);
							},
							onClickRow:function(index,row){
							$('#datagrid').datagrid('checkRow', index);
							}
							
						});
	}

	function searchByName(departmentId) {
		var name = $('#name').val();
			datagrid(name, departmentId);
		
	}

        

        $(function(){
          $('#datagrid').on("cellclick",function(e){ 
             alert(1);
             });
       });

</script>
<div id="datagrid" style="width: 100%; height: 100%"></div>
