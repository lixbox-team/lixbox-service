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

import java.io.Serializable;
import java.net.Socket;
import java.net.URI;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSession;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.exception.ExceptionUtils;

import fr.lixbox.common.exceptions.BusinessException;
import fr.lixbox.common.exceptions.ProcessusException;
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
    
    
    
    public static ServiceState checkHealth(ServiceType type, String uri) 
    {
        ServiceState state;
        switch (type)
        {
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



    public static ServiceState checkHealthMicroProfileHealth(String uri)
    {
        ServiceState state;
        try
        {
            ClientBuilder cliBuilder = ClientBuilder.newBuilder();
            cliBuilder.hostnameVerifier((String hostname, SSLSession session) -> true);
            state = parseResponse(cliBuilder.build().target(URI.create(uri+"/health")).request().get(), new GenericType<ServiceState>(){});
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
        return state;
    }



    public static ServiceState checkHealthHttp(String uri)
    {
        ServiceState state = new ServiceState();
        try
        {
            ClientBuilder cliBuilder = ClientBuilder.newBuilder();
            cliBuilder.connectTimeout(1, TimeUnit.SECONDS);
            cliBuilder.hostnameVerifier((String hostname, SSLSession session) -> true);
            Response response = cliBuilder.build().target(URI.create(uri)).request().get();
            if (response.getStatus()>=200 && response.getStatus()<300) {
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
        return state;
    }



    public static ServiceState checkHealthTcp(String uri)
    {
        String hostName = uri.substring(6,uri.lastIndexOf(':'));
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
    
    
    
    public static <T> T parseResponse(Response response, GenericType<T> type) throws BusinessException
    {
        T result;
        switch(response.getStatus())
        {
            case 200:
            case 201:
                result = response.readEntity(type);
                break;
            case 404:
                throw new BusinessException(response.readEntity(String.class));
            default:
                throw new ProcessusException(response.readEntity(String.class));
        }
        return result;
    }
}
