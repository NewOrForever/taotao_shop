package com.taotao.common.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 自定义solr索引库响应结果
 * <p>Description：</p>
 * @author： sq
 * @date： 2018年9月17日 下午4:50:28
 *
 */

public class SearchResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6707065990583349262L;
	private Long totalPages;
	private List<SearchItem> itemList;
	private Long recordCount;
	
	
	public Long getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(Long totalPages) {
		this.totalPages = totalPages;
	}
	public List<SearchItem> getItemList() {
		return itemList;
	}
	public void setItemList(List<SearchItem> itemList) {
		this.itemList = itemList;
	}
	public Long getRecordCount() {
		return recordCount;
	}
	public void setRecordCount(Long recordCount) {
		this.recordCount = recordCount;
	}
	
	
}
