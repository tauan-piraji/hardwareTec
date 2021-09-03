package com.tauanoliveira.softwaretec.service;

import java.util.Collection;
import java.util.Collections;

import com.tauanoliveira.softwaretec.domain.Funcionario;
import com.tauanoliveira.softwaretec.domain.enums.Perfil;
import com.tauanoliveira.softwaretec.repository.FuncionarioRepository;
import com.tauanoliveira.softwaretec.security.UserSS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Funcionario F = funcionarioRepository.findByEmail(email);
        if(F == null){
            throw new UsernameNotFoundException(email); 
        }
        Collection<Perfil> perfil = Collections.singleton(F.getPerfil());
        return new UserSS(F.getId(), F.getEmail(), F.getSenha(), perfil);
    }   
}