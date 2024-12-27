package com.alurachallenges.forohub.modelo.usuario;

import com.alurachallenges.forohub.infra.seguridad.ValidadorPermisos;
import com.alurachallenges.forohub.modelo.usuario.dto.*;
import com.alurachallenges.forohub.infra.errores.RegistroSinCambios;
import com.alurachallenges.forohub.infra.errores.UsuarioNoAutorizado;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ValidadorPermisos validadorPermisos;

//    public UsuarioService(PasswordEncoder passwordEncoder) {
//        this.passwordEncoder = passwordEncoder;
//    }

    @Transactional
    public ResponseEntity registrarUsuario(@Valid @RequestBody DatosRegistroUsuario datosUsuario,
                                           UriComponentsBuilder uriComponentsBuilder){
        String claveEncriptada = passwordEncoder.encode(datosUsuario.clave());
        Usuario usuario = usuarioRepository.save(new Usuario(datosUsuario, claveEncriptada));
        var datosDeUsuarioRegistrado = new DatosDetalleUsuario(usuario);
        URI url = uriComponentsBuilder.path("/usuarios/{id}").buildAndExpand(usuario.getId()).toUri();
        return ResponseEntity.created(url).body(datosDeUsuarioRegistrado);
    }

    // Metodo GET para ver un usuario registrado
    public DatosDetalleUsuario obtenerUsuarioEspecifico(@PathVariable Long id){
        Usuario usuario = usuarioRepository.getReferenceById(id);
        return new DatosDetalleUsuario(usuario);

    }

    // Metodo GET listar usuarios
    public ResponseEntity obtenerListadoUsuarios(@PageableDefault(size = 10) Pageable paginacion){
        var usuarios = usuarioRepository.findAll(paginacion);
        return ResponseEntity.ok(usuarios.map(DatosDetalleUsuario::new));
    }

    public Usuario obtenerElIdUsuarioActual(){
        Long idUsuario = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (idUsuario == null){
            return null;
        }
        Usuario usuario = usuarioRepository.getReferenceById(idUsuario);
        return usuario;
    }

    public boolean verificarCambiosUsuario(Long id, DatosActualizarUsuario datosNuevos){
        var usuario = usuarioRepository.getReferenceById(id);
        return (datosNuevos.nombre() != null && !datosNuevos.nombre().equals(usuario.getNombre())) ||
                (datosNuevos.email() != null && !datosNuevos.email().equals(usuario.getEmail())) ||
                ((datosNuevos.clave() != null && !datosNuevos.clave().equals(usuario.getClave())));
    }

    // Metodo PUT - actualizar Usuarios
    @Transactional
    public DatosDetalleUsuario actualizarInformacionDelUsuario(@RequestBody @Valid DatosActualizarUsuario datosActualizar){
        Usuario usuario = obtenerElIdUsuarioActual();
        if (validadorPermisos.validarPerfilDeUsuario(PerfilUsuario.ADMINISTRADOR)){
            if (verificarCambiosUsuario(usuario.getId(), datosActualizar)){
                var claveEncriptada = passwordEncoder.encode(datosActualizar.clave());
                usuario.actualizarDatos(datosActualizar, claveEncriptada);
                return new DatosDetalleUsuario(usuario);
            }
            throw new RegistroSinCambios("Los datos ingresados no presentan cambios.");
        }

        throw new UsuarioNoAutorizado("El usuario no tiene permiso para esta acción.");
    }

    // Metodo DELETE
    @Transactional
    public ResponseEntity eliminarUsuario(@PathVariable Long id){
        if (validadorPermisos.validarPerfilDeUsuario(PerfilUsuario.ADMINISTRADOR)){
            Usuario usuario = usuarioRepository.getReferenceById(id);
            usuario.desactivarUsuario();
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @Transactional
    public DatosCompletoUsuarioAdministrador cambiarPerfilUsuario(Long id, DatosCambioDeUsuario datosCambioDeUsuario) {
        if (validadorPermisos.validarPerfilDeUsuario(PerfilUsuario.ADMINISTRADOR)) {
            Usuario usuario = usuarioRepository.getReferenceById(id);
            usuario.cambiarPerfil(datosCambioDeUsuario);
            return new DatosCompletoUsuarioAdministrador(usuario);
        } else {
            throw new UsuarioNoAutorizado("El usuario no tiene permiso para realizar esta acción");
        }
    }
}
