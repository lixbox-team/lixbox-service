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
