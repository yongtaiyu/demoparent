package org.ydk.transaction.demo1;

import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * @Date: Created in  2018/2/12 15:48
 * 未决事务，服务器端回查客户端
 */
public class TransactionCheckListenerImpl implements TransactionListener {
    public LocalTransactionState checkLocalTransactionState(MessageExt messageExt) {

        System.out.println("服务器端回查事务消息： "+messageExt.toString());
        //由于RocketMQ迟迟没有收到消息的确认消息，因此主动询问这条prepare消息，是否正常？
        //可以查询数据库看这条数据是否已经处理

        return LocalTransactionState.COMMIT_MESSAGE;
    }

	public LocalTransactionState checkLocalTransaction(MessageExt arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public LocalTransactionState executeLocalTransaction(Message arg0, Object arg1) {
		// TODO Auto-generated method stub
		return null;
	}
}