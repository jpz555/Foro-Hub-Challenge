alter table topicos
modify column mensaje varchar(5000),
modify column titulo varchar(5000);
alter table topicos
add constraint titulo_mensaje_unique unique(titulo(355), mensaje(355));

