package be.dpa.bootiful.activities.infrastructure.bored;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

/**
 * Bored activity configuration.
 *
 * @author denis
 */
@Configuration
public class BoredActivityConfiguration {

    private SSLConnectionSocketFactory createSocketFactory() {
        try {
            ClassPathResource resource = new ClassPathResource("/boredapi-truststore.jks");
            SSLContext context = SSLContextBuilder.create()
                    .loadTrustMaterial(resource.getURL(), "Panda1337!".toCharArray())
                    .build();
            return new SSLConnectionSocketFactory(context);
        } catch (NoSuchAlgorithmException | KeyStoreException | CertificateException | IOException
                 | KeyManagementException e) {
            throw new IllegalStateException(e);
        }
    }

    @Bean
    public RestTemplate restTemplate() {
        var socketFactory = createSocketFactory();
        var client = HttpClients.custom().setSSLSocketFactory(socketFactory).build();
        return new RestTemplateBuilder().requestFactory(() -> new HttpComponentsClientHttpRequestFactory(client))
                .build();
    }
}
