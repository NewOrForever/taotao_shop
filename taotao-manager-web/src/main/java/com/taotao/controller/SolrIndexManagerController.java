package com.taotao.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaoTaoResult;
import com.taotao.search.service.SearchItemService;

/**
 * solr索引库后台
 * <p>Description：</p>
 * @author： sq
 * @date： 2018年9月17日 上午10:43:57
 *
 */

@Controller
@RequestMapping("/solrIndex")
public class SolrIndexManagerController {
	
	@Resource
	private SearchItemService searchItemService;
	
	@RequestMapping("/import")
	@ResponseBody
	public TaoTaoResult importItemsToSolrIndex(){
		return searchItemService.importItemsToSolrIndex();
	}
	
}
