package br.com.roadtowork.dto.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CadastroUsuarioDto {

    @NotBlank
    @Size(max = 100)
    private String nome;

    @NotBlank
    @Size(max = 254)
    private String email;

    @NotBlank
    @Size(max = 20)
    private String senha;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
