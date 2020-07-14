package com.bailbots.fb.quartersbot.parser.genericKeyboard;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.Data;

@Data
@JacksonXmlRootElement(localName = "generic-button")
public class GenericButton {
    @JacksonXmlText
    private String text;

    @JacksonXmlProperty(isAttribute = true)
    private String payload;
}
