drop table databasechangeloglock;
drop table databasechangelog;

drop table refresh_token;
drop table role_manager;
drop table role;
drop table transaction;
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

select * from transaction