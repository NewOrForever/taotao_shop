package com.taotao.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;


/**
 * activemq测试
 * <p>Description：</p>
 * @author： sq
 * @date： 2018年9月26日 上午9:50:22
 *
 */

public class TestActiveMq {

	/*******************************
	 * 1.queue(持久化、保存在服务端、没有接收到不会丢失)
	 * point-to-point(ptp)
	 *******************************/
	// producer
	@Test
	public void testQueueProducer() throws Exception{
		// 1.创建ConnectionFactory对象，需要指定服务端ip及端口号。
		//brokerURL服务器的ip及端口号（61616）
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.120.157:61616");
		// 2.使用ConnectionFactory创建一个连接Connection对象
		Connection connection = connectionFactory.createConnection();
		// 3.开启连接。调用Connection对象的start方法
		connection.start();
		// 4.使用Connection对象创建一个Session对象
		//第一个参数是是否开启事务，一般不使用事务。保证数据的最终一致，可以使用消息队列实现。
		//如果第一个参数为true，第二个参数自动忽略。如果不开启事务false，第二个参数为消息的应答模式。一般自动应答就可以
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 5.使用Session对象创建一个Destination对象，两种形式queue、topic。现在应该使用queue
		//参数就是消息队列的名称
		Queue queue = session.createQueue("test-queue");
		// 6.使用Session对象创建一个Producer对象
		MessageProducer producer = session.createProducer(queue);
		// 7.创建一个TextMessage对象
		/*TextMessage textMessage = new ActiveMQTextMessage();
		textMessage.setText("hello activemq");*/
		TextMessage textMessage = session.createTextMessage("hello test-queue activemq");
		//8.发送消息
		producer.send(textMessage);
		// 9.关闭资源
		producer.close();
		session.close();
		connection.close();
	}
	
	// consumer
	@Test
	public void testConsumerQueue() throws Exception{
		// 工厂对象
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.120.157:61616");
		// 连接对象
		Connection connection = connectionFactory.createConnection();
		// 开启连接
		connection.start();
		// session对象
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// session创建destination对象
		Queue queue = session.createQueue("test-queue");
		// session创建consumer
		MessageConsumer consumer = session.createConsumer(queue);
		// 向Consumer对象中设置一个MessageListener对象，用来接收消息
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				// 取消息内容
				if(message instanceof TextMessage){
					TextMessage textMessage = (TextMessage) message;
					try {
						String text = textMessage.getText();
						// 打印消息内容
						System.out.println(text);
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			}
		});
		// 系统等待接收消息
		/*while(true) {
			Thread.sleep(100);
		}*/
		System.in.read();
		// 关闭资源
		consumer.close();
		session.close();
		connection.close();
	}
	
	/*******************************
	 * 2.topic(非持久化、不保存在服务端、没有接收到会丢失)
	 * public/subscribe(pub/sub)
	 *******************************/
	// producer
	@Test
	public void testTopicProducer() throws Exception{
		// 创建工厂连接对象
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.120.157:61616");
		// 创建连接对象
		Connection connection = connectionFactory.createConnection();
		// 开启连接
		connection.start();
		// 创建session
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// session创建destination对象（topic）
		Topic topic = session.createTopic("test-topic");
		// session创建producer对象
		MessageProducer producer = session.createProducer(topic);
		// 创建textmessager对象
		TextMessage textMessage = session.createTextMessage("test activemq topic producer");
		// 发送消息
		producer.send(textMessage);
		// 关闭资源
		producer.close();
		session.close();
		connection.close();
	}
	
	// consumer
	@Test
	public void testTopicConsumer() throws Exception{
		// 创建工厂连接对象
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.120.157:61616");
		// 创建连接对象
		Connection connection = connectionFactory.createConnection();
		// 开启连接
		connection.start();
		// 创建session
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// session创建destination对象（topic）
		Topic topic = session.createTopic("test-topic");
		// session创建consumer
		MessageConsumer consumer = session.createConsumer(topic);
		// 向Consumer对象中设置一个MessageListener对象，用来接收消息
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				// 取消息内容
				if(message instanceof TextMessage){
					TextMessage textMessage = (TextMessage) message;
					String text;
					try {
						// 打印消息内容
						text = textMessage.getText();
						System.out.println(text);
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			}
		});
		// 等待消息接收
		/*while(true) {
			Thread.sleep(100);
		}*/
		System.out.println("consumer3");
		System.in.read();
		// 关闭资源
		consumer.close();
		session.close();
		connection.close();
	}
	
}
