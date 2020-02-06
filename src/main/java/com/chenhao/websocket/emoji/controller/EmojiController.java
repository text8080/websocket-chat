package com.chenhao.websocket.emoji.controller;

import com.chenhao.websocket.emoji.pojo.TbEmoji;
import com.chenhao.websocket.emoji.service.EmojiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/emoji")
public class EmojiController {
    @Autowired
    private EmojiService emojiService;

    /**
     * 返回所有表情包
     * @return
     */
    @GetMapping("/get-emoji")
    public ResponseEntity<List<TbEmoji>> getEmoji(){
        List<TbEmoji> emojis = emojiService.getEmoji();
        return ResponseEntity.ok(emojis);
    }
}
