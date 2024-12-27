create table topicos (
    id bigint not null auto_increment,
    titulo varchar(100) not null,
    mensaje text not null,
    fecha_de_creacion datetime not null,
    status tinyint not null,
    autor varchar(100) not null,
    curso varchar(100),
    usuario_id bigint not null,

    primary key(id),
    constraint fk_topicos_usuarios_id foreign key(usuario_id) references usuarios_foro(id)
);