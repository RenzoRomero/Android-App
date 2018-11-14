package pe.com.smartvet.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Promotion {
    private String id;
    private String name;
    private String description;
    private String photo;
    private String startDate;
    private String endingDate;

    public Promotion() {
    }

    public Promotion(String id, String name, String description, String photo, String startDate, String endingDate) {
        this.setId(id);
        this.setName(name);
        this.setDescription(description);
        this.setPhoto(photo);
        this.setStartDate(startDate);
        this.setEndingDate(endingDate);
    }

    public String getId() {
        return id;
    }

    public Promotion setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Promotion setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Promotion setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getPhoto() {
        return photo;
    }

    public Promotion setPhoto(String photo) {
        this.photo = photo;
        return this;
    }

    public String getStartDate() {
        return startDate;
    }

    public Promotion setStartDate(String startDate) {
        this.startDate = startDate;
        return this;
    }

    public String getEndingDate() {
        return endingDate;
    }

    public Promotion setEndingDate(String endingDate) {
        this.endingDate = endingDate;
        return this;
    }

    public static Promotion build(JSONObject jsonPromotion) {
        if(jsonPromotion == null) return null;
        Promotion product = new Promotion();
        try {
            product.setId(jsonPromotion.getString("_id"));
            product.setName(jsonPromotion.getString("name"));
            product.setDescription(jsonPromotion.getString("description"));
            product.setPhoto(jsonPromotion.getString("photo"));
            product.setStartDate(jsonPromotion.getString("startDate"));
            product.setEndingDate(jsonPromotion.getString("endingDate"));
            return product;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Promotion> build(JSONArray jsonPromotions) {
        if(jsonPromotions == null) return null;
        int length = jsonPromotions.length();
        List<Promotion> products = new ArrayList<>();
        for(int i = 0; i < length; i++)
            try {
                products.add(Promotion.build(jsonPromotions.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return products;
    }
}
