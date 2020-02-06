package com.chenhao.websocket.emoji.service;

import com.chenhao.websocket.emoji.mapper.EmojiMapper;
import com.chenhao.websocket.emoji.pojo.TbEmoji;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmojiService {

    @Autowired
    private EmojiMapper emojiMapper;

    public List<TbEmoji> getEmoji() {
        return emojiMapper.selectAll();
    }
}
