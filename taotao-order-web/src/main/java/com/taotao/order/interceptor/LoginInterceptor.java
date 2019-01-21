package com.taotao.order.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.common.pojo.TaoTaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.sso.service.UserService;

/**
 * 登录拦截器
 * <p>Description：</p>
 * @author： sq
 * @date： 2018年10月17日 下午2:08:49
 *
 */

public class LoginInterceptor implements HandlerInterceptor {

	@Resource
	private UserService userService;
	
	@Value("${COOKIE_TOKEN_KEY}")
	private String COOKIE_TOKEN_KEY;
	
	@Value("${SSO_LOGIN_URL}")
	private String SSO_LOGIN_URL;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		/**
		 * 1.获取cookie中用户的token
		 * 2.调用sso服务根据token获取user
		 * 3.user保存到request
		 */
		// 当前请求URL
		String url = request.getRequestURL().toString();
		System.out.println("==========拦截器==============" + url);
		
		String token = CookieUtils.getCookieValue(request, COOKIE_TOKEN_KEY);
		// cookie过期
		if(StringUtils.isBlank(token)){
			// 跳转登录页
			response.sendRedirect(SSO_LOGIN_URL + "/page/login.html?redirectUrl=" + url);
			return false;
		}
		
		TaoTaoResult result = userService.getUserByToken(token);
		// token过期
		if(result.getStatus() != 200){
			// 跳转登录页
			response.sendRedirect(SSO_LOGIN_URL + "/page/login.html?redirectUrl=" + url);
			return false;
		}
		
		// 登录，保存用户信息，放行
		request.setAttribute("loginUser", result.getData());
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
