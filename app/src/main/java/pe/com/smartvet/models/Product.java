package pe.com.smartvet.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Product {
    private String id;
    private String brand;
    private String name;
    private String description;
    private String photo;
    private Double price;
    private Integer quantity;

    public Product() {
    }

    public Product(String id, String brand, String name, String description, String photo, Double price, Integer quantity) {
        this.setId(id);
        this.setBrand(brand);
        this.setName(name);
        this.setDescription(description);
        this.setPhoto(photo);
        this.setPrice(price);
        this.setQuantity(quantity);
    }

    public String getId() {
        return id;
    }

    public Product setId(String id) {
        this.id = id;
        return this;
    }

    public String getBrand() {
        return brand;
    }

    public Product setBrand(String brand) {
        this.brand = brand;
        return this;
    }

    public String getName() {
        return name;
    }

    public Product setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Product setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getPhoto() {
        return photo;
    }

    public Product setPhoto(String photo) {
        this.photo = photo;
        return this;
    }

    public Double getPrice() {
        return price;
    }

    public Product setPrice(Double price) {
        this.price = price;
        return this;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Product setQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }
    
    public static Product build(JSONObject jsonProduct) {
        if(jsonProduct == null) return null;
        Product product = new Product();
        try {
            product.setId(jsonProduct.getString("_id"));
            product.setBrand(jsonProduct.getString("brand"));
            product.setName(jsonProduct.getString("name"));
            product.setDescription(jsonProduct.getString("description"));
            product.setPhoto(jsonProduct.getString("photo"));
            product.setPrice(Double.parseDouble(jsonProduct.getString("price")));
            product.setQuantity(Integer.parseInt(jsonProduct.getString("quantity")));
            return product;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Product> build(JSONArray jsonProducts) {
        if(jsonProducts == null) return null;
        int length = jsonProducts.length();
        List<Product> products = new ArrayList<>();
        for(int i = 0; i < length; i++)
            try {
                products.add(Product.build(jsonProducts.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return products;
    }
}
