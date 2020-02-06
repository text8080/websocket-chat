package com.chenhao.websocket.pageController;

import com.chenhao.websocket.user.controller.UserController;
import com.chenhao.websocket.user.pojo.TbUser;
import com.chenhao.websocket.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 *
 * Created by Silence on 2017/4/21.
 */
@Controller
public class PageController {

	@Autowired
	private UserService userService;

	/**
	 * index页面
	 * @return
	 */
	@GetMapping(value = "/")
	public String index() {
		return "index";
	}

	/**
	 * 返回聊天页
	 * @param  user 当前用户
	 * @param model
	 * @return
	 */
	/*@GetMapping(value = "/chat")
	public String chat(TbUser user, Model model) {
		System.out.println("当前登陆的是===>>>"+user.toString());
		model.addAttribute("user", user);
		String username = user.getUsername();

		// 通过用户名查看该用户的好友列表
		List<TbUser> friends = userService.listFriends(username);
		model.addAttribute("friends", friends);
		return "chat";
	}*/

	/**
	 * 返回登录页
	 * @return
	 */
	@GetMapping(value = "/pagelogin")
	public String login() {
		return "login";
	}

	/**
	 * 返回注册页
	 * @return
	 */
	@GetMapping(value = "/register")
	public String register() {
		return "register";
	}

}
