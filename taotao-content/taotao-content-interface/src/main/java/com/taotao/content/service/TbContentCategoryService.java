package com.taotao.content.service;

import java.util.List;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaoTaoResult;
import com.taotao.pojo.TbContentCategory;

public interface TbContentCategoryService {

	/**
	 * 根据父分类查询内容分类
	 * @param parentId
	 * @return
	 */
	List<EasyUITreeNode> getContentCatByPId(long parentId);

	/**
	 * 添加分类
	 * @param parentId
	 * @param name
	 */
	TaoTaoResult addContentCategory(long parentId, String name);

	/**
	 * 根据主键更新
	 * @param tbContentCategory
	 */
	int updateContentCategoryById(TbContentCategory tbContentCategory);

	/**
	 * 删除分类（修改其状态，父分类也要修改）
	 * @param parentId
	 * @param id
	 * @return
	 */
	TaoTaoResult deleteContentCategoryById(Long parentId, Long id);

}
