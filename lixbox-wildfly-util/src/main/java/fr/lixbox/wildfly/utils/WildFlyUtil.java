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