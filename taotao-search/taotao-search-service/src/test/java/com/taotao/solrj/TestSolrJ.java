package com.taotao.solrj;

import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

/**
 * solrJ管理solr测试
 * <p>Description：</p>
 * @author： sq
 * @date： 2018年9月14日 下午2:06:25
 *
 */

public class TestSolrJ {
	
	@Test
	public void testSolrCloudAddDocument() throws Exception{
		// 创建cloudSolrServer对象
		@SuppressWarnings("deprecation")
		CloudSolrClient cloudSolrServer = new CloudSolrClient("192.168.120.24:2183,192.168.120.24:2184,192.168.120.24:2185");
		// 设置默认的collection（必须）
		cloudSolrServer.setDefaultCollection("collection1");
		// 创建文档对象(SolrInputDocument)
		SolrInputDocument document = new SolrInputDocument();
		// 文档对象添加域
		document.addField("item_title", "测试集群商品");
		document.addField("item_price", 100);
		document.addField("id", "test001");
		// 文档写入索引库
		cloudSolrServer.add(document);
		// 提交
		cloudSolrServer.commit();
		cloudSolrServer.close();
	}
	
	@Test
	public void testAddDocument() throws Exception{
		// 创建solrserver对象
		@SuppressWarnings("deprecation")
		SolrClient solrServer = new HttpSolrClient("http://192.168.120.25:8080/solr/mycore");
		//new HttpSolrClient.Builder(http://192.168.120.25:8080/solr/mycore);
		// 创建文档对象SolrInputDocument
		SolrInputDocument document = new SolrInputDocument();
		// 向文档中添加域，必须有id域，域的名称必须在schema中定义
		document.addField("id", "test005");
		document.addField("item_title", "测试商品005测中");
		document.addField("item_price", 100);
		// 将文档写入索引库
		solrServer.add(document);
		// 提交
		solrServer.commit();
		solrServer.close();
	}
	
	@Test
	public void testDeleteById() throws Exception{
		// 创建solrserver对象
		@SuppressWarnings("deprecation")
		SolrClient solrServer = new HttpSolrClient("http://192.168.120.25:8080/solr/mycore");
		// 根据id域删除
		solrServer.deleteById("test001");
		// 提交
		solrServer.commit();
		// 关闭
		solrServer.close();
	}
	
	@Test
	public void testDeleteByQuery() throws Exception{
		// 创建solrserver对象
		@SuppressWarnings("deprecation")
		SolrClient solrServer = new HttpSolrClient("http://192.168.120.25:8080/solr/mycore");
		// 根据id域删除
		solrServer.deleteByQuery("item_title:测试商品003");
		// 提交
		solrServer.commit();
		// 关闭
		solrServer.close();
	}
	
	@Test 
	public void testSearchDocument() throws Exception{
		@SuppressWarnings("deprecation")
		SolrClient solrServer = new HttpSolrClient("http://192.168.120.25:8080/solr/mycore");
		// solrquery查询对象
		SolrQuery solrQuery = new SolrQuery();
		/*******************************************************
		 *  1.设置查询条件、过滤条件、搜索域、分页条件、排序条件、高亮
		 *******************************************************/
		//solrQuery.set("q", "*:*");
		solrQuery.setQuery("测试"); // 等价于上面一条语句
		// 设置分页条件
		solrQuery.setStart(0);
		solrQuery.setRows(10);
		// 设置默认搜索域
		solrQuery.set("df", "item_keywords");
		/** 设置高亮显示**/
		// 高亮显示
		solrQuery.setHighlight(true);
		// 高亮显示的域
		solrQuery.addHighlightField("item_title");
		// 高亮显示样式
		solrQuery.setHighlightSimplePre("<em>");
		solrQuery.setHighlightSimplePost("</em>");
		
		/**************************************
		 * 2.执行查询、获取查询结果、取高亮显示
		 **************************************/
		// 执行查询
		QueryResponse queryResponse = solrServer.query(solrQuery);
		// 获取查询结果
		SolrDocumentList solrDocumentList = queryResponse.getResults();
		// 总记录数
		System.out.println("查询到的总记录数：" + solrDocumentList.getNumFound());
		// 迭代器
		ListIterator<SolrDocument> solrDocIteator = solrDocumentList.listIterator();
		// 总记录数
		while (solrDocIteator.hasNext()) {
			SolrDocument solrDocument = solrDocIteator.next();
			System.out.println(solrDocument.get("id"));
			/***** 取高亮显示*****/
			Map<String, Map<String, List<String>>> hlMap = queryResponse.getHighlighting();
			List<String> hlList = hlMap.get(solrDocument.get("id")).get("item_title");
			String item_title = "";
			if(hlList != null && hlList.size() > 0){
				// 有高亮数据查询到，取值
				item_title = hlList.get(0);
			}else{
				item_title = (String) solrDocument.get("item_title");
			}
			System.out.println(item_title);
			System.out.println(solrDocument.get("item_sell_point"));
			System.out.println(solrDocument.get("item_price"));
			System.out.println(solrDocument.get("item_image"));
			System.out.println(solrDocument.get("item_category_name"));
			System.out.println("=============================================");
			
		}
		solrServer.close();
	}
	
}
