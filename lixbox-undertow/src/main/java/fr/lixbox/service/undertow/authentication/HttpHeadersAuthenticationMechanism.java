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
package fr.lixbox.service.undertow.authentication;


import java.io.ByteArrayInputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Map;

import io.undertow.security.api.AuthenticationMechanism;
import io.undertow.security.api.AuthenticationMechanismFactory;
import io.undertow.security.api.SecurityContext;
import io.undertow.security.idm.Account;
import io.undertow.security.idm.Credential;
import io.undertow.security.idm.IdentityManager;
import io.undertow.security.idm.X509CertificateCredential;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.form.FormParserFactory;
import io.undertow.util.HeaderValues;
import io.undertow.util.HttpString;
import io.undertow.util.StatusCodes;

/**
 * Ce mécanisme d'authentification récupère et autorise une session via un certificat transmis 
 * dans un header HTTP : X-SSL-Client-Cert-Base64
 * 
 * @author didier.gissinger
 * @author ludovic.terral
 */
public class HttpHeadersAuthenticationMechanism implements AuthenticationMechanism
{
    // ----------- Attributs -----------    
    public static final String MECHANISM_NAME = "HTTP-HEADERS";
    private static final String CERT_HEADER = "X-SSL-Client-Cert-Base64";
    private static final ChallengeResult CHALLENGE_RESULT = new ChallengeResult(true, StatusCodes.UNAUTHORIZED);



    // ----------- Methodes -----------
    @Override
    @SuppressWarnings("deprecation")
    public AuthenticationMechanismOutcome authenticate(HttpServerExchange exchange,
            SecurityContext securityContext)
    {
        Account account = securityContext.getAuthenticatedAccount();
        if (account != null)
        {
            return AuthenticationMechanismOutcome.AUTHENTICATED;
        }
        try
        {
            HeaderValues header = authorizationHeader(exchange);
            if (header == null)
            {
                return AuthenticationMechanismOutcome.NOT_ATTEMPTED;
            }
            byte[] derCert = Base64.getDecoder().decode(header.getFirst());
            ByteArrayInputStream is =  new ByteArrayInputStream(derCert);
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            Certificate cert = cf.generateCertificate(is);
            is.close();
            Credential credential = new X509CertificateCredential((X509Certificate) cert);
            IdentityManager idm = securityContext.getIdentityManager();
            account = idm.verify(credential);
            if (account != null) 
            {
                securityContext.authenticationComplete(account, MECHANISM_NAME, false);
                return AuthenticationMechanismOutcome.AUTHENTICATED;
            }
        }
        catch (Exception e)
        {
            securityContext.authenticationFailed(e.getMessage(), MECHANISM_NAME);
        }
        return AuthenticationMechanismOutcome.NOT_ATTEMPTED;
    }



    @Override
    public ChallengeResult sendChallenge(HttpServerExchange exchange,
            SecurityContext securityContext)
    {
        return CHALLENGE_RESULT;
    }
    
    
    
    protected HeaderValues authorizationHeader(HttpServerExchange exchange) {
        return exchange.getRequestHeaders().get(new HttpString(CERT_HEADER));
    }
    

    public static final class Factory implements AuthenticationMechanismFactory 
    {
        @Override
        public AuthenticationMechanism create(String mechanismName,
                FormParserFactory formParserFactory, Map<String, String> properties)
        {
            return new HttpHeadersAuthenticationMechanism();
        }
    }
}
