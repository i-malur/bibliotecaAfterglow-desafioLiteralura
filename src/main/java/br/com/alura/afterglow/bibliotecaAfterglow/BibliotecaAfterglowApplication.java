package br.com.alura.afterglow.bibliotecaAfterglow;

import br.com.alura.afterglow.bibliotecaAfterglow.principal.Principal;
import br.com.alura.afterglow.bibliotecaAfterglow.repository.LivroRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BibliotecaAfterglowApplication implements CommandLineRunner {

	@Autowired
	private Principal principal;

	public static void main(String[] args) {

		SpringApplication.run(BibliotecaAfterglowApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {
		principal.executar();
	}
}
