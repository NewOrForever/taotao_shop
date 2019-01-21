package com.taotao.common.pojo;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 淘淘商城自定义响应结构
 * <p>Description：</p>
 * @author： sq
 * @date： 2018年8月29日 上午8:24:50
 *
 */

public class TaoTaoResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2521633878381073948L;
	
	// 常量
	//private final static int STATUS_OK = 200;
	
	public enum Result{
		OK(200,"OK");
		private int status;
		private String msg;
		private Result(int status, String msg){
			this.setStatus(status);
			this.setMsg(msg);
		}
		public int getStatus() {
			return status;
		}
		public void setStatus(int status) {
			this.status = status;
		}
		public String getMsg() {
			return msg;
		}
		public void setMsg(String msg) {
			this.msg = msg;
		}
	}
	
	// 定义Jackson对象
	private static ObjectMapper mapper = new ObjectMapper();
	
	private Integer status; 	// 响应状态
	private String msg;		// 响应消息
	private Object data;	// 响应数据

	public TaoTaoResult() {
	}
	
	// OK状态信息返回
	public static TaoTaoResult OK(){
		return new TaoTaoResult(null);
	}

	public static TaoTaoResult OK(Object data){
		return new TaoTaoResult(data);
	}
	
	public TaoTaoResult(Object data) {
		this.status = Result.OK.status;
		this.msg = Result.OK.msg;
		this.data = data;
	}
	
	// 自定义响应参数
	public static TaoTaoResult build(Integer status, String msg, Object data){
		return new TaoTaoResult(status, msg, data);
	} 
	public static TaoTaoResult build(Integer status, String msg) {
        return new TaoTaoResult(status, msg, null);
    }
	
	public TaoTaoResult(Integer status, String msg, Object data) {
		this.status = status;
		this.msg = msg;
		this.data = data;
	}
	
	
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	/*************************************
	 * json转TaoTaoResult对象
	 *************************************/
	
	/**
	 * 将json结果集转化为TaotaoResult对象
	 * @param jsonData  json数据
	 * @param clazz		TaoTaoResult中的object类型（data）
	 * @return
	 */
	public static TaoTaoResult formatToPoJo(String jsonData, Class<?> clazz){
		try {
			if(clazz == null){
				return mapper.readValue(jsonData,TaoTaoResult.class);
			}
			JsonNode jsonNode = mapper.readTree(jsonData);
			JsonNode data = jsonNode.get("data");
			Object obj = null;
			if(data.isObject()){
				obj = mapper.readValue(data.traverse(), clazz);
			}else if(data.isTextual()){
				obj = mapper.readValue(data.asText(), clazz);
			}
			return build(jsonNode.get("status").intValue(), jsonNode.get("msg").asText(), obj);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
     * 没有object对象的转化
     * 
     * @param json
     * @return
     */
	public static TaoTaoResult format(String json){
		try {
			return mapper.readValue(json, TaoTaoResult.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Object是集合转化
	 * @param jsonData  json数据
	 * @param clazz		集合中的类型
	 * @return
	 */
	public static TaoTaoResult formatToList(String jsonData, Class<?> clazz){
		JsonNode jsonNode;
		try {
			jsonNode = mapper.readTree(jsonData);
			JsonNode data = jsonNode.get("data");
			Object obj = null;
			if(data.isArray() && data.size() > 0){
				obj = mapper.readValue(data.traverse(), mapper.getTypeFactory().constructCollectionType(List.class, clazz));
			}
			return build(jsonNode.get("status").intValue(), jsonNode.get("msg").asText(), obj);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
