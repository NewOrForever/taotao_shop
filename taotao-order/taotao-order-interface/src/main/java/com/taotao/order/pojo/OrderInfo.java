package com.taotao.order.pojo;

import java.io.Serializable;
import java.util.List;

import com.taotao.pojo.TbOrderItem;
import com.taotao.pojo.TbOrderShipping;

/**
 * 订单信息，包装了订单明细和订单收货地址
 * <p>Description：</p>
 * @author： sq
 * @date： 2018年10月17日 下午3:17:36
 *
 */

public class OrderInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7249343593283024134L;
	
	private List<TbOrderItem> orderItems;
	private TbOrderShipping orderShipping;
	
	public OrderInfo() {
	}

	public List<TbOrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<TbOrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public TbOrderShipping getOrderShipping() {
		return orderShipping;
	}

	public void setOrderShipping(TbOrderShipping orderShipping) {
		this.orderShipping = orderShipping;
	}

	
}
