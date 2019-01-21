package com.taotao.service.impl;


import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaoTaoResult;
import com.taotao.common.utils.IdUtil;
import com.taotao.common.utils.JsonUtil;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemExample;
import com.taotao.pojo.TbItemExample.Criteria;
import com.taotao.service.TbItemService;

@Service("tbItemService")
public class TbItemServiceImpl implements TbItemService {

	@Autowired
	private TbItemMapper tbItemMapper;

	@Autowired
	private TbItemDescMapper tbItemDescMapper;
	
	// 通过类型来注入，单机版和集群版实现该接口，当切换单机，集群的时候只需要修改配置文件就可以了，不需要改代码了
	@Autowired	
	private JedisClient jedisClient;
	
	@Resource
	private JmsTemplate jmsTemplate;
	
	@Resource(name="itemAddTopic")
	private Destination destination;
	
	@Value("${ITEM_INFO}")
	private String ITEM_INFO; 
	@Value("${BASE}")
	private String BASE;
	@Value("${DESC}")
	private String DESC;
	
	@Value("${EXPIRE_TIME}")
	private int EXPIRE_TIME;
	
	
	// 添加redis缓存
	@Override
	public TbItem getItemById(long id) {
		// 读取redis缓存
		// 有查询到则调用缓存
		try {
			String json = jedisClient.get(ITEM_INFO + ":" + id + ":" + BASE);
			if(StringUtils.isNotBlank(json)){
				// json转pojo
				TbItem tbItem = JsonUtil.jsonToPoJo(json, TbItem.class);
				return tbItem;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 没有查询到则查询数据库
		TbItem tbItem = tbItemMapper.selectByPrimaryKey(id);
		// 将查询到的数据存入redis，并设置过期时间提高缓存利用率
		try {
			jedisClient.set(ITEM_INFO + ":" + id + ":" + BASE, JsonUtil.objectToJson(tbItem));
			jedisClient.expire(ITEM_INFO + ":" + id + ":" + BASE, EXPIRE_TIME);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tbItem;
	}
	
	@Override
	public TbItemDesc getItemDescById(long id) {
		// 读取redis缓存
		// 有查询到则调用缓存
		try {
			String json = jedisClient.get(ITEM_INFO + ":" + id + ":" + DESC);
			if(StringUtils.isNotBlank(json)){
				// json转pojo
				TbItemDesc tbItemDesc = JsonUtil.jsonToPoJo(json, TbItemDesc.class);
				return tbItemDesc;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 没有查询到则查询数据库
		TbItemDesc tbItemDesc = tbItemDescMapper.selectByPrimaryKey(id);
		// 将查询到的数据存入redis，并设置过期时间提高缓存利用率
		try {
			jedisClient.set(ITEM_INFO + ":" + id + ":" + DESC, JsonUtil.objectToJson(tbItemDesc));
			jedisClient.expire(ITEM_INFO + ":" + id + ":" + DESC, EXPIRE_TIME);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tbItemDesc;
	}

	@Override
	public EasyUIDataGridResult getItemList(int pageNum, int pageSize) {
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		// 执行查询
		TbItemExample example = new TbItemExample();
		List<TbItem> itemList = tbItemMapper.selectByExample(example);
		// 获取查询结果
		PageInfo<TbItem> pageInfo = new PageInfo<TbItem>(itemList);
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setTotal(pageInfo.getTotal());
		result.setRows(itemList);
		
		return result;
	}

	@Override
	public int deleteItemByIds(Long[] ids) {
		List<Long> idList = Arrays.asList(ids);
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdIn(idList);
		TbItem tbItem = new TbItem();
		tbItem.setStatus((byte) 3);
		tbItem.setUpdated(new Date());
		int r = tbItemMapper.updateByExampleSelective(tbItem, example);
		return r;
	}

	@Override
	public int updateItemByIds(Long[] ids, TbItem tbItem) {
		List<Long> idList = Arrays.asList(ids);
		// 设置查询参数
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdIn(idList);
		
		// 执行查询
		int r = tbItemMapper.updateByExampleSelective(tbItem, example);
		return r;
	}

	@Override
	public TaoTaoResult addItem(TbItem item, String itemDesc) {
		// 商品id,发送消息时用final来修饰
		final long itemId = IdUtil.genItemId();
		// 补全商品信息
		item.setId(itemId);
		// 测试用，搭建图片服务器后就注释掉
		item.setImage("C:/Users/Administrator/Desktop/images/m_03.jpg");
		item.setStatus((byte) 1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		// 添加商品
		tbItemMapper.insert(item);
		
		// 补全商品描述
		TbItemDesc tbItemDesc = new TbItemDesc();
		tbItemDesc.setItemId(itemId);
		tbItemDesc.setItemDesc(itemDesc);
		tbItemDesc.setCreated(new Date());
		tbItemDesc.setUpdated(new Date());
		// 添加商品描述
		tbItemDescMapper.insert(tbItemDesc);
		
		/************同步索引库************/
		// 消息队列发送消息
		jmsTemplate.send(destination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(itemId + "");
			}
		});
		
		return TaoTaoResult.OK();
	}

	


	
}
