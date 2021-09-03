package com.tauanoliveira.softwaretec.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tauanoliveira.softwaretec.domain.enums.Perfil;

@JsonIgnoreProperties(value = "senha", allowSetters = true)
@Entity
public class Funcionario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//estrategia de geração de ID cria id ordenado
    private Integer id;

    @NotBlank(message = "Campo obrigatorio!")
    private String email;

    @NotBlank(message = "Campo obrigatorio!")
    private String senha;

	private Perfil perfil;

    public Funcionario() {
    }

    public Funcionario(Integer id, String email, String senha, Integer perfil) {
        this.id = id;
        this.email = email;
        this.senha = senha;
        this.perfil = Perfil.toEnum(perfil);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Integer perfil) {
		this.perfil = Perfil.toEnum(perfil);
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
        Funcionario other = (Funcionario) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}