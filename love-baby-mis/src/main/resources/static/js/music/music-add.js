$(function () {

    //上传文件
    $('#fileupload').fileupload({
        dataType: 'json',
        singleFileUploads: false,
        acceptFileTypes: /(\.|\/)(mp3|avi|aac|mpg|mpeg|wav|flac|ape)$/i,
        maxFileSize: 50 * 1024 * 1024,
        minFileSize: 5,
        messages: {
            maxFileSize: '文件最大不能大于50M',
            acceptFileTypes: '只能上传音乐类型文件',
        },
        processfail: function (e, data) {
            var currentFile = data.files[data.index];
            if (data.files.error && currentFile.error) {
                parent.layer.msg(currentFile.error);
            }
        },
        done: function (e, data) {
            $("#path").val(data.result.data[0].path);
        },
        success: function (result, textStatus, jqXHR) {
            parent.layer.msg(result.message);
        }
    });

    var id = location.search.substring(1);//获取查询串
    if (id != "" && id != null) {
        $.ajax({
            type: "GET",
            url: host + "/music/" + id,
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
                        $("#filePathId").val(data.data.filePathId);
                        $("#author").val(data.data.author == null ? "" : data.data.author.name);
                        $("#album").val(data.data.album == null ? "" : data.data.album.name);
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
            var music = {
                "name": $("#name").val(),
                "authorId": $("#author").val(),
                "albumId":$("#album").val(),
                "filePathId": $("#path").val()
            };
            var url = host + "/music";
            if (id != "" && id != null) {
                url = host + "/music/update";
                music.id = id;
            }
            $.ajax({
                async: false,
                type: "PUT",
                url: url,
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify(music),
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