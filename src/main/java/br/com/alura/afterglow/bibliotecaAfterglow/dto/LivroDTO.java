package br.com.alura.afterglow.bibliotecaAfterglow.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LivroDTO(@JsonAlias("title") String titulo,
                       @JsonAlias("download_count") Double numeroDownload,
                       @JsonAlias("languages") List<String> idioma,
                       @JsonAlias("authors") List<AutorDTO> autores) {

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Título: %s\n", titulo));
        sb.append("Autor(es): \n");
        for (AutorDTO autor : autores) {
            sb.append(String.format("  - %s\n", autor.autor()));
        }
        sb.append(String.format("Idioma(s): %s\n", String.join(", ", idioma)));
        sb.append(String.format("Downloads: %.0f\n", numeroDownload));
        sb.append("----------------------------------------");
        return sb.toString();
    }
}

