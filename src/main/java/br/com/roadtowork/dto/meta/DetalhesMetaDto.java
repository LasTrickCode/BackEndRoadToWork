package br.com.roadtowork.dto.meta;

import br.com.roadtowork.dto.usuario.DetalheIDUsuarioDto;

import java.time.LocalDate;

public class DetalhesMetaDto {

    private int id;
    private int metaTotal;
    private int atual;
    private boolean concluida;
    private LocalDate dataCriacao;
    private LocalDate dataConclusao;
    private DetalheIDUsuarioDto usuario;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMetaTotal() {
        return metaTotal;
    }

    public void setMetaTotal(int metaTotal) {
        this.metaTotal = metaTotal;
    }

    public int getAtual() {
        return atual;
    }

    public void setAtual(int atual) {
        this.atual = atual;
    }

    public boolean isConcluida() {
        return concluida;
    }

    public void setConcluida(boolean concluida) {
        this.concluida = concluida;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDate getDataConclusao() {
        return dataConclusao;
    }

    public void setDataConclusao(LocalDate dataConclusao) {
        this.dataConclusao = dataConclusao;
    }

    public DetalheIDUsuarioDto getUsuario() {
        return usuario;
    }

    public void setUsuario(DetalheIDUsuarioDto usuario) {
        this.usuario = usuario;
    }
}
