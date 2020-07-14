package com.bailbots.fb.quartersbot.service;

import com.bailbots.fb.quartersbot.dao.User;
import com.bailbots.fb.quartersbot.dto.RegistrationDto;

public interface UserService {
    User registerUser(RegistrationDto registrationDto);

    boolean isUserExist(String facebookId);

    User getByFacebookId(String facebookId);
}
