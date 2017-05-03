$(function() {
   var ctxPath=$("#ctxPath").val();
	var nspath=ctxPath+"/console/detform";  //命名空间路径
    var datAppId=$("#datAppId").val();
    var dataGridId=$("#_dataGridId").val();
    if(dataGridId==null){
    	dataGridId="grid1";
    }
     var uploader_zip = WebUploader.create({
	    // 选完文件后，是否自动上传。
	    auto: true,
	    // swf文件路径
	    swf: ctxPath+'/scripts/webuploader-0.1.5/Uploader.swf',
	    // 文件接收服务端。
	    server: ctxPath+'/console/zipFile/upload.action?appId='+datAppId,
	    // 选择文件的按钮。可选。
	    // 内部根据当前运行是创建，可能是input元素，也可能是flash.
	    pick: '#zipFileImport',
	    // 只允许选择图片文件。
	   resize : false,  
       duplicate :true ,
       accept: {
           title: '只允许上传zip文件',
           extensions: 'zip',
           mimeTypes: 'zip'
       }
	   
	});
		
	// 文件上传过程中创建进度条实时显示。
     uploader_zip.on( 'uploadProgress', function( file, percentage ) {
	    var win = $.messager.progress({
			title:'温馨提示',
			msg:'文件上传中，请稍后。。。'
			});
	});
     uploader_zip.on( 'uploadSuccess', function( file,response) {
		 $.messager.progress('close');
		 $("#"+dataGridId).datagrid("load",{r2:Math.random(), datappid:datAppId});
		 });
     uploader_zip.on('uploadError', function(file, response){
    	 $.messager.progress('close');
    	 $("#"+dataGridId).datagrid("load",{r2:Math.random(), datappid:datAppId});
        });
     uploader_zip.on("error",function (type){
	    	$.messager.progress('close');
	        if (type=="Q_TYPE_DENIED"){
	        	$.messager.show({ // show error message
					title : '温馨提示',
					msg : '只允许上传后缀.zip的文件'
				});
	        }
	    });
});
		