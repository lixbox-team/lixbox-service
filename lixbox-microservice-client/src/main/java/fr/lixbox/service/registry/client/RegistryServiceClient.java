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
package fr.lixbox.service.registry.client;

import java.net.Socket;
import java.net.URI;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLSession;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

import com.fasterxml.jackson.core.type.TypeReference;

import fr.lixbox.common.exceptions.ProcessusException;
import fr.lixbox.common.util.StringUtil;
import fr.lixbox.io.json.JsonFileStore;
import fr.lixbox.service.registry.RegistryService;
import fr.lixbox.service.registry.cdi.RegistryConfigLoader;
import fr.lixbox.service.registry.model.ServiceEntry;

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
    private static RegistryServiceClient instance = null;
    
    private static final String DISCOVER_PATH = "/discover";
    private static final String ENTRIES_PATH = "/entries";
    private static final String EMPTY_REPONSE_MSG = "Empty response content :";
    
    private Map<String, ServiceEntry> cache = new HashMap<>();
    
    private transient TypeReference<HashMap<String, ServiceEntry>> typeRef = new TypeReference<HashMap<String, ServiceEntry>>(){};
    private transient WebTarget currentRegistry = null;
    private transient JsonFileStore jsonFileStore;
    
        

    // ----------- Methode(s) -----------
    private RegistryServiceClient()
    {
        init();
    }
    private RegistryServiceClient(String uri)
    {
        addRegistryServiceUri(uri);
    }

 

    public static RegistryServiceClient getInstance()
    {
        if (instance == null || StringUtil.isEmpty(instance.getCurrentRegistryServiceUri()))
        {
            instance = new RegistryServiceClient();
        }   
        return instance;
    }
    public static RegistryServiceClient getInstance(String RegistryServiceUri)
    {
        if (instance == null || StringUtil.isEmpty(instance.getCurrentRegistryServiceUri()))
        {
            instance = new RegistryServiceClient(RegistryServiceUri);
        }   
        return instance;
    }
    public static void clearInstance()
    {
        instance = null;
    }


 
    public void init()
    {
        jsonFileStore = new JsonFileStore(Paths.get(System.getProperty("java.io.tmpdir") + "/service_registry_client"));
        cache = jsonFileStore.load(typeRef);
        RegistryConfigLoader loader = new RegistryConfigLoader();
        String localUri = loader.getRegistryURI();
        if (!cache.containsKey(RegistryService.SERVICE_NAME+RegistryService.SERVICE_VERSION) || 
                (!StringUtil.isEmpty(localUri) && !localUri.equals(cache.get(RegistryService.SERVICE_NAME+RegistryService.SERVICE_VERSION).getUris().get(0))))
        {
            ServiceEntry local = null;
            if (!StringUtil.isEmpty(localUri))
            {
                local = new ServiceEntry();
                local.setName(RegistryService.SERVICE_NAME);
                local.setVersion(RegistryService.SERVICE_VERSION);
                local.getUris().addAll(Arrays.asList(localUri));
                storeServiceEntry(local);
                return;
            }      
            
            ServiceEntry distant = null;
            try
            {
                distant = getRegistryService().path(DISCOVER_PATH).path(RegistryService.SERVICE_NAME).path(RegistryService.SERVICE_VERSION).request().get().readEntity(ServiceEntry.class);
                storeServiceEntry(distant);
                return;
            }
            catch (ProcessusException e) 
            {
                LOG.error("UNABLE TO FIND A LIVE INSTANCE OF "+RegistryService.SERVICE_NAME+"-"+RegistryService.SERVICE_VERSION);   
                LOG.debug(e);
            }
                        
            ServiceEntry generic = new ServiceEntry();
            generic.setName(RegistryService.SERVICE_NAME);
            generic.setVersion(RegistryService.SERVICE_VERSION);
            generic.getUris().addAll(
                    Arrays.asList(
                    "http://localhost:9876/api/"+RegistryService.SERVICE_NAME+"/1.0", 
                    "http://localhost:8080/registry/api/1.0",  
                    "http://service.dev.lan:8080/registry/api/1.0",                  
                    "https://service.dev.lan/registry/api/1.0",
                    "https://service.pam.lan/registry/api/1.0"));   
            storeServiceEntry(generic);
        }   
    }
    
    
    
    /**
     * Cette methode renvoie l'uri du service registry
     * courant
     * 
     * @return l'uri
     */
    public String getCurrentRegistryServiceUri()
    {        
        String currentUri = "";
        try
        {
            if (currentRegistry!=null)
            {
                currentUri = currentRegistry.getUri().toString();
            }
            else
            {
                currentUri = getRegistryServiceURI();
            }
        }
        catch (Exception e)
        {
            LOG.debug(e);
        }
        return currentUri;
    }
        
    

    /**
     * Cette methode ajoute une URI à un service. 
     * @param name
     * @param version
     * @param uri
     * 
     * @return true si le registre a bien été mis à jour.
     */
    @Override
    public boolean registerService(String name, String version, String uri)
    {
        boolean result = false;        
        WebTarget registryService = getRegistryService();
        if (registryService!=null)
        {
            Response response = getRegistryService().path("/register").path(name).path(version).queryParam("uri", uri).request().get();
            result = response.readEntity(Boolean.class);
            if (result)
            {
                cache.put(name+version, discoverService(name, version));
                jsonFileStore.write(cache);
            }
            response.close();
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
            Response response = getRegistryService().path("/unregister").path(name).path(version).queryParam("uri", uri).request().delete();
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
            Response response = getRegistryService().path(DISCOVER_PATH).path(name).path(version).request().get();        
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
        return result;
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
            Response response = getRegistryService().path(DISCOVER_PATH).path(name).path(version).request().get();        
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
        return result;
    }
    
    
    
    
	/**
	 * Cette méthode récupère les entrées correspondant à un nom
	 * indépendemment des versions
	 * 
	 * @return null si rien ne correspond
	 * 		   un liste des entrées 
	 */
	@Override
	public List<ServiceEntry> getEntries(String name) 
	{
		List<ServiceEntry> result = new ArrayList<>();
        WebTarget registryService = getRegistryService();
        if(registryService != null)
        {
        	Response response = getRegistryService().path(ENTRIES_PATH).path(name).request().get();
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
        	Response response = getRegistryService().path(ENTRIES_PATH).request().get();
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
		return result;
	} 


    
    /**
     * Cette methode verifie la disponibilité du registry service. 
     * 
     * @return true si dispo
     */
    @Override
    public boolean checkHealth()
    {
        boolean result = false;
        try
        {
            result = checkHealth(getRegistryServiceURI());
        }
        catch (ProcessusException pre) 
        {
            LOG.info(pre.getMessage());
            LOG.debug(pre);            
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
        WebTarget registryService = getRegistryService();
        if (registryService!=null)
        {
            Response response = getRegistryService().path("version").request().get();        
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
        if (checkHealth(uri))
        {
            currentRegistry = null;
            init();
            ServiceEntry registry = cache.get(RegistryService.SERVICE_NAME+RegistryService.SERVICE_VERSION);
            if (!registry.getUris().contains(uri))
            {
                registry.getUris().add(0, uri);
                cache.put(RegistryService.SERVICE_NAME+RegistryService.SERVICE_VERSION, registry);
                jsonFileStore.write(cache);
            }
            else
            {
                Collections.swap(registry.getUris(), 0, registry.getUris().indexOf(uri));
                cache.put(RegistryService.SERVICE_NAME+RegistryService.SERVICE_VERSION, registry);
                jsonFileStore.write(cache);
            }
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
        if(currentRegistry==null || !checkHttpHealth(currentRegistry.getUri().toString()))
        {
            ClientBuilder cliBuilder = ClientBuilder.newBuilder();
            ((ResteasyClientBuilder) cliBuilder).connectionPoolSize(20);
            cliBuilder.hostnameVerifier((String hostname, SSLSession session) -> true);
            currentRegistry = cliBuilder.build().target(URI.create(getRegistryServiceURI()));
        }
        return currentRegistry;
    }

    

    /**
     * Cette methode renvoie l'uri d'un service d'enregistrement actif
     * 
     * @return une URI d'un service actif
     * 
     * @throws ProcessusException s'il est impossible de trouver aucun service d'enregistrement actif.
     */
    private String getRegistryServiceURI()
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
            storeServiceEntry(serviceEntry);
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
     * Cette methode verifie la disponibilite du service d'enregistrement dont l'url
     * est passée en paramètre
     * 
     * @param uri
     * 
     * @return true si disponible
     */
    private boolean checkHealth(String uri)
    {
        boolean result = false;
        if (uri!=null)
        {
            if (uri.startsWith("http"))        
            {
                result = checkHttpHealth(uri);
            }
            else
            {
                result = checkTcpHealth(uri);
            }
        }
        return result;
    }
    
    
    
    /**
     * Cette methode verifie la santé d'un service http 
     * @param uri
     * 
     * @return true si ok
     */
    private boolean checkHttpHealth(String uri)
    {
        boolean result=false;
        try
        {
            ClientBuilder cliBuilder = ClientBuilder.newBuilder();
            cliBuilder.hostnameVerifier((String hostname, SSLSession session) -> true);
            WebTarget registryService = cliBuilder.build().target(URI.create(uri));
            Response response = registryService.path("/checkHealth").request().get();
            result = response.readEntity(Boolean.class);
            response.close();
        }
        catch (ProcessingException pe) 
        {
            LOG.info("FAILED TO CONNECT ON "+uri, pe);            
        }
        return result;
    }
    
    
    
    /**
     * Cette methode verifie la santé d'un service tcp 
     * @param uri
     * 
     * @return true si ok
     */
    private boolean checkTcpHealth(String uri)
    {
        boolean result=false;
        String hostName = uri.substring(uri.indexOf(':')+3,uri.lastIndexOf(':'));
        String port = uri.substring(uri.lastIndexOf(':')+1);
        try( Socket socket = new Socket(hostName, Integer.parseInt(port)); )
        {
            result = socket.isBound();
        }
        catch (Exception e) 
        {
            LOG.info("FAILED TO CONNECT ON "+uri, e);            
        }        
        return result;
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