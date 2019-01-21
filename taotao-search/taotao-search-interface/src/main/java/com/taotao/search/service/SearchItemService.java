package com.taotao.search.service;


import com.taotao.common.pojo.SearchResult;
import com.taotao.common.pojo.TaoTaoResult;

public interface SearchItemService {
	/**
	 * 后台导入全部商品到solr索引库
	 * @return 自定义返回对象
	 */
	TaoTaoResult importItemsToSolrIndex();
	
	SearchResult search(String stringQuery, int page, int rows) throws Exception;
	
	
}
