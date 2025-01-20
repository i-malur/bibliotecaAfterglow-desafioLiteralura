package br.com.alura.afterglow.bibliotecaAfterglow.principal;

import br.com.alura.afterglow.bibliotecaAfterglow.dto.AutorDTO;
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
import java.util.*;
import java.util.stream.Collectors;

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
                    running = false;
                }
                default -> System.out.println("Opção inválida!");
            }
        }
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

    private void salvarLivros(List<Livro> livros) {
        livros.forEach(livroRepository::save);
    }

    private void buscarLivrosPeloTitulo() {
        String baseURL = "https://gutendex.com/books?search=";
        System.out.print("Digite o título do livro: ");
        String titulo = leitura.nextLine();
        String endereco = baseURL + titulo.replace(" ", "%20");
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

            List<Livro> livrosExistentes = livroRepository.findByTituloIgnoreCase(titulo);
            List<LivroDTO> livrosNaoRegistrados = filtrarLivrosNaoRegistrados(livrosDTO, livrosExistentes);

            if (!livrosNaoRegistrados.isEmpty()) {
                System.out.println("Salvando novos livros encontrados...");
                salvarLivros(livrosNaoRegistrados);
                System.out.println("Livros salvos com sucesso!");
            } else {
                System.out.println("Todos os livros já estão registrados.");
            }

            exibirLivros(livrosNaoRegistrados);
        } catch (Exception e) {
            System.out.println("Erro ao buscar livros: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private List<LivroDTO> filtrarLivrosNaoRegistrados(List<LivroDTO> livrosDTO, List<Livro> livrosExistentes) {
        return livrosDTO.stream()
                .filter(livroDTO -> livrosExistentes.stream()
                        .noneMatch(livroExistente -> livroExistente.getTitulo().equals(livroDTO.titulo())))
                .collect(Collectors.toList());
    }

    private void exibirLivrosDTO(List<LivroDTO> livrosDTO) {
        livrosDTO.forEach(livroDTO -> System.out.println("Livro encontrado: " + livroDTO));
    }

    private void salvarLivros(List<LivroDTO> livrosDTO) {
        livrosDTO.stream()
                .map(this::mapearParaLivro)
                .forEach(livroRepository::save);
    }

    private Livro mapearParaLivro(LivroDTO livroDTO) {
        Autor autor = buscarAutor(livroDTO.autores().get(0));
        String idioma = livroDTO.idioma().isEmpty() ? "Desconhecido" : livroDTO.idioma().get(0);
        return new Livro(livroDTO.titulo(), autor, idioma, livroDTO.numeroDownload());
    }

    private Autor buscarAutor(AutorDTO autorDTO) {
        List<Autor> autores = livroRepository.findAutoresVivos(Year.now());
        return autores.stream()
                .filter(autor -> autor.getAutor().equals(autorDTO.autor()))
                .findFirst()
                .orElse(null);
    }

    private void exibirLivros(List<LivroDTO> livrosDTO) {
        Set<String> titulosExibidos = new HashSet<>();
        livrosDTO.stream()
                .filter(livro -> titulosExibidos.add(livro.titulo()))
                .forEach(System.out::println);
    }

    private void listarLivrosRegistrados() {
        livroRepository.findAll().forEach(System.out::println);
    }

    private void listarAutoresRegistrados() {
        livroRepository.findAll().stream()
                .map(Livro::getAutor)
                .distinct()
                .forEach(autor -> System.out.println(autor.getAutor()));
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
        Integer ano = leitura.nextInt();
        leitura.nextLine();

        Year year = Year.of(ano);
        List<Autor> autores = fetcher.fetchAutores(year);

        if (autores.isEmpty()) {
            System.out.println("Nenhum autor encontrado.");
        } else {
            System.out.println("Lista de autores que " + tipo + " no ano de " + ano + ":\n");
            autores.forEach(autor -> System.out.println(formatAutor(autor)));
        }
    }

    private String formatAutor(Autor autor) {
        return Autor.possuiAno(autor.getAnoNascimento()) && Autor.possuiAno(autor.getAnoFalecimento()) ?
                String.format("%s (%d - %d)", autor.getAutor(), autor.getAnoNascimento(), autor.getAnoFalecimento()) :
                autor.getAutor();
    }

    private void listarLivrosPorIdioma() {
        System.out.println("""
            Digite o idioma pretendido:
            Inglês (en)
            Português (pt)
            Espanhol (es)
            Francês (fr)
            Alemão (de)
            """);
        String idioma = leitura.nextLine();

        livroRepository.findByIdioma(idioma).forEach(livro -> {
            System.out.println("Título: " + livro.getTitulo());
            System.out.println("Autor: " + livro.getAutor().getAutor());
            System.out.println("Idioma: " + livro.getIdioma());
            System.out.println("----------------------------------------");
        });
    }

    @FunctionalInterface
    private interface AutorFetcher {
        List<Autor> fetchAutores(Year ano);
    }
}
