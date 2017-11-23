package com.bit.app.redis;

import com.bit.app.redis.MessagePublisher;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

@Data
@AllArgsConstructor
@Service
public class RedisMessagePublisher implements MessagePublisher {
    @Override
    public void publish(String message) {

    }

//    @Autowired
//    @Qualifier("redisTemplate")
//    private RedisTemplate<String, Object> redisTemplate;

//    @Autowired
//    @Qualifier("topic")
//    private ChannelTopic topic;

//    @Override
//    public void publish(String message) {
//        redisTemplate.convertAndSend(topic.getTopic(), message);
//    }
}
