package com.bailbots.fb.quartersbot;

import com.github.messenger4j.Messenger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BotInitializer {

    @Bean
    public Messenger messenger(@Value("${bot.facebook.pageAccessToken}") String pageAccessToken,
                               @Value("${bot.facebook.appSecret}") final String appSecret,
                               @Value("${bot.facebook.verifyToken}") final String verifyToken) {
        return Messenger.create(pageAccessToken, appSecret, verifyToken);
    }

    public static void main(String[] args) {
        SpringApplication.run(BotInitializer.class, args);
    }
}
