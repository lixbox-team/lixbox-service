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
package fr.lixbox.service.registry.client;

import java.net.URI;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;

import com.fasterxml.jackson.core.type.TypeReference;

import fr.lixbox.common.exceptions.ProcessusException;
import fr.lixbox.common.util.StringUtil;
import fr.lixbox.io.json.JsonFileStore;
import fr.lixbox.service.common.model.Instance;
import fr.lixbox.service.common.util.ServiceUtil;
import fr.lixbox.service.registry.RegistryService;
import fr.lixbox.service.registry.cdi.RegistryConfigLoader;
import fr.lixbox.service.registry.model.ServiceEntry;
import fr.lixbox.service.registry.model.ServiceType;
import fr.lixbox.service.registry.model.health.ServiceState;
import fr.lixbox.service.registry.model.health.ServiceStatus;

/**
 * Cette classe est le client d'accès au fixed-registry-service.
 * 
 * @author ludovic.terral
 *
 */
public class RegistryServiceClient implements RegistryService
{
    // ----------- Attribut(s) -----------   
    private static final long serialVersionUID = 1201703311337L;
    private static final Log LOG = LogFactory.getLog(RegistryServiceClient.class);
    
    private static final String DISCOVER_PATH = "/discover";
    private static final String ENTRIES_PATH = "/entries";
    private static final String EMPTY_REPONSE_MSG = "Empty response content :";
    
    private Map<String, ServiceEntry> cache = new HashMap<>();
    
    private transient TypeReference<HashMap<String, ServiceEntry>> typeRef = new TypeReference<HashMap<String, ServiceEntry>>(){};
    private transient Client currentRegistry = null;
    private transient JsonFileStore jsonFileStore;
    
        

    // ----------- Methode(s) -----------
    public RegistryServiceClient()
    {
        init();
    }
    public RegistryServiceClient(String uri)
    {
        addRegistryServiceUri(uri);
    }

    
 
    public void init()
    {
        jsonFileStore = new JsonFileStore(Paths.get(System.getProperty("java.io.tmpdir") + "/service_registry_client"));
        cache = jsonFileStore.load(typeRef);
        RegistryConfigLoader loader = new RegistryConfigLoader();
        String localUri = loader.getRegistryURI();
        if (!cache.containsKey(RegistryService.SERVICE_NAME+RegistryService.SERVICE_VERSION) || 
                (!StringUtil.isEmpty(localUri) && !localUri.equals(cache.get(RegistryService.SERVICE_NAME+RegistryService.SERVICE_VERSION).getPrimary().getUri())))
        {
            ServiceEntry local = null;
            if (!StringUtil.isEmpty(localUri))
            {
                local = new ServiceEntry();
                local.setType(ServiceType.MICRO_PROFILE);
                local.setName(RegistryService.SERVICE_NAME);
                local.setVersion(RegistryService.SERVICE_VERSION);
                local.setPrimary(new Instance(localUri));
                storeServiceEntry(local);
                return;
            }      
                        
            ServiceEntry generic = new ServiceEntry();
            generic.setType(ServiceType.MICRO_PROFILE);
            generic.setName(RegistryService.SERVICE_NAME);
            generic.setVersion(RegistryService.SERVICE_VERSION);
            generic.getInstances().addAll(
                    Arrays.asList(
                        new Instance("http://localhost:9876/api/"+RegistryService.SERVICE_NAME+"/1.0"), 
                        new Instance("http://localhost:8080/registry/api/1.0")));   
            storeServiceEntry(generic);
        }   
    }

    

    @Override
    public ServiceState checkHealth()
    {
        ServiceState state = new ServiceState(ServiceStatus.DOWN);
        if (currentRegistry!=null)
        {
            state.setStatus(ServiceStatus.UP);
        }
        return state;
    }
    
    
    
    @Override
    public ServiceState checkLive()
    {
        return checkHealth();
    }
    
    
    
    @Override
    public ServiceState checkReady()
    {
        return checkHealth();
    }  



    /**
     * Cette methode ajoute une URI à un service. 
     * @param name
     * @param version
     * @param type
     * @param uri
     * 
     * @return true si le registre a bien été mis à jour.
     */
    @Override
    public boolean registerService(String name, String version, ServiceType type, String uri, String endPointUri)
    {
        boolean result = false;
        WebTarget registryService = getRegistryService();
        if (registryService!=null)
        {
            Response response = registryService.path("/register").path(name).path(version)
                .queryParam("uri", uri).queryParam("type", type).queryParam("endPointUri", endPointUri)
                .request().get();
            result = response.readEntity(Boolean.class);
            response.close();
            clearClients();
            if (result)
            {
                cache.put(name+version, discoverService(name, version));
                jsonFileStore.write(cache);
            }
        }
        return result;
    }


    

