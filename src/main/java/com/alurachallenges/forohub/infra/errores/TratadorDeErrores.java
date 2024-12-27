package com.alurachallenges.forohub.infra.errores;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class TratadorDeErrores {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity tratarErrorDeIdentificacion400(NoSuchElementException e){
        var error = e.getMessage();
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler({EntityNotFoundException.class, RecursoNoEncontrado.class})
    public ResponseEntity tratarError404(RuntimeException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler({InternalAuthenticationServiceException.class, UsuarioNoAutorizado.class})
    public ResponseEntity tratarError401(RuntimeException error){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(RegistroSinCambios.class)
    public ResponseEntity tratarExcepcionDatosIguales(RegistroSinCambios e){
        return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
    }








}
