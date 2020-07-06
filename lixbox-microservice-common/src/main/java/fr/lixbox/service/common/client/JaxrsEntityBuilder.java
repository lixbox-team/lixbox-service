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
package fr.lixbox.service.common.client;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Variant;

/**
 * Cette classe génère des entités pour les requêtes JAXRS
 * sur lesquelles on peut configurer l'encoding
 *
 * @author didier.gissinger
 * @author ludovic.terral
 */
public class JaxrsEntityBuilder
{
    private JaxrsEntityBuilder()
    {
        //Builder non instanciable
    }
    public static <T> Entity<T> entity(final T entity, final MediaType mediaType) {
        Variant variant = new Variant(mediaType.withCharset(StandardCharsets.UTF_8.toString()), (Locale)null, StandardCharsets.UTF_8.toString());
        return Entity.entity(entity, variant);
    }
    
    
    public static <T> Entity<T> entity(final T entity, final MediaType mediaType, final String encoding) {
        Variant variant = new Variant(mediaType.withCharset(encoding), (Locale)null, encoding);
        return Entity.entity(entity, variant);
    }

    
    public static <T> Entity<T> entity(final T entity, final String mediaType) {
        Variant variant = new Variant(MediaType.valueOf(mediaType).withCharset(StandardCharsets.UTF_8.toString()), (Locale)null, StandardCharsets.UTF_8.toString());
        return Entity.entity(entity, variant);
    }

    
    public static <T> Entity<T> entity(final T entity, final String mediaType, final String encoding) {
        Variant variant = new Variant(MediaType.valueOf(mediaType).withCharset(encoding), (Locale)null, encoding);
        return Entity.entity(entity, variant);
    }

    
    public static <T> Entity<T> text(final T entity) {
        Variant variant = new Variant(MediaType.TEXT_PLAIN_TYPE.withCharset(StandardCharsets.UTF_8.toString()), (Locale)null, StandardCharsets.UTF_8.toString());
        return Entity.entity(entity, variant);
    }

    
    public static <T> Entity<T> text(final T entity, final String encoding) {
        Variant variant = new Variant(MediaType.TEXT_PLAIN_TYPE.withCharset(encoding), (Locale)null, encoding);
        return Entity.entity(entity, variant);
    }

    
    public static <T> Entity<T> xml(final T entity) {
        Variant variant = new Variant(MediaType.APPLICATION_XML_TYPE.withCharset(StandardCharsets.UTF_8.toString()), (Locale)null, StandardCharsets.UTF_8.toString());
        return Entity.entity(entity, variant);
    }
    
    
    public static <T> Entity<T> xml(final T entity, final String encoding) {
        Variant variant = new Variant(MediaType.APPLICATION_XML_TYPE.withCharset(encoding), (Locale)null, encoding);
        return Entity.entity(entity, variant);
    }

    
    public static <T> Entity<T> json(final T entity) {
        Variant variant = new Variant(MediaType.APPLICATION_JSON_TYPE.withCharset(StandardCharsets.UTF_8.toString()), (Locale)null, StandardCharsets.UTF_8.toString());
        return Entity.entity(entity, variant);
    }

    
    public static <T> Entity<T> json(final T entity, final String encoding) {
        Variant variant = new Variant(MediaType.APPLICATION_JSON_TYPE.withCharset(encoding), (Locale)null, encoding);
        return Entity.entity(entity, variant);
    }

    
    public static <T> Entity<T> html(final T entity) {
        Variant variant = new Variant(MediaType.TEXT_HTML_TYPE.withCharset(StandardCharsets.UTF_8.toString()), (Locale)null, StandardCharsets.UTF_8.toString());
        return Entity.entity(entity, variant);
    }

    
    public static <T> Entity<T> html(final T entity, final String encoding) {
        Variant variant = new Variant(MediaType.TEXT_HTML_TYPE.withCharset(encoding), (Locale)null, encoding);
        return Entity.entity(entity, variant);
    }

    
    public static <T> Entity<T> xhtml(final T entity) {
        Variant variant = new Variant(MediaType.APPLICATION_XHTML_XML_TYPE.withCharset(StandardCharsets.UTF_8.toString()), (Locale)null, StandardCharsets.UTF_8.toString());
        return Entity.entity(entity, variant);
    }

    
    public static <T> Entity<T> xhtml(final T entity, final String encoding) {
        Variant variant = new Variant(MediaType.APPLICATION_XHTML_XML_TYPE.withCharset(encoding), (Locale)null, encoding);
        return Entity.entity(entity, variant);
    }

    
    public static Entity<Form> form(final Form entity) {
        Variant variant = new Variant(MediaType.APPLICATION_FORM_URLENCODED_TYPE.withCharset(StandardCharsets.UTF_8.toString()), (Locale)null, StandardCharsets.UTF_8.toString());
        return Entity.entity(entity, variant);
    }

    
    public static Entity<Form> form(final Form entity, final String encoding) {
        Variant variant = new Variant(MediaType.APPLICATION_FORM_URLENCODED_TYPE.withCharset(encoding), (Locale)null, encoding);
        return Entity.entity(entity, variant);
    }

    
    public static Entity<Form> form(final MultivaluedMap<String, String> formData) {
        Variant variant = new Variant(MediaType.APPLICATION_FORM_URLENCODED_TYPE.withCharset(StandardCharsets.UTF_8.toString()), (Locale)null, StandardCharsets.UTF_8.toString());
        return Entity.entity(new Form(formData), variant);
    }

    
    public static Entity<Form> form(final MultivaluedMap<String, String> formData, final String encoding) {
        Variant variant = new Variant(MediaType.APPLICATION_FORM_URLENCODED_TYPE.withCharset(encoding), (Locale)null, encoding);
        return Entity.entity(new Form(formData), variant);
    }
}
