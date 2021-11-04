package com.dev.virtualstore;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@Order(1)
public class SecurityCliente extends WebSecurityConfigurerAdapter {
	@Autowired
	private DataSource dataSource;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// auth.inMemoryAuthentication()
		// 	.withUser("user")
		// 	.password(this.passwordEncoder().encode("123"))
		// 	.roles("USER")
		// 	.and()
		// 	.withUser("admin")
		// 	.password(this.passwordEncoder().encode("admin"))
		// 	.roles("USER", "ADMIN");

		auth.jdbcAuthentication()
			.dataSource(this.dataSource)
			.usersByUsernameQuery("SELECT email AS username, senha AS password, 1 AS enable FROM cliente WHERE email = ?")
			.authoritiesByUsernameQuery("SELECT email AS username, 'cliente' AS authority FROM cliente WHERE email = ?")
			.passwordEncoder(this.passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.antMatcher("/finalizar/**")
			.authorizeRequests()
			.anyRequest()
			.hasAnyAuthority("cliente")
			.and()
			.csrf()
			.disable()
			.formLogin()
			.loginPage("/cliente/cadastrar")
			.permitAll()
			.failureUrl("/cliente/cadastrar")
			.loginProcessingUrl("/finalizar/login")
			.defaultSuccessUrl("/finalizar")
			.usernameParameter("username")
			.passwordParameter("password")
			.and()
			.logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/finalizar/logout"))
			.logoutSuccessUrl("/")
			.permitAll()
			.and()
			.exceptionHandling()
			.accessDeniedPage("/negadoCliente");
	}
}
