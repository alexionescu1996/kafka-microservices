CREATE TABLE products (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    category VARCHAR(50),
    availability_status VARCHAR(50)
);

CREATE TABLE product_details (
    id UUID PRIMARY KEY,
    title VARCHAR(50),
    description VARCHAR(250),
    price DECIMAL(10, 2),
    discount DECIMAL(10, 2),
    rating DECIMAL(3, 2),
    stock INT,
    brand VARCHAR(100),
    FOREIGN KEY (id) REFERENCES products(id)
);

CREATE TABLE reviews (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    rating INT,
    comment VARCHAR(500),
    date TIMESTAMP WITH TIME ZONE,
    reviewer_name VARCHAR(100),
    reviewer_email VARCHAR(100),
    product_id UUID,
    FOREIGN KEY (product_id) REFERENCES products(id)
);
