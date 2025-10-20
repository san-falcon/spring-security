package com.milerfalcon.app_security.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.Optional;

public class ApiKeyFilter extends OncePerRequestFilter {

    private final String HEADER_KEY = "api_key";
    private final String HEADER_VALUE = "clave_api_key";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        try {
            final var miKey = Optional.ofNullable(request.getHeader(HEADER_KEY))
                    .orElseThrow(() -> new BadCredentialsException("Not header api_key"));
            if (!miKey.equals(HEADER_VALUE)) {
                throw new BadCredentialsException("Invalid credentials - No son iguales");
            }
        } catch (Exception exception) {
            throw new BadCredentialsException("Invalid credentials - Grave");
        }

        filterChain.doFilter(request, response);
    }
}
