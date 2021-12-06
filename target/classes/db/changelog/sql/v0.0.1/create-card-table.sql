-- КАРТА
create table card (
    id serial,
    worker_id integer not null,
    payment_system varchar(255),
    card_number varchar(16),
    account bigint,
    type varchar(255),
    purpose_of_creation varchar(255),
    status varchar(100),
    "limit" bigint,
    term bigint,
    remains bigint,
    auto_update bool,
    currency varchar(10),

    primary key (id),
    foreign key (worker_id) references worker (id)
);