package br.com.alura.afterglow.bibliotecaAfterglow.controller;

import br.com.alura.afterglow.bibliotecaAfterglow.dto.LivroDTO;
import br.com.alura.afterglow.bibliotecaAfterglow.service.GutendexService;
import br.com.alura.afterglow.bibliotecaAfterglow.util.ConsoleMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Scanner;

@Controller
public class MenuController {

    @Autowired
    private GutendexService gutendexService;

    public void exibirMenu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            ConsoleMenu.exibirMenu();
            int opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir a quebra de linha

            switch (opcao) {
                case 1 -> buscarLivroPeloTitulo(scanner);
                case 2 -> listarLivrosRegistrados();
                case 0 -> {
                    System.out.println("Saindo...");
                    return;
                }
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private void buscarLivroPeloTitulo(Scanner scanner) {
        System.out.print("Digite o título do livro: ");
        String titulo = scanner.nextLine();

        List<LivroDTO> livros = gutendexService.buscarLivros(titulo);
        if (!livros.isEmpty()) {
            livros.forEach(System.out::println);
        } else {
            System.out.println("Nenhum livro encontrado.");
        }
    }

    private void listarLivrosRegistrados() {
        System.out.println("\nFuncionalidade a ser implementada.");
    }
}
