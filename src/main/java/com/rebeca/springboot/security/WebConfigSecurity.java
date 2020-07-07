package com.rebeca.springboot.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebConfigSecurity extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private ImplementacaoUserDetailsService implementacaoUserDetailsService;
	
	@Override //configura as solicitações de acesso por http 
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf()
		.disable() //desativa as configurações padrão de memória
		.authorizeRequests() //permiti restringir acessos 
		.antMatchers(HttpMethod.GET, "/").permitAll() //qualqur usuario acessa a pagina inicial 
		.anyRequest().authenticated() 
		.and().formLogin().permitAll() // permite qlqr usuario
		.and().logout() // Mapeia URL de logout e invalida usuario autentificado 
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
		
	}
	
	
	@Override //cria atentificaçao do usuario com banco ou em memória 
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(implementacaoUserDetailsService)
		.passwordEncoder(new BCryptPasswordEncoder());
		} 
	
	@Override //ignora URL especificas
	public void configure(WebSecurity web) throws Exception {
	//	web.ignoring().antMatchers("/materialize/**");
	}
}
