package com.bailbots.fb.quartersbot.controller.main;

import com.bailbots.fb.quartersbot.bpp.annotation.BotController;
import com.bailbots.fb.quartersbot.bpp.annotation.BotRequestMapping;
import com.bailbots.fb.quartersbot.service.KeyboardLoaderService;
import com.bailbots.fb.quartersbot.service.MessageService;
import com.bailbots.fb.quartersbot.utils.Pageable;
import com.bailbots.fb.quartersbot.utils.callback.CallbackParser;
import com.bailbots.fb.quartersbot.utils.callback.CallbackUtil;
import com.github.messenger4j.webhook.Event;

import java.util.List;

@BotController("GenericMenuRepository")
public class GenericMenuRepositoryController {
    private final MessageService messageService;
    private final KeyboardLoaderService keyboardLoaderService;

    public GenericMenuRepositoryController(MessageService messageService, KeyboardLoaderService keyboardLoaderService) {
        this.messageService = messageService;
        this.keyboardLoaderService = keyboardLoaderService;
    }

    @BotRequestMapping("Page")
    public void page(Event event) {
        String facebookId = event.senderId();
        String payload = event.asQuickReplyMessageEvent().payload();

        CallbackParser parser = CallbackParser.parseCallback(payload, "page", "maxPage", "keyboardName");

        List parameters = CallbackUtil.parametersParser(payload);

        messageService.sendGenericList(facebookId,
                keyboardLoaderService.getGenericMenuFromXMLWithRepository(parser.getStringByName("keyboardName"),
                        parser.getIntByName("page"), parameters.toArray()));
    }

    @BotRequestMapping("Next")
    public void next(Event event) {
        String facebookId = event.senderId();
        String payload = event.asQuickReplyMessageEvent().payload();

        CallbackParser parser = CallbackParser.parseCallback(payload, "page", "maxPage", "keyboardName");

        List parameters = CallbackUtil.parametersParser(payload);

        messageService.sendGenericList(facebookId,
                keyboardLoaderService.getGenericMenuFromXMLWithRepository(parser.getStringByName("keyboardName"),
                        Pageable.getNextPage(parser.getIntByName("maxPage"), parser.getIntByName("page")), parameters.toArray()));
    }

    @BotRequestMapping("Previous")
    public void previous(Event event) {
        String facebookId = event.senderId();
        String payload = event.asQuickReplyMessageEvent().payload();

        CallbackParser parser = CallbackParser.parseCallback(payload, "page", "maxPage", "keyboardName");

        List parameters = CallbackUtil.parametersParser(payload);

        messageService.sendGenericList(facebookId,
                keyboardLoaderService.getGenericMenuFromXMLWithRepository(parser.getStringByName("keyboardName"),
                        Pageable.getPreviousPage(parser.getIntByName("maxPage"), parser.getIntByName("page")), parameters.toArray()));
    }
}
