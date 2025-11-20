package br.com.roadtowork.dto.projetopessoal;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class AtualizarStatusProjetoPessoalDto {

    private Boolean concluido;

    @NotNull
    private LocalDate conclusao;

    public Boolean getConcluido() {
        return concluido;
    }

    public void setConcluido(Boolean concluido) {
        this.concluido = concluido;
    }

    public LocalDate getConclusao() {
        return conclusao;
    }

    public void setConclusao(LocalDate conclusao) {
        this.conclusao = conclusao;
    }
}
