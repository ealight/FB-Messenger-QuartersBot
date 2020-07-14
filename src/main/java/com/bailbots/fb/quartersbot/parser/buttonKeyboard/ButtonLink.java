package com.bailbots.fb.quartersbot.parser.buttonKeyboard;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.Data;

@Data
@JacksonXmlRootElement(localName = "button-link")
public class ButtonLink {
    @JacksonXmlText
    private String text;

    @JacksonXmlProperty(isAttribute = true)
    private String payload;
}
