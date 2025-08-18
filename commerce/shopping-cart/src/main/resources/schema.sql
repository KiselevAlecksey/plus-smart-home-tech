CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE SCHEMA IF NOT EXISTS shopping_store;

-- Таблица: shopping_cart (корзины покупок)
CREATE TABLE IF NOT EXISTS shopping_store.shopping_cart (
    shopping_cart_id UUID DEFAULT gen_random_uuid() PRIMARY KEY NOT NULL,
    user_name VARCHAR(256) NOT NULL,
    state VARCHAR(128) NOT NULL
);

COMMENT ON TABLE shopping_store.shopping_cart IS 'Содержит информацию о корзинах покупок';
COMMENT ON COLUMN shopping_store.shopping_cart.shopping_cart_id IS 'Уникальный идентификатор корзины';
COMMENT ON COLUMN shopping_store.shopping_cart.user_name IS 'Имя пользователя, владельца корзины';
COMMENT ON COLUMN shopping_store.shopping_cart.state IS 'Состояние корзины';

-- Таблица: cart_products (товары в корзинах)
CREATE TABLE IF NOT EXISTS shopping_store.cart_products (
    product_id UUID PRIMARY KEY NOT NULL,
    shopping_cart_id UUID NOT NULL,
    quantity BIGINT NOT NULL,
    CONSTRAINT fk_shopping_cart
        FOREIGN KEY (shopping_cart_id)
        REFERENCES shopping_store.shopping_cart(shopping_cart_id)
        ON DELETE CASCADE,
    CONSTRAINT unique_cart_product UNIQUE (shopping_cart_id, product_id)
);

COMMENT ON TABLE shopping_store.cart_products IS 'Содержит информацию о товарах в корзинах';
COMMENT ON COLUMN shopping_store.cart_products.product_id IS 'Уникальный идентификатор товара в корзине';
COMMENT ON COLUMN shopping_store.cart_products.shopping_cart_id IS 'Ссылка на корзину';
COMMENT ON COLUMN shopping_store.cart_products.quantity IS 'Количество данного товара в корзине';