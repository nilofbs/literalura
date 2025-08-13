package com.alura.literalura.principal;

import com.alura.literalura.dto.AutorDTO;
import com.alura.literalura.dto.LivroDTO;
import com.alura.literalura.dto.ResultadoBuscaDTO;
import com.alura.literalura.model.Autor;
import com.alura.literalura.model.Idioma;
import com.alura.literalura.model.Livro;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LivroRepository;
import com.alura.literalura.service.ConsumoApi;
import com.alura.literalura.service.ConverteDados;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    // --- Atributos para interação e serviços ---
    private final Scanner leitura = new Scanner(System.in);
    private final ConsumoApi consumoApi = new ConsumoApi();
    private final ConverteDados conversor = new ConverteDados();
    private final String ENDERECO_BASE = "https://gutendex.com/books/?search=";

    // --- Repositórios para acesso ao banco de dados ---
    private final LivroRepository livroRepositorio;
    private final AutorRepository autorRepositorio;

    // --- Construtor que recebe os repositórios ---
    public Principal(LivroRepository livroRepositorio, AutorRepository autorRepositorio) {
        this.livroRepositorio = livroRepositorio;
        this.autorRepositorio = autorRepositorio;
    }

    // --- Método principal que exibe o menu e processa as opções ---
    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    \n-----------------------------------------
                    Escolha o número de sua opção:
                    1- Buscar livro pelo título
                    2- Listar livros registrados
                    3- Listar autores registrados
                    4- Listar autores vivos em um determinado ano
                    5- Listar livros em um determinado idioma
                    0- Sair
                    -----------------------------------------
                    """;

            System.out.println(menu);
            try {
                opcao = leitura.nextInt();
                leitura.nextLine();

                switch (opcao) {
                    case 1:
                        buscarLivroWeb();
                        break;
                    case 2:
                        listarLivrosRegistrados();
                        break;
                    case 3:
                        listarAutoresRegistrados();
                        break;
                    case 4:
                        listarAutoresVivos();
                        break;
                    case 5:
                        listarLivrosPorIdioma();
                        break;
                    case 0:
                        System.out.println("Encerrando a aplicação...");
                        break;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Erro: Por favor, digite um número válido para a opção.");
                leitura.nextLine();
            }
        }
    }

    // --- Implementação de cada opção do menu ---

    private void buscarLivroWeb() {
        System.out.println("Insira o nome do livro que você deseja procurar:");
        var nomeLivro = leitura.nextLine();
        var json = consumoApi.obterDados(ENDERECO_BASE + nomeLivro.replace(" ", "%20"));
        ResultadoBuscaDTO dadosBusca = conversor.obterDados(json, ResultadoBuscaDTO.class);

        Optional<LivroDTO> livroBuscado = dadosBusca.resultados().stream().findFirst();

        if (livroBuscado.isPresent()) {
            LivroDTO livroDTO = livroBuscado.get();

            // Tratamento para caso não haja autor
            Autor autor;
            if (livroDTO.autores() != null && !livroDTO.autores().isEmpty()) {
                AutorDTO autorDTO = livroDTO.autores().get(0);
                autor = autorRepositorio.findByNomeIgnoreCase(autorDTO.name())
                        .orElseGet(() -> autorRepositorio.save(new Autor(autorDTO)));
            } else {
                autor = null;
            }

            Livro livro = new Livro(livroDTO);
            livro.setAutor(autor);
            try {
                livroRepositorio.save(livro);
                System.out.println("Livro salvo com sucesso!");
                System.out.println(livro);
            } catch (Exception e) {
                System.out.println("Erro: Livro já cadastrado no banco de dados.");
            }
        } else {
            System.out.println("Livro não encontrado na API!");
        }
    }

    private void listarLivrosRegistrados() {
        List<Livro> livros = livroRepositorio.findAll();
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro registrado.");
        } else {
            System.out.println("\n--- Livros Registrados ---");
            livros.forEach(System.out::println);
        }
    }

    private void listarAutoresRegistrados() {
        List<Autor> autores = autorRepositorio.findAll();
        if (autores.isEmpty()) {
            System.out.println("Nenhum autor registrado.");
        } else {
            System.out.println("\n--- Autores Registrados ---");
            autores.forEach(System.out::println);
        }
    }

    private void listarAutoresVivos() {
        System.out.println("Insira o ano para pesquisar os autores vivos:");
        try {
            var ano = leitura.nextInt();
            leitura.nextLine();
            List<Autor> autoresVivos = autorRepositorio.findAutoresVivosNoAno(ano);
            if (autoresVivos.isEmpty()) {
                System.out.println("Nenhum autor vivo encontrado no ano de " + ano);
            } else {
                System.out.println("\n--- Autores vivos em " + ano + " ---");
                autoresVivos.forEach(System.out::println);
            }
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Por favor, digite um ano no formato numérico (ex: 1888).");
            leitura.nextLine();
        }
    }

    private void listarLivrosPorIdioma() {
        String menuIdiomas = """
                \nInsira o idioma para a busca:
                es - espanhol
                en - inglês
                fr - francês
                pt - português
                """;
        System.out.println(menuIdiomas);
        String codigoIdioma = leitura.nextLine();

        try {
            Idioma idioma = Idioma.fromString(codigoIdioma);
            List<Livro> livrosPorIdioma = livroRepositorio.findByIdioma(idioma);
            if (livrosPorIdioma.isEmpty()) {
                System.out.println("Não existem livros nesse idioma no banco de dados.");
            } else {
                System.out.println("\n--- Livros no idioma '" + idioma + "' ---");
                livrosPorIdioma.forEach(System.out::println);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Idioma inválido. Por favor, escolha uma das opções fornecidas.");
        }
    }
}