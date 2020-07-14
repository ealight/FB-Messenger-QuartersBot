package com.bailbots.fb.quartersbot.service.impl;

import com.bailbots.fb.quartersbot.dao.House;
import com.bailbots.fb.quartersbot.repository.HouseRepository;
import com.bailbots.fb.quartersbot.service.KeyboardItemService;
import com.bailbots.fb.quartersbot.service.KeyboardLoaderService;
import com.bailbots.fb.quartersbot.service.MessageService;
import com.github.messenger4j.send.message.template.GenericTemplate;
import com.github.messenger4j.send.message.template.button.PostbackButton;
import com.github.messenger4j.send.message.template.common.Element;
import com.github.messenger4j.webhook.Event;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Optional.empty;
import static java.util.Optional.of;

@Service
public class KeyboardItemServiceImpl implements KeyboardItemService {
    private final MessageService messageService;
    private final HouseRepository houseRepository;
    private final KeyboardLoaderService keyboardLoaderService;

    public KeyboardItemServiceImpl(MessageService messageService, HouseRepository houseRepository, KeyboardLoaderService keyboardLoaderService) {
        this.messageService = messageService;
        this.houseRepository = houseRepository;
        this.keyboardLoaderService = keyboardLoaderService;
    }

    @Override
    public void house(String facebookId, Long houseId) {
        House house = houseRepository.getById(houseId);

        messageService.sendGenericMenu(getGenericHouse(house), facebookId);
        messageService.sendButtonList("↕", keyboardLoaderService.getButtonKeyboardFromXML("buttonHouse", houseId), facebookId);
    }

    @SneakyThrows
    private GenericTemplate getGenericHouse(House house) {
        String imageUrl = house.getImages().get(0).getUrl();

        String caption = "Дом #" + house.getId() + "\n\n"
                + "Описание: " + house.getDescription()  + "\n"
                + "Номер владельца: " + house.getOwnerPhoneNumber();

        List<Element> elements = new ArrayList<>();

        elements.add(Element.create(house.getName(),
                of(caption),
                of(new URL(imageUrl)),
                empty(),
                of(Arrays.asList(PostbackButton.create("Больше фото", "House" + "&" + "MorePhoto" + "&" + house.getId()),
                        PostbackButton.create("Детальнее", "House" + "&" + "Detail" + "&" + house.getId())))));
        return GenericTemplate.create(elements);
    }
}
