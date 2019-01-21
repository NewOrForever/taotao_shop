package com.taotao.jedis;

import java.util.HashSet;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

/**
 * 测试redis
 * <p>Description：</p>
 * @author： sq
 * @date： 2018年9月10日 上午11:14:37
 *
 */

public class TestJedis {
	
	/*********************************
	 * 单机版
	 ********************************/
	@Test
	public void testJedisClient(){
		// 创建jedis对象，根据IP和端口(连接时，redis.conf的bind 127.0.0.1需注释掉)
		Jedis jedis = new Jedis("192.168.120.55", 6379);
		// 使用jedis对象操作数据库，方法名通命令相同
		jedis.set("key2", "flying");
		String result = jedis.get("key1");
		// 打印结果
		System.out.println(result);
		// 关闭jedis
		jedis.close();
	}
	
	// 连接池
	@Test
	public void testJedisPool(){
		// 创建连接池
		JedisPool jedisPool = new JedisPool("192.168.120.55", 6379);
		// 获取jedis
		Jedis jedis = jedisPool.getResource();
		// 操作数据库(NX:键不存在时设置值，XX:键存在时设置值，EX:有效时间为秒,PX：有效时间为毫秒)，
		//jedis.set("key1", "bird2", "NX", "EX", 100);
		jedis.set("key1","lion");
		String result = jedis.get("key2");
		// 打印结果
		System.out.println(result);
		
		// 关闭连接池
		jedis.close();
		jedisPool.close();
		
	}
	
	/*****************************
	 * 集群版(需要关闭节点的防火墙)
	 * @throws Exception 
	 *****************************/
	@Test
	public void testJedisCluster() throws Exception{
		// 创建集群
		HashSet<HostAndPort> clusterNodes = new HashSet<>();
		clusterNodes.add(new HostAndPort("192.168.120.55", 7001));
		clusterNodes.add(new HostAndPort("192.168.120.55", 7002));
		clusterNodes.add(new HostAndPort("192.168.120.55", 7003));
		clusterNodes.add(new HostAndPort("192.168.120.55", 7004));
		clusterNodes.add(new HostAndPort("192.168.120.55", 7005));
		clusterNodes.add(new HostAndPort("192.168.120.55", 7006));
		JedisCluster jedisCluster = new JedisCluster(clusterNodes);
		// 操作redis，jedisCluster自带连接池，可以是单例的
		jedisCluster.set("bird", "flying");
		String result = jedisCluster.get("lion");
		// 打印结果
		System.out.println(result);
		// 关闭连接
		jedisCluster.close();
		
	}
	
	
	
	
	
	
}
