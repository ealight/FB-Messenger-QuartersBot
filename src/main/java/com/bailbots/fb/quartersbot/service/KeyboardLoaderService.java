package com.bailbots.fb.quartersbot.service;

import com.github.messenger4j.messengerprofile.persistentmenu.PersistentMenu;
import com.github.messenger4j.send.message.TemplateMessage;
import com.github.messenger4j.send.message.quickreply.QuickReply;
import com.github.messenger4j.send.message.template.GenericTemplate;
import com.github.messenger4j.send.message.template.button.Button;

import java.util.List;

public interface KeyboardLoaderService {
    PersistentMenu getPersistentMenuFromXML(String filename);
    List<Button> getButtonKeyboardFromXML(String filename, Long... itemId);
    TemplateMessage getGenericMenuFromXML(String filename, Integer page);
    List<QuickReply> getQuickReplyKeyboardFromXML(String filename);
    TemplateMessage getGenericMenuFromXMLWithRepository(String filename, Integer page, Object ...args);
}
