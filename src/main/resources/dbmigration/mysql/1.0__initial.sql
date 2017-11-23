-- apply changes
create table item (
  item_id                       integer auto_increment not null,
  name                          varchar(255) not null,
  price                         double not null,
  description                   varchar(255) not null,
  constraint uq_item_name unique (name),
  constraint pk_item primary key (item_id)
);

