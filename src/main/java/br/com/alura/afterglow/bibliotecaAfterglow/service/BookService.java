package br.com.alura.afterglow.bibliotecaAfterglow.service;

import br.com.alura.afterglow.bibliotecaAfterglow.repository.BookRepository;
import br.com.alura.afterglow.bibliotecaAfterglow.model.Book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> searchBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<String> getAllAuthors() {
        return bookRepository.findAll().stream()
                .map(Book::getAuthor)
                .distinct()
                .toList();
    }

    public List<Book> getBooksByLanguage(String language) {
        return bookRepository.findByLanguageIgnoreCase(language);
    }

    public List<Book> getAuthorsAliveInYear(int year) {
        return bookRepository.findByAuthorDeathYearNullAndAuthorBirthYearLessThanEqual(year);
    }
}
