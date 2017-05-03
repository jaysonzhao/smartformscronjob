/**
 * 登录超时后 ajax 请求 重定向到登录界面
 * 
 */
jQuery(function($) {
    var curWwwPath = window.document.location.href;
    var pathName = window.document.location.pathname;
    var pos = curWwwPath.indexOf(pathName);
    var localhostPaht = curWwwPath.substring(0, pos);
    var projectName = pathName
            .substring(0, pathName.substr(1).indexOf('/') + 1);
    var rootPath = localhostPaht + projectName;
    // 备份jquery的ajax方法
    var _ajax = $.ajax;
    // 重写ajax方法，
    $.ajax = function(opt) {
        var _success = opt && opt.success || function(a, b) {
        };
        var _error = opt && opt.error || function(a, b) {
        };
        var _opt = $.extend(opt, {
            success : function(data, textStatus) {
                // 如果后台将请求重定向到了登录页，则data里面存放的就是登录页的源码，这里需要判断(登录页面一般是源码，所以这里只判断是否有html标签)
                if ((data + "").indexOf('html') != -1&&(data + "").indexOf("_#####_loging_####_")>0) {
                    $.messager.confirm('温馨提示', '您的登录已过期,请重新登录', function(r) {
                        if (r) {
                            window.location.href = rootPath+ "/console/login.xsp";
                        } else {
                           // window.location.href = rootPath+ "/console/login.xsp";
                        }
                    });
                    return;
                }
                _success(data, textStatus);
            },
            error : function(data, textStatus) {
                if ((data + "").indexOf('html') != -1&&(data + "").indexOf("_#####_loging_####_")>0) {
                    $.messager.confirm('温馨提示', '您的登录已过期,请重新登录', function(r) {
                        if (r) {
                            window.location.href = rootPath+ "/console/login.xsp";
                        } else {
                           // window.location.href = rootPath+ "/console/login.xsp";
                        }
                    });
                    return;
                }

                if (typeof data == "object") {
                    if (data.hasOwnProperty('responseText')&&data.responseText.indexOf('html') != -1&&data.responseText.indexOf("_#####_loging_####_")>0) {
                        $.messager.confirm('温馨提示', '您的登录已过期,请重新登录',
                                function(r) {
                                    if (r) {
                                        window.location.href = rootPath+ "/console/login.xsp";
                                    } else {
                                      //  window.location.href = rootPath+ "/console/login.xsp";
                                    }
                                });
                        return;
                    }

                }
                _error(data, textStatus);
            },
            beforeSend : function(XHR) {
                // 提交前回调方法
            }
        });
        return _ajax(_opt);
    };
});
