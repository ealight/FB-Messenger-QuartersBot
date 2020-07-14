package com.bailbots.fb.quartersbot.bpp;

import com.bailbots.fb.quartersbot.bpp.container.BotApiMethodContainer;
import com.github.messenger4j.webhook.Event;

public class BotSelectHandle {
    private static BotApiMethodContainer container = BotApiMethodContainer.getInstanse();

    public static void processByUpdate(Event event) {
        String path;
        BotApiMethodController controller = null;

        if (event.isTextMessageEvent() && !event.asTextMessageEvent().text().isEmpty()) {
            path = event.asTextMessageEvent().text();

            controller = container.getControllerMap().get(path);
            if (controller == null) controller = container.getControllerMap().get("");
        } else if (event.isQuickReplyMessageEvent()) {
            String botControllerValue = event.asQuickReplyMessageEvent().payload().split("&")[0].trim();
            String requestMappingValue = event.asQuickReplyMessageEvent().payload().split("&")[1].trim();

            path = botControllerValue + requestMappingValue;

            controller = container.getControllerMap().get(path);
        } else if (event.isPostbackEvent()) {
            String botControllerValue = event.asPostbackEvent().payload().get().split("&")[0].trim();
            String requestMappingValue = event.asPostbackEvent().payload().get().split("&")[1].trim();

            path = botControllerValue + requestMappingValue;

            controller = container.getControllerMap().get(path);
        }

        if (controller == null) {
            return;
        }

        controller.process(event);
    }
}
