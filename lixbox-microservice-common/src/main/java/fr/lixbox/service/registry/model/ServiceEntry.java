/*******************************************************************************
 *    
 *                           FRAMEWORK Lixbox
 *                          ==================
 *      
 * This file is part of lixbox-service.
 *
 *    lixbox-supervision is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    lixbox-supervision is distributed in the hope that it will be useful,
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
package fr.lixbox.service.registry.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.lixbox.service.common.model.Instance;
import fr.lixbox.service.registry.model.health.ServiceStatus;

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
    private List<Instance> instances;
    private Instance primary;
    
        

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
    
    
    
    public ServiceStatus getStatus()
    {
        Optional<Instance> found = getInstances().stream().filter(x->x.isReady()).findFirst();
        return found.isPresent()?ServiceStatus.UP:ServiceStatus.DOWN;                
    }
    
    
    
    public ServiceType getType()
    {
        if (type==null)
        {
            this.type=ServiceType.TCP;
        }
        return type;
    }
    public void setType(ServiceType type)
    {
        this.type = type;
    }
    
    
    
    public List<Instance> getInstances()
    {
        if (instances == null)
        {
            instances = new ArrayList<>();
        }
        return instances;
    }
    public void setInstances(List<Instance> instances)
    {
        this.instances = instances;
    }
    
    
    
    public Instance getPrimary()
    {
        return primary;
    }
    public void setPrimary(Instance primary)
    {
        this.primary = primary;
    }
    
    
    
    public boolean containsInstanceUri(String uri) 
    {
        boolean result = false;
        for (Instance instance : getInstances())
        {
            if (instance.getUri().equals(uri))
            {
                result = true;
            }
        }
        return result;
    }
    
    
    
    @JsonIgnore
    public Instance getInstanceByUri(String uri) 
    {
        Instance result = null;
        for (Instance instance : getInstances())
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