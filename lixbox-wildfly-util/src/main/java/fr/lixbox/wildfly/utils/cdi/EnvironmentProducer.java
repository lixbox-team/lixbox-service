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
package fr.lixbox.wildfly.utils.cdi;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import fr.lixbox.wildfly.utils.WildFlyUtil;
import fr.lixbox.wildfly.utils.qualifiers.QSecureServerPort;
import fr.lixbox.wildfly.utils.qualifiers.QServerName;
import fr.lixbox.wildfly.utils.qualifiers.QServerPort;

/**
 * Cette classe fournit des informations sur wildfly à CDI.
 *  
 * @author ludovic.terral
 */
@ApplicationScoped
public class EnvironmentProducer
{
    // ----------- Attribut(s) -----------    
    @Inject private WildFlyUtil wildflyUtil;   
    
    
    
    // ----------- Methode(s) -----------  
    @Produces
    @QServerName
    public String getServerName()
    {
        return wildflyUtil.getHostName();
    }



    @Produces
    @QServerPort
    public int getServerPort()
    {
        return wildflyUtil.getHostPort();
    }



    @Produces
    @QSecureServerPort
    public int getSecurePort()
    {
        return wildflyUtil.getSecurePort();
    }
}