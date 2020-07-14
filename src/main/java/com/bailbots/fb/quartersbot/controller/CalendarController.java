package com.bailbots.fb.quartersbot.controller;

import com.bailbots.fb.quartersbot.bpp.annotation.BotController;
import com.bailbots.fb.quartersbot.bpp.annotation.BotRequestMapping;
import com.bailbots.fb.quartersbot.handler.Handler;
import com.bailbots.fb.quartersbot.service.CalendarService;
import com.bailbots.fb.quartersbot.service.MessageService;
import com.bailbots.fb.quartersbot.service.ReserveHouseService;
import com.bailbots.fb.quartersbot.utils.CalendarUtil;
import com.bailbots.fb.quartersbot.utils.callback.CallbackParser;
import com.github.messenger4j.webhook.Event;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

@BotController("Calendar")
public class CalendarController {
    private static final Logger LOGGER = LogManager.getLogger(CalendarController.class);

    private final MessageService messageService;
    private final CalendarService calendarService;
    private final ReserveHouseService reserveHouseService;

    public CalendarController(MessageService messageService, CalendarService calendarService, ReserveHouseService reserveHouseService) {
        this.messageService = messageService;
        this.calendarService = calendarService;
        this.reserveHouseService = reserveHouseService;
    }

    @BotRequestMapping("ChoseDateFrom")
    public void choseDateFrom(Event event) {
        String facebookId = event.senderId();
        String payload = event.asQuickReplyMessageEvent().payload();

        CallbackParser parser = CallbackParser.parseCallback(payload, "id", "year", "month", "day");

        if (reserveHouseService.existByFacebookId(facebookId, parser.getLongByName("id"))) {
            return;
        }

        Date date = CalendarUtil.getDate(parser.getIntByName("year"), parser.getIntByName("month"), parser.getIntByName("day"));

        String text = "\uD83D\uDCC6 Дата приезда " + CalendarUtil.formatDate(date) + " выбрана" +
                "\nТеперь выберите дату отьезда ⬇";

        messageService.sendQuickReply(facebookId, text,
                calendarService.getLinearCalendar(1, parser.getLongByName("id"), "ChoseDateTo",
                        parser.getIntByName("year"), parser.getIntByName("month"), parser.getIntByName("day")));
    }

    @BotRequestMapping("ChoseDateTo")
    public void choseDateTo(Event event) {
        String facebookId = event.senderId();
        String payload = event.asQuickReplyMessageEvent().payload();

        CallbackParser parser = CallbackParser.parseCallback(payload,
                "id", "yearTo", "monthTo", "dayTo", "yearFrom", "monthFrom", "dayFrom");

        Date dateFrom = CalendarUtil.getDate(
                parser.getIntByName("yearFrom"), parser.getIntByName("monthFrom"), parser.getIntByName("dayFrom"));
        Date dateTo = CalendarUtil.getDate(
                parser.getIntByName("yearTo"), parser.getIntByName("monthTo"), parser.getIntByName("dayTo"));

        if(reserveHouseService.dateFromMoreThenDateTo(facebookId, dateFrom, dateTo)) {
            return;
        }

        if(reserveHouseService.selectedPastDate(facebookId, parser, dateFrom, dateTo)) {
            return;
        }

        if(reserveHouseService.houseAlreadyReserved(facebookId, parser, dateFrom, dateTo)) {
            return;
        }

        try {
            reserveHouseService.reserve(facebookId, parser.getLongByName("id"), dateFrom, dateTo);
        }
        catch (Exception e) {
            messageService.sendMessage("Now we can't process this query :(" +
                    "\n Please wait a few minutes", facebookId);
            LOGGER.error(e);
        }
    }

    @BotRequestMapping("Next")
    public void next(Event event) {
        String facebookId = event.senderId();
        String payload = event.asQuickReplyMessageEvent().payload();

        CallbackParser parser = CallbackParser.parseCallback(payload, "id", "payload", "page", "dateFromYear", "dateFromMonth", "dateFromDay");

        messageService.sendQuickReply(facebookId, "Выберите дату: ",
                calendarService.getLinearCalendar(parser.getIntByName("page") + 1, parser.getLongByName("id"), parser.getStringByName("payload"),
                        parser.getIntByName("dateFromYear"), parser.getIntByName("dateFromMonth"), parser.getIntByName("dateFromDay")));
    }

    @BotRequestMapping("Previous")
    public void previous(Event event) {
        String facebookId = event.senderId();
        String payload = event.asQuickReplyMessageEvent().payload();

        CallbackParser parser = CallbackParser.parseCallback(payload, "id", "payload", "page", "dateFromYear", "dateFromMonth", "dateFromDay");

        if (parser.getIntByName("page") == 1) {
            messageService.sendQuickReply(facebookId, "Выберите дату: ",
                    calendarService.getLinearCalendar(parser.getIntByName("page"), parser.getLongByName("id"), parser.getStringByName("payload"),
                            parser.getIntByName("dateFromYear"), parser.getIntByName("dateFromMonth"), parser.getIntByName("dateFromDay")));
            return;
        }

        messageService.sendQuickReply(facebookId, "Выберите дату: ",
                calendarService.getLinearCalendar(parser.getIntByName("page") - 1, parser.getLongByName("id"), parser.getStringByName("payload"),
                        parser.getIntByName("dateFromYear"), parser.getIntByName("dateFromMonth"), parser.getIntByName("dateFromDay")));
    }

}
