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

import javax.net.ssl.SSLSession;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.resteasy.client.jaxrs.BasicAuthentication;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

import fr.lixbox.common.exceptions.BusinessException;
import fr.lixbox.common.exceptions.ProcessusException;
import fr.lixbox.common.util.StringUtil;
import fr.lixbox.service.common.MicroService;
import fr.lixbox.service.registry.RegistryService;
import fr.lixbox.service.registry.client.RegistryServiceClient;
import fr.lixbox.service.registry.model.ServiceEntry;

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
        serviceRegistry = RegistryServiceClient.getInstance(serviceRegistryUri);
        loadInfosService();
        LOG.debug(getService());
    }
    public void init()
    {
        serviceRegistry = RegistryServiceClient.getInstance();
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
        serviceRegistry = RegistryServiceClient.getInstance(uri); 
        LOG.debug(getService());
    }
    
        
    
    public void setServiceUri(String providerUri)
    {
        try 
        {
            if (this.currentService == null) 
            {
                ResteasyClientBuilder clientBuilder = new ResteasyClientBuilder();
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
    public boolean checkHealth()
    {
        boolean result = false;
        if (currentService!=null)
        {
            result = checkHealth(currentService.getUri().toString());
        }
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
    
    

    /**
     * Cette methode verifie la disponibilite du service d'enregistrement dont l'url
     * est passée en paramètre
     * 
     * @param uri
     * 
     * @return true si disponible
     */
    private boolean checkHealth(String uri)
    {
        boolean result=false;
        try
        {
            ClientBuilder cliBuilder = ClientBuilder.newBuilder();
            cliBuilder.hostnameVerifier((String hostname, SSLSession session) -> true);
            LOG.debug(cliBuilder.build().target(URI.create(uri)).request().get());
            result = true;
        }
        catch (ProcessingException pe) 
        {
            LOG.info("FAILED TO CONNECT ON "+uri);
            LOG.info(pe);            
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
    protected String getServiceURI(ServiceEntry serviceEntry)
    {
        String serviceName="UNKNOW";
        String serviceVersion="UNKNOW";
        String uriFound="";
        if (serviceEntry!=null)
        {
            serviceName = serviceEntry.getName();
            serviceVersion = serviceEntry.getVersion();
            for (String uri : serviceEntry.getUris())
            {
                if (checkHealth(uri))
                {   
                    uriFound = uri;
                    break;
                }
            }
        }
        if (!StringUtil.isEmpty(uriFound)&&serviceEntry!=null&&!serviceEntry.getUris().get(0).equals(uriFound))
        {
            serviceEntry.getUris().remove(uriFound);
            serviceEntry.getUris().add(0, uriFound);
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
                ResteasyClientBuilder clientBuilder = new ResteasyClientBuilder();
                clientBuilder = clientBuilder.connectionPoolSize(20);
                clientBuilder.disableTrustManager();
                String uri = getServiceURI(serviceRegistry.discoverService(serviceName, serviceVersion));
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
                ResteasyClientBuilder clientBuilder = new ResteasyClientBuilder();
                clientBuilder = clientBuilder.connectionPoolSize(20);
                clientBuilder.disableTrustManager();
                String uri =  getServiceURI(serviceRegistry.discoverService(serviceName, serviceVersion));
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
    

    
    protected Object parseResponse(Response response, GenericType<?> type) throws BusinessException
    {
        Object result;
        switch(response.getStatus())
        {
            case 200:
            case 201:
                result = response.readEntity(type);
                break;
            case 404:
                throw new BusinessException(response.readEntity(String.class));
            default:
                currentSecureService=null;
                throw new ProcessusException(response.readEntity(String.class));
        }
        return result;
    }

    
    
    protected abstract void syncCache();
    protected abstract void loadCache();
    protected abstract void initStore();
    protected abstract void loadInfosService();
}