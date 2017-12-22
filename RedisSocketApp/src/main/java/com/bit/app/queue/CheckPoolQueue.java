package com.bit.app.queue;


import com.bit.app.entities.AbstractCheckEntity;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CheckPoolQueue {

    @Getter
    private ConcurrentHashMap<Long, CheckViewQueue> queuePool = new ConcurrentHashMap<>();
    private final AtomicLong queueKeyCounter = new AtomicLong();

    /**
     *
     * @return
     */
    public CheckQueue createSession() {
        return new CheckQueue();
    }

    /**
     *
     * @param message
     */
    public void pushState(AbstractCheckEntity message) {
        for (CheckViewQueue queue : queuePool.values()) {
            queue.offer(message);
        }
    }

    /**
     * очередь
     */
    public class CheckQueue {

        private final Long key;
        @Getter
        private final CheckViewQueue checkViewQueue;

        CheckQueue() {
            checkViewQueue = new CheckViewQueue();
            key = queueKeyCounter.getAndIncrement();

            queuePool.put(key, checkViewQueue);
        }

        /**
         *
         */
        public void unregister() {
            queuePool.remove(key);
        }
    }
}
