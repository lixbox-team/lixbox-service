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