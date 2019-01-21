package com.taotao.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.cart.service.CartService;
import com.taotao.common.pojo.TaoTaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.JsonUtil;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbUser;
import com.taotao.service.TbItemService;
import com.taotao.sso.service.UserService;

/**
 * 购物车
 * <p>Description：</p>
 * @author： sq
 * @date： 2018年10月12日 下午3:20:34
 *
 */

@Controller
public class CartController {
	
	@Resource
	private TbItemService tbItemService;
	
	@Resource
	private UserService userService;
	
	@Resource
	private CartService cartService;
	
	@Value("${TT_CART}")
	private String TT_CART;
	
	@Value("${COOKIE_EXPIRE}")
	private int COOKIE_EXPIRE;
	
	@Value("${COOKIE_TOKEN_KEY}")
	private String COOKIE_TOKEN_KEY;

		
	/**
	 * 购物车展示
	 * 1.登录状态时从redis获取购物车，并检查cookie中是否有购物车
	 * 2.有则合并，没有则直接获取redis的购物车
	 * 3.未登录时，直接从cookie中获取购物车
	 * @param request
	 * @return
	 */
	@RequestMapping("/cart/cart")
	public String showCartList(HttpServletRequest request, HttpServletResponse response){
		// 获取cookie中购物车
		List<TbItem> cartItemList = getCartItemListFromCookie(request);
		/**
		 * 1.登录状态时检查cookie中是否有购物车
		 * 2.有则合并，删除cookie
		 * 3.没有则直接获取redis的购物车
		 */
		TbUser loginUser = getLoginUser(getUserToken(request));
		if(loginUser != null){
			// 合并购物车
			cartService.mergeCart(loginUser.getId(), cartItemList);
			// 删除cookie的购物车
			CookieUtils.deleteCookie(request, response, TT_CART);
			// 获取redis中购物车
			cartItemList = cartService.getCartItemList(loginUser.getId());
		}
		
		request.setAttribute("cartList", cartItemList);
		return "cart";
	}
	
	/**
	 * 商品添加购物车
	 * @param itemId 商品id
	 * @param num    商品数量
	 */
	@RequestMapping("/cart/add/{itemId}")
	public String addCart(@PathVariable Long itemId,@RequestParam(defaultValue="1") Integer num, 
			HttpServletRequest request, HttpServletResponse response){
		
		/**
		 * 已登录
		 *  调用服务添加到redis
		 */
		TbUser loginUser = getLoginUser(getUserToken(request));
		if(loginUser != null){
			cartService.redisAddCart(loginUser.getId(), itemId, num);
			// 添加成功，跳转页面
			return "redirect:/cart/cart.html";
		}

		/**未登录
		 * 1.cookie中取出购物车（商品列表）
		 * 2.查询购物车是否有该商品
		 * 3.有则商品数量相加
		 * 4.没有则从数据库中查询该商品
		 * 5.添加到购物车
		 * 6.购物车添加到cookie	
		 */
		List<TbItem> cartItemList = getCartItemListFromCookie(request);
		// 商品是否存在
		boolean flag = false; 
		for (TbItem tbItem : cartItemList) {
			// Long为引用类型，==比较的是地址
			if(tbItem.getId().longValue() == itemId.longValue()){
				tbItem.setNum(tbItem.getNum() + num);
				flag = true;
				break;
			}
		}
		// 商品不在购物车
		if(!flag){
			// 查询数据库
			TbItem item = tbItemService.getItemById(itemId);
			// 设置数量
			item.setNum(num);
			// 取商品图片
			String image = item.getImage();
			if(StringUtils.isNotBlank(image)){
				String[] images = image.split(",");
				item.setImage(images[0]);
			}
			// 添加购物车
			cartItemList.add(item);
		}
		CookieUtils.setCookie(request, response, TT_CART, JsonUtil.objectToJson(cartItemList), COOKIE_EXPIRE, true);
		
		// 跳转到加入购物车成功页面
		return "cartSuccess";
	}
	
	/**
	 * 修改购物车中商品数量
	 * @param itemId 商品id
	 * @param num	 商品数量
	 * @return
	 */
	@RequestMapping("/cart/update/num/{itemId}/{num}")
	@ResponseBody
	public TaoTaoResult updateCartItemNum(@PathVariable Long itemId, @PathVariable Integer num, 
				HttpServletRequest request, HttpServletResponse response){
		/**
		 * 已登录
		 *  调服务修改redis中购物车 
		 */
		TbUser loginUser = getLoginUser(getUserToken(request));
		if(loginUser != null){
			cartService.redisUpdateCartItemNum(loginUser.getId(), itemId, num);
			// 添加成功，跳转页面
			return TaoTaoResult.OK();
		}
		
		/**
		 * 未登录
		 */
		//1.获取购物车
		List<TbItem> cartItemList = getCartItemListFromCookie(request);
		//2.遍历购物车，修改指定商品的数量
		for (TbItem tbItem : cartItemList) {
			if(tbItem.getId().longValue() == itemId.longValue()){
				tbItem.setNum(num);
				break;
			}
		}
		//3.购物车写入cookie
		CookieUtils.setCookie(request, response, TT_CART, JsonUtil.objectToJson(cartItemList), COOKIE_EXPIRE, true);		
		
		return TaoTaoResult.OK();
	}
	
	/**
	 * 删除购物车中的商品
	 * @param itemId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/cart/delete/{itemId}")
	public String deleteCartItem(@PathVariable Long itemId, 
				HttpServletRequest request, HttpServletResponse response){
		/**
		 * 已登录
		 *  调服务删除商品
		 */
		TbUser loginUser = getLoginUser(getUserToken(request));
		if(loginUser != null){
			cartService.redisDeleteCart(loginUser.getId(), itemId);
			// 添加成功，跳转页面
			return "redirect:/cart/cart.html";
		}
		
		/**
		 * 未登录
		 */
		//1.获取购物车
		List<TbItem> cartItemList = getCartItemListFromCookie(request);
		//2.删除指定商品
		for (TbItem tbItem : cartItemList) {
			if(tbItem.getId().longValue() == itemId.longValue()){
				cartItemList.remove(tbItem);
				break;
			}
		}
		//3.写入cookie
		CookieUtils.setCookie(request, response, TT_CART, JsonUtil.objectToJson(cartItemList), COOKIE_EXPIRE, true);
		//4.跳转到cart.jsp
		return "redirect:/cart/cart.html";
	}
	
	/**
	 * 未登录时获取cookie中的购物车
	 * @param request
	 * @return
	 */
	public List<TbItem> getCartItemListFromCookie(HttpServletRequest request){
		String cartItemListJosn = CookieUtils.getCookieValue(request, TT_CART, true);
		if(StringUtils.isNotBlank(cartItemListJosn)){
			// 返回list
			List<TbItem> cartItemList = JsonUtil.jsonToList(cartItemListJosn, TbItem.class);
			return cartItemList;
		}
		// 购物车为空，创建购物车
		return new ArrayList<TbItem>();
	}
	
	// 调用sso服务获取登录用户
	public TbUser getLoginUser(String token){
		TaoTaoResult result = userService.getUserByToken(token);
		if(result.getStatus() == 200){
			return (TbUser) result.getData();
		}
		return null;
	}
	// 从cookie中获取token
	public String getUserToken(HttpServletRequest request){
		String token = CookieUtils.getCookieValue(request, COOKIE_TOKEN_KEY, false);
		return token;
	}
	
	
}
