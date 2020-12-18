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
 * https://docs.microsoft.com/en-us/outlook/actionable-messages/card-reference#image-object
 *
 * @author ludovic.terral
 */
public class Image implements Serializable
{
    // ----------- Attribut(s) -----------   
    private static final long serialVersionUID = 202012181131L;
    
    private String image;
    private String title;

    
    
    // ----------- Methode(s) -----------   
    public Image()
    {
    }
    public Image(String image, String title)
    {
        this.image = image;
        this.title = title;
    }



    // ----------- Methode(s) -----------   
    public String getImage()
    {
        return this.image;
    }
    public void setImage(String image)
    {
        this.image = image;
    }



    public String getTitle()
    {
        return this.title;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }



    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image1 = (Image) o;
        return Objects.equals(image, image1.image) && Objects.equals(title, image1.title);
    }



    @Override
    public int hashCode()
    {
        return Objects.hash(image, title);
    }



    public String toString()
    {
        return JsonUtil.transformObjectToJson(this, false);
    }
}
