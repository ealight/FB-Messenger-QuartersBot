package com.bailbots.fb.quartersbot.service.impl;

import com.bailbots.fb.quartersbot.dao.User;
import com.bailbots.fb.quartersbot.dto.RegistrationDto;
import com.bailbots.fb.quartersbot.repository.UserRepository;
import com.bailbots.fb.quartersbot.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LogManager.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User registerUser(RegistrationDto dto) {
        User user = User.builder()
                .facebookId(dto.getFacebookId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .build();

        LOGGER.info("Facebook ID #{} successfully register!",  dto.getFacebookId());

        return userRepository.save(user);
    }

    @Override
    public boolean isUserExist(String facebookId) {
        return userRepository.existsByFacebookId(facebookId);
    }

    @Override
    public User getByFacebookId(String facebookId) {
        return userRepository.getByFacebookId(facebookId);
    }
}
