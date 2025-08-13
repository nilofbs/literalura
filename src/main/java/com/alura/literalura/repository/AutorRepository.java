package com.alura.literalura.repository;

import com.alura.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository; // <-- ADICIONE AQUI TAMBÉM
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    // Busca autores vivos em um determinado ano
    @Query("SELECT a FROM Autor a WHERE a.anoDeNascimento <= :ano AND (a.anoDeFalecimento IS NULL OR a.anoDeFalecimento >= :ano)")
    List<Autor> findAutoresVivosNoAno(Integer ano);

    // Opcional: para evitar duplicatas ao salvar
    Optional<Autor> findByNomeIgnoreCase(String nome);
}