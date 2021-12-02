-- МЕНЕДЖЕР
create table manager (
     id serial,
     email varchar (30) not null unique,
     password varchar (100) not null,

     primary key (id)
);