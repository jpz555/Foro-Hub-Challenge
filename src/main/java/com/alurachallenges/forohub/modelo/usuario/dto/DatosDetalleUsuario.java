package com.alurachallenges.forohub.modelo.usuario.dto;

import com.alurachallenges.forohub.modelo.usuario.EstadoUsuario;
import com.alurachallenges.forohub.modelo.usuario.Usuario;

public record DatosDetalleUsuario(
        Long id,
        String nombreDelUsuario,
        String email,
        EstadoUsuario estado

) {
    public DatosDetalleUsuario(Usuario usuario){
        this(usuario.getId(), usuario.getNombre(), usuario.getEmail(), usuario.getEstado());
    }
}
