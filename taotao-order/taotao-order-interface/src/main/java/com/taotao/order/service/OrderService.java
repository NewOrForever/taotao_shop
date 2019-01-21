package com.taotao.order.service;

import com.taotao.common.pojo.TaoTaoResult;
import com.taotao.order.pojo.OrderInfo;
import com.taotao.pojo.TbOrder;
import com.taotao.pojo.TbUser;

/**
 * 订单系统服务接口
 * <p>Description：</p>
 * @author： sq
 * @date： 2018年10月17日 下午1:05:59
 *
 */

public interface OrderService {
	/**
	 * 创建订单
	 * @param orderInfo		自定义对象，包装了订单明细和订单收货地址
	 * @param order			订单对象
	 * @param loginUser 	用户
	 * @return
	 */
	TaoTaoResult createOrder(OrderInfo orderInfo, TbOrder order, TbUser loginUser);

}
