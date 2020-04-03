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
