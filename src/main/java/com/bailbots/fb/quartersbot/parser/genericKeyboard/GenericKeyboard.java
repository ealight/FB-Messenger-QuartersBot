package com.bailbots.fb.quartersbot.parser.genericKeyboard;

import com.bailbots.fb.quartersbot.parser.persistentKeyboard.PersistentCategory;
import com.bailbots.fb.quartersbot.parser.persistentKeyboard.PersistentLink;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@JacksonXmlRootElement(localName = "generic-keyboard")
public class GenericKeyboard {
    @JacksonXmlElementWrapper(useWrapping = false, localName = "generic-db-item")
    @JacksonXmlProperty(localName = "generic-db-item")
    private List<GenericDbItem> genericDbItems;

    @JacksonXmlElementWrapper(useWrapping = false, localName = "generic-quick-reply")
    @JacksonXmlProperty(localName = "generic-quick-reply")
    private List<GenericQuickReply> genericQuickReplies;

    @JacksonXmlProperty(isAttribute = true)
    private String controller;

    @Getter(AccessLevel.NONE)
    @JacksonXmlProperty(isAttribute = true)
    private String noNamespaceSchemaLocation;
}