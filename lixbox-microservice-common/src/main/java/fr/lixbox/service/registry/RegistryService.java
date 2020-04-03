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
package fr.lixbox.service.registry;

import java.io.Serializable;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import fr.lixbox.service.registry.model.ServiceEntry;

/**
 * @author ludovic.terral
 */
@Path(RegistryService.SERVICE_URI)
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
public interface RegistryService  extends Serializable
{  
    // ----------- Attribut(s) -----------   
    static final long serialVersionUID = 1201704311316L;
    
    static final String SERVICE_NAME = "global-service-api-registry";
    static final String SERVICE_VERSION = "1.0";
    public static final String SERVICE_URI = "/"+SERVICE_VERSION+"";
    public static final String FULL_SERVICE_URI = "/registry/api"+SERVICE_URI;
    final String SERVICE_CODE = "REGSERV";
    
    

    // ----------- Methodes ----------- 
    @GET @Path("/checkHealth") boolean checkHealth();
    @GET @Path("/version") String getVersion();
    
    @GET @Path("/register/{name}/{version}") boolean registerService( @PathParam("name") String name,  @PathParam("version") String version,  @QueryParam("uri") String uri);
    @DELETE @Path("/unregister/{name}/{version}") boolean unregisterService( @PathParam("name") String name,  @PathParam("version") String version,  @QueryParam("uri") String uri);
    @GET @Path("/discover/{name}/{version}") ServiceEntry discoverService( @PathParam("name") String name,  @PathParam("version") String version);
    @GET @Path("/entries/{name}") List<ServiceEntry> getEntries( @PathParam("name") String name);
    @GET @Path("/entries") List<ServiceEntry> getEntries();
}