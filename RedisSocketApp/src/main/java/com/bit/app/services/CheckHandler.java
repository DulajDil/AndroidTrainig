package com.bit.app.services;

import com.bit.app.entities.CheckEntity;
import com.bit.app.queue.CheckViewQueue;
import com.bit.app.queue.CheckPoolQueue;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class CheckHandler {

    private SimpMessagingTemplate simpMessagingTemplate;
    private CheckPoolQueue checkPoolQueue;
    @Getter
    private final Map<String, SessionWorker> sessionMap = new ConcurrentHashMap<>();
    private Integer countThread = 0;


    @Autowired
    public CheckHandler(SimpMessagingTemplate simpMessagingTemplate,
                 CheckPoolQueue checkPoolQueue) {
       this.simpMessagingTemplate = simpMessagingTemplate;
       this.checkPoolQueue = checkPoolQueue;
    }

    /**
     * запуск потока для юзера:
     * @param sessionId (id юзера)
     */
    private void runWorker(String sessionId) {
        log.warn("start new worker from user: " + sessionId);
        CheckPoolQueue.CheckQueue checkQueue = checkPoolQueue.createSession();
        SessionWorker sessionWorker = new SessionWorker(sessionId, checkQueue);

        sessionMap.put(sessionId, sessionWorker);
        sessionWorker.start();
        countThread++;
    }

    /**
     *  останавливаем поток для юзера:
     *  @param sessionId (id юзера)
     */
    private void stopWorker(String sessionId) {
        SessionWorker sessionWorker = sessionMap.get(sessionId);

        if (sessionWorker != null) {
            sessionWorker.deactivate();
            sessionMap.remove(sessionId);
            countThread--;
            log.warn("stop worker from user: " + sessionId);
        }
    }

    /**
     * освобождаем ресурсы от:
     * @param sessionId юзера
     */
    public void freeResources(String sessionId) {
        stopWorker(sessionId);
    }

    /**
     * точка запуска обработчика
     * @param sessionId
     */
    public void get(String sessionId) {
        stopWorker(sessionId);

        runWorker(sessionId);
    }

    /**
     * отправка сообщений клиенту
     */
    private void sendMessage(String sessionId, CheckEntity checkEntity) {
        log.warn(String.format("Count threads: %s", countThread));
        simpMessagingTemplate.convertAndSendToUser(
                sessionId,
                "/topic/check-entity",
                checkEntity,
                createHeaders(sessionId));
        log.warn(String.format("Send message to /user/topic/check-entity: %s, session id: %s", checkEntity, sessionId));
    }

    /**
     * исполнитель задач, в отдельном потоке
     */
    public class SessionWorker extends Thread {

        private final String sessionId;
        private CheckPoolQueue.CheckQueue checkQueue;
        private CheckViewQueue checkViewQueue;

        SessionWorker(String sessionId, CheckPoolQueue.CheckQueue checkQueue) {
            super("CheckQueue-worker-" + sessionId);
            this.sessionId = sessionId;
            this.checkQueue = checkQueue;
        }

        @Override
        public void run() {
            checkViewQueue = checkQueue.getCheckViewQueue();
            CheckEntity checkEntity = CheckEntity.builder().build();
            while (!Thread.interrupted()) {
                try {
                    String name = checkViewQueue.take();
                    checkEntity.setName(name);
                    sendMessage(sessionId, checkEntity);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }

        /**
         *
         */
        public void deactivate() {
            checkQueue.unregister();
            this.interrupt();
        }
    }

    /**
     * создаем заголовки для отправки сообщений подписки socket
     * @param sessionId
     * @return
     */
    private MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }
}
