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
package fr.lixbox.service.registry.model.health;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Cette classe represente un état d'une instance d'un service
 * <code>
 *  {
 *   "status": "UP",
 *   "checks": [
 *     {
 *      "name": "firstCheck",
 *      "status": "UP",
 *      "data": {
 *        "key": "value",
 *         "foo": "bar"
 *       }
 *     },
 *     {
 *       "name": "secondCheck",
 *       "status": "UP"
 *     }
 *   ]
 * }
 * </code>
 * 
 * @author ludovic.terral
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceState implements Serializable
{
    // ----------- Attribut(s) -----------   
    private static final long serialVersionUID = -1981609597130085609L;
    private static final Log LOG = LogFactory.getLog(ServiceState.class);
    
    private ServiceStatus status;
    private List<Check> checks;
    
        

    // ----------- Methode(s) -----------
    public ServiceState()
    {
        //classic
    }
    public ServiceState(ServiceStatus status)
    {
        this.status=status;
    }
    
    
    
    public ServiceStatus getStatus()
    {
        return status;
    }
    public void setStatus(ServiceStatus status)
    {
        this.status = status;
    }

    

    public List<Check> getChecks()
    {
        if (checks==null)
        {
            checks=new ArrayList<>();
        }
        return checks;
    }
    public void setChecks(List<Check> checks)
    {
        this.checks = checks;
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
