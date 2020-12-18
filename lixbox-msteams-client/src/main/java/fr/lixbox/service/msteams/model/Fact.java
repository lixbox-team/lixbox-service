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

import java.io.Serializable;
import java.util.Objects;

import fr.lixbox.io.json.JsonUtil;

/**
 * https://docs.microsoft.com/en-us/outlook/actionable-messages/message-card-reference#section-fields
 * 
 * @author ludovic.terral
 */
public class Fact implements Serializable
{
    // ----------- Attribut(s) -----------   
    private static final long serialVersionUID = 202012181132L;
    
    private String name;
    private String value;

    
    
    // ----------- Methode(s) -----------   
    public Fact()
    {
    }
    public Fact(String name, String value)
    {
        this.name = name;
        this.value = value;
    }



    public String getName()
    {
        return this.name;
    }
    public void setName(String name)
    {
        this.name = name;
    }



    public String getValue()
    {
        return this.value;
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
        Fact fact = (Fact) o;
        return Objects.equals(name, fact.name) && Objects.equals(value, fact.value);
    }



    @Override
    public int hashCode()
    {
        return Objects.hash(name, value);
    }



    public String toString()
    {
        return JsonUtil.transformObjectToJson(this, false);
    }
}
