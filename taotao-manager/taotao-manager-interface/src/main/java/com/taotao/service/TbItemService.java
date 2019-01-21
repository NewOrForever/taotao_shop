package com.taotao.service;




import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaoTaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;


public interface TbItemService {
	/**
	 * 根据主键获取商品
	 * @param id
	 */
	public TbItem getItemById(long id);

	/**
	 * 根据id获取商品描述
	 * @param id
	 * @return
	 */
	public TbItemDesc getItemDescById(long id);
	
	/**
	 * 获取商品列表
	 * @param page 当前页号
	 * @param rows 页大小
	 */
	public EasyUIDataGridResult getItemList(int pageNum, int pageSize);

	/**
	 * 删除商品（假删除，改变status）
	 * @param ids
	 */
	public int deleteItemByIds(Long[] ids);

	/**
	 * 更新商品
	 * @param updateMap
	 * @return
	 */
	public int updateItemByIds(Long[] ids, TbItem tbItem);

	/**
	 * 添加商品
	 * @param item 商品
	 * @param itemDesc 商品描述
	 * @return
	 */
	public TaoTaoResult addItem(TbItem item, String itemDesc);
	
	
}
