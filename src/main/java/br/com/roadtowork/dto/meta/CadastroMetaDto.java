package br.com.roadtowork.dto.meta;

import jakarta.validation.constraints.NotNull;

public class CadastroMetaDto {

    @NotNull
    private Integer usuario;

    public Integer getUsuario() {
        return usuario;
    }

    public void setUsuario(Integer usuario) {
        this.usuario = usuario;
    }
}
