package com.taotao.activemq;



import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 测试spring整合activemq（consumer），producer在manager-service
 * <p>Description：</p>
 * @author： sq
 * @date： 2018年9月27日 下午1:41:51
 *
 */

public class TestSpringActiveMq {
	
	@Test
	public void testSpringActiveMq() throws Exception{
		// 启动容器
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
		// 等待
		System.in.read();
	}
	
}
