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
package fr.lixbox.service.common.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Cette classe represente la classe de configuration pour le client
 * @author Ludovic.Terral
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientConfig implements Serializable
{
    // ----------- Attribut(s) -----------   
    private static final long serialVersionUID = 202501302251L;
    private static final Log LOG = LogFactory.getLog(ClientConfig.class);
    
    private int poolSize;
    private String proxyHost;
    private Integer proxyPort;
    private long connectionTTL;
    private TimeUnit connectionTTLUnit;
    private long connectionCheckoutTimeout;
    private TimeUnit connectionCheckoutTimeoutUnit;
    private long connectTimeout;
    private TimeUnit connectTimeoutUnit;
    private long readTimeout;
    private TimeUnit readTimeoutUnit;

    
    
    // ----------- Attribut(s) -----------   
    public ClientConfig(int poolSize, String proxyHost, Integer proxyPort,
                        long connectionTTL, TimeUnit connectionTTLUnit,
                        long connectionCheckoutTimeout, TimeUnit connectionCheckoutTimeoutUnit,
                        long connectTimeout, TimeUnit connectTimeoutUnit,
                        long readTimeout, TimeUnit readTimeoutUnit) 
    {
        this.poolSize = poolSize;
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
        this.connectionTTL = connectionTTL;
        this.connectionTTLUnit = connectionTTLUnit;
        this.connectionCheckoutTimeout = connectionCheckoutTimeout;
        this.connectionCheckoutTimeoutUnit = connectionCheckoutTimeoutUnit;
        this.connectTimeout = connectTimeout;
        this.connectTimeoutUnit = connectTimeoutUnit;
        this.readTimeout = readTimeout;
        this.readTimeoutUnit = readTimeoutUnit;
    }
    


    public int getPoolSize()
    {
        return poolSize;
    }



    public String getProxyHost()
    {
        return proxyHost;
    }



    public Integer getProxyPort()
    {
        return proxyPort;
    }



    public long getConnectionTTL()
    {
        return connectionTTL;
    }
    public TimeUnit getConnectionTTLUnit()
    {
        return connectionTTLUnit;
    }



    public long getConnectionCheckoutTimeout()
    {
        return connectionCheckoutTimeout;
    }
    public TimeUnit getConnectionCheckoutTimeoutUnit()
    {
        return connectionCheckoutTimeoutUnit;
    }



    public long getConnectTimeout()
    {
        return connectTimeout;
    }
    public TimeUnit getConnectTimeoutUnit()
    {
        return connectTimeoutUnit;
    }



    public long getReadTimeout()
    {
        return readTimeout;
    }
    public TimeUnit getReadTimeoutUnit()
    {
        return readTimeoutUnit;
    }



    @Override
    public int hashCode()
    {
        return Objects.hash(connectTimeout, connectTimeoutUnit, connectionCheckoutTimeout,
                connectionCheckoutTimeoutUnit, connectionTTL, connectionTTLUnit, poolSize,
                proxyHost, proxyPort, readTimeout, readTimeoutUnit);
    }



    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ClientConfig other = (ClientConfig) obj;
        return connectTimeout == other.connectTimeout
                && connectTimeoutUnit == other.connectTimeoutUnit
                && connectionCheckoutTimeout == other.connectionCheckoutTimeout
                && connectionCheckoutTimeoutUnit == other.connectionCheckoutTimeoutUnit
                && connectionTTL == other.connectionTTL
                && connectionTTLUnit == other.connectionTTLUnit && poolSize == other.poolSize
                && Objects.equals(proxyHost, other.proxyHost)
                && Objects.equals(proxyPort, other.proxyPort) && readTimeout == other.readTimeout
                && readTimeoutUnit == other.readTimeoutUnit;
    }


    
    @Override
    public String toString() 
    {
        String result = "";
        ObjectMapper mapper = new ObjectMapper();
        try {
            result = mapper.writeValueAsString(this);
        } 
        catch (JsonProcessingException e) 
        {
            LOG.error(e);
        }
        return result;
    }
}
