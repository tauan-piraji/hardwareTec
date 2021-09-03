package com.tauanoliveira.softwaretec.domain.enums;

public enum EstadoServico {
 
    PENDENTE(1, "Serviço em espera"),
    APROVADO(2, "Serviço aprovado"),
    REPROVADO(3, "Serviço reprovado"),
    CONCLUIDO(4, "Serviço concluido");

    private int cod;
    private String descricao;

    private EstadoServico(int cod, String descricao) {
        this.cod = cod;
        this.descricao = descricao;
    }

    public int getCod(){
        return cod;
    }
    
    public void setCod(int cod) {
        this.cod = cod;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) { 
        this.descricao = descricao;
    }

    public static EstadoServico toEnum(Integer cod) {

        if(cod == null) {
            return null;
        }

        for (EstadoServico x : EstadoServico.values()) {
            if(cod.equals(x.getCod())) {
                return x;
            }
        }

        throw new IllegalArgumentException("Id invalido: " + cod);
    }
}