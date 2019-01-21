package com.taotao.search.messageListener;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import com.taotao.common.pojo.SearchItem;
import com.taotao.search.mapper.SearchItemMapper;

/**
 * 商品添加，接收商品id消息，同步索引库
 * <p>Description：</p>
 * @author： sq
 * @date： 2018年9月27日 下午2:21:23
 *
 */

public class ItemAddMessageListener implements MessageListener {
	
	@Autowired
	private SolrClient solrServer;

	@Autowired
	private SearchItemMapper searchItemMapper;
	
	@Override
	public void onMessage(Message message) {
		try {
				// 接收消息
				TextMessage textMessage = (TextMessage) message;
				long itemId = Long.parseLong(textMessage.getText());
				/************添加商品到索引库*************/
				// 创建文档
				SolrInputDocument document = new SolrInputDocument();
				// 等待manager-service事务提交，商品入到数据库
				// 或者在manager的表现层去发送消息
				Thread.sleep(1000);
				// 文档添加域
				SearchItem searchItem = searchItemMapper.getItemById(itemId);
				document.addField("id", searchItem.getId());
				document.addField("item_title", searchItem.getTitle());
				document.addField("item_sell_point", searchItem.getSell_point());
				document.addField("item_price", searchItem.getPrice());
				document.addField("item_image", searchItem.getImage());
				document.addField("item_category_name", searchItem.getCategory_name());
				document.addField("item_desc", searchItem.getItem_desc());
				// 文档添加到索引库
				solrServer.add(document);
				// 提交
				solrServer.commit();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
