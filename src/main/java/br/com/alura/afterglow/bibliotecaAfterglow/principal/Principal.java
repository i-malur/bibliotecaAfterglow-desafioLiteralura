package br.com.alura.afterglow.bibliotecaAfterglow.principal;

import br.com.alura.afterglow.bibliotecaAfterglow.dto.LivroDTO;
import br.com.alura.afterglow.bibliotecaAfterglow.model.Autor;
import br.com.alura.afterglow.bibliotecaAfterglow.model.Livro;
import br.com.alura.afterglow.bibliotecaAfterglow.repository.LivroRepositorio;
import br.com.alura.afterglow.bibliotecaAfterglow.service.ConsumoAPI;
import br.com.alura.afterglow.bibliotecaAfterglow.service.ConverteDados;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Year;
import java.util.List;
import java.util.Scanner;

@Component
public class Principal {

    @Autowired
    private LivroRepositorio livroRepository;

    @Autowired
    private ConsumoAPI consumoAPI;

    @Autowired
    private ConverteDados converteDados;

    private final Scanner leitura = new Scanner(System.in);

    public void executar() {
        boolean running = true;
        while (running) {
            exibirMenu();
            if (leitura.hasNextInt()) { // Verifica se a entrada é um número inteiro
                var opcao = leitura.nextInt();
                leitura.nextLine();
                switch (opcao) {
                    case 1 -> buscarLivrosPeloTitulo();
                    case 2 -> listarLivrosRegistrados();
                    case 3 -> listarAutoresRegistrados();
                    case 4 -> listarAutoresVivos();
                    case 5 -> listarAutoresVivosRefinado();
                    case 6 -> listarAutoresPorAnoDeMorte();
                    case 7 -> listarLivrosPorIdioma();
                    case 0 -> {
                        System.out.println("Encerrando...");
                        running = false; // Encerrando o loop
                    }
                    default -> System.out.println("Opção inválida!");
                }
            } else {
                System.out.println("Por favor, insira um número válido.");
                leitura.nextLine();
            }
        }
        leitura.close();
        System.out.println("Programa encerrado.");
    }

    private void exibirMenu() {
        System.out.println("""
            ===========================================================
                                Biblioteca Afterglow
                   Escolha um número no menu abaixo:
            -----------------------------------------------------------
                                 Menu
                       1- Buscar livros pelo título
                       2- Listar livros registrados
                       3- Listar autores registrados
                       4- Listar autores vivos em um determinado ano
                       5- Listar autores nascidos em determinado ano
                       6- Listar autores por ano de sua morte
                       7- Listar livros em um determinado idioma
                       0- Sair
            """);
    }

    private void buscarLivrosPeloTitulo() {
        System.out.print("Digite o título do livro: ");
        String titulo = leitura.nextLine();
        String endereco = "https://gutendex.com/books?search=" + titulo.replace(" ", "+");
        System.out.println("URL da API: " + endereco);

        try {
            String jsonResponse = consumoAPI.consumirAPI(endereco);
            if (jsonResponse == null || jsonResponse.isEmpty()) {
                System.out.println("Resposta da API está vazia.");
                return;
            }

            JsonNode resultsNode = converteDados.getObjectMapper().readTree(jsonResponse).path("results");
            if (resultsNode.isEmpty()) {
                System.out.println("Não foi possível encontrar o livro buscado.");
                return;
            }

            List<LivroDTO> livrosDTO = converteDados.getObjectMapper()
                    .readerForListOf(LivroDTO.class)
                    .readValue(resultsNode);

            exibirLivrosDTO(livrosDTO);

            salvarLivrosNaoRegistrados(livrosDTO);
        } catch (Exception e) {
            System.out.println("Erro ao buscar livros: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void salvarLivrosNaoRegistrados(List<LivroDTO> livrosDTO) {
        List<Livro> livrosExistentes = livroRepository.findAll();
        livrosDTO.stream()
                .filter(livroDTO -> livrosExistentes.stream()
                        .noneMatch(livroExistente -> livroExistente.getTitulo().equalsIgnoreCase(livroDTO.titulo().trim())))
                .map(this::mapearParaLivro)
                .forEach(livro -> {
                    try {
                        livroRepository.save(livro);
                        System.out.println("Livro '" + livro.getTitulo() + "' registrado com sucesso.");
                    } catch (Exception e) {
                        System.out.println("O livro '" + livro.getTitulo() + "' já está registrado. Ignorando duplicação.");
                    }
                });
    }

    private Livro mapearParaLivro(LivroDTO livroDTO) {
        System.out.println("Mapeando livro: " + livroDTO.titulo());
        Autor autor = livroDTO.autores().isEmpty() ? null : Autor.fromDTO(livroDTO.autores().get(0));
        String idioma = livroDTO.idioma().isEmpty() ? "Desconhecido" : livroDTO.idioma().get(0);
        Livro livro = new Livro(livroDTO.titulo(), autor, idioma, livroDTO.numeroDownload());
        System.out.println("Livro mapeado: " + livro.getTitulo() + " - " + livro.getAutor());
        return livro;
    }



    private void exibirLivrosDTO(List<LivroDTO> livrosDTO) {
        livrosDTO.forEach(System.out::println);
    }

    private void listarLivrosRegistrados() {
        livroRepository.findAll().forEach(System.out::println);
    }

    private void listarAutoresRegistrados() {
        livroRepository.findAll().stream()
                .map(Livro::getAutor)
                .distinct()
                .forEach(System.out::println);
    }

    private void listarAutoresVivos() {
        listarAutoresPorAno("vivos", livroRepository::findAutoresVivos);
    }

    private void listarAutoresVivosRefinado() {
        listarAutoresPorAno("nascidos", livroRepository::findAutoresVivosRefinado);
    }

    private void listarAutoresPorAnoDeMorte() {
        listarAutoresPorAno("faleceram", livroRepository::findAutoresPorAnoDeMorte);
    }

    private void listarAutoresPorAno(String tipo, AutorFetcher fetcher) {
        System.out.print("Digite o ano: ");
        Year ano = Year.of(leitura.nextInt());
        leitura.nextLine();

        List<Autor> autores = fetcher.fetchAutores(ano);

        if (autores.isEmpty()) {
            System.out.println("Nenhum autor encontrado.");
        } else {
            System.out.printf("Autores que %s no ano de %s:%n", tipo, ano);
            autores.forEach(System.out::println);
        }
    }

    private void listarLivrosPorIdioma() {
        System.out.print("Digite o idioma: ");
        String idioma = leitura.nextLine();

        livroRepository.findByIdioma(idioma).forEach(System.out::println);
    }

    @FunctionalInterface
    private interface AutorFetcher {
        List<Autor> fetchAutores(Year ano);
    }
}
