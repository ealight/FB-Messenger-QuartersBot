package com.bailbots.fb.quartersbot.controller;

import com.bailbots.fb.quartersbot.bpp.annotation.BotController;
import com.bailbots.fb.quartersbot.bpp.annotation.BotRequestMapping;
import com.bailbots.fb.quartersbot.service.KeyboardLoaderService;
import com.bailbots.fb.quartersbot.service.MessageService;
import com.github.messenger4j.webhook.Event;

@BotController("StartButtonController")
public class StartButtonController {
    private final MessageService messageService;
    private final KeyboardLoaderService keyboardLoaderService;

    public StartButtonController(MessageService messageService, KeyboardLoaderService keyboardLoaderService) {
        this.messageService = messageService;
        this.keyboardLoaderService = keyboardLoaderService;
    }

    @BotRequestMapping("PressedStart")
    public void pressedStartButton(Event event) {
        String facebookId = event.senderId();

        messageService.sendButtonList("Привет! Выберите дейтсвие",
                keyboardLoaderService.getButtonKeyboardFromXML("start"), facebookId);
    }
}
