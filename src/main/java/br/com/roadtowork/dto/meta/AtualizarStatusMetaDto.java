package br.com.roadtowork.dto.meta;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class AtualizarStatusMetaDto {

    private Boolean concluida;

    @NotNull
    private LocalDate dataConclusao;

    public Boolean getConcluida() {
        return concluida;
    }

    public void setConcluida(Boolean concluida) {
        this.concluida = concluida;
    }

    public LocalDate getDataConclusao() {
        return dataConclusao;
    }

    public void setDataConclusao(LocalDate dataConclusao) {
        this.dataConclusao = dataConclusao;
    }
}
