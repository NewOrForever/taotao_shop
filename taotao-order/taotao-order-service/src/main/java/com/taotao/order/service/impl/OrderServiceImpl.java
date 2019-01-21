package com.taotao.order.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaoTaoResult;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbOrderItemMapper;
import com.taotao.mapper.TbOrderMapper;
import com.taotao.mapper.TbOrderShippingMapper;
import com.taotao.order.pojo.OrderInfo;
import com.taotao.order.service.OrderService;
import com.taotao.pojo.TbOrder;
import com.taotao.pojo.TbOrderItem;
import com.taotao.pojo.TbOrderShipping;
import com.taotao.pojo.TbUser;

/**
 * 订单服务接口实现
 * <p>Description：</p>
 * @author： sq
 * @date： 2018年10月17日 下午1:07:07
 *
 */

@Service("orderService")
public class OrderServiceImpl implements OrderService {

	@Autowired
	private JedisClient jedisClient;
	
	@Resource
	private TbOrderMapper tbOrderMapper;
	@Resource
	private TbOrderItemMapper tbOrderItemMapper;
	@Resource
	private TbOrderShippingMapper tbOrderShippingMapper;
	
	@Value("${ORDER_ID_GEN_KEY}")
	private String ORDER_ID_GEN_KEY;
	
	@Value("${ORDER_ID_BEGIN_VALUE}")
	private String ORDER_ID_BEGIN_VALUE;

	@Value("${ORDER_ITEM_ID_GEN_KEY}")
	private String ORDER_ITEM_ID_GEN_KEY;
	
	
	@Override
	public TaoTaoResult createOrder(OrderInfo orderInfo, TbOrder order, TbUser loginUser) {
		/**
		 * 1.补全订单，添加到数据库
		 * 2.orderinfo中取订单明细列表和订单收货地址
		 * 3.保存订单明细和地址到数据库
		 */
		//1.订单
		// 订单id使用redis的incr，需设置初始值
		if(!jedisClient.exists(ORDER_ID_GEN_KEY)){
			jedisClient.set(ORDER_ID_GEN_KEY, ORDER_ID_BEGIN_VALUE);
		}
		String orderId = jedisClient.incr(ORDER_ID_GEN_KEY).toString();
		order.setOrderId(orderId);
		order.setBuyerNick(loginUser.getUsername());
		order.setUserId(loginUser.getId());
		order.setCreateTime(new Date());
		order.setUpdateTime(new Date());
		order.setStatus(1);
		// 添加数据库
		tbOrderMapper.insert(order);
		
		// 2.订单明细、收货地址
		// 明细
		List<TbOrderItem> orderItems = orderInfo.getOrderItems();
		for (TbOrderItem tbOrderItem : orderItems) {
			tbOrderItem.setOrderId(orderId);
			tbOrderItem.setId(jedisClient.incr(ORDER_ITEM_ID_GEN_KEY).toString());
			// 添加到数据库
			tbOrderItemMapper.insert(tbOrderItem);
		}
		// 地址
		TbOrderShipping orderShipping = orderInfo.getOrderShipping();
		orderShipping.setOrderId(orderId);
		orderShipping.setCreated(new Date());
		orderShipping.setUpdated(new Date());
		// 数据库
		tbOrderShippingMapper.insert(orderShipping);
		
		return TaoTaoResult.OK(orderId);
	}

}
