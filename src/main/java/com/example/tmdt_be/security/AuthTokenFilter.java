package com.example.tmdt_be.security;

import com.example.tmdt_be.repository.UserRepo;
import com.example.tmdt_be.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

public class AuthTokenFilter extends OncePerRequestFilter {
    private static final String header = "Authorization";

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepo userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        getTokenString(request.getHeader(header)).ifPresent(token -> {
            tokenService.getSubFromToken(token).ifPresent(id -> {
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    userRepository.findById(id).ifPresent(user -> {
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                user,
                                null,
                                Collections.emptyList()
                        );
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    });
                }
            });
        });
        filterChain.doFilter(request, response);
    }

    public static Optional<String> getTokenString(String header) {
        if (header == null) {
            return Optional.empty();
        } else {
            String[] split = header.split(" ");
            if (split.length < 2) {
                return Optional.empty();
            } else {
                return Optional.ofNullable(split[1]);
            }
        }
    }
}
