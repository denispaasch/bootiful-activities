package be.dpa.bootiful.activities.infrastructure.bored;

import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.core5.ssl.SSLContexts;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${activity.truststore.password:}")
    private String truststorePassword;

    private SSLContext createContext() {
        ClassPathResource resource = new ClassPathResource("/boredapi-truststore.jks");
        try {
            return SSLContexts.custom().loadTrustMaterial(resource.getURL(),
                    truststorePassword.toCharArray()).build();
        } catch (NoSuchAlgorithmException | IOException | KeyStoreException | CertificateException
                 | KeyManagementException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Registers a rest template with a trust store to access the bored API.
     *
     * @return a rest template
     */
    @Bean
    public RestTemplate restTemplate() {
        var socketFactory = createContext();
        PoolingHttpClientConnectionManager connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
                .setSSLSocketFactory(SSLConnectionSocketFactoryBuilder.create()
                        .setSslContext(socketFactory)
                        .build())
                .build();

        var client = HttpClients.custom().setConnectionManager(connectionManager).build();
        return new RestTemplateBuilder().requestFactory(() -> new HttpComponentsClientHttpRequestFactory(client))
                .build();
    }
}
