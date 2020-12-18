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
package fr.lixbox.service.msteams.model.action;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * https://docs.microsoft.com/en-us/outlook/actionable-messages/card-reference#openuri-action
 *
 * @author ludovic.terral
 */
public enum OperatingSystem
{
    @JsonProperty("default") DEFAULT, 
    @JsonProperty("iOS") IOS, 
    @JsonProperty("android") ANDROID,
    @JsonProperty("linux") LINUX,
    @JsonProperty("windows") WINDOWS;
}
