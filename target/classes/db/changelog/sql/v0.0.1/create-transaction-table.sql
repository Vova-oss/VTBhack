-- ТРАНЗАКЦИЯ
create table transaction (
    id serial,
    account_id integer,
    card_id integer,
    purpose varchar(255),
    category varchar(255),
    date date,
    time time,
    value bigint,

    primary key (id),
    foreign key (account_id) references account (id),
    foreign key (card_id) references card (id)

);