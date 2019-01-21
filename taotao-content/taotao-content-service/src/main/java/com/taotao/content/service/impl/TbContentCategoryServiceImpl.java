package com.taotao.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaoTaoResult;
import com.taotao.content.service.TbContentCategoryService;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import com.taotao.pojo.TbContentCategoryExample.Criteria;

@Service("contentCatService")
public class TbContentCategoryServiceImpl implements TbContentCategoryService {

	@Autowired
	private TbContentCategoryMapper tbContentCategoryMapper;
	
	@Override
	public List<EasyUITreeNode> getContentCatByPId(long parentId) {
		
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		criteria.andStatusEqualTo(1);
		
		List<TbContentCategory> contentCatList = tbContentCategoryMapper.selectByExample(example);
		
		if(contentCatList == null){
			return null;
		}
		
		// 配置Tree节点
		ArrayList<EasyUITreeNode> treeNodeList = new ArrayList<>();
		for (TbContentCategory contentCat : contentCatList) {
			EasyUITreeNode treeNode = new EasyUITreeNode();
			treeNode.setId(contentCat.getId());
			treeNode.setText(contentCat.getName());
			treeNode.setState(contentCat.getIsParent() ? "closed" : "open"); // 父类不展开
			treeNodeList.add(treeNode);
		}
		return treeNodeList;
	}

	@Override
	public TaoTaoResult addContentCategory(long parentId, String name) {
		/**
		 * 1.新增分类
		 * 2.修改parentId对应的id的isparent字段
		 */
		TbContentCategory contentCategory = new TbContentCategory();
		contentCategory.setParentId(parentId);
		contentCategory.setName(name);
		contentCategory.setIsParent(false);
		contentCategory.setStatus(1);
		contentCategory.setSortOrder(1);
		contentCategory.setCreated(new Date());
		contentCategory.setUpdated(new Date());
		tbContentCategoryMapper.insertSelective(contentCategory);
		//System.out.println("================================" + contentCategory.getId());
		
		// 1.判断父节点isparent 2.false 时修改
		TbContentCategory parentCategory = tbContentCategoryMapper.selectByPrimaryKey(parentId);
		if(!parentCategory.getIsParent()){
			parentCategory.setIsParent(true);
			parentCategory.setUpdated(new Date());
			tbContentCategoryMapper.updateByPrimaryKey(parentCategory);
		}
		
		// 新增成功
		return TaoTaoResult.OK(contentCategory);
		
	}

	@Override
	public int updateContentCategoryById(TbContentCategory tbContentCategory) {
		return tbContentCategoryMapper.updateByPrimaryKeySelective(tbContentCategory);
	}

	@Override
	public TaoTaoResult deleteContentCategoryById(Long parentId, Long id) {
		/**
		 * 1.修改id所对应分类的状态（status为2）
		 * 2.查看parentId下是否有其他子节点，有则不修改parentId对应节点，没有则修改该父节点isparent
		 * 3.如果id对应节点的isparent为true，则连同其子节点一起删除（status为2）
		 */
		// 1.删除选中节点
		TbContentCategory selectedContentCategory = tbContentCategoryMapper.selectByPrimaryKey(id);
		deleteNode(selectedContentCategory);
		
		// 2.
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		criteria.andIdNotEqualTo(id);
		criteria.andStatusEqualTo(1);
		List<TbContentCategory> contentCatList = tbContentCategoryMapper.selectByExample(example);
		if(contentCatList == null || contentCatList.size() == 0){
			 //没有其他未删除子节点,修改父节点
			TbContentCategory parentNode = tbContentCategoryMapper.selectByPrimaryKey(parentId);
			if(parentNode != null){
				parentNode.setIsParent(false);
				parentNode.setUpdated(new Date());
				tbContentCategoryMapper.updateByPrimaryKey(parentNode);
			}
		}
		
		// 3.递归开始，当前选中的节点为父节点
		if(selectedContentCategory.getIsParent()){
			// 递归删除以id作为parentId的所有节点status为2
			/**
			 * 1.边界条件：node.isparent = false;
			 * 2.使用iterator迭代每个父节点下的子节点
			 */
			RecursiveDeleteNode(selectedContentCategory);
		}
		
		return TaoTaoResult.OK();
	}
	
	// 递归删除节点
	public void RecursiveDeleteNode(TbContentCategory contentCatNode){
		if(contentCatNode.getIsParent()){ 
			// 父节点，递归向前 
			// 获取子节点迭代器
			TbContentCategoryExample nodeExample = new TbContentCategoryExample();
			Criteria criteria = nodeExample.createCriteria();
			criteria.andParentIdEqualTo(contentCatNode.getId());
			List<TbContentCategory> childNodeList = tbContentCategoryMapper.selectByExample(nodeExample);
			Iterator<TbContentCategory> iterator = childNodeList.iterator();
			while (iterator.hasNext()) {
				TbContentCategory childNode = (TbContentCategory) iterator.next(); // 子节点
				RecursiveDeleteNode(childNode);
			}
		}  
		// 父节点此时没有子节点
		// 删除当前节点
		deleteNode(contentCatNode);
	}

	// 删除当前节点
	public void deleteNode(TbContentCategory contentCatNode) {
		contentCatNode.setStatus(2);
		contentCatNode.setUpdated(new Date());
		tbContentCategoryMapper.updateByPrimaryKey(contentCatNode);
	}
		
}
