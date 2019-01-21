package com.taotao.search.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class GlobalExceptionResolver implements HandlerExceptionResolver {

	private Logger logger = LoggerFactory.getLogger(GlobalExceptionResolver.class);
	
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		logger.info("=================全局异常处理==================");
		// 输出异常到日志文件
		logger.error("系统发生异常==>", ex);
		// 给相关人员发短信
		// 发邮件
		
		// 错误页面展示
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("message", "网络故障请稍后再试！");
		modelAndView.setViewName("/error/exception");
		
		return modelAndView;
	}

}
