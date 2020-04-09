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
package fr.lixbox.service.registry.model.health;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Cette classe represente un check
 * {
 *   "name": "firstCheck",
 *   "status": "UP",
 *   "data": {
 *      "key": "value",
 *      "foo": "bar"
 *   }
 * }
 * </code>
 * 
 * @author ludovic.terral
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Check implements Serializable
{
    // ----------- Attribut(s) -----------   
    private static final long serialVersionUID = -1981609597130085609L;
    private static final Log LOG = LogFactory.getLog(Check.class);
    
    private String name;
    private ServiceStatus status;
    private Map<String, String> data;
    
        

    // ----------- Methode(s) -----------
    public Check()
    {
        
    }
    public Check(ServiceStatus status, String name)
    {
        this.name=name;
        this.status=status;
    }
    
    
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    
    
    
    public ServiceStatus getStatus()
    {
        return status;
    }
    public void setStatus(ServiceStatus status)
    {
        this.status = status;
    }
    
    
    
    public Map<String, String> getData()
    {
        if (data == null)
        {
            data = new HashMap<>();
        }
        return data;
    }
    public void setData(Map<String, String> data)
    {
        this.data = data;
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
