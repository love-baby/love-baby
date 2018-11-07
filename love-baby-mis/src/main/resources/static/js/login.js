$(document).ready(function () {
    $("#login_form").ajaxForm(function (data) {
        if (data.code == 200) {
            var date = new Date();
            date.setTime(date.getTime() + (1 * 30 * 24 * 60 * 60 * 1000));//一个月
            $.cookie('token', data.data.token, {path: "/", expires: date});
            window.location.replace("index.html");
        } else {
            $(".clearfix").html(data.message);
        }
    });
});