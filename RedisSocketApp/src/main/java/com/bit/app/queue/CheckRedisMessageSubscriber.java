package com.bit.app.queue;

import com.bit.app.entities.AbstractCheckEntity;
import com.bit.app.entities.CheckEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.Serializable;

//@Slf4j
//@Component
//public class CheckRedisMessageSubscriber implements MessageListener {
//
//    private CheckPoolQueue checkPoolQueue;
//
//    @Autowired
//    public CheckRedisMessageSubscriber(CheckPoolQueue checkPoolQueue) {
//        this.checkPoolQueue = checkPoolQueue;
//    }
//
//    @Override
//    public void onMessage(Message message, byte[] pattern) {
//        byte[] body = message.getBody();
//        log.warn("Redis received Message message: " + body);
//        log.warn("Redis received message: " + body);
//        CheckEntity checkEntity = CheckEntity.builder().name(new String(body)).build();
//        checkPoolQueue.pushState(checkEntity);
//    }

//    @Override
//    public void onMessage(Message message, byte[] pattern) {
//        byte[] body = message.getBody();
//        log.warn("Redis received Message message: " + body);
//        log.warn("Redis received message: " + body);
//        CheckEntity checkEntity = CheckEntity.builder().name(new String(body)).build();
//        checkPoolQueue.pushState(checkEntity);
//    }
//}

@Slf4j
@Component
public class CheckRedisMessageSubscriber implements MessageDelegate {

    private CheckPoolQueue checkPoolQueue;

    @Autowired
    public CheckRedisMessageSubscriber(CheckPoolQueue checkPoolQueue) {
        this.checkPoolQueue = checkPoolQueue;
    }

    @Override
    public void handleMessage(AbstractCheckEntity message, String channel) {
        log.warn("receved message serializable: " + message);
        checkPoolQueue.pushState(message);
    }
}
