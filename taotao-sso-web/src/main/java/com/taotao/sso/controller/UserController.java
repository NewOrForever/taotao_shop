package com.taotao.sso.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaoTaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;

/**
 * 单点登录系统表现层
 * <p>Description：</p>
 * @author： sq
 * @date： 2018年10月10日 下午2:44:25
 *
 */

@Controller
public class UserController {

	@Resource
	private UserService userService;
	
	@Value("${COOKIE_TOKEN_KEY}")
	private String COOKIE_TOKEN_KEY;
	
	/**
	 * 注册检查数据是否可用
	 * @param param 需要检查的参数
	 * @param type  数据类型   1-username   2-phone   3-email
	 * @return
	 */
	@RequestMapping("/user/check/{param}/{type}")
	@ResponseBody
	public TaoTaoResult checkRegisterData(@PathVariable String param, @PathVariable Integer type){
		TaoTaoResult result = userService.checkRegisterData(param, type);
		return result;
	}
	
	/**
	 * 注册用户
	 * @param user
	 * @return
	 */
	@RequestMapping(value="/user/register", method=RequestMethod.POST)
	@ResponseBody
	public TaoTaoResult registerUser(TbUser user){
		TaoTaoResult result = userService.register(user);
		return result;
	}
	
	/**
	 * 用户登录
	 * @param username
	 * @param password
	 * @return
	 */
	@RequestMapping(value="/user/login", method=RequestMethod.POST)
	@ResponseBody
	public TaoTaoResult userLogin(String username, String password, 
			HttpServletRequest request, HttpServletResponse response) {

		TaoTaoResult result = userService.userLogin(username, password);
		// 登录成功，取token，存入cookie
		if(result.getStatus() == 200){
			String token = result.getData().toString();
			// cookie要跨域（二级域名或是域名相同端口号不同）
			CookieUtils.setCookie(request, response, COOKIE_TOKEN_KEY, token);
		}
		
		// 响应数据，包装了token
		return result;
	}
	
	/**
	 * 根据token获取user
	 * @param token
	 * @return
	 */
	/*@RequestMapping(value="/user/token/{token}", 
					// 指定返回响应数据的content-type，不配置的话返回的中文会有乱码
					produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String getUserByToken(@PathVariable String token, String callback){
		TaoTaoResult result = userService.getUserByToken(token);
		// js跨域处理(jsonp)
		// callback(data) 拼接成这样的js语句
		if(StringUtils.isNotBlank(callback)){
			return callback + "(" + JsonUtil.objectToJson(result) + ");";
		}
		return JsonUtil.objectToJson(result);
	}*/
	
	// jsonp处理的第二种方法，spring4.1以上使用
	@RequestMapping(value="/user/token/{token}")
	@ResponseBody
	public Object getUserByToken(@PathVariable String token, String callback){
		TaoTaoResult result = userService.getUserByToken(token);
		// 判断是否是jsonp请求
		if(StringUtils.isNotBlank(callback)){
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
			// 设置回调方法
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		}
		return result;
	}
	
	
	/**
	 * 退出登录
	 * @param token
	 * @return
	 */
	@RequestMapping("/user/logout/{token}")
	public String userLoginOut(@PathVariable String token){
		userService.userLoginOut(token);
		return "redirect:http://localhost:8082";
	}
	
}
