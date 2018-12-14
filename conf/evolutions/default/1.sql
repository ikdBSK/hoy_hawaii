# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table enquetes (
  id                            bigint auto_increment not null,
  name                          varchar(255) not null,
  sex                           varchar(6) not null,
  impressions                   varchar(255) not null,
  registered_at                 timestamp not null,
  updated_at                    timestamp not null,
  constraint ck_enquetes_sex check ( sex in ('male','female')),
  constraint pk_enquetes primary key (id)
);

create table tasks (
  id                            bigint auto_increment not null,
  title                         varchar(255) not null,
  done                          boolean default false not null,
  registered_at                 timestamp not null,
  constraint pk_tasks primary key (id)
);


# --- !Downs

drop table if exists enquetes;

drop table if exists tasks;

