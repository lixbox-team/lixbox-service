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

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import fr.lixbox.io.json.JsonUtil;

/**
 * https://docs.microsoft.com/en-us/outlook/actionable-messages/message-card-reference#dateinput
 *
 * @author ludovic.terral
 */
@JsonTypeName("DateInput")
public class DateInput implements Input
{
    // ----------- Attribut(s) -----------
    private static final long serialVersionUID = 202012181135L;
    
    private String id;
    @JsonProperty("isRequired") private boolean required;
    private String title;
    private String value;

    

    // ----------- Methode(s) -----------
    public DateInput()
    {
    }
    public DateInput(String id, String title)
    {
        this.id = id;
        this.title = title;
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
    
    
    
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateInput dateInput = (DateInput) o;
        return Objects.equals(id, dateInput.id) && Objects.equals(required, dateInput.required)
                && Objects.equals(title, dateInput.title) && Objects.equals(value, dateInput.value);
    }



    @Override
    public int hashCode()
    {
        return Objects.hash(id, required, title, value);
    }



    public String toString()
    {
        return JsonUtil.transformObjectToJson(this, false);
    }
}
