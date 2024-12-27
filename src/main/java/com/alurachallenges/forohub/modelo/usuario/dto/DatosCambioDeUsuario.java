package com.alurachallenges.forohub.modelo.usuario.dto;

import com.alurachallenges.forohub.modelo.usuario.PerfilUsuario;
import jakarta.validation.constraints.NotNull;

public record DatosCambioDeUsuario(
//        @NotNull
//        String idDeUsuario,
        @NotNull
        PerfilUsuario perfil) {

}
