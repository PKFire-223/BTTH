import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public void initDatabase() throws SQLException {
        createProductTable();
        seedProductsIfEmpty();
    }

    private void createProductTable() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS products (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                brand TEXT NOT NULL,
                price REAL NOT NULL CHECK(price >= 0),
                image_path TEXT NOT NULL,
                description TEXT
            );
            """;

        try (Connection connection = DatabaseManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    private void seedProductsIfEmpty() throws SQLException {
        String countSql = "SELECT COUNT(*) AS total FROM products";

        try (Connection connection = DatabaseManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(countSql)) {

            if (resultSet.next() && resultSet.getInt("total") > 0) {
                return;
            }
        }

        List<Product> products = List.of(
            new Product("4DFWD PULSE SHOES", "Adidas", 160.00, "img1.png", "This product is excluded from all promotional discounts and offers."),
            new Product("FORUM MID SHOES", "Adidas", 100.00, "img2.png", "This product is excluded from all promotional discounts and offers."),
            new Product("SUPERNOVA SHOES", "Adidas", 150.00, "img3.png", "NMD City Stock 2"),
            new Product("Adidas", "Adidas", 160.00, "img4.png", "NMD City Stock 2"),
            new Product("Adidas NMD", "Adidas", 120.00, "img5.png", "NMD City Stock 2"),
            new Product("4DFWD PULSE RED", "Adidas", 160.00, "img6.png", "This product is excluded from all promotional discounts and offers."),
            new Product("4DFWD PULSE GREEN", "Adidas", 160.00, "img1.png", "This product is excluded from all promotional discounts and offers."),
            new Product("FORUM MID BLUE", "Adidas", 100.00, "img2.png", "This product is excluded from all promotional discounts and offers.")
        );

        String insertSql = """
            INSERT INTO products(name, brand, price, image_path, description)
            VALUES (?, ?, ?, ?, ?)
            """;

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {

            for (Product product : products) {
                preparedStatement.setString(1, product.getName());
                preparedStatement.setString(2, product.getBrand());
                preparedStatement.setDouble(3, product.getPrice());
                preparedStatement.setString(4, product.getImagePath());
                preparedStatement.setString(5, product.getDescription());
                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
        }
    }

    public List<Product> findAll() throws SQLException {
        String sql = """
            SELECT id, name, brand, price, image_path, description
            FROM products
            ORDER BY id
            """;

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            return mapResultSetToProducts(resultSet);
        }
    }

    public List<Product> search(String keyword) throws SQLException {
        if (keyword == null || keyword.isBlank()) {
            return findAll();
        }

        String sql = """
            SELECT id, name, brand, price, image_path, description
            FROM products
            WHERE LOWER(name) LIKE LOWER(?)
               OR LOWER(brand) LIKE LOWER(?)
               OR LOWER(description) LIKE LOWER(?)
            ORDER BY id
            """;

        String likeKeyword = "%" + keyword.trim() + "%";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, likeKeyword);
            preparedStatement.setString(2, likeKeyword);
            preparedStatement.setString(3, likeKeyword);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return mapResultSetToProducts(resultSet);
            }
        }
    }

    private List<Product> mapResultSetToProducts(ResultSet resultSet) throws SQLException {
        List<Product> products = new ArrayList<>();

        while (resultSet.next()) {
            products.add(new Product(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("brand"),
                resultSet.getDouble("price"),
                resultSet.getString("image_path"),
                resultSet.getString("description")
            ));
        }

        return products;
    }
}
