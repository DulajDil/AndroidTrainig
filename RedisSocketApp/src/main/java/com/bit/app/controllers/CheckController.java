package com.bit.app.controllers;

import com.bit.app.entities.Greeting;
import com.bit.app.entities.HelloMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class CheckController {

    @MessageMapping("/filter")
    @SendTo("/topic/greetings")
    public Greeting setFilter(HelloMessage message) throws InterruptedException {
        log.warn("Start greeting controller " + message.getName());
//        Thread.sleep(1500);
        return new Greeting(String.format("Check name, %s!", message.getName()));
    }
}
