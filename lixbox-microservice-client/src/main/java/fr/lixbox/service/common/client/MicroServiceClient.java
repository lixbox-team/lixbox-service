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
package fr.lixbox.service.common.client;

import java.net.URI;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

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
    protected transient WebTarget currentService;
    protected transient WebTarget currentSecureService;
    protected transient BasicAuthentication basicAuth;
    protected transient TokenAuthentication tokenAuth;

    protected static final String SECURE_PATH = "secure";
    
    protected ServiceEntry serviceEntry;
    protected String serviceName = "micro-service";
    protected String serviceVersion = "0.1";
    
    
    
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
        LOG.debug(getService());
    }
    public void init()
    {
        serviceRegistry = new RegistryServiceClient();
        loadInfosService(); 
        LOG.debug(getService());
    }
    
    
    
    public void setCredentials(String user, String password)
    {
        basicAuth = new BasicAuthentication(user, password);
    }
    
    
    public void setToken(String type, String token)
    {
        tokenAuth = new TokenAuthentication(type, token);
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
    
        
    
    public void setServiceUri(String providerUri)
    {
        try 
        {
            if (this.currentService == null) 
            {
                ResteasyClientBuilder clientBuilder = (ResteasyClientBuilder) ClientBuilder.newBuilder();
                clientBuilder = clientBuilder.connectionPoolSize(20);
                clientBuilder.disableTrustManager();
                this.currentService = clientBuilder.build().target(providerUri);
            }
        } 
        catch (Exception e) 
        {
            LOG.fatal(e);
        }        
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
        if (currentService!=null)
        {
            result = ServiceUtil.checkHealth(serviceEntry.getType(), currentService.getUri().toString());
        }
        else {
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
        String serviceName="UNKNOW";
        String serviceVersion="UNKNOW";
        String uriFound="";
        if (serviceEntry!=null)
        {
            serviceName = serviceEntry.getName();
            serviceVersion = serviceEntry.getVersion();
            for (Instance instance : serviceEntry.getInstances())
            {
                if (ServiceStatus.UP.equals(ServiceUtil.checkHealth(serviceEntry.getType(), instance.getUri()).getStatus()))
                {   
                    uriFound = instance.getUri();
                    break;
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
    
    
    
    protected WebTarget getService()
    {
        try
        {
            if (currentService==null)
            {
                ResteasyClientBuilder clientBuilder = (ResteasyClientBuilder)ClientBuilder.newBuilder();
                clientBuilder = clientBuilder.connectionPoolSize(20);
                clientBuilder.disableTrustManager();
                this.serviceEntry = serviceRegistry.discoverService(serviceName, serviceVersion);
                String uri = getServiceURI();
                if (!StringUtil.isEmpty(uri))
                {
                    currentService = clientBuilder.build().target(URI.create(uri));
                }
            }
        }
        catch (Exception e)
        {
            LOG.fatal(e);
        }
        return currentService;
    }
    
    
    
    protected WebTarget getSecureService()
    {
        try
        {
            if (currentSecureService==null)
            {
                ResteasyClientBuilder clientBuilder = (ResteasyClientBuilder)ClientBuilder.newBuilder();
                clientBuilder = clientBuilder.connectionPoolSize(20);
                clientBuilder.disableTrustManager();
                String uri =  getServiceURI();
                if (!StringUtil.isEmpty(uri) && basicAuth!=null)
                {
                    currentSecureService = clientBuilder.build().target(URI.create(uri));
                    currentSecureService.register(basicAuth);
                    launchSyncCache();                    
                }
                if (!StringUtil.isEmpty(uri) && tokenAuth!=null)
                {
                    currentSecureService = clientBuilder.build().target(URI.create(uri));
                    currentSecureService.register(tokenAuth);
                    launchSyncCache();                    
                }
            }    
        }
        catch (Exception e)
        {
            LOG.fatal(e);
        }
        return currentSecureService;
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
            throw pe;
        }
        return result;
    }

    
    
    protected abstract void syncCache();
    protected abstract void loadCache();
    protected abstract void initStore();
    protected abstract void loadInfosService();
}