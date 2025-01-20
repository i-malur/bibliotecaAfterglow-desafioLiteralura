package br.com.alura.afterglow.bibliotecaAfterglow.service;

import br.com.alura.afterglow.bibliotecaAfterglow.dto.LivroDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConverteDados implements IConverteDados {

    private final ObjectMapper objectMapper;

    public ConverteDados() {
        this.objectMapper = new ObjectMapper();
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public <T> T obterDados(String json, Class<T> classe) {
        return parseJson(json, classe);
    }

    public List<LivroDTO> obterListaDeLivros(String json) {
        return parseJsonList(json, LivroDTO.class);
    }

    private <T> T parseJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao processar o JSON: " + e.getMessage(), e);
        }
    }

    private <T> List<T> parseJsonList(String json, Class<T> clazz) {
        try {
            CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, clazz);
            return objectMapper.readValue(json, listType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao processar a lista de objetos JSON: " + e.getMessage(), e);
        }
    }
}
