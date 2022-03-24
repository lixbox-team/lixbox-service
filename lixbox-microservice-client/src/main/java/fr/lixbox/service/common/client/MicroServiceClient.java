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
package fr.lixbox.service.common.client;

import java.net.URI;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;

import com.fasterxml.jackson.core.type.TypeReference;

import fr.lixbox.common.exceptions.BusinessException;
import fr.lixbox.common.exceptions.ProcessusException;
import fr.lixbox.common.util.StringUtil;
import fr.lixbox.service.common.MicroService;
import fr.lixbox.service.common.model.Instance;
import fr.lixbox.service.common.util.ServiceUtil;
import fr.lixbox.service.registry.RegistryService;
import fr.lixbox.service.registry.client.RegistryServiceClient;
import fr.lixbox.service.registry.model.ServiceEntry;
import fr.lixbox.service.registry.model.health.ServiceState;
import fr.lixbox.service.registry.model.health.ServiceStatus;

/**
 * Cette classe est le client d'accès minimal pour un microservice.
 * 
 * @author ludovic.terral
 *
 */
public abstract class MicroServiceClient implements MicroService
{
    // ----------- Attribut(s) -----------
    private static final long serialVersionUID = 120520170956L;
    private static final Log LOG = LogFactory.getLog(MicroServiceClient.class);
        
    protected transient RegistryService serviceRegistry;
    protected transient Client currentService;
    protected transient Client currentSecureService;
    protected transient BasicAuthentication basicAuth;
    protected transient TokenAuthentication tokenAuth;

    protected static final String SECURE_PATH = "secure";
    
    protected ServiceEntry serviceEntry;
    protected String serviceName = "micro-service";
    protected String serviceVersion = "0.1";
    protected String proxyHost;
    protected Integer proxyPort;
    protected Integer poolSize = 3;
    protected String staticUri = null;
    
    
    
    // ----------- Methode(s) -----------
    public MicroServiceClient()
    {
        init();
    }
    public MicroServiceClient(String serviceRegistryUri)
    {
        init(serviceRegistryUri);
    }
    
        
    
    public void init(String serviceRegistryUri)
    {
        serviceRegistry = new RegistryServiceClient(serviceRegistryUri);
        loadInfosService();
    }
    public void init()
    {
        serviceRegistry = new RegistryServiceClient();
        loadInfosService(); 
    }
    
    
    
    public Integer getPoolSize()
    {
        return poolSize;
    }
    public void setPoolSize(Integer poolSize)
    {
        this.poolSize = poolSize;
    }
    
    
    
    public void setProxy(String proxyHost, int proxyPort)
    {
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
        clearClients();
    }
    
    
    
    public void setCredentials(String user, String password)
    {
        if (basicAuth==null)
        {
            basicAuth = new BasicAuthentication(user, password);
        }
        else
        {
            basicAuth.setData(user, password);
        }
    }
    
    
    
    public void setToken(String type, String token)
    {
        if (tokenAuth==null)
        {
            tokenAuth = new TokenAuthentication(type, token);
        }
        else
        {
            tokenAuth.setData(type, token);
        }
    }
    
        
    
    /**
     * Cette methode donne accès à un registry service.
     * @param uri
     */
    public void addServiceRegistryUri(String uri)
    {
        serviceRegistry = new RegistryServiceClient(uri); 
        LOG.debug(getService());
    }
    
    
    
    public void setServiceUri(String uri)
    {   
        this.staticUri = uri;
        clearClients();
    } 
    
    
    
    public void clearClients()
    {
        if (this.currentSecureService!=null)
        {
            this.currentSecureService.close();
            this.currentSecureService = null;
        }
        if (this.currentService!=null)
        {
            this.currentService.close();
            this.currentService = null;
        }
        this.serviceEntry = null;
    }
    


