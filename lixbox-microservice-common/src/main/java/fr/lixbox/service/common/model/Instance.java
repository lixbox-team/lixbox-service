/*******************************************************************************
 *    
 *                           FRAMEWORK Lixbox
 *                          ==================
 *      
 *   Copyrigth - LIXTEC - Tous droits reserves.
 *   
 *   Le contenu de ce fichier est la propriete de la societe Lixtec.
 *   
 *   Toute utilisation de ce fichier et des informations, sous n'importe quelle
 *   forme necessite un accord ecrit explicite des auteurs
 *   
 *   @AUTHOR Ludovic TERRAL
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
    private boolean isLive;
    private boolean isReady;
    private ServiceState liveState;
    private ServiceState readyState;
    private String username;
    private String credential;
    private String token;
    
        

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
        String result = "Content error";
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
