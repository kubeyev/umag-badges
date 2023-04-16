create sequence s_sale;

create sequence s_supply;

create table sale (
    id        bigint   not null default nextval('s_sale'::regclass),
    barcode   bigint   not null,
    quantity  integer  not null default 1,
    price     integer  not null default 0,
    time      timestamp not null default now(),
    margin    integer,
    margin_time timestamp
);

create table supply (
    id          bigint   not null default nextval('s_supply'::regclass),
    barcode     bigint   not null,
    quantity    integer  not null default 1,
    price       integer  not null default 0,
    time        timestamp not null default now()
);

create index sale_time_barcode on sale(barcode, time);

create index supply_time_barcode on supply(barcode, time);