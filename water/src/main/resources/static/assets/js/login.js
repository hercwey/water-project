/*
 * @Description: 水司登录页
 * @Author: Wang Rfan
 * @Date: 2019-07-24 23:32:37
 */

var Login = {
    login: function(){
        var account = $("#account").val();
        var password = $("#password").val();
        console.log(account,password);
    },
    togglePassword: function () {
        var inputType = $("#password").prop("type");
        if(inputType == "password"){
            $("#password").prop("type","text");
            $("#eye").removeClass("icon-hide");
            $("#eye").addClass("icon-eye");

        }else{
            $("#password").prop("type","password");
            $("#eye").removeClass("icon-eye");
            $("#eye").addClass("icon-hide"); 
        }
    }
}