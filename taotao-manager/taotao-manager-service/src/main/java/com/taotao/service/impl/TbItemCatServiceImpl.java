package com.taotao.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemCatExample;
import com.taotao.pojo.TbItemCatExample.Criteria;
import com.taotao.service.TbItemCatService;

@Service("tbItemCatService")
public class TbItemCatServiceImpl implements TbItemCatService {

	@Autowired
	private TbItemCatMapper tbItemCatMapper;
	
	@Override
	public List<EasyUITreeNode> getItemCatByParentId(long parentId) {
		// 设置查询条件
		TbItemCatExample example = new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		// 执行查询
		List<TbItemCat> itemCatList = tbItemCatMapper.selectByExample(example);
		if(itemCatList == null){
			return null;
		}
		
		// 配置Tree节点
		ArrayList<EasyUITreeNode> treeNodeList = new ArrayList<>();
		for (TbItemCat tbItemCat : itemCatList) {
			EasyUITreeNode treeNode = new EasyUITreeNode();
			treeNode.setId(tbItemCat.getId());
			treeNode.setText(tbItemCat.getName());
			treeNode.setState(tbItemCat.getIsParent() ? "closed" : "open"); // 父类不展开
			treeNodeList.add(treeNode);
		}
		return treeNodeList;
	}

}
