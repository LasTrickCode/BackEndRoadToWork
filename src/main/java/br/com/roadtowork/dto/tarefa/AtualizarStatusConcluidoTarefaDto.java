package br.com.roadtowork.dto.tarefa;

import jakarta.validation.constraints.NotNull;

public class AtualizarStatusConcluidoTarefaDto {

    @NotNull(message = "O campo concluido é obrigatório")
    private Boolean concluido;

    public Boolean getConcluido() {
        return concluido;
    }

    public void setConcluido(Boolean concluido) {
        this.concluido = concluido;
    }

}
