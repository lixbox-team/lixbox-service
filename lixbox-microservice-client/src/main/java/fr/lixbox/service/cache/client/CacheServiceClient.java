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
package fr.lixbox.service.cache.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.lixbox.service.cache.CacheService;
import fr.lixbox.service.common.client.MicroServiceClient;

/**
 * Cette classe est le client d'accès au cache-service.
 * 
 * @author ludovic.terral
 *
 */
public class CacheServiceClient extends MicroServiceClient implements CacheService
{
    // ----------- Attribut(s) -----------   
    private static final long serialVersionUID = 6978802125622879826L;    
    private static final Log LOG = LogFactory.getLog(CacheServiceClient.class);
    
    private static final String KEYS_PATH = "keys";
    private static final String VALUES_PATH = "values";
    private static final String VALUE_PATH = "value";
    private static final String PATTERN_PARAM = "pattern";
    private static final String MSG_NO_CACHE = "NO LOCAL CACHE";
        

    
    // ----------- Methode(s) -----------
    public CacheServiceClient()
    {
        init();
    }
    public CacheServiceClient(String serviceRegistryUri)
    {
        init(serviceRegistryUri);
    }
    @Override
    protected void loadInfosService()
    {
        serviceName = CacheService.SERVICE_NAME;
        serviceVersion = CacheService.SERVICE_VERSION;
    }
    
    

    /**
     * Cette methode renvoie une liste de clé correspondant à une ou plusieurs patterns.
     * Si aucune pattern n'est transmise, le wildcard est utilisé.
     * @param pattern
     * 
     * @return la liste des clés correspondantes.
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<String> getKeys(String pattern)
    {
        List<String> result = new ArrayList<>();
        WebTarget cache = getService();
        if (cache!=null)
        {
            Response response = cache.path(KEYS_PATH).queryParam(PATTERN_PARAM, pattern).request().get();
            result = response.readEntity(List.class);
        }
        return result;
    }



    /**
     * Cette methode renvoie la valeur associée à une clé
     * @param key
     * 
     * @return null si pas de valeur.
     */
    @Override
    public String get(String key)
    {
        String result = "";
        WebTarget cache = getService();
        if (cache!=null)
        {
            Response response = cache.path(VALUE_PATH).path(key).request().get();
            result = response.readEntity(String.class);
        }
        return result;
    }



    /**
     * Cette methode supprime une clé et sa valeur dans le cache.
     * @param key
     * 
     * @return true si la suppression est effective.
     */
    @Override
    public boolean remove(String key)
    {
      boolean result = false;
      WebTarget cache = getService();
      if (cache!=null)
      {
          Response response = cache.path(KEYS_PATH).path(key).request().delete();
          result = response.readEntity(boolean.class);
      }
      return result;
    }



    /**
     * Cette methode supprime des clés et leurs valeurs dans le cache.
     * 
     * @param Map<String,String> values)
     * 
     * @return true si la suppression est effective.
     */
    @Override
    public boolean remove(String... keys)
    {
      boolean result = false;
      WebTarget cache = getService();
      if (cache!=null)
      {
          Response response = cache.path(KEYS_PATH).queryParam("keys", (Object[])keys).request().delete();
          result = response.readEntity(boolean.class);
      }
      return result;
    }



    /**
     * Cette methode renvoie le nombre de clés qui correspondent à une pattern.
     * Si la pattern n'est pas renseigné le wildcar est utilisé.
     * @param pattern
     * 
     * return le nombre de clés.
     */
    @Override
    public int size(String pattern)
    {
        int result = 0;
        WebTarget cache = getService();
        if (cache!=null)
        {
            Response response = cache.path("keys").path("size").queryParam(PATTERN_PARAM, pattern).request().get();
            result = response.readEntity(int.class);
        }
        return result;
    }



    /**
     * Cette methode verifie la présence d'une clé.     
     * @param pattern
     * 
     * return true si la clé est présente.
     */
    @Override
    public boolean containsKey(String key)
    {
        boolean result = false;
        WebTarget cache = getService();
        if (cache!=null)
        {
            Response response = cache.path("keys").path("contains").queryParam(PATTERN_PARAM, key).request().get();
            result = response.readEntity(boolean.class);
        }
        return result;
    }

    
    
    /**
     * Cette methode insère une clé et sa valeur dans le cache.
     * @param key
     * @param value
     * 
     * @return true si l'enregistrement est effectif.
     */
    @Override
    public boolean put(String key, String value)
    {        
        boolean result = false;
        if (value!=null)
        {
            WebTarget cache = getService();
            if (cache!=null)
            {
                Response response = cache.path(VALUE_PATH).path(key).request().post(Entity.entity(value, MediaType.APPLICATION_JSON+"; charset=utf-8"));
                result = response.readEntity(boolean.class);
            }
        }
        return result;
    }


    
    /**
     * Cette methode efface l'ensemble des données du cache.
     * 
     * @return true si le nettoyage est ok.
     */
    @Override
    public boolean clear()
    {
        boolean result = false;
        WebTarget cache = getService();
        if (cache!=null)
        {
            Response response = cache.path("clear").request().delete();
            result = response.readEntity(boolean.class);
        }
        return result;
    }

    
    
    /**
     * Cette methode enregistre les associations clé valeur dans le cache.
     * 
     * @param values
     * 
     * @return true si l'écriture est ok
     */
    @Override   
    public boolean put(Map<String,String> values)
    {
        boolean result = false;
        if (values!=null)
        {
            WebTarget cache = getService();
            if (cache!=null)
            {
                Response response = cache.path(VALUES_PATH).request().post(Entity.entity(values, MediaType.APPLICATION_JSON+"; charset=utf-8"));
                result = response.readEntity(boolean.class);
            }
        }
        return result;
    }

    
    
    /**
     * Cette methode récupère les valeurs associées à la liste des clés
     * fournie en paramètres
     * 
     * @param keys
     * 
     * @return la liste des valeurs
     */
    @Override   
    public Map<String, String> get(String... keys)
    {
        Map<String, String> result = null;
        WebTarget cache = getService();
        if (cache!=null)
        {
            Response response = cache.path(VALUES_PATH).queryParam("keys", (Object[])keys).request().get();
            result = response.readEntity( new GenericType<Map<String, String>>() {});
        }
        return result;
    }
    
    
    
    @Override
    protected void syncCache()
    {
        LOG.debug(MSG_NO_CACHE);
    }
    @Override
    protected void loadCache()
    {
        LOG.debug(MSG_NO_CACHE);
    }
    @Override
    protected void initStore()
    {
        LOG.debug(MSG_NO_CACHE);
    }
}