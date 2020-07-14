package com.bailbots.fb.quartersbot.controller;

import com.bailbots.fb.quartersbot.bpp.annotation.BotController;
import com.bailbots.fb.quartersbot.bpp.annotation.BotRequestMapping;
import com.bailbots.fb.quartersbot.service.KeyboardLoaderService;
import com.bailbots.fb.quartersbot.service.MessageService;
import com.github.messenger4j.webhook.Event;

@BotController("Menu")
public class MenuController {
    private final MessageService messageService;
    private final KeyboardLoaderService keyboardLoaderService;

    public MenuController(MessageService messageService, KeyboardLoaderService keyboardLoaderService) {
        this.messageService = messageService;
        this.keyboardLoaderService = keyboardLoaderService;
    }

    @BotRequestMapping("Start")
    public void start(Event event) {
        String facebookId = event.senderId();

        messageService.sendButtonList("Вы перешли в главное меню\nВыберите действие:",
                keyboardLoaderService.getButtonKeyboardFromXML("start"), facebookId);
    }

    @BotRequestMapping("House")
    public void house(Event event) {
        String facebookId = event.senderId();

        messageService.sendButtonList("Хорошо, выберите что вы хотите сделать:",
                keyboardLoaderService.getButtonKeyboardFromXML("choseHouse"), facebookId);
    }

    @BotRequestMapping("Profile")
    public void profile(Event event) {
        String facebookId = event.senderId();

        messageService.sendButtonList("Хорошо, выберите что вы хотите сделать:",
                keyboardLoaderService.getButtonKeyboardFromXML("profileUp"), facebookId);
        messageService.sendButtonList("↕",
                keyboardLoaderService.getButtonKeyboardFromXML("profileDown"), facebookId);
    }

}
