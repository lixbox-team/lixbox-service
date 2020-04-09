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
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
