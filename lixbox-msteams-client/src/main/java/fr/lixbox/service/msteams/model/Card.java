/*******************************************************************************
 *    
 *                           FRAMEWORK Lixbox
 *                          ==================
 *      
 *    This file is part of lixbox-service.
 *
 *    lixbox-service is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    lixbox-service is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *    along with lixbox-service.  If not, see <https://www.gnu.org/licenses/>
 *   
 *   @AUTHOR Lixbox-team
 *
 ******************************************************************************/
package fr.lixbox.service.msteams.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.lixbox.io.json.JsonUtil;
import fr.lixbox.service.msteams.model.action.Action;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Adapted from
 * https://docs.microsoft.com/en-us/outlook/actionable-messages/card-reference
 *
 * @author ludovic.terral
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Card implements Serializable
{
    // ----------- Attribut(s) -----------
    private static final long serialVersionUID = 202012181133L;
    
    @JsonIgnore private String type = "MessageCard";
    @JsonIgnore private String context = "http://schema.org/extensions";
    private UUID correlationId;
    private String originator;
    private String summary;
    private String themeColor;
    private String title;
    private String text;
    private List<Section> sections; 
    @JsonProperty("potentialAction") private List<Action> potentialActions;

    
    
    // ----------- Methode(s) -----------   
    public Card()
    {
    }
    public Card(String summary, String themeColor, String title)
    {
        this.summary = summary;
        this.themeColor = themeColor;
        this.title = title;
    }
    public Card(String summary, String themeColor, String title, String text)
    {
        this.summary = summary;
        this.themeColor = themeColor;
        this.title = title;
        this.text = text;
    }
    public Card(String type, String context, UUID correlationId, String originator, String summary,
            String themeColor, String title, String text, List<Section> sections,
            List<Action> potentialActions)
    {
        this.type = type;
        this.context = context;
        this.correlationId = correlationId;
        this.originator = originator;
        this.summary = summary;
        this.themeColor = themeColor;
        this.title = title;
        this.text = text;
        this.sections = sections;
        this.potentialActions = potentialActions;
    }



    public String getType()
    {
        return type;
    }
    public void setType(String type)
    {
        this.type = type;
    }
    
    
    
    public String getContext()
    {
        return context;
    }
    public void setContext(String context)
    {
        this.context = context;
    }
    
    
    
    public UUID getCorrelationId()
    {
        return correlationId;
    }
    public void setCorrelationId(UUID correlationId)
    {
        this.correlationId = correlationId;
    }
    
    
    
    public String getOriginator()
    {
        return originator;
    }
    public void setOriginator(String originator)
    {
        this.originator = originator;
    }
    
    
    
    public String getSummary()
    {
        return summary;
    }
    public void setSummary(String summary)
    {
        this.summary = summary;
    }
    
    
    
    public String getThemeColor()
    {
        return themeColor;
    }
    public void setThemeColor(String themeColor)
    {
        this.themeColor = themeColor;
    }
    public void setThemeColor(Color themeColor)
    {
        this.themeColor = String.format("#%02x%02x%02x", themeColor.getRed(), themeColor.getGreen(),
                themeColor.getBlue()).toUpperCase().substring(1);
    }
    
    
    
    public String getTitle()
    {
        return title;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }
    
    
    
    public String getText()
    {
        return text;
    }
    public void setText(String text)
    {
        this.text = text;
    }
    
    
    
    public List<Section> getSections()
    {
        return sections;
    }
    public void setSections(List<Section> sections)
    {
        this.sections = sections;
    }
    
    
    
    public List<Action> getPotentialActions()
    {
        return potentialActions;
    }
    public void setPotentialActions(List<Action> potentialActions)
    {
        this.potentialActions = potentialActions;
    }
    
    
    
    public void addPotentialAction(Action... actions)
    {
        if (null == this.potentialActions) this.potentialActions = new ArrayList<>();
        for (Action action : actions)
            this.potentialActions.add(action);
    }



    public void addSection(Section... sections)
    {
        if (null == this.sections) this.sections = new ArrayList<>();
        for (Section section : sections)
            this.sections.add(section);
    }



    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(type, card.type) && Objects.equals(context, card.context)
                && Objects.equals(correlationId, card.correlationId)
                && Objects.equals(originator, card.originator)
                && Objects.equals(summary, card.summary)
                && Objects.equals(themeColor, card.themeColor) && Objects.equals(title, card.title)
                && Objects.equals(text, card.text) && Objects.equals(sections, card.sections)
                && Objects.equals(potentialActions, card.potentialActions);
    }



    @Override
    public int hashCode()
    {
        return Objects.hash(type, context, correlationId, originator, summary, themeColor, title,
                text, sections, potentialActions);
    }



    public String toString()
    {
        return JsonUtil.transformObjectToJson(this, false);
    }
}
