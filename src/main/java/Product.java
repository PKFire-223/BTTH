public class Product {
    private String name;
    private String brand;
    private double price;
    private String imagePath;
    private String description;

    public Product(String name, String brand, double price, String imagePath, String description) {
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.imagePath = imagePath;
        this.description = description;
    }

    public String getName() { return name; }
    public String getBrand() { return brand; }
    public double getPrice() { return price; }
    public String getImagePath() { return imagePath; }
    public String getDescription() { return description; }
}