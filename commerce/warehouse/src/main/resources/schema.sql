create EXTENSION IF NOT EXISTS pgcrypto;

create SCHEMA IF NOT EXISTS shopping_store;

CREATE TABLE IF NOT EXISTS shopping_store.products_in_warehouse (
    product_id UUID PRIMARY KEY NOT NULL,
    quantity BIGINT NOT NULL DEFAULT 0,
    fragile BOOLEAN NOT NULL DEFAULT false,
    width FLOAT8 NOT NULL,
    height FLOAT8 NOT NULL,
    depth FLOAT8 NOT NULL,
    weight FLOAT8 NOT NULL,
    price NUMERIC(12, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS shopping_store.order_bookings (
    order_booking_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id UUID NOT NULL
);

CREATE TABLE IF NOT EXISTS shopping_store.booked_product_items (
    booked_item_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_booking_id UUID NOT NULL REFERENCES shopping_store.order_bookings(order_booking_id) ON DELETE CASCADE,
    product_id UUID NOT NULL REFERENCES shopping_store.products_in_warehouse(product_id),
    delivery_id UUID,
    quantity BIGINT NOT NULL,
    CONSTRAINT unique_booking_product UNIQUE (order_booking_id, product_id)
);

comment on table shopping_store.products_in_warehouse is 'Содержит информацию о товарах в корзинах';
comment on column shopping_store.products_in_warehouse.product_id is 'Уникальный идентификатор товара в корзине';
comment on column shopping_store.products_in_warehouse.quantity is 'Количество данного товара в корзине';