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
package fr.lixbox.service.msteams.model.action;

import java.io.Serializable;
import java.util.Objects;

import fr.lixbox.io.json.JsonUtil;

/**
 * https://docs.microsoft.com/en-us/outlook/actionable-messages/message-card-reference#actions
 *
 * @author ludovic.terral
 */
public class Target implements Serializable
{
    // ----------- Attribut(s) -----------   
    private static final long serialVersionUID = 202012181141L;
    
    private OperatingSystem os = OperatingSystem.DEFAULT;
    private String uri;

    

    // ----------- Methode(s) -----------   
    public Target()
    {
    }
    public Target(String uri)
    {
        this.uri = uri;
    }
    public Target(OperatingSystem os, String uri)
    {
        this.os = os;
        this.uri = uri;
    }

    
    
    public OperatingSystem getOs()
    {
        return os;
    }
    public void setOs(OperatingSystem os)
    {
        this.os = os;
    }
    
    
    
    public String getUri()
    {
        return uri;
    }
    public void setUri(String uri)
    {
        this.uri = uri;
    }
    
    
    
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Target target = (Target) o;
        return os == target.os && Objects.equals(uri, target.uri);
    }



    @Override
    public int hashCode()
    {
        return Objects.hash(os, uri);
    }



    @Override
    public String toString()
    {
        return JsonUtil.transformObjectToJson(this, false);
    }
}
