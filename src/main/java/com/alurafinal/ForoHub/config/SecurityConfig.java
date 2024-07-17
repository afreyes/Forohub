package com.alurafinal.ForoHub.config;

import com.alurafinal.ForoHub.security.JwtAuthenticationEntryPoint;
import com.alurafinal.ForoHub.security.JwtTokenFilter;
import com.alurafinal.ForoHub.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint() {
        return new JwtAuthenticationEntryPoint();
    }

    @Bean
    public JwtTokenFilter jwtTokenFilter() {
        return new JwtTokenFilter(jwtTokenProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()  // Deshabilitar CSRF para usar tokens JWT
                .cors()  // Configurar política CORS
                .and()
                .authorizeRequests()
                .antMatchers("/api/auth/**").permitAll()  // Permitir acceso público a la ruta de autenticación
                .antMatchers(HttpMethod.POST, "/topicos").authenticated()  // Ejemplo de ruta que requiere autenticación
                .anyRequest().authenticated()  // Todas las demás rutas requieren autenticación
                .and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint())  // Manejo de excepciones de autenticación
                .and()
                .addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);  // Filtro JWT antes del filtro de autenticación básica
    }
}
