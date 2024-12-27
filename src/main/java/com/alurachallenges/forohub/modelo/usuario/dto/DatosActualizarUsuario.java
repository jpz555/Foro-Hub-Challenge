package com.alurachallenges.forohub.modelo.usuario.dto;

import jakarta.validation.constraints.Email;

public record DatosActualizarUsuario(
        String nombre,
        @Email
        String email,
        String clave


) {
}
