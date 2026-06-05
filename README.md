# Bài thực hành 04 - Shoe Store JavaFX + SQLite

Dựa trên Bài thực hành 03, project đã được chỉnh để đáp ứng yêu cầu Bài thực hành 04: xây dựng CSDL lưu thông tin sản phẩm và truy vấn sản phẩm từ CSDL.

## Chức năng đã làm

- Thiết kế giao diện bán sản phẩm bằng JavaFX giống BTTH3.
- Click chọn sản phẩm ở danh sách bên phải để đổi sản phẩm hiển thị bên trái.
- Có hiệu ứng fade khi đổi sản phẩm.
- Thêm CSDL SQLite `shoestore.db` để lưu sản phẩm.
- Khi chạy lần đầu, chương trình tự tạo bảng `products` và tự thêm dữ liệu mẫu.
- Danh sách sản phẩm không còn lấy từ `generateMockData()` mà được truy vấn từ bảng `products` bằng JDBC.
- Có ô tìm kiếm để truy vấn sản phẩm theo tên, hãng hoặc mô tả.

## Cấu trúc chính

```text
src/main/java/
├── DatabaseManager.java   # Tạo kết nối SQLite
├── Product.java           # Model sản phẩm
├── ProductDAO.java        # Tạo bảng, seed dữ liệu, truy vấn CSDL
└── ShoeStoreApp.java      # Giao diện JavaFX và gọi DAO

database/products.sql      # Script SQL tham khảo
resources/images/          # Ảnh sản phẩm
pom.xml                    # Maven dependencies: JavaFX + SQLite JDBC
```

## Cách chạy

Yêu cầu máy đã cài JDK 17 và Maven.

```bash
mvn clean javafx:run
```

Sau khi chạy lần đầu, file `shoestore.db` sẽ được tạo ở thư mục gốc project.

## Các điểm cần trình bày khi nộp

1. Dữ liệu sản phẩm được lưu trong bảng `products`.
2. `ProductDAO.initDatabase()` tạo bảng và seed dữ liệu mẫu nếu bảng đang rỗng.
3. `ProductDAO.findAll()` truy vấn toàn bộ sản phẩm để hiển thị lên giao diện.
4. `ProductDAO.search(keyword)` truy vấn sản phẩm theo từ khóa bằng `PreparedStatement`.
5. `ShoeStoreApp` gọi DAO để lấy dữ liệu từ CSDL thay vì dùng dữ liệu mock trong code.

## Nộp bài

1. Push toàn bộ project lên GitHub.
2. Tạo file `<MSSV>.txt`.
3. Dán link GitHub repository vào file txt.
4. Upload file txt lên website môn học.
