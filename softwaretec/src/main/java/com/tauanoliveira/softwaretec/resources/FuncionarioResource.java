package com.tauanoliveira.softwaretec.resources;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import com.tauanoliveira.softwaretec.domain.Funcionario;
import com.tauanoliveira.softwaretec.dto.FuncionarioDTO;
import com.tauanoliveira.softwaretec.service.FuncionarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(value = "/funcionarios")
public class FuncionarioResource {
    
    @Autowired
    private FuncionarioService funcionarioService;

    @RequestMapping(method=RequestMethod.GET)
    public ResponseEntity<List<Funcionario>> findAll() {
        List<Funcionario> entity = funcionarioService.findAll();
        return ResponseEntity.ok().body(entity);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Funcionario> find(@PathVariable Integer id) {
        Funcionario entity = funcionarioService.findById(id);
        return ResponseEntity.ok().body(entity);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Funcionario> insert(@Valid @RequestBody Funcionario entity) {
        funcionarioService.insert(entity);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(entity.getId()).toUri();	
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ATENDENTE', 'TECNICO')")
    @RequestMapping(value = "/changePassword/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> update(@Valid @RequestBody FuncionarioDTO entityDTO, @PathVariable Integer id) {
        funcionarioService.update(id, entityDTO);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Funcionario> delete(@PathVariable Integer id) {
        funcionarioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}