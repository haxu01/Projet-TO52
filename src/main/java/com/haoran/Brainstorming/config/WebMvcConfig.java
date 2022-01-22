package com.haoran.Brainstorming.config;

import com.haoran.Brainstorming.interceptor.CommonInterceptor;
import com.haoran.Brainstorming.interceptor.UserInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.annotation.Resource;
import java.util.Locale;

@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    @Resource
    private CommonInterceptor commonInterceptor;
    @Resource
    private UserInterceptor userInterceptor;

    @Override
    protected void addCorsMappings(CorsRegistry registry) {
        super.addCorsMappings(registry);
        registry.addMapping("/api/**").allowedHeaders("*").allowedMethods("*").allowedOrigins("*").allowCredentials(false);
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        // Configurez un intercepteur de journal global pour enregistrer les enregistrements de demande d'utilisateur
        registry.addInterceptor(commonInterceptor).addPathPatterns("/**");
        // Intercepteur d'utilisateur, intercepte si l'utilisateur est connecté
        registry.addInterceptor(userInterceptor).addPathPatterns("/settings", "/settings/*", "/topic/create", "/topic/edit/*");
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/", "file:./static/");
    }

    // configure la langue par défaut du site web
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
        sessionLocaleResolver.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
        return sessionLocaleResolver;
    }


}
