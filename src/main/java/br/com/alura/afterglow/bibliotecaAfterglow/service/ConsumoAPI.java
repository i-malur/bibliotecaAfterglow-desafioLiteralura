package br.com.alura.afterglow.bibliotecaAfterglow.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class ConsumoAPI {

    private static final Logger logger = LoggerFactory.getLogger(ConsumoAPI.class);
    private static final int HTTP_OK = 200;
    private static final int HTTP_MOVED_PERMANENTLY = 301;
    private static final int HTTP_FOUND = 302;

    private final HttpClient client;

    public ConsumoAPI() {
        this.client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();
    }

    public String consumirAPI(String url) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == HTTP_OK) {
                logger.info("Resposta da API: {}", response.body());
                return response.body();
            } else if (response.statusCode() == HTTP_MOVED_PERMANENTLY || response.statusCode() == HTTP_FOUND) {
                String novaURL = response.headers().firstValue("location").orElse("");
                logger.warn("Redirecionamento detectado. Nova URL: {}", novaURL);
                return consumirAPI(novaURL); // Tenta consumir a nova URL
            } else {
                logger.error("Erro na requisição para a URL {}. Status code: {}", url, response.statusCode());
                return null;
            }
        } catch (IOException | InterruptedException e) {
            logger.error("Erro ao consumir a API na URL {}: ", url, e);
            return null;
        }
    }
}
