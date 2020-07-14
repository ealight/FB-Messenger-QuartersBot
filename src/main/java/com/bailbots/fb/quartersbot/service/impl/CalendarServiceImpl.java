package com.bailbots.fb.quartersbot.service.impl;

import com.bailbots.fb.quartersbot.parser.quickreplyKeyboard.QuickReplyButton;
import com.bailbots.fb.quartersbot.service.CalendarService;
import com.bailbots.fb.quartersbot.utils.CalendarUtil;
import com.bailbots.fb.quartersbot.utils.callback.CallbackBuilder;
import com.bailbots.fb.quartersbot.utils.callback.CallbackUtil;
import com.github.messenger4j.send.message.quickreply.QuickReply;
import com.github.messenger4j.send.message.quickreply.TextQuickReply;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class CalendarServiceImpl implements CalendarService {

    @Override
    public List<QuickReply> getLinearCalendar(Integer page, Long forItemId, String requestCallback, Integer... args) {
        Integer currentDay = CalendarUtil.getCurrentDay();
        Integer currentYear = CalendarUtil.getCurrentYear();
        Integer currentMonth = 0;

        List<QuickReply> quickReplyList = new ArrayList<>();

        List<Integer> dates = IntStream.range(currentDay + (page - 1) * 10, currentDay + page * 10)
                .boxed()
                .collect(Collectors.toList());

        for (Integer day : dates) {
            Calendar calendar = CalendarUtil.getCalendarDate(currentYear, currentMonth, day);

            Integer year = calendar.get(Calendar.YEAR);
            Integer month = calendar.get(Calendar.MONTH);
            Integer dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

            CallbackBuilder callback = CallbackBuilder.createCallback();
            callback.appendAll("Calendar", requestCallback, forItemId, year, month, dayOfMonth);

            if (args.length != 0) {
                callback.appendAll(args[0], args[1], args[2]);
            }

            quickReplyList.add(TextQuickReply.create(CalendarUtil.formatDate(calendar.getTime()), callback.toString()));
        }

        CallbackBuilder navigationCallback = CallbackBuilder.createCallback();
        navigationCallback.appendAll(forItemId, requestCallback, page);

        if (args.length != 0) {
            navigationCallback.appendAll(args[0], args[1], args[2]);
        } else {
            navigationCallback.appendAll(0, 0, 0);
        }

        quickReplyList.add(0, TextQuickReply.create("<< Назад", "Calendar&Previous&" + navigationCallback));
        quickReplyList.add(TextQuickReply.create("Вперед >>", "Calendar&Next&" + navigationCallback));

        return quickReplyList;
    }
}
