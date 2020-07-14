package com.bailbots.fb.quartersbot.parser.buttonKeyboard;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@JacksonXmlRootElement(localName = "button-keyboard")
public class ButtonKeyboard {
    @JacksonXmlElementWrapper(useWrapping = false, localName = "button-link")
    @JacksonXmlProperty(localName = "button-link")
    private List<ButtonLink> links;

    @JacksonXmlProperty(isAttribute = true)
    private String controller;

    @Getter(AccessLevel.NONE)
    @JacksonXmlProperty(isAttribute = true)
    private String noNamespaceSchemaLocation;
}
