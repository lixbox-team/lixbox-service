/*******************************************************************************
 *    
 *                           FRAMEWORK Lixbox
 *                          ==================
 *      
 * This file is part of lixbox-service.
 *
 *    lixbox-supervision is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    lixbox-supervision is distributed in the hope that it will be useful,
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
package fr.lixbox.wildfly.utils;

import java.lang.management.ManagementFactory;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.lixbox.common.exceptions.ProcessusException;

/**
 * Cette classe est utilitaire d'accès à Wildfly.
 * 
 * @author ludovic.terral
 */
@Startup
@Singleton
public class WildFlyUtil
{
    // ----------- Attribut(s) -----------    
    private static final Log LOG = LogFactory.getLog(WildFlyUtil.class);
    private String hostName = "localhost";
    private Integer hostPort = 8080;
    private Integer hostSecurePort = 8443;



    // ----------- Methode(s) -----------
    @PostConstruct
    void init()
    {
        try
        {
            MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
            ObjectName ws = new ObjectName("jboss.ws", "service", "ServerConfig");
            hostName = (String) mBeanServer.getAttribute(ws, "WebServiceHost");
            hostPort = (Integer) mBeanServer.getAttribute(ws, "WebServicePort");
            hostSecurePort = (Integer) mBeanServer.getAttribute(ws, "WebServiceSecurePort");
            LOG.info("--> " + hostName + " : " + hostPort + "/" + hostSecurePort);
        }
        catch (Exception e)
        {
            LOG.error(e);
            throw new ProcessusException(e);
        }
    }



    public String getHostName()
    {
        return hostName;
    }



    public int getHostPort()
    {
        return hostPort;
    }



    public int getSecurePort()
    {
        return hostSecurePort;
    }
}