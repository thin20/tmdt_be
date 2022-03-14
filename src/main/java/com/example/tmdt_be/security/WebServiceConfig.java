package com.example.tmdt_be.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@ResponseBody
public class WebServiceConfig extends WebSecurityConfigurerAdapter {
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf()
//                .disable()
//                .exceptionHandling()
//                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
//                .and()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authorizeRequests()
//                .antMatchers(HttpMethod.OPTIONS)
//                .permitAll()
//                .antMatchers(HttpMethod.GET, "/articles/feed", "/gs-guide-websocket")
//                .authenticated()
//                .antMatchers(HttpMethod.POST, "/users", "/users/login", "/users/token","/product/create","/api/**","/comment/**")
//                .permitAll()
//                .antMatchers(HttpMethod.GET, "/articles/**", "/profiles/**", "/tags","/hello" ,"/product/{productId}", "/comment/**", "/category/**", "/api/**", "/product/**", "/gs-guide-websocket")
//                .permitAll()
//                .antMatchers(HttpMethod.DELETE, "/product/**", "/api/**", "*")
//                .permitAll()
//                .antMatchers(HttpMethod.PUT, "/product/**", "/api/**")
//                .permitAll()
//                .antMatchers("/gs-guide-websocket/**")
//                .permitAll()
//                .anyRequest()
//                .authenticated();
//
//        http.cors().disable();
//        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);

        http
                .cors()
                .and()
                .csrf()
                .disable()
                .authorizeRequests()
                .anyRequest()
                .permitAll();

        http.addFilterBefore(filter, CsrfFilter.class);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("Access-Control-Allow-Headers", "Access-Control-Allow-Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers", "Origin", "Cache-Control", "Content-Type", "Authorization", "Accept", "X-Requested-With", "Referer", "User-Agent"));
        configuration.setAllowedMethods(Arrays.asList("DELETE", "GET", "POST", "PATCH", "PUT"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
