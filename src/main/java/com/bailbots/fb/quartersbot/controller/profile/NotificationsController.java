package com.bailbots.fb.quartersbot.controller.profile;

import com.bailbots.fb.quartersbot.bpp.annotation.BotController;
import com.bailbots.fb.quartersbot.bpp.annotation.BotRequestMapping;
import com.bailbots.fb.quartersbot.dao.User;
import com.bailbots.fb.quartersbot.repository.UserRepository;
import com.bailbots.fb.quartersbot.service.MessageService;
import com.bailbots.fb.quartersbot.service.UserSessionService;
import com.github.messenger4j.webhook.Event;


@BotController("Notifications")
public class NotificationsController {
    private final MessageService messageService;
    private final UserRepository userRepository;
    private final UserSessionService userSessionService;

    public NotificationsController(MessageService messageService, UserRepository userRepository, UserSessionService userSessionService) {
        this.messageService = messageService;
        this.userRepository = userRepository;
        this.userSessionService = userSessionService;
    }

    @BotRequestMapping("Enable")
    public void enable(Event event) {
        String facebookId = event.senderId();

        User user = userSessionService.getUserFromSession(facebookId);

        if(user.isNotifications()) {
            messageService.sendMessage("❌ Уведомления уже включены", facebookId);
            return;
        }

        user.setNotifications(true);
        userRepository.save(user);

        String text = "Уведомления успешно включены \uD83D\uDD14" +
                "\n\uD83D\uDCE3 Каждый день в 7:00 я буду оповещать вас о ваших бронях";

        messageService.sendMessage(text, facebookId);
    }

    @BotRequestMapping("Disable")
    public void disable(Event event) {
        String facebookId = event.senderId();

        User user = userSessionService.getUserFromSession(facebookId);

        if(!user.isNotifications()) {
            messageService.sendMessage("❌ Уведомления уже выключены", facebookId);
            return;
        }

        user.setNotifications(false);
        userRepository.save(user);

        messageService.sendMessage("Уведомления успешно выключены \uD83D\uDD15", facebookId);
    }
}
