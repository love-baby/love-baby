$(function () {
    $.ajax({
        type: "GET",
        url: "user/userInfo",
        success: function (data) {
            if (data.code == 200) {
                $("#userName").html(data.data.name + '<i class="Hui-iconfont">&#xe6d5;</i>');
            } else {
                window.location.replace("login.html");
            }
        },
        beforeSend: function (request) {
            request.setRequestHeader("token", $.cookie('token'));
        },
        complete: function (XMLHttpRequest, textStatus) {

        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            window.location.replace("login.html");
        }
    });
});

/*个人信息*/
function myselfinfo() {
    layer.open({
        type: 1,
        area: ['300px', '200px'],
        fix: false, //不固定
        maxmin: true,
        shade: 0.4,
        title: '查看信息',
        content: '<div>管理员信息</div>'
    });
}