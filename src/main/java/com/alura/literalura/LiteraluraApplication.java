package com.alura.literalura;

import com.alura.literalura.principal.Principal;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

	@Autowired // O Spring injeta a implementação do repositório aqui
	private LivroRepository livroRepositorio;

	@Autowired // O Spring injeta a implementação do repositório aqui
	private AutorRepository autorRepositorio;

	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Cria uma instância da classe Principal, passando os repositórios
		Principal principal = new Principal(livroRepositorio, autorRepositorio);

		// Chama o método que exibe o menu
		principal.exibeMenu();
	}
}