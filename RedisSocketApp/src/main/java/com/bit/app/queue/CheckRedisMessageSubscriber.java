package com.bit.app.queue;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CheckRedisMessageSubscriber implements MessageListener {

    private CheckPoolQueue checkPoolQueue;

    @Autowired
    public CheckRedisMessageSubscriber(CheckPoolQueue checkPoolQueue) {
        this.checkPoolQueue = checkPoolQueue;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String s = new String(message.getBody());
        log.warn("Redis received message: " + s);
        checkPoolQueue.pushState(s);
    }
}
