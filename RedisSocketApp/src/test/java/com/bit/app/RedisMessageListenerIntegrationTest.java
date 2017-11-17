package com.bit.app;

import com.bit.app.queue.RedisMessagePublisher;
import com.bit.app.queue.RedisMessageSubscriber;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.junit.Assert.assertTrue;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class RedisMessageListenerIntegrationTest {

    @Autowired
    private RedisMessagePublisher redisMessagePublisher;

    @Test
    public void testOnMessage() throws Exception {
        String message = "Message " + UUID.randomUUID();
        redisMessagePublisher.publish(message);
        Thread.sleep(100);
        assertTrue(RedisMessageSubscriber.messageList.get(0).contains(message));
    }
}
