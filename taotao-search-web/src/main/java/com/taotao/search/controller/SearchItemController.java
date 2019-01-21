package com.taotao.search.controller;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.common.pojo.SearchResult;
import com.taotao.search.service.SearchItemService;

/**
 * solr索引搜索表现层
 * <p>Description：</p>
 * @author： sq
 * @date： 2018年9月18日 上午9:23:36
 *
 */

@Controller
public class SearchItemController {
	@Resource
	private SearchItemService searchItemService;
	
	@Value("${rows}")
	private Integer rows;
	
	
	@RequestMapping("/search")
	public String search(@RequestParam(value="q")String stringQuery, 
			@RequestParam(defaultValue="1") Integer page, Model model) throws Exception{
		//int a = 1/0; // 全局异常测试
		// get请求乱码处理
		stringQuery = new String(stringQuery.getBytes("iso8859-1"), "utf-8");
		SearchResult result = searchItemService.search(stringQuery, page, rows);
		model.addAttribute("query", stringQuery);
		model.addAttribute("totalPages", result.getTotalPages());
		model.addAttribute("page", page);
		model.addAttribute("itemList", result.getItemList());
		return "search";
	}
}
