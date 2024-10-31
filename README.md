# Literalura
Repositorio para a entrega do Challenge de Literatura do programa ONE (Alura + Oracle)
### ONE
- "O ONE é um programa de educação e empregabilidade com objetivo social de capacitar pessoas em tecnologia e conectá-las com o mercado de trabalho por meio de empresas parceiras."
- " O curso é 100% online e totalmente gratuito, feito para quem não teve acesso à educação de qualidade e deseja transformar a sua realidade social."
- Esta formação faz parte do programa ONE, parceria de Alura + Oracle
> Saiba mais em https://www.oracle.com/br/education/oracle-next-education/


## Classes
- Modelos:
> Autor: criada para representar uma entidade no banco de dados relacional <br>
```java
package br.com.alura.literalura.model;

import jakarta.persistence.*;

import java.util.List;
@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nome",nullable = false,length = 100)
    private String nome;
    private Integer anoMorte;
    private Integer anoNascimento;
    @OneToMany(mappedBy = "autor", fetch = FetchType.EAGER)
    private List<Livro> livros;

    public Autor() {
    }

    public Autor(String nome, Integer anoMorte, Integer anoNascimento, List<Livro> livros) {
        this.nome = nome;
        this.anoMorte = anoMorte;
        this.anoNascimento = anoNascimento;
        this.livros = livros;
    }

    public Autor(DadosAutor dadosAutor) {
        this.nome= dadosAutor.nome();
        this.anoMorte= dadosAutor.anoMorte();
        this.anoNascimento= dadosAutor.anoNascimento();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getAnoMorte() {
        return anoMorte;
    }

    public void setAnoMorte(Integer anoMorte) {
        this.anoMorte = anoMorte;
    }

    public Integer getAnoNascimento() {
        return anoNascimento;
    }

    public void setAnoNascimento(Integer anoNascimento) {
        this.anoNascimento = anoNascimento;
    }

    @Override
    public String toString() {
        return "Autor:" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", anoMorte=" + anoMorte +
                ", anoNascimento=" + anoNascimento +
                ", livros=" + livros.toString();
    }
}

```
> Livro: outra classe representando uma entidade no banco de dados relacional <br>
```java
package br.com.alura.literalura.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "livros")
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    @ManyToOne
    @JoinColumn(name = "autor_id")
    private Autor autor;
    private List<String> idiomas;
    @Column(name = "download_count")
    private Integer downloads;

    public Livro(DadosLivro dadosLivro) {
        this.titulo= dadosLivro.titulo();
        this.downloads = dadosLivro.downloads();
        this.idiomas=dadosLivro.idioma();
    }

    @Override
    public String toString() {
        return "Livro: " +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", autor=" + autor.getNome() +
                ", idiomas=" + idiomas +
                ", downloads=" + downloads;
    }

    public Livro(){

    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public List<String> getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(List<String> idiomas) {
        this.idiomas = idiomas;
    }

    public Integer getDownloads() {
        return downloads;
    }

    public void setDownloads(Integer downloads) {
        this.downloads = downloads;
    }
}

```
> Records para conversao de JSON vindos da API  para objetos : <br>
```java
package br.com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosAutor(@JsonProperty("name") String nome,
                         @JsonProperty("birth_year") Integer anoNascimento,
                         @JsonProperty("death_year") Integer anoMorte) {
}

```
```java
package br.com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosLivro(@JsonProperty("title") String titulo,
                         @JsonProperty("authors") List<DadosAutor> autor,
                         @JsonProperty("languages") List<String> idioma,
                         @JsonProperty("download_count") Integer downloads) {
}

```
```java
package br.com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RespostaLivros(
        @JsonProperty("count") Integer count,
        @JsonProperty("next") String next,
        @JsonProperty("previous") String previous,
        @JsonProperty("results") List<DadosLivro> resultados
) {}
```

- Repositorios: utilizados para realizar as queries no banco de dados relacional
```java
package br.com.alura.literalura.repository;

import br.com.alura.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {
    Optional<Autor> findByNome(String nome);

    List<Autor> findAll();
}
```
```java
package br.com.alura.literalura.repository;

import br.com.alura.literalura.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LivroRepository extends JpaRepository<Livro,Long> {

    List<Livro> findAll();
}

```

- Classe Main:
```java
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

```

- Classe da aplicacao gerenciado pelo Spring com suas injecoes de dependencias:
```java
package br.com.alura.literalura;

import br.com.alura.literalura.main.Main;
//import br.com.alura.literalura.repository.AutorRepository;
//import br.com.alura.literalura.repository.LivroRepository;
import br.com.alura.literalura.repository.AutorRepository;
import br.com.alura.literalura.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {
	@Autowired
	private LivroRepository livroRepository;
	@Autowired
	private AutorRepository autorRepository;

	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Main main = new Main(livroRepository,autorRepository);

		main.exibirMenu();
	}
}

```
