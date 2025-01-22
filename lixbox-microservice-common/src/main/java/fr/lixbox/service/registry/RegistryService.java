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
package fr.lixbox.service.registry;

import java.util.List;

import fr.lixbox.service.common.MicroService;
import fr.lixbox.service.registry.model.ServiceEntry;
import fr.lixbox.service.registry.model.ServiceType;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

/**
 * @author ludovic.terral
 */
@Path(RegistryService.SERVICE_URI)
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
public interface RegistryService  extends MicroService
{  
    // ----------- Attribut(s) -----------   
    static final long serialVersionUID = 1201704311316L;
    
    static final String SERVICE_NAME = "global-service-api-registry";
    static final String SERVICE_VERSION = "1.0";
    public static final String SERVICE_URI = "/"+SERVICE_VERSION+"";
    public static final String FULL_SERVICE_URI = "/registry/api"+SERVICE_URI;
    final String SERVICE_CODE = "REGSERV";
    
    

    // ----------- Methodes ----------- 
    @GET @Path("/register/{name}/{version}") boolean registerService( @PathParam("name") String name,  @PathParam("version") String version, @QueryParam("type") ServiceType type,  @QueryParam("uri") String uri, @QueryParam("endPointUri") String endPointUri);
    @DELETE @Path("/unregister/{name}/{version}") boolean unregisterService( @PathParam("name") String name,  @PathParam("version") String version,  @QueryParam("uri") String uri);
    @GET @Path("/discover/{name}/{version}") ServiceEntry discoverService( @PathParam("name") String name,  @PathParam("version") String version);
    @GET @Path("/entries/{name}") List<ServiceEntry> getEntries( @PathParam("name") String name);
    @GET @Path("/entries") List<ServiceEntry> getEntries();
}