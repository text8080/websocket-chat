package com.chenhao.websocket.user.controller;


import com.chenhao.websocket.user.pojo.TbUser;
import com.chenhao.websocket.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;


@Controller
@RequestMapping("/api/common")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 注册
     *
     * @return
     */
    @PostMapping(value = "/register")
    @ResponseBody
    public ResponseEntity<Boolean> register(String username, String password, String nickname) {
        TbUser user = new TbUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setNickname(nickname);
        System.out.println("user===>>>" + user);
        Boolean register = userService.register(user);
        return ResponseEntity.ok(register);
    }

    /**
     * 登录
     *
     * @param user
     * @param model
     * @return
     */
    @PostMapping("/login")
    public String login(TbUser user, Model model, HttpSession session) {

        TbUser resUser = userService.login(user);
        if (resUser != null) {
            System.out.println("当前登陆的是===>>>" + resUser.toString());
            model.addAttribute("user", resUser);
            String username = resUser.getUsername();


            // 通过用户名查看该用户的好友列表
            List<TbUser> friends = userService.listFriends(username);
            model.addAttribute("friends", friends);

            session.setAttribute("username",user.getUsername());
            return "chat";
        }
        return "/login";
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<Boolean> add(String username, String friend) {
        System.out.println(username);
        boolean b = userService.addFriend(username, friend);
        return ResponseEntity.ok(b);
    }

}
