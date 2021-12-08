drop table databasechangeloglock;
drop table databasechangelog;

drop table refresh_token;
drop table role_manager;
drop table role;
drop table transaction;
drop table limit_history;
drop table card;
drop table worker;
drop table department;
drop table account;
drop table manager;

delete from card where id > 0;
delete from department where id > 1;



select * from role;
select * from card;
select * from worker;
select * from manager;
select * from account;
select * from department;
select * from transaction;
select * from role_manager;
select * from refresh_token;

select worker.*
    from worker
    join department d on worker.department_id = d.id
    where d.account_id = 1;

select sum(account)
from card
where worker_id = 3;

update account set current_account = 2821650 where id = 1;
select * from account;

update card set remains = 200 where id = 1;

select * from
(select
        date
        , time
        , category
        , concat(w.surname, ' ', substring(w.name from 1 for 1), '. ', substring(w.patronymic from 1 for 1),'.' ) as fio
        , d.name
        , c.type
        , c.payment_system
        , c.card_number
        , value
        , c.currency
    from transaction
    join card c on transaction.card_id = c.id
    join worker w on c.worker_id = w.id
    join department d on w.department_id = d.id
where d.account_id = 1
and date + time <= now()


union all
select
        date
        , time
        , category
        , concat(m.surname, ' ', substring(m.name from 1 for 1), '. ', substring(m.patronymic from 1 for 1), '.')
        , m.post
        , 'Пополнение карты'
        , 'Счёт'
        , a.account_number
        , value
        , a.currency
    from transaction
    join account a on transaction.account_id = a.id
    join manager m on a.manager_id = m.id
where a.id = 1
and date + time <= now()) as big_table


order by date, time
limit 10 offset 10*?;
