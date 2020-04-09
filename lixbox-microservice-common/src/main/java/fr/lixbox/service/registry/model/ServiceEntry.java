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
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Cette classe represente un entrée de service
 * <code>
 *  {
 *      "name":"fixed-registry-service",
 *      "version":"1.0",
 *      "uris":["http://localhost:8080/test/api/1.0/fixed"]
 *  }
 * </code>
 * 
 * @author ludovic.terral
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceEntry implements Serializable
{
    // ----------- Attribut(s) -----------   
    private static final long serialVersionUID = -1981609597130085609L;
    private static final Log LOG = LogFactory.getLog(ServiceEntry.class);
    
    private String name;
    private String version;
    private String endpointUri;
    private ServiceType type;
    private List<ServiceInstance> instances;
    private ServiceInstance primary;
    
        

    // ----------- Methode(s) -----------
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    
        
        
    public String getVersion()
    {
        return version;
    }
    public void setVersion(String version)
    {
        this.version = version;
    }
    
        
    
    public String getEndpointUri()
    {
        return endpointUri;
    }
    public void setEndpointUri(String endpointUri)
    {
        this.endpointUri = endpointUri;
    }
    
    
    
    public ServiceType getType()
    {
        return type;
    }
    public void setType(ServiceType type)
    {
        this.type = type;
    }
    
    
    
    public List<ServiceInstance> getInstances()
    {
        if (instances == null)
        {
            instances = new ArrayList<>();
        }
        return instances;
    }
    public void setInstances(List<ServiceInstance> instances)
    {
        this.instances = instances;
    }
    
    
    
    public ServiceInstance getPrimary()
    {
        return primary;
    }
    public void setPrimary(ServiceInstance primary)
    {
        this.primary = primary;
    }
    
    
    
    public boolean containsInstanceUri(String uri) 
    {
        boolean result = false;
        for (ServiceInstance instance : getInstances())
        {
            if (instance.getUri().equals(uri))
            {
                result = true;
            }
        }
        return result;
    }
    
    
    
    @JsonIgnore
    public ServiceInstance getInstanceByUri(String uri) 
    {
        ServiceInstance result = null;
        for (ServiceInstance instance : getInstances())
        {
            if (instance.getUri().equals(uri))
            {
                result = instance;
            }
        }
        return result;
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