package br.com.alura.afterglow.bibliotecaAfterglow.service;

import br.com.alura.afterglow.bibliotecaAfterglow.model.Book;
import br.com.alura.afterglow.bibliotecaAfterglow.dto.BookDTO;
import br.com.alura.afterglow.bibliotecaAfterglow.model.GutendexBook;
import br.com.alura.afterglow.bibliotecaAfterglow.model.GutendexResponse;
import br.com.alura.afterglow.bibliotecaAfterglow.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GutendexService {

    @Autowired
    private final RestTemplate restTemplate;

    @Autowired
    private BookRepository bookRepository;

    public GutendexService() {
        this.restTemplate = new RestTemplate();
    }

    public List<BookDTO> fetchBooksFromGutendex(String title) {
        // Codificando o título para a URL
        String encodedTitle = UriComponentsBuilder.fromUriString("https://gutendex.com/books/")
                .queryParam("title", title)
                .toUriString();

        // Requisição à API Gutendex
        GutendexResponse response = restTemplate.getForObject(encodedTitle, GutendexResponse.class);

        if (response != null && response.getResults() != null) {
            List<BookDTO> books = response.getResults().stream()
                    .map(this::convertToBookDTO)
                    .collect(Collectors.toList());

            // Salvando os livros no banco de dados
            saveBooksToDatabase(books);

            return books;
        }
        return List.of();
    }


    private BookDTO convertToBookDTO(GutendexBook book) {
        return new BookDTO(book.getTitle(), book.getAuthor(), book.getLanguage(),
                book.getPublicationYear(), book.getAuthorBirthYear(), book.getAuthorDeathYear());
    }

    private void saveBooksToDatabase(List<BookDTO> books) {
        for (BookDTO bookDTO : books) {
            // Verifica se o livro já existe no banco de dados
            Book existingBook = bookRepository.findByTitleContainingIgnoreCase(bookDTO.getTitle())
                    .stream().findFirst().orElse(null);

            if (existingBook == null) {
                // Se não existir, cria um novo livro e salva
                Book book = new Book(bookDTO.getTitle(), bookDTO.getAuthor(), bookDTO.getLanguage(),
                        bookDTO.getPublicationYear(), bookDTO.getAuthorBirthYear(), bookDTO.getAuthorDeathYear());
                bookRepository.save(book);
            }
        }
    }

}
