package com.bailbots.fb.quartersbot.controller.main;

import com.bailbots.fb.quartersbot.bpp.annotation.BotController;
import com.bailbots.fb.quartersbot.bpp.annotation.BotRequestMapping;
import com.bailbots.fb.quartersbot.service.KeyboardLoaderService;
import com.bailbots.fb.quartersbot.service.MessageService;
import com.bailbots.fb.quartersbot.utils.Pageable;
import com.bailbots.fb.quartersbot.utils.callback.CallbackParser;
import com.github.messenger4j.webhook.Event;

@BotController("GenericMenu")
public class GenericMenuController {
    private final MessageService messageService;
    private final KeyboardLoaderService keyboardLoaderService;

    public GenericMenuController(MessageService messageService, KeyboardLoaderService keyboardLoaderService) {
        this.messageService = messageService;
        this.keyboardLoaderService = keyboardLoaderService;
    }

    @BotRequestMapping("Page")
    public void page(Event event) {
        String facebookId = event.senderId();
        String payload = event.asQuickReplyMessageEvent().payload();

        CallbackParser parser = CallbackParser.parseCallback(payload, "page", "maxPage", "keyboardName");

        messageService.sendGenericList(facebookId,
                keyboardLoaderService.getGenericMenuFromXML(parser.getStringByName("keyboardName"), parser.getIntByName("page")));
    }

    @BotRequestMapping("Next")
    public void next(Event event) {
        String facebookId = event.senderId();
        String payload = event.asQuickReplyMessageEvent().payload();

        CallbackParser parser = CallbackParser.parseCallback(payload, "page", "maxPage", "keyboardName");

        messageService.sendGenericList(facebookId,
                keyboardLoaderService.getGenericMenuFromXML(parser.getStringByName("keyboardName"),
                        Pageable.getNextPage(parser.getIntByName("maxPage"), parser.getIntByName("page"))));
    }

    @BotRequestMapping("Previous")
    public void previous(Event event) {
        String facebookId = event.senderId();
        String payload = event.asQuickReplyMessageEvent().payload();

        CallbackParser parser = CallbackParser.parseCallback(payload, "page", "maxPage", "keyboardName");

        messageService.sendGenericList(facebookId,
                keyboardLoaderService.getGenericMenuFromXML(parser.getStringByName("keyboardName"),
                        Pageable.getPreviousPage(parser.getIntByName("maxPage"), parser.getIntByName("page"))));
    }
}
