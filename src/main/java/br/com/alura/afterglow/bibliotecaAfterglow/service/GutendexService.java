package br.com.alura.afterglow.bibliotecaAfterglow.service;

import br.com.alura.afterglow.bibliotecaAfterglow.dto.LivroDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class GutendexService {

    private static final String GUTENDEX_API_URL = "https://gutendex.com/books/";

    private final RestTemplate restTemplate;

    @Autowired
    public GutendexService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<LivroDTO> buscarLivros(String query) {
        String url = GUTENDEX_API_URL + "?search=" + query;
        var response = restTemplate.getForObject(url, GutendexResponse.class);
        return response != null ? response.getBooks() : List.of();
    }
}
