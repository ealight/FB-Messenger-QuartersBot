package com.bailbots.fb.quartersbot.controller.house;

import com.bailbots.fb.quartersbot.bpp.annotation.BotController;
import com.bailbots.fb.quartersbot.bpp.annotation.BotRequestMapping;
import com.bailbots.fb.quartersbot.dao.User;
import com.bailbots.fb.quartersbot.repository.HouseRepository;
import com.bailbots.fb.quartersbot.service.KeyboardLoaderService;
import com.bailbots.fb.quartersbot.service.MessageService;
import com.bailbots.fb.quartersbot.service.MessageValueService;
import com.bailbots.fb.quartersbot.service.UserSessionService;
import com.github.messenger4j.webhook.Event;

@BotController("Menu")
public class FilterController {
    private final MessageService messageService;
    private final MessageValueService messageValueService;
    private final KeyboardLoaderService keyboardLoaderService;
    private final UserSessionService userSessionService;
    private final HouseRepository houseRepository;

    public FilterController(MessageService messageService, MessageValueService messageValueService, KeyboardLoaderService keyboardLoaderService, UserSessionService userSessionService, HouseRepository houseRepository) {
        this.messageService = messageService;
        this.messageValueService = messageValueService;
        this.keyboardLoaderService = keyboardLoaderService;
        this.userSessionService = userSessionService;
        this.houseRepository = houseRepository;
    }

    @BotRequestMapping("SeatsNumber")
    public void seatsNumber(Event event) {
        String facebookId = event.senderId();

        messageValueService.turnGetMessageValueForUser(facebookId, "houseSeatsNumber");

        messageService.sendMessage("Хорошо, введите сообщением ниже минимальное количество мест в доме:", facebookId);
    }

    @BotRequestMapping("Price")
    public void price(Event event) {
        String facebookId = event.senderId();

        messageService.sendButtonList("Выберите какую цену вы хотите установить максимальную, или минимальную",
                keyboardLoaderService.getButtonKeyboardFromXML("house/priceFilter"), facebookId);
    }

    @BotRequestMapping("MoreFilters")
    public void moreFilters(Event event) {
        String facebookId = event.senderId();

        messageService.sendButtonList("Нажмите на фильтр, что-бы выбрать",
                keyboardLoaderService.getButtonKeyboardFromXML("house/moreFilters"), facebookId);
    }

    @BotRequestMapping("MaxPrice")
    public void maxPrice(Event event) {
        String facebookId = event.senderId();

        messageValueService.turnGetMessageValueForUser(facebookId, "houseMaxPrice");

        messageService.sendMessage("Хорошо, введите сообщением ниже максимальную цену:", facebookId);
    }

    @BotRequestMapping("MinPrice")
    public void minPrice(Event event) {
        String facebookId = event.senderId();

        messageValueService.turnGetMessageValueForUser(facebookId, "houseMinPrice");

        messageService.sendMessage("Хорошо, введите сообщением ниже минимальную цену:", facebookId);
    }

    @BotRequestMapping("SwimmingPool")
    public void swimmingPool(Event event) {
        String facebookId = event.senderId();

        User user = userSessionService.getUserFromSession(facebookId);

        boolean swimmingPool = !user.getHouseFilter().isSwimmingPool();

        user.getHouseFilter().setSwimmingPool(swimmingPool);

        String text = swimmingPool ? "✅ Басейн включен в фильтры" : "❎ Басейн выключен из фильтров";

        messageService.sendMessage(text, facebookId);
    }

    @BotRequestMapping("Bath")
    public void bath(Event event) {
        String facebookId = event.senderId();

        User user = userSessionService.getUserFromSession(facebookId);

        boolean bath = !user.getHouseFilter().isBath();

        user.getHouseFilter().setBath(bath);

        String text = bath ? "✅ Баня включена в фильтры" : "❎ Баня выключена из фильтров";

        messageService.sendMessage(text, facebookId);
    }

    @BotRequestMapping("ShowHouses")
    public void houses(Event event) {
        String facebookId = event.senderId();

        User user = userSessionService.getUserFromSession(facebookId);

        int minSeatsNumber = user.getHouseFilter().getMinSeatsNumber();
        int minPrice = user.getHouseFilter().getMinPrice();
        int maxPrice = user.getHouseFilter().getMaxPrice();
        boolean swimmingPool = user.getHouseFilter().isSwimmingPool();
        boolean bath = user.getHouseFilter().isBath();

        String filtersText = "\n\nМинимальное количетсво мест: " + minSeatsNumber
                + "\nМинимальная цена: " + minPrice
                + "\nМаксимальная цена: " + maxPrice
                + "\nБасейн: " + (swimmingPool ? "Да ✅" : "Нет ❎")
                + "\nБаня: " + (bath ? "Да ✅" : "Нет ❎");

        if(houseRepository.countByFilters(minSeatsNumber, minPrice, maxPrice, swimmingPool, bath).equals(0L)) {
            messageService.sendMessage("Мы не можем найти дома с такими фильтрами: " + filtersText, facebookId);
            return;
        }

        messageService.sendMessage(filtersText, facebookId);
        messageService.sendGenericList(facebookId,
                keyboardLoaderService.getGenericMenuFromXMLWithRepository("filterHouses", 0, minSeatsNumber, minPrice, maxPrice, swimmingPool, bath));
    }

}
