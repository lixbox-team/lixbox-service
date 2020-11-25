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
    MANUAL, TCP, HTTP, MICRO_PROFILE;
}
