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

