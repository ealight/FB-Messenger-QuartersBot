package com.bailbots.fb.quartersbot.service;

import com.bailbots.fb.quartersbot.parser.quickreplyKeyboard.QuickReplyButton;
import com.github.messenger4j.send.message.quickreply.QuickReply;

import java.util.List;

public interface CalendarService {
    List<QuickReply> getLinearCalendar(Integer page, Long forItemId, String requestCallback, Integer... args);
}
