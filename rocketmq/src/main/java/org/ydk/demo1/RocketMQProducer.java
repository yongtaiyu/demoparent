package org.ydk.demo1;


import java.util.UUID;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;

/**
 * Created by lance on 2017/2/10.
 */
public class RocketMQProducer {

    private DefaultMQProducer sender;

    protected String nameServer;

    protected String groupName;

    protected String topics;

    public void init() {
    	 /**
         * 一个应用创建一个Producer，由应用来维护此对象，可以设置为全局对象或者单例
         * 注意：ProducerGroupName需要由应用来保证唯一
         * ProducerGroup这个概念发送普通的消息时，作用不大，但是发送分布式事务消息时，比较关键，
         * 因为服务器会回查这个Group下的任意一个Producer
         */
        sender = new DefaultMQProducer(groupName);
        sender.setRetryTimesWhenSendFailed(3);//消息发送重试次数
        sender.setNamesrvAddr(nameServer);
        sender.setInstanceName(UUID.randomUUID().toString());
        try {
            sender.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    public RocketMQProducer(String nameServer, String groupName, String topics) {
        this.nameServer = nameServer;
        this.groupName = groupName;
        this.topics = topics;
    }

    public void send(Message message) {
    	// 消息主题名 
        message.setTopic(topics);
        try {
        	// 发送消息并返回结果，该消息10000ms没发送成功则重试
            SendResult result = sender.send(message);
            SendStatus status = result.getSendStatus();
            System.out.println("messageId=" + result.getMsgId() + ", status=" + status);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 应用退出时，要调用shutdown来清理资源，关闭网络连接，从MetaQ服务器上注销自己
     * 注意：我们建议应用在JBOSS、Tomcat等容器的退出钩子里调用shutdown方法
     */
    public void shutdown()
    {
    	sender.shutdown();
    }
}
