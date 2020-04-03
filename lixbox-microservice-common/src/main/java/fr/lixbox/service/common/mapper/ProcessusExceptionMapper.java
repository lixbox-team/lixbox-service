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

import fr.lixbox.common.exceptions.ProcessusException;

/**
 * Cette classe transforme une ProcessusException en une reponse
 * rest adequate.
 *  
 * @author ludovic.terral
 */
@Provider
public class ProcessusExceptionMapper implements ExceptionMapper<ProcessusException>
{
    @Override
    public Response toResponse(ProcessusException ex)
    {
        return Response.status(500).entity(ex.getMessage()).type("text/plain").build();
    }
}