create sequence s_pointer;

create table pointer (
      id            bigint   not null default nextval('s_pointer'::regclass),
      barcode       bigint   not null,
      start_value   timestamp not null default now(),
      end_value     timestamp not null default now(),
      sum           integer  not null default 0
);

create index pointer_start_barcode on pointer(barcode, start_value);