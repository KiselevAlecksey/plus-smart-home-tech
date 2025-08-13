CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE SCHEMA IF NOT EXISTS shopping_store;

-- Таблица: orders (заказы)
CREATE TABLE IF NOT EXISTS shopping_store.orders (
    order_id UUID DEFAULT gen_random_uuid() PRIMARY KEY NOT NULL,
    shopping_cart_id UUID NOT NULL,
    user_name VARCHAR(256) NOT NULL,
    payment_id UUID NOT NULL DEFAULT gen_random_uuid(),
    delivery_id UUID NOT NULL DEFAULT gen_random_uuid(),
    state VARCHAR(128) NOT NULL,
    fragile BOOLEAN NOT NULL DEFAULT false,
    delivery_weight NUMERIC(12, 2),
    delivery_volume NUMERIC(12, 2),
    total_price NUMERIC(12, 2),
    delivery_price NUMERIC(12, 2),
    product_price NUMERIC(12, 2)
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
CREATE TABLE IF NOT EXISTS shopping_store.order_cart_products (
    cart_product_id UUID DEFAULT gen_random_uuid() PRIMARY KEY NOT NULL,
    product_id UUID NOT NULL,
    order_id UUID NOT NULL,
    shopping_cart_id UUID ,
    quantity BIGINT NOT NULL,
    price NUMERIC(12, 2),
    CONSTRAINT fk_order
        FOREIGN KEY (order_id)
        REFERENCES shopping_store.orders(order_id)
        ON DELETE CASCADE
);

COMMENT ON TABLE shopping_store.order_cart_products IS 'Содержит информацию о товарах в корзинах';
COMMENT ON COLUMN shopping_store.order_cart_products.product_id IS 'Уникальный идентификатор товара в корзине';
COMMENT ON COLUMN shopping_store.order_cart_products.shopping_cart_id IS 'Ссылка на корзину';
COMMENT ON COLUMN shopping_store.order_cart_products.quantity IS 'Количество данного товара в корзине';

CREATE TABLE IF NOT EXISTS shopping_store.addresses (
    address_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    country VARCHAR(100) NOT NULL,
    city VARCHAR(100) NOT NULL,
    street VARCHAR(200) NOT NULL,
    house VARCHAR(20) NOT NULL,
    flat VARCHAR(20),
    order_id UUID NOT NULL UNIQUE,
    CONSTRAINT fk_order_address
        FOREIGN KEY (order_id)
        REFERENCES shopping_store.orders(order_id)
        ON DELETE CASCADE
);

COMMENT ON TABLE shopping_store.addresses IS 'Содержит адреса доставки заказов';
COMMENT ON COLUMN shopping_store.addresses.address_id IS 'Уникальный идентификатор адреса';
COMMENT ON COLUMN shopping_store.addresses.country IS 'Страна доставки';
COMMENT ON COLUMN shopping_store.addresses.city IS 'Город доставки';
COMMENT ON COLUMN shopping_store.addresses.street IS 'Улица доставки';
COMMENT ON COLUMN shopping_store.addresses.house IS 'Номер дома';
COMMENT ON COLUMN shopping_store.addresses.flat IS 'Номер квартиры';
