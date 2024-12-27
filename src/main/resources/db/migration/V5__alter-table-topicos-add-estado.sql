alter table topicos
add column estado enum('ACTIVO','ACTUALIZADO','ELIMINADO') default 'ACTIVO';



