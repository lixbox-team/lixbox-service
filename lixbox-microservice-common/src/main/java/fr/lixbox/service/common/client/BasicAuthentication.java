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

import java.io.IOException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.HttpHeaders;

import org.jboss.resteasy.util.BasicAuthHelper;

/**
 * Client filter that will do token or bearer authentication.  
 * You must allocate it and then register it with the Client or WebTarget
 *
 * @author ludovic.terral
 */
public class BasicAuthentication implements ClientRequestFilter
{
   private final String authHeader;

   /**
    *
    * @param username user name
    * @param password password
    */
   public BasicAuthentication(final String username, final String password)
   {
      authHeader = BasicAuthHelper.createHeader(username, password);
   }

   @Override
   public void filter(ClientRequestContext requestContext) throws IOException
   {
      requestContext.getHeaders().putSingle(HttpHeaders.AUTHORIZATION, authHeader);
   }
}

