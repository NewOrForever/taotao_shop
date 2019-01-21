package com.taotao.portal.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.taotao.common.utils.JsonUtil;
import com.taotao.content.service.TbContentService;
import com.taotao.pojo.TbContent;
import com.taotao.portal.pojo.AD1Node;

/**
 * 首页
 * <p>Description：</p>
 * @author： sq
 * @date： 2018年8月29日 下午2:22:58
 *
 */

@Controller
public class IndexController {
	
	@Value("${AD1_CID}")
	private Long AD1_CID; // 大广告id
	
	@Value("${AD1_HEIGHT}")
	private Integer AD1_HEIGHT;
	
	@Value("${AD1_HEIGHTB}")
	private Integer AD1_HEIGHTB;
	
	@Value("${AD1_WIDTH}")
	private Integer AD1_WIDTH;
	
	@Value("${AD1_WIDTHB}")
	private Integer AD1_WIDTHB;
	
	@Resource
	private TbContentService contentService;
	
	@RequestMapping("/index")
	public String showIndex(Model model) throws Exception{
		
		// 获取轮播图内容信息
		List<TbContent> contentList = contentService.getContentListByCatId(AD1_CID);
		// 补全轮播图内容
		ArrayList<AD1Node> AD1List = new ArrayList<>();
		for (TbContent tbContent : contentList) {
			AD1Node ad1Node = new AD1Node();
			ad1Node.setAlt(tbContent.getTitle());
			ad1Node.setHeight(AD1_HEIGHT);
			ad1Node.setHeightB(AD1_HEIGHTB);
			ad1Node.setHref(tbContent.getUrl());
			ad1Node.setSrcB(tbContent.getPic());
			ad1Node.setSrcB(tbContent.getPic2());
			ad1Node.setWidth(AD1_WIDTH);
			ad1Node.setWidthB(AD1_WIDTHB);
			// 添加到内容列表
			AD1List.add(ad1Node);
		}
		
		// 转json
		String ad1ListJson = JsonUtil.objectToJson(AD1List);
		
		model.addAttribute("ad1", ad1ListJson);
		return "index";
	}
}
