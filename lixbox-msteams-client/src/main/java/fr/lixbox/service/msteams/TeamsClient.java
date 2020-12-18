/*******************************************************************************
 *    
 *                           FRAMEWORK Lixbox
 *                          ==================
 *      
 *    This file is part of lixbox-service.
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
package fr.lixbox.service.msteams;

import java.net.URI;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import fr.lixbox.common.exceptions.BusinessException;
import fr.lixbox.common.exceptions.ProcessusException;
import fr.lixbox.common.resource.LixboxResources;
import fr.lixbox.io.json.JsonUtil;
import fr.lixbox.service.common.MicroService;
import fr.lixbox.service.common.client.MicroServiceClient;
import fr.lixbox.service.msteams.model.Card;

/**
 * Cette classe est le client d'accès au service Microsoft Teams.
 * 
 * @author ludovic.terral
 *
 */
public class TeamsClient extends MicroServiceClient implements MicroService
{
    // ----------- Attribut(s) -----------   
    private static final long serialVersionUID = 6978802125622879826L;
    private static final String MSG_ERROR_EXCEPUTI_02 = "MSG.ERROR.EXCEPUTI_02";
    private static final String MSG_ERROR_EXCEPUTI_09 = "MSG.ERROR.EXCEPUTI_09";
    private static final String SERVICE_CODE = "TEAMSSERV";
    

    
    // ----------- Methode(s) -----------
    public TeamsClient(URI uri)
    {
        staticUri = uri.toString();
        init();
    }
    public TeamsClient(String serviceName, String serviceVersion)
    {
        this.serviceName = serviceName;
        this.serviceVersion = serviceVersion;
        init();
    }
    public TeamsClient(String serviceRegistryUri,String serviceName, String serviceVersion)
    {
        this.serviceName = serviceName;
        this.serviceVersion = serviceVersion;
        init(serviceRegistryUri);
    }
    @Override
    protected void loadInfosService()
    {
        //no cache
    }
    @Override
    protected void syncCache()
    {
        //no cache
    }
    @Override
    protected void loadCache()
    {
        //no cache
    }
    @Override
    protected void initStore()
    {
        //no cache
    }
    
    
    
    public boolean sendMessage(Card message) throws BusinessException
    {
      //Controler les parametres
        if (message==null)
        {
            throw new BusinessException(LixboxResources.getString(
                    MSG_ERROR_EXCEPUTI_02, 
                    new String[] { SERVICE_CODE, "message" }));
        }
        
        boolean result=false;
        WebTarget service = getService();
        if(service!=null)
        {
            Response response = service
                    .path("/")
                    .request().post(Entity.json(JsonUtil.transformObjectToJson(message, false)));
            result = parseResponse(response, new GenericType<Boolean>() {});
        }
        else
        {
            throw new ProcessusException(LixboxResources.getString(MSG_ERROR_EXCEPUTI_09,
                    new String[]{SERVICE_CODE, serviceName}));
        }
        return result;
    }
}