package br.com.roadtowork.dto.tarefa;

import jakarta.validation.constraints.*;

public class CadastroTarefaDto {

    @NotNull(message = "Usuário é obrigatório")
    @Min(value = 1, message = "Usuário deve ser um ID válido")
    private Integer usuario;

    @NotBlank
    @Size(max = 100)
    private String titulo;

    @Size(max = 500)
    private String descricao;

    private boolean concluido;

    public int getUsuario() {
        return usuario;
    }

    public void setUsuario(int usuario) {
        this.usuario = usuario;
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
}
