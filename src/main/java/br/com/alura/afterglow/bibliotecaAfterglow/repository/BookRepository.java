package br.com.alura.afterglow.bibliotecaAfterglow.repository;

import br.com.alura.afterglow.bibliotecaAfterglow.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByLanguageIgnoreCase(String language);

    List<Book> findByAuthorDeathYearNullAndAuthorBirthYearLessThanEqual(Integer year);
}
