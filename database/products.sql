-- CSDL cho Bài thực hành 04
-- Ứng dụng JavaFX tự tạo file shoestore.db và seed dữ liệu khi chạy lần đầu.

CREATE TABLE IF NOT EXISTS products (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    brand TEXT NOT NULL,
    price REAL NOT NULL CHECK(price >= 0),
    image_path TEXT NOT NULL,
    description TEXT
);

INSERT INTO products(name, brand, price, image_path, description) VALUES
('4DFWD PULSE SHOES', 'Adidas', 160.00, 'img1.png', 'This product is excluded from all promotional discounts and offers.'),
('FORUM MID SHOES', 'Adidas', 100.00, 'img2.png', 'This product is excluded from all promotional discounts and offers.'),
('SUPERNOVA SHOES', 'Adidas', 150.00, 'img3.png', 'NMD City Stock 2'),
('Adidas', 'Adidas', 160.00, 'img4.png', 'NMD City Stock 2'),
('Adidas NMD', 'Adidas', 120.00, 'img5.png', 'NMD City Stock 2'),
('4DFWD PULSE RED', 'Adidas', 160.00, 'img6.png', 'This product is excluded from all promotional discounts and offers.'),
('4DFWD PULSE GREEN', 'Adidas', 160.00, 'img1.png', 'This product is excluded from all promotional discounts and offers.'),
('FORUM MID BLUE', 'Adidas', 100.00, 'img2.png', 'This product is excluded from all promotional discounts and offers.');