    /**
     * Cette methode retire une URI d'un service. 
     * @param name
     * @param version
     * @param uri
     * 
     * @return true si le registre a bien été mis à jour.
     */
    @Override
    public boolean unregisterService(String name, String version, String uri)
    {
        boolean result = false;
        WebTarget registryService = getRegistryService();
        if (registryService!=null)
        {
            Response response = registryService.path("/unregister").path(name).path(version).queryParam("uri", uri).request().delete();
            result = response.readEntity(Boolean.class);
            if (result)
            {
                ServiceEntry tmp = discoverService(name, version);
                if (tmp != null)
                {
                    cache.put(name+version, tmp);    
                }
                else
                {
                    cache.remove(name+version);
                }                
                jsonFileStore.write(cache);
            }
            response.close();
            clearClients();
        }
        return result;
    }



    /**
     * Cette methode renvoie le service entry de registry concernant le service
     * passé en paramètre. 
     * @param name
     * @param version
     * 
     * @return null si aucun service trouvé
     *         le descripteur de service
     */
    @Override
    public ServiceEntry discoverService(String name, String version)
    {
        ServiceEntry result=null;
        WebTarget registryService = getRegistryService();
        if (registryService != null)
        {
            Response response = registryService.path(DISCOVER_PATH).path(name).path(version).request().get();        
            try
            {
                result = response.readEntity(ServiceEntry.class);
            }
            catch (ProcessingException pe)
            {
                LOG.info(EMPTY_REPONSE_MSG+pe.getMessage());
                LOG.trace(pe);
            }
            finally 
            {
                response.close();
            }
        }
        else
        {
            result = cache.get(name+version);
        }
        clearClients();
        return result;
    }
    
    
    
    private void clearClients()
    {
        if (currentRegistry!=null)
        {
            currentRegistry.close();
            currentRegistry=null;
        }
    }



    /**
     * Cette methode renvoie l'url du premier service actif correspondant au paramètre.
     * 
     * @param name
     * @param version
     * 
     * @return null si aucun service actif trouvé pour ces paramètres
     *         l'url du service trouvé
     */
    public String discoverServiceURI(String name, String version)
    {
        String result="";
        WebTarget registryService = getRegistryService();
        if (registryService!=null)
        {
            Response response = registryService.path(DISCOVER_PATH).path(name).path(version).request().get();        
            try 
            {
                ServiceEntry tmp = response.readEntity(ServiceEntry.class);
                if (tmp !=null)
                {
                    result = getServiceURI(tmp);
                }
            }
            catch (ProcessingException pe)
            {
                LOG.info(EMPTY_REPONSE_MSG+pe.getMessage());
                LOG.trace(pe);
            }
            finally 
            {
                response.close();
            }
        }
        else
        {
            result = getServiceURI(cache.get(name+version));
        }
        clearClients();
        return result;
    }
    
    
    
    
    /**
     * Cette méthode récupère les entrées correspondant à un nom
     * indépendemment des versions
     * 
     * @return null si rien ne correspond
     *         un liste des entrées 
     */
    @Override
    public List<ServiceEntry> getEntries(String name) 
    {
        List<ServiceEntry> result = new ArrayList<>();
        WebTarget registryService = getRegistryService();
        if(registryService != null)
        {
            Response response = registryService.path(ENTRIES_PATH).path(name).request().get();
            try 
            {
                result = response.readEntity( new GenericType<List<ServiceEntry>>(){});
            } 
            catch (ProcessingException pe) 
            {
                LOG.info(EMPTY_REPONSE_MSG+pe.getMessage());
                LOG.trace(pe);
            }
            finally 
            {
                if (response!=null)
                {
                    response.close();
                }
            }
                
        }
        else
        {
            for(Map.Entry<String, ServiceEntry> entry : cache.entrySet())
            {
                if(entry.getKey().contains(name))
                {
                    result.add(entry.getValue());
                }
            }
        }
        clearClients();
        return result;
    } 
    
    
    

    
    /**
     * Cette méthode récupère toutes les entries
     */
    @Override
    public List<ServiceEntry> getEntries() 
    {
        List<ServiceEntry> result = new ArrayList<>();
        WebTarget registryService = getRegistryService();
        if(registryService != null)
        {
            Response response = registryService.path(ENTRIES_PATH).request().get();
            try 
            {
                result = response.readEntity( new GenericType<List<ServiceEntry>>(){});
            } 
            catch (ProcessingException pe) 
            {
                LOG.info(EMPTY_REPONSE_MSG+pe.getMessage());
                LOG.trace(pe);
            }
            finally 
            {
                if (response!=null)
                {
                    response.close();
                }
            }
        }
        else
        {
            for(Map.Entry<String, ServiceEntry> entry : cache.entrySet())
            {
                result.add(entry.getValue());
            }
        }
        clearClients();
        return result;
    } 


    
    /**
     * Cette methode renvoie la version courante du registry service. 
     * 
     * @return la version courante
     */
    @Override
    public String getVersion()
    {
        String result="DEGRADED MODE RECORDING SERVICE";
        WebTarget registryService = getRegistryService();
        if (registryService!=null)
        {
            Response response = registryService.path("version").request().get();        
            try
            {
                if (response.getStatus()==200)
                {
                    result = response.readEntity(String.class);
                }
            }
            catch (ProcessingException pe)
            {
                LOG.info(EMPTY_REPONSE_MSG+pe.getMessage());
                LOG.trace(pe);
            }
            finally 
            {
                response.close();
            }
        }
        return result;
    }
    
    
    