    /**
     * Cette methode verifie la disponibilité du cache service. 
     * 
     * @return true si dispo
     */
    @Override
    public ServiceState checkHealth()
    {
        ServiceState result;
        if (getService()!=null)
        {
            result = ServiceUtil.checkHealth(serviceEntry.getType(), getService().getUri().toString());
        }
        else 
        {
            result = new ServiceState(ServiceStatus.DOWN);
        }
        return result;
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
     * Cette methode renvoie la version courante du registry service. 
     * 
     * @return la version courante
     */
    @Override
    public String getVersion()
    {
        String result="DEGRADED MODE RECORDING SERVICE";
        try
        {
            WebTarget service = getService();
            if (service!=null)
            {
                Response response = service.path("version").request().get();   
                if (response.getStatus()==200)
                {
                    result = response.readEntity(String.class);
                }
                response.close();
            }
        }
        catch (ProcessingException pe)
        {
            LOG.info("Empty response content :"+pe.getMessage());
            LOG.trace(pe);
        }
        return result;
    }

    
    
    private void launchSyncCache()
    {
        Thread t = new Thread() 
        {
            @Override
            public void run() 
            {
                syncCache();
            }
        };
        t.start();
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
    protected String getServiceURI()
    {
        String uriFound="";
        if (StringUtil.isNotEmpty(this.staticUri))
        {
            uriFound = this.staticUri;
        }
        else
        {
            if (serviceEntry!=null)
            {
                serviceName = serviceEntry.getName();
                serviceVersion = serviceEntry.getVersion();
                for (Instance instance : serviceEntry.getInstances())
                {
                    if (ServiceStatus.UP.equals(ServiceUtil.checkHealth(serviceEntry.getType(), instance).getStatus()))
                    {   
                        uriFound = instance.getUri();
                        break;
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
            this.clearClients();
            throw new ProcessusException("UNABLE TO FIND A LIVE INSTANCE OF "+serviceName+"-"+serviceVersion);
        }
    }
    
    
    
    protected WebTarget getService()
    {
        WebTarget target = null;
        try
        {
            if (currentService==null || ((ResteasyClient)currentService).isClosed())
            {
                currentService = ServiceUtil.getPooledClient(poolSize, proxyHost, proxyPort);
                this.serviceEntry = serviceRegistry.discoverService(serviceName, serviceVersion);
            }
            String uri = getServiceURI();
            if (!StringUtil.isEmpty(uri))
            {
                target = currentService.target(URI.create(uri));
            }
        }
        catch (Exception e)
        {
            LOG.fatal(e);
        }
        if (target==null)
        {
            clearClients();
        }
        return target;
    }
    
    
    
    protected WebTarget getSecureService()
    {
        WebTarget target = null;
        try
        {
            if (currentSecureService==null || ((ResteasyClient)currentSecureService).isClosed()) 
            {
                currentSecureService = ServiceUtil.getPooledClient(poolSize, proxyHost, proxyPort);
                int retry=0;
                do {
                    this.serviceEntry = serviceRegistry.discoverService(serviceName, serviceVersion);
                    retry++;
                }
                while (this.serviceEntry==null && retry<3);
                    
                
                if (basicAuth!=null)
                {
                    currentSecureService.register(basicAuth);
                }
                if (tokenAuth!=null)
                {
                    currentSecureService.register(tokenAuth);
                }
            }
            String uri = getServiceURI();
            if (!StringUtil.isEmpty(uri))
            {
                target = currentSecureService.target(URI.create(uri));
                launchSyncCache();
            }
        }
        catch (Exception e)
        {
            LOG.fatal(e);
        }
        if (target==null)
        {
            clearClients();
        }
        return target;
    }
    

    
    protected <T> T parseResponse(Response response, TypeReference<T> type) throws BusinessException
    {
        T result;
        try
        {
            result = ServiceUtil.parseResponse(response, type);
        }
        catch(ProcessingException pe)
        {
            currentSecureService = null;
            currentService = null;
            serviceEntry = null;
            throw pe;
        }
        return result;
    }
    

    
    protected <T> T parseResponse(Response response, GenericType<T> type) throws BusinessException
    {
        T result;
        try
        {
            result = ServiceUtil.parseResponse(response, type);
        }
        catch(ProcessingException pe)
        {
            currentSecureService = null;
            currentService = null;
            serviceEntry = null;
            throw pe;
        }
        return result;
    }

    
    
    protected abstract void syncCache();
    protected abstract void loadCache();
    protected abstract void initStore();
    protected abstract void loadInfosService();
}