package com.bfh.service;

import com.bfh.dto.GenerateWebhookRequest;
import com.bfh.dto.GenerateWebhookResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ExamClient {
    private final WebClient webClient;

    public ExamClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<GenerateWebhookResponse> generateWebhook(GenerateWebhookRequest request) {
        return webClient.post()
                .uri("/hiring/generateWebhook/JAVA")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(GenerateWebhookResponse.class);
    }
    public Mono<String> submitFinalQuery(String submitUrl, String accessToken, String finalQuery) {
    record FinalBody(String finalQuery) {}
    return webClient.post()
            .uri(submitUrl)
            .header("Authorization", accessToken) // JWT
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new FinalBody(finalQuery))
            .retrieve()
            .bodyToMono(String.class);
}

}
