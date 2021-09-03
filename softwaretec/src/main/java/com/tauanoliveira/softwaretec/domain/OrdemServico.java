package com.tauanoliveira.softwaretec.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tauanoliveira.softwaretec.domain.enums.EstadoServico;

@Entity
public class OrdemServico implements Serializable{
	private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate instante;

    @Enumerated(EnumType.ORDINAL)
    private EstadoServico estadoServico;

    @OneToOne(cascade=CascadeType.ALL, mappedBy = "ordemServico")
    private Pagamento pagamento;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ordemServico")
    private List<ItemPedido> itens = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "cliente_Id")                                                                                
    private Cliente cliente;
 
    public OrdemServico() {
    }

    public OrdemServico(Integer id, Cliente cliente, EstadoServico estadoServico) {
        this.id = id;
        this.cliente = cliente;
        this.instante = LocalDate.now();
        this.estadoServico = estadoServico;
    }

    public BigDecimal getPrecoTotal(){
        BigDecimal soma = BigDecimal.ZERO;
        for(ItemPedido ip: itens){
            soma = soma.add(ip.getSubTotal());
        }
        return soma;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getInstante() {
        return instante;
    }
    
    public void setInstante(LocalDate instante) {
        this.instante = instante;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public EstadoServico getEstadoServico() {
        return estadoServico;
    }

    public void setEstadoServico(EstadoServico estadoServico) {
        this.estadoServico = estadoServico;
    }

    public Pagamento getPagamento() {
		return pagamento;
	}

	public void setPagamento(Pagamento pagamento) {
		this.pagamento = pagamento;
	}

    public List<ItemPedido> getItens() {
		return itens;
	}

	public void setItens(List<ItemPedido> itens) {
		this.itens = itens;
	}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        OrdemServico other = (OrdemServico) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        StringBuilder builder = new StringBuilder();
        builder.append("Pedido número: ");
        builder.append(getId());
        builder.append(", Instante: ");
        builder.append(getInstante());
        builder.append(", Cliente: ");
        builder.append(getCliente().getName());
		builder.append("\nDetalhes:\n");
		for(ItemPedido ip: getItens()){
			builder.append(ip.toString());
		}
        builder.append("valor total: ");
		builder.append(nf.format(getPrecoTotal()));
        return builder.toString();
    }
}