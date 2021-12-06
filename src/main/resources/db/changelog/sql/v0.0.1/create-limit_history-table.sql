-- ИСТОРИЯ ЛИМИТОВ
create table limit_history (
    id serial,
    card_id integer not null,
    "limit" bigint,
    term bigint,
    remains bigint,
    auto_update bool,

    primary key (id),
    foreign key (card_id) references card (id)
);