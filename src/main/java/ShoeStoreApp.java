import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class ShoeStoreApp extends Application {

    private ImageView mainImageView;
    private Label mainNameLabel;
    private Label mainPriceLabel;
    private Label mainBrandLabel;
    private Label mainDescLabel;
    private VBox leftPanel;
    private VBox selectedCard = null;

    @Override
    public void start(Stage primaryStage) {
        List<Product> products = generateMockData();

        // 1. Panel Bên Trái (Chi tiết sản phẩm)
        leftPanel = new VBox(8);
        leftPanel.setPadding(new Insets(30, 20, 20, 20));
        leftPanel.setPrefWidth(350);
        leftPanel.setMaxWidth(350);
        leftPanel.setAlignment(Pos.TOP_LEFT);
        leftPanel.setStyle("-fx-background-color: #ffffff;"); // Nền trắng

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

        // 2. Panel Bên Phải (Danh sách sản phẩm - Lưới 4 cột)
        TilePane gridPane = new TilePane();
        gridPane.setPadding(new Insets(30));
        gridPane.setHgap(15);
        gridPane.setVgap(15);
        gridPane.setPrefColumns(4);
        gridPane.setAlignment(Pos.TOP_LEFT);
        gridPane.setStyle("-fx-background-color: #f4f5f7;"); // Nền xám nhạt

        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            VBox card = createProductCard(product);
            gridPane.getChildren().add(card);
            
            if (i == 0) {
                selectCard(card, product, false);
            }
        }

        ScrollPane scrollPane = new ScrollPane(gridPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-control-inner-background: #f4f5f7;");
        scrollPane.setBorder(Border.EMPTY);

        // 3. Layout Tổng
        HBox root = new HBox(); // Loại bỏ khoảng cách HBox để viền tiếp xúc liền mạch
        root.setStyle("-fx-background-color: #f4f5f7;");
        root.getChildren().addAll(leftPanel, scrollPane);
        HBox.setHgrow(scrollPane, Priority.ALWAYS);

        // Tăng chiều rộng Scene lên 1400 để chứa đủ 4 cột (220px * 4 + gap + padding + scrollbar)
        Scene scene = new Scene(root, 1400, 750);
        primaryStage.setTitle("Shoe Store");
        primaryStage.setScene(scene);
        primaryStage.show();
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

        Label descLabel = new Label(product.getDescription());
        descLabel.setFont(Font.font("Arial", FontWeight.BOLD, 11));
        descLabel.setTextFill(Color.web("#a0a0a0"));
        descLabel.setMaxWidth(190);

        ImageView imageView = new ImageView(new Image("file:resources/images/" + product.getImagePath()));
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
        mainImageView.setImage(new Image("file:resources/images/" + product.getImagePath()));
        mainNameLabel.setText(product.getName());
        mainPriceLabel.setText("$" + String.format("%.2f", product.getPrice()));
        mainBrandLabel.setText(product.getBrand());
        mainDescLabel.setText(product.getDescription());
    }

    private List<Product> generateMockData() {
        List<Product> list = new ArrayList<>();
        list.add(new Product("4DFWD PULSE SHOES", "Adidas", 160.00, "img1.png", "This product is excluded from all promotional discounts and offers."));
        list.add(new Product("FORUM MID SHOES", "Adidas", 100.00, "img2.png", "This product is excluded from all promotional discounts and offers."));
        list.add(new Product("SUPERNOVA SHOES", "Adidas", 150.00, "img3.png", "NMD City Stock 2"));
        list.add(new Product("Adidas", "Adidas", 160.00, "img4.png", "NMD City Stock 2"));
        list.add(new Product("Adidas NMD", "Adidas", 120.00, "img5.png", "NMD City Stock 2"));
        list.add(new Product("4DFWD PULSE RED", "Adidas", 160.00, "img6.png", "This product is excluded from all promotional discounts and offers."));
        list.add(new Product("4DFWD PULSE GREEN", "Adidas", 160.00, "img1.png", "This product is excluded from all promotional discounts and offers."));
        list.add(new Product("FORUM MID BLUE", "Adidas", 100.00, "img2.png", "This product is excluded from all promotional discounts and offers."));
        return list;
    }

    public static void main(String[] args) {
        launch(args);
    }
}