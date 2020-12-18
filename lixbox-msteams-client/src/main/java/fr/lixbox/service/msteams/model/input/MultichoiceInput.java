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
package fr.lixbox.service.msteams.model.input;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import fr.lixbox.io.json.JsonUtil;

/**
 * https://docs.microsoft.com/en-us/outlook/actionable-messages/message-card-reference#multichoiceinput
 *
 * @author ludovic.terral
 */
@JsonTypeName("MultichoiceInput")
public class MultichoiceInput implements Input, Serializable
{
    // ----------- Attribut(s) -----------
    private static final long serialVersionUID = 202012181134L;
    
    private String id;
    @JsonProperty("isRequired") private boolean required;
    private String title;
    private String value;
    private List<MultichoiceOption> choices;
    @JsonProperty("isMultiSelect") private boolean multiSelect;
    private Style style;

    

    // ----------- Methode(s) -----------
    public MultichoiceInput()
    {
    }
    public MultichoiceInput(String id, String title)
    {
        this.id = id;
        this.title = title;
    }
    public MultichoiceInput(String id, boolean required, String title, String value,
            List<MultichoiceOption> choices, boolean multiSelect, Style style)
    {
        this.id = id;
        this.required = required;
        this.title = title;
        this.value = value;
        this.choices = choices;
        this.multiSelect = multiSelect;
        this.style = style;
    }



    public String getId()
    {
        return id;
    }
    public void setId(String id)
    {
        this.id = id;
    }



    public boolean isRequired()
    {
        return required;
    }
    public void setRequired(boolean required)
    {
        this.required = required;
    }
    
    
    
    public String getTitle()
    {
        return title;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }



    public String getValue()
    {
        return value;
    }
    public void setValue(String value)
    {
        this.value = value;
    }



    public List<MultichoiceOption> getOptions()
    {
        return choices;
    }
    public void setOptions(List<MultichoiceOption> choices)
    {
        this.choices = choices;
    }
    public void addOptions(MultichoiceOption... options)
    {
        if (null == this.choices) this.choices = new ArrayList<>();
        for (MultichoiceOption option : options)
            this.choices.add(option);
    }


    public boolean isMultiSelect()
    {
        return multiSelect;
    }
    public void setMultiSelect(boolean multiSelect)
    {
        this.multiSelect = multiSelect;
    }



    public Style getStyle()
    {
        return style;
    }
    public void setStyle(Style style)
    {
        this.style = style;
    }



    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MultichoiceInput that = (MultichoiceInput) o;
        return Objects.equals(id, that.id) && Objects.equals(required, that.required)
                && Objects.equals(title, that.title) && Objects.equals(value, that.value)
                && Objects.equals(choices, that.choices)
                && Objects.equals(multiSelect, that.multiSelect) && style == that.style;
    }



    @Override
    public int hashCode()
    {
        return Objects.hash(id, required, title, value, choices, multiSelect, style);
    }
    
    
    
    public String toString()
    {
        return JsonUtil.transformObjectToJson(this, false);
    }
}