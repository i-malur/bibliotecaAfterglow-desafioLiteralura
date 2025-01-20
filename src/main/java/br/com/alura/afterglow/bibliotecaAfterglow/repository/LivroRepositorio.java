package br.com.alura.afterglow.bibliotecaAfterglow.repository;

import br.com.alura.afterglow.bibliotecaAfterglow.model.Autor;
import br.com.alura.afterglow.bibliotecaAfterglow.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Year;
import java.util.List;

public interface LivroRepositorio extends JpaRepository<Livro, Long> {


    // Busca livros pelo título, ignorando maiúsculas e minúsculas
    @Query("SELECT l FROM Livro l WHERE LOWER(l.titulo) = LOWER(:titulo)")
    List<Livro> findByTituloIgnoreCase(@Param("titulo") String titulo);

    // Busca autores vivos em um determinado ano
    @Query("SELECT a FROM Autor a WHERE a.anoNascimento <= :ano AND (a.anoFalecimento IS NULL OR a.anoFalecimento >= :ano)")
    List<Autor> findAutoresVivos(@Param("ano") Year ano);

    // Busca autores nascidos no ano e ainda vivos ou com falecimento após o ano
    @Query("SELECT a FROM Autor a WHERE a.anoNascimento = :ano AND (a.anoFalecimento IS NULL OR a.anoFalecimento >= :ano)")
    List<Autor> findAutoresVivosRefinado(@Param("ano") Year ano);

    // Busca autores falecidos no ano específico
    @Query("SELECT a FROM Autor a WHERE a.anoNascimento <= :ano AND a.anoFalecimento = :ano")
    List<Autor> findAutoresPorAnoDeMorte(@Param("ano") Year ano);

    // Busca livros em um determinado idioma
    @Query("SELECT l FROM Livro l WHERE l.idioma LIKE %:idioma%")
    List<Livro> findByIdioma(@Param("idioma") String idioma);
}
