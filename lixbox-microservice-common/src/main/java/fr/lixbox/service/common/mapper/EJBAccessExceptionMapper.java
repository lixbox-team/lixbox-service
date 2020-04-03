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
package fr.lixbox.service.common.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Cette classe transforme une ProcessusException en une reponse
 * rest adequate.
 *  
 * @author ludovic.terral
 */
@Provider
public class EJBAccessExceptionMapper implements ExceptionMapper<javax.ejb.EJBAccessException>
{
    @Override
    public Response toResponse(javax.ejb.EJBAccessException ex)
    {
        return Response.status(401).entity("Authentification required").type("text/plain").build();
    }
}