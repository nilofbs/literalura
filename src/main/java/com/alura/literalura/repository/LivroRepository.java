package com.alura.literalura.repository;

import com.alura.literalura.model.Idioma;
import com.alura.literalura.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository; // <-- ADICIONE ESTA LINHA
import java.util.List;

public interface LivroRepository extends JpaRepository<Livro, Long> {
    List<Livro> findByIdioma(Idioma idioma);
}