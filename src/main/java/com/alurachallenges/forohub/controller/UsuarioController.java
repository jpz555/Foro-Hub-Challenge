package com.alurachallenges.forohub.controller;

import com.alurachallenges.forohub.modelo.usuario.UsuarioRepository;
import com.alurachallenges.forohub.infra.seguridad.ValidadorPermisos;
import com.alurachallenges.forohub.modelo.usuario.dto.*;
import com.alurachallenges.forohub.modelo.usuario.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ValidadorPermisos validadorPermisos;

    @PostMapping
    public ResponseEntity registrarUsuario(@Valid @RequestBody DatosRegistroUsuario datosRegistroUsuario,
                                           UriComponentsBuilder uriComponentsBuilder){
        return usuarioService.registrarUsuario(datosRegistroUsuario, uriComponentsBuilder);
    }

    @GetMapping
    public ResponseEntity obtenerUsuarios(Pageable paginacion){
        return usuarioService.obtenerListadoUsuarios(paginacion);
    }

    @GetMapping("/{id}")
    public DatosDetalleUsuario obtenerUsuarioEspecifico(@PathVariable Long id){
        return usuarioService.obtenerUsuarioEspecifico(id);
    }

    @PutMapping
    public ResponseEntity<DatosDetalleUsuario> actualizarUsuario(@RequestBody @Valid DatosActualizarUsuario datosActualizar){
        var usuarioModificado = usuarioService.actualizarInformacionDelUsuario(datosActualizar);
        return ResponseEntity.ok(usuarioModificado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity eliminarUsuario(@PathVariable Long id){
        return usuarioService.eliminarUsuario(id);
    }

    @PutMapping("/perfil/{id}")
    public ResponseEntity cambiarRolUsuario(@PathVariable Long id,
                                            @Valid @RequestBody DatosCambioDeUsuario datosCambioDeUsuario){
        var usuario = usuarioService.cambiarPerfilUsuario(id, datosCambioDeUsuario);
        return ResponseEntity.ok(usuario);
    }

}
