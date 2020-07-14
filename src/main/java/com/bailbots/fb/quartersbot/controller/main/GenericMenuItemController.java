package com.bailbots.fb.quartersbot.controller.main;

import com.bailbots.fb.quartersbot.bpp.annotation.BotController;
import com.bailbots.fb.quartersbot.bpp.annotation.BotRequestMapping;
import com.bailbots.fb.quartersbot.dao.House;
import com.bailbots.fb.quartersbot.dao.ReserveHouse;
import com.bailbots.fb.quartersbot.repository.UserMagazineRepository;
import com.bailbots.fb.quartersbot.service.CalendarService;
import com.bailbots.fb.quartersbot.service.KeyboardItemService;
import com.bailbots.fb.quartersbot.service.MessageService;
import com.bailbots.fb.quartersbot.service.ReserveHouseService;
import com.bailbots.fb.quartersbot.utils.callback.CallbackParser;
import com.bailbots.fb.quartersbot.utils.callback.CallbackUtil;
import com.github.messenger4j.webhook.Event;
import lombok.SneakyThrows;

@BotController("GenericMenuItem")
public class GenericMenuItemController {
    private final KeyboardItemService keyboardItemService;
    private final UserMagazineRepository userMagazineRepository;
    private final MessageService messageService;
    private final CalendarService calendarService;
    private final ReserveHouseService reserveHouseService;

    public GenericMenuItemController(KeyboardItemService keyboardItemService, UserMagazineRepository userMagazineRepository, MessageService messageService, CalendarService calendarService, ReserveHouseService reserveHouseService) {
        this.keyboardItemService = keyboardItemService;
        this.userMagazineRepository = userMagazineRepository;
        this.messageService = messageService;
        this.calendarService = calendarService;
        this.reserveHouseService = reserveHouseService;
    }

    @BotRequestMapping("Delete")
    public void delete(Event event) {
        String facebookId = event.senderId();

        Long houseId = Long.parseLong(event.asPostbackEvent().payload().get().split(CallbackUtil.SEPARATOR)[3].trim());

        userMagazineRepository.removeById(houseId);

        messageService.sendMessage("Дом #" + houseId + " успешно удален из вашего журнала", facebookId);
    }

    @BotRequestMapping("ReserveDate")
    public void reserveDate(Event event) {
        String facebookId = event.senderId();

        Long houseId = Long.parseLong(event.asPostbackEvent().payload().get().split(CallbackUtil.SEPARATOR)[3].trim());

        reserveHouseService.showReserveHouseDate(facebookId, houseId);
    }

    @BotRequestMapping("Reserve")
    public void reserve(Event event) {
        String facebookId = event.senderId();

        Long houseId = Long.parseLong(event.asPostbackEvent().payload().get().split(CallbackUtil.SEPARATOR)[3].trim());

        messageService.sendQuickReply(facebookId, "\uD83D\uDCCC Отличный выбор! Выберите дату приезда: ",
                calendarService.getLinearCalendar(1, houseId, "ChoseDateFrom"));
    }

    @BotRequestMapping("ToMagazine")
    public void toMagazine(Event event) {
        String facebookId = event.senderId();

        Long houseId = Long.parseLong(event.asPostbackEvent().payload().get().split(CallbackUtil.SEPARATOR)[3].trim());

        if(userMagazineRepository.houseIdAlreadyExistForFacebookId(houseId, facebookId)) {
            messageService.sendMessage("❌ Вы уже добавляли этом дом в свой журнал", facebookId);
            return;
        }

        userMagazineRepository.saveForFacebookId(facebookId, houseId);

        messageService.sendMessage("Дом #" + houseId + " успешно добавлен в ваш журнал", facebookId);
    }

    @BotRequestMapping("MoreDetails")
    @SneakyThrows
    public void moreDetails(Event event) {
        String facebookId = event.senderId();
        String payload = event.asPostbackEvent().payload().get();

        CallbackParser parser = CallbackParser.parseCallback(payload, "itemResponseMethod", "id");

        keyboardItemService.getClass().getDeclaredMethod(parser.getStringByName("itemResponseMethod"), String.class, Long.class)
                .invoke(keyboardItemService, facebookId, parser.getLongByName("id"));
    }
}
