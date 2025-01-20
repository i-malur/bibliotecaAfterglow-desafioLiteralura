package br.com.alura.afterglow.bibliotecaAfterglow.mapper;

import br.com.alura.afterglow.bibliotecaAfterglow.model.Book;
import br.com.alura.afterglow.bibliotecaAfterglow.dto.BookDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookMapper {

    public BookDTO toDTO(Book book) {
        return new BookDTO(book.getTitle(), book.getAuthor(), book.getLanguage(),
                book.getPublicationYear(), book.getAuthorBirthYear(), book.getAuthorDeathYear());
    }

    public List<BookDTO> toDTOList(List<Book> books) {
        return books.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
