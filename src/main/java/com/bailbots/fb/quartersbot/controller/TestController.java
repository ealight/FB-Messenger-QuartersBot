package com.bailbots.fb.quartersbot.controller;

import com.bailbots.fb.quartersbot.bpp.annotation.BotController;
import com.bailbots.fb.quartersbot.bpp.annotation.BotRequestMapping;
import com.bailbots.fb.quartersbot.service.NotificationService;
import com.github.messenger4j.webhook.Event;

@BotController
public class TestController {
    private final NotificationService notificationService;

    public TestController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @BotRequestMapping("SendNotForAll")
    public void sayHi(Event event) {
        notificationService.sendNotificationAboutReserveHouse();
    }
}
