$(function() {
    if (window != top) {
      top.location.href = location.href; 
    }
    $("#username").focus();
    var ctxpath=$("#ctxpath").val();
    $(".r1").css("background", "url('"+ctxpath+"/images/login/user_logo.png') #f4f4f4 no-repeat center center");
    $(".r3").css("background", "url('"+ctxpath+"/images/login/psw_logo.png') #f4f4f4 no-repeat center center");
});