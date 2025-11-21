package br.com.roadtowork.model;

import java.time.LocalDate;

public class Meta {

    private int id;
    private int metaTotal;
    private int atual;
    private LocalDate dataCriacao;
    private LocalDate dataConclusao;
    private boolean concluida;
    private Usuario usuario;

    public Meta() {
        this.metaTotal = 40;
        this.atual = 0;
        this.dataCriacao = LocalDate.now();
        this.concluida = false;
    }

    public Meta(int id, int metaTotal, int atual, LocalDate dataCriacao,
                LocalDate dataConclusao, boolean concluida, Usuario usuario) {
        this.id = id;
        this.metaTotal = metaTotal;
        this.atual = atual;
        this.dataCriacao = dataCriacao;
        this.dataConclusao = dataConclusao;
        this.concluida = concluida;
        this.usuario = usuario;
    }

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

    public boolean isConcluida() {
        return concluida;
    }

    public void setConcluida(boolean concluida) {
        this.concluida = concluida;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}

