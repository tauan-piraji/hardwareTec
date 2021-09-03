package com.tauanoliveira.softwaretec.repository;

import com.tauanoliveira.softwaretec.domain.Funcionario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Integer>{
    
    @Transactional(readOnly = true)
    Funcionario findByEmail(String email);
}