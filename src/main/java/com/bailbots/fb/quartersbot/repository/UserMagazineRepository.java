package com.bailbots.fb.quartersbot.repository;

public interface UserMagazineRepository {
    void saveForFacebookId(String facebookId, Long houseId);
    void removeById(Long houseId);
    boolean houseIdAlreadyExistForFacebookId(Long houseId, String facebookId);
}

