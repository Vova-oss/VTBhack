-- ОТДЕЛ
create table department (
    id serial,
    name varchar(255),
    account_id integer not null,

    primary key (id),
    foreign key (account_id) references account (id)

);