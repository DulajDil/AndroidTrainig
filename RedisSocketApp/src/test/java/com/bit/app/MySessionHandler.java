package com.bit.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.*;

import java.lang.reflect.Type;

@Slf4j
public class MySessionHandler extends StompSessionHandlerAdapter {

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        session.subscribe("/user/topic/check-entity", new StompFrameHandler() {

            public Type getPayloadType(StompHeaders stompHeaders) {
                log.warn("Stomp headers: " + stompHeaders);
                return byte[].class;
            }

            public void handleFrame(StompHeaders stompHeaders, Object o) {
                String s = new String((byte[]) o);
                log.warn("Received subscribe " + s);
            }
        });
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        log.error("" + exception);
    }
}
