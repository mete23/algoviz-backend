package de.algoviz.algoviz;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuration class for the Algoviz application.
 *
 * @author David
 * @version 1.0
 */
@Configuration
public class AlgovizApplicationConfiguration {

    public static final String KAGEN_BACKEND_URL = "https://kagen.algoviz.de";
    private static final int DATA_BUFFER_SIZE = 512 * 1024 * 1024;

    /**
     * Creates a {@link WebClient} bean for the Kagen backend.
     *
     * @return the {@link WebClient}
     */
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(KAGEN_BACKEND_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer
                                .defaultCodecs()
                                .maxInMemorySize(DATA_BUFFER_SIZE))
                        .build())
                .build();
    }

}
