CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE SCHEMA IF NOT EXISTS shopping_store;

-- Table: products

CREATE TABLE IF NOT EXISTS shopping_store.products (
    product_id UUID DEFAULT gen_random_uuid() PRIMARY KEY NOT NULL,
    product_name VARCHAR(256) NOT NULL,
    description TEXT NOT NULL,
    image_src TEXT,
    quantity_state VARCHAR(128) NOT NULL,
    product_state VARCHAR(128) NOT NULL,
    product_category VARCHAR(128),
    price NUMERIC(12, 2) NOT NULL
);

COMMENT ON TABLE shopping_store.products IS 'Содержит информацию о товарах';
COMMENT ON COLUMN shopping_store.products.product_id IS 'Уникальный идентификатор';
COMMENT ON COLUMN shopping_store.products.product_name IS 'Наименование товара';
COMMENT ON COLUMN shopping_store.products.description IS 'Описание товара';
COMMENT ON COLUMN shopping_store.products.image_src IS 'Ссылка на картинку во внешнем хранилище или SVG';
COMMENT ON COLUMN shopping_store.products.quantity_state IS 'Статус остатка товара';
COMMENT ON COLUMN shopping_store.products.product_state IS 'Статус товара';
COMMENT ON COLUMN shopping_store.products.product_category IS 'Категория товара';
COMMENT ON COLUMN shopping_store.products.price IS 'Цена товара';