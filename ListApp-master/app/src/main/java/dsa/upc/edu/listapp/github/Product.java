package dsa.upc.edu.listapp.github;

public class Product {
    public final String id;
    public final String name;
    public final double price;
    public final String image_url;
    public final int sales;

    public Product(String id, String name, double price, String avatar_url, int sales) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.sales = sales;
        this.image_url = avatar_url;
    }

    @Override
    public String toString() {
        return name+" ("+name+")";
    }
}