package com.tauanoliveira.softwaretec.resources;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import com.tauanoliveira.softwaretec.domain.OrdemServico;
import com.tauanoliveira.softwaretec.domain.enums.AprovaServico;
import com.tauanoliveira.softwaretec.service.OrdemServicoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(value = "/ordemServicos")
public class OrdemServicoResource {

    @Autowired
    private OrdemServicoService ordemServicoService;

    @RequestMapping(method=RequestMethod.GET)
    public ResponseEntity<List<OrdemServico>> findAll() {
        List<OrdemServico> entity = ordemServicoService.findAll();
        return ResponseEntity.ok().body(entity);
    }

    @RequestMapping(value="/{id}",method=RequestMethod.GET)
    public ResponseEntity<OrdemServico> findById(@PathVariable Integer id) {//transforma obj em um methodo http com bodu status header etc
        OrdemServico entity = ordemServicoService.findById(id);
        return ResponseEntity.ok().body(entity);
    }
    
    @PreAuthorize("hasAnyRole('ADMIN', 'ATENDENTE')")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> insert(@Valid @RequestBody OrdemServico entity) {
        ordemServicoService.insert(entity);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id").buildAndExpand(entity.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }
    
    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
    @RequestMapping(value="/picture/equipamento/{id}", method=RequestMethod.POST)
	public ResponseEntity<Void> uploadPicture(@RequestParam(name="file") MultipartFile file, @PathVariable Integer id){
        URI uri = ordemServicoService.uploadPicture(file, id);//upload imagem na URI dessa imagem
		return ResponseEntity.created(uri).build();//created resposta 201 http com uri do recurso
	}
    
    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
    @RequestMapping(value="/{id}",method=RequestMethod.PUT)
    public ResponseEntity<Void> update(@Valid @RequestBody OrdemServico entity, @PathVariable Integer id) {
        ordemServicoService.update(id, entity);
        return ResponseEntity.noContent().build();
    }
    
        @RequestMapping(value="/{id}/itemAprovada/{idIt}",method=RequestMethod.PUT)
        public ResponseEntity<String> aprovaById(@PathVariable(name = "id") Integer id, @PathVariable(name = "idIt") Integer idIt) {
            ordemServicoService.changeEstadoServicoById(id, AprovaServico.APROVADO, idIt);
            return ResponseEntity.ok().body("ordem aprovada");
        }
    
        @RequestMapping(value="/{id}/itemReprovada/{idIt}",method=RequestMethod.PUT)
        public ResponseEntity<String> reprovaById(@PathVariable(name = "id") Integer id, @PathVariable(name = "idIt") Integer idIt) {
            ordemServicoService.changeEstadoServicoById(id, AprovaServico.REPROVADO, idIt);
            return ResponseEntity.ok().body("ordem Reprovada");
        }
    
        @PreAuthorize("hasAnyRole('ADMIN', 'ATENDENTE')")
        @RequestMapping(value="/{id}/pagamentoQuitado",method=RequestMethod.PUT)
        public ResponseEntity<String> pagaById(@PathVariable Integer id) {
            ordemServicoService.pagaById(id);
            return ResponseEntity.ok().build();
        }
    
    @PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping(value="/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        ordemServicoService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
}