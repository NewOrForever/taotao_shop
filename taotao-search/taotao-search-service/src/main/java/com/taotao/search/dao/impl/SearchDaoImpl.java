package com.taotao.search.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.stereotype.Repository;

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.SearchResult;
import com.taotao.search.dao.SearchDao;

@Repository("searchDao")
public class SearchDaoImpl implements SearchDao {

	@Resource
	private SolrClient solrServer;
	
	@Override
	public SearchResult search(SolrQuery solrQuery) throws Exception {
		
		SearchResult searchResult = new SearchResult();
		ArrayList<SearchItem> searchItemList = new ArrayList<>();
		/**************************************
		 * 执行查询、获取查询结果、取高亮显示
		 **************************************/
		// 执行查询
		QueryResponse queryResponse = solrServer.query(solrQuery);
		// 获取查询结果
		SolrDocumentList solrDocumentList = queryResponse.getResults();
		// 总记录数
		searchResult.setRecordCount(solrDocumentList.getNumFound());
		// 迭代器
		ListIterator<SolrDocument> solrDocIteator = solrDocumentList.listIterator();
		while (solrDocIteator.hasNext()) {
			SolrDocument solrDocument = solrDocIteator.next();
			SearchItem searchItem = new SearchItem();
			/***** 取高亮显示*****/
			// 1.判断是否设置了高亮
			/*Map<String, Map<String, List<String>>> hlMap = queryResponse.getHighlighting();
			Map<String, List<String>> map = hlMap.get(solrDocument.get("id"));
			if(map != null && map.size() > 0){ 
				// 2.已设置高亮，取出设置的要查询高亮的域
				for (Map.Entry<String, List<String>> entry : map.entrySet()) {
					List<String> hlList = entry.getValue();
					String hf = entry.getKey();
					if(hlList != null && hlList.size() > 0){
						// 3.查询到高亮，取值
						hf = hlList.get(0);
					}
				}
			}*/
			Map<String, Map<String, List<String>>> hlMap = queryResponse.getHighlighting();
			List<String> hlList = hlMap.get(solrDocument.get("id")).get("item_title");
			String item_title = "";
			if(hlList != null && hlList.size() > 0){
				// 有高亮数据查询到，取值
				item_title = hlList.get(0);
			}else{
				item_title = (String) solrDocument.get("item_title");
			}
			searchItem.setId((String) solrDocument.get("id"));
			searchItem.setTitle(item_title);
			searchItem.setSell_point((String) solrDocument.get("item_sell_point"));
			searchItem.setPrice((Long) solrDocument.get("item_price"));
			// 商品列表展示页面只能取一张图
			String image = (String) solrDocument.get("item_image");
			if(StringUtils.isNotBlank(image)){
				image = image.split(",")[0];
			}
			searchItem.setImage(image);
			searchItem.setCategory_name((String) solrDocument.get("item_category_name"));
			
			searchItemList.add(searchItem);
		}
		searchResult.setItemList(searchItemList);
		return searchResult;
	
	}

}
