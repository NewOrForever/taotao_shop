package com.taotao.order.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.taotao.cart.service.CartService;
import com.taotao.common.pojo.TaoTaoResult;
import com.taotao.order.pojo.OrderInfo;
import com.taotao.order.service.OrderService;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbOrder;
import com.taotao.pojo.TbUser;

/**
 * 订单系统表现层
 * <p>Description：</p>
 * @author： sq
 * @date： 2018年10月17日 下午1:10:48
 *
 */

@Controller
public class OrderCartController {
	
	@Resource
	private CartService cartService;
	@Resource
	private OrderService orderService;
	
	@Value("${SSO_LOGIN_URL}")
	private String SSO_LOGIN_URL;
	
	/**
	 * 显示订单结算页面
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/order/order-cart")
	public String showOrderCart(HttpServletRequest request, Model model){
		/**
		 *  显示订单结算页面
		 *    1.登录拦截器，已登录才能进入
		 *    2.未登录跳到登录页，带上当前请求到订单页的URL，实现登录后跳转到改URL
		 *    3.订单展示页面
		 *      a) 必须是登录状态，取用户id
		 *      b) 根据用户id取用户收货地址列表
		 *      c) 根据用户id从redis中取出该用户的购物车列表
		 */
		System.out.println(request.getRequestURL().toString());
		// 1.取拦截器中的保存的user
		TbUser loginUser = (TbUser) request.getAttribute("loginUser");
		if(loginUser == null){
			return "redirect:" + SSO_LOGIN_URL +"/page/login/html?url=" + request.getRequestURL();
		}
		// 2.获取购物车列表
		List<TbItem> cartItemList = cartService.getCartItemList(loginUser.getId());
		
		model.addAttribute("cartList", cartItemList);
		return "order-cart";
	}
	
	/**
	 * 提交订单
	 * @param orderInfo  自定义对象，包装了订单明细和订单收货地址
	 * @param order		  订单对象
	 * @return			  跳转到订单提交成功页面
	 */
	@RequestMapping("/order/create")
	public String createOrder(OrderInfo orderInfo, TbOrder order, 
			HttpServletRequest request, Model model){
		
		System.out.println(request.getRequestURL().toString());
		// 1.取拦截器中的保存的user
		TbUser loginUser = (TbUser) request.getAttribute("loginUser");
		if(loginUser == null){
			return "redirect:" + SSO_LOGIN_URL +"/page/login/html?url=" + request.getRequestURL();
		}
		// 2.创建订单
		TaoTaoResult result = orderService.createOrder(orderInfo, order, loginUser);
		
		// 3.返回订单号，金额，时间
		model.addAttribute("orderId", result.getData().toString());
		model.addAttribute("payment", order.getPayment());
		// 当前时间加3天
		DateTime dateTime = new DateTime();
		dateTime = dateTime.plusDays(3);
		model.addAttribute("date", dateTime.toString("yyyy-MM-dd"));
		return "success";
	}
	
	/*public TbUser getLoginUser(HttpServletRequest request, HttpServletResponse response) {
		TbUser loginUser = (TbUser) request.getAttribute("loginUser");
		if(loginUser == null){
			//return "redirect:" + SSO_LOGIN_URL +"/page/login/html?url=" + request.getRequestURL();
			try {
				response.sendRedirect(SSO_LOGIN_URL +"/page/login/html?url=" + request.getRequestURL());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		return loginUser;
		
	}*/
	
	
}
