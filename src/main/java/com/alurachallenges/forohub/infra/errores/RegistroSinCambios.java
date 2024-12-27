package com.alurachallenges.forohub.infra.errores;

public class RegistroSinCambios extends RuntimeException {
    public RegistroSinCambios(String mensaje) {
        super(mensaje);
    }
}
