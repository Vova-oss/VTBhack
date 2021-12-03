-- СВЯЗЬ РОЛЕЙ И МЕНЕДЖЕРОВ
create table role_manager (
      role_id integer,
      manager_id integer,

      foreign key (role_id) references role (id),
      foreign key (manager_id) references manager (id)
);