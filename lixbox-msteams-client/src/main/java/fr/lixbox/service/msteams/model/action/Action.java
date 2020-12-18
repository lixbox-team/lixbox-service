package fr.lixbox.service.msteams.model.action;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * @author ludovic.terral
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
@JsonSubTypes({ @JsonSubTypes.Type(value = ActionCard.class, name = "ActionCard"),
        @JsonSubTypes.Type(value = HttpPost.class, name = "HttpPOST"),
        @JsonSubTypes.Type(value = OpenUri.class, name = "OpenUri") })
public interface Action
{
    String getName();
}
