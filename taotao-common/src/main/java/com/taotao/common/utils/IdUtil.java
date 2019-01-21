package com.taotao.common.utils;

import java.util.Random;
import java.util.UUID;

/**
 * id生成工具
 * <p>Description：</p>
 * @author： sq
 * @date： 2018年9月27日 上午10:07:41
 *
 */

public class IdUtil {
	
	/**
	 * 生成UUID
	 * @return
	 */
	public static String generateUUID(){
		String uuid = UUID.randomUUID().toString().replace("-", "");
		return uuid;
	}

	/**
	 * 图片名生成
	 */
	public static String genImageName(){
		// 取当前时间的长整型包含毫秒
		long currentTimeMillis = System.currentTimeMillis();
		// long nanoTime = System.nanoTime();
		// 取三位随机数
		int random = new Random().nextInt(999);
		// 拼接
		String imageName = currentTimeMillis + String.format("%03d", random);
		return imageName;
	}
	
	/**
	 * 商品id生成
	 */
	public static long genItemId(){
		// 当前时间长整型包含毫秒
		long currentTimeMillis = System.currentTimeMillis();
		// 取两位随机数
		int random = new Random().nextInt(99);
		// 拼接
		String itemIdStr = currentTimeMillis + String.format("%02d", random);
		Long itemId = new Long(itemIdStr);
		return itemId;
	}
	
	public static void main(String[] args){
		for(int i=0;i<100;i++){
			System.out.println("================" + genImageName());
			System.out.println("********************" + genItemId());
		}
	}
		
}
