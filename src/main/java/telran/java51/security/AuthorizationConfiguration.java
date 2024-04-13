package telran.java51.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

import lombok.RequiredArgsConstructor;
import telran.java51.accounting.model.Role;


@Configuration
@RequiredArgsConstructor
public class AuthorizationConfiguration {
		
	@Bean
	public SecurityFilterChain web(HttpSecurity http) throws Exception{
		http.httpBasic(Customizer.withDefaults());
		http.csrf(csrf -> csrf.disable());
		http.sessionManagement(session ->session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));
		http.authorizeHttpRequests(authorize-> authorize
				.requestMatchers("/account/register")
					.permitAll()
				.requestMatchers("/account/user/{email}/role/{role}") 
					.hasRole(Role.ADMINISTRATOR.name())
				.requestMatchers(HttpMethod.PUT, "/account/user/{email}")
					.access(new WebExpressionAuthorizationManager("#email == authentication.name"))
				.requestMatchers(HttpMethod.DELETE, "/account/user/{email}")
					.access(new WebExpressionAuthorizationManager("#email == authentication.name or hasRole('ADMINISTRATOR')"))
		
				.anyRequest()
					.authenticated()
		);
		return http.build();
	}

}
