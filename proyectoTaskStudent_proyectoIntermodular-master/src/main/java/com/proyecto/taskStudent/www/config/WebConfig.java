package com.proyecto.taskStudent.www.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new SesionInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/",
                        "/mostrarFormularioRegistro",
                        "/registrar",
                        "/iniciar",
                        "/logout",
                        "/mostrarFormularioInicio",
                        "/css/**",
                        "/js/**",
                        "/img/**"
                );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
