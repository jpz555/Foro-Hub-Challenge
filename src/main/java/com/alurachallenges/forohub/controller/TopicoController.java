package com.alurachallenges.forohub.controller;


import com.alurachallenges.forohub.modelo.topico.*;
import com.alurachallenges.forohub.modelo.topico.TopicoRepository;
import com.alurachallenges.forohub.modelo.topico.dto.DatosActualizarTopico;
import com.alurachallenges.forohub.modelo.topico.dto.DatosRegistroTopico;
import com.alurachallenges.forohub.modelo.topico.dto.DatosRespuestaTopico;
import com.alurachallenges.forohub.modelo.usuario.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    private TopicoService topicoService;

    @Autowired
    private TopicoRepository repositorio;

    @Autowired
    private UsuarioRepository usuarioRepository;



    @PostMapping
    public ResponseEntity registrarTopico(@Valid @RequestBody DatosRegistroTopico datos, UriComponentsBuilder uriComponentsBuilder) {
        return topicoService.registrarTopico(datos, uriComponentsBuilder);
    }

    @GetMapping("/{id}")
    public ResponseEntity obtenerTopidoPorId(@PathVariable Long id){
        return topicoService.obtenerTopidoPorId(id);
    }

    @GetMapping
    public ResponseEntity<Page<DatosRespuestaTopico>> buscarTopicosPorTituloYFecha(
            Pageable paginacion,
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false, name = "fecha")@DateTimeFormat(pattern = "yyyy-mm-dd") LocalDate fecha
            ){

        var topicos = topicoService.buscarTopicosPorTituloYFecha(paginacion, titulo, fecha );
        return ResponseEntity.ok(topicos.map(DatosRespuestaTopico::new));
    }

    @GetMapping("/personal")
    public ResponseEntity<Page<DatosRespuestaTopico>> topicosPersonales(Pageable paginacion){
        var topicos = topicoService.buscarTopicosPersonales(paginacion);
        return ResponseEntity.ok(topicos.map(DatosRespuestaTopico::new));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DatosRespuestaTopico> actualizarDatosTopico(@PathVariable Long id, @RequestBody @Valid DatosActualizarTopico datos) {
        return topicoService.actualizarTopico(id, datos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity eliminarTopico(@PathVariable Long id){
        return topicoService.eliminarTopico(id);
    }

}
