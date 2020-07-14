package com.bailbots.fb.quartersbot.controller;

import com.bailbots.fb.quartersbot.bpp.annotation.BotController;
import com.bailbots.fb.quartersbot.bpp.annotation.BotRequestMapping;
import com.bailbots.fb.quartersbot.repository.HouseRepository;
import com.bailbots.fb.quartersbot.service.KeyboardLoaderService;
import com.bailbots.fb.quartersbot.service.MessageService;
import com.bailbots.fb.quartersbot.service.UserSessionService;
import com.github.messenger4j.webhook.Event;

@BotController("Menu")
public class ProfileController {
    private static final Long NULL_LONG = 0L;
    private static final Integer NULL_INT = 0;

    private final MessageService messageService;
    private final KeyboardLoaderService keyboardLoaderService;
    private final UserSessionService userSessionService;
    private final HouseRepository houseRepository;

    public ProfileController(MessageService messageService, KeyboardLoaderService keyboardLoaderService, UserSessionService userSessionService, HouseRepository houseRepository) {
        this.messageService = messageService;
        this.keyboardLoaderService = keyboardLoaderService;
        this.userSessionService = userSessionService;
        this.houseRepository = houseRepository;
    }

    @BotRequestMapping("Notifications")
    public void notifications(Event event) {
        String facebookId = event.senderId();

        messageService.sendButtonList("Выберите хотите ли вы включить или выключить уведомления",
                keyboardLoaderService.getButtonKeyboardFromXML("profile/notifications"), facebookId);
    }

    @BotRequestMapping("ReservedHouses")
    public void reserveHouses(Event event) {
        String facebookId = event.senderId();

        if(houseRepository.countByFacebookId(facebookId, "reserve_house").equals(NULL_LONG)) {
            messageService.sendMessage("Ваш список забронированных домов пусть", facebookId);
            return;
        }

        messageService.sendGenericList(facebookId,
                keyboardLoaderService.getGenericMenuFromXMLWithRepository("profile/reserveHousesList", NULL_INT,
                        facebookId, "reserve_house"));
    }

    @BotRequestMapping("Magazine")
    public void magazine(Event event) {
        String facebookId = event.senderId();

        if(houseRepository.countByFacebookId(facebookId, "user_magazine").equals(NULL_LONG)) {
            messageService.sendMessage("Ваш журнал понравившихся домов пуст", facebookId);
            return;
        }

        messageService.sendGenericList(facebookId,
                keyboardLoaderService.getGenericMenuFromXMLWithRepository("profile/magazineList", NULL_INT,
                        facebookId, "user_magazine"));
    }
}
