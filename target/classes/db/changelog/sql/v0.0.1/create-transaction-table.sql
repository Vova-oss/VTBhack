-- ТРАНЗАКЦИЯ
create table transaction (
    id serial,
    card_id integer not null,
    category varchar(255),
    date date,
    value bigint,

    primary key (id),
    foreign key (card_id) references card (id)

);