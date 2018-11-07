$(function () {
    //上传文件
    $('#fileupload').fileupload({
        dataType: 'json',
        singleFileUploads: false,
        acceptFileTypes: /(\.|\/)(gif|jpe?g|png)$/i,
        maxFileSize: 5 * 1024 * 1024,//文件最大
        messages: {
            maxFileSize: '文件最大不能大于5M',
            acceptFileTypes: '只能上传图片类型文件'
        },
        processfail: function (e, data) {
            var currentFile = data.files[data.index];
            if (data.files.error && currentFile.error) {
                parent.layer.msg(currentFile.error);
            }
        },
        progressall: function (e, data) {
            var progress = parseInt(data.loaded / data.total * 100);
            $('#progress_one').css('width', progress + '%').text(progress + "%");
        },
        done: function (e, data) {

        },
        success: function (result) {
            parent.layer.msg(result.message);
        }
    });

    //多文件一次提交
    $('#fileupload_one_submit').fileupload({
        dataType: 'json',
        singleFileUploads: false,
        acceptFileTypes: /(\.|\/)(gif|jpe?g|png)$/i,//文件类型
        maxFileSize: 5 * 1024 * 1024,//文件大小
        minFileSize: 5,
        maxNumberOfFiles: 5,//最大文件数量
        messages: {
            maxFileSize: '文件最大不能大于5M',
            acceptFileTypes: '只能上传图片类型文件',
            maxNumberOfFiles: '上传的文件数量超过允许的最大值'
        },
        processfail: function (e, data) {//验证失败
            var currentFile = data.files[data.index];
            if (data.files.error && currentFile.error) {
                parent.layer.msg(currentFile.error);
                console.log("错误" + currentFile.error);
                return;
            }
        },
        start: function (e, data) {//开始上传
            $("#fileupload_one_submit_file_name").empty();
            $.each(data.files, function (index, file) {
                $("#fileupload_one_submit_file_name").append("<div title='" + file.fileName + "' style='text-align: left;white-space: nowrap; overflow: hidden;text-overflow: ellipsis;'>" + (index + 1) + "、" + file.name + "</div>");
            });
        },
        progressall: function (e, data) {//进度条事件
            var progress = parseInt(data.loaded / data.total * 100);
            $('#progress_fileupload_one_submit').css('width', progress + '%').text(progress + "%");
        },
        done: function (e, data) {//上传完成之后的操作
            $("#fileupload_one_submit_file_name").empty();
            $.each(data.result.data, function (index, file) {
                $("#fileupload_one_submit_file_name").append("<div title='" + file.fileName + "' style='text-align: left;white-space: nowrap; overflow: hidden;text-overflow: ellipsis;'>" + (index + 1) + "、" + file.fileName + "</div>");
            });
        },
        success: function (result) {
            parent.layer.msg(result.message);
        }
    });


});