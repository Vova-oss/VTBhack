-- ТРАНЗАКЦИЯ
create table transaction (
    id serial,
    account_id integer,
    card_id integer,
    category varchar(255),
    date date,
    value bigint,

    primary key (id),
    foreign key (account_id) references account (id),
    foreign key (card_id) references card (id)

);