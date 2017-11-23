package com.bit.app.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CountDownLatch;

@Slf4j
public class Receiver {

//    private CountDownLatch latch;
//
//    @Autowired
//    public Receiver(CountDownLatch latch) {
//        this.latch = latch;
//    }
//
//    public void receiveMessage(String message) {
//        log.info(String.format("Getting redis message %s", message));
//        latch.countDown();
//    }
}
