-- СЧЁТ
create table account (
    id serial,
    manager_id integer not null,
    account_number varchar(255) not null,
    current_account bigint,
    currency varchar(10),
    total_balance bigint,
    allocated_funds bigint,
    monthly_expenses bigint,

    primary key (id),
    foreign key (manager_id) references manager (id)

);