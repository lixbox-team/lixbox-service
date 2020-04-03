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

/**
 * Client filter that will do token or bearer authentication.  
 * You must allocate it and then register it with the Client or WebTarget
 *
 * @author ludovic.terral
 */
public class TokenAuthentication implements ClientRequestFilter
{
    // ----------- Attribut(s) -----------
    private final String authHeader;



    // ----------- Methode(s) -----------
    public TokenAuthentication(String type, String token)
    {
        authHeader = type + " " + token;
    }



    @Override
    public void filter(ClientRequestContext requestContext) throws IOException
    {
        requestContext.getHeaders().putSingle(HttpHeaders.AUTHORIZATION, authHeader);
    }
}