    /**
     * Cette methode ajoute une nouvelle adresse d'un registry service.
     * @param uri
     */
    public void addRegistryServiceUri(String uri)
    {
        if (ServiceStatus.UP.equals(ServiceUtil.checkHealthMicroProfileHealth(uri).getStatus()))
        {
            currentRegistry = null;
            init();
            ServiceEntry registry = cache.get(RegistryService.SERVICE_NAME+RegistryService.SERVICE_VERSION);
            registry.getInstances().add(new Instance(uri));
            registry.setPrimary(registry.getInstanceByUri(uri));
            cache.put(RegistryService.SERVICE_NAME+RegistryService.SERVICE_VERSION, registry);
            jsonFileStore.write(cache);
            LOG.debug(getRegistryService());
        }
        else
        {
            throw new ProcessusException("UNABLE TO CONNECT ON "+uri);
        }
    }
    
    
    
    /**
     * Cette methode renvoie un accès au service d'enregistrement.
     * 
     * @return l'accès au service d'enregistrement.
     */
    private WebTarget getRegistryService()
    {   
        WebTarget target = null;
        try
        {
            if (currentRegistry==null || ((ResteasyClient)currentRegistry).isClosed()) 
            {
                currentRegistry = ServiceUtil.getPooledClient(1, "", 0);
            }
            String uri = getCurrentRegistryServiceUri();
            if (!StringUtil.isEmpty(uri))
            {
                target = currentRegistry.target(URI.create(uri));
            }
        }
        catch (Exception e)
        {
            LOG.fatal(e);
        }
        return target;
    }

    

    /**
     * Cette methode renvoie l'uri d'un service d'enregistrement actif
     * 
     * @return une URI d'un service actif
     * 
     * @throws ProcessusException s'il est impossible de trouver aucun service d'enregistrement actif.
     */
    public String getCurrentRegistryServiceUri()
    {
        return getServiceURI(cache.get(RegistryService.SERVICE_NAME+RegistryService.SERVICE_VERSION));
    }
    
    
    
    /**
     * Cette methode analyse l'ensemble des URIs fourni afin d'identifier
     * le premier service actif.
     * 
     * @param serviceEntry
     * 
     * @return une URI d'un service actif
     * 
     * @throws ProcessusException s'il est impossible de trouver aucun service d'enregistrement actif.
     */
    private String getServiceURI(ServiceEntry serviceEntry)
    {
        String serviceName="UNKNOW";
        String serviceVersion="UNKNOW";
        String uriFound="";
        if (serviceEntry!=null)
        {
            serviceName = serviceEntry.getName();
            serviceVersion = serviceEntry.getVersion();
            if (serviceEntry.getPrimary()!=null && ServiceStatus.UP.equals(ServiceUtil.checkHealth(serviceEntry.getType(), serviceEntry.getPrimary().getUri()).getStatus()))
            {
                uriFound = serviceEntry.getPrimary().getUri();
            }
            else 
            {
                for (Instance servInstance : serviceEntry.getInstances())
                {
                    if (ServiceStatus.UP.equals(ServiceUtil.checkHealth(serviceEntry.getType(), servInstance.getUri()).getStatus()))
                    {
                        uriFound = servInstance.getUri();
                        serviceEntry.setPrimary(servInstance);
                        storeServiceEntry(serviceEntry);
                    }
                }
            }
        }
        if (!StringUtil.isEmpty(uriFound))
        {
            return uriFound;
        }
        else
        {
            throw new ProcessusException("UNABLE TO FIND A LIVE INSTANCE OF "+serviceName+"-"+serviceVersion);
        }
    }
    
    
    
    /**
     * Cette methode enregistre la mise à jour du registre
     * @param registry
     */
    private void storeServiceEntry(ServiceEntry entry)
    {
        cache.put(entry.getName()+entry.getVersion(), entry);
        jsonFileStore.write(cache);
    }
}