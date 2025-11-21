package br.com.roadtowork.dto.projetopessoal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CadastroProjetoPessoalDto {

    @NotNull
    private Integer usuario;

    @NotBlank
    private String titulo;

    private String descricao;

    public Integer getUsuario() { return usuario; }
    public void setUsuario(Integer usuario) { this.usuario = usuario; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
}

