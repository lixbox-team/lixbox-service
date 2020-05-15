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
package fr.lixbox.service.jaxrs.provider;

import org.jboss.resteasy.plugins.interceptors.CorsFilter;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

/**
 * Ce provider ajoute une extension dans le service JAXRS
 * 
 * @author ludovic.terral
 */
@Provider
public class CORSProvider implements Feature 
{
    // ----------- Methode(s) -----------  
    @Override
    public boolean configure(FeatureContext context) 
    {
        CorsFilter filter = new CorsFilter();
        filter.getAllowedOrigins().add("*");
        filter.setAllowedMethods("GET, POST, OPTIONS, HEAD");
        filter.setAllowedHeaders("access-control-allow-origin, authorization , accept, content-type, origin");
        context.register(filter);
        return true;
    }
}