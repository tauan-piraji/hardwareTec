package com.tauanoliveira.softwaretec.repository;

import com.tauanoliveira.softwaretec.domain.OrdemServico;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdemServicoRepository extends JpaRepository<OrdemServico, Integer>{

}