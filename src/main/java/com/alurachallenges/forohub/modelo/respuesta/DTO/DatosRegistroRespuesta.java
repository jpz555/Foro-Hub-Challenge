package com.alurachallenges.forohub.modelo.respuesta.DTO;

import jakarta.validation.constraints.NotNull;

public record DatosRegistroRespuesta(
        @NotNull
        String idTopico,
        @NotNull
        String mensaje
        ) {


}
