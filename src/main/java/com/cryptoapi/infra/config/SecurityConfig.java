package com.cryptoapi.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import com.cryptoapi.infra.service.AuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector)
			throws Exception {
		MvcRequestMatcher h2RequestMatcher = new MvcRequestMatcher(introspector, "/**");
		h2RequestMatcher.setServletPath("/h2-ui");
		http.csrf(AbstractHttpConfigurer::disable);
		http.csrf().disable();
		http.headers().frameOptions().disable();
		http.authorizeHttpRequests(
				authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
						.requestMatchers("/h2-ui/**").permitAll().requestMatchers("/**").authenticated())
				.httpBasic(Customizer.withDefaults())
				.sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(new AuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

}