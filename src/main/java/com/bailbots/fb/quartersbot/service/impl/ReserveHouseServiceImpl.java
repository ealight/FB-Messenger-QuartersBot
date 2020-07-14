package com.bailbots.fb.quartersbot.service.impl;

import com.bailbots.fb.quartersbot.dao.House;
import com.bailbots.fb.quartersbot.dao.ReserveHouse;
import com.bailbots.fb.quartersbot.repository.ReserveHouseRepository;
import com.bailbots.fb.quartersbot.service.CalendarService;
import com.bailbots.fb.quartersbot.service.ReserveHouseService;
import com.bailbots.fb.quartersbot.utils.CalendarUtil;
import com.bailbots.fb.quartersbot.utils.callback.CallbackParser;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ReserveHouseServiceImpl implements ReserveHouseService {

    private final MessageServiceImpl messageService;
    private final ReserveHouseRepository reserveHouseRepository;
    private final CalendarService calendarService;

    public ReserveHouseServiceImpl(MessageServiceImpl messageService, ReserveHouseRepository reserveHouseRepository, CalendarService calendarService) {
        this.messageService = messageService;
        this.reserveHouseRepository = reserveHouseRepository;
        this.calendarService = calendarService;
    }

    @Override
    public boolean houseAlreadyReserved(String facebookId, CallbackParser parser, Date dateFrom, Date dateTo) {
        Long houseId = parser.getLongByName("id");

        if (null != reserveHouseRepository.getByHouseIdAndDateToAfterAndDateFromBefore(houseId, dateFrom, dateTo)) {
            ReserveHouse reserveHouse = reserveHouseRepository.getByHouseIdAndDateToAfterAndDateFromBefore(houseId, dateFrom, dateTo);
            sendHouseAlreadyReserveMessage(reserveHouse, facebookId, parser);
            return true;
        }

        if (null != reserveHouseRepository.getByHouseIdAndDateFromBeforeAndDateToAfter(houseId, dateFrom, dateTo)) {
            ReserveHouse reserveHouse = reserveHouseRepository.getByHouseIdAndDateFromBeforeAndDateToAfter(houseId, dateFrom, dateTo);
            sendHouseAlreadyReserveMessage(reserveHouse, facebookId, parser);
            return true;
        }

        return false;
    }

    @Override
    public boolean existByFacebookId(String facebookId, Long houseId) {
        if (reserveHouseRepository.existsByFacebookIdAndHouseId(facebookId, houseId)) {
            messageService.sendMessage("❌ Вы уже бронировали этот дом", facebookId);
            return true;
        }
        return false;
    }

    @Override
    public boolean dateFromMoreThenDateTo(String facebookId, Date dateFrom, Date dateTo) {
        if (dateFrom.compareTo(dateTo) > 0) {
            String text = "❌ Вы неверно выбрали дату"
                    + "\n\uD83D\uDCC6 Выбранная дата с: " + CalendarUtil.formatDate(dateFrom) + " по " + CalendarUtil.formatDate(dateTo);

            messageService.sendMessage(text, facebookId);
            return true;
        }
        return false;
    }

    @Override
    public boolean selectedPastDate(String facebookId, CallbackParser parser,Date dateFrom, Date dateTo) {
        Date currentDate = CalendarUtil.getCurrentDate();

        if (currentDate.compareTo(dateTo) > 0 || currentDate.compareTo(dateFrom) > 0) {
            String text = "❌ Вы не можете забронировать дом в прошлом"
                    + "\n\uD83D\uDCC6 Выбранная дата с: " + CalendarUtil.formatDate(dateFrom) + " по " + CalendarUtil.formatDate(dateTo);

            messageService.sendQuickReply(facebookId, text,
                    calendarService.getLinearCalendar(parser.getIntByName("page"), parser.getLongByName("id"), "choseDateFrom"));
            return true;
        }
        return false;
    }

    @Override
    public ReserveHouse reserve(String facebookId, Long houseId, Date dateFrom, Date dateTo) {
        String text = "Вы успешно создали заказ на бронь дома №" + houseId + " ✔"
                + "\n\uD83D\uDCC5 Дата: " + CalendarUtil.formatDate(dateFrom) + " - " + CalendarUtil.formatDate(dateTo)
                + "\nКогда владелец подтвердит вашу бронь, я вам отпишу :)";

        messageService.sendMessage(text, facebookId);

        return reserveHouseRepository.save(ReserveHouse.builder()
                .facebookId(facebookId)
                .houseId(houseId)
                .dateFrom(dateFrom)
                .dateTo(dateTo)
                .build());
    }

    @Override
    public void showReserveHouseDate(String facebookId, Long houseId) {
        ReserveHouse reserveHouse = reserveHouseRepository.getByHouseId(houseId);

        String text = "\uD83D\uDD50 Дата брони от: " + CalendarUtil.formatDate(reserveHouse.getDateFrom())
                + " до " + CalendarUtil.formatDate(reserveHouse.getDateTo());

        messageService.sendMessage(text, facebookId);
    }

    private void sendHouseAlreadyReserveMessage(ReserveHouse reserveHouse, String facebookId, CallbackParser parser) {
        Date dateFrom = reserveHouse.getDateFrom();
        Date dateTo = reserveHouse.getDateTo();

        String text = "❌ Извините, этот дом забронирован на это время"
                + "\n\uD83D\uDCC5 Дата с: " + CalendarUtil.formatDate(dateFrom) + " по " + CalendarUtil.formatDate(dateTo);

        messageService.sendQuickReply(facebookId, text,
                calendarService.getLinearCalendar(parser.getIntByName("page"), parser.getLongByName("id"), "choseDateFrom"));
    }

}
