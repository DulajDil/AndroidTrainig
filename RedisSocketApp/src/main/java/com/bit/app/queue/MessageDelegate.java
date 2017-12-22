package com.bit.app.queue;

import com.bit.app.entities.AbstractCheckEntity;
import com.bit.app.entities.CheckEntity;

import java.io.Serializable;

public interface MessageDelegate {

    void handleMessage(AbstractCheckEntity message, String channel);
}
