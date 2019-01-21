package com.taotao.item.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * 全局异常处理器
 * <p>Description：</p>
 * @author： sq
 * @date： 2018年9月28日 下午3:57:21
 *
 */

public class GlobalExceptionResolver implements HandlerExceptionResolver {

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		Logger logger = LoggerFactory.getLogger(GlobalExceptionResolver.class);
		logger.info("=================开始处理异常===================");
		logger.error("*****************系统出现的异常信息为：", ex);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("error/exception");
		// 全局处理的异常信息可以放在一个配置文件
		modelAndView.addObject("message", "网络异常，请检查您的网络！");
		
		return modelAndView;
	}

}
