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
import java.util.Objects;

import fr.lixbox.io.json.JsonUtil;

/**
 * @author ludovic.terral
 */
public class MultichoiceOption implements Serializable
{
    // ----------- Attribut(s) -----------
    private static final long serialVersionUID = 202012181137L;
    
    private String display;
    private String value;

    

    // ----------- Methode(s) -----------
    public MultichoiceOption()
    {
    }
    public MultichoiceOption(String display, String value)
    {
        this.display = display;
        this.value = value;
    }



    public String getDisplay()
    {
        return display;
    }
    public void setDisplay(String display)
    {
        this.display = display;
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
        MultichoiceOption option = (MultichoiceOption) o;
        return Objects.equals(display, option.display) && Objects.equals(value, option.value);
    }



    @Override
    public int hashCode()
    {
        return Objects.hash(display, value);
    }



    public String toString()
    {
        return JsonUtil.transformObjectToJson(this, false);
    }
}
