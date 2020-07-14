package com.bailbots.fb.quartersbot.parser.quickreplyKeyboard;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.Data;

@Data
@JacksonXmlRootElement(localName = "quick-reply-button")
public class QuickReplyButton {
    @JacksonXmlText
    private String text;

    @JacksonXmlProperty(isAttribute = true)
    private String payload;
}
