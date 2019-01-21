package com.taotao.activemq;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

/**
 * ActiveMQ和spring整合测试
 * <p>Description：</p>
 * @author： sq
 * @date： 2018年9月27日 下午12:31:00
 *
 */

public class TestSpringActiveMq {

	@Test
	public void testJmsTemplate(){
		// 启动spring容器
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
		// 从容器中获取对象
		JmsTemplate jmsTemplate = ac.getBean(JmsTemplate.class);
		Destination destination = (Destination) ac.getBean("test-queue");
		// 发送消息
		jmsTemplate.send(destination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage("test spring activemq integration");
				return textMessage;
			}
		});	
		
	}
	
}
