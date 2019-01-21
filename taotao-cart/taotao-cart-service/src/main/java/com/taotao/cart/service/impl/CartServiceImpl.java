package com.taotao.cart.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.cart.service.CartService;
import com.taotao.common.pojo.TaoTaoResult;
import com.taotao.common.utils.JsonUtil;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;

@Service("cartService")
public class CartServiceImpl implements CartService {

	@Resource
	private TbItemMapper tbItemMapper;
	
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${REDIS_CART_PREFIX}")
	private String REDIS_CART_PREFIX;
	
	@Override
	public TaoTaoResult mergeCart(long userId, List<TbItem> cartItemList) {
		/**
		 * 业务逻辑分析：
		 * 		相当于将一个商品列表添加到购物车中
		 * 实现：
		 * 	1.遍历商品列表
		 *  2.添加商品到redis购物车
		 *  
		 * 当cartItemList为空时，foreach不会走，此时不会添加到redis，直接返回，所以购物车这里不需要判断是否为空
		 */
		for (TbItem tbItem : cartItemList) {
			redisAddCart(userId, tbItem.getId(), tbItem.getNum());
		}
		
		return TaoTaoResult.OK();
	}

	@Override
	public List<TbItem> getCartItemList(long userId) {
		/**
		 * 1.购物车key为REDIS_CART_PREFIX:userId，redis中存储的数据类型为hash
		 * 2.根据key取出hash
		 * 3.没有数据，则创建购物车（list）
		 */
		// 商品列表，存的是itemJson
		List<String> cartItemJsonList = jedisClient.hvals(REDIS_CART_PREFIX + ":" + userId);
		// 创建购物车
		ArrayList<TbItem> cartItemList = new ArrayList<>();
		
		// 遍历，json转对象
		for (String cartItemJson : cartItemJsonList) {
			TbItem item = JsonUtil.jsonToPoJo(cartItemJson, TbItem.class);
			// 添加到购物车
			cartItemList.add(item);
		}
		
		return cartItemList;
	}

	@Override
	public TaoTaoResult redisAddCart(long userId, long itemId, int itemNum) {
		
		/**
		 * 1.redis判断商品是否存在
		 * 2.有则商品数量相加
		 * 3.没有则从数据库中查询该商品
		 * 4.添加到redis
		 */
		//List<TbItem> cartItemList = getCartItemList(userId); // 不推荐
		/*for (TbItem tbItem : cartItemList) {
			// Long为引用类型,引用类型时  ==比较的是地址
			if(tbItem.getId().longValue() == itemId){
				tbItem.setNum(tbItem.getNum() + itemNum);
				flag = true;
				break;
			}
		}*/
		TbItem item = null;
		// 商品是否存在
		Boolean flag = jedisClient.hexists(REDIS_CART_PREFIX + ":" + userId, itemId + "");
		if(flag){
			// redis购物车中存在该商品
			// 获取该商品，修改数量
			String itemJson = jedisClient.hget(REDIS_CART_PREFIX + ":" + userId, itemId + "");
			item = JsonUtil.jsonToPoJo(itemJson, TbItem.class);
			item.setNum(item.getNum() + itemNum);
			// 写回redis
			jedisClient.hset(REDIS_CART_PREFIX + ":" + userId, itemId + "", JsonUtil.objectToJson(item));
			
			return TaoTaoResult.OK();
		}
		// 商品不在购物车
		// 查询数据库,如果不存在,根据商品id取商品信息,服务层尽量别相互调用
		// 这里cart-service拉出去做服务，别的系统取购物车数据时，取得就是数据库（redis）中的数据
		item = tbItemMapper.selectByPrimaryKey(itemId);
		if(item == null){
			return TaoTaoResult.OK();
		}
		// 设置数量
		item.setNum(itemNum);
		// 取商品图片
		String image = item.getImage();
		if(StringUtils.isNotBlank(image)){
			String[] images = image.split(",");
			item.setImage(images[0]);
		}
		// 添加到redis
		jedisClient.hset(REDIS_CART_PREFIX + ":" + userId, itemId + "", JsonUtil.objectToJson(item));
	
		return TaoTaoResult.OK();
	}

	@Override
	public TaoTaoResult redisUpdateCartItemNum(long userId, long itemId, int itemNum) {
		/**
		 *  1.从redis获取该用户购物车中的该商品
		 *  2.修改商品的数量
		 *  3.写回redis
		 */
		String itemJson = jedisClient.hget(REDIS_CART_PREFIX + ":" + userId, itemId + "");
		TbItem tbItem = JsonUtil.jsonToPoJo(itemJson, TbItem.class);
		tbItem.setNum(itemNum);
		jedisClient.hset(REDIS_CART_PREFIX + ":" + userId, itemId + "", JsonUtil.objectToJson(tbItem));
		
		return TaoTaoResult.OK();
	}

	@Override
	public TaoTaoResult redisDeleteCart(long userId, long itemId) {
		// redis删除key的field
		jedisClient.hdel(REDIS_CART_PREFIX + ":" + userId, itemId + "");
		return TaoTaoResult.OK();
	}
}
