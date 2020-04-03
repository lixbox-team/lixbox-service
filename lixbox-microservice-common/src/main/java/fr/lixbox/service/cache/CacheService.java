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
package fr.lixbox.service.cache;

import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import fr.lixbox.service.common.MicroService;

/**
 * Cette interface represente les fonctions disponnibles
 * dans le service de cache.
 * 
 * @author ludovic.terral
 */
@Path(CacheService.SERVICE_URI)
@Produces({ MediaType.APPLICATION_JSON+"; charset=utf-8" })
@Consumes({ MediaType.APPLICATION_JSON+"; charset=utf-8" })
public interface CacheService  extends MicroService
{  
    // ----------- Attribut(s) -----------   
    static final long serialVersionUID = 1306745024315L;
    static final String SERVICE_NAME = "global-service-api-cache";
    static final String SERVICE_VERSION = "1.0";
    static final String SERVICE_URI = SERVICE_VERSION;
    static final String FULL_SERVICE_URI = "/cache/api/"+SERVICE_URI;
    static final String SERVICE_CODE = "CACHSERV";
    
    

    // ----------- Methodes -----------     
    @GET @Path("/keys") List<String> getKeys(@QueryParam("pattern") String pattern);
    @GET @Path("/keys/contains") boolean containsKey(@QueryParam("pattern") String pattern);
    @GET @Path("/keys/size") int size(@QueryParam("pattern") String pattern);
    
    @DELETE @Path("/keys/{key}") boolean remove(@PathParam("key") String key);
    @DELETE @Path("/keys")  boolean remove(@QueryParam("keys") String... keys);
    @DELETE @Path("/clear") boolean clear();
    
    @POST @Path("/values") boolean put(Map<String,String> values);
    @GET @Path("/values") Map<String,String> get(@QueryParam("keys") String... keys);
    @POST @Path("/value/{key}") boolean put(@PathParam("key") String key, String value);
    @GET @Path("/value/{key}") String get(@PathParam("key") String key);
}
