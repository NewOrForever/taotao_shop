package com.taotao.search.messageListener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * 消息监听器测试
 * <p>Description：</p>
 * @author： sq
 * @date： 2018年9月27日 下午1:27:58
 *
 */

public class MyMessageListener implements MessageListener {

	@Override
	public void onMessage(Message message) {
		// 取消息
		try {
				TextMessage textMessage = (TextMessage) message;
				String text = textMessage.getText();
				System.out.println(text);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
