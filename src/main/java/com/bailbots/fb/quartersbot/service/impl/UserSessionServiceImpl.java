package com.bailbots.fb.quartersbot.service.impl;

import com.bailbots.fb.quartersbot.dao.User;
import com.bailbots.fb.quartersbot.service.UserSessionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@EnableScheduling
public class UserSessionServiceImpl implements UserSessionService {
    private static final Logger LOGGER = LogManager.getLogger(UserSessionServiceImpl.class);

    private Map<String, User> usersInSession = new HashMap<>();
    private Map<String, Long> facebookIdToExpirationTime = new HashMap<>();

    @Value("${bot.session.time.millis}")
    private Long sessionTime;

    @Override
    public void addUserToSession(String facebookId, User user) {
        Long timestamp = System.currentTimeMillis();

        usersInSession.put(facebookId, user);
        facebookIdToExpirationTime.put(facebookId, timestamp + sessionTime);

        LOGGER.info("Facebook ID #{} successfully added to session", facebookId);
    }

    @Override
    public User getUserFromSession(String facebookId) {
        return usersInSession.get(facebookId);
    }

    @Scheduled(cron = "0 0/${bot.session.time.checkdelay} * * * *")
    private void removeExpiredUserSession() {
        Long currentTimestamp = System.currentTimeMillis();
        int startSize = usersInSession.size();

        Set<String> collect = facebookIdToExpirationTime.entrySet().stream()
                .filter(entry -> currentTimestamp > entry.getValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.toUnmodifiableSet());

        for (String facebookIdToRemove : collect) {
            usersInSession.remove(facebookIdToRemove);
            facebookIdToExpirationTime.remove(facebookIdToRemove);
        }

        LOGGER.info("Removed {} logged users", startSize - usersInSession.size());
    }
}
