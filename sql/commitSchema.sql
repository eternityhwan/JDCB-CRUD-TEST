drop table member2 if exists;
create table member2 (
                        member_id varchar(10),
                        money integer not null default 0,
                        primary key (member_id)
);
