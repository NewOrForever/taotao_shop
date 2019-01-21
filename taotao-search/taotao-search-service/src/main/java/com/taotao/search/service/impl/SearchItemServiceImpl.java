package com.taotao.search.service.impl;


import java.util.List;
import java.util.ListIterator;

import javax.annotation.Resource;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.SearchResult;
import com.taotao.common.pojo.TaoTaoResult;
import com.taotao.search.dao.SearchDao;
import com.taotao.search.mapper.SearchItemMapper;
import com.taotao.search.service.SearchItemService;

@Service("searchItemService")
public class SearchItemServiceImpl implements SearchItemService {

	@Autowired
	private SearchItemMapper searchItemMapper;
	
	@Resource
	private SearchDao searchDao;
	
	@Autowired // 根据Type来注入（所以单机版和集群版切换只需要重新配置bean就可以了）
	private SolrClient solrServer;

	@Override
	public TaoTaoResult importItemsToSolrIndex() {
		try {
			/*
			 * 1.查询所有商品信息、遍历
			 */
			List<SearchItem> itemList = searchItemMapper.getItemList();
			ListIterator<SearchItem> itemIt = itemList.listIterator();
			while (itemIt.hasNext()) {
				/**
				 * 2.将每个商品添加到solr文档
				 */
				SearchItem searchItem = itemIt.next();
				SolrInputDocument document = new SolrInputDocument();
				document.addField("id", searchItem.getId());
				document.addField("item_title", searchItem.getTitle());
				document.addField("item_sell_point", searchItem.getSell_point());
				document.addField("item_price", searchItem.getPrice());
				document.addField("item_image", searchItem.getImage());
				document.addField("item_category_name", searchItem.getCategory_name());
				document.addField("item_desc", searchItem.getItem_desc());
				
				// 3.文档写入索引库
				solrServer.add(document);
				
			}
			// 4.索引库提交
			solrServer.commit();
		} catch (Exception e) {
			e.printStackTrace();
			// 返回错误信息到页面
			TaoTaoResult.build(500, e.toString());
		} 
		
		return TaoTaoResult.OK();
	}

	@Override
	public SearchResult search(String stringQuery, int page, int rows) throws Exception {
		// solrquery查询对象
		SolrQuery solrQuery = new SolrQuery();
		/*******************************************************
		 *  1.设置查询条件、过滤条件、搜索域、分页条件、排序条件、高亮
		 *******************************************************/
		//solrQuery.set("q", "*:*");
		solrQuery.setQuery(stringQuery); // 等价于上面一条语句
		// 设置分页条件
		if(page < 1) page = 1;
		solrQuery.setStart((page - 1) * rows);
		if(rows < 1) rows = 10;
		solrQuery.setRows(rows);
		// 设置默认搜索域
		solrQuery.set("df", "item_keywords");
		/** 设置高亮显示**/
		// 高亮显示
		solrQuery.setHighlight(true);
		// 高亮显示的域
		solrQuery.addHighlightField("item_title");
		// 高亮显示样式
		solrQuery.setHighlightSimplePre("<em style=\"color:red\">");
		solrQuery.setHighlightSimplePost("</em>");
		
		// 2.执行查询
		SearchResult result = searchDao.search(solrQuery);
		// 计算总页数
		Long recordCount = result.getRecordCount();
		long totalPages = recordCount / rows;
		if(recordCount % rows > 0){
			totalPages++;
		}
		result.setTotalPages(totalPages);
		return result;
		
	}
	

}
