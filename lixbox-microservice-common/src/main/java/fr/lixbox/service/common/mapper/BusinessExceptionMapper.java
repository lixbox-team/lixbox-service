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

import fr.lixbox.common.exceptions.BusinessException;

/**
 * Cette classe transforme une BusinessException en une reponse
 * rest adequate.
 *  
 * @author ludovic.terral
 */
@Provider
public class BusinessExceptionMapper implements ExceptionMapper<BusinessException>
{
    @Override
    public Response toResponse(BusinessException ex)
    {
        return Response.status(404).entity(ex.getMessage()).type("text/plain").build();
    }
}