package com.taotao.cart.service;

import java.util.List;

import com.taotao.common.pojo.TaoTaoResult;
import com.taotao.pojo.TbItem;

/**
 * 购物车业务层接口
 * <p>Description：</p>
 * @author： sq
 * @date： 2018年10月13日 下午2:07:27
 *
 */

public interface CartService {

	/**
	 * 合并cookie的购物车到redis 
	 * @param userId         登录用户的id，redis中的购物车的key
	 * @param cartItemList   cookie中获取的购物车
	 */
	TaoTaoResult mergeCart(long userId, List<TbItem> cartItemList);

	/**
	 * 从redis中获取该用户的购物车
	 * @param userId       登录用户的id，redis中的购物车的key
	 * @return			      购物车（商品列表）
	 */
	List<TbItem> getCartItemList(long userId);
	
	/**
	 * 添加商品到redis的购物车
	 * @param userId	用户id，购物车key
	 * @param itemId	要添加商品的id
	 * @param itemNum   要添加商品的数量
	 * @return
	 */
	TaoTaoResult redisAddCart(long userId, long itemId, int itemNum);

	/**
	 * redis购物车修改指定商品的数量
	 * @param userId 		用户id，购物车key		
	 * @param itemId		商品id
	 * @param itemNum		商品数量
	 * @return
	 */
	TaoTaoResult redisUpdateCartItemNum(long userId, long itemId, int itemNum);

	/**
	 * 删除用户购物车中的指定商品
	 * @param id
	 * @param itemId
	 * @return
	 */
	TaoTaoResult redisDeleteCart(long userId, long itemId);
	
}
