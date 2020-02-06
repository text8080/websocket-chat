package com.chenhao.websocket.chat.Model;

import lombok.Data;


@Data
public class ChatMessage {

  private String username;

  private String nickname;

  private String avatar;

  private String content;

  private String sendTime;

}
