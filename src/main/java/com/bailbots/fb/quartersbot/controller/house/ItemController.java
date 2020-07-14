package com.bailbots.fb.quartersbot.controller.house;

import com.bailbots.fb.quartersbot.bpp.annotation.BotController;
import com.bailbots.fb.quartersbot.bpp.annotation.BotRequestMapping;
import com.bailbots.fb.quartersbot.dao.House;
import com.bailbots.fb.quartersbot.dao.HouseImageData;
import com.bailbots.fb.quartersbot.repository.HouseRepository;
import com.bailbots.fb.quartersbot.service.KeyboardLoaderService;
import com.bailbots.fb.quartersbot.service.MessageService;
import com.github.messenger4j.webhook.Event;

@BotController("House")
public class ItemController {
    private final HouseRepository houseRepository;
    private final MessageService messageService;
    private final KeyboardLoaderService keyboardLoaderService;

    public ItemController(HouseRepository houseRepository, MessageService messageService, KeyboardLoaderService keyboardLoaderService) {
        this.houseRepository = houseRepository;
        this.messageService = messageService;
        this.keyboardLoaderService = keyboardLoaderService;
    }

    @BotRequestMapping("BackToList")
    public void backToList(Event event) {
        String facebookId = event.senderId();

        Integer page = Integer.parseInt(event.asPostbackEvent().payload().get().split("&")[2].trim());
        String keyboardName = event.asPostbackEvent().payload().get().split("&")[3].trim();

        messageService.sendGenericList(facebookId,
                keyboardLoaderService.getGenericMenuFromXML(keyboardName, page));
    }

    @BotRequestMapping("MorePhoto")
    public void morePhoto(Event event) {
        String facebookId = event.senderId();

        Long houseId = Long.parseLong(event.asPostbackEvent().payload().get().split("&")[2].trim());

        House house = houseRepository.getById(houseId);

        for(HouseImageData image : house.getImages()) {
            messageService.sendPhoto(image.getUrl(), facebookId);
        }
    }

    @BotRequestMapping("Detail")
    public void detail(Event event) {
        String facebookId = event.senderId();

        Long houseId = Long.parseLong(event.asPostbackEvent().payload().get().split("&")[2].trim());

        House house = houseRepository.getById(houseId);

        boolean swimmingPool = house.isSwimmingPool();
        boolean bath = house.isBath();

        String detailInformation = "Дом #" + house.getId()
                + "\n\nПодробное описание:\n"
                + house.getDetailInfo()
                + "\nБасейн: " + (swimmingPool ? "Есть ✅" : "Нету ❎")
                + "\nБаня: " + (bath ? "Есть ✅" : "Нету ❎");

        messageService.sendMessage(detailInformation, facebookId);
    }

    @BotRequestMapping("OwnerRequirements")
    public void ownerRequirements(Event event) {
        String facebookId = event.senderId();

        Long houseId = Long.parseLong(event.asPostbackEvent().payload().get().split("&")[2].trim());

        House house = houseRepository.getById(houseId);

        String detailInformation = "Дом #" + house.getId()
                + "\n\n"
                + "Требования владельца: \n" + house.getOwnerRequirements();

        messageService.sendMessage(detailInformation, facebookId);
    }

    @BotRequestMapping("AdditionalService")
    public void additionalService(Event event) {
        String facebookId = event.senderId();

        Long houseId = Long.parseLong(event.asPostbackEvent().payload().get().split("&")[2].trim());

        House house = houseRepository.getById(houseId);

        String detailInformation = "Дом #" + house.getId()
                + "\n\nДополнительные услуги:\n"
                + house.getAdditionalService();

        messageService.sendMessage(detailInformation, facebookId);
    }

}
