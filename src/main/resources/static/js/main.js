var target = "TO_ALL";

var stompClient = null;


function connect() {
    // 建立连接对象（还未发起连接）
    var socket = new SockJS('/any-socket');
    // 获取 STOMP 子协议的客户端对象
    stompClient = Stomp.over(socket);


    // 向服务器发起 Web Socket 连接并发送CONNECT帧
    stompClient.connect({}, function (frame) {
        console.log('发送Connected帧: ' + frame);

        //客户端消息订阅,群发订阅
        stompClient.subscribe('/topic/notice', function (message) {

            var username = (JSON.parse(message.body)).username;
            // console.log(JSON.parse(message.body).username);
            if($("#user_hidden").val() == username){
                showMyMessage(JSON.parse(message.body));
            }else {
                showMessage(JSON.parse(message.body));
            }
        });

        //私发订阅
        stompClient.subscribe('/topic/chat/'+$("#user_hidden").val(), function (message) {
            console.log("接收>>>==="+message);
            showMessage(JSON.parse(message.body));
        });
    });
}

//别人发的
function showMessage(message) {
    $("#messageWindow").append(
        "<div class='message-div row'>"+
        "<div style='display: inline-block; margin-right: 5px;' >"+
        "<img class='media-object f-image img-circle' style='position: absolute' src=" + message.avatar + ">"+
        "</div>"+
        "<div style='margin-left: 55px;width: 40%;'>"+
        "<p class='user-nickname'>" + message.nickname + "</p> <p class='user-time'>"+message.sendTime+"</p><br>"+
        "<div class='message-box-left myMessageView'>" + message.content + "</div>" +
        "</div>" +
        "</div><br>"
    );
}

//自己发的
function showMyMessage(message) {
    $("#messageWindow").append(
        "<div class='my-message-div row'>"+
        "<div style='float: right;height:45px;width: 45px;margin-right: 5px;'>" +
        "<img src=" + message.avatar + " style='height: 43px;width: 43px;margin: 0 auto;border-radius: 22px;'>" +
        "</div>" +
        "<div style='margin-right: 5px;width: 40%;float: right;'>" +
        "<p class='my-user-nickname'>" + message.nickname + "</p> <p class='my-user-time'>"+message.sendTime+"</p><br>" +
        "<div class='message-box-right myMessageView'>" + message.content + "</div>" +
        "</div>" +
        "</div><br>"
    );


}

//发送消息时间前补0
function add0(int){
    if(int < 10)
        return "0"+int;
}

//表情点击
function inputEmoji(emoji){
    var emojiSrc = emoji.src;
    $("#message").append("<img src='"+emojiSrc+"'>");
}


$(function () {
    connect();

    //发送按钮
    $("#send").click(function () {

        console.log($("#message").html());
        var date = new Date();
        var month = date.getMonth()*1+1;


        //发送到全部
        if (target == "TO_ALL"){
            //客户端可使用 send () 方法向服务器发送信息：$("#message").val()
            var chatMessage = {
                username: $("#user_hidden").val(),
                nickname: $("#nickname_hidden").val(),
                avatar: $("#avatar_hidden").val(),
                content: $("#message").html(),
                sendTime: add0(month)+"-"+add0(date.getDate()*1)+" "+date.getHours()+":"+date.getMinutes()
            };
            // var body = $("#user_hidden").val()+"-"+$("#message").html();
            stompClient.send("/app/all", {}, JSON.stringify(chatMessage));
        }else{

            //发送给好友
            //var content = "{'type':'text','content':'" + $("#message").val() + "','toType':'USER','receiver':'"+target+"'}";
            // var body = $("#user_hidden").val()+"-"+target+$("#message").val();
            var baseMessage = {
                type:"text",
                content:$("#message").html(),
                sender:$("#user_hidden").val(),
                toType:"USER",
                receiver:target,
                date:new Date()
            };
            stompClient.send("/app/chat", {}, JSON.stringify(baseMessage));
            var chatMessage = {
                username: baseMessage.sender,
                nickname: $("#nickname_hidden").val(),
                avatar: $("#avatar_hidden").val(),
                content: baseMessage.content,
                sendTime: add0(month)+"-"+add0(date.getDate()*1)+" "+date.getHours()+":"+date.getMinutes()
            };
            showMyMessage(chatMessage);
            console.log(chatMessage);
        }
        $("#message").html("");
        responMessage = null;
    });

    //点击好友
    $(".friend").click(function () {
        target = $(this).find("p.f-username").text();
        var nickname = $(this).find("h4.f-nickname").text();
        $("#target").text(nickname);
        $("#groupBtn").attr("class","btn btn-default");
    });

    //群聊模式按钮
    $("#groupBtn").click(function(){
        $(this).attr("class","btn btn-default disabled");
        target = "TO_ALL";
        $("#target").text("所有人");
    });

    //添加好友
    $("#addBtn").click(function(){
        var friend = $("#friendInput").val();
        var username = $("#user_hidden").val();
        if(friend.length == 0){
            return;
        }
        $.post("/api/common/add",
            {
                "username" : username,
                "friend" : friend
            },
            function(data){
                if(data){
                    window.location.reload();
                }else{
                    alert("用户不存在，添加失败！");
                }
            }
        );
    });

    //检测登陆状态
    $("#loginout").show();
    $("#login").hide();
    $("#loginout").click(function () {

        stompClient.disconnect(function () {
            alert("再见！！")
        })
    });

    //表情包窗口
    $("#emoji-box").hide();

    //小窗口只能开一个
    var emoli_k = false;
    $("#emoji").click(function () {
        emoli_k = !emoli_k;
        if(emoli_k){
            $("#emoji-box").show();

            //$("#emoji-box").load("emoji.html");
            $("#emoji-box").html("");
            //遍历输出表情包
            $.get("/emoji/get-emoji",function (rest) {
                //console.log(rest[1].emoji);
                for(var i = 0;i<rest.length;i++){
                    //var emojiSrc = " \' "+rest[i].emoji+ " \' ";
                    // console.log("<img onclick='inputEmoji("+emojiSrc+")' src="+rest[i].emoji+">");

                    $("#emoji-box").append("<img onclick='inputEmoji(this)' src="+rest[i].emoji+">")
                }
            })
        }else
            $("#emoji-box").hide();
    });

    //表情框失去焦点就关闭
    $("#emoji-box").blur(function () {
        $("#emoji-box").hide();
    })

});
