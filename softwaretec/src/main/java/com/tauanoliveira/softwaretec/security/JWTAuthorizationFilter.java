package com.tauanoliveira.softwaretec.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tauanoliveira.softwaretec.service.UserDetailsServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, UserDetailsServiceImpl userDetailsServiceImpl) {
        super(authenticationManager);
        this.jwtUtil = jwtUtil;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }
    
    //Pega token e confirma authorization
    @Override
    protected void doFilterInternal(HttpServletRequest request,//pega a requisição http
                                    HttpServletResponse response,//Resposta
                                    FilterChain chain)throws IOException, ServletException{
        //Processo de authorization do endpoint(token)
        String header = request.getHeader("Authorization");
        if(header != null && header.startsWith("Bearer ")){
            UsernamePasswordAuthenticationToken auth = getAuthentication(header.substring(7));
            if(auth != null){
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }  
        chain.doFilter(request, response); 
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String token) {
        if(jwtUtil.tokenValido(token)) {//valida token
            String username = jwtUtil.getUsername(token);//pega usuario  partir do token
            UserDetails user = userDetailsServiceImpl.loadUserByUsername(username);
            return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        }
        return null;
    }
}