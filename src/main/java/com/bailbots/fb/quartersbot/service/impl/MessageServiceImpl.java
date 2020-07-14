package com.bailbots.fb.quartersbot.service.impl;


import com.bailbots.fb.quartersbot.service.MessageService;
import com.github.messenger4j.Messenger;
import com.github.messenger4j.send.MessagePayload;
import com.github.messenger4j.send.MessagingType;
import com.github.messenger4j.send.NotificationType;
import com.github.messenger4j.send.SenderActionPayload;
import com.github.messenger4j.send.message.RichMediaMessage;
import com.github.messenger4j.send.message.TemplateMessage;
import com.github.messenger4j.send.message.TextMessage;
import com.github.messenger4j.send.message.quickreply.QuickReply;
import com.github.messenger4j.send.message.quickreply.TextQuickReply;
import com.github.messenger4j.send.message.richmedia.RichMediaAsset;
import com.github.messenger4j.send.message.richmedia.UrlRichMediaAsset;
import com.github.messenger4j.send.message.template.ButtonTemplate;
import com.github.messenger4j.send.message.template.GenericTemplate;
import com.github.messenger4j.send.message.template.button.Button;
import com.github.messenger4j.send.message.template.common.Element;
import com.github.messenger4j.send.recipient.IdRecipient;
import com.github.messenger4j.send.senderaction.SenderAction;
import com.github.messenger4j.webhook.event.attachment.RichMediaAttachment;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Optional.empty;
import static java.util.Optional.of;

@Service
public class MessageServiceImpl implements MessageService {
    private final Messenger messenger;

    public MessageServiceImpl(Messenger messenger) {
        this.messenger = messenger;
    }

    @Override
    public void sendMessage(String text, String recipientId) {
        IdRecipient recipient = IdRecipient.create(recipientId);
        NotificationType notificationType = NotificationType.REGULAR;

        TextMessage textMessage = TextMessage.create(text, empty(), empty());
        MessagePayload messagePayload = MessagePayload.create(recipient, MessagingType.RESPONSE, textMessage,
                of(notificationType), empty());
        sendMessageAction(messagePayload, recipientId);
    }

    @Override
    public void sendQuickReply(String recipientId, String text, List<QuickReply> quickReplyList) {
        TextMessage message = TextMessage.create(text, of(quickReplyList), empty());
        MessagePayload messagePayload = MessagePayload.create(recipientId, MessagingType.RESPONSE, message);
        sendMessageAction(messagePayload, recipientId);
    }

    @Override
    public void sendGenericMenu(GenericTemplate genericTemplate, String recipientId) {
        TemplateMessage templateMessage = TemplateMessage.create(genericTemplate);
        MessagePayload messagePayload = MessagePayload.create(recipientId, MessagingType.RESPONSE, templateMessage);
        sendMessageAction(messagePayload, recipientId);
    }

    @Override
    public void sendButtonList(String text, List<Button> buttonList, String recipientId) {
        ButtonTemplate buttonTemplate = ButtonTemplate.create(text, buttonList);
        TemplateMessage templateMessage = TemplateMessage.create(buttonTemplate);
        MessagePayload messagePayload = MessagePayload.create(recipientId, MessagingType.RESPONSE, templateMessage);
        sendMessageAction(messagePayload, recipientId);
    }

    @Override
    @SneakyThrows
    public void sendPhoto(String imageURL, String recipientId) {
        UrlRichMediaAsset richMediaAsset = UrlRichMediaAsset.create(RichMediaAsset.Type.IMAGE, new URL(imageURL));
        RichMediaMessage richMediaMessage = RichMediaMessage.create(richMediaAsset);
        MessagePayload messagePayload = MessagePayload.create(recipientId, MessagingType.RESPONSE, richMediaMessage);
        sendMessageAction(messagePayload, recipientId);
    }

    @Override
    public void sendGenericList(String recipientId, TemplateMessage templateMessage) {
        MessagePayload messagePayload = MessagePayload.create(recipientId, MessagingType.RESPONSE, templateMessage);
        sendMessageAction(messagePayload, recipientId);
    }


    @SneakyThrows
    private void sendMessageAction(MessagePayload messagePayload, String recipientId) {
        messenger.send(SenderActionPayload.create(recipientId, SenderAction.TYPING_ON));
        messenger.send(messagePayload);
    }
}
