package br.com.alura.afterglow.bibliotecaAfterglow.model;

public class GutendexBook {
    private String title;
    private String author;
    private String language;
    private Integer publicationYear;
    private Integer authorBirthYear;
    private Integer authorDeathYear;

    // Getters e Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }

    public Integer getAuthorBirthYear() {
        return authorBirthYear;
    }

    public void setAuthorBirthYear(Integer authorBirthYear) {
        this.authorBirthYear = authorBirthYear;
    }

    public Integer getAuthorDeathYear() {
        return authorDeathYear;
    }

    public void setAuthorDeathYear(Integer authorDeathYear) {
        this.authorDeathYear = authorDeathYear;
    }
}
