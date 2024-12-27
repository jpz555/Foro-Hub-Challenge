package com.alurachallenges.forohub.modelo.respuesta;

import com.alurachallenges.forohub.infra.errores.RecursoNoEncontrado;
import com.alurachallenges.forohub.infra.errores.UsuarioNoAutorizado;
import com.alurachallenges.forohub.modelo.respuesta.DTO.DatosActualizarRespuesta;
import com.alurachallenges.forohub.modelo.respuesta.DTO.DatosDetalleRespuesta;
import com.alurachallenges.forohub.modelo.respuesta.DTO.DatosRegistroRespuesta;
import com.alurachallenges.forohub.modelo.topico.TopicoRepository;
import com.alurachallenges.forohub.modelo.usuario.Usuario;
import com.alurachallenges.forohub.modelo.usuario.UsuarioRepository;
import com.alurachallenges.forohub.infra.seguridad.ValidadorPermisos;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@Service
public class RespuestaService {

    @Autowired
    private RespuestaRepository respuestaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private ValidadorPermisos validador;

    // Encontrar Respuesta por ID
    public Respuesta validarRespuesta(Long id){
        Optional<Respuesta> respuestaABuscar =respuestaRepository.findById(id);
        Respuesta respuesta = respuestaABuscar.get();
        if (respuestaABuscar.isPresent()){
            respuestaABuscar.get();
        } else {
            System.out.println("No hay una respuesta registrada con este id.");
        }
        return respuesta;
    }

    // Verificar cambios del topico
    public boolean verificarCambiosRespuesta(Long id, DatosActualizarRespuesta datosActualizarRespuesta){
        var topicoBuscado = validarRespuesta(id);
        return !datosActualizarRespuesta.mensaje().equals(topicoBuscado.getMensaje());
    }

    @Transactional
    public ResponseEntity responderTopico(@Valid @RequestBody DatosRegistroRespuesta datos,
                                          UriComponentsBuilder uriComponentsBuilder){
        Long idUsuario = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (idUsuario == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no Autorizado");
        }
        Usuario usuario = usuarioRepository.getReferenceById(idUsuario);
        var topico = topicoRepository.getReferenceById(Long.valueOf(datos.idTopico()));

        if (topico == null){
            throw new RuntimeException("El topico que esta ingresando no es valido");
        }
        Respuesta respuesta = respuestaRepository.save(new Respuesta(datos, usuario, topico));
        var detalleRespuesta = new DatosDetalleRespuesta(respuesta);
        URI url = uriComponentsBuilder.path("/respuestas/{id}").buildAndExpand( respuesta.getId()).toUri();
        return ResponseEntity.created(url).body(detalleRespuesta);

    }

    // Metodo GET
    public ResponseEntity<Page<DatosDetalleRespuesta>> obtenerListadoDeRespuestas(@PageableDefault(sort = {"fechaDeCreacion"}) Pageable pageable){
        var respuestas = respuestaRepository.findByEstado(pageable);
        return ResponseEntity.ok(respuestas.map(DatosDetalleRespuesta::new));
    }

    // Metodo GET detallado
    public ResponseEntity<DatosDetalleRespuesta> obtenerComentarioEspecifico(@PathVariable Long id){
        Respuesta respuesta = respuestaRepository.getReferenceById(id);
        if (respuesta.getEstado().equals(EstadoRespuesta.ELIMINADO)){
            return ResponseEntity.ok(new DatosDetalleRespuesta(respuesta));
        }
        return ResponseEntity.notFound().build();
    }

    // Metodo PUT
    @Transactional
    public ResponseEntity<DatosDetalleRespuesta> actualizarRespuesta(@Valid DatosActualizarRespuesta datos, @PathVariable Long id){
        Respuesta respuestaAActualizar = validarRespuesta(id);
        if (validador.validarUsuarioactualEsAutor(respuestaAActualizar) || (validador.validarRolAdministrador() || validador.validarRolModerador())){
            if (verificarCambiosRespuesta(id, datos)){
                respuestaAActualizar.actualizarRespuesta(datos);
                return ResponseEntity.ok(new DatosDetalleRespuesta(respuestaAActualizar));
            }
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // Metodo DELETE
    @Transactional
    public ResponseEntity eliminarRespuesta(@PathVariable Long id){
        Respuesta respuesta = respuestaRepository.getReferenceById(id);
        if (validador.validarUsuarioactualEsAutor(respuesta) || (validador.validarRolAdministrador() || validador.validarRolModerador())){
            Respuesta respuestaAEliminar = validarRespuesta(id);
            respuestaAEliminar.desactivarRespuesta();
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @Transactional
    public DatosDetalleRespuesta solucionarTopico(Long id){
        Respuesta respuesta = respuestaRepository.getReferenceById(id);
        if (respuesta == null){
            throw new RecursoNoEncontrado("Registro no encontrado.");
        }
        if (validador.validarRolAdministrador() || validador.validarRolModerador()){
             respuesta.solucionarRespuesta();
            return new DatosDetalleRespuesta(respuesta);
        } else {
            throw new UsuarioNoAutorizado("El usuario no tiene permiso para esta acción.");
        }
    }


}
