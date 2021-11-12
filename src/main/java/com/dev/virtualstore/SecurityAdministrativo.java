package com.dev.virtualstore;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
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
@Order(2)
public class SecurityAdministrativo extends WebSecurityConfigurerAdapter {
	@Autowired
	private DataSource dataSource;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication()
			.dataSource(this.dataSource)
			.usersByUsernameQuery("SELECT email AS username, senha AS password, 1 AS enable FROM funcionario WHERE email = ?")
			.authoritiesByUsernameQuery("SELECT f.email AS username, pa.nome AS authority FROM permissoes AS p INNER JOIN funcionario AS f ON f.id = p.funcionario_id INNER JOIN papel AS pa ON pa.id = p.papel_id WHERE f.email = ?")
			.passwordEncoder(new BCryptPasswordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("login/**")
			.permitAll()
			.antMatchers("/administrativo/cadastrar/**")
			.hasAuthority("gerente")
			.antMatchers("/administrativo/**")
			.authenticated()
			.and()
			.formLogin()
			.loginPage("/login")
			.failureUrl("/login")
			.loginProcessingUrl("/admin")
			.defaultSuccessUrl("/administrativo")
			.usernameParameter("username")
			.passwordParameter("password")
			.and()
			.logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/administrativo/logout"))
			.logoutSuccessUrl("/login")
			.deleteCookies("JSESSIONID")
			.and()
			.exceptionHandling()
			.accessDeniedPage("/negadoAdministrativo")
			.and()
			.csrf()
			.disable();
	}
}
