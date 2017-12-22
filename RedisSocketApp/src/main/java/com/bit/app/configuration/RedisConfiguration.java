package com.bit.app.configuration;

import com.bit.app.entities.AbstractCheckEntity;
import com.bit.app.entities.CheckEntity;
import com.bit.app.listeners.CheckDisconnectListener;
import com.bit.app.queue.CheckPoolQueue;
import com.bit.app.queue.CheckRedisMessageSubscriber;
import com.bit.app.services.CheckHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.messaging.simp.SimpMessagingTemplate;


@Configuration
@ComponentScan("com.bit.app")
public class RedisConfiguration {

    /**
     * фабрика коннекта к редису
     */
    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        return jedisConnectionFactory;
    }

    /**
     * адаптер слушателя сообщений, делегирующий обратку CheckRedisMessageSubscriber checkRedisMessageSubscriber.
     * Устаналиваем слушателю сериализовать в AbstractCheckEntity объект
     */
    @Bean
    @Autowired
    MessageListenerAdapter messageListener(CheckRedisMessageSubscriber checkRedisMessageSubscriber) {
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(checkRedisMessageSubscriber);
        messageListenerAdapter.setSerializer(new Jackson2JsonRedisSerializer<>(CheckEntity.class));
        return messageListenerAdapter;
    }

    /**
     * Контейнер асинхронного поведения слушателей сообщений.
     * Обрабатывает низкоуровневые детали слушателей, преобразование и отправка сообщений.
     * добавляем адаптер слушателей: MessageListenerAdapter messageListener
     */
    @Bean
    @Autowired
    RedisMessageListenerContainer redisContainer(CheckRedisMessageSubscriber checkRedisMessageSubscriber) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(messageListener(checkRedisMessageSubscriber), topic());
        return container;
    }

    @Bean("topic")
    ChannelTopic topic() {
        return new ChannelTopic("pubsub:queue");
    }

    /**
     * Обработчик сокет подписок
     */
    @Bean
    @Autowired
    CheckHandler checkHandler(SimpMessagingTemplate simpMessagingTemplate,
                              CheckPoolQueue checkPoolQueue) {
        return new CheckHandler(simpMessagingTemplate, checkPoolQueue);
    }

    /**
     * слушатель завершения сокет сессий
     */
    @Bean
    @Autowired
    CheckDisconnectListener checkDisconnectListener(SimpMessagingTemplate simpMessagingTemplate,
                                                    CheckPoolQueue checkPoolQueue) {
        return new CheckDisconnectListener(checkHandler(simpMessagingTemplate, checkPoolQueue));
    };


    /**
     * доступ к данным редиса
     */
//    @Bean("redisTemplate")
//    RedisTemplate<String, Object> redisTemplate() {
//        RedisTemplate<String, Object> template = new RedisTemplate<>();
//        template.setConnectionFactory(jedisConnectionFactory());
//        template.setValueSerializer(new GenericToStringSerializer<>(Object.class));
//        return template;
//    }

//    @Bean
//    MessagePublisher redisPublisher() {
//        return new RedisMessagePublisher(redisTemplate(), topic());
//    }
}
