package com.taotao.content.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.utils.JsonUtil;
import com.taotao.content.service.TbContentService;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import com.taotao.pojo.TbContentExample.Criteria;

@Service("contentService")
public class TbContentServiceImpl implements TbContentService {
	
	@Autowired
	private TbContentMapper tbContentMapper;
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${INDEX_CONTENT}")
	private String INDEX_CONTENT;

	@Override
	public EasyUIDataGridResult getContentList(int pageNum, int pageSize, long categoryId) {
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		// 配置查询条件
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		// 执行查询
		List<TbContent> contentList = tbContentMapper.selectByExample(example);
		// 获取分页结果
		PageInfo<TbContent> pageInfo = new PageInfo<TbContent>(contentList);
	
		// 返回分页数据
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setTotal(pageInfo.getTotal());
		result.setRows(contentList);
		
		return result;
	}

	/**
	 * 增删改时，缓存同步（删除缓存）
	 */

	@Override
	public List<TbContent> getContentListByCatId(Long cid) {
		// 先查询缓存
		try {
				String json = jedisClient.hget(INDEX_CONTENT, cid + "");
				//System.out.println("======================" + json);
				if(StringUtils.isNotBlank(json)){
				return JsonUtil.jsonToList(json, TbContent.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 缓存没有命中则查询数据库
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(cid);
		List<TbContent> adList = tbContentMapper.selectByExample(example);
		
		// 将查询结果添加到缓存
		try {
			jedisClient.hset(INDEX_CONTENT, cid + "", JsonUtil.objectToJson(adList));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return adList;
				
	}
	
	
	
}
