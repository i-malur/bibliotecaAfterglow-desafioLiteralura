package br.com.alura.afterglow.bibliotecaAfterglow.controller;

import br.com.alura.afterglow.bibliotecaAfterglow.dto.BookDTO;
import br.com.alura.afterglow.bibliotecaAfterglow.service.BookService;
import br.com.alura.afterglow.bibliotecaAfterglow.service.GutendexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private GutendexService gutendexService;

    public void searchBookByTitle(String title) {
        // Enviando o título codificado para a API
        List<BookDTO> books = gutendexService.fetchBooksFromGutendex(title);

        if (books.isEmpty()) {
            System.out.println("Nenhum livro encontrado com o título: " + title);
        } else {
            books.forEach(book -> System.out.println("Título: " + book.getTitle() + ", Autor: " + book.getAuthor()));
        }
    }


    public void listAllBooks() {
        List<BookDTO> books = bookService.getAllBooks();
        if (books.isEmpty()) {
            System.out.println("Nenhum livro registrado.");
        } else {
            books.forEach(book -> System.out.println("Título: " + book.getTitle() + ", Autor: " + book.getAuthor()));
        }
    }

    public void listAllAuthors() {
        List<String> authors = bookService.getAllAuthors();
        if (authors.isEmpty()) {
            System.out.println("Nenhum autor registrado.");
        } else {
            authors.forEach(System.out::println);
        }
    }

    public void listAuthorsAliveInYear(int year) {
        List<BookDTO> authors = bookService.getAuthorsAliveInYear(year);
        if (authors.isEmpty()) {
            System.out.println("Nenhum autor vivo encontrado no ano: " + year);
        } else {
            authors.forEach(author -> System.out.println("Autor: " + author.getAuthor() + ", Ano de nascimento: " + author.getAuthorBirthYear()));
        }
    }

    public void listBooksByLanguage(String language) {
        List<BookDTO> books = bookService.getBooksByLanguage(language);
        if (books.isEmpty()) {
            System.out.println("Nenhum livro encontrado no idioma: " + language);
        } else {
            books.forEach(book -> System.out.println("Título: " + book.getTitle() + ", Autor: " + book.getAuthor()));
        }
    }
}
