CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE SCHEMA IF NOT EXISTS shopping_store;

-- Таблица адресов
CREATE TABLE IF NOT EXISTS shopping_store.addresses (
    address_id UUID DEFAULT gen_random_uuid() PRIMARY KEY NOT NULL,
    country VARCHAR(100) NOT NULL,
    city VARCHAR(100) NOT NULL,
    street VARCHAR(100) NOT NULL,
    house VARCHAR(20) NOT NULL,
    flat VARCHAR(20),
    is_warehouse BOOLEAN NOT NULL DEFAULT false
);

-- Таблица доставок
CREATE TABLE IF NOT EXISTS shopping_store.deliveries (
    delivery_id UUID DEFAULT gen_random_uuid() PRIMARY KEY NOT NULL,
    from_address_id UUID NOT NULL,
    to_address_id UUID NOT NULL,
    order_id UUID NOT NULL,
    state VARCHAR(20) NOT NULL,
    CONSTRAINT fk_from_address FOREIGN KEY (from_address_id)
        REFERENCES shopping_store.addresses(address_id),
    CONSTRAINT fk_to_address FOREIGN KEY (to_address_id)
        REFERENCES shopping_store.addresses(address_id),
    CONSTRAINT fk_order FOREIGN KEY (order_id)
        REFERENCES shopping_store.orders(order_id)
);

-- Индексы для улучшения производительности
CREATE INDEX IF NOT EXISTS idx_deliveries_order_id ON shopping_store.deliveries(order_id);
CREATE INDEX IF NOT EXISTS idx_deliveries_state ON shopping_store.deliveries(state);
CREATE INDEX IF NOT EXISTS idx_deliveries_from_address ON shopping_store.deliveries(from_address_id);
CREATE INDEX IF NOT EXISTS idx_deliveries_to_address ON shopping_store.deliveries(to_address_id);