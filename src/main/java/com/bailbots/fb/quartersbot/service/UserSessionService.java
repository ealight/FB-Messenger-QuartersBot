package com.bailbots.fb.quartersbot.service;

import com.bailbots.fb.quartersbot.dao.User;

public interface UserSessionService {
    void addUserToSession(String facebookId, User user);

    User getUserFromSession(String facebookId);
}
