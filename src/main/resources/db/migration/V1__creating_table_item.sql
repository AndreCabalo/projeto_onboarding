CREATE TABLE items (

    id BIGINT PRIMARY KEY NOT NULL auto_increment,
    name VARCHAR(100) NOT NULL,
    price DOUBLE NOT NULL,
    amount INT NOT NULL

);

CREATE TABLE carts (
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
        creation_date DATE
);

CREATE TABLE carts_items (
    cart_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    PRIMARY KEY (cart_id, item_id),
    CONSTRAINT fk_cart
        FOREIGN KEY (cart_id)
        REFERENCES carts (id)
        ON DELETE CASCADE,
    CONSTRAINT fk_item
        FOREIGN KEY (item_id)
        REFERENCES items (id)
        ON DELETE CASCADE
);

