package com.dev.virtualstore;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class BasicConfiguration extends WebSecurityConfigurerAdapter {
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
			.usersByUsernameQuery("SELECT email AS username, senha AS password, 1 AS enable FROM funcionario WHERE email = ?")
			.authoritiesByUsernameQuery("SELECT f.email AS username, pa.nome AS authority FROM permissoes AS p INNER JOIN funcionario AS f ON f.id = p.funcionario_id INNER JOIN papel AS pa ON pa.id = p.papel_id WHERE f.email = ?")
			.passwordEncoder(this.passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf()
			.disable()
			.authorizeRequests()
			.antMatchers("/administrativo/entrada/**")
			.hasAnyRole("gerente")
			.and()
			.formLogin()
			.loginPage("/login")
			.permitAll()
			.and()
			.logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			.logoutSuccessUrl("/administrativo")
			.and()
			.exceptionHandling()
			.accessDeniedPage("/negado");
	}
}
