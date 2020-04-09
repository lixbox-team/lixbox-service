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
package fr.lixbox.service.common.util;

import org.junit.Assert;
import org.junit.Test;

import fr.lixbox.service.registry.model.ServiceType;
import fr.lixbox.service.registry.model.health.ServiceStatus;

public class ServiceUtilTest
{
    @Test
    public void test_checkHealthTcp_ok()
    {
        Assert.assertTrue(ServiceUtil.checkHealthTcp("tcp://www.google.fr:80")!=null);
        Assert.assertTrue(ServiceStatus.UP.equals(ServiceUtil.checkHealthTcp("tcp://www.google.fr:80").getStatus()));
    }
    
    @Test
    public void test_checkHealthTcp_ko()
    {
        Assert.assertTrue(ServiceUtil.checkHealthTcp("tcp://www.google.fr:180")!=null);
        Assert.assertTrue(ServiceStatus.DOWN.equals(ServiceUtil.checkHealthTcp("tcp://www.google.fr:180").getStatus()));
    }
    @Test
    public void test_checkHealthHttp_ok()
    {
        Assert.assertTrue(ServiceUtil.checkHealthHttp("https://www.google.fr:443")!=null);
        Assert.assertTrue(ServiceStatus.UP.equals(ServiceUtil.checkHealthHttp("https://www.google.fr:443").getStatus()));
    }
    
    @Test
    public void test_checkHealthHttp_ko()
    {
        Assert.assertTrue(ServiceUtil.checkHealthHttp("https://www.google.fr:4443")!=null);
        Assert.assertTrue(ServiceStatus.DOWN.equals(ServiceUtil.checkHealthHttp("https://www.google.fr:4443").getStatus()));
    }
    
    @Test
    public void test_checkHealth_ok()
    {
        Assert.assertTrue(ServiceUtil.checkHealth(ServiceType.HTTP, "https://www.google.fr:4443")!=null);
        Assert.assertTrue(ServiceStatus.DOWN.equals(ServiceUtil.checkHealth(ServiceType.HTTP, "https://www.google.fr:4443").getStatus()));
    }
}
