/*******************************************************************************
 *    
 *                           FRAMEWORK Lixbox
 *                          ==================
 *      
 * This file is part of lixbox-service.
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
package fr.lixbox.service.common.model;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.lixbox.service.registry.model.health.ServiceState;

/**
 * Cette classe represente une instance d'un service
 * <code>
 *  {
 *      "uri":"http://localhost:8080/test/api/1.0/fixed",
 *      "isLive":true,
 *      "isReady":true,
 *      "liveState":{
 *            "status": "UP",
 *            "checks": [
 *              {
 *                "name": "firstCheck",
 *                "status": "UP",
 *                "data": {
 *                  "key": "value",
 *                   "foo": "bar"
 *                 }
 *               },
 *               {
 *                 "name": "secondCheck",
 *                 "status": "UP"
 *               }
 *             ]
 *           },
 *      "readyState":{
 *            "status": "UP",
 *            "checks": [
 *              {
 *                "name": "firstCheck",
 *                "status": "UP",
 *                "data": {
 *                  "key": "value",
 *                   "foo": "bar"
 *                 }
 *               },
 *               {
 *                 "name": "secondCheck",
 *                 "status": "UP"
 *               }
 *             ]
 *           },
 *  }
 * </code>
 * 
 * @author ludovic.terral
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Instance implements Serializable
{
    // ----------- Attribut(s) -----------   
    private static final long serialVersionUID = -1981609597130085609L;
    private static final Log LOG = LogFactory.getLog(Instance.class);
    
    private String uri;
    private boolean isLive=true;
    private boolean isReady=true;
    private ServiceState liveState;
    private ServiceState readyState;
    private String username;
    private String credential;
    private String token;
    private String manualCheckUri;
    
        

    // ----------- Methode(s) -----------
    public Instance() 
    {
        //base constructor
    }
    public Instance(String uri) 
    {
        this.uri=uri;
    }
    
    
    public String getUri()
    {
        return uri;
    }
    public void setUri(String uri)
    {
        this.uri = uri;
    }



    public String getManualCheckUri()
    {
        return manualCheckUri;
    }
    public void setManualCheckUri(String manualCheckUri)
    {
        this.manualCheckUri = manualCheckUri;
    }

    
    
    public boolean isLive()
    {
        return isLive;
    }
    public void setLive(boolean isLive)
    {
        this.isLive = isLive;
    }



    public boolean isReady()
    {
        return isReady;
    }
    public void setReady(boolean isReady)
    {
        this.isReady = isReady;
    }
    
    

    public String getUsername()
    {
        return username;
    }
    public void setUsername(String username)
    {
        this.username = username;
    }
    
    
    
    public String getCredential()
    {
        return credential;
    }
    public void setCredential(String credential)
    {
        this.credential = credential;
    }
    
    
    
    public String getToken()
    {
        return token;
    }
    public void setToken(String token)
    {
        this.token = token;
    }
    
    
    
    public ServiceState getLiveState()
    {
        return liveState;
    }
    public void setLiveState(ServiceState liveState)
    {
        this.liveState = liveState;
    }



    public ServiceState getReadyState()
    {
        return readyState;
    }
    public void setReadyState(ServiceState readyState)
    {
        this.readyState = readyState;
    }
    
    
    
    @Override
    public String toString() 
    {
        String result = "{}";
        ObjectMapper mapper = new ObjectMapper();
        try {
            result = mapper.writeValueAsString(this);
        } 
        catch (JsonProcessingException e) 
        {
            LOG.error(e);
        }
        return result;
    }
}
