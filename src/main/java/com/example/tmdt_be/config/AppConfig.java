package com.example.tmdt_be.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import me.coong.web.advice.WebFilterChainProxyAdvice;
import me.coong.web.filter.WebTracingFilter;

import java.util.Arrays;

@Configuration
@EnableJpaAuditing
@EnableAspectJAutoProxy
@EnableTransactionManagement(proxyTargetClass = true)
public class AppConfig {

    @Bean
    public FilterRegistrationBean<CommonsRequestLoggingFilter> commonsRequestLoggingFilter() {
        FilterRegistrationBean<CommonsRequestLoggingFilter> filterRegBean = new
                FilterRegistrationBean<>();

        CommonsRequestLoggingFilter commonsRequestLoggingFilter = new CommonsRequestLoggingFilter();
        commonsRequestLoggingFilter.setIncludeQueryString(true);
        commonsRequestLoggingFilter.setIncludePayload(true);
        commonsRequestLoggingFilter.setIncludeHeaders(true);
        commonsRequestLoggingFilter.setIncludeClientInfo(true);
        commonsRequestLoggingFilter.setMaxPayloadLength(10240); //
        filterRegBean.setFilter(commonsRequestLoggingFilter);
        filterRegBean.addUrlPatterns("/*"); //
        filterRegBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 9);
        filterRegBean.setOrder(99);

        return filterRegBean;
    }

    @Bean
    public FilterRegistrationBean<WebTracingFilter> webTracingFilter() {
        FilterRegistrationBean<WebTracingFilter> filterRegBean = new FilterRegistrationBean<>();
        filterRegBean.setFilter(new WebTracingFilter());
        filterRegBean.addUrlPatterns("/*");
        filterRegBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 10);
        filterRegBean.setOrder(98);
        return filterRegBean;
    }

    @Bean
    public WebFilterChainProxyAdvice filterChainProxyAdvice() {
        return new WebFilterChainProxyAdvice();
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

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasenames("message/message_application");
        source.setUseCodeAsDefaultMessage(true);
        source.setDefaultEncoding("UTF-8");
        return source;
    }
}
