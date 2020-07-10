package com.rebeca.springboot;

//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.Ordered;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EntityScan(basePackages = "com.rebeca.springboot.model")
@ComponentScan(basePackages = "com.rebeca.*")
@EnableJpaRepositories(basePackages = "com.rebeca.springboot.repository")
@EnableTransactionManagement
@EnableWebMvc
public class SpringbootApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootApplication.class, args);
	}
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/login").setViewName("/login");
		registry.setOrder(Ordered.LOWEST_PRECEDENCE);
		
	}
		
		
//		Quando quiser cripto na pag
//		BCryptPasswordEncoder enconder = new BCryptPasswordEncoder();
//		String result = enconder.encode("teste");
//		System.out.println(result);


}
