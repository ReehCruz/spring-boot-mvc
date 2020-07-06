package com.rebeca.springboot;

//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EntityScan(basePackages = "com.rebeca.springboot.model")
@ComponentScan(basePackages = "com.rebeca.*")
@EnableJpaRepositories(basePackages = "com.rebeca.springboot.repository")
@EnableTransactionManagement
public class SpringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootApplication.class, args);

		
//		Quando quiser cripto na pag
//		BCryptPasswordEncoder enconder = new BCryptPasswordEncoder();
//		String result = enconder.encode("root");
//		System.out.println(result);
	}

}
