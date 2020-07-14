package com.bailbots.fb.quartersbot.parser.genericKeyboard;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.util.List;

@Data
@JacksonXmlRootElement(localName = "generic-quick-reply")
public class GenericQuickReply {
    @JacksonXmlElementWrapper(useWrapping = false, localName = "quick-reply-button")
    @JacksonXmlProperty(localName = "quick-reply-button")
    private List<GenericQuickReplyButton> buttons;
}
