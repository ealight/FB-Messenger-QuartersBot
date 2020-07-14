package com.bailbots.fb.quartersbot.service.impl;

import com.bailbots.fb.quartersbot.dao.User;
import com.bailbots.fb.quartersbot.service.MessageService;
import com.bailbots.fb.quartersbot.service.MessageValueService;
import com.bailbots.fb.quartersbot.service.UserSessionService;
import com.github.messenger4j.webhook.Event;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MessageValueServiceImpl implements MessageValueService {
    private final MessageService messageService;
    private final UserSessionService userSessionService;

    private Map<String, String> facebookIdToAction = new HashMap<>();

    public MessageValueServiceImpl(MessageService messageService, UserSessionService userSessionService) {
        this.messageService = messageService;
        this.userSessionService = userSessionService;
    }

    @Override
    public void getValueFromMessage(Event event, String facebookId) {

        switch(facebookIdToAction.get(facebookId)) {

            case "houseSeatsNumber": {
                int minSeatsNumber;

                try {
                    minSeatsNumber = Integer.parseInt(event.asTextMessageEvent().text());
                }
                catch (NumberFormatException e) {
                    messageService.sendMessage("❌ Введите количество мест в чат цифрой", facebookId);
                    return;
                }

                User user = userSessionService.getUserFromSession(facebookId);

                user.getHouseFilter().setMinSeatsNumber(minSeatsNumber);

                String text = "Хорошо, мы подберем вам дома с количеством мест больше " + minSeatsNumber + "."
                        + " Желаете еще поставить какие-то фильтры?";

                messageService.sendMessage(text, facebookId);

                facebookIdToAction.remove(facebookId);
                break;
            }

            case "houseMinPrice": {
                int price;

                try {
                    price = Integer.parseInt(event.asTextMessageEvent().text());
                }
                catch (NumberFormatException e) {
                    messageService.sendMessage("❌ Введите минимальную цену в чат цифрой", facebookId);
                    return;
                }

                User user = userSessionService.getUserFromSession(facebookId);

                user.getHouseFilter().setMinPrice(price);

                String text = "Хорошо, мы подберем вам дома с минимальной ценой " + price + "."
                        + " Желаете еще поставить какие-то фильтры?";

                messageService.sendMessage(text, facebookId);

                facebookIdToAction.remove(facebookId);
                break;
            }

            case "houseMaxPrice": {
                int price;

                try {
                    price = Integer.parseInt(event.asTextMessageEvent().text());
                }
                catch (NumberFormatException e) {
                    messageService.sendMessage("❌ Введите максимальную цену в чат цифрой", facebookId);
                    return;
                }

                User user = userSessionService.getUserFromSession(facebookId);

                user.getHouseFilter().setMaxPrice(price);

                String text = "Хорошо, мы подберем вам дома с максимальной ценой " + price + "."
                        + " Желаете еще поставить какие-то фильтры?";

                messageService.sendMessage(text, facebookId);

                facebookIdToAction.remove(facebookId);
                break;
            }

        }

    }

    @Override
    public void turnGetMessageValueForUser(String facebookId, String action) {
        facebookIdToAction.put(facebookId, action);
    }

    @Override
    public boolean isGetValueTurn(String facebookId) {
        return facebookIdToAction.containsKey(facebookId);
    }

}
