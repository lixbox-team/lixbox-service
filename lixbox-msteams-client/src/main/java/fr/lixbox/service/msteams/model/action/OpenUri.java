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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonTypeName;

import fr.lixbox.io.json.JsonUtil;

/**
 * https://docs.microsoft.com/en-us/outlook/actionable-messages/card-reference#openuri-action
 *
 * @author ludovic.terral
 */
@JsonTypeName("OpenUri")
public class OpenUri implements Action
{
    // ----------- Attribut(s) -----------
    private static final long serialVersionUID = 202012181144L;
    private String name;
    private List<Target> targets;

    

    // ----------- Methode(s) -----------
    public OpenUri()
    {
    }
    public OpenUri(String name, String defaultTarget)
    {
        this.name = name;
        this.targets = Arrays.asList(new Target(defaultTarget));
    }
    public OpenUri(String name, List<Target> targets)
    {
        this.name = name;
        this.targets = targets;
    }

    
    
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    
    
    
    public List<Target> getTargets()
    {
        return targets;
    }
    public void setTargets(List<Target> targets)
    {
        this.targets = targets;
    }
    
    
    
    public void addTarget(Target... targets)
    {
        if (null == this.targets) this.targets = new ArrayList<>();
        for (Target target : targets)
            this.targets.add(target);
    }



    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpenUri openUri = (OpenUri) o;
        return Objects.equals(name, openUri.name) && Objects.equals(targets, openUri.targets);
    }



    @Override
    public int hashCode()
    {
        return Objects.hash(name, targets);
    }



    public String toString()
    {
        return JsonUtil.transformObjectToJson(this, false);
    }
}
