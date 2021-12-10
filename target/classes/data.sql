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



-------------------- transactionHistory ---------------------------------------------------------------------------------
select * from
(select
        date
        , time
        , purpose
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
where w.id = 1
and date + time <= now()


union all
select
        date
        , time
        , purpose
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

---------transactionHistoryByWorkerId -----------------------------------------------------------------------------------
select
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
where w.id = 1
  and date + time <= now()

order by date, time
limit 10 offset 10;

---------------- top spending categories --------------------------------------------------------------------------------


select SUM(value), category
from (
      select *
      from (
               select category,
                      value * (-1) value,
                      purpose
               from transaction
                        join card c on transaction.card_id = c.id
                        join worker w on c.worker_id = w.id
                        join department d on w.department_id = d.id
               where d.account_id = 1
                 and value < 0


               union all
               select category,
                      value * (-1),
                      purpose
               from transaction
                        join account a on transaction.account_id = a.id
                        join manager m on a.manager_id = m.id
               where a.id = 1
                 and value < 0
           ) as tsc
      where purpose like '%'
  ) as tscTwoCol
group by category;


--------------------- topSpendingCategoriesByWorker -----------------------------------------------------------------------

select SUM(value), category
from (
         select *
         from (
                  select category,
                         value * (-1) value,
                         purpose
                  from transaction
                           join card c on transaction.card_id = c.id
                           join worker w on c.worker_id = w.id
                           join department d on w.department_id = d.id
                  where w.id = 1
                    and value < 0
              ) as tsc
         where purpose like '%'
     ) as tscTwoCol
group by category;

-------------------------------------------- Выделено средст --------------

select sum(account)
from card
join worker w on card.worker_id = w.id
join department d on w.department_id = d.id
where account_id = 1;

---------------------------- Траты за месяц ----------------------------------------------

select SUM(t.value) * (-1) value
    from transaction t
join card c on c.id = t.card_id
join worker w on c.worker_id = w.id
join department d on w.department_id = d.id
where t.card_id is not null
and d.account_id = ?
and t.value < 0
and to_char(t.date, 'YYYY') = to_char(NOW(), 'YYYY')
and to_char(t.date, 'MM') = to_char(NOW(), 'MM')
and t.date + t.time <= now();

--------------------------- Активных карт ----------------------
select count(*)
    from card
    join worker w on card.worker_id = w.id
    join department d on d.id = w.department_id
where d.account_id = 1
and status = 'ACTIVE';

-------------------------------Сотрудников --------------------------
select count(*)
     from worker
    join department d on worker.department_id = d.id
where d.account_id = 1;


-----------------------------График расходов ----------------------------------
select SUM(value), date
from (
      select date,
             value * (-1) value,
             purpose
      from transaction
               join card c on transaction.card_id = c.id
               join worker w on c.worker_id = w.id
               join department d on w.department_id = d.id
      where d.account_id = 1
        and value < 0

      union all
      select date,
             value * (-1),
             purpose
      from transaction
               join account a on transaction.account_id = a.id
               join manager m on a.manager_id = m.id
      where a.id = 1
        and value < 0
  ) as tsc

group by date
order by date;



------------------------- expensesShuduled ----------------------

select SUM(val), date
from (
         select date,
                transaction.value * (-1) val,
                purpose
         from transaction
                  join card c on transaction.card_id = c.id
                  join worker w on c.worker_id = w.id
                  join department d on w.department_id = d.id
         where d.account_id = 1
           and value < 0

         union all
         select transaction.date,
             transaction.value * (-1),
             purpose
         from transaction
             join account a on transaction.account_id = a.id
             join manager m on a.manager_id = m.id
         where a.id = ?
           and value < 0
            and purpose != 'Банковская карта'
               ) as tsc

group by date
order by date;


----------------------- CardDAO findAllByAccountId ------------------------------
select card.* from card
join worker w on card.worker_id = w.id
join department d on w.department_id = d.id
where d.account_id = 1;
