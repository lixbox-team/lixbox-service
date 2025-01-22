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
package fr.lixbox.service.cache;

import java.util.List;
import java.util.Map;

import fr.lixbox.service.common.MicroService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

/**
 * Cette interface represente les fonctions disponibles
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
