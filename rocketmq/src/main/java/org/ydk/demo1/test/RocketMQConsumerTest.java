package org.ydk.demo1.test;


import org.ydk.demo1.RocketMQConsumer;

/**
 * Created by lance on 2017/2/10.
 */
public class RocketMQConsumerTest {


    public static void main(String[] args) {
        String mqNameServer = "192.168.3.6:9876;192.168.3.7:9876";
        String mqTopics = "MQ-MSG-TOPICS-TODOTASK";

        String consumerMqGroupName = "CONSUMER-MQ-GROUP-TODOTASK";
        RocketMQListener mqListener = new RocketMQListener();
        RocketMQConsumer mqConsumer = new RocketMQConsumer(mqListener, mqNameServer, consumerMqGroupName, mqTopics);
        mqConsumer.init();
try {
	Thread.sleep(1000*60L);
} catch (InterruptedException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
    }
}
