-- СОТРУДНИК
create table worker (
    id serial,
    name varchar(255),
    surname varchar(255),
    patronymic varchar(255),
    department_id integer not null,

    primary key (id),
    foreign key (department_id) references department (id)

);