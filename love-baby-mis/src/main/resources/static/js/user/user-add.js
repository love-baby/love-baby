$(function () {

    //上传文件
    $('#fileupload').fileupload({
        dataType: 'json',
        singleFileUploads: false,
        acceptFileTypes: /(\.|\/)(gif|jpe?g|png)$/i,
        maxFileSize: 5 * 1024 * 1024,
        minFileSize: 5,
        messages: {
            maxFileSize: '文件最大不能大于5M',
            acceptFileTypes: '只能上传图片类型文件',
        },
        processfail: function (e, data) {
            var currentFile = data.files[data.index];
            if (data.files.error && currentFile.error) {
                parent.layer.msg(currentFile.error);
            }
        },
        done: function (e, data) {
            $("#avatar").val(data.result.data[0].path);
            $("#avatar_img").attr("src", img_host + data.result.data[0].path);
            $("#avatar_img").css("display","block");
        },
        success: function (result, textStatus, jqXHR) {
            parent.layer.msg(result.message);
        }
    });

    var id = location.search.substring(1);//获取查询串
    if (id != "" && id != null) {
        $.ajax({
            type: "GET",
            url: host + "/user/userInfo/" + id,
            headers: {
                'token': $.cookie('token')
            },
            dataType: "json",
            success: function (data) {
                if (data.code == 500) {
                    window.location = "/login.html";
                } else if (data.code == 200) {
                    if (data.data != null) {
                        $("#name").val(data.data.name);
                        $("#pwd").val(data.data.pwd);
                        $("input[name='sex'][value=" + data.data.sex + "]").attr("checked", true);
                        $("input[name='sex'][value=" + data.data.sex + "]").parent().addClass("checked");
                        $("#avatar").val(data.data.avatar);
                        $("#avatar_img").attr("src", img_host + data.data.avatar);
                        $("#avatar_img").css("display","block");
                    }

                } else {
                    parent.layer.msg(data.message);
                }
            }
        });
    }
    $('.skin-minimal input').iCheck({
        checkboxClass: 'icheckbox-blue',
        radioClass: 'iradio-blue',
        increaseArea: '20%'
    });

    $("#form-user-add").validate({
        rules: {
            name: {
                required: true
            },
            sex: {
                required: true
            },
            pwd: {
                required: true,
                isPwd: true
            },
            avatar: {
                required: true
            }

        },
        onkeyup: false,
        focusCleanup: true,
        success: "valid",
        submitHandler: function () {
            var user = {
                "name": $("#name").val(),
                "pwd": $("#pwd").val(),
                "sex": $("input[name='sex']:checked").val(),
                "avatar": $("#avatar").val()
            };
            var url = host + "/user";
            if (id != "" && id != null) {
                url = host + "/user/update";
                user.id = id;
            }
            $.ajax({
                async: false,
                type: "PUT",
                url: url,
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify(user),
                dataType: "json",
                headers: {
                    'token': $.cookie('token')
                },
                success: function (data) {
                    if (data.code == 200) {
                        parent.layer.msg("提交成功");
                    } else {
                        parent.layer.msg(data.message);
                    }
                },
                error: function () {
                    parent.layer.msg("提交成功");
                }
            });
            var index = parent.layer.getFrameIndex(window.name);
            parent.$('.btn-refresh').click();
            parent.layer.close(index);
        }
    });
});