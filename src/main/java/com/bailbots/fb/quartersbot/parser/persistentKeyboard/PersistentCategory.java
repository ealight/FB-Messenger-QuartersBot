package com.bailbots.fb.quartersbot.parser.persistentKeyboard;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.util.List;

@Data
@JacksonXmlRootElement(localName = "persistent-category")
public class PersistentCategory {

    @JacksonXmlElementWrapper(useWrapping = false, localName = "persistent-link")
    @JacksonXmlProperty(localName = "persistent-link")
    private List<PersistentLink> links;

    @JacksonXmlProperty(isAttribute = true)
    private String title;
}
