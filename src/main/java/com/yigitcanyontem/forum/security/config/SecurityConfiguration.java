package com.yigitcanyontem.forum.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static com.yigitcanyontem.forum.entity.enums.Role.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

  private final JwtAuthenticationFilter jwtAuthFilter;
  private final AuthenticationProvider authenticationProvider;
  private final LogoutHandler logoutHandler;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf()
        .disable()
            .anonymous().disable()
            .cors((cors) ->{
                CorsConfiguration config = new CorsConfiguration();
                config.addAllowedOrigin("http://localhost:4200");
                config.addAllowedOrigin("http://192.168.0.19:4200/");
                config.addAllowedMethod("GET");
                config.addAllowedMethod("POST");
                config.addAllowedMethod("PUT");
                config.addAllowedMethod("DELETE");
                config.addAllowedMethod("PATCH");
                config.addAllowedHeader("Authorization");
                config.addAllowedHeader("Content-Type");

                cors.configurationSource(request -> config);
            })
        .authorizeHttpRequests()
        .requestMatchers(
                "/api/v1/auth/**",
                "/api/v1/entertainment/**",
                "/api/v1/review/**",
                "/api/v1/search/**"
        )
        .permitAll()
        .requestMatchers(
                HttpMethod.GET,
                "/api/v1/user/**"
        ).permitAll()
        .requestMatchers(
                HttpMethod.PATCH,
                "/api/v1/user/**"
        ).hasAnyRole(ADMIN.name(), USER.name())
        .requestMatchers(
                HttpMethod.POST,
                "/api/v1/user/**"
        ).hasAnyRole(ADMIN.name(), USER.name())
        .requestMatchers(
                HttpMethod.PUT,
                "/api/v1/user/**"
        ).hasAnyRole(ADMIN.name(), USER.name())
        .requestMatchers(
                HttpMethod.DELETE,
                "/api/v1/user/**"
        ).hasAnyRole(ADMIN.name(), USER.name())
        .anyRequest()
          .authenticated()
        .and()
          .sessionManagement()
          .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
         .and()
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .logout()
        .logoutUrl("/api/v1/auth/logout")
        .addLogoutHandler(logoutHandler)
        .permitAll()
        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
    ;

    return http.build();
  }

}
