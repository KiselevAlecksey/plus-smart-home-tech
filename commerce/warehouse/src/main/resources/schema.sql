create EXTENSION IF NOT EXISTS pgcrypto;

create SCHEMA IF NOT EXISTS shopping_store;

CREATE TABLE IF NOT EXISTS shopping_store.products_in_warehouse (
    product_id UUID PRIMARY KEY NOT NULL,
    quantity BIGINT NOT NULL DEFAULT 0,
    fragile BOOLEAN NOT NULL DEFAULT false,
    width FLOAT8 NOT NULL,
    height FLOAT8 NOT NULL,
    depth FLOAT8 NOT NULL,
    weight FLOAT8 NOT NULL
);

comment on table shopping_store.products_in_warehouse is 'Содержит информацию о товарах в корзинах';
comment on column shopping_store.products_in_warehouse.product_id is 'Уникальный идентификатор товара в корзине';
comment on column shopping_store.products_in_warehouse.quantity is 'Количество данного товара в корзине';