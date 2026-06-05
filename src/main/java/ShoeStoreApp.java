import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.SQLException;
import java.util.List;

public class ShoeStoreApp extends Application {

    private ProductDAO productDAO;
    private ImageView mainImageView;
    private Label mainNameLabel;
    private Label mainPriceLabel;
    private Label mainBrandLabel;
    private Label mainDescLabel;
    private TextField searchField;
    private TilePane gridPane;
    private VBox leftPanel;
    private VBox selectedCard = null;

    @Override
    public void start(Stage primaryStage) {
        productDAO = new ProductDAO();

        try {
            productDAO.initDatabase();
        } catch (SQLException e) {
            showError("Không thể khởi tạo CSDL", e.getMessage());
        }

        // 1. Panel bên trái: chi tiết sản phẩm được chọn
        leftPanel = new VBox(8);
        leftPanel.setPadding(new Insets(30, 20, 20, 20));
        leftPanel.setPrefWidth(350);
        leftPanel.setMaxWidth(350);
        leftPanel.setAlignment(Pos.TOP_LEFT);
        leftPanel.setStyle("-fx-background-color: #ffffff;");

        mainImageView = new ImageView();
        mainImageView.setFitWidth(300);
        mainImageView.setFitHeight(200);
        mainImageView.setPreserveRatio(true);

        VBox imageContainer = new VBox(mainImageView);
        imageContainer.setAlignment(Pos.CENTER);
        imageContainer.setPrefHeight(220);

        Separator separator = new Separator();
        separator.setPadding(new Insets(10, 0, 10, 0));

        mainNameLabel = new Label();
        mainNameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        mainNameLabel.setTextFill(Color.web("#333333"));
        mainNameLabel.setWrapText(true);

        mainPriceLabel = new Label();
        mainPriceLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        mainPriceLabel.setTextFill(Color.web("#444444"));

        mainBrandLabel = new Label();
        mainBrandLabel.setFont(Font.font("Arial", 13));
        mainBrandLabel.setTextFill(Color.web("#666666"));

        mainDescLabel = new Label();
        mainDescLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        mainDescLabel.setTextFill(Color.web("#999999"));
        mainDescLabel.setWrapText(true);

        leftPanel.getChildren().addAll(
            imageContainer,
            separator,
            mainNameLabel,
            mainPriceLabel,
            mainBrandLabel,
            mainDescLabel
        );

        // 2. Panel bên phải: ô tìm kiếm + lưới sản phẩm đọc từ CSDL
        searchField = new TextField();
        searchField.setPromptText("Tìm theo tên, hãng hoặc mô tả...");
        searchField.setPrefWidth(360);
        searchField.setOnAction(e -> searchProducts());

        Button searchButton = new Button("Tìm kiếm");
        searchButton.setOnAction(e -> searchProducts());

        Button showAllButton = new Button("Tất cả sản phẩm");
        showAllButton.setOnAction(e -> {
            searchField.clear();
            loadAllProducts();
        });

        Label titleLabel = new Label("Shoe Store - Products from Database");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        titleLabel.setTextFill(Color.web("#333333"));

        Region topSpacer = new Region();
        HBox.setHgrow(topSpacer, Priority.ALWAYS);

        HBox topBar = new HBox(10, titleLabel, topSpacer, searchField, searchButton, showAllButton);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(20, 30, 0, 30));
        topBar.setStyle("-fx-background-color: #f4f5f7;");

        gridPane = new TilePane();
        gridPane.setPadding(new Insets(20, 30, 30, 30));
        gridPane.setHgap(15);
        gridPane.setVgap(15);
        gridPane.setPrefColumns(4);
        gridPane.setAlignment(Pos.TOP_LEFT);
        gridPane.setStyle("-fx-background-color: #f4f5f7;");

