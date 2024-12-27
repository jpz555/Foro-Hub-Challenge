package com.alurachallenges.forohub.infra.errores;

public class UsuarioNoAutorizado extends RuntimeException {
    public UsuarioNoAutorizado(String mensaje) {
        super(mensaje);
    }
}
