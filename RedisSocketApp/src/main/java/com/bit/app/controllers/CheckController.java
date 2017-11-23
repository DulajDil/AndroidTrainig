package com.bit.app.controllers;

import com.bit.app.entities.FilterMessage;
import com.bit.app.services.CheckHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class CheckController {

    @Autowired
    CheckHandler checkHandler;

    @MessageMapping("/filter")
    public void filter(FilterMessage filterMessage, SimpMessageHeaderAccessor headers) throws InterruptedException {
        log.warn("Start filter controller " + filterMessage.getCategory());
        log.warn("Getting session id: " + headers.getSessionId());
        checkHandler.get(headers.getSessionId());
    }

//    @MessageMapping("/filter")
//    @SendTo("/topic/check-entity")
//    public CheckEntity filter(FilterMessage filterMessage, SimpMessageHeaderAccessor headers) throws InterruptedException {
//        log.warn("Start filter controller " + filterMessage.getCategory());
//        subscribeManager.filter(filterMessage, headers.getSessionId());
//        CheckEntity test_igor = CheckEntity.builder().name("test igor").build();
//        return test_igor;
//    }
}
