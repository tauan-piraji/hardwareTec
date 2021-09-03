package com.tauanoliveira.softwaretec.dto;

import java.io.Serializable;

import com.tauanoliveira.softwaretec.domain.Cliente;

public class ClienteDTO implements Serializable{
	private static final long serialVersionUID = 1L;

    private Integer id;
    private String name;
    private String email;
    private String CPFouCNPJ;
    private String contato;
    private Object endereco;
    private String estado;
    private String cidade;
    private String bairro;
    private String numero;
    private String CEP;

    public ClienteDTO() {
    }

    public ClienteDTO(Cliente entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.email = entity.getEmail();
        this.contato = entity.getContato();
        this.endereco = entity.getEndereco();
    }

    public Integer getId() {
		return id;
	}
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCPFouCNPJ() {
        return CPFouCNPJ;
    }

    public void setCPFouCNPJ(String CPFouCNPJ) {
        this.CPFouCNPJ = CPFouCNPJ;
    }

    public String getcontato() {
        return contato;
    }

    public void setcontato(String contato) {
        this.contato = contato;
    }

    public Object getEndereco() {
        return endereco;
    }

    public void setEndereco(Object endereco) {
        this.endereco = endereco;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCEP() {
        return CEP;
    }

    public void setCEP(String cEP) {
        CEP = cEP;
    }  

    
}