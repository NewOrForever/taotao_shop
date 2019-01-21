package com.taotao.item.messageListener;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.taotao.item.pojo.Item;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.TbItemService;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 接收商品添加的消息（id），实现商品详情页的静态化
 * <p>Description：</p>
 * @author： sq
 * @date： 2018年9月30日 下午1:54:06
 *
 */

public class ItemAddMessageListener implements MessageListener {

	@Resource
	private TbItemService tbItemService;
	
	@Resource
	private FreeMarkerConfigurer freeMarkerConfigurer;
	
	@Value("${HTML_OUT_PATH}")
	private String HTML_OUT_PATH;
	
	@Override
	public void onMessage(Message message) {
		try {
			// 获取消息
			TextMessage textMessage = (TextMessage) message;
			String itemIdStr = textMessage.getText();
			long itemId = Long.parseLong(itemIdStr);
			// 等待消息发出方事务提交，商品添加到数据库
			Thread.sleep(1000);	
			// 根据id查询商品
			TbItem tbItem = tbItemService.getItemById(itemId);
			// 子类，扩展了images
			Item item = new Item(tbItem);
			//商品描述
			TbItemDesc itemDesc = tbItemService.getItemDescById(itemId);
			
			/**********网页静态化实现************/
			Configuration configuration = freeMarkerConfigurer.getConfiguration();
			// 模板
			Template template = configuration.getTemplate("item.ftl");
			// 数据集
			Map<String, Object> dataModel = new HashMap<>();
			dataModel.put("item", item);
			dataModel.put("itemDesc", itemDesc);
			// 指定输出路径
			FileWriter out = new FileWriter(new File(HTML_OUT_PATH + itemId + ".html"));
			// 生成静态页面
			template.process(dataModel, out);
			// 关闭输出流
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}