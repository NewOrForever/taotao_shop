package com.taotao.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.service.TbItemCatService;

/**
 * 
 * <p>Description：商品类目</p>
 * @author： sq
 * @date： 2018年8月24日 下午3:34:15
 *
 */

@RestController // ==> @Controller + @ResponseBody
@RequestMapping("/item/cat")
public class TbItemCatController {
	@Resource
	private TbItemCatService tbItemCatService;
	
	@RequestMapping("/list")
	public List<EasyUITreeNode> getItemCatByParentId(@RequestParam(name="id", defaultValue="0 ") Long parentId){
		return tbItemCatService.getItemCatByParentId(parentId);
		
	}
}	
