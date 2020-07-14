package com.bailbots.fb.quartersbot.service;

import com.github.messenger4j.webhook.Event;

public interface MessageValueService {
    void getValueFromMessage(Event event, String facebookId);
    void turnGetMessageValueForUser(String facebookId, String action);
    boolean isGetValueTurn(String facebookId);
}
