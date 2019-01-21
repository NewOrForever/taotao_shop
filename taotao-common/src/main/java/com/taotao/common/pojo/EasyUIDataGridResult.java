package com.taotao.common.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * <p>Description：EasyUI DataGrid 分页返回数据对象</p>
 * @author： sq
 * @date： 2018年8月24日 下午1:10:39
 *
 */

public class EasyUIDataGridResult implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5835666890950375843L;
	
	
	private long total;
	private List<?> rows;
	
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public List<?> getRows() {
		return rows;
	}
	public void setRows(List<?> rows) {
		this.rows = rows;
	}
	
	
}
