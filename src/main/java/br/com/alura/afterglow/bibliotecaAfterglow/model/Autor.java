package br.com.alura.afterglow.bibliotecaAfterglow.model;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import br.com.alura.afterglow.bibliotecaAfterglow.dto.AutorDTO;
import jakarta.persistence.*;

@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String autor;

    @Column(name = "ano_nascimento")
    private Year anoNascimento;

    @Column(name = "ano_falecimento")
    private Year anoFalecimento;

    @OneToMany(mappedBy = "autor", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Livro> livros = new ArrayList<>();

    public Autor() {}

    public Autor(String autor, Year anoNascimento, Year anoFalecimento) {
        this.autor = autor;
        this.anoNascimento = anoNascimento;
        this.anoFalecimento = anoFalecimento;
    }

    public static Autor fromDTO(AutorDTO autorDTO) {
        if (autorDTO == null) {
            throw new IllegalArgumentException("O DTO de Autor não pode ser nulo.");
        }
        return new Autor(
                autorDTO.autor(),
                autorDTO.anoNascimento() != null ? Year.of(autorDTO.anoNascimento()) : null,
                autorDTO.anoFalecimento() != null ? Year.of(autorDTO.anoFalecimento()) : null
        );
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public Year getAnoNascimento() {
        return anoNascimento;
    }

    public void setAnoNascimento(Year anoNascimento) {
        this.anoNascimento = anoNascimento;
    }

    public Year getAnoFalecimento() {
        return anoFalecimento;
    }

    public void setAnoFalecimento(Year anoFalecimento) {
        this.anoFalecimento = anoFalecimento;
    }

    public List<Livro> getLivros() {
        return livros;
    }

    public void setLivros(List<Livro> livros) {
        this.livros = livros;
    }

    @Override
    public String toString() {
        String nascimento = anoNascimento != null ? anoNascimento.toString() : "Desconhecido";
        String falecimento = anoFalecimento != null ? anoFalecimento.toString() : "Desconhecido";
        return "Autor: " + autor + ", Nascimento: " + nascimento + ", Falecimento: " + falecimento;
    }

    public static boolean possuiAno(Year ano) {
        return ano != null;
    }
}
