package com.bailbots.fb.quartersbot.repository;

import com.bailbots.fb.quartersbot.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByFacebookId(String facebookId);

    User getByFacebookId(String facebookId);
}
