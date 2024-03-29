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

import static org.junit.Assert.fail;

import java.net.URI;

import org.junit.Test;

import fr.lixbox.service.msteams.model.Card;
import fr.lixbox.service.msteams.model.Section;
import fr.lixbox.service.msteams.model.action.ActionCard;
import fr.lixbox.service.msteams.model.action.HttpPost;
import fr.lixbox.service.msteams.model.action.OpenUri;
import fr.lixbox.service.msteams.model.input.DateInput;
import fr.lixbox.service.msteams.model.input.MultichoiceInput;
import fr.lixbox.service.msteams.model.input.MultichoiceOption;
import fr.lixbox.service.msteams.model.input.TextInput;

/**
 * 
 * @author ludovic.terral
 *
 */
public class TestTeamsClient
{
    private static final String URI = "https://lixtec.webhook.office.com/webhookb2/a33109e1-d967-4ee8-9398-cd2a033bcccf@9f42a344-3c4b-4a7f-bede-4577fca30ece/IncomingWebhook/e5b142b4bf36411db84add775f7bcc26/926b47a8-5875-45ab-8952-dba8c40aecc8";
    
    
    
    @Test
    public void test()
    {
        try
        {
            TeamsClient teamsClient = new TeamsClient(new URI(URI));
            
            Card message = new Card();
            message.setSummary("Card \"Test card\"");
            message.setThemeColor("0078D7");
            message.setTitle("Card created: \"Name of card\"");
            
            Section section = new Section("David Claux", "9/13/2016, 3:34pm", 
                    "https://connectorsdemo.azurewebsites.net/images/MSC12_Oscar_002.jpg",
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna "
                    + "aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.");
            section.addFact("Board", "Name of board");
            section.addFact("List", "Name of list");
            section.addFact("Assigned to", "Ludovic TERRAL");
            section.addFact("Due date", "(none)");
            message.addSection(section);
            
            ActionCard action = new ActionCard("Set due date");
            action.addInput(new DateInput("dueDate", "Select a date"));
            action.addAction(new HttpPost("Ok", "https://test"));
            message.addPotentialAction(action);
            
            action = new ActionCard("Move");
            MultichoiceInput input = new MultichoiceInput("dueDate", "Select a date");
            input.addOptions(new MultichoiceOption("List 1", "l1"));
            input.addOptions(new MultichoiceOption("List 2", "l2"));
            action.addInput(input);
            action.addAction(new HttpPost("Ok", "https://test"));
            message.addPotentialAction(action);

            action = new ActionCard("Add a comment");
            action.addInput(new TextInput("comment", true, "Enter your comment"));
            action.addAction(new HttpPost("Ok", "https://test"));
            message.addPotentialAction(action);
            
            message.addPotentialAction(new OpenUri("View in Trello", "https://"));
            
            teamsClient.sendMessage(message);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
}


