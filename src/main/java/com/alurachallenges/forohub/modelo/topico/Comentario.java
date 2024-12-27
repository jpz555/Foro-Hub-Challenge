package com.alurachallenges.forohub.modelo.topico;

import com.alurachallenges.forohub.modelo.usuario.Usuario;
import lombok.Getter;

@Getter
public abstract class Comentario {
    private Usuario usuario;
}
