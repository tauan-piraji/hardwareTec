package com.tauanoliveira.softwaretec.config;

import java.util.Arrays;

import com.tauanoliveira.softwaretec.security.JWTAuthenticationFilter;
import com.tauanoliveira.softwaretec.security.JWTAuthorizationFilter;
import com.tauanoliveira.softwaretec.security.JWTUtil;
import com.tauanoliveira.softwaretec.service.UserDetailsServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{
    
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private Environment env;

    @Autowired
    private JWTUtil jwtUtil;
    //define acesso ao endpoint
    private static final String[] PUBLIC_MATCHERS = {
        "/h2-console/**",
        // "/ordemServicos/**",
        // "/funcionarios/**",
        // "/clientes/**"
    };

    private static final String[] PUBLIC_MATCHERS_GET = {
        "/ordemServicos/**"
        // "/funcionarios/**",
        // "/clientes/**"
    };

    private static final String[] PUBLIC_MATCHERS_POST = {
        // "/funcionarios/**",
        // "/ordemServicos/**",
        // "/picture/equipamento/**",
        "/auth/forgot"
    };

    private static final String[] PUBLIC_MATCHERS_PUT = {
        "/ordemServicos/**/itemAprovada/**",
        "/ordemServicos/**/itemReprovada/**"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        if(Arrays.asList(env.getActiveProfiles()).contains("test")) {
            http.headers().frameOptions().disable();
        }

        http.cors().and().csrf().disable();
        http.authorizeRequests()
            .antMatchers(PUBLIC_MATCHERS).permitAll()
            .antMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).permitAll()
            .antMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll()
            .antMatchers(HttpMethod.PUT, PUBLIC_MATCHERS_PUT).permitAll()
            .anyRequest().authenticated();
        http.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtUtil));
        http.addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtUtil, userDetailsServiceImpl));
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceImpl).passwordEncoder(bCryptPasswordEncoder());
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration().applyPermitDefaultValues();//passa os valores de authentication padrão do cors
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));//Passa lista de metodos HTTP que serão permitidos no config
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();//Cria obj source do tipo UrlBasedCorsConfigurationSource e instancia ele "new"
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}