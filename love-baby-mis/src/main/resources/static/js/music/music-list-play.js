var mplayer_song = [[
    {
        "basic": true,
        "name": "试听列表",
        "singer": "love",
        "img": "https://images.love-baby.vip/20181010180902.png"
    },
    {
        "name": "童话镇",
        "singer": "陈一发儿",
        "img": "https://images.love-baby.vip/20181010180902.png",
        "src": "https://images.love-baby.vip/a9f34c704fd5a2eb86bf619fa04b7b07.mp3",
        "lrc": "[ti:童话镇]\n[ar:陈一发儿]\n[al:童话镇]\n[by:]\n[offset:0]\n[00:00.00]童话镇\n" +
            "[00:05.00]演唱：陈一发\n" +
            "[00:10.00]作曲 : 暗杠\n" +
            "[00:15.00]作词 : 竹君\n" +
            "[00:22.93]听说白雪公主在逃跑\n" +
            "[00:26.43] 小红帽在担心大灰狼\n" +
            "[00:29.83]听说疯帽喜欢爱丽丝 \n" +
            "[00:33.17]丑小鸭会变成白天鹅\n" +
            "[00:36.34]听说彼得潘总长不大\n" +
            "[00:40.23]杰克他有竖琴和魔法\n" +
            "[00:43.56]听说森林里有糖果屋\n" +
            "[00:46.82]灰姑娘丢了心爱的玻璃鞋\n" +
            "[00:50.39]只有睿智的河水知道\n" +
            "[00:53.68]白雪是因为贪玩跑出了城堡\n" +
            "[00:57.31]小红帽有件抑制自己\n" +
            "[01:00.73]变成狼的大红袍\n" +
            "[01:03.80]总有一条蜿蜒在童话镇里七彩的河\n" +
            "[01:11.00]沾染魔法的乖张气息\n" +
            "[01:14.42]却又在爱里曲折\n" +
            "[01:17.76]川流不息扬起水\n" +
            "[01:20.87]又卷入一帘时光入水\n" +
            "[01:24.68]让全部很久很久以前\n" +
            "[01:28.12]都走到幸福结局的时刻\n" +
            "[01:33.18]music....\n" +
            "[01:47.00]听说睡美人被埋藏\n" +
            "[01:50.44]小人鱼在眺望金殿堂\n" +
            "[01:53.79]听说阿波罗变成金乌\n" +
            "[01:57.12]草原有奔跑的剑齿虎\n" +
            "[02:00.73]听说匹诺曹总说着谎\n" +
            "[02:04.16]侏儒怪拥有宝石满箱\n" +
            "[02:07.57]听说悬崖有颗长生树\n" +
            "[02:10.80]红鞋子不知疲倦地在跳舞\n" +
            "[02:14.43]只有睿智的河水知道\n" +
            "[02:17.84]睡美人逃避了生活的煎熬\n" +
            "[02:21.14]小人鱼把阳光抹成眼影\n" +
            "[02:24.58]投进泡沫的怀抱\n" +
            "[02:27.77]总有一条蜿蜒在童话镇里七彩的河\n" +
            "[02:35.06]沾染魔法的乖张气息\n" +
            "[02:38.43]却又在爱里曲折\n" +
            "[02:41.82]川流不息扬起水花\n" +
            "[02:44.87]又卷入一帘时光入水\n" +
            "[02:48.69]让全部很久很久以前\n" +
            "[02:52.00]都走到幸福结局的时刻\n" +
            "[02:55.46]总有一条蜿蜒在童话镇里梦幻的河\n" +
            "[03:02.47]分隔了理想分隔现实\n" +
            "[03:05.82]又在前方的山口汇合\n" +
            "[03:09.22]川流不息扬起水花\n" +
            "[03:12.36]又卷入一帘时光入水\n" +
            "[03:16.23]让全部很久很久以前\n" +
            "[03:19.38]都走到幸福结局的时刻\n" +
            "[03:22.72]又陌生\n" +
            "[03:24.52]啊~~啊~~啊~~啊~~\n"
    }
]];

function audition(obj, id) {
    $.ajax({
        type: "GET",
        url: host + "/music/audition/" + id,
        headers: {
            'token': $.cookie('token')
        },
        dataType: "json",
        success: function (data) {
            if (data.code == 500) {
                window.location = "/login.html";
            } else if (data.code == 200) {
                if (data.data != null) {

                    var lists = player.getCurrentList(true);
                    var b = false;
                    $.each(lists, function (index, vaule) {
                        for (var index in vaule) {
                            if (vaule[index].name == data.data.name) {
                                b = true;
                            }
                        }
                    });
                    if (!b) {
                        player.addSong(data.data);
                    }
                }
            } else {
                parent.layer.msg(data.message);
            }
        }
    });
}



var modeText = ['顺序播放', '单曲循环', '随机播放', '列表循环'];
var player = null;

function initMusic() {
    player = new MPlayer({
        // 容器选择器名称
        containerSelector: '.mp',
        // 播放列表
        songList: mplayer_song,
        // 专辑图片错误时显示的图片
        defaultImg: 'https://images.love-baby.vip/mplayer_error.png',
        // 自动播放
        autoPlay: true,
        // 播放模式(0->顺序播放,1->单曲循环,2->随机播放,3->列表循环(默认))
        playMode: 0,
        playList: 0,
        playSong: 0,
        // 当前歌词距离顶部的距离
        lrcTopPos: 34,
        // 列表模板，用${变量名}$插入模板变量
        listFormat: '<tr><td>${name}$</td><td>${singer}$</td><td>${time}$</td></tr>',
        // 音量滑块改变事件名称
        volSlideEventName: 'change',
        // 初始音量
        defaultVolume: 80
    }, function () {
        // 绑定事件
        this.on('afterInit', function () {
            console.log('播放器初始化完成，正在准备播放');
        }).on('beforePlay', function () {
            var $this = this;
            var song = $this.getCurrentSong(true);
            var songName = song.name + ' - ' + song.singer;
            console.log('即将播放' + songName + '，return false;可以取消播放');
        }).on('timeUpdate', function () {
            var $this = this;
            // console.log('当前歌词：' + $this.getLrc());
        }).on('end', function () {
            var $this = this;
            var song = $this.getCurrentSong(true);
            var songName = song.name + ' - ' + song.singer;
            console.log(songName + '播放完毕，return false;可以取消播放下一曲');
        }).on('mute', function () {
            var status = this.getIsMuted() ? '已静音' : '未静音';
            console.log('当前静音状态：' + status);
        }).on('changeMode', function () {
            var $this = this;
            var mode = modeText[$this.getPlayMode()];
            $this.dom.container.find('.mp-mode').attr('title', mode);
            console.log('播放模式已切换为：' + mode);
        });
    });
}

initMusic();
$(document.body).append(player.audio); // 测试用
setEffects(player);