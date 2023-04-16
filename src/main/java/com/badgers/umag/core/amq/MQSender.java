package com.badgers.umag.core.amq;

import com.badgers.umag.modules.supplies.models.requests.RecalculateRequest;

public interface MQSender {

    void send(RecalculateRequest data);

    void send();
}
