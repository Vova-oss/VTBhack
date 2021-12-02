-- REFRESH-ТОКЕН
create table refresh_token (
     id serial,
     manager_id serial,
     token varchar (255) not null,
     expiry_date varchar(255) not null,

     primary key (id),
     foreign key (manager_id) references manager (id)
);