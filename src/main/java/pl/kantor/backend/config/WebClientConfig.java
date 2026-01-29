package pl.kantor.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${nbp.api.url:https://api.nbp.pl/api}")
    private String nbpApiUrl;

    @Bean
    public WebClient nbpWebClient() {
        return WebClient.builder()
                .baseUrl(nbpApiUrl)
                .build();
    }
}
