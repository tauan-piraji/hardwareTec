package com.tauanoliveira.softwaretec.service;

import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.tauanoliveira.softwaretec.domain.Equipamento;
import com.tauanoliveira.softwaretec.domain.ItemPedido;
import com.tauanoliveira.softwaretec.domain.OrdemServico;
import com.tauanoliveira.softwaretec.domain.Pagamento;
import com.tauanoliveira.softwaretec.domain.PagamentoComBoleto;
import com.tauanoliveira.softwaretec.domain.enums.AprovaServico;
import com.tauanoliveira.softwaretec.domain.enums.EstadoPagamento;
import com.tauanoliveira.softwaretec.domain.enums.EstadoServico;
import com.tauanoliveira.softwaretec.repository.EquipamentoRepository;
import com.tauanoliveira.softwaretec.repository.ItemPedidoRepository;
import com.tauanoliveira.softwaretec.repository.OrdemServicoRepository;
import com.tauanoliveira.softwaretec.repository.PagamentoRepository;
import com.tauanoliveira.softwaretec.service.exception.ObjectNotFoundException;
import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class OrdemServicoService {
    
    @Autowired
    private OrdemServicoRepository ordemServicoRepository;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private EquipamentoRepository equipamentoRepository;

    @Autowired
    private EmailService emailService; 

    @Autowired
	private S3Service s3Service;

	@Autowired
	private ImageService imageService;

    @Value("${spring.profiles.active}")
	private String perfil;

	@Value("${img.prefix.client.profile}")
	private String prefix;

	@Value("${img.profile.size}")
	private Integer size;

    @Value("${img.prefix.uri}")
    private String uriPrefix;
    
    public List<OrdemServico> findAll() {
        return ordemServicoRepository.findAll();
    }

    public OrdemServico findById(Integer id) {
        Optional<OrdemServico> entity = ordemServicoRepository.findById(id);
        
        for(ItemPedido ip: entity.get().getItens()) {
            Equipamento e = ip.getEquipamento();
            e.setImgURI(uriPrefix + e.getImgName());
        }

        return entity.orElseThrow(() -> new EntityNotFoundException(
            "Ordem de serviço não encontrada! id; " + id + ", Tipo: " + OrdemServico.class.getName()
        ));
    }

    public void changeEstadoServicoById(Integer id, AprovaServico aprovaServico, Integer idIt) {
        OrdemServico entity = ordemServicoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
            "Ordem de serviço não encontrada! id; " + id + ", Tipo: " + OrdemServico.class.getName()
        ));
        ItemPedido IP = itemPedidoRepository.findById(idIt).orElseThrow(() -> new EntityNotFoundException(
            "Item pedido não encontrada! id; " + id + ", Tipo: " + OrdemServico.class.getName()
        ));
        IP.setAprovaServico(aprovaServico);
        for(ItemPedido ip: entity.getItens()) {
            if(ip.getAprovaServico() == AprovaServico.APROVADO) {
                entity.setEstadoServico(EstadoServico.APROVADO);
            }else{
                itemPedidoRepository.delete(ip);//deleta equipamento do item em vez do item ??????
            }
        }
        if(entity.getEstadoServico().equals(EstadoServico.PENDENTE)) {
            entity.setEstadoServico(EstadoServico.REPROVADO);
        }
        ordemServicoRepository.save(entity);
    }

    public void pagaById(Integer id) {
        OrdemServico entity = ordemServicoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
            "Ordem de serviço não encontrada! id; " + id + ", Tipo: " + OrdemServico.class.getName()
        ));
        if(entity.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) entity.getPagamento();
			pagto.setDataPagamento(LocalDate.now());
        }
        Pagamento pag = entity.getPagamento();
        pag.setEstadoPagamento(EstadoPagamento.QUITADO);
        pagamentoRepository.save(pag);
        ordemServicoRepository.save(entity);
    }

    public ItemPedido findByIdItem(Integer id) {
        Optional<ItemPedido> obj = itemPedidoRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
            "Ordem de serviço não encontrada! id; " + id + ", Tipo: " + ItemPedido.class.getName()
        ));
    }

    @Transactional
    public OrdemServico insert(OrdemServico entity) {
        entity.setInstante(LocalDate.now());
        entity.setCliente(clienteService.findById(entity.getCliente().getId()));
        entity.setEstadoServico(EstadoServico.PENDENTE);
        entity.getPagamento().setEstadoPagamento(EstadoPagamento.PENDENTE);
        entity.getPagamento().setOrdemServico(entity);
        if(entity.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) entity.getPagamento();
			LocalDate localDate = entity.getInstante();
            pagto.setDataVencimento(localDate.plusDays(7));
        }
        entity = ordemServicoRepository.save(entity);
        pagamentoRepository.save(entity.getPagamento());
        for(ItemPedido ip: entity.getItens()) {
            ip.setDesconto(BigDecimal.ZERO);

            Equipamento e = new Equipamento();
            e.setNome(ip.getEquipamento().getNome());
            e.setDescricao(ip.getEquipamento().getDescricao());
            e.setItens(ip);
            equipamentoRepository.save(e);

            ip.setEquipamento(e);
            ip.setPreco(BigDecimal.ZERO);
            ip.setOrdemServico(entity);
            itemPedidoRepository.save(ip);
        }
        return entity;
    }

    public OrdemServico update(Integer id, OrdemServico entity) {
        OrdemServico os = this.fromDTO(entity, id);
        return ordemServicoRepository.save(os);   
    }

    public OrdemServico fromDTO(OrdemServico os, Integer id) {
        OrdemServico entity = this.findById(id);
        entity.setInstante(os.getInstante());
        if(os.getEstadoServico() == null) {
            os.setEstadoServico(entity.getEstadoServico());
        }else if(os.getEstadoServico().equals(EstadoServico.CONCLUIDO)) {
            entity.setEstadoServico(EstadoServico.CONCLUIDO);
        }else{
            System.out.println("test json só aceita CONCLUIDO");
        }
        for(ItemPedido ipEntity: entity.getItens()){
            for(ItemPedido ipJson: os.getItens()) {
                if(ipEntity.getId() == ipJson.getId()) {
                    ipEntity.setDesconto(ipJson.getDesconto());

                    Equipamento e = ipEntity.getEquipamento();
                    e.setNome(ipJson.getEquipamento().getNome());
                    e.setDescricao(ipJson.getEquipamento().getDescricao());
                    equipamentoRepository.save(e);

                    ipEntity.setPreco(ipJson.getPreco());
                    itemPedidoRepository.save(ipEntity);
                }
            }
        }
        ordemServicoRepository.save(entity);
        if(os.getEstadoServico().equals(EstadoServico.PENDENTE)) {
            if(perfil == "test") {
                emailService.sendOrderConfirmationEmail(entity);
            }else{
                emailService.sendOrderConfirmationHtmlEmail(entity);
            }
        }else{
            if(perfil == "test") {
                emailService.sendOrderComcluidoEmail(entity);
            }else{
                emailService.sendOrderComcluidoHtmlEmail(entity);
            }
        }
        return entity;
    }

    public void delete(Integer id) {
        ordemServicoRepository.deleteById(id);
    }

    public URI uploadPicture(MultipartFile multipartFile, Integer id) {//upload imagem de perfil no s3
		Equipamento Eq = equipamentoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
            "Equipamento não encontrada! id; " + id + ", Tipo: " + Equipamento.class.getName()
        ));
		
		BufferedImage jpgImage = imageService.getJpgImageFromFile(multipartFile);//arquivo da imagem
		jpgImage = imageService.cropSquare(jpgImage);//busca o metodo que recorta a img
		jpgImage = imageService.resize(jpgImage, size);//Busca o metodo que ajusta a dimenção da img
		String fileName = prefix + Eq.getId() + ".jpg";//cria o nome da arquivo de img
        Eq.setImgName(fileName);
        equipamentoRepository.save(Eq);

		return s3Service.uploadFile(imageService.getInputStream(jpgImage, "jpg"), fileName, "image");//salva img no s3
	}
}   