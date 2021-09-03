package com.tauanoliveira.softwaretec.service;

import java.util.List;
import java.util.Optional;

import com.tauanoliveira.softwaretec.domain.Cliente;
import com.tauanoliveira.softwaretec.domain.Endereco;
import com.tauanoliveira.softwaretec.domain.enums.TipoCliente;
import com.tauanoliveira.softwaretec.dto.ClienteDTO;
import com.tauanoliveira.softwaretec.dto.ClienteNewDTO;
import com.tauanoliveira.softwaretec.repository.ClienteRepository;
import com.tauanoliveira.softwaretec.repository.EnderecoRepository;
import com.tauanoliveira.softwaretec.service.exception.DataIntegrityException;
import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteService {
    
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    public Cliente findById(Integer id) {
        Optional<Cliente> entity = clienteRepository.findById(id);
        return entity.orElseThrow(() -> new EntityNotFoundException(
            "Cliente não encontrado! id; " + id + ", Tipo: " + Cliente.class.getName()
        ));
    }

    @Transactional
    public Cliente insert(Cliente entity) {
        entity = clienteRepository.save(entity);
        enderecoRepository.save(entity.getEndereco());
        return entity;
    }

    public Cliente update(Integer id, ClienteDTO entityDTO) {
        Cliente entity = this.fromDTO(entityDTO, id);
        return clienteRepository.save(entity);
    }

    public Cliente fromDTO(ClienteDTO entityDTO, Integer id) {
        Cliente entity = this.findById(id);
        entity.setName(entityDTO.getName());
        entity.setEmail(entityDTO.getEmail());
        entity.setContato(entityDTO.getcontato());

        Endereco e = entity.getEndereco();
        e.setCliente(entity);
        e.setEstado(entityDTO.getEstado());
        e.setCidade(entityDTO.getCidade());
        e.setBairro(entityDTO.getBairro());
        e.setNumero(entityDTO.getNumero());
        e.setCEP(entityDTO.getCEP());
        enderecoRepository.save(e);

        entity.setEndereco(e);
        return entity;
    }

    public Cliente fromNewDTO(ClienteNewDTO entityDTO) {
        Cliente cli = new Cliente(null, entityDTO.getName(), entityDTO.getEmail(), entityDTO.getCpfOuCnpj(), entityDTO.getContato(), TipoCliente.toEnum(entityDTO.getTipoCliente()));
        Endereco end = new Endereco(null, cli, entityDTO.getEstado(), entityDTO.getCidade(), entityDTO.getBairro(), entityDTO.getNumero(), entityDTO.getCEP());
        cli.setEndereco(end);
        return cli;
    }

    public void delete(Integer id) {
		findById(id);
		try {
			clienteRepository.deleteById(id);
		}catch(DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não E possível Excluir porque há dominios relacionadas");
		}
	}

}