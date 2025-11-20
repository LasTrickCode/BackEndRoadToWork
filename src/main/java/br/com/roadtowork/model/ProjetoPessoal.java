package br.com.roadtowork.model;

import java.time.LocalDate;

public class ProjetoPessoal {

    private int id;
    private LocalDate periodo;
    private String titulo;
    private String descricao;
    private boolean concluido;
    private LocalDate conclusao;
    private Usuario usuario;

    public ProjetoPessoal() {
    }

    public ProjetoPessoal(int id, LocalDate periodo, String titulo, String descricao, boolean concluido, LocalDate conclusao, Usuario usuario) {
        this.id = id;
        this.periodo = periodo;
        this.titulo = titulo;
        this.descricao = descricao;
        this.concluido = concluido;
        this.conclusao = conclusao;
        this.usuario = usuario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getPeriodo() {
        return periodo;
    }

    public void setPeriodo(LocalDate periodo) {
        this.periodo = periodo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isConcluido() {
        return concluido;
    }

    public void setConcluido(boolean concluido) {
        this.concluido = concluido;
    }

    public LocalDate getConclusao() {
        return conclusao;
    }

    public void setConclusao(LocalDate conclusao) {
        this.conclusao = conclusao;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
