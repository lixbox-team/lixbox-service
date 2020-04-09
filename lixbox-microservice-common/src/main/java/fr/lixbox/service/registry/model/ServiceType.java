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
package fr.lixbox.service.registry.model;

/**
 * Cette énumération sert à spécifier le type d'un service
 * 
 * TCP: service tcp -> check tcp connect
 * HTTP: service http -> check http wait for 200<=status<=299
 * MICRO_PROFILE: service REST supportant la spec microprofile health: service 200 + payload
 *  
 * @author ludovic.terral
 */
public enum ServiceType
{
    TCP, HTTP, MICRO_PROFILE;
}
