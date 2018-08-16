package org.ydk.transaction.demo1;

import java.util.Date;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;

/**
 * @Date: Created in  2018/2/12 15:24
 * 测试本地事务
 */
public class TestTransactionProducer {
    public static void main(String[] args){
        //事务回查监听器
        TransactionCheckListenerImpl checkListener = new TransactionCheckListenerImpl();
        //事务消息生产者
        TransactionMQProducer producer = new TransactionMQProducer("transactionProducerGroup");
        //MQ服务器地址
        producer.setNamesrvAddr("192.168.3.6:9876;192.168.3.7:9876");
        //注册事务回查监听
        producer.setTransactionCheckListener(checkListener);
        //本地事务执行器
        TransactionExecuterimpl executerimpl = null;
        try {
            //启动生产者
            producer.start();
            executerimpl = new TransactionExecuterimpl();
            Message msg1 = new Message("TransactionTopic", "tag", "KEY1", "hello RocketMQ 1".getBytes());
            Message msg2 = new Message("TransactionTopic", "tag", "KEY2", "hello RocketMQ 2".getBytes());

            SendResult sendResult = producer.sendMessageInTransaction(msg1, executerimpl, null);
            System.out.println(new Date() + " 【msg1】 "+sendResult);

            sendResult = producer.sendMessageInTransaction(msg2, executerimpl, null);
            System.out.println(new Date() + " 【msg2】 "+sendResult);

        } catch (MQClientException e) {
            e.printStackTrace();
        }
        producer.shutdown();
    }
}