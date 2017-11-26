-- apply changes
create table item (
  id                            integer auto_increment not null,
  name                          varchar(255) not null,
  price                         double not null,
  description                   varchar(255) not null,
  constraint pk_item primary key (id)
);

