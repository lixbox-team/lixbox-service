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
package fr.lixbox.service.common.util;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

/**
 * Cet utilitaire suspend la vérification des certificats serveurs.
 * 
 * @AUTHOR Lixbox-team
 */
public class SSLContextUtil 
{
    public static SSLContext createTrustAllSSLContext() 
    {
        try 
        {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {
                        // Ne fait rien : toutes les connexions sont considérées comme sûres
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {
                        // Ne fait rien : toutes les connexions sont considérées comme sûres
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                }
            };
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            return sslContext;
        } 
        catch (NoSuchAlgorithmException | java.security.KeyManagementException e) 
        {
            throw new RuntimeException("Erreur lors de la création d'un SSLContext permissif", e);
        }
    }
}
