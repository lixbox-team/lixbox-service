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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonTypeName;

import fr.lixbox.io.json.JsonUtil;
import fr.lixbox.service.msteams.model.input.Input;

/**
 * From
 * https://docs.microsoft.com/en-us/outlook/actionable-messages/card-reference#actioncard-action
 * 
 * @author ludovic.terral
 */
@JsonTypeName("ActionCard")
public class ActionCard implements Action, Serializable
{
    // ----------- Attribut(s) -----------
    private static final long serialVersionUID = 202012181149L;
    
    private String name;
    private List<Input> inputs;
    private List<Action> actions;

    

    // ----------- Methode(s) -----------
    public ActionCard()
    {
    }
    public ActionCard(String name)
    {
        this.name = name;
    }
    public ActionCard(String name, List<Input> inputs, List<Action> actions)
    {
        this.name = name;
        this.inputs = inputs;
        this.actions = actions;
    }



    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    
    
    
    public List<Input> getInputs()
    {
        return inputs;
    }
    public void setInputs(List<Input> inputs)
    {
        this.inputs = inputs;
    }
    
    
    
    public List<Action> getActions()
    {
        return actions;
    }
    public void setActions(List<Action> actions)
    {
        this.actions = actions;
    }
    
    
    
    public void addInput(Input... inputs)
    {
        if (null == this.inputs) this.inputs = new ArrayList<>();
        for (Input input : inputs)
            this.inputs.add(input);
    }



    public void addAction(Action... actions)
    {
        if (null == this.actions) this.actions = new ArrayList<>();
        for (Action action : actions)
            this.actions.add(action);
    }



    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActionCard that = (ActionCard) o;
        return Objects.equals(name, that.name) && Objects.equals(inputs, that.inputs)
                && Objects.equals(actions, that.actions);
    }



    @Override
    public int hashCode()
    {
        return Objects.hash(name, inputs, actions);
    }



    @Override
    public String toString()
    {
        return JsonUtil.transformObjectToJson(this, false);
    }
}
