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