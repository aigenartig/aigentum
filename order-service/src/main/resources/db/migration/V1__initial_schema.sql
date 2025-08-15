-- Create custom enum types
CREATE TYPE order_status AS ENUM ('CART', 'PENDING', 'PROCESSING', 'SHIPPED', 'DELIVERED', 'CANCELLED');

-- Create orders table
CREATE TABLE orders (
    id UUID PRIMARY KEY,
    order_number VARCHAR(255) NOT NULL UNIQUE,
    user_id UUID NOT NULL,
    status order_status NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    shipping_address TEXT,
    billing_address TEXT,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

-- Create order_items table
CREATE TABLE order_items (
    id UUID PRIMARY KEY,
    order_id UUID NOT NULL,
    product_id UUID NOT NULL,
    quantity INTEGER NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    CONSTRAINT fk_order
        FOREIGN KEY(order_id)
        REFERENCES orders(id)
);
