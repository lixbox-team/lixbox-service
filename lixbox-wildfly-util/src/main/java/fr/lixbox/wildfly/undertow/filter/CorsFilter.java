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
package fr.lixbox.wildfly.undertow.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;

/**
 * Cette classe assure le traitement des headers
 * lié à CORS.
 * 
 * @author ludovic.terral
 */
public class CorsFilter implements HttpHandler
{
    // ----------- Attributs -----------    
    private static final Log log = LogFactory.getLog(CorsFilter.class);
    private static final String ORIGIN_KEY = "Origin";
    private static final String ACCESS_CONTROL_ALLOW_METHODS_KEY = "Access-Control-Allow-Methods";
    private static final String ACCESS_CONTROL_ALLOW_HEADERS_KEY = "Access-Control-Allow-Headers";
    private static final String ACCESS_CONTROL_ALLOW_ORIGIN_KEY = "Access-Control-Allow-Origin";
    private static final String ACCESS_CONTROL_ALLOW_CREDENTIALS_KEY = "Access-Control-Allow-Credentials";
    
    private HttpHandler next;
    private String acceptedOrigin = "*";



    // ----------- Methodes -----------
    public CorsFilter(HttpHandler next)
    {
        this.next = next;
    }


    public void setAcceptedOrigin(String s) 
    {
        this.acceptedOrigin = s;
    }


    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception
    {
        exchange.getResponseHeaders().add(new HttpString(ACCESS_CONTROL_ALLOW_CREDENTIALS_KEY), "true");
        exchange.getResponseHeaders().add(new HttpString(ACCESS_CONTROL_ALLOW_HEADERS_KEY), "origin, content-type, accept, authorization");
        exchange.getResponseHeaders().add(new HttpString(ACCESS_CONTROL_ALLOW_METHODS_KEY), "GET, POST, OPTIONS");
        if (exchange.getRequestHeaders().getFirst(ORIGIN_KEY)!=null && "*".equals(acceptedOrigin))
        {
            log.info("==================== Origin: "+exchange.getRequestHeaders().getFirst(ORIGIN_KEY));
            exchange.getResponseHeaders().add(new HttpString(ACCESS_CONTROL_ALLOW_ORIGIN_KEY) , exchange.getRequestHeaders().getFirst(ORIGIN_KEY));
        }
        else
        {
            exchange.getResponseHeaders().add(new HttpString(ACCESS_CONTROL_ALLOW_ORIGIN_KEY), acceptedOrigin);
        }
        next.handleRequest(exchange);
    }
}