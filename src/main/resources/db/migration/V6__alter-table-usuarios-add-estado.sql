alter table usuarios_foro
add column estado enum('ACTIVO','INACTIVO') default 'ACTIVO';

alter table usuarios_foro
change rol perfil varchar(100);




