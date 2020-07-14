package com.bailbots.fb.quartersbot.parser.genericKeyboard;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.util.List;

@Data
@JacksonXmlRootElement(localName = "generic-db-item")
public class GenericDbItem {
    @JacksonXmlElementWrapper(useWrapping = false, localName = "generic-button")
    @JacksonXmlProperty(localName = "generic-button")
    private List<GenericButton> buttons;

    @JacksonXmlProperty(isAttribute = true)
    private String entity;

    @JacksonXmlProperty(isAttribute = true)
    private String maxItemsOnPage;

    @JacksonXmlProperty(isAttribute = true)
    private String itemResponseMethod;

    @JacksonXmlProperty(isAttribute = true)
    private String methodForTitle;

    @JacksonXmlProperty(isAttribute = true)
    private String methodForSubtitle;

    @JacksonXmlProperty(isAttribute = true)
    private String methodForImage;

    @JacksonXmlProperty(isAttribute = true)
    private String methodForPayload;

    @JacksonXmlProperty(isAttribute = true)
    private String imageEntity;

    @JacksonXmlProperty(isAttribute = true)
    private String methodForUrlImage;

    @JacksonXmlProperty(isAttribute = true)
    private String repository;

    @JacksonXmlProperty(isAttribute = true)
    private String repoMethod;
}