        ScrollPane scrollPane = new ScrollPane(gridPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-control-inner-background: #f4f5f7;");
        scrollPane.setBorder(Border.EMPTY);

        VBox rightPanel = new VBox(topBar, scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        rightPanel.setStyle("-fx-background-color: #f4f5f7;");

        // 3. Layout tổng
        HBox root = new HBox();
        root.setStyle("-fx-background-color: #f4f5f7;");
        root.getChildren().addAll(leftPanel, rightPanel);
        HBox.setHgrow(rightPanel, Priority.ALWAYS);

        loadAllProducts();

        Scene scene = new Scene(root, 1400, 750);
        primaryStage.setTitle("Shoe Store - BTTH4 Database");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadAllProducts() {
        try {
            displayProducts(productDAO.findAll());
        } catch (SQLException e) {
            showError("Không thể truy vấn danh sách sản phẩm", e.getMessage());
        }
    }

    private void searchProducts() {
        try {
            displayProducts(productDAO.search(searchField.getText()));
        } catch (SQLException e) {
            showError("Không thể tìm kiếm sản phẩm", e.getMessage());
        }
    }

    private void displayProducts(List<Product> products) {
        gridPane.getChildren().clear();
        selectedCard = null;

        if (products == null || products.isEmpty()) {
            clearMainData("Không tìm thấy sản phẩm phù hợp.");
            Label emptyLabel = new Label("Không tìm thấy sản phẩm phù hợp trong CSDL.");
            emptyLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
            emptyLabel.setTextFill(Color.web("#666666"));
            gridPane.getChildren().add(emptyLabel);
            return;
        }

        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            VBox card = createProductCard(product);
            gridPane.getChildren().add(card);

            if (i == 0) {
                selectCard(card, product, false);
            }
        }
    }

    private VBox createProductCard(Product product) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(15));
        card.setPrefSize(220, 260);
        card.setMaxSize(220, 260);

        String defaultStyle = "-fx-background-color: #ffffff; -fx-background-radius: 10; -fx-border-color: transparent; -fx-border-width: 2; -fx-cursor: hand;";
        card.setStyle(defaultStyle);

        Label nameLabel = new Label(product.getName());
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        nameLabel.setTextFill(Color.web("#333333"));
        nameLabel.setMaxWidth(190);
        nameLabel.setWrapText(true);

        Label descLabel = new Label(product.getDescription());
        descLabel.setFont(Font.font("Arial", FontWeight.BOLD, 11));
        descLabel.setTextFill(Color.web("#a0a0a0"));
        descLabel.setMaxWidth(190);
        descLabel.setWrapText(true);

        ImageView imageView = new ImageView(loadProductImage(product.getImagePath()));
        imageView.setFitWidth(170);
        imageView.setFitHeight(120);
        imageView.setPreserveRatio(true);

        VBox imageBox = new VBox(imageView);
        imageBox.setAlignment(Pos.CENTER);
        imageBox.setPrefHeight(130);
        VBox.setVgrow(imageBox, Priority.ALWAYS);

        HBox priceBox = new HBox();
        priceBox.setAlignment(Pos.BOTTOM_LEFT);

        Label brandLabel = new Label(product.getBrand());
        brandLabel.setTextFill(Color.web("#666666"));
        brandLabel.setFont(Font.font("Arial", 12));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label priceLabel = new Label("$" + String.format("%.2f", product.getPrice()));
        priceLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        priceLabel.setTextFill(Color.web("#333333"));

        priceBox.getChildren().addAll(brandLabel, spacer, priceLabel);
        card.getChildren().addAll(nameLabel, descLabel, imageBox, priceBox);
        card.setOnMouseClicked(e -> selectCard(card, product, true));

        return card;
    }

    private void selectCard(VBox card, Product product, boolean animate) {
        if (selectedCard != null) {
            selectedCard.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 10; -fx-border-color: transparent; -fx-border-width: 2; -fx-cursor: hand;");
        }

        selectedCard = card;
        selectedCard.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 10; -fx-border-color: #82b1ff; -fx-border-radius: 10; -fx-border-width: 2; -fx-cursor: hand;");

        if (!animate) {
            setMainData(product);
            return;
        }

        FadeTransition fadeOut = new FadeTransition(Duration.millis(150), leftPanel);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> {
            setMainData(product);
            FadeTransition fadeIn = new FadeTransition(Duration.millis(150), leftPanel);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });
        fadeOut.play();
    }

    private void setMainData(Product product) {
        mainImageView.setImage(loadProductImage(product.getImagePath()));
        mainNameLabel.setText(product.getName());
        mainPriceLabel.setText("$" + String.format("%.2f", product.getPrice()));
        mainBrandLabel.setText(product.getBrand());
        mainDescLabel.setText(product.getDescription());
    }

    private void clearMainData(String message) {
        mainImageView.setImage(null);
        mainNameLabel.setText(message);
        mainPriceLabel.setText("");
        mainBrandLabel.setText("");
        mainDescLabel.setText("");
    }

    private Image loadProductImage(String imagePath) {
        return new Image("file:resources/images/" + imagePath);
    }

    private void showError(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Lỗi");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
