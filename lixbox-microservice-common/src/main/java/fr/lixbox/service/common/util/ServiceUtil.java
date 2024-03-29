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
package fr.lixbox.service.common.util;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.net.Socket;
import java.net.URI;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSession;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

import com.fasterxml.jackson.core.type.TypeReference;

import fr.lixbox.common.exceptions.BusinessException;
import fr.lixbox.common.exceptions.ProcessusException;
import fr.lixbox.common.util.StringUtil;
import fr.lixbox.io.json.JsonUtil;
import fr.lixbox.service.common.model.Instance;
import fr.lixbox.service.registry.model.ServiceType;
import fr.lixbox.service.registry.model.health.Check;
import fr.lixbox.service.registry.model.health.ServiceState;
import fr.lixbox.service.registry.model.health.ServiceStatus;

/**
 * Cet utilitaire sert à checker un service
 *  
 * @author ludovic.terral
 */
public class ServiceUtil implements Serializable
{
    // ----------- Attribut(s) -----------   
    private static final long serialVersionUID = -202004091526L;
    

    
    // ----------- Methode(s) -----------
    private ServiceUtil() {
        //classe utilitaire
    }
    
    
    
    public static Client getPooledClient(int poolSize, String proxyHost, Integer proxyPort)
    {
        ResteasyClientBuilder cliBuilder = (ResteasyClientBuilder) ClientBuilder.newBuilder();
        cliBuilder.connectionPoolSize(poolSize);
        cliBuilder.hostnameVerifier((String hostname, SSLSession session) -> true);
        cliBuilder.connectionTTL(1, TimeUnit.MINUTES);
        cliBuilder.connectionCheckoutTimeout(50, TimeUnit.MILLISECONDS);
        cliBuilder.connectTimeout(2, TimeUnit.SECONDS);
        cliBuilder.readTimeout(10, TimeUnit.SECONDS);
        cliBuilder.disableTrustManager();
        if (StringUtil.isNotEmpty(proxyHost))
        {
            cliBuilder.defaultProxy(proxyHost,proxyPort);
        }
        return cliBuilder.build();
    }
    
    
    public static ServiceState checkHealth(ServiceType type, String uri) 
    {
        if (type==null)
        {
            type=ServiceType.TCP;
        }
        ServiceState state;
        switch (type)
        {
            case MANUAL:
                state = checkHealthManual(uri);
                break;
            case HTTP:
                state = checkHealthHttp(uri);
                break;
            case MICRO_PROFILE:
                state = checkHealthMicroProfileHealth(uri);
                break;
            case TCP:
            default:
                state = checkHealthTcp(uri);
                break;
        }
        return state;
    }

    
    
    public static ServiceState checkHealth(ServiceType type, Instance instance) 
    {
        if (type==null)
        {
            type=ServiceType.TCP;
        }
        ServiceState state;
        switch (type)
        {
            case MANUAL:
                state = checkHealthManual(instance.getManualCheckUri());
                break;
            case HTTP:
                state = checkHealthHttp(instance.getUri());
                break;
            case MICRO_PROFILE:
                state = checkHealthMicroProfileHealth(instance.getUri());
                break;
            case TCP:
            default:
                state = checkHealthTcp(instance.getUri());
                break;
        }
        return state;
    }


    
    public static ServiceState checkHealthManual(String uri)
    {   
        ServiceState state = new ServiceState();
        state.setStatus(ServiceStatus.UP);
        if (StringUtil.isNotEmpty(uri) && (uri.startsWith("tcp") || uri.startsWith("remote")))
        {
            state = checkHealthTcp(uri);
        }
        if (StringUtil.isNotEmpty(uri) && uri.startsWith("http"))
        {
            state = checkHealthHttp(uri);
        }
        return state;
    }

    

