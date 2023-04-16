select setval('s_sale', (select max(id) from sale));

select setval('s_supply', (select max(id) from supply));