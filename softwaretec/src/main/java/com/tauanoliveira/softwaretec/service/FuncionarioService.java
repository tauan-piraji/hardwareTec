package com.tauanoliveira.softwaretec.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.tauanoliveira.softwaretec.domain.Funcionario;
import com.tauanoliveira.softwaretec.domain.enums.Perfil;
import com.tauanoliveira.softwaretec.dto.FuncionarioDTO;
import com.tauanoliveira.softwaretec.repository.FuncionarioRepository;
import com.tauanoliveira.softwaretec.security.UserSS;
import com.tauanoliveira.softwaretec.service.exception.AuthorizationException;
import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class FuncionarioService {
 
    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public List<Funcionario> findAll() {
        return funcionarioRepository.findAll();
    }

    public Funcionario findById(Integer id) {
        UserSS user = UserService.authenticated();//usuario logado
		if(user == null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso negado");
		}
        
        Optional<Funcionario> entity = funcionarioRepository.findById(id);
        return entity.orElseThrow(() -> new EntityNotFoundException(
            "Funcionario não encontrado! id; " + id + ", Tipo: " + Funcionario.class.getName()
        ));
    }

    public Funcionario insert(Funcionario entity) {
        entity.setSenha(bCryptPasswordEncoder.encode(entity.getSenha()));
        return funcionarioRepository.save(entity);
    }

    public Funcionario update(Integer id, FuncionarioDTO entityDTO) {
        Funcionario funcionario = this.fromDTO(entityDTO, id);
        return funcionarioRepository.save(funcionario);
    }

    public Funcionario fromDTO(FuncionarioDTO entityDTO, Integer id) {
        Funcionario entity = this.findById(id);
        entity.setSenha(bCryptPasswordEncoder.encode(entityDTO.getSenha()));
        return entity;
    }

    public void delete(Integer id) {
        funcionarioRepository.deleteById(id);
    }
}