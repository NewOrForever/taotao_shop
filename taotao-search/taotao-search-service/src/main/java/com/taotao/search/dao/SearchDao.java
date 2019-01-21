package com.taotao.search.dao;

import org.apache.solr.client.solrj.SolrQuery;

import com.taotao.common.pojo.SearchResult;

/**
 * 索引库查询
 * <p>Description：</p>
 * @author： sq
 * @date： 2018年9月17日 下午3:29:32
 *
 */

public interface SearchDao {
	/**
	 * 根据查询对象执行查询
	 * @param solrQuery 查询对象（设置查询条件、过滤条件、搜索域、分页条件、排序条件、高亮等）
	 * @throws Exception 
	 */
	public SearchResult search(SolrQuery solrQuery) throws Exception;
}
