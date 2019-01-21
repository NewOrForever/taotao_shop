package com.taotao.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaoTaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.service.TbItemService;

/**
 * 
 * <p>Description：</p>
 * @author： sq
 * @date： 2018年8月23日 下午1:46:06
 *
 */

@Controller
//@RequestMapping("/item")
public class TbItemController {
	
	@Resource
	private TbItemService tbItemService;
	
	@RequestMapping("/item/{itemId}")
	@ResponseBody
	public TbItem getItemById(@PathVariable Long itemId){
		TbItem item = tbItemService.getItemById(itemId);
		return item;
		
	}
	
	@RequestMapping("/item/list")
	@ResponseBody
	public EasyUIDataGridResult getItemList(Integer page,Integer rows){
		return tbItemService.getItemList(page,rows);
	}
	
	@RequestMapping("/item/save")
	@ResponseBody
	public TaoTaoResult addItem(TbItem item, String desc){
		return tbItemService.addItem(item, desc);
		
	}
	
	@RequestMapping("/rest/item/delete")
	@ResponseBody
	public Map<String, Object> deleteItemByIds(Long[] ids){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		int i = tbItemService.deleteItemByIds(ids);
		if(i > 0){
			resultMap.put("status", 200);
			//resultMap.put();
		}else{
			resultMap.put("status", 0);
		}
		return resultMap;
	}
	
	@RequestMapping("/rest/item/instock")
	@ResponseBody
	public Map<String, Object> instock(Long[] ids){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 需要修改的字段
		TbItem tbItem = new TbItem();
		
		if(ids == null || ids.length == 0){
			resultMap.put("status", 0);
			resultMap.put("message", "未选中要下架的商品！");
			return resultMap;
		}
		
		// 设置需要修改的字段
		tbItem.setStatus((byte) 2);
		tbItem.setUpdated(new Date());
		
		int i = tbItemService.updateItemByIds(ids,tbItem);
		
		// 返回结果
		if(i > 0){
			resultMap.put("status", 200);
		}else{
			resultMap.put("status", 0);
			resultMap.put("message", "商品下架失败！");
		}
		return resultMap;
	} 
	
	@RequestMapping("/rest/item/reshelf")
	@ResponseBody
	public Map<String, Object> reshelf(Long[] ids){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 需要修改的字段
		TbItem tbItem = new TbItem();
		
		if(ids == null || ids.length == 0){
			resultMap.put("status", 0);
			resultMap.put("message", "未选中要上架的商品！");
			return resultMap;
		}
		
		// 设置需要修改的字段
		tbItem.setStatus((byte) 1);
		tbItem.setUpdated(new Date());
		
		int i = tbItemService.updateItemByIds(ids,tbItem);
		
		// 返回结果
		if(i > 0){
			resultMap.put("status", 200);
		}else{
			resultMap.put("status", 0);
			resultMap.put("message", "商品上架失败！");
		}
		return resultMap;
	} 
	
}
