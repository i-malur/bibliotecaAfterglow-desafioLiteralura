package br.com.alura.afterglow.bibliotecaAfterglow;

import br.com.alura.afterglow.bibliotecaAfterglow.controller.BookController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class App implements CommandLineRunner {

    @Autowired
    private BookController bookController;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        int option = -1;

        while (option != 0) {
            System.out.println("Menu:");
            System.out.println("1. Buscar livro pelo título");
            System.out.println("2. Listar livros registrados");
            System.out.println("3. Listar autores registrados");
            System.out.println("4. Listar autores vivos em um determinado ano");
            System.out.println("5. Listar livros em um determinado idioma");
            System.out.println("0. Sair");
            System.out.print("Selecione uma opção: ");
            option = scanner.nextInt();
            scanner.nextLine(); // Consumir a nova linha

            switch (option) {
                case 1:
                    System.out.print("Digite o título do livro: ");
                    String title = scanner.nextLine();
                    bookController.searchBookByTitle(title);
                    break;
                case 2:
                    bookController.listAllBooks();
                    break;
                case 3:
                    bookController.listAllAuthors();
                    break;
                case 4:
                    System.out.print("Digite o ano: ");
                    int year = scanner.nextInt();
                    scanner.nextLine(); // Consumir a nova linha
                    bookController.listAuthorsAliveInYear(year);
                    break;
                case 5:
                    System.out.print("Digite o idioma: ");
                    String language = scanner.nextLine();
                    bookController.listBooksByLanguage(language);
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }
}
