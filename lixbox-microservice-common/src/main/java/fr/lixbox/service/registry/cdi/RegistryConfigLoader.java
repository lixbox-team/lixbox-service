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
package fr.lixbox.service.registry.cdi;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.lixbox.common.exceptions.ProcessusException;
import fr.lixbox.common.util.StringUtil;

/**
 * Cettte classe charge et produit la config du registry.
 * 
 * @author ludovic.terral
 */
@Singleton
public class RegistryConfigLoader implements Serializable
{
    // ----------- Attribut -----------
    private static final long serialVersionUID = 1305718542842L;
    private static final Log LOG = LogFactory.getLog(RegistryConfigLoader.class);
    private static final String WILDFLY_LINUX_PATH = "/opt/wildfly/";
    private static final String ROOT_LINUX_PATH = "/";
    
    private static String registryURI = lireConfigLocale();
    
        

    // ----------- Methode -----------  
    /**
     * Cette methode renvoie l'url du registry configurée dans le fichier
     */
    @Produces @ApplicationScoped @WildflyLocalRegistryConfig
    public String getRegistryURI()
    {   
        if (StringUtil.isEmpty(registryURI))
        {
            setRegistryURI(lireConfigLocale());
        }
        
        if (StringUtil.isEmpty(registryURI))
        {   
            LOG.debug("========> ABSENCE DE FICHIER DE CONFIGURATION DU REGISTRY");
        }
        else
        {
            LOG.info("Uri du RegistryService extraite du fichier: "+registryURI);
        }
        return registryURI;
    }
    public static void setRegistryURI(String registryURI)
    {
        RegistryConfigLoader.registryURI = registryURI;
    }



    /**
     * Cette methode recupere la configuration ecrite sur le disque dur
     *  
     * @return
     */
    private static String lireConfigLocale()
    {
        String result = null;
        
        //lecture du fichier de configuration temporaire
        File configfile = Paths.get(System.getProperty("java.io.tmpdir") + "/registryConfig.properties").toFile();  
        result = extractRegistryUriFromFile(configfile);

        if (StringUtil.isEmpty(result))
        {
            //lecture du fichier de configuration permanent
            String userDir = System.getProperty("user.dir");
            if (userDir.equals(ROOT_LINUX_PATH))
            {
                userDir = WILDFLY_LINUX_PATH;
            }
            configfile = Paths.get(userDir.replace("bin", "") + "/appconfig/registryConfig.properties").toFile();
            result = extractRegistryUriFromFile(configfile);
        }
        return result;
    }
    
    
    
    private static String extractRegistryUriFromFile(File configfile)
    {
        String registryUri="";
        if (configfile.exists())
        {
            try (FileInputStream fis = new FileInputStream(configfile))
            {
                Properties properties = new Properties();
                properties.load(fis);
                                            
                //Unmarshall Data
                registryUri = properties.getProperty("registry.uri");
            }
            catch (ProcessusException e)
            {
                LOG.error(e, e);
                throw e;
            }
            catch (Exception e)
            {
                LOG.error(e, e);
                throw new ProcessusException(e);
            }
        }
        return registryUri;
    }
}