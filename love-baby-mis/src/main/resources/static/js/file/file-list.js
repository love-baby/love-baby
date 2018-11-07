$(function () {
    var fileTable = $('#fileTable').DataTable({
        "bFilter": false,//关闭搜索
        "bProcessing": true,//是否显示取数据时的那个等待提示
        "serverSide": true,//启用服务器端分页
        "sAjaxSource": host + "/file/listAll",
        "bAutoWidth": true,
        "fnServerData": function (sSource, aoData, fnCallback) {
            aoData.push(
                {"name": "searchText", "value": $('#searchText').val()},
                {"name": "dateMin", "value": $('#dateMin').val()},
                {"name": "dateMax", "value": $('#dateMax').val()}
            );
            $.ajax({
                headers: {
                    'token': $.cookie('token')
                },
                async: false,
                type: 'post',
                url: sSource,
                dataType: "json",
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify(aoData),
                success: function (resp) {
                    if (resp.code == 200) {
                        fnCallback(resp.data);
                    } else {
                        layer.msg("查询失败");
                    }
                }, error: function () {
                    layer.msg("查询失败");
                }

            });
        },
        "aoColumnDefs": [{"bSortable": false, "aTargets": [0, 5]}],
        "aaSorting": [[4, "desc"]],
        "aoColumns": [
            {
                "data": null,
                "sTitle": "全选&nbsp;<input type='checkbox' id='selectAll'>",
                "width": "50px",
                "render": function (data, type, row, meta) {
                    var startIndex = meta.settings._iDisplayStart;
                    return "<div align='center'>" + (startIndex + meta.row + 1) + "&nbsp;<input type='checkbox' value='" + data.id + "'></div>";
                }
            },
            {
                "mDataProp": "name",
                "sTitle": "文件名称",
                "sDefaultContent": ""
            }, {
                "mDataProp": "fileType",
                "sTitle": " 类型",
                "sDefaultContent": ""
            }, {
                "mDataProp": "path",
                "sTitle": "路径",
                "sDefaultContent": ""
            }, {
                "mDataProp": "createTime",
                "sTitle": "创建时间",
                "sDefaultContent": ""
            }, {
                "data": null,
                "orderable": false,
                "title": "操作",
                "render": function (data, type, row, meta) {
                    return '<a title="删除" href="javascript:;" onclick="user_delete(this,\'' + data.id + '\')" class="ml-5" style="text-decoration:none"><i class="Hui-iconfont">&#xe6e2;</i></a>';
                }
            }
        ],
        language: {
            "sProcessing": "处理中...",
            "sLengthMenu": "显示 _MENU_ 项结果",
            "sZeroRecords": "没有匹配结果",
            "sInfo": "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
            "sInfoEmpty": "显示第 0 至 0 项结果，共 0 项",
            "sInfoFiltered": "(由 _MAX_ 项结果过滤)",
            "sInfoPostFix": "",
            "sSearch": "搜索:",
            "sUrl": "",
            "sEmptyTable": "表中数据为空",
            "sLoadingRecords": "载入中...",
            "sInfoThousands": ",",
            "oPaginate": {
                "sFirst": "首页",
                "sPrevious": "上页",
                "sNext": "下页",
                "sLast": "末页"
            },
            "oAria": {
                "sSortAscending": ": 以升序排列此列",
                "sSortDescending": ": 以降序排列此列"
            }
        }
    });


    $('#fileTable tbody').on('click', 'tr', function () {
        $(this).toggleClass('selected');
    });

    $('#selectAll').on('click', function () {
        if (this.checked) {
            $("#fileTable tbody input").prop('checked', true);
        } else {
            $("#fileTable tbody input").prop('checked', false);
        }
    });

});

/*用户-删除*/
function user_delete(obj, id) {
    layer.confirm('确认要删除吗？', function (index) {
        $.ajax({
            type: "delete",
            url: host + "/file/" + id,
            headers: {
                'token': $.cookie('token')
            },
            datatype: "json",
            success: function (data) {
                if (data.code == 500) {
                    window.location = "/login.html";
                } else if (data.code == 200) {
                    $(obj).parents("tr").remove();
                    layer.msg('已删除!', {icon: 1, time: 1000});
                } else {
                    layer.msg(data.message);
                }
            }
        });

    });
}

function datadeAll() {
    layer.confirm('确认要删除吗？', function (index) {
        var vals = [];
        $("#fileTable tbody input:checkbox:checked").each(function (index, item) {
            vals.push($(this).val());
        });
        $.ajax({
            type: "delete",
            url: host + "/file/list",
            headers: {
                'token': $.cookie('token')
            },
            contentType: "application/json; charset=utf-8",
            datatype: "json",
            data:JSON.stringify(vals),
            success: function (data) {
                if (data.code == 500) {
                    window.location = "/login.html";
                } else if (data.code == 200) {
                    layer.msg('已删除!', {icon: 1, time: 1000});
                    var table = $('#fileTable').DataTable();
                    table.draw();
                    $("#selectAll").prop('checked', false);
                } else {
                    layer.msg(data.message);
                }
            }
        });
    });
}