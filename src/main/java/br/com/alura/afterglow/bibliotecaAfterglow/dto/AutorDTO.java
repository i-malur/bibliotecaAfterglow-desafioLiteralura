package br.com.alura.afterglow.bibliotecaAfterglow.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

public record AutorDTO(@JsonAlias("name") String autor,
                       @JsonAlias("birth_year") Integer anoNascimento,
                       @JsonAlias("death_year") Integer anoFalecimento) {
}
