package com.alurachallenges.forohub.modelo.topico.dto;

import com.alurachallenges.forohub.modelo.respuesta.DTO.DatosDetalleRespuesta;
import com.alurachallenges.forohub.modelo.respuesta.Respuesta;
import com.alurachallenges.forohub.modelo.topico.Estado;
import com.alurachallenges.forohub.modelo.topico.Topico;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record DatosTopicoConRespuestas(
        Long id,
        String autor,
        String titulo,
        String mensaje,
        LocalDateTime fechaDeCreacion,
        Estado estado,
        String curso,
        List<DatosDetalleRespuesta> respuestas

) {

    public DatosTopicoConRespuestas (Topico topico){
        this(topico.getId(), topico.getUsuario().getNombre(),topico.getTitulo(),
                topico.getMensaje(), topico.getFechaDeCreacion(), topico.getEstado(),
                topico.getCurso(), topico.getRespuestas()
                        .stream()
                        .map(respuesta -> new DatosDetalleRespuesta(respuesta))
                        .collect(Collectors.toList()));
    }
}
