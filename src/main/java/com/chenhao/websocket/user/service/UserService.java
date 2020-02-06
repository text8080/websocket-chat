package com.chenhao.websocket.user.service;

import com.chenhao.websocket.user.mapper.UserMapper;
import com.chenhao.websocket.user.pojo.TbUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 通过用户名查找该用户的好友列表
     *
     * @param username
     * @return
     */
    public List<TbUser> listFriends(String username) {
        List<TbUser> users = userMapper.listFriend(username);
        return users;
    }

    /**
     * 通过用户名返回用户,登录使用
     *
     * @param username
     * @return
     */
    public TbUser getUserByUsername(String username) {
        TbUser user = new TbUser();
        user.setUsername(username);
        TbUser resUser = userMapper.selectOne(user);
        if (resUser == null) {
            return null;
        }
        return resUser;
    }

    /**
     * 注册
     *
     * @param user
     * @return
     */
    public Boolean register(TbUser user) {
        if (getUserByUsername(user.getUsername()) != null) {
          return false;
        }
        user.setAvatar("/static.image/avatar/avatar" + new Random().nextInt(10) + ".jpg");
        user.setCreateTime(new Date());
        user.setJoinTime(new Date());
        int insert = userMapper.insert(user);
        if(insert == 0){
            return false;
        }
        return true;
    }


    /**
     * 加好友
     *
     * @param username 当前用户名
     * @param friend   好友的用户名
     * @return
     */
    public boolean addFriend(String username, String friend) {
        System.out.println(username+"==="+friend);

        TbUser user = getUserByUsername(username);

        System.out.println("自己为====>>>>"+user.toString());

        TbUser fUser = getUserByUsername(friend);

        System.out.println("好友为====>>>>"+user.toString());

        if (user == null || fUser == null) {
            return false;
        }else {
            userMapper.insertUserFriend(user.getId(),fUser.getId());
            return true;
        }
    }

    /**
     * 登录
     * @param user
     * @return
     */
    public TbUser login(TbUser user) {
        TbUser resUser = getUserByUsername(user.getUsername());
        if(resUser == null){
            return null;
        }
        if(user.getPassword().equals(resUser.getPassword())){
           return resUser;
        }
        return null;
    }
}
