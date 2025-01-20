package br.com.alura.afterglow.bibliotecaAfterglow.controller;

import br.com.alura.afterglow.bibliotecaAfterglow.service.BookService;
import br.com.alura.afterglow.bibliotecaAfterglow.model.Book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class BookController {

    @Autowired
    private BookService bookService;

    public void searchBookByTitle(String title) {
        List<Book> books = bookService.searchBooksByTitle(title);
        if (books.isEmpty()) {
            System.out.println("Nenhum livro encontrado com o título: " + title);
        } else {
            books.forEach(book -> System.out.println("Título: " + book.getTitle() + ", Autor: " + book.getAuthor()));
        }
    }

    public void listAllBooks() {
        List<Book> books = bookService.getAllBooks();
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
        List<Book> authors = bookService.getAuthorsAliveInYear(year);
        if (authors.isEmpty()) {
            System.out.println("Nenhum autor vivo encontrado no ano: " + year);
        } else {
            authors.forEach(author -> System.out.println("Autor: " + author.getAuthor() + ", Ano de nascimento: " + author.getAuthorBirthYear()));
        }
    }

    public void listBooksByLanguage(String language) {
        List<Book> books = bookService.getBooksByLanguage(language);
        if (books.isEmpty()) {
            System.out.println("Nenhum livro encontrado no idioma: " + language);
        } else {
            books.forEach(book -> System.out.println("Título: " + book.getTitle() + ", Autor: " + book.getAuthor()));
        }
    }
}
