create table respuestas (
    id bigint not null auto_increment,
    mensaje varchar(5000) not null,
    fecha_de_creacion datetime not null,
    solucion tinyint not null,
    topico_id bigint not null,
    usuario_id bigint not null,
    estado enum('ACTIVO','ACTUALIZADO','ELIMINADO') default 'ACTIVO',

    primary key(id),
    constraint fk_respuestas_topico foreign key(topico_id) references topicos(id) on delete cascade,
    constraint fk_respuestas_usuario foreign key(usuario_id) references usuarios_foro(id)
);