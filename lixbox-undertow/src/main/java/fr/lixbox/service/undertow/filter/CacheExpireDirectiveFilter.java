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
package fr.lixbox.service.undertow.filter;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

/**
 * Ce filter genere les headers pour les resources
 * 
 * @author ludovic.terral
 */
@WebFilter(urlPatterns = { "/resources/*", "/javax.faces.resource/*" })
public class CacheExpireDirectiveFilter implements Filter
{
    // ----------- Methodes -----------
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException
    {
        final DateFormat httpDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'");
        final Calendar calendar = new GregorianCalendar();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        calendar.add(Calendar.MONTH, 1);
        ((HttpServletResponse) response).addHeader("Expires", httpDateFormat.format(calendar.getTime()));
        chain.doFilter(request, response);
    }



    @Override
    public void destroy()
    {
        //override obligatoire
    }



    @Override
    public void init(FilterConfig fConfig) throws ServletException
    {
        //override obligatoire
    }
}