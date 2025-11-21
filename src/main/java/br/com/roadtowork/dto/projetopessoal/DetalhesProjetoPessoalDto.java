package br.com.roadtowork.dto.projetopessoal;

import br.com.roadtowork.dto.usuario.DetalheIDUsuarioDto;

import java.time.LocalDate;

public class DetalhesProjetoPessoalDto {

    private int id;
    private String titulo;
    private String descricao;
    private boolean concluido;
    private LocalDate dataCriacao;
    private LocalDate dataConclusao;
    private DetalheIDUsuarioDto usuario;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public boolean isConcluido() { return concluido; }
    public void setConcluido(boolean concluido) { this.concluido = concluido; }

    public LocalDate getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDate dataCriacao) { this.dataCriacao = dataCriacao; }

    public LocalDate getDataConclusao() { return dataConclusao; }
    public void setDataConclusao(LocalDate dataConclusao) { this.dataConclusao = dataConclusao; }

    public DetalheIDUsuarioDto getUsuario() { return usuario; }
    public void setUsuario(DetalheIDUsuarioDto usuario) { this.usuario = usuario; }
}
