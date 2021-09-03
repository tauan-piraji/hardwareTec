package com.tauanoliveira.softwaretec.resources;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import com.tauanoliveira.softwaretec.domain.Cliente;
import com.tauanoliveira.softwaretec.dto.ClienteDTO;
import com.tauanoliveira.softwaretec.dto.ClienteNewDTO;
import com.tauanoliveira.softwaretec.service.ClienteService;

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
@RequestMapping(value = "/clientes")
public class ClienteResource {
    
    @Autowired
    private ClienteService clienteService;

    @RequestMapping(method=RequestMethod.GET)
    public ResponseEntity<List<Cliente>> findAll() {
        List<Cliente> entity = clienteService.findAll();
        return ResponseEntity.ok().body(entity);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Cliente> find(@PathVariable Integer id) {
        Cliente entity = clienteService.findById(id);
        return ResponseEntity.ok().body(entity);
    }

    @PreAuthorize("hasAnyRole('ADMIN','ATENDENTE')")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Cliente> insert(@Valid @RequestBody ClienteNewDTO entityDTO) {
        Cliente entity = clienteService.fromNewDTO(entityDTO);
        entity = clienteService.insert(entity);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(entity.getId()).toUri();	
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','ATENDENTE')")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> update(@Valid @RequestBody ClienteDTO entityDTO, @PathVariable Integer id) {
        clienteService.update(id, entityDTO);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Cliente> delete(@PathVariable Integer id) {
        clienteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}