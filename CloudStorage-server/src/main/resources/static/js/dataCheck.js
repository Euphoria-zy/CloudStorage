function checkForm1() {
    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;
    var hint1 = document.getElementById("hint1");
    var hint2 = document.getElementById("hint2");
    var hint3 = document.getElementById("hint3");
    hint1.innerHTML = "";hint2.innerHTML = "";hint3.innerHTML = "";
    var b1,b2,b3;
    b1=b2=b3=true;
    //验证用户名：是否符合电话号码规范
    var reg1=/^[1][3,4,5,7,8][0-9]{9}$/;
    //验证密码：至少6位数字或字母的组合
    var reg2=/^[0-9a-zA-Z]{6,}$/;
    //判断用户名
    if (username == ''){
        hint1.innerHTML = "用户名不能为空";
        b1=false;
    }
    if (!reg1.test(username)) {
        hint1.innerHTML = "用户名不符合电话号码规范";
        b1=false;
    }
    //验证密码
    if (!reg2.test(password)){
        hint2.innerHTML = "密码至少为6位字母或数字的组合";
        b2=false;
    }
    return (b1&&b2);
}
function checkForm2() {
    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;
    //确认密码
    var checkPwd = document.getElementById("checkPwd").value;
    var email = document.getElementById("email").value;
    var hint1 = document.getElementById("hint1");
    var hint2 = document.getElementById("hint2");
    var hint3 = document.getElementById("hint3");
    var hint4 = document.getElementById("hint4");
    hint1.innerHTML = "";hint2.innerHTML = "";hint3.innerHTML = "";hint4.innerHTML="";
    var b1,b2,b3,b4;
    b1=b2=b3=b4=true;
    //验证用户名：是否符合电话号码规范
    var reg1= /^[1][3,4,5,7,8][0-9]{9}$/;
    //验证密码：至少6位数字或字母的组合
    var reg2= /^[0-9a-zA-Z]{6,}$/;
    //验证邮箱
    var reg3= /[\w!#$%&'*+/=?^_`{|}~-]+(?:\.[\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\w](?:[\w-]*[\w])?\.)+[\w](?:[\w-]*[\w])?/;
    //判断用户名
    if (username == ''){
        hint1.innerHTML = "用户名不能为空";
        b1=false;
    }
    if (!reg1.test(username)) {
        hint1.innerHTML = "用户名不符合电话号码规范";
        b1=false;
    }
    //验证密码
    if (!reg2.test(password)){
        hint2.innerHTML = "密码至少为6位字母或数字的组合";
        b2=false;
    }
    if (password !== checkPwd){
        hint3.innerHTML= "两次密码不一致";
        b3=false;
    }
    if (!reg3.test(email)){
        hint4.innerHTML= "邮箱格式不正确";
        b4=false;
    }
    return (b1&&b2&&b3&&b4);
}
function checkForm3() {
    var username = document.getElementById("username").value;
    var password = document.getElementById("newPwd").value;
    //确认密码
    var checkPwd = document.getElementById("checkPwd").value;
    var hint1 = document.getElementById("hint1");
    var hint2 = document.getElementById("hint2");
    var hint3 = document.getElementById("hint3");
    hint1.innerHTML = "";hint2.innerHTML = "";hint3.innerHTML = "";
    var b1,b2,b3;
    b1=b2=b3=true;
    //验证用户名：是否符合电话号码规范
    var reg1=/^[1][3,4,5,7,8][0-9]{9}$/;
    //验证密码：至少6位数字或字母的组合
    var reg2=/^[0-9a-zA-Z]{6,}$/;
    //判断用户名
    if (username == ''){
        hint1.innerHTML = "用户名不能为空";
        b1=false;
    }
    if (!reg1.test(username)) {
        hint1.innerHTML = "用户名不符合电话号码规范";
        b1=false;
    }
    //验证密码
    if (!reg2.test(password)){
        hint2.innerHTML = "密码至少为6位字母或数字的组合";
        b2=false;
    }
    if (password !== checkPwd){
        hint3.innerHTML= "两次密码不一致";
        b3=false;
    }
    return (b1&&b2&&b3);
}