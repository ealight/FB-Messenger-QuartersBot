package com.bailbots.fb.quartersbot.controller;

import com.bailbots.fb.quartersbot.bpp.annotation.BotController;
import com.bailbots.fb.quartersbot.bpp.annotation.BotRequestMapping;
import com.bailbots.fb.quartersbot.service.KeyboardLoaderService;
import com.bailbots.fb.quartersbot.service.MessageService;
import com.github.messenger4j.webhook.Event;

@BotController("Menu")
public class HouseController {
    private final MessageService messageService;
    private final KeyboardLoaderService keyboardLoaderService;

    public HouseController(MessageService messageService, KeyboardLoaderService keyboardLoaderService) {
        this.messageService = messageService;
        this.keyboardLoaderService = keyboardLoaderService;
    }

    @BotRequestMapping("AllHouses")
    public void allHouses(Event event) {
        String senderId = event.senderId();

        messageService.sendGenericList(senderId,
                keyboardLoaderService.getGenericMenuFromXML("allHouses", 0));
    }

    @BotRequestMapping("Filters")
    public void filters(Event event) {
        String facebookId = event.senderId();

        messageService.sendButtonList("Хорошо, выберите фильтры:",
                keyboardLoaderService.getButtonKeyboardFromXML("house/filtersUp"), facebookId);

        messageService.sendButtonList("↕",
                keyboardLoaderService.getButtonKeyboardFromXML("house/filtersDown"), facebookId);
    }
}
