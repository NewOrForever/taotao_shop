package com.taotao.controller;


import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.content.service.TbContentService;

/**
 * 内容管理系统后台显示
 * <p>Description：内容列表</p>
 * @author： sq
 * @date： 2018年8月31日 下午3:49:03
 *
 */

@RestController
@RequestMapping("/content")
public class TbContentController {
	
	@Resource
	private TbContentService tbContentService;

	@RequestMapping("/query/list")
	public EasyUIDataGridResult getContentList(Integer page, Integer rows, Long categoryId){
		EasyUIDataGridResult result = tbContentService.getContentList(page, rows, categoryId);
		return result;
	}
}
