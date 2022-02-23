drop table if exists pets;

create table pets (
    id numeric(5) primary key,
    name varchar(30) not null,
    age numeric(3) not null,
    species varchar(30) not null
)

create sequence seq_pet;

insert into pets values (nextval(seq_pet), 'bob', 10, 'chinchilla');