package br.com.alura.afterglow.bibliotecaAfterglow.dto;

public class LivroDTO {
    private String title;
    private String author;
    private int downloadCount;
    private String language;

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

    public int getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return "Título: " + title + ", Autor: " + author + ", Idioma: " + language +
                ", Downloads: " + downloadCount;
    }
}

