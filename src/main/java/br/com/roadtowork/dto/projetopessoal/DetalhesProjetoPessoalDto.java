package br.com.roadtowork.dto.projetopessoal;

import br.com.roadtowork.dto.usuario.DetalheIDUsuarioDto;

import java.time.LocalDate;

public class DetalhesProjetoPessoalDto {

    private int id;
    private LocalDate periodo;
    private String titulo;
    private String descricao;
    private boolean concluido;
    private LocalDate conclusao;
    private DetalheIDUsuarioDto usuario;

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

    public DetalheIDUsuarioDto getUsuario() {
        return usuario;
    }

    public void setUsuario(DetalheIDUsuarioDto usuario) {
        this.usuario = usuario;
    }
}
