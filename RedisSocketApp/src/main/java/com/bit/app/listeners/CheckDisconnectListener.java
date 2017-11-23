package com.bit.app.listeners;

import com.bit.app.services.CheckHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@Component
public class CheckDisconnectListener implements ApplicationListener<SessionDisconnectEvent> {

    private CheckHandler checkHandler;

    @Autowired
    public CheckDisconnectListener(CheckHandler checkHandler) {
        this.checkHandler = checkHandler;
    }

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        log.warn("Session disconnect event: " + sessionId);
        checkHandler.freeResources(sessionId);
    }
}
