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
package fr.lixbox.service.registry.model;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
public class ServiceInstance implements Serializable
{
    // ----------- Attribut(s) -----------   
    private static final long serialVersionUID = -1981609597130085609L;
    private static final Log LOG = LogFactory.getLog(ServiceInstance.class);
    
    private String uri;
    private boolean isLive;
    private boolean isReady;
    private ServiceState liveState;
    private ServiceState readyState;
    
        

    // ----------- Methode(s) -----------
    public ServiceInstance() 
    {
        //base constructor
    }
    public ServiceInstance(String uri) 
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
