package com.tauanoliveira.softwaretec.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tauanoliveira.softwaretec.domain.enums.AprovaServico;

@Entity
public class ItemPedido implements Serializable{
	private static final long serialVersionUID = 1L;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="ordemServico_id")
    private OrdemServico ordemServico;

    @Enumerated(EnumType.ORDINAL)
    private AprovaServico AprovaServico;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private BigDecimal preco;
    private BigDecimal desconto;

    @OneToOne(orphanRemoval = true, mappedBy = "itemPedido")
    private Equipamento equipamento;

    public ItemPedido() {
    }

    public ItemPedido(Integer id, OrdemServico ordemServico, BigDecimal preco, BigDecimal desconto, AprovaServico aprovaServico) {
        this.id = id;
        this.ordemServico = ordemServico;
        this.preco = preco;
        this.desconto = desconto;
        this.AprovaServico = aprovaServico;
    }

    public BigDecimal getSubTotal(){
        BigDecimal des = desconto.divide(new BigDecimal("100.00"));
        BigDecimal desco = preco.multiply(des);
        BigDecimal valor = preco.subtract(desco);
		return valor;
	}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public OrdemServico getOrdemServico() {
        return ordemServico;
    }

    public void setOrdemServico(OrdemServico ordemServico) {
        this.ordemServico = ordemServico;
    }

    public Equipamento getEquipamento() {
        return equipamento;
    }

    public void setEquipamento(Equipamento equipamento) {
        this.equipamento = equipamento;
    }

    public AprovaServico getAprovaServico() {
        return AprovaServico;
    }

    public void setAprovaServico(AprovaServico aprovaServico) {
        this.AprovaServico = aprovaServico;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public BigDecimal getDesconto() {
        return desconto;
    }

    public void setDesconto(BigDecimal desconto) {
        this.desconto = desconto;
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
        ItemPedido other = (ItemPedido) obj;
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
        String URL = "http://localhost:8080/ordemServicos/";
		StringBuilder builder = new StringBuilder();
        builder.append("Produto: ");
		builder.append(getEquipamento().getNome());
        builder.append("\nDescrição do problema: ");
        builder.append(getEquipamento().getDescricao());
        builder.append("\nImagem do aparelho: ");
        builder.append(getEquipamento().getImgURI());
		builder.append("\nvalor de reparo: ");
		builder.append(nf.format(getPreco()));
        builder.append(", desconto: ");
		builder.append(nf.format(getDesconto()));
		builder.append(", subtotal: ");
		builder.append(nf.format(getSubTotal()));
        builder.append("\nAprova conserto?");
        builder.append("\n" 
                        +"Aprova o conserto do aparelho ?" + URL + getOrdemServico().getId() + "/itemAprovada/" + getId() + "\n" 
                        +"Reprova o conserto do aparelho ?" + URL + getOrdemServico().getId() + "/itemReprovada/" + getId());
		builder.append("\n");
		return builder.toString();
	}
}