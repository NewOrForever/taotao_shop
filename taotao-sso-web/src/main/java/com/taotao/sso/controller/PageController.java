package com.taotao.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 页面显示
 * <p>Description：</p>
 * @author： sq
 * @date： 2018年10月11日 下午2:45:42
 *
 */

@Controller
public class PageController {
	
	@RequestMapping("/page/login")
	public String showLogin(String redirectUrl, Model model){
		model.addAttribute("redirect", redirectUrl);
		return "login";
	}
	
	@RequestMapping("/page/register")
	public String showRegister(){
		return "register";
	}
}
