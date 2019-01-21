package com.taotao.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaoTaoResult;
import com.taotao.content.service.TbContentCategoryService;
import com.taotao.pojo.TbContentCategory;

/**
 * 内容管理系统后台显示
 * <p>Description：内容分类</p>
 * @author： sq
 * @date： 2018年8月29日 下午3:48:36
 *
 */

@Controller
@RequestMapping("/contentCat")
public class TbContentCategoryController {
	
	@Resource
	private TbContentCategoryService contentCatService;

	@RequestMapping("/category/list")
	@ResponseBody
	public List<EasyUITreeNode> getContentCategory(@RequestParam(value="id", defaultValue="0") Long parentId){
		List<EasyUITreeNode> result = contentCatService.getContentCatByPId(parentId);
		return result;
	} 
	
	@RequestMapping("/category/create")
	@ResponseBody
	public TaoTaoResult addContentCategory(Long parentId, String name){
		TaoTaoResult result = contentCatService.addContentCategory(parentId, name);
		return result;
	}
	
	@RequestMapping("/category/update")
	public void updateContentCategory(TbContentCategory tbContentCategory){
		tbContentCategory.setUpdated(new Date());
		contentCatService.updateContentCategoryById(tbContentCategory);
	}
	
	@RequestMapping("/category/delete/")
	@ResponseBody
	public TaoTaoResult deleteContentCategory(Long parentId, Long id){
		TaoTaoResult result = contentCatService.deleteContentCategoryById(parentId, id);
		return result;
	}
}