    public static ServiceState checkHealthMicroProfileHealth(String uri)
    {
        ServiceState state;
        Client client = getPooledClient(1,"",0);
        try
        {
            state = parseResponse(client.target(URI.create(uri+"/health")).request().get(), new GenericType<ServiceState>(){});
        }
        catch (BusinessException e) 
        {
            state = new ServiceState();
            state.setStatus(ServiceStatus.DOWN);
            Check check = new Check(ServiceStatus.DOWN, "IS SERVICE LIVE?");
            check.getData().put("error", ExceptionUtils.getMessage(e));
            state.getChecks().add(check);
        }
        catch (ProcessingException pe) 
        {
            state = new ServiceState();
            state.setStatus(ServiceStatus.DOWN);            
            Check check = new Check(ServiceStatus.DOWN, "IS SERVICE LIVE?");
            check.getData().put("error", ExceptionUtils.getMessage(pe));
            state.getChecks().add(check);
        }
        client.close();
        return state;
    }



    public static ServiceState checkHealthHttp(String uri)
    {
        ServiceState state = new ServiceState();
        Client client = getPooledClient(1,"",0);
        try (Response response = client.target(URI.create(uri)).request().get())
        {
            if (response!=null && (response.getStatus()>=200 && response.getStatus()<300))
            {
                state.setStatus(ServiceStatus.UP);
            }
            else 
            {
                state.setStatus(ServiceStatus.DOWN);
            }
        }
        catch (ProcessingException pe) 
        {
            state.setStatus(ServiceStatus.DOWN);
            Check check = new Check(ServiceStatus.DOWN, "IS SERVICE LIVE?");
            check.getData().put("error", ExceptionUtils.getMessage(pe));
            state.getChecks().add(check);
        }
        client.close();
        return state;
    }



    public static ServiceState checkHealthTcp(String uri)
    {
        String hostName = uri.substring(uri.indexOf(':')+3,uri.lastIndexOf(':'));
        String port = uri.substring(uri.lastIndexOf(':')+1);
        
        
        ServiceState state;
        try(
            Socket s = new Socket(hostName, Integer.parseInt(port));
        )
        {
            s.setSoTimeout(1000);            
            state = new ServiceState();
            state.setStatus(s.isBound()?ServiceStatus.UP:ServiceStatus.DOWN);
        }
        catch (Exception e)
        {
            state = new ServiceState();
            state.setStatus(ServiceStatus.DOWN);
            Check check = new Check(ServiceStatus.DOWN, "IS SERVICE LIVE?");
            check.getData().put("error", ExceptionUtils.getMessage(e));
            state.getChecks().add(check);
        }
        return state;
    }
    
    
    
    public static <T> T parseReponse(Response response, Type type) throws BusinessException
    {
        T result;
        switch(response.getStatus())
        {
            case 200:
            case 201:
                result = JsonUtil.transformJsonToObject(response.readEntity(String.class), new TypeReference<T>()
                {
                    @Override
                    public Type getType() {
                        return  type;
                    }
                });
                response.close();
                break;
            case 401:
                response.close();
                throw new ProcessusException("401 - NECESSITE UNE AUTHENTIFICATION");
            case 403:
                response.close();
                throw new ProcessusException("403 - VOUS N'ETES PAS AUTORISE A UTILISER CETTE RESSOURCE");
            case 404:
                BusinessException busExcept = JsonUtil.transformJsonToObject(response.readEntity(String.class), new TypeReference<BusinessException>(){});
                response.close();
                throw busExcept;
            case 503:
                ProcessusException processExcept = JsonUtil.transformJsonToObject(response.readEntity(String.class), new TypeReference<ProcessusException>(){});
                response.close();
                throw processExcept;
                
            default:
                String msg = response.readEntity(String.class);
                response.close();
                throw new ProcessusException(msg);
        }
        return result;
    }
    
    
    
    public static <T> T parseResponse(Response response, GenericType<T> type) throws BusinessException
    {
        return parseReponse(response, type.getType());
    }
    
    
    
    public static <T> T parseResponse(Response response, TypeReference<T> type) throws BusinessException
    {
        return parseReponse(response, type.getType());
    }
}
