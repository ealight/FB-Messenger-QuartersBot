package com.bailbots.fb.quartersbot.service;

import com.github.messenger4j.send.message.TemplateMessage;
import com.github.messenger4j.send.message.quickreply.QuickReply;
import com.github.messenger4j.send.message.template.GenericTemplate;
import com.github.messenger4j.send.message.template.button.Button;

import java.util.List;

public interface MessageService {
    void sendMessage(String text, String recipientId);
    void sendQuickReply(String recipientId, String text, List<QuickReply> quickReplyList);
    void sendButtonList(String text, List<Button> buttonList, String recipientId);
    void sendPhoto(String imageURL, String recipientId);
    void sendGenericMenu(GenericTemplate genericTemplate, String recipientId);
    void sendGenericList(String recipientId, TemplateMessage templateMessage);
}
