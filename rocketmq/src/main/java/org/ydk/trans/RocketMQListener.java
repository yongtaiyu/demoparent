package org.ydk.trans;

import java.util.List;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * Created by lance on 2017/2/10.
 */
public class RocketMQListener implements MessageListenerConcurrently {

	public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
		for (MessageExt message : msgs) {
			String msg = new String(message.getBody());
			System.out.println("msg data from rocketMQ:" + msg);
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}
}
