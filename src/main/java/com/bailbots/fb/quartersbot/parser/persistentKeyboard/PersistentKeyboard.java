package com.bailbots.fb.quartersbot.parser.persistentKeyboard;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@JacksonXmlRootElement(localName = "persistent-keyboard")
public class PersistentKeyboard {
    @JacksonXmlElementWrapper(useWrapping = false, localName = "persistent-category")
    @JacksonXmlProperty(localName = "persistent-category")
    private List<PersistentCategory> categories;

    @JacksonXmlElementWrapper(useWrapping = false, localName = "persistent-independent-link")
    @JacksonXmlProperty(localName = "persistent-independent-link")
    private List<PersistentLink> independentLinks;

    @JacksonXmlProperty(isAttribute = true)
    private String controller;

    @Getter(AccessLevel.NONE)
    @JacksonXmlProperty(isAttribute = true)
    private String noNamespaceSchemaLocation;
}
