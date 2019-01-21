package com.taotao.common.utils;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 淘淘商城自定义响应结构
 * <p>Description：</p>
 * @author： sq
 * @date： 2018年9月4日 上午9:11:35
 *
 */

public class JsonUtil {
	
	private static ObjectMapper mapper = new ObjectMapper();
	/**
	 * 对象转json 
	 * @param data
	 * @return
	 */
	public static String objectToJson(Object data){
		try {
			String result = mapper.writeValueAsString(data);
			return result;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * json 转pojo
	 * @param jsonString
	 */
	public static <T> T jsonToPoJo(String jsonString, Class<T> beanType){
		try {
			T result = mapper.readValue(jsonString, beanType);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	/**
	 * json 转list
	 * @param jsonList
	 * @param beanType
	 * @return
	 */
	public static <T> List<T> jsonToList(String jsonList, Class<T> beanType){
	
		try {
			JavaType javaType= mapper.getTypeFactory().constructParametricType(List.class, beanType);
			List<T> result = mapper.readValue(jsonList, javaType);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return null;
	}
	
	
}
