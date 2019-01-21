package com.taotao.item.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.taotao.item.pojo.Item;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.TbItemService;

/**
 * 商品详情页
 * <p>Description：</p>
 * @author： sq
 * @date： 2018年9月28日 下午2:28:07
 *
 */

@Controller
public class ItemController {
	
	@Resource
	private TbItemService tbItemService;
	
	@RequestMapping("/item/{itemId}")
	public String showItemDetail(@PathVariable long itemId, Model model){
		// 获取商品
		TbItem tbItem = tbItemService.getItemById(itemId);
		Item item = new Item(tbItem);
		// 获取商品描述
		TbItemDesc itemDesc = tbItemService.getItemDescById(itemId);
		// 存放到域
		model.addAttribute("item", item);
		model.addAttribute("itemDesc", itemDesc);
		
		return "item";
	}
	
	
	
}
