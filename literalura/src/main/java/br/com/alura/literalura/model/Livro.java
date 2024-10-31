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
