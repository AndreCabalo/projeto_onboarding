CREATE TABLE carts (
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    creation_date DATE,
    total_value DECIMAL(10,2)
);

CREATE TABLE carts_items (
    cart_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    FOREIGN KEY (cart_id) REFERENCES carts(id),
    FOREIGN KEY (item_id) REFERENCES items(id)
);