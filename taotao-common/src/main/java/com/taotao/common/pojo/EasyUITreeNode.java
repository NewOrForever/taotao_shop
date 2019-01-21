package com.taotao.common.pojo;

import java.io.Serializable;

/**
 * 
 * <p>Description：EasyUI Tree树控件返回对象</p>
 * @author： sq
 * @date： 2018年8月24日 下午3:56:16
 *
 */

public class EasyUITreeNode implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 269599586049224709L;
	
	private long id;
	private String text;
	private String state; 
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	
	
}
