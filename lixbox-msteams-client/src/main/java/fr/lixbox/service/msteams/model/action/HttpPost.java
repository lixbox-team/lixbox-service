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
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonTypeName;

import fr.lixbox.io.json.JsonUtil;

/**
 *  https://docs.microsoft.com/en-us/outlook/actionable-messages/card-reference#httppost-action
 *  
 *  @author ludovic.terral
 */
@JsonTypeName("HttpPOST")
public class HttpPost implements Action
{
    // ----------- Attribut(s) -----------
    private static final long serialVersionUID = 202012181145L;

    private String name;
    private String target;
    private List<Header> headers;
    private String body;
    private String bodyContentType = "application/json";


    
    // ----------- Methode(s) -----------
    public HttpPost()
    {
    }
    public HttpPost(String name, String target)
    {
        this.name = name;
        this.target = target;
    }
    public HttpPost(String name, String target, String body)
    {
        this.name = name;
        this.target = target;
        this.body = body;
    }



    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }



    public String getTarget()
    {
        return target;
    }
    public void setTarget(String target)
    {
        this.target = target;
    }



    public List<Header> getHeaders()
    {
        return headers;
    }
    public void setHeaders(List<Header> headers)
    {
        this.headers = headers;
    }



    public String getBody()
    {
        return body;
    }
    public void setBody(String body)
    {
        this.body = body;
    }



    public String getBodyContentType()
    {
        return bodyContentType;
    }
    public void setBodyContentType(String bodyContentType)
    {
        this.bodyContentType = bodyContentType;
    }



    public void addHeader(Header... headers)
    {
        if (null == this.headers) this.headers = new ArrayList<>();
        for (Header header : headers)
            this.headers.add(header);
    }



    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpPost httpPost = (HttpPost) o;
        return Objects.equals(name, httpPost.name) && Objects.equals(target, httpPost.target)
                && Objects.equals(headers, httpPost.headers) && Objects.equals(body, httpPost.body)
                && Objects.equals(bodyContentType, httpPost.bodyContentType);
    }



    @Override
    public int hashCode()
    {
        return Objects.hash(name, target, headers, body, bodyContentType);
    }



    public String toString()
    {
        return JsonUtil.transformObjectToJson(this, false);
    }
}
