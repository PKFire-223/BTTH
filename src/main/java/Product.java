public class Product {
    private int id;
    private String name;
    private String brand;
    private double price;
    private String imagePath;
    private String description;

    public Product(int id, String name, String brand, double price, String imagePath, String description) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.imagePath = imagePath;
        this.description = description;
    }

    public Product(String name, String brand, double price, String imagePath, String description) {
        this(0, name, brand, price, imagePath, description);
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getBrand() { return brand; }
    public double getPrice() { return price; }
    public String getImagePath() { return imagePath; }
    public String getDescription() { return description; }
}
