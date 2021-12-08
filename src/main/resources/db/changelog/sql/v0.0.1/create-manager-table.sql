-- МЕНЕДЖЕР
create table manager (
     id serial,
     email varchar (30) not null unique,
     password varchar (100) not null,
     name varchar(255),
     surname varchar(255),
     patronymic varchar(255),
     post varchar(255),

     primary key (id)
);