package com.bailbots.fb.quartersbot.parser.quickreplyKeyboard;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@JacksonXmlRootElement(localName = "quick-reply-keyboard")
public class QuickReplyKeyboard {
    @JacksonXmlElementWrapper(useWrapping = false, localName = "quick-reply-button")
    @JacksonXmlProperty(localName = "quick-reply-button")
    private List<QuickReplyButton> quickReplies;

    @JacksonXmlProperty(isAttribute = true)
    private String controller;

    @Getter(AccessLevel.NONE)
    @JacksonXmlProperty(isAttribute = true)
    private String noNamespaceSchemaLocation;
}
