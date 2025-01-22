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
package fr.lixbox.service.common;

import java.io.Serializable;

import fr.lixbox.service.registry.model.health.ServiceState;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * Cette interface represente les methodes accessibles à distance
 * concernant les microservices.
 *
 * @author ludovic.terral
 */
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
public interface MicroService extends Serializable
{
	// ----------- Attribut -----------
	static final long serialVersionUID = 201706081514L;

    

	// ----------- Methode -----------
    @GET @Path("/health") ServiceState checkHealth();
    @GET @Path("/health/live") ServiceState checkLive();
    @GET @Path("/health/ready") ServiceState checkReady();
    @GET @Path("/version") String getVersion();
}