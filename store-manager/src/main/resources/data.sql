INSERT INTO products(id, category, availability_status) VALUES ('a1b2c3d4-e5f6-7890-abcd-ef1234567890', 'ELECTRONICS', 'IN_STOCK');
INSERT INTO products(id, category, availability_status) VALUES ('b2c3d4e5-f6a7-8901-bcde-f12345678901', 'ELECTRONICS', 'IN_STOCK');
INSERT INTO products(id, category, availability_status) VALUES ('c3d4e5f6-a7b8-9012-cdef-123456789012', 'ELECTRONICS', 'LOW_STOCK');

INSERT INTO product_details(id, title, description, price, discount, rating, stock, brand) VALUES ('a1b2c3d4-e5f6-7890-abcd-ef1234567890', 'Laptop', 'High performance laptop', 8000.41, 5.00, 4.5, 50, 'Dell');
INSERT INTO product_details(id, title, description, price, discount, rating, stock, brand) VALUES ('b2c3d4e5-f6a7-8901-bcde-f12345678901', 'TV', '4K Ultra HD Smart TV', 4599.23, 10.00, 4.2, 30, 'Samsung');
INSERT INTO product_details(id, title, description, price, discount, rating, stock, brand) VALUES ('c3d4e5f6-a7b8-9012-cdef-123456789012', 'Desktop', 'Gaming desktop computer', 89573.89, 0.00, 4.8, 5, 'Asus');

INSERT INTO reviews(rating, comment, date, reviewer_name, reviewer_email, product_id) VALUES (5, 'Excellent laptop, very fast!', '2025-01-15T10:30:00+00:00', 'John Doe', 'john@example.com', 'a1b2c3d4-e5f6-7890-abcd-ef1234567890');
INSERT INTO reviews(rating, comment, date, reviewer_name, reviewer_email, product_id) VALUES (4, 'Great value for money', '2025-02-20T14:00:00+00:00', 'Jane Smith', 'jane@example.com', 'a1b2c3d4-e5f6-7890-abcd-ef1234567890');
INSERT INTO reviews(rating, comment, date, reviewer_name, reviewer_email, product_id) VALUES (4, 'Amazing picture quality', '2025-03-10T09:15:00+00:00', 'Bob Wilson', 'bob@example.com', 'b2c3d4e5-f6a7-8901-bcde-f12345678901');
