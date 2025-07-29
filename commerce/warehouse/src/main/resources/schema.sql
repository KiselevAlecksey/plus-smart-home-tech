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

--CREATE TABLE IF NOT EXISTS shopping_store.products_in_warehouse (
--    product_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
--    quantity BIGINT NOT NULL CHECK (quantity >= 0),
--    fragile BOOLEAN NOT NULL DEFAULT false,
--    width NUMERIC(10,2) NOT NULL CHECK (width > 0),
--    height NUMERIC(10,2) NOT NULL CHECK (height > 0),
--    depth NUMERIC(10,2) NOT NULL CHECK (depth > 0),
--    weight NUMERIC(10,2) NOT NULL CHECK (weight > 0),
--    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
--    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
--);
--
--CREATE INDEX IF NOT EXISTS idx_warehouse_product ON shopping_store.products_in_warehouse (product_id);