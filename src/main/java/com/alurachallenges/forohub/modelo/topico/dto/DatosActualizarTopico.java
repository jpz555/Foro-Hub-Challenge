package com.alurachallenges.forohub.modelo.topico.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotNull;

public record DatosActualizarTopico(
        String titulo,
        String mensaje,
        @JsonAlias("nombreDelCurso")
        String curso

) {
}
