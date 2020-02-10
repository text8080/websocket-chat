package com.chenhao.websocket.chat;

import com.alibaba.fastjson.JSON;
import com.chenhao.websocket.chat.Model.BaseMessage;
import com.chenhao.websocket.chat.Model.ChatMessage;
import com.chenhao.websocket.user.pojo.TbUser;
import com.chenhao.websocket.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Controller
public class ChatController {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm");

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private StringRedisTemplate redisTemplate;  //redis模板


    @Autowired
    private UserService userService;

    /**
     * 群聊
     *
     * @param baseMessage
     */
    @MessageMapping("/all")
    public void all(@RequestParam(value = "baseMessage") BaseMessage baseMessage) {

        //System.out.println("要发送到群聊的数据==>>" + baseMessage.toString());
        if (baseMessage.getType().equals("cache")) {
            System.out.println("消息缓存===>>"+baseMessage.toString());

            // 存储数据
            this.redisTemplate.opsForList().rightPush(baseMessage.getUuid(),baseMessage.getContent());
        }
        if (baseMessage.getType().equals("trigger")) {
            System.out.println("分段合并！");
            List<String> strings = redisTemplate.opsForList()
                    .range(baseMessage.getUuid(), 0, redisTemplate.opsForList().size(baseMessage.getUuid()));
            this.redisTemplate.delete(baseMessage.getUuid());

            String content = "";

            for (String s : strings) {
                content += s;
            }

            System.out.println("======>>>>"+content);
            ChatMessage chatMessage2 = createMessage(baseMessage.getSender(), content);
            template.convertAndSend("/topic/notice", JSON.toJSONString(chatMessage2));
        }


        /*ChatMessage chatMessage2 = createMessage(baseMessage.getSender(), baseMessage.getContent());
        template.convertAndSend("/topic/notice", JSON.toJSONString(chatMessage2));*/
    }

    /**
     * 单聊
     *
     * @param baseMessage
     */
    @MessageMapping("/chat")
    public void chat(@RequestParam(value = "baseMessage") BaseMessage baseMessage) {
//		BaseMessage baseMessage = JSON.parseObject(message, BaseMessage.class);
//		baseMessage.setSender(user.getUsername());
        System.out.println("发送内容" + baseMessage.toString());

        ChatMessage chatMessage = createMessage(baseMessage.getSender(), baseMessage.getContent());
        template.convertAndSend("/topic/chat/" + baseMessage.getReceiver(), JSON.toJSONString(chatMessage));


        //this.send(baseMessage);
    }

    @Async
    private void send(BaseMessage message) {
        message.setDate(new Date());
        ChatMessage chatMessage = createMessage(message.getSender(), message.getContent());

        //接收的用户，订阅地址，消息
        //template.convertAndSendToUser(message.getReceiver(), "/topic/chat", JSON.toJSONString(chatMessage));
        template.convertAndSend("/topic/notice/" + message.getReceiver(), JSON.toJSONString(chatMessage));
    }

    /**
     * 创建消息
     *
     * @param username
     * @param message
     * @return
     */
    private ChatMessage createMessage(String username, String message) {
        ChatMessage chatMessage = new ChatMessage();
        System.out.println("发送的用户===》》》" + username);

        chatMessage.setUsername(username);
        TbUser user = userService.getUserByUsername(username);
        System.out.println("用户详情" + user.toString());

        chatMessage.setAvatar(user.getAvatar());
        chatMessage.setNickname(user.getNickname());
        chatMessage.setContent(message);
        chatMessage.setSendTime(simpleDateFormat.format(new Date()));
        System.out.println("消息实体===》》》" + chatMessage.toString());
        return chatMessage;
    }

}
