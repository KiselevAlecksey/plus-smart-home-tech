CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE SCHEMA IF NOT EXISTS shopping_store;

-- Таблица: payments (платежи)
CREATE TABLE IF NOT EXISTS shopping_store.payments (
    payment_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    total_payment NUMERIC(12, 2) NOT NULL,
    delivery_total NUMERIC(12, 2),
    fee_total NUMERIC(12, 2),
    state VARCHAR(50) NOT NULL,
    order_id UUID NOT NULL,
    CONSTRAINT fk_order_payment
        FOREIGN KEY (order_id)
        REFERENCES shopping_store.orders(order_id)
        ON DELETE CASCADE
);

COMMENT ON TABLE shopping_store.payments IS 'Содержит информацию о платежах';
COMMENT ON COLUMN shopping_store.payments.payment_id IS 'Уникальный идентификатор платежа';
COMMENT ON COLUMN shopping_store.payments.total_payment IS 'Общая сумма платежа';
COMMENT ON COLUMN shopping_store.payments.delivery_total IS 'Сумма за доставку';
COMMENT ON COLUMN shopping_store.payments.fee_total IS 'Сумма комиссий';
COMMENT ON COLUMN shopping_store.payments.state IS 'Статус платежа';
COMMENT ON COLUMN shopping_store.payments.order_id IS 'Ссылка на заказ';