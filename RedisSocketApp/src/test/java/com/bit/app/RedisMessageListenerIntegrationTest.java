package com.bit.app;

import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class RedisMessageListenerIntegrationTest {

//    @Autowired
//    private RedisMessagePublisher redisMessagePublisher;
//
//    @Test
//    public void testOnMessage() throws Exception {
//        String message = "Message " + UUID.randomUUID();
//        redisMessagePublisher.publish(message);
//        Thread.sleep(100);
//        assertTrue(RedisMessageSubscriber.messageList.get(0).contains(message));
//    }
}
