package com.tauanoliveira.softwaretec.repository;

import java.util.List;

import com.tauanoliveira.softwaretec.domain.Equipamento;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface EquipamentoRepository extends JpaRepository<Equipamento, Integer> {
    
	@Transactional(readOnly=true)
	public List<Equipamento> findAllByOrderByNome();
}