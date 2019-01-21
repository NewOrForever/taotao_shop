package com.taotao.jedis;

/**
 * 常用的redis方法提取出来做接口，提供给单机版和集群版两个实现类
 * <p>Description：</p>
 * @author： sq
 * @date： 2018年9月10日 下午2:44:09
 *
 */

public interface JedisClient {

	String set(String key, String value);
	String get(String key);
	boolean exists(String key);
	Long expire(String key, int seconds);
	Long ttl(String key);
	Long incr(String key);
	Long hset(String key, String field, String value);
	String hget(String key, String field);
	Long hdel(String key, String... field);
	
}
