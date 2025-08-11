CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE SCHEMA IF NOT EXISTS shopping_store;

-- Таблица: orders (заказы)
CREATE TABLE IF NOT EXISTS shopping_store.orders (
    order_id UUID PRIMARY KEY NOT NULL,
    shopping_cart_id UUID NOT NULL,
    user_name VARCHAR(256) NOT NULL UNIQUE,
    payment_id UUID NOT NULL,
    delivery_id UUID NOT NULL,
    state VARCHAR(128) NOT NULL,
    fragile BOOLEAN NOT NULL DEFAULT false,
    delivery_weight FLOAT8 NOT NULL,
    delivery_volume FLOAT8 NOT NULL,
    total_price NUMERIC(12, 2) NOT NULL,
    delivery_price NUMERIC(12, 2) NOT NULL,
    product_price NUMERIC(12, 2) NOT NULL
);

COMMENT ON TABLE shopping_store.orders IS 'Содержит информацию о заказах';
COMMENT ON COLUMN shopping_store.orders.order_id IS 'Уникальный идентификатор заказа';
COMMENT ON COLUMN shopping_store.orders.shopping_cart_id IS 'Уникальный идентификатор корзины';
COMMENT ON COLUMN shopping_store.orders.payment_id IS 'Уникальный идентификатор оплаты';
COMMENT ON COLUMN shopping_store.orders.delivery_id IS 'Уникальный идентификатор доставки';
COMMENT ON COLUMN shopping_store.orders.state IS 'Состояние заказа';
COMMENT ON COLUMN shopping_store.orders.fragile IS 'Хрупкость';
COMMENT ON COLUMN shopping_store.orders.delivery_weight IS 'Вес доставки';
COMMENT ON COLUMN shopping_store.orders.delivery_volume IS 'Объем доставки';
COMMENT ON COLUMN shopping_store.orders.total_price IS 'Общая цена заказа';
COMMENT ON COLUMN shopping_store.orders.delivery_price IS 'Цена доставки';
COMMENT ON COLUMN shopping_store.orders.product_price IS 'Цена товара';

-- Таблица: cart_products (товары в корзинах)
CREATE TABLE IF NOT EXISTS shopping_store.cart_products (
    product_id UUID PRIMARY KEY NOT NULL,
    order_id UUID NOT NULL,
    shopping_cart_id UUID NOT NULL,
    quantity BIGINT NOT NULL,
    price NUMERIC(12, 2) NOT NULL
    CONSTRAINT fk_shopping_cart
        FOREIGN KEY (order_id)
        REFERENCES shopping_store.orders(order_id)
        ON DELETE CASCADE,
    CONSTRAINT unique_cart_product UNIQUE (order_id, product_id)
);

COMMENT ON TABLE shopping_store.cart_products IS 'Содержит информацию о товарах в корзинах';
COMMENT ON COLUMN shopping_store.cart_products.product_id IS 'Уникальный идентификатор товара в корзине';
COMMENT ON COLUMN shopping_store.cart_products.shopping_cart_id IS 'Ссылка на корзину';
COMMENT ON COLUMN shopping_store.cart_products.quantity IS 'Количество данного товара в корзине';