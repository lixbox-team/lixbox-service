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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.lixbox.io.json.JsonUtil;
import fr.lixbox.service.msteams.model.action.Action;

import java.io.Serializable;
import java.util.*;

/**
 * https://docs.microsoft.com/en-us/outlook/actionable-messages/message-card-reference#section-fields
 * 
 * @author ludovic.terral
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Section implements Serializable
{
    // ----------- Attribut(s) -----------   
    private static final long serialVersionUID = 202012181130L;
    
    private String title;
    private boolean startGroup;
    private String activityTitle;
    private String activitySubtitle;
    private String activityImage;
    private String activityText;
    private Image heroImage;
    private String text;
    private List<Fact> facts;
    private List<Image> images;
    private boolean markdown;
    @JsonProperty("potentialAction") private List<Action> potentialActions;

    

    // ----------- Methode(s) -----------   
    public Section()
    {
    }
    public Section(String activityTitle, String activitySubtitle, String activityImage)
    {
        this.activityTitle = activityTitle;
        this.activitySubtitle = activitySubtitle;
        this.activityImage = activityImage;
    }
    public Section(String activityTitle, String activitySubtitle, String activityImage,
            String activityText)
    {
        this.activityTitle = activityTitle;
        this.activitySubtitle = activitySubtitle;
        this.activityImage = activityImage;
        this.activityText = activityText;
    }



    public Section(String title, boolean startGroup, String activityTitle, String activitySubtitle,
            String activityImage, String activityText, Image heroImage, String text,
            List<Fact> facts, List<Image> images, List<Action> potentialActions)
    {
        this.title = title;
        this.startGroup = startGroup;
        this.activityTitle = activityTitle;
        this.activitySubtitle = activitySubtitle;
        this.activityImage = activityImage;
        this.activityText = activityText;
        this.heroImage = heroImage;
        this.text = text;
        this.facts = facts;
        this.images = images;
        this.potentialActions = potentialActions;
    }


    public String getTitle()
    {
        return title;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }
    
    
    
    public boolean getStartGroup()
    {
        return startGroup;
    }
    public void setStartGroup(boolean startGroup)
    {
        this.startGroup = startGroup;
    }
    
    
    
    public String getActivityTitle()
    {
        return activityTitle;
    }
    public void setActivityTitle(String activityTitle)
    {
        this.activityTitle = activityTitle;
    }
    
    
    
    public String getActivitySubtitle()
    {
        return activitySubtitle;
    }
    public void setActivitySubtitle(String activitySubtitle)
    {
        this.activitySubtitle = activitySubtitle;
    }
    
    
    
    public String getActivityImage()
    {
        return activityImage;
    }
    public void setActivityImage(String activityImage)
    {
        this.activityImage = activityImage;
    }
    
    
    
    public String getActivityText()
    {
        return activityText;
    }
    public void setActivityText(String activityText)
    {
        this.activityText = activityText;
    }
    
    
    
    public Image getHeroImage()
    {
        return heroImage;
    }
    public void setHeroImage(Image heroImage)
    {
        this.heroImage = heroImage;
    }
    
    
    
    public String getText()
    {
        return text;
    }
    public void setText(String text)
    {
        this.text = text;
    }
    
    
    
    public List<Fact> getFacts()
    {
        return facts;
    }
    public void setFacts(List<Fact> facts)
    {
        this.facts = facts;
    }
    
    
    
    public List<Image> getImages()
    {
        return images;
    }
    public void setImages(List<Image> images)
    {
        this.images = images;
    }
    
    
    
    public boolean isMarkdown()
    {
        return markdown;
    }
    public void setMarkdown(boolean markdown)
    {
        this.markdown = markdown;
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
        this.potentialActions.addAll(Arrays.asList(actions));
    }



    public void addFact(String name, String value)
    {
        addFact(new Fact(name, value));
    }



    public void addFact(Fact... facts)
    {
        if (null == this.facts) this.facts = new ArrayList<>();
        this.facts.addAll(Arrays.asList(facts));
    }



    public void addImage(Image... images)
    {
        if (null == this.images) this.images = new ArrayList<>();
        Collections.addAll(this.images, images);
    }



    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return Objects.equals(title, section.title)
                && Objects.equals(startGroup, section.startGroup)
                && Objects.equals(activityTitle, section.activityTitle)
                && Objects.equals(activitySubtitle, section.activitySubtitle)
                && Objects.equals(activityImage, section.activityImage)
                && Objects.equals(activityText, section.activityText)
                && Objects.equals(heroImage, section.heroImage)
                && Objects.equals(text, section.text) && Objects.equals(facts, section.facts)
                && Objects.equals(images, section.images)
                && Objects.equals(potentialActions, section.potentialActions);
    }



    @Override
    public int hashCode()
    {
        return Objects.hash(title, startGroup, activityTitle, activitySubtitle, activityImage,
                activityText, heroImage, text, facts, images, potentialActions);
    }



    public String toString()
    {
        return JsonUtil.transformObjectToJson(this, false);
    }
}
