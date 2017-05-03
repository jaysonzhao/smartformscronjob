/**
 * @author sujialin
 * 公共函数
 */

//时间戳转换为日期时间格式化函数
function timestampFormat(timestamp) {
    return (new Date(timestamp)).format("yyyy-MM-dd hh:mm:ss");
} 

/**
 * 时间戳转换日期
 * easyui datagrid 时间列格式转换
 */
function gridDateFmt(val,row) {
    var datestr=timestampFormat(val);
    return datestr;
}

/**
 * easyui validatebox自定义验证规则
 */
$.extend($.fn.validatebox.defaults.rules, { 
    //验证名称是否英文、数字和下划线
    isEngName: { 
        validator: function(value, param){ 
            return /^[a-zA-Z0-9_]+$/.test(value);
            //return value.length >= param[0]; 
        }, 
        message: "只允许英文字母、数字和下划线！" 
    },
    //验证是否以数字开头
    isCharFirst:{
        validator:function(value, param) {
            return !(/^\d/.test(value));
        },
        message:"不能使用数字开头！"
    }
}); 

/**
 * 判断当前浏览器是否有安装Flash插件
 * @returns true=已安装，false=没有安装
 */
function haveFlash() {
	var isIE = /msie/.test(navigator.userAgent.toLowerCase());
	var result=false;
	if (isIE) {
		try{
	        var swf1 = new ActiveXObject('ShockwaveFlash.ShockwaveFlash');
	        result=true;
	    } catch(e){
	        result=false;
	    }
	} else {
		//非IE浏览器直接返回true
		result=true;
		/*
		try {
	        var swf2 = navigator.plugins['Shockwave Flash'];
	        if(swf2 == undefined){
	            result=false;
	        } else {
	            result=true;
	        }
	    } catch(e){
	        result=false;
	    }
	    */
	}
	return result;
}
