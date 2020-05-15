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
package fr.lixbox.service.undertow.servlet;

import javax.servlet.ServletContext;

import fr.lixbox.service.undertow.authentication.HttpHeadersAuthenticationMechanism;
import io.undertow.servlet.ServletExtension;
import io.undertow.servlet.api.AuthMethodConfig;
import io.undertow.servlet.api.DeploymentInfo;

/**
 * Cette extension de servlet sert à traiter le certificat lorsqu'il est
 * transmis via une entête HTTP.
 * 
 * @author didier.gissinger
 * @author ludovic.terral
 */
public class HttpHeadersAuthExtension implements ServletExtension
{
    // ----------- Methodes -----------
    @Override
    public void handleDeployment(DeploymentInfo deploymentInfo, ServletContext servletContext)
    {
        deploymentInfo.addAuthenticationMechanism(HttpHeadersAuthenticationMechanism.MECHANISM_NAME, new HttpHeadersAuthenticationMechanism.Factory());
        AuthMethodConfig authConfig = new AuthMethodConfig(HttpHeadersAuthenticationMechanism.MECHANISM_NAME);
        deploymentInfo.getLoginConfig().addFirstAuthMethod(authConfig);
    }
}