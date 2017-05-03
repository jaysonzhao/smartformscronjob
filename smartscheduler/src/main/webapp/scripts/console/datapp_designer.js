$(function() {
    var ctxPath=$("#ctxPath").val();
    var datAppId=$("#datAppId").val();
    //缺省页面为表单定义页面
    $("#iframe1").attr("src", ctxPath+"/console/detform/index/"+datAppId+".xsp?r1="+Math.random());

    $('#navtree').tree({
        data: [{
            id:'root1',
            text: $("#datAppTitle").val()+"("+$("#datAppName").val()+")",
            //state: 'closed',
            children: [{
                id:'node1',
                text: '表单',
                href:ctxPath+"/console/detform/index/"+datAppId+".xsp"
            },{
                id:'node2',
                text: '视图',
                href:ctxPath+"/console/detview/index/"+datAppId+".xsp"
            },{
                id:'node3',
                text:'子表单',
                href:ctxPath+"/console/detsubform/index/"+datAppId+".xsp"
            },{
                id:'node4',
                text:'脚本库',
                href:ctxPath+"/console/detScriptLib/index/"+datAppId+".xsp"
            }]
        }],
        onClick:function(node) {
            if (node.id!="root1") {
                var url=node.href+"?r1="+Math.random();
                $("#iframe1").attr("src", url);
            }
        }
    });
});
