package br.com.alura.literalura.main;

import br.com.alura.literalura.model.*;
//import br.com.alura.literalura.repository.AutorRepository;
//import br.com.alura.literalura.repository.LivroRepository;
import br.com.alura.literalura.repository.AutorRepository;
import br.com.alura.literalura.repository.LivroRepository;
import br.com.alura.literalura.services.ConsumoApi;
import br.com.alura.literalura.services.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private final String ENDERECO = "https://gutendex.com/books?search=";
    private ConsumoApi consumo = new ConsumoApi();
    private Scanner leitura = new Scanner(System.in);
    private ConverteDados conversor = new ConverteDados();
    private List<DadosLivro> livros = new ArrayList<>();
    private LivroRepository livroRepository;
    private AutorRepository autorRepository;


    public Main(LivroRepository livroRepository, AutorRepository autorRepository) {
        this.livroRepository = livroRepository;
        this.autorRepository=autorRepository;
    }


    public void exibirMenu(){
        int opcao = 0;
        var menu = """
                1 - Buscar Livro
                2 - Listar livros registrados
                3 - Listar autores registrados
                4 - Listar autores vivos em um determinado ano
                5 - Listar livros em um determinado idioma
                6 - Listar os livros mais baixados na web
                
                9 - Sair
                """;
        while(opcao!=9) {
            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();
            switch (opcao){
                case 1:
                    buscarLivro();
                    break;
                case 2:
                    listarLivros();
                    break;
                case 3:
                    listarAutores();
                    break;
                case 4:
                    listarAutoresVivos();
                    break;
                case 5:
                    listarLivrosEmDeterminadoIdioma();
                    break;
                case 6:
                    listarLivrosMaisBaixados();
                    break;
                case 9:
                    break;
            }
        }
    }

    private void listarLivrosMaisBaixados() {
        List<Livro> livros = livroRepository.findAll();
        List<Livro> livrosOrdenados = livros.stream()
                .sorted(Comparator.comparingInt(Livro::getDownloads).reversed())
                .collect(Collectors.toList());
        System.out.println("Livros mais baixados");
        if(livrosOrdenados.isEmpty()) {
            System.out.println("Nenhum livro encontrado. Voce adicionou livros no banco?");
        }else {
            for(Livro l : livrosOrdenados) {
                System.out.printf("Titulo: %s | Downloads: %d%n",l.getTitulo(),l.getDownloads());
            }
        }
    }

    private void listarLivrosEmDeterminadoIdioma() {
        String opcoes= """
                Insira o idioma desejado:
                es - Espanhol
                en - Ingles
                fr - Frances
                pt - Portugues
                """;
        System.out.println(opcoes);
        String idiomaInserido = leitura.nextLine().trim();
        List<Livro> livros = livroRepository.findAll();
        List<Livro> livrosNoIdioma = livros.stream()
                .filter(l -> l.getIdiomas().contains(idiomaInserido))
                .collect(Collectors.toList());
        if(livrosNoIdioma.isEmpty()){
            System.out.println("Nenhum livro encontrado no idioma: "+idiomaInserido);
        }else{
            System.out.println("Livros no idioma: "+idiomaInserido);
            livrosNoIdioma.forEach(l -> System.out.println(l.getTitulo()));
        }

    }


    private void listarAutoresVivos() {
        System.out.println("Insira o ano em que procura os autores:");
        int anoInserido = leitura.nextInt();
        List<Autor> autores = autorRepository.findAll();
        List<Autor> autoresVivos = autores.stream()
                .filter(a -> (a.getAnoNascimento() <= anoInserido) && (a.getAnoMorte() == null || a.getAnoMorte() >= anoInserido))
                .collect(Collectors.toList());
        if(autoresVivos.isEmpty()) {
            System.out.println("Nenhum autor encontrado no banco que esteja vivo em "+anoInserido);
        }else{
            System.out.println("Atores vivos em "+anoInserido+":");
            autoresVivos.forEach(a -> System.out.println(a.getNome()));
        }

    }

    private void listarLivros() {
        List<Livro> livrosNoBanco = livroRepository.findAll();
        livrosNoBanco.forEach(System.out::println);
    }

    private void listarAutores(){
        List<Autor> autoresNoBanco = autorRepository.findAll();
        autoresNoBanco.forEach(System.out::println);
    }

    private void buscarLivro() {
        DadosLivro dadosLivro = getDadosLivro();
        System.out.println(dadosLivro);

        DadosAutor dadosAutor = dadosLivro.autor().get(0);
        Optional<Autor> optionalAutor = autorRepository.findByNome(dadosAutor.nome());
        Autor autor;
        if (optionalAutor.isPresent()) {
            autor = optionalAutor.get();
            System.out.println("Autor " + autor.getNome() + " ja existe no banco de dados.");
        } else {
            autor = new Autor(dadosAutor);
            autorRepository.save(autor);
            System.out.println("Autor " + autor.getNome() + " salvo no banco de dados.");
        }
        Livro livro = new Livro(dadosLivro);
        livro.setAutor(autor);
        livroRepository.save(livro);
        System.out.println("Livro " + livro.getTitulo() + " salvo no banco de dados.");
    }

    public DadosLivro getDadosLivro(){
        System.out.println("Insira o livro desejado para busca:");
        String livroInserido = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + livroInserido.replace(" ","+"));
        RespostaLivros respostaLivros = conversor.obterDados(json,RespostaLivros.class);
        if (respostaLivros.resultados() != null && !respostaLivros.resultados().isEmpty()) {
            DadosLivro dadosLivro = respostaLivros.resultados().get(0); //o indice 0 no caso seria os dados dos livros, por conta da resposta contendo listas da API
            return dadosLivro;
        } else {
            System.out.println("Nenhum livro encontrado.");
            return null;
        }
    }
}
