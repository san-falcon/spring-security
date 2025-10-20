package com.milerfalcon.app_security.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;
import java.util.List;

@Configuration
//@EnableMethodSecurity
public class SecurityConfiguration {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        //  auth.requestMatchers("/loans", "/balance", "/cards", "/account").authenticated()

        // Agregamos un filtro antes que se pase por BasicAuthenticationFilter
        httpSecurity.addFilterBefore(new ApiKeyFilter(), BasicAuthenticationFilter.class);

        var requestHandler = new CsrfTokenRequestAttributeHandler();
        requestHandler.setCsrfRequestAttributeName("_csrf");


        httpSecurity
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/loans", "/balance").hasRole("USER")
                        .requestMatchers("/cards", "/account").hasRole("ADMIN")
                        .anyRequest().permitAll()
                )
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults());

        httpSecurity
                .cors(cors -> corsConfigurationSource());

        httpSecurity
                .csrf(csrf -> csrf
                        .csrfTokenRequestHandler(requestHandler)
                        .ignoringRequestMatchers("/welcome", "/about")
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class);

        return httpSecurity.build();
    }

    /*@Bean
    InMemoryUserDetailsManager userDetailsManager() {
        UserDetails admin = User.withUsername("admin")
                .password("admin")
                .authorities("admin")
                .build();
        UserDetails vendedor = User.withUsername("vendedor")
                .password("vendedor")
                .authorities("vendedor")
                .build();
        return new InMemoryUserDetailsManager(admin, vendedor);
    }*/

   /* @Bean
    UserDetailsService userDetailsService(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }*/

    @Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        var config = new CorsConfiguration();
        // config.setAllowedOrigins(List.of("http://localhost:4200", "https://my-app-front-end.com"));
        //? Con esto permitimos que todos los dominios puedan consumir nuestra API REST
        config.setAllowedOrigins(List.of("*"));
        // config.setAllowedMethods(List.of("POST", "GET", "PUT", "DELETE"));
        config.setAllowedMethods(List.of("*"));

        config.setAllowedHeaders(List.of("*"));

        var source = new UrlBasedCorsConfigurationSource();
        //? El primer parametro es para saber que ruta quiero proteger
        source.registerCorsConfiguration("/**", config);

        return source;
    }



    /*
        ? Aca le estamos diciendo a Spring Security:
        ? “Usa este algoritmo (BCrypt) para comparar las contraseñas de usuarios.”
    */
    /*@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }*/

}
