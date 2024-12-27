package com.alurachallenges.forohub.modelo.usuario.dto;

import com.alurachallenges.forohub.modelo.usuario.PerfilUsuario;
import com.alurachallenges.forohub.modelo.usuario.Usuario;

public record DatosCompletoUsuarioAdministrador(
        Long id,
        String nombreDelUsuario,
        String email,
        String perfil,
        Integer cantidadDeTopicos,
        Integer cantidadDeRespuestas
) {
    public DatosCompletoUsuarioAdministrador(Usuario usuario){
        this(usuario.getId(), usuario.getNombre(), usuario.getEmail()
                ,usuario.getPerfil().name(), usuario.getTopicos().size(),
                usuario.getRespuestas().size());

    }
}
