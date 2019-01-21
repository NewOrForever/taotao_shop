package com.taotao.service;

import java.util.List;

import com.taotao.common.pojo.EasyUITreeNode;

public interface TbItemCatService {

	/**
	 * 根据父节点获取类目列表
	 * @param parentId
	 * @return
	 */
	List<EasyUITreeNode> getItemCatByParentId(long parentId);

}
