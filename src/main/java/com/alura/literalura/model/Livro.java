package com.alura.literalura.model;

import com.alura.literalura.dto.LivroDTO;
import jakarta.persistence.*;

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

    @Enumerated(EnumType.STRING)
    private Idioma idioma;

    private Integer numeroDeDownloads;

    // Construtor padrão obrigatório para o JPA/Hibernate
    public Livro() {}

    // Construtor de conveniência para criar um Livro a partir de um LivroDTO
    public Livro(LivroDTO dadosLivro) {
        this.titulo = dadosLivro.titulo();
        // A API pode retornar mais de um idioma, pegamos o primeiro
        if (dadosLivro.idiomas() != null && !dadosLivro.idiomas().isEmpty()) {
            this.idioma = Idioma.fromString(dadosLivro.idiomas().get(0).trim());
        }
        this.numeroDeDownloads = dadosLivro.numeroDeDownloads();
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Idioma getIdioma() {
        return idioma;
    }

    public void setIdioma(Idioma idioma) {
        this.idioma = idioma;
    }

    public Integer getNumeroDeDownloads() {
        return numeroDeDownloads;
    }

    public void setNumeroDeDownloads(Integer numeroDeDownloads) {
        this.numeroDeDownloads = numeroDeDownloads;
    }

    @Override
    public String toString() {
        String nomeAutor = (autor != null) ? autor.getNome() : "Autor desconhecido";
        return "----- LIVRO -----\n" +
                " Título: " + titulo + "\n" +
                " Autor: " + nomeAutor + "\n" +
                " Idioma: " + idioma + "\n" +
                " Número de downloads: " + numeroDeDownloads + "\n" +
                "-----------------\n";
    }
}