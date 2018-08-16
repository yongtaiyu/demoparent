package org.ydk.demo1;

import java.util.UUID;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListener;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;

/**
 * Created by lance on 2017/2/10.
 */
public class RocketMQConsumer {

	private DefaultMQPushConsumer consumer;

	private MessageListener listener;

	protected String nameServer;

	protected String groupName;

	protected String topics;
	/**
	 *  订阅表达式.它只支持或操作像 "tag1 || tag2 || tag3",表示只订阅有这些标签的；如果其为 null或*表示订阅所有。
	 */
	protected String subExpression;

	public RocketMQConsumer(MessageListener listener, String nameServer, String groupName, String topics) {
		this.listener = listener;
		this.nameServer = nameServer;
		this.groupName = groupName;
		this.topics = topics;
		this.subExpression = "*";
	}

	public void init() {
		try {
			/**
			 * 一个应用创建一个Consumer，由应用来维护此对象，可以设置为全局对象或者单例 注意：ConsumerGroupName需要由应用来保证唯一
			 */
			consumer = new DefaultMQPushConsumer(groupName);
			consumer.setNamesrvAddr(nameServer);
			 /** 
	         * 订阅指定topic下tags，默认指定*，表示订阅所有 
	         */  
			consumer.subscribe(topics, this.subExpression);
			consumer.setInstanceName(UUID.randomUUID().toString());
			/**
			 * 设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费 如果非第一次启动，那么按照上次消费的位置继续消费
			 */
			consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
			//真正的处理消息逻辑在这里
			consumer.registerMessageListener((MessageListenerConcurrently) this.listener);
			//Consumer对象在使用之前必须要调用start初始化，初始化一次即可
			consumer.start();
		} catch (MQClientException e) {
			e.printStackTrace();
			System.out.println("RocketMQConsumer Start Failed! group=" + consumer.getConsumerGroup() + " instance="
					+ consumer.getInstanceName());
		}
		System.out.println("RocketMQConsumer Started! group=" + consumer.getConsumerGroup() + " instance="
				+ consumer.getInstanceName());
	}

	public String getSubExpression() {
		return subExpression;
	}

	public void setSubExpression(String subExpression) {
		this.subExpression = subExpression;
	}

}
