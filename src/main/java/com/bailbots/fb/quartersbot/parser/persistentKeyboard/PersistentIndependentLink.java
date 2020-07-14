package com.bailbots.fb.quartersbot.parser.persistentKeyboard;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.Data;

@Data
@JacksonXmlRootElement(localName = "persistent-independent-link")
public class PersistentIndependentLink {
    @JacksonXmlText
    private String text;

    @JacksonXmlProperty(isAttribute = true)
    private String payload;
}
