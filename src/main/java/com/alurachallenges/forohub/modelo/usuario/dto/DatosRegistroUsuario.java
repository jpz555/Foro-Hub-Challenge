package com.alurachallenges.forohub.modelo.usuario.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record DatosRegistroUsuario(
        @NotNull
        String nombre,
        @NotNull
        @JsonAlias("correo electronico")
        @Email
        String email,
        @NotNull
        String clave
) {
}
