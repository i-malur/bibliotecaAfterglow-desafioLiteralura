package br.com.alura.afterglow.bibliotecaAfterglow.service;

import br.com.alura.afterglow.bibliotecaAfterglow.mapper.BookMapper;
import br.com.alura.afterglow.bibliotecaAfterglow.repository.BookRepository;
import br.com.alura.afterglow.bibliotecaAfterglow.model.Book;
import br.com.alura.afterglow.bibliotecaAfterglow.dto.BookDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookMapper bookMapper;

    public List<BookDTO> searchBooksByTitle(String title) {
        List<Book> books = bookRepository.findByTitleContainingIgnoreCase(title);
        return bookMapper.toDTOList(books);
    }

    public List<BookDTO> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return bookMapper.toDTOList(books);
    }

    public List<String> getAllAuthors() {
        return bookRepository.findAll().stream()
                .map(Book::getAuthor)
                .distinct()
                .toList();
    }

    public List<BookDTO> getBooksByLanguage(String language) {
        List<Book> books = bookRepository.findByLanguageIgnoreCase(language);
        return bookMapper.toDTOList(books);
    }

    public List<BookDTO> getAuthorsAliveInYear(int year) {
        List<Book> books = bookRepository.findByAuthorDeathYearNullAndAuthorBirthYearLessThanEqual(year);
        return bookMapper.toDTOList(books);
    }
}
