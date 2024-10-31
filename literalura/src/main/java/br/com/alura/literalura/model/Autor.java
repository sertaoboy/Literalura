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