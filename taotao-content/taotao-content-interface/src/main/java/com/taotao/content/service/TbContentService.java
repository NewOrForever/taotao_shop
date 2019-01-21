package com.taotao.content.service;


import java.util.List;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.pojo.TbContent;

/**
 * 内容接口
 * <p>Description：</p>
 * @author： sq
 * @date： 2018年8月31日 下午4:10:55
 *
 */

public interface TbContentService {

	/**
	 * 分页查询
	 * @param categoryId 
	 * @param page
	 * @param rows
	 * @return
	 */
	EasyUIDataGridResult getContentList(int pageNum, int pageSize, long categoryId);

	
	/**
	 * 获取内容分类对应的内容
	 * @param aD1_CID
	 * @return
	 */
	List<TbContent> getContentListByCatId(Long cid);
	
}
